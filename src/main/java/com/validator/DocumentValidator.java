package com.validator;

import com.errors.Error;
import com.model.RequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.validation.Valid;

@Component
public class DocumentValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        RequestWrapper requestWrapper = (RequestWrapper)o;

        ValidationUtils.rejectIfEmptyOrWhitespace( errors,"name", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace( errors,"document", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);



    }
}
