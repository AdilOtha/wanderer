package ca.dal.cs.wanderer.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessages {
    //add your fields here
    EMAIL_NOT_FOUND("Email can not be found, please check the principal", HttpStatus.FORBIDDEN),
    PRINCIPAL_NOT_FOUND("Principal not found, please login", HttpStatus.FORBIDDEN),
    PIN_NOT_SAVED("Error while saving Pin",HttpStatus.INTERNAL_SERVER_ERROR),
    PIN_NOT_FOUND("Unable to retrieve Pins", HttpStatus.INTERNAL_SERVER_ERROR);

    //below should be always fixed
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
