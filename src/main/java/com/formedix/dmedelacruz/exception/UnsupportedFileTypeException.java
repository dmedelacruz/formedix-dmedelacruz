package com.formedix.dmedelacruz.exception;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import lombok.Getter;

@Getter
public class UnsupportedFileTypeException extends AbstractException {

    private final String fileType;

    public UnsupportedFileTypeException(ErrorCode errorCode, String fileType) {
        super(errorCode);
        this.fileType = fileType;
    }

    public UnsupportedFileTypeException(ErrorCode errorCode, String fileType, Throwable cause) {
        super(errorCode, cause);
        this.fileType = fileType;
    }
}
