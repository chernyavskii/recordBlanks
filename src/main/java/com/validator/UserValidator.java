package com.validator;

import com.errors.Error;
import com.model.RequestWrapper;
import com.model.User;
import com.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    private String method;

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RequestWrapper.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RequestWrapper requestWrapper = (RequestWrapper) o;
        User user = requestWrapper.getUser();
        Long user_id = requestWrapper.getUser_id();
        String role = requestWrapper.getRole();
        user.setId(user_id);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.username",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.address", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.firstName", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.middleName", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.lastName", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.unp", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.organization", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.position", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.rs", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.ks", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.bank", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.bik", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"user.phone", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"role", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);

        if(method.equals("post")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.password", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
            if(user.getPassword().length() < 8){
                errors.rejectValue("user.password", Error.PASSWORD_LENGTH_STATUS, Error.PASSWORD_LENGTH_MESSAGE);
            }
            if(userService.checkUsername(user, "post").booleanValue()) {
                errors.rejectValue("user.username", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkUnp(user, "post").booleanValue()) {
                errors.rejectValue("user.unp", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkRs(user, "post").booleanValue()) {
                errors.rejectValue("user.rs", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkKs(user, "post").booleanValue()) {
                errors.rejectValue("user.ks", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkBik(user, "post").booleanValue()) {
                errors.rejectValue("user.bik", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(!("ROLE_ADMIN".equals(role) || "ROLE_USER".equals(role))) {
                errors.rejectValue("role", Error.WRONG_ROLE_STATUS, Error.WRONG_ROLE_MESSAGE);
            }
        }

        if(method.equals("update")) {
            if(userService.checkUsername(user, "update").booleanValue()) {
                errors.rejectValue("user.username", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkUnp(user, "update").booleanValue()) {
                errors.rejectValue("user.unp", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkRs(user, "update").booleanValue()) {
                errors.rejectValue("user.rs", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkKs(user, "update").booleanValue()) {
                errors.rejectValue("user.ks", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkBik(user, "update").booleanValue()) {
                errors.rejectValue("user.bik", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(!("ROLE_ADMIN".equals(role) || "ROLE_USER".equals(role))) {
                errors.rejectValue("role", Error.WRONG_ROLE_STATUS, Error.WRONG_ROLE_MESSAGE);
            }
        }

        if(user.getUsername().length() < 5){
            errors.rejectValue("user.username", Error.USERNAME_LENGTH_STATUS, Error.USERNAME_LENGTH_MESSAGE);
        }
        if(!user.getLastName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("user.lastName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }
        if(!user.getFirstName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("user.firstName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }
        if(!user.getMiddleName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("user.middleName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }
        if(!user.getUnp().matches("\\d+") || user.getUnp().length() != 9){
            errors.rejectValue("user.unp", Error.UNP_BIK_LENGTH_STATUS, Error.UNP_BIK_LENGTH_MESSAGE);
        }
        if(!user.getRs().matches("\\d+") || user.getRs().length() != 20){
            errors.rejectValue("user.rs", Error.RS_KS_LENGTH_STATUS, Error.RS_KS_LENGTH_MESSAGE);
        }
        if(!user.getKs().matches("\\d+") || user.getKs().length() != 20){
            errors.rejectValue("user.ks", Error.RS_KS_LENGTH_STATUS, Error.RS_KS_LENGTH_MESSAGE);
        }
        if(!user.getBik().matches("\\d+") || user.getBik().length() != 9){
            errors.rejectValue("user.bik", Error.UNP_BIK_LENGTH_STATUS, Error.UNP_BIK_LENGTH_MESSAGE);
        }
        if(!user.getPhone().matches("(\\+375 (25|29|33|44) ([0-9]{3}( [0-9]{2}){2}))")){
            errors.rejectValue("user.phone", Error.PHONE_INCORRECT_STATUS, Error.PHONE_INCORRECT_MESSAGE);
        }
    }
}
