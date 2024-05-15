package edu.miu.cs.cs544.aop;

import edu.miu.cs.cs544.dto.response.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Slf4j
@Component
public class Log {
    private final Logger logger = LoggerFactory.getLogger(Log.class);

    @AfterReturning(
            pointcut = "execution(* edu.miu.cs.cs544.*.AuthenticationServiceImpl.login(..))",
            returning = "result")
    public void logSuccessfulAuthentication(JoinPoint joinPoint, Object result) {
        ResponseDTO responseDTO = (ResponseDTO) result;
        logger.info("login response: '{}' ", responseDTO);
    }

    @AfterReturning(
            pointcut = "execution(* edu.miu.cs.cs544.*.AuthenticationServiceImpl.register(..))",
            returning = "result")
    public void registerSuccessfulAuthentication(JoinPoint joinPoint, Object result) {
        ResponseDTO responseDTO = (ResponseDTO) result;
        logger.info("User registration response '{}': ", responseDTO);
    }

    @AfterReturning(
            pointcut = "execution(* edu.miu.cs.cs544.*.AuthenticationServiceImpl.registerAdmin(..))",
            returning = "result")
    public void adminRegisterSuccessfulAuthentication(JoinPoint joinPoint, Object result) {
        ResponseDTO responseDTO = (ResponseDTO) result;
        logger.info("Admin registration response '{}': ", responseDTO);
    }

    @AfterReturning(
            pointcut = "execution(* edu.miu.cs.cs544.*.AuthenticationServiceImpl.logout(..))",
            returning = "result")
    public void logoutSuccessful(JoinPoint joinPoint, Object result) {
        boolean responseDTO = (Boolean) result;
        logger.info("logout response '{}': ", (responseDTO) ? "successfully logged out" : "Not logged out");
    }


//    @After("execution(* edu.miu.cs.cs544.*.JwtRequestFilter.doFilterInternal(..))")
//    public void securityFilter(JoinPoint joinPoint) {
//        logger.info("Authenticating user based on token");
//    }


    @Around("execution(* edu.miu.cs.cs544.service.ReservationServiceImpl.*(..))")
    public Object aroundAdd(ProceedingJoinPoint joinpoint) throws Throwable {
        log.info(joinpoint.getSignature().toString() + " method execution start");
        Instant start = Instant.now();
        Object returnObj = joinpoint.proceed();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time took to execute " + joinpoint.getSignature().toString() + " method is : "+timeElapsed);
        log.info(joinpoint.getSignature().toString() + " method execution end");
        return returnObj;
    }
    @AfterThrowing(value = "execution(* edu.miu.cs.cs544.service.ReservationServiceImpl.*(..))",throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error(joinPoint.getSignature()+ " An exception happened due to : "+ex.getMessage());
    }


    @Around("execution(* edu.miu.cs.cs544.service.CountryServiceImpl.*(..))")
    public Object aroundCustomer(ProceedingJoinPoint joinpoint) throws Throwable {
        log.info(joinpoint.getSignature().toString() + " method execution start");
        Instant start = Instant.now();
        Object returnObj = joinpoint.proceed();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time took to execute " + joinpoint.getSignature().toString() + " method is : "+timeElapsed);
        log.info(joinpoint.getSignature().toString() + " method execution end");
        return returnObj;
    }
    @AfterThrowing(value = "execution(* edu.miu.cs.cs544.service.CountryServiceImpl.*(..))",throwing = "ex")
    public void logCustomerException(JoinPoint joinPoint, Exception ex) {
        log.error(joinPoint.getSignature()+ " An exception happened due to : "+ex.getMessage());
    }
    @Around("execution(* edu.miu.cs.cs544.service.ProductServiceImpl.*(..))")
    public Object aroundProduct(ProceedingJoinPoint joinpoint) throws Throwable {
        log.info(joinpoint.getSignature().toString() + " method execution start");
        Instant start = Instant.now();
        Object returnObj = joinpoint.proceed();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time took to execute " + joinpoint.getSignature().toString() + " method is : "+timeElapsed);
        log.info(joinpoint.getSignature().toString() + " method execution end");
        return returnObj;
    }
    @AfterThrowing(value = "execution(* edu.miu.cs.cs544.service.ProductServiceImpl.*(..))",throwing = "ex")
    public void logProductException(JoinPoint joinPoint, Exception ex) {
        log.error(joinPoint.getSignature()+ " An exception happened due to : "+ex.getMessage());
    }
    @Around("execution(* edu.miu.cs.cs544.service.StateServiceImpl.*(..))")
    public Object aroundState(ProceedingJoinPoint joinpoint) throws Throwable {
        log.info(joinpoint.getSignature().toString() + " method execution start");
        Instant start = Instant.now();
        Object returnObj = joinpoint.proceed();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time took to execute " + joinpoint.getSignature().toString() + " method is : "+timeElapsed);
        log.info(joinpoint.getSignature().toString() + " method execution end");
        return returnObj;
    }
    @AfterThrowing(value = "execution(* edu.miu.cs.cs544.service.StateServiceImpl.*(..))",throwing = "ex")
    public void logStateException(JoinPoint joinPoint, Exception ex) {
        log.error(joinPoint.getSignature()+ " An exception happened due to : "+ex.getMessage());
    }
    @Around("execution(* edu.miu.cs.cs544.service.UserServiceImpl.*(..))")
    public Object aroundUser(ProceedingJoinPoint joinpoint) throws Throwable {
        log.info(joinpoint.getSignature().toString() + " method execution start");
        Instant start = Instant.now();
        Object returnObj = joinpoint.proceed();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time took to execute " + joinpoint.getSignature().toString() + " method is : "+timeElapsed);
        log.info(joinpoint.getSignature().toString() + " method execution end");
        return returnObj;
    }
    @AfterThrowing(value = "execution(* edu.miu.cs.cs544.service.UserServiceImpl.*(..))",throwing = "ex")
    public void logUserException(JoinPoint joinPoint, Exception ex) {
        log.error(joinPoint.getSignature()+ " An exception happened due to : "+ex.getMessage());
    }
}
