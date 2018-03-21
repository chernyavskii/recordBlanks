package com.validator;

import com.errors.Error;
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
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"username",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"address", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"firstName", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"middleName", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"lastName", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"unp", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"organization", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"position", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"rs", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"ks", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"bank", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"bik", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"phone", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);

        if(method.equals("post")) {
            if(userService.checkUnp(user, "post").booleanValue()) {
                errors.rejectValue("unp", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkRs(user, "post").booleanValue()) {
                errors.rejectValue("rs", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkKs(user, "post").booleanValue()) {
                errors.rejectValue("ks", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkBik(user, "post").booleanValue()) {
                errors.rejectValue("bik", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
        }

        if(method.equals("update")) {
            if(userService.checkUnp(user, "update").booleanValue()) {
                errors.rejectValue("unp", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkRs(user, "update").booleanValue()) {
                errors.rejectValue("rs", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkKs(user, "update").booleanValue()) {
                errors.rejectValue("ks", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(userService.checkBik(user, "update").booleanValue()) {
                errors.rejectValue("bik", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
        }

        if(userService.findByUsername(user.getUsername()) != null){
            errors.rejectValue("username", Error.DUPLICATED_ENTITY_STATUS,Error.DUPLICATED_ENTITY_MESSAGE);
        }
        if(user.getPassword().length() < 8){
            errors.rejectValue("password", Error.PASSWORD_LENGTH_STATUS, Error.PASSWORD_LENGTH_MESSAGE);
        }
        if(user.getUsername().length() < 5){
            errors.rejectValue("username", Error.USERNAME_LENGTH_STATUS, Error.USERNAME_LENGTH_MESSAGE);
        }
        if(!user.getLastName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("lastName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }
        if(!user.getFirstName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("firstName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }
        if(!user.getMiddleName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("middleName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }
        if(user.getUnp().length() != 9){
            errors.rejectValue("unp", Error.UNP_BIK_LENGTH_STATUS, Error.UNP_BIK_LENGTH_MESSAGE);
        }
        if(!user.getUnp().matches("\\d+") || user.getUnp().length() != 9){
            errors.rejectValue("unp", Error.UNP_BIK_LENGTH_STATUS, Error.UNP_BIK_LENGTH_MESSAGE);
        }
        if(!user.getRs().matches("\\d+") || user.getRs().length() != 20){
            errors.rejectValue("rs", Error.RS_KS_LENGTH_STATUS, Error.RS_KS_LENGTH_MESSAGE);
        }
        if(!user.getKs().matches("\\d+") || user.getKs().length() != 20){
            errors.rejectValue("ks", Error.RS_KS_LENGTH_STATUS, Error.RS_KS_LENGTH_MESSAGE);
        }
        if(!user.getBik().matches("\\d+") || user.getBik().length() != 9){
            errors.rejectValue("bik", Error.UNP_BIK_LENGTH_STATUS, Error.UNP_BIK_LENGTH_MESSAGE);
        }
        if(!user.getPhone().matches("(\\+375 (25|29|33|44) ([0-9]{3}( [0-9]{2}){2}))")){
            errors.rejectValue("phone", Error.PHONE_INCORRECT_STATUS, Error.PHONE_INCORRECT_MESSAGE);
        }
    }
}
