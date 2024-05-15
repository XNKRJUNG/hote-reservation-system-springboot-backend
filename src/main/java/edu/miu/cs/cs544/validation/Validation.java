package edu.miu.cs.cs544.validation;


import edu.miu.cs.cs544.exception.UsernameExistsException;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Validation {

    @Autowired
    private LocalValidatorFactoryBean validator;


    public <T> boolean isEntityValid(T t) throws Exception {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new UsernameExistsException("Entity validation failed: " + violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));
        }
        return true;
    }
}
