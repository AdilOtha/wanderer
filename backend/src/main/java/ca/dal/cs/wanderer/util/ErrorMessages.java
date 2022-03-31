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
    PIN_NOT_FOUND("Unable to retrieve Pins", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_COORDINATES("Invalid Center Coordinates provided", HttpStatus.INTERNAL_SERVER_ERROR),
    PINID_NOT_FOUND("Pin Id not found in the request", HttpStatus.BAD_REQUEST),
    USERID_NOT_FOUND("User Id not found in the request", HttpStatus.BAD_REQUEST),
    FUTURETRIP_NOT_SAVED("Future Trip not saved", HttpStatus.INTERNAL_SERVER_ERROR);

    //below should be always fixed
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
