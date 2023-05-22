package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;

public class InvalidDateRangeException extends AbstractException{
    public InvalidDateRangeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidDateRangeException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
