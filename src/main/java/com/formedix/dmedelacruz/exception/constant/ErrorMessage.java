package com.formedix.dmedelacruz.exception.constant;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    DEFAULT_ERROR_MESSAGE("Uh Oh. Something went wrong. Contact the admin for assistance.", "Uh Oh. Something went wrong. Contact the admin for assistance."),
    REQUIRED_PARAMETER_MISSING("Required Parameter '%s' is not present or is blank", "Make sure required parameter is present and is not empty"),
    REQUIRED_NUMERIC_PARAMETER("Required Parameter: '%s' must be numeric", "Make sure value is strictly numeric"),
    REQUIRED_POSITIVE_NUMERIC_PARAMETER("Required Parameter: '%s' must be not be blank and greater than zero (0)", "Make sure value is not blank and strictly greater than zero (0)"),
    FILE_TYPE_NOT_SUPPORTED("File Type: '%s' Not Supported", "Currently supporting only CSV Files for this version."),
    UNKNOWN_FILE_TYPE("Unknown File Type: '%s'", "File Extension may be invalid or empty."),
    FILE_READ_ERROR("Processing File Failed", "Please contact support team"),
    FILE_EMPTY_ERROR("Provided File is empty", "Make Sure File has contents"),
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
