package com.formedix.dmedelacruz.fileprocessor;

import lombok.NonNull;

public enum FileType {
    CSV, PDF, XML;

    public static FileType valueOfIgnoreCase(String code) {
        return valueOf(code.toUpperCase());
    }
}
