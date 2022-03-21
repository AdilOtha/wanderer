package ca.dal.cs.wanderer.exception.category;

import ca.dal.cs.wanderer.util.ErrorMessages;
import org.springframework.http.HttpStatus;

public class PinNotFound extends RuntimeException{
    private final String message;
    private final HttpStatus status;
    public PinNotFound(ErrorMessages messages)
    {
        this.message=messages.getErrorMessage();
        this.status=messages.getHttpStatus();
    }
}
