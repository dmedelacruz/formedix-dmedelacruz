package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import lombok.Getter;

@Getter
public class CurrencyNotFoundException extends AbstractException {
    private final String code;
    public CurrencyNotFoundException(ErrorCode errorCode, String code) {
        super(errorCode);
        this.code = code;
    }

    public CurrencyNotFoundException(ErrorCode errorCode, String code, Throwable cause) {
        super(errorCode, cause);
        this.code = code;
    }
}
