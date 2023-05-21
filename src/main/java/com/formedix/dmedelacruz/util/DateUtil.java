package com.formedix.dmedelacruz.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /*
     * Default date parsing method that assumes default date format yyyy-MM-dd
     */
    public static LocalDate parseDate(String dateString) {
        return parse(dateString, DEFAULT_DATE_FORMAT);
    }

    /*
     * Date Parsing method that parses a given dateString using a specified dateFormat
     */
    public static LocalDate parseDate(String dateString, String dateFormat) {
        return parse(dateString, dateFormat);
    }

    private static LocalDate parse(String dateString, String dateFormat) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return LocalDate.parse(dateString, dateTimeFormatter);
    }

}
