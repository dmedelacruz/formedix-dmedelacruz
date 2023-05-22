package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.exception.constant.ErrorMessage;

public class DataNotFoundException extends AbstractException {
    public DataNotFoundException(ErrorCode errorCode, ErrorMessage errorMessage) {
        super(errorCode, errorMessage);
    }

    public DataNotFoundException(ErrorCode errorCode, ErrorMessage errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }
}
