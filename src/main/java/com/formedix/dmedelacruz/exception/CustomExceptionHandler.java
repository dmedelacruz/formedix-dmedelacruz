package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.dao.ErrorDetail;
import com.formedix.dmedelacruz.dao.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            DataNotFoundException.class,
    })
    protected ResponseEntity<Response> dataNotFoundExceptionHandler(DataNotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, errorMessage.getMessage(), errorMessage.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            CurrencyNotFoundException.class
    })
    protected ResponseEntity<Response> currencyNotFoundExceptionHandler(CurrencyNotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorMessage.getMessage(), ex.getCode()), errorMessage.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            UnsupportedFileTypeException.class
    })
    protected ResponseEntity<Response> unsupportedFileTypeExceptionHandler(UnsupportedFileTypeException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorMessage.getMessage(), ex.getFileType()), errorMessage.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            ExchangeRateUnavailableException.class
    })
    protected ResponseEntity<Response> exchangeRateUnavailableExceptionHandler(ExchangeRateUnavailableException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorMessage.getMessage(), ex.getCode()), errorMessage.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class
    })
    protected ResponseEntity<Response> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {

        ErrorCode errorCode = ErrorCode.DEF_001;
        ErrorMessage errorMessage = ErrorMessage.DEFAULT_ERROR_MESSAGE;

        if (ex.getRequiredType() != null && ex.getRequiredType().equals(Double.class)) {
            errorCode = ErrorCode.REQ_002;
            errorMessage = ErrorMessage.REQUIRED_NUMERIC_PARAMETER;

            ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorMessage.getMessage(), ex.getName()), errorMessage.getDetails());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.builder().errors(List.of(errorDetail)).build());

        }

        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorMessage.getMessage()), errorMessage.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

}
