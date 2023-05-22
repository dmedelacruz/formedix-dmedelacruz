package com.formedix.dmedelacruz.fileprocessor;

import com.formedix.dmedelacruz.exception.UnsupportedFileTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FileProcessorFactoryTest {

    @Nested
    class TestGetFileProcessor {

        @Test
        @DisplayName("Test Get FileProcessor With Unsupported File Provided - Should Throw UnsupportedFileTypeException")
        void testGetFileProcessorUnsupportedFile() {
            FileType fileType = FileType.PDF;
            assertThrows(UnsupportedFileTypeException.class, () -> FileProcessorFactory.getFileProcessor(fileType));
        }

        @Test
        @DisplayName("Test Get FileProcessor With CSV File Provided - Should Return CsvFileProcessor Instance")
        void testGetCsvFileProcessor() {
            FileType fileType = FileType.CSV;
            FileProcessor fileProcessor = FileProcessorFactory.getFileProcessor(fileType);
            assertNotNull(fileProcessor);
            assertTrue(fileProcessor instanceof CsvFileProcessor);
        }

    }
}