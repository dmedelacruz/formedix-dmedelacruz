package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;

public class FileReadException extends AbstractException {
    public FileReadException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileReadException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
