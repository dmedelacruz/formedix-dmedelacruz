package com.formedix.dmedelacruz.exception.handler;

import com.formedix.dmedelacruz.dto.ErrorDetail;
import com.formedix.dmedelacruz.dto.Response;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.validation.NotBlankPositive;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ValidationExceptionHandlerTest {

    private final ConstraintViolationException ex = Mockito.mock(ConstraintViolationException.class);

    private final ValidationExceptionHandler handler = new ValidationExceptionHandler();

    private String testField = "field";

    @Test
    @DisplayName("Test Handling NotBlank Field Value Validation")
    void testNotBlankHandler() {
        ConstraintViolation violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(ex.getConstraintViolations()).thenReturn(Set.of(violation));

        Path path = Mockito.mock(Path.class);
        Mockito.when(path.toString()).thenReturn("method." + testField);
        Mockito.when(violation.getPropertyPath()).thenReturn(path);

        ConstraintDescriptor constraintDescriptor = Mockito.mock(ConstraintDescriptor.class);
        Annotation annotation = Mockito.mock(Annotation.class, Mockito.withSettings().extraInterfaces(NotBlank.class));
        Mockito.when(constraintDescriptor.getAnnotation()).thenReturn(annotation);
        Mockito.when(violation.getConstraintDescriptor()).thenReturn(constraintDescriptor);

        ResponseEntity<Response> responseEntity = handler.constraintViolationExceptionHandler(ex);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody().getContent());
        assertNotNull(responseEntity.getBody().getErrors());

        List<ErrorDetail> errors = responseEntity.getBody().getErrors();

        assertEquals(1, errors.size());
        assertEquals(ErrorCode.REQ_001, errors.get(0).code());
        assertEquals(String.format(ErrorCode.REQ_001.getMessage(), testField), errors.get(0).message());
        assertEquals(ErrorCode.REQ_001.getDetails(), errors.get(0).details());
    }

    @Test
    @DisplayName("Test Handling NotBlankPositive Field Value Validation")
    void testNotBlankPositiveHandler() {
        ConstraintViolation violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(ex.getConstraintViolations()).thenReturn(Set.of(violation));

        Path path = Mockito.mock(Path.class);
        Mockito.when(path.toString()).thenReturn("method." + testField);
        Mockito.when(violation.getPropertyPath()).thenReturn(path);

        ConstraintDescriptor constraintDescriptor = Mockito.mock(ConstraintDescriptor.class);
        Annotation annotation = Mockito.mock(Annotation.class, Mockito.withSettings().extraInterfaces(NotBlankPositive.class));
        Mockito.when(constraintDescriptor.getAnnotation()).thenReturn(annotation);
        Mockito.when(violation.getConstraintDescriptor()).thenReturn(constraintDescriptor);

        ResponseEntity<Response> responseEntity = handler.constraintViolationExceptionHandler(ex);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody().getContent());
        assertNotNull(responseEntity.getBody().getErrors());

        List<ErrorDetail> errors = responseEntity.getBody().getErrors();

        assertEquals(1, errors.size());
        assertEquals(ErrorCode.REQ_003, errors.get(0).code());
        assertEquals(String.format(ErrorCode.REQ_003.getMessage(), testField), errors.get(0).message());
        assertEquals(ErrorCode.REQ_003.getDetails(), errors.get(0).details());
    }

    @Test
    @DisplayName("Test Handling Default Field Value Validation")
    void testDefaultHandler() {
        ConstraintViolation violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(ex.getConstraintViolations()).thenReturn(Set.of(violation));

        Path path = Mockito.mock(Path.class);
        Mockito.when(path.toString()).thenReturn("method." + testField);
        Mockito.when(violation.getPropertyPath()).thenReturn(path);

        ConstraintDescriptor constraintDescriptor = Mockito.mock(ConstraintDescriptor.class);
        Annotation annotation = Mockito.mock(Annotation.class, Mockito.withSettings().extraInterfaces(NotNull.class));
        Mockito.when(constraintDescriptor.getAnnotation()).thenReturn(annotation);
        Mockito.when(violation.getConstraintDescriptor()).thenReturn(constraintDescriptor);

        ResponseEntity<Response> responseEntity = handler.constraintViolationExceptionHandler(ex);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody().getContent());
        assertNotNull(responseEntity.getBody().getErrors());

        List<ErrorDetail> errors = responseEntity.getBody().getErrors();

        assertEquals(1, errors.size());
        assertEquals(ErrorCode.DEF_001, errors.get(0).code());
        assertEquals(String.format(ErrorCode.DEF_001.getMessage(), testField), errors.get(0).message());
        assertEquals(ErrorCode.DEF_001.getDetails(), errors.get(0).details());
    }

}