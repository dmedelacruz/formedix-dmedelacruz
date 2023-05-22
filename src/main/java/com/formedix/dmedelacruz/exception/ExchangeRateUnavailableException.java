package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ExchangeRateUnavailableException extends AbstractException {
    private final String code;
    public ExchangeRateUnavailableException(ErrorCode errorCode, String code) {
        super(errorCode);
        this.code = code;
    }

    public ExchangeRateUnavailableException(ErrorCode errorCode, String code, Throwable cause) {
        super(errorCode, cause);
        this.code = code;
    }
}
