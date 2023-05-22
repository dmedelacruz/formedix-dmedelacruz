package com.formedix.dmedelacruz.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class NotBlankPositiveValidatorTest {

    private final NotBlankPositiveValidator validator = new NotBlankPositiveValidator();
    private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

    @Nested
    class TestIsValid {

        @Test
        @DisplayName("Test Null, Blank, Negative Values - Should return FALSE")
        void testIsValidFalse() {
            Double value = null;
            boolean isValid1 = validator.isValid(value, context);
            assertFalse(isValid1);

            value = -10.0;
            boolean isValid2 = validator.isValid(value, context);
            assertFalse(isValid2);
        }

        @Test
        @DisplayName("Test NonBlank Positive Values - Should return TRUE")
        void testIsValidtrue() {
            Double value = 1.0;
            boolean isValid = validator.isValid(value, context);
            assertTrue(isValid);
        }

    }

}