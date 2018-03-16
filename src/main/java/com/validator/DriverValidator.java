package com.validator;

import com.errors.Error;
import com.model.Driver;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DriverValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Driver.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Driver driver = (Driver)o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"firstName",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"middleName",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"lastName",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"carModel",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"carNumber",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);

        if(!driver.getLastName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("lastName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }

        if(!driver.getFirstName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("firstName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }

        if(!driver.getMiddleName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("middleName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }
    }
}
