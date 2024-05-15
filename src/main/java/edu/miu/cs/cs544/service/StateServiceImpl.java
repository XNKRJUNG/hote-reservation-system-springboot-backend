package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Country;
import edu.miu.cs.cs544.domain.State;
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
public class StateServiceImpl implements StateService {

    @Autowired
    StateRepository stateRepository;

    @Autowired
    Validation validation;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    UserService userService;

    @Override
    public State addState(State state) throws Exception {
        validation.isEntityValid(state);
        if (stateRepository.findByCode(state.getCode()) != null) {
            throw new AppException("State Already exist");
        }
        if (countryRepository.findByCode(state.getCountry().getCode()) == null) {
            throw new AppException("Country code is not available on database");
        }
        state.getAuditData().setCreatedBy(userService.getLoggedInUser().getUser().getUserName());
        state.getAuditData().setUpdatedBy(userService.getLoggedInUser().getUser().getUserName());
        state.getAuditData().setUpdatedOn(LocalDateTime.now());
        state.getAuditData().setCreatedOn(LocalDateTime.now());
        return stateRepository.save(state);
    }

    @Override
    public State updateState(Long stateId, State state) throws Exception {
        validation.isEntityValid(state);
        Optional<State> response = stateRepository.findById(stateId);
        if (response.isPresent()) {
            response.get().setName(state.getName());
            response.get().setCode(state.getCode());
            response.get().setCountry(state.getCountry());
            response.get().getAuditData().setUpdatedOn(LocalDateTime.now());
            response.get().getAuditData().setUpdatedBy(userService.getLoggedInUser().getUser().getUserName());
            stateRepository.save(response.get());
            return response.get();
        }
        return null;
    }

    @Override
    public State getStateById(Long stateId) {
        return stateRepository.findById(stateId).get();
    }

    @Override
    public List<State> getAllState() {
        return stateRepository.findAll();
    }
}
