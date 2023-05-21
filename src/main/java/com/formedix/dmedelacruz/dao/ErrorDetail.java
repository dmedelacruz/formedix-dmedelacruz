package com.formedix.dmedelacruz.dao;

import com.formedix.dmedelacruz.exception.ErrorCode;

public record ErrorDetail(ErrorCode code, String message, String details) {
}
