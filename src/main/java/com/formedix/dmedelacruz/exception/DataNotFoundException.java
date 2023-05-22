package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;

public class DataNotFoundException extends AbstractException {
    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DataNotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
