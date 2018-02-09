package com.validator;

import com.errors.Error;
import com.model.Document;
import com.model.Product;
import com.model.RequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DocumentValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
       Product p = (Product)o;

/*//        List<Product> productList = p.getProducts();
        Iterator<Product> productIterator = productList.iterator();
        for(Product product : productList){
            errors.rejectValue("name", Error.DUPLICATED_ENTITY_STATUS, Error.DUPLICATED_ENTITY_MESSAGE);
            ValidationUtils.rejectIfEmptyOrWhitespace( errors,product.getName(), Error.EMPTY_FIElD_STATUS, Error.EMPTY_FIElD_MESSAGE);
        }*/
    }
}
