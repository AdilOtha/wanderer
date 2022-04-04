package ca.dal.cs.wanderer.exception.category.pinexception;

import ca.dal.cs.wanderer.util.ErrorMessages;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidCoordinates extends RuntimeException{
    private final String message;
    private final HttpStatus status;
    public InvalidCoordinates(ErrorMessages messages)
    {
        this.message=messages.getErrorMessage();
        this.status=messages.getHttpStatus();
    }
}
