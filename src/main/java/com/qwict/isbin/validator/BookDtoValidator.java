package com.qwict.isbin.validator;

import com.qwict.isbin.domein.Formatter;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.repository.BookRepository;
import com.qwict.isbin.service.BookService;
import com.qwict.isbin.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;


public class BookDtoValidator implements Validator {
    @Autowired
    private BookService bookService;
    @Autowired
    private LocationService locationService;
    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDto book = (BookDto) target;

        if (book.getIsbn().isEmpty()) {
            errors.rejectValue("isbn", "isbn.empty", "ISBN cannot be empty");
        } else {

//            @Pattern(
//                    regexp = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$",
//                    message = "ISBN13 should be valid"
//            )
            String formattedIsbn = Formatter.formatISBNToString(book.getIsbn());
            if (bookService.findBookByIsbn(formattedIsbn) != null) {
                errors.rejectValue("isbn", "isbn.unique", "This ISBN is already in the database; edit the book instead.");
            }
            if (formattedIsbn.length() != 13) {
                errors.rejectValue("isbn", "isbn.length", "ISBN must be exactly 13 characters long");
            } else if (!formattedIsbn.matches("^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$")) {
                errors.rejectValue("isbn", "isbn.valid", "This ISBN is not valid");
            }
        }
        AuthorDto primaryAuthor = book.getAuthorDtos().get(0);
        if (primaryAuthor.getFirstName().isBlank() || primaryAuthor.getLastName().isBlank()) {
            errors.rejectValue("authorDtos[0]", "author.firstAndLast", "The primary author must have a first and last name.");
        }

        LocationDto primaryLocation = book.getLocationDtos().get(0);
        if (primaryLocation.getName().isBlank()) {
            errors.rejectValue("locationDtos[0]", "location.empty", "A book needs at least one location.");
        } else {
            if (book.getLocationDtos().get(0).getPlaceCode1()==0 || book.getLocationDtos().get(0).getPlaceCode2()==0 || book.getLocationDtos().get(0).getName().isBlank()) {
                errors.rejectValue("locationDtos[0]", "location.primaryEmpty", "Enter a valid place name and place codes.");
            }
            for (LocationDto location : book.getLocationDtos()) {
                if (!(location.getPlaceCode1()==0 || location.getPlaceCode2()==0 || location.getName().isBlank())) {
                    if (location.getPlaceCode1()==0 || location.getPlaceCode2()==0 || location.getName().isBlank()) {
                        errors.rejectValue(
                                String.format("locationDtos[%d]", book.getLocationDtos().indexOf(location)),
                                "location.primaryEmpty", "Enter a valid place name and place codes.");
                    } if (locationService.locationAlreadyExists(location.getName(), location.getPlaceCode1(), location.getPlaceCode2())) {
                        errors.rejectValue(String.format("locationDtos[%d]", book.getLocationDtos().indexOf(location)), "location.unique", "This location already exists in the database");
                    } if (location.getPlaceCode1() < 50 || location.getPlaceCode2() < 50) {
                        errors.rejectValue(String.format("locationDtos[%d]", book.getLocationDtos().indexOf(location)), "location.code", "The place codes must be greater than 50");
                    } if (location.getPlaceCode1() > 300 || location.getPlaceCode2() > 300) {
                        errors.rejectValue(String.format("locationDtos[%d]", book.getLocationDtos().indexOf(location)), "location.code", "The place codes cannot be greater than 300");
                    }

                    int result = Math.abs(location.getPlaceCode1() - location.getPlaceCode2());
                    if (result < 50) {
                        errors.rejectValue(String.format("locationDtos[%d]", book.getLocationDtos().indexOf(location)), "location.distance", "The distance between the 2 place codes must be at least 50");
                    }
                    if (!location.getName().matches("[A-Za-z]+")) {
                        errors.rejectValue(String.format("locationDtos[%d]", book.getLocationDtos().indexOf(location)), "location.name", "The place name must consist of letters only");
                    }
                }
            }
        }

        if (!Objects.equals(book.getPrice(), "")) {
            double price;
            book.getPrice().replace("$", "");
            book.getPrice().replace(",", ".");
            book.getPrice().replace(" ", "");
            book.getPrice().replace("€", "");
            try {
                price = Double.parseDouble(book.getPrice());
                double priceTwoDecimals = Math.round(price * 100.0) / 100.0;
                if (priceTwoDecimals < 0.01) {
                    errors.rejectValue("price", "price.min", "The price must be at least 0.01");
                } if (priceTwoDecimals > 99.99) {
                    errors.rejectValue("price", "price.max", "The price must be at most 99.99");
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("price", "price.format", "The price must be a number");
            }
        }

    }
}
