package com.formedix.dmedelacruz.util;

import com.formedix.dmedelacruz.exception.DateFormatException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /*
     * Default date parsing method that assumes default date format yyyy-MM-dd
     */
    public static Date parseDate(String dateString) {
        return parse(dateString, DEFAULT_DATE_FORMAT);
    }

    /*
     * Date Parsing method that parses a given dateString using a specified dateFormat
     */
    public static Date parseDate(String dateString, String dateFormat) {
        return parse(dateString, dateFormat);
    }

    private static Date parse(String dateString, String dateFormat) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException p) {
            throw new DateFormatException();
        }
    }

}
