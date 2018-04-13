package com.validator;

import com.dao.AgentDAO;
import com.dao.DocumentDAO;
import com.dao.UserDAO;
import com.errors.Error;
import com.model.RequestWrapper;
import com.service.agent.AgentService;
import com.service.document.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.io.IOException;

@Component
public class SharingValidator implements Validator {

    @Autowired
    private AgentService agentService;

    @Autowired
    private DocumentDAO documentDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public boolean supports(Class<?> aClass) {
        return RequestWrapper.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RequestWrapper requestWrapper = (RequestWrapper) o;
        Long agent_id = requestWrapper.getAgent_id();
        Long document_id = requestWrapper.getDocument_id();
        String unp = "";

        if(agentService.getAgentById(SecurityContextHolder.getContext().getAuthentication().getName(), agent_id) == null) {
            errors.rejectValue("agent_id", Error.ENTITY_NOT_FOUND_STATUS, Error.ENTITY_NOT_FOUND_MESSAGE);
        }
        else {
            unp = agentService.getAgentById(SecurityContextHolder.getContext().getAuthentication().getName(), agent_id).getUnp();
        }

        if(userDAO.findByUnp(unp) == null) {
            errors.rejectValue("agent_id", Error.USER_IS_NOT_REGISTERED_STATUS, Error.USER_IS_NOT_REGISTERED_MESSAGE);
        }

        if(documentDAO.findOne(document_id) == null) {
            errors.rejectValue("document_id", Error.ENTITY_NOT_FOUND_STATUS, Error.ENTITY_NOT_FOUND_MESSAGE);
        }
    }
}
