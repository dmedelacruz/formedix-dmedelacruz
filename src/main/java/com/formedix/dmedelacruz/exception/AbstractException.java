package com.formedix.dmedelacruz.exception;

import lombok.Getter;

@Getter
public class AbstractException extends RuntimeException {
    private final ErrorCode errorCode;
    private final ErrorMessage errorMessage;

    public AbstractException(ErrorCode errorCode, ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AbstractException(ErrorCode errorCode, ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getMessage(), cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
