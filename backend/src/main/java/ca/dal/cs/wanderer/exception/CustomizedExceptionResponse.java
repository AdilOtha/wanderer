package ca.dal.cs.wanderer.exception;

import ca.dal.cs.wanderer.exception.category.EmailNotFound;
import ca.dal.cs.wanderer.exception.category.InvalidCoordinates;
import ca.dal.cs.wanderer.exception.category.PinNotFound;
import ca.dal.cs.wanderer.exception.category.PinNotSaved;
import ca.dal.cs.wanderer.util.ErrorMessages;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomizedExceptionResponse extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date()
                , ex.getMessage()
                , request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailNotFound.class)
    public final ResponseEntity<Object> emailNotFound(EmailNotFound ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        GenericResponse<ExceptionResponse> genericResponse = new GenericResponse<>(false, ErrorMessages.EMAIL_NOT_FOUND.getHttpStatus().name(), response);
        return handleExceptionInternal(ex, genericResponse, new HttpHeaders(), ErrorMessages.EMAIL_NOT_FOUND.getHttpStatus(), request);
    }

    @ExceptionHandler(PinNotSaved.class)
    public final ResponseEntity<Object> pinNotSaved(PinNotSaved ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        GenericResponse<ExceptionResponse> genericResponse = new GenericResponse<>(false, ErrorMessages.PIN_NOT_SAVED.getHttpStatus().name(), response);
        return handleExceptionInternal(ex, genericResponse, new HttpHeaders(), ErrorMessages.PIN_NOT_SAVED.getHttpStatus(), request);
    }

    @ExceptionHandler(PinNotFound.class)
    public final ResponseEntity<Object> pinNotFound(PinNotFound ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        GenericResponse<ExceptionResponse> genericResponse = new GenericResponse<>(false, ErrorMessages.PIN_NOT_FOUND.getHttpStatus().name(), response);
        return handleExceptionInternal(ex, genericResponse, new HttpHeaders(), ErrorMessages.PIN_NOT_FOUND.getHttpStatus(), request);
    }

    @ExceptionHandler(InvalidCoordinates.class)
    public final ResponseEntity<Object> invalidCoordinates(InvalidCoordinates ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        GenericResponse<ExceptionResponse> genericResponse = new GenericResponse<>(false, ErrorMessages.INVALID_COORDINATES.getHttpStatus().name(), response);
        return handleExceptionInternal(ex, genericResponse, new HttpHeaders(), ErrorMessages.INVALID_COORDINATES.getHttpStatus(), request);
    }


}
