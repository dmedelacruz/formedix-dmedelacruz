package com.formedix.dmedelacruz.exception.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    DEF_001("Uh Oh. Something went wrong. Contact the admin for assistance.", "Uh Oh. Something went wrong. Contact the admin for assistance."),
    REQ_001("Required Parameter '%s' is not present or is blank", "Make sure required parameter is present and is not empty"),
    REQ_002("Required Parameter: '%s' must be numeric", "Make sure value is strictly numeric"),
    REQ_003("Required Parameter: '%s' must be not be blank and greater than zero (0)", "Make sure value is not blank and strictly greater than zero (0)"),
    FILE_001("File Type: '%s' Not Supported", "Currently supporting only CSV Files for this version."),
    FILE_002("Unknown File Type: '%s'", "File Extension may be invalid or empty."),
    FILE_003("Provided File is empty", "Make Sure File has contents"),
    FILE_004("Processing File Failed", "Please contact support team"),
    DATA_001("No Data For Specified Date", "No existing records for specified date. No Records available for weekends."),
    DATA_002("No Date For Specified Date Range", "No existing records for specified date range."),
    CURR_001("Unknown Currency: '%s'", "Make sure currency is a valid currency"),
    CURR_002("Exchange Rate Unavailable For Specified Currency: '%s'", "Specific currencies may not have available exchange rates");


    private final String message;
    private final String details;

    ErrorCode(String message, String details) {
        this.message = message;
        this.details = details;
    }

}
