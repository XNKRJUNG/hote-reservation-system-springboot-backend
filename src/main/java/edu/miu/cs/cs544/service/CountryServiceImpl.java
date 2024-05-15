package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Country;
import edu.miu.cs.cs544.exception.AppException;
import edu.miu.cs.cs544.repository.CountryRepository;
import edu.miu.cs.cs544.repository.StateRepository;
import edu.miu.cs.cs544.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    Validation validation;

    @Autowired
    private UserService userService;
    @Autowired
    private StateRepository stateRepository;


    @Override
    public Country addCountry(Country country) throws Exception {
        validation.isEntityValid(country);
        if (getCountryById(country.getCode()) != null) {
            throw new AppException("Country Already exist");
        }
        country.getAuditData().setCreatedBy(userService.getLoggedInUser().getUser().getUserName());
        country.getAuditData().setUpdatedBy(userService.getLoggedInUser().getUser().getUserName());
        country.getAuditData().setUpdatedOn(LocalDateTime.now());
        country.getAuditData().setCreatedOn(LocalDateTime.now());
        return countryRepository.save(country);

    }

    @Override
    public Country updateCountry(String countryCode, Country country) throws Exception {
        validation.isEntityValid(country);
        Country response = countryRepository.findByCode(countryCode);
        if (response != null) {
            response.setName(country.getName());
            response.setPopulation(country.getPopulation());
            response.getAuditData().setUpdatedOn(LocalDateTime.now());
            response.getAuditData().setUpdatedBy(userService.getLoggedInUser().getUser().getUserName());
            return response;
        }
        return null;
    }

    @Override
    public Country getCountryById(String countryCode) {
        return countryRepository.findByCode(countryCode);
    }

    @Override
    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }





}
