package com.formedix.dmedelacruz.exception;

public class ExchangeRateUnavailableException extends AbstractException {
    public ExchangeRateUnavailableException(ErrorCode errorCode, ErrorMessage errorMessage) {
        super(errorCode, errorMessage);
    }

    public ExchangeRateUnavailableException(ErrorCode errorCode, ErrorMessage errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }
}
