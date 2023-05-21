package com.formedix.dmedelacruz.exception;

import lombok.Getter;

@Getter
public class CurrencyNotFoundException extends AbstractException {
    private final String code;
    public CurrencyNotFoundException(ErrorCode errorCode, ErrorMessage errorMessage, String code) {
        super(errorCode, errorMessage);
        this.code = code;
    }

    public CurrencyNotFoundException(ErrorCode errorCode, ErrorMessage errorMessage, String code, Throwable cause) {
        super(errorCode, errorMessage, cause);
        this.code = code;
    }
}
