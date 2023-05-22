package com.formedix.dmedelacruz.exception.handler;

import com.formedix.dmedelacruz.dto.ErrorDetail;
import com.formedix.dmedelacruz.dto.Response;
import com.formedix.dmedelacruz.exception.*;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CustomExceptionHandlerTest {

    private final String paramName = "param";
    private final CustomExceptionHandler handler = new CustomExceptionHandler();

    @Test
    void testDataNotFoundExceptionHandler() {
        final DataNotFoundException ex = Mockito.mock(DataNotFoundException.class);
        ErrorCode errorCode = ErrorCode.DATA_001;
        Mockito.when(ex.getErrorCode()).thenReturn(errorCode);
        ResponseEntity<Response> responseEntity = handler.dataNotFoundExceptionHandler(ex);
        validate(responseEntity, HttpStatus.BAD_REQUEST, errorCode, paramName);
    }

    @Test
    void testFileReadExceptionhandler() {
        final FileReadException ex = Mockito.mock(FileReadException.class);
        ErrorCode errorCode = ErrorCode.FILE_004;
        Mockito.when(ex.getErrorCode()).thenReturn(errorCode);
        ResponseEntity<Response> responseEntity = handler.fileReadExceptionhandler(ex);
        validate(responseEntity, HttpStatus.UNPROCESSABLE_ENTITY, errorCode, paramName);
    }

    @Test
    void testCurrencyNotFoundExceptionHandler() {
        final CurrencyNotFoundException ex = Mockito.mock(CurrencyNotFoundException.class);
        final String code = "UNKNOWN";
        Mockito.when(ex.getCode()).thenReturn(code);
        ErrorCode errorCode = ErrorCode.CURR_001;
        Mockito.when(ex.getErrorCode()).thenReturn(errorCode);
        ResponseEntity<Response> responseEntity = handler.currencyNotFoundExceptionHandler(ex);
        validate(responseEntity, HttpStatus.BAD_REQUEST, errorCode, code);
    }

    @Test
    void testUnsupportedFileTypeExceptionHandler() {
        final UnsupportedFileTypeException ex = Mockito.mock(UnsupportedFileTypeException.class);
        final String fileType = "PDF";
        Mockito.when(ex.getFileType()).thenReturn(fileType);
        ErrorCode errorCode = ErrorCode.FILE_001;
        Mockito.when(ex.getErrorCode()).thenReturn(errorCode);
        ResponseEntity<Response> responseEntity = handler.unsupportedFileTypeExceptionHandler(ex);
        validate(responseEntity, HttpStatus.BAD_REQUEST, errorCode, fileType);
    }

    @Test
    void testExchangeRateUnavailableExceptionHandler() {
        final ExchangeRateUnavailableException ex = Mockito.mock(ExchangeRateUnavailableException.class);
        final String code = "UNKNOWN";
        Mockito.when(ex.getCode()).thenReturn(code);
        ErrorCode errorCode = ErrorCode.CURR_002;
        Mockito.when(ex.getErrorCode()).thenReturn(errorCode);
        ResponseEntity<Response> responseEntity = handler.exchangeRateUnavailableExceptionHandler(ex);
        validate(responseEntity, HttpStatus.BAD_REQUEST, errorCode, code);
    }

    @Test
    void testMethodArgumentTypeMismatchExceptionHandler() {
        final MethodArgumentTypeMismatchException ex = Mockito.mock(MethodArgumentTypeMismatchException.class);
        Mockito.doReturn(Double.class).when(ex).getRequiredType();
        Mockito.when(ex.getName()).thenReturn(paramName);
        ErrorCode errorCode = ErrorCode.REQ_002;
        ResponseEntity<Response> responseEntity = handler.methodArgumentTypeMismatchExceptionHandler(ex);
        validate(responseEntity, HttpStatus.BAD_REQUEST, errorCode, paramName);
    }

    @Test
    void testMultipartExceptionHandler() {
        final MultipartException ex = Mockito.mock(MultipartException.class);
        ErrorCode errorCode = ErrorCode.REQ_001;
        ResponseEntity<Response> responseEntity = handler.multipartExceptionHandler(ex);
        validate(responseEntity, HttpStatus.BAD_REQUEST, errorCode, "file");
    }

    @Test
    void testHandleMissingServletRequestPart() {
        HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);
        HttpStatus expectedStatusCode = HttpStatus.BAD_REQUEST;
        WebRequest webRequest = Mockito.mock(WebRequest.class);

        final MissingServletRequestPartException ex = Mockito.mock(MissingServletRequestPartException.class);
        ErrorCode errorCode = ErrorCode.REQ_001;
        ResponseEntity<Object> responseEntity = handler.handleMissingServletRequestPart(ex, httpHeaders, expectedStatusCode, webRequest);

        assertNotNull(responseEntity);
        assertEquals(expectedStatusCode, responseEntity.getStatusCode());
        assertNull(((Response) responseEntity.getBody()).getContent());
        assertNotNull(((Response) responseEntity.getBody()).getErrors());

        List<ErrorDetail> errors = ((Response) responseEntity.getBody()).getErrors();

        assertEquals(1, errors.size());
        assertEquals(errorCode, errors.get(0).code());
        assertEquals(String.format(errorCode.getMessage(), "file"), errors.get(0).message());
        assertEquals(errorCode.getDetails(), errors.get(0).details());
    }

    private void validate(ResponseEntity<Response> responseEntity, HttpStatus expectedStatusCode, ErrorCode expectedErrorCode, String field) {
        assertNotNull(responseEntity);
        assertEquals(expectedStatusCode, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody().getContent());
        assertNotNull(responseEntity.getBody().getErrors());

        List<ErrorDetail> errors = responseEntity.getBody().getErrors();

        assertEquals(1, errors.size());
        assertEquals(expectedErrorCode, errors.get(0).code());
        assertEquals(String.format(expectedErrorCode.getMessage(), field), errors.get(0).message());
        assertEquals(expectedErrorCode.getDetails(), errors.get(0).details());
    }

}