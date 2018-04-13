package com.validator;

import com.errors.Error;
import com.model.Agent;
import com.model.RequestWrapper;
import com.service.agent.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AgentValidator implements Validator {

    @Autowired
    private AgentService agentService;

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
        Agent agent = requestWrapper.getAgent();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"firstName",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"middleName",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"lastName",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"unp",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"organization",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"position",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"address",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"rs",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"ks",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"bank",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"bik",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"phone",  Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);

        if(method.equals("post")) {
            if(agentService.checkUnp(SecurityContextHolder.getContext().getAuthentication().getName(), agent, "post").booleanValue()) {
                errors.rejectValue("unp", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(agentService.checkRs(SecurityContextHolder.getContext().getAuthentication().getName(), agent, "post").booleanValue()) {
                errors.rejectValue("rs", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(agentService.checkKs(SecurityContextHolder.getContext().getAuthentication().getName(), agent, "post").booleanValue()) {
                errors.rejectValue("ks", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(agentService.checkBik(SecurityContextHolder.getContext().getAuthentication().getName(), agent, "post").booleanValue()) {
                errors.rejectValue("bik", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
        }

        if(method.equals("update")) {
            if(agentService.checkUnp(SecurityContextHolder.getContext().getAuthentication().getName(), agent, "update").booleanValue()) {
                errors.rejectValue("unp", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(agentService.checkRs(SecurityContextHolder.getContext().getAuthentication().getName(), agent, "update").booleanValue()) {
                errors.rejectValue("rs", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(agentService.checkKs(SecurityContextHolder.getContext().getAuthentication().getName(), agent, "update").booleanValue()) {
                errors.rejectValue("ks", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
            if(agentService.checkBik(SecurityContextHolder.getContext().getAuthentication().getName(), agent, "update").booleanValue()) {
                errors.rejectValue("bik", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            }
        }

        if(agent.getUnp().length() != 9){
            errors.rejectValue("unp", Error.UNP_BIK_LENGTH_STATUS, Error.UNP_BIK_LENGTH_MESSAGE);
        }

        if(!agent.getUnp().matches("\\d+") || agent.getUnp().length() != 9){
            errors.rejectValue("unp", Error.UNP_BIK_LENGTH_STATUS, Error.UNP_BIK_LENGTH_MESSAGE);
        }

        if(!agent.getRs().matches("\\d+") || agent.getRs().length() != 20){
            errors.rejectValue("rs", Error.RS_KS_LENGTH_STATUS, Error.RS_KS_LENGTH_MESSAGE);
        }

        if(!agent.getKs().matches("\\d+") || agent.getKs().length() != 20){
            errors.rejectValue("ks", Error.RS_KS_LENGTH_STATUS, Error.RS_KS_LENGTH_MESSAGE);
        }

        if(!agent.getBik().matches("\\d+") || agent.getBik().length() != 9){
            errors.rejectValue("bik", Error.UNP_BIK_LENGTH_STATUS, Error.UNP_BIK_LENGTH_MESSAGE);
        }

        if(!agent.getPhone().matches("(\\+375 (25|29|33|44) ([0-9]{3}( [0-9]{2}){2}))")){
            errors.rejectValue("phone", Error.PHONE_INCORRECT_STATUS, Error.PHONE_INCORRECT_MESSAGE);
        }

        if(!agent.getLastName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("lastName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }

        if(!agent.getFirstName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("firstName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }

        if(!agent.getMiddleName().matches("[А-ЯЁ][а-яё]+([-'][А-ЯЁа-яё]+)?")){
            errors.rejectValue("middleName", Error.FIO_INCORRECT_STATUS, Error.FIO_INCORRECT_MESSAGE);
        }
    }
}
