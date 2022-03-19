package ca.dal.cs.wanderer.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorMessages {
    //add you fields here
    EMAIL_NOT_FOUND("Email can not be found, please check the principal", HttpStatus.NOT_FOUND),
    PRINCIPAL_NOT_FOUND("Principal not found, please login", HttpStatus.FORBIDDEN),
    Blog_NF("gfuyewbfiewu",HttpStatus.BAD_REQUEST);

    //below should be always fixed
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
