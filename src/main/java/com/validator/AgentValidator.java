package com.validator;

import com.errors.Error;
import com.model.Agent;
import com.service.agent.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AgentValidator implements Validator {

    @Autowired
    private AgentService agentService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Agent.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Agent agent = (Agent)o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"firstName",  Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"middleName",  Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"lastName",  Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"unp",  Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"organization",  Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"position",  Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"address",  Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);


    }
}
