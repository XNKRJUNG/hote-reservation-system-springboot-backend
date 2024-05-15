package edu.miu.cs.cs544.security;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.Token;
import edu.miu.cs.cs544.domain.UserType;
import edu.miu.cs.cs544.dto.request.UserDTO;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import edu.miu.cs.cs544.repository.TokenRepository;
import edu.miu.cs.cs544.service.CustomerMapper;
import edu.miu.cs.cs544.service.CustomerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenService implements Serializable {

    @Value("${jwt.token.validity}")
    private long JWT_TOKEN_VALIDITY;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    CustomerService customerService;

    @Autowired
    TokenRepository tokenRepository;

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean isTokenInDatabase(String token, CustomerResponseDTO customerResponseDTO) {
        Token retrivedToken = tokenRepository.findByCustomer_id(customerResponseDTO.getId());
        return retrivedToken != null && retrivedToken.getToken().equals(token);
    }

    public boolean isTokenValid(String token, CustomerResponseDTO customerResponseDTO) {
        return isTokenInDatabase(token, customerResponseDTO) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    private Date extractExpiration(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public int getUserNameAndPasswordFromToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        String tokenWithoutBearer = token.substring(7);
        return Integer.parseInt(getClaimFromToken(tokenWithoutBearer, Claims::getSubject));
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String generateToken(CustomerResponseDTO customerResponseDTO) {
        return generateToken(new HashMap<>(), customerResponseDTO);
    }


    public String generateToken(Map<String, Object> claims, CustomerResponseDTO userDTO) {

        String generatedToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userDTO.getUser().getUserName()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) //* 1000 to turn millis into seconds
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        Token token = tokenRepository.findByCustomer_id(userDTO.getId());
        if (token != null) {
            token.setToken(generatedToken);
            return tokenRepository.save(token).getToken();
        } else {
            token = new Token();
            Customer customer = CustomerMapper.INSTANCE.mapToEntity(userDTO);
            token.setToken(generatedToken);
            token.setCustomer(customer);
            return tokenRepository.save(token).getToken();
        }
    }

    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomerResponseDTO customerResponseDTO = customerService.loadUserByUsername(username);
            if (customerResponseDTO != null && isTokenValid(token, customerResponseDTO)) {
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + customerResponseDTO.getUser().getUserType().name())); // Add the "ROLE_" prefix if using Spring Security
                return new UsernamePasswordAuthenticationToken(customerResponseDTO, null, authorities);
            } else {
                return null;
            }
        }
        return null;
    }

    @Transactional
    public boolean deleteToken(Long customerID) {
        System.out.println("customerID" + customerID);
        return tokenRepository.deleteByCustomer_id(customerID) > 0;
    }
}
