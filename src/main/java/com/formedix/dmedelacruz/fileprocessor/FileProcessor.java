package com.formedix.dmedelacruz.fileprocessor;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessor {

    void processData(MultipartFile file);
    FileType getFileType();
}
