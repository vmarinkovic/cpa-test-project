package com.cpaglobal.testproject.exception.handler;

import com.cpaglobal.testproject.exception.ApiError;
import com.cpaglobal.testproject.exception.EmployeeDataException;
import com.cpaglobal.testproject.exception.EmployeeNotFoundException;
import com.cpaglobal.testproject.exception.MissingParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameterException(MissingParameterException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiError> handleEmployeeNotFoundException(EmployeeNotFoundException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeDataException.class)
    public ResponseEntity<ApiError> handleEmployeeDataException(EmployeeDataException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
