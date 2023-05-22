package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.exception.constant.ErrorMessage;
import lombok.Getter;

@Getter
public class UnsupportedFileTypeException extends AbstractException {

    private final String fileType;

    public UnsupportedFileTypeException(ErrorCode errorCode, ErrorMessage errorMessage, String fileType) {
        super(errorCode, errorMessage);
        this.fileType = fileType;
    }

    public UnsupportedFileTypeException(ErrorCode errorCode, ErrorMessage errorMessage, String fileType, Throwable cause) {
        super(errorCode, errorMessage, cause);
        this.fileType = fileType;
    }
}
