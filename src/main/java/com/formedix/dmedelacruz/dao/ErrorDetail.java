package com.formedix.dmedelacruz.dao;

import com.formedix.dmedelacruz.exception.ErrorCode;
import com.formedix.dmedelacruz.exception.ErrorMessage;

public record ErrorDetail(ErrorCode code, String message, String details) {
}
