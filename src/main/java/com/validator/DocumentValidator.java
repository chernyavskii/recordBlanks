package com.validator;

import com.errors.Error;
import com.model.Product;
import com.model.RequestWrapper;
import com.model.Work;
import com.service.agent.AgentService;
import com.service.driver.DriverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class DocumentValidator implements Validator {

    @Autowired
    private AgentService agentService;

    @Autowired
    private DriverService driverService;

    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RequestWrapper.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RequestWrapper requestWrapper = (RequestWrapper) o;
        String documentName = requestWrapper.getDocumentName();
        Long agent_id = requestWrapper.getAgent_id();
        Long driver_id = requestWrapper.getDriver_id();
        List<Product> productList = requestWrapper.getProducts();
        List<Work> workList = requestWrapper.getWorks();
        if(documentName.equals("")) {
            errors.rejectValue("documentName", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
        }
        if(type.equals("tn")) {
            if(agentService.getAgentById(SecurityContextHolder.getContext().getAuthentication().getName(), agent_id) == null) {
                errors.rejectValue("agent_id", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
            }
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getName().length() == 0) {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "products[" + i + "].name", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
                }
                if (productList.get(i).getMeasure().length() == 0) {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "products[" + i + "].measure", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
                }
                if (productList.get(i).getNumber() == 0) {
                    errors.rejectValue("products[" + i + "].number", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
                if (productList.get(i).getPrice() == 0) {
                    errors.rejectValue("products[" + i + "].price", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
            }
        }
        if(type.equals("ttn")) {
            if(agentService.getAgentById(SecurityContextHolder.getContext().getAuthentication().getName(), agent_id) == null) {
                errors.rejectValue("agent_id", Error.ENTITY_NOT_FOUND_STATUS, Error.ENTITY_NOT_FOUND_MESSAGE);
            }
            if(driverService.getDriverById(SecurityContextHolder.getContext().getAuthentication().getName(), driver_id) == null) {
                errors.rejectValue("driver_id", Error.ENTITY_NOT_FOUND_STATUS, Error.ENTITY_NOT_FOUND_MESSAGE);
            }
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getName().length() == 0) {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "products[" + i + "].name", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
                }
                if (productList.get(i).getMeasure().length() == 0) {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "products[" + i + "].measure", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
                }
                if (productList.get(i).getNumber() == 0) {
                    errors.rejectValue("products[" + i + "].number", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
                if (productList.get(i).getPrice() == 0) {
                    errors.rejectValue("products[" + i + "].price", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
                if (productList.get(i).getPackageNumber() == 0) {
                    errors.rejectValue("products[" + i + "].packageNumber", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
                if (productList.get(i).getWeight() == 0) {
                    errors.rejectValue("products[" + i + "].weight", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
            }
        }
        if(type.equals("aspr")) {
            if(agentService.getAgentById(SecurityContextHolder.getContext().getAuthentication().getName(), agent_id) == null) {
                errors.rejectValue("agent_id", Error.ENTITY_NOT_FOUND_STATUS, Error.ENTITY_NOT_FOUND_MESSAGE);
            }
            for (int i = 0; i < workList.size(); i++) {
                if (workList.get(i).getName().length() == 0) {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "works[" + i + "].name", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
                }
                if (workList.get(i).getPrice() == 0) {
                    errors.rejectValue("works[" + i + "].price", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
            }
        }
        if(type.equals("sf")) {
            if(agentService.getAgentById(SecurityContextHolder.getContext().getAuthentication().getName(), agent_id) == null) {
                errors.rejectValue("agent_id", Error.ENTITY_NOT_FOUND_STATUS, Error.ENTITY_NOT_FOUND_MESSAGE);
            }
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getName().length() == 0) {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "products[" + i + "].name", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
                }
                if (productList.get(i).getMeasure().length() == 0) {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "products[" + i + "].measure", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
                }
                if (productList.get(i).getNumber() == 0) {
                    errors.rejectValue("products[" + i + "].number", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
                if (productList.get(i).getPrice() == 0) {
                    errors.rejectValue("products[" + i + "].price", Error.FIELD_INCORRECT_STATUS, Error.FIELD_INCORRECT_MESSAGE);
                }
            }
        }
    }
}
