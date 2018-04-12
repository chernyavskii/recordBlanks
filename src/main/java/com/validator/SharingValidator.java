package com.validator;

import com.dao.AgentDAO;
import com.dao.UserDAO;
import com.errors.Error;
import com.model.RequestWrapper;
import com.service.agent.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SharingValidator implements Validator {

    @Autowired
    private AgentService agentService;

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
/*        String unp = agentDAO.findOne(agent_id).getUnp();*/

        if(agentService.getAgentById(SecurityContextHolder.getContext().getAuthentication().getName(), agent_id) == null) {
            errors.rejectValue("agent_id", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        }

    }
}
