package com.formedix.dmedelacruz.exception;

public class CurrencyNotFoundException extends AbstractException {
    public CurrencyNotFoundException(ErrorCode errorCode, ErrorMessage errorMessage) {
        super(errorCode, errorMessage);
    }

    public CurrencyNotFoundException(ErrorCode errorCode, ErrorMessage errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }
}
