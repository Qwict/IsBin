package com.qwict.isbin.validator;

import com.qwict.isbin.dto.BookDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BookDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDto book = (BookDto) target;

//        if (book.getAuthorDtos().get(0).getFirstName() != null && ){
//            errors.reject("authors.empty","Primary author must have a first name.");
        }
}
