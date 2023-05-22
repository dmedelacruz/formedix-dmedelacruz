package com.formedix.dmedelacruz.exception.handler;

import com.formedix.dmedelacruz.dto.ErrorDetail;
import com.formedix.dmedelacruz.dto.Response;
import com.formedix.dmedelacruz.exception.*;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            DataNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Response> dataNotFoundExceptionHandler(DataNotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, errorCode.getMessage(), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            FileReadException.class,
    })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Response> fileReadExceptionhandler(FileReadException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, errorCode.getMessage(), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            CurrencyNotFoundException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Response> currencyNotFoundExceptionHandler(CurrencyNotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorCode.getMessage(), ex.getCode()), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            UnsupportedFileTypeException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Response> unsupportedFileTypeExceptionHandler(UnsupportedFileTypeException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorCode.getMessage(), ex.getFileType()), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            ExchangeRateUnavailableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Response> exchangeRateUnavailableExceptionHandler(ExchangeRateUnavailableException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorCode.getMessage(), ex.getCode()), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Response> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        ErrorCode errorCode = ErrorCode.DEF_001;
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(Double.class)) {
            errorCode = ErrorCode.REQ_002;
            ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorCode.getMessage(), ex.getName()), errorCode.getDetails());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.builder().errors(List.of(errorDetail)).build());

        }
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorCode.getMessage()), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            MultipartException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Response> multipartExceptionHandler(MultipartException ex) {
        ErrorCode errorCode = ErrorCode.REQ_001;
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorCode.getMessage(), "file"), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @ExceptionHandler(value = {
            InvalidDateRangeException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Response> invalidDateRangeExceptionHandler(InvalidDateRangeException ex) {
        ErrorCode errorCode = ErrorCode.REQ_004;
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorCode.getMessage()), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCode errorCode = ErrorCode.REQ_001;
        ErrorDetail errorDetail = new ErrorDetail(errorCode, String.format(errorCode.getMessage(), "file"), errorCode.getDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.builder().errors(List.of(errorDetail)).build());
    }
}
