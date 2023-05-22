package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.exception.constant.ErrorMessage;
import lombok.Getter;

@Getter
public class ExchangeRateUnavailableException extends AbstractException {
    private final String code;
    public ExchangeRateUnavailableException(ErrorCode errorCode, ErrorMessage errorMessage, String code) {
        super(errorCode, errorMessage);
        this.code = code;
    }

    public ExchangeRateUnavailableException(ErrorCode errorCode, ErrorMessage errorMessage, String code, Throwable cause) {
        super(errorCode, errorMessage, cause);
        this.code = code;
    }
}
