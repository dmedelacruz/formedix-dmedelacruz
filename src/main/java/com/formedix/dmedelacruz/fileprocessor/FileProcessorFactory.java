package com.formedix.dmedelacruz.fileprocessor;

import org.springframework.beans.factory.annotation.Autowired;
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
        if(fileProcessor == null) throw new UnsupportedOperationException("File Type Not Supported");
        return fileProcessor;
    }
}
