package com.formedix.dmedelacruz.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    REQUIRED_PARAMETER_MISSING("Required Parameter '%s' is not present", "Make sure required parameter is present and is not empty"),
    FILE_TYPE_NOT_SUPPORTED("File Type: '%s' Not Supported", "Currently supporting only CSV Files for this version."),
    UNKNOWN_FILE_TYPE("Unknown File Type: '%s'", "File Extension may be invalid or empty."),
    NO_DATA_FOR_DATE("No Data For Specified Date", "No existing records for specified date. No Records available for weekends."),
    NO_DATA_FOR_DATE_RANGE("No Date For Specified Date Range", "No existing records for specified date range."),
    CURRENCY_NOT_FOUND("Unknown Currency: '%s'", "Make sure currency is a valid currency"),
    NO_EXCHANGE_RATE_AVAILABLE("Exchange Rate Unavailable For Specified Currency: '%s'", "Specific currencies may not have available exchange rates");

    private final String message;
    private final String details;

    ErrorMessage(String message, String details) {
        this.message = message;
        this.details = details;
    }

}
