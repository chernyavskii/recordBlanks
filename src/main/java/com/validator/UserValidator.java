package com.validator;

import com.errors.Error;
import com.model.User;
import com.service.user.UserService;
import org.apache.xmlbeans.impl.validator.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"username",  Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"address", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"firstName", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"middleName", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"lastName", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"unp", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"organization", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"position", Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);

        if(userService.findByUsername(user.getUsername()) != null){
            errors.rejectValue("username", Error.DUPLICATED_ENTITY_STATUS,Error.DUPLICATED_ENTITY_MESSAGE);
        }
        ///ВСЕ BAD REQUEST



      /*  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Required");
        if (user.getUsername().length() < 8 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }*/
    }
}
