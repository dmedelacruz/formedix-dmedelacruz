package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.dao.ErrorDetail;
import com.formedix.dmedelacruz.dao.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {
            CurrencyNotFoundException.class,
            DataNotFoundException.class,
            ExchangeRateUnavailableException.class,
            UnsupportedFileTypeException.class,
    })
    protected ResponseEntity<Response> badRequestExceptionHandler(AbstractException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorMessage errorMessage = ex.getErrorMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().error(new ErrorDetail(errorCode, errorMessage.getMessage(), errorMessage.getDetails())).build());
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
