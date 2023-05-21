package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.dao.ErrorDetail;
import com.formedix.dmedelacruz.dao.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            DataNotFoundException.class,
    })
    protected ResponseEntity<Response> dataNotFoundExceptionHandler(DataNotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().error(new ErrorDetail(errorCode, errorMessage.getMessage(), errorMessage.getDetails())).build());
    }

    @ExceptionHandler(value = {
            CurrencyNotFoundException.class
    })
    protected ResponseEntity<Response> currencyNotFoundExceptionHandler(CurrencyNotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().error(new ErrorDetail(
                        errorCode,
                        String.format(errorMessage.getMessage(), ex.getCode()),
                        errorMessage.getDetails())).build());
    }

    @ExceptionHandler(value = {
            UnsupportedFileTypeException.class
    })
    protected ResponseEntity<Response> unsupportedFileTypeExceptionHandler(UnsupportedFileTypeException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().error(new ErrorDetail(
                        errorCode,
                        String.format(errorMessage.getMessage(), ex.getFileType()),
                        errorMessage.getDetails())).build());
    }

    @ExceptionHandler(value = {
            ExchangeRateUnavailableException.class
    })
    protected ResponseEntity<Response> exchangeRateUnavailableExceptionHandler(ExchangeRateUnavailableException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().error(new ErrorDetail(
                        errorCode,
                        String.format(errorMessage.getMessage(), ex.getCode()),
                        errorMessage.getDetails())).build());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String name = ex.getParameterName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().error(new ErrorDetail(
                        ErrorCode.REQ_001,
                        String.format(ErrorMessage.REQUIRED_PARAMETER_MISSING.getMessage(), name),
                        String.format(ErrorMessage.REQUIRED_PARAMETER_MISSING.getDetails(), name))).build()
                );
    }

}
