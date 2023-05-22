package com.formedix.dmedelacruz.exception.handler;

import com.formedix.dmedelacruz.dto.ErrorDetail;
import com.formedix.dmedelacruz.dto.Response;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.exception.constant.ErrorMessage;
import com.formedix.dmedelacruz.validation.NotBlankPositive;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(value = {
            ConstraintViolationException.class,
    })
    protected ResponseEntity<Response> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString().split("\\.")[1];

            ErrorCode errorCode = ErrorCode.DEF_001;
            ErrorMessage errorMessage = ErrorMessage.DEFAULT_ERROR_MESSAGE;

            if(violation.getConstraintDescriptor().getAnnotation() instanceof NotBlank) {
                errorCode = ErrorCode.REQ_001;
                errorMessage = ErrorMessage.REQUIRED_PARAMETER_MISSING;
            }

            if(violation.getConstraintDescriptor().getAnnotation() instanceof NotBlankPositive) {
                errorCode = ErrorCode.REQ_003;
                errorMessage = ErrorMessage.REQUIRED_POSITIVE_NUMERIC_PARAMETER;
            }

            ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorMessage.getMessage(), field), errorMessage.getDetails());
            errorDetails.add(errorDetail);
        });
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Response.builder().errors(errorDetails).build());
    }


}
