package com.formedix.dmedelacruz.exception;

public class DataNotFoundException extends AbstractException {
    public DataNotFoundException(ErrorCode errorCode, ErrorMessage errorMessage) {
        super(errorCode, errorMessage);
    }

    public DataNotFoundException(ErrorCode errorCode, ErrorMessage errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }
}
