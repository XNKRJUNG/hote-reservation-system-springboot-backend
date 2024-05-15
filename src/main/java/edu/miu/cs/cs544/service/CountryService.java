package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Country;
import edu.miu.cs.cs544.repository.CountryRepository;
import edu.miu.cs.cs544.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService {

    Country addCountry(Country country) throws Exception;

    Country updateCountry(String countryCode, Country country) throws Exception;

    Country getCountryById(String countryCode);

    List<Country> getAllCountry();

}
