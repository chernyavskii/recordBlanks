package com.validator;

import com.errors.Error;
import com.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class DocumentValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        List<Product> productList = (List<Product>)o;

        for(Product product : productList) {
            if(product.getName().equals("")){
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
            }
            if(product.getPrice() == 0){
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
            }
            if(product.getNumber() == 0){
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "number", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
            }
            if(product.getMeasure().equals("")){
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "measure", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
            }
            if(product.getNote().equals("")){
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "note", Error.EMPTY_FIELD_STATUS, Error.EMPTY_FIELD_MESSAGE);
            }
        }
    }
}
