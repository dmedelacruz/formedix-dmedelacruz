package com.formedix.dmedelacruz.exception;

public class UnsupportedFileTypeException extends AbstractException {
    public UnsupportedFileTypeException(ErrorCode errorCode, ErrorMessage errorMessage) {
        super(errorCode, errorMessage);
    }

    public UnsupportedFileTypeException(ErrorCode errorCode, ErrorMessage errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }
}
