package com.formedix.dmedelacruz.fileprocessor;

import com.formedix.dmedelacruz.exception.FileReadException;
import com.formedix.dmedelacruz.service.ExchangeRateWriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CsvFileProcessorTest {

    private ExchangeRateWriteService exchangeRateWriteService = Mockito.mock(ExchangeRateWriteService.class);

    private final CsvFileProcessor csvFileProcessor = new CsvFileProcessor(exchangeRateWriteService);

    private final MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

    @Nested
    class TestProcessData {

        @Test
        @DisplayName("Test Processing Error Reading File Data - Should Throw FileReadException")
        void testProcessDataFileReadException() throws IOException {
            Mockito.when(multipartFile.getInputStream()).thenThrow(IOException.class);
            assertThrows(FileReadException.class, () -> csvFileProcessor.processData(multipartFile));
        }

        @Test
        @DisplayName("Test Processing EmptyFile Data - Should Throw FileReadException")
        void testProcessEmptyFile() throws FileNotFoundException {
            File file = ResourceUtils.getFile("classpath:empty.csv");
            try(FileInputStream inputStream = new FileInputStream(file)) {
                Mockito.when(multipartFile.getInputStream()).thenReturn(inputStream);
                assertThrows(FileReadException.class, () -> csvFileProcessor.processData(multipartFile));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("Test Processing Valid File Data - Should Not Have Issues")
        void testProcessData() throws FileNotFoundException {
            File file = ResourceUtils.getFile("classpath:eurofxref-hist.csv");
            try(FileInputStream inputStream = new FileInputStream(file)) {
                Mockito.when(multipartFile.getInputStream()).thenReturn(inputStream);
                assertDoesNotThrow(() -> csvFileProcessor.processData(multipartFile));
                Mockito.verify(exchangeRateWriteService, Mockito.times(1)).saveExchangeRate(Mockito.anyMap());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}