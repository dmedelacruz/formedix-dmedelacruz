package com.formedix.dmedelacruz.dto;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;

public record ErrorDetail(ErrorCode code, String message, String details) {
}
