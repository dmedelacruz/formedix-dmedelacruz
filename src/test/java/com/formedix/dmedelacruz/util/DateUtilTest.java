package com.formedix.dmedelacruz.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DateUtilTest {


    @Test
    @DisplayName("Test String to LocalDate Conversion")
    void testParseDate() {
        String dateString = "2023-05-21";
        LocalDate localDate = DateUtil.parseDate(dateString);
        assertNotNull(localDate);
        assertEquals(21, localDate.getDayOfMonth());
        assertEquals(5, localDate.getMonthValue());
        assertEquals(2023, localDate.getYear());
    }

    @Test
    @DisplayName("Test String to LocalDate Conversion With User Provided Date Format")
    void testParseDateWithGivenFormat() {
        String dateString = "2023-05-21";
        String dateFormat = "yyyy-MM-dd";
        LocalDate localDate = DateUtil.parseDate(dateString, dateFormat);
        assertNotNull(localDate);
        assertEquals(21, localDate.getDayOfMonth());
        assertEquals(5, localDate.getMonthValue());
        assertEquals(2023, localDate.getYear());
    }

}