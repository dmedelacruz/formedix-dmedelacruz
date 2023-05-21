package com.formedix.dmedelacruz.fileprocessor;

import com.formedix.dmedelacruz.exception.ErrorCode;
import com.formedix.dmedelacruz.exception.ErrorMessage;
import com.formedix.dmedelacruz.exception.UnsupportedFileTypeException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileProcessorFactory {

    private static final Map<FileType, FileProcessor> fileProcessorsMap = new HashMap<>();

    private FileProcessorFactory(List<FileProcessor> fileProcessors) {
        for(FileProcessor fileProcessor : fileProcessors) {
            fileProcessorsMap.put(fileProcessor.getFileType(), fileProcessor);
        }
    }

    public static FileProcessor getFileProcessor(FileType fileType) {
        FileProcessor fileProcessor = fileProcessorsMap.get(fileType);
        if(fileProcessor == null) throw new UnsupportedFileTypeException(ErrorCode.FILE_001, ErrorMessage.FILE_TYPE_NOT_SUPPORTED, fileType.name());
        return fileProcessor;
    }
}
