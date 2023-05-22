package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.exception.CurrencyNotFoundException;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
import com.formedix.dmedelacruz.exception.ExchangeRateUnavailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CurrencyConverterServiceImplTest extends BaseTest {

    @Autowired
    private CurrencyConverterService currencyConverterService;

    @Autowired
    CurrencyConverterServiceImplTest(@Autowired MockMvc mockMvc) {
        super(mockMvc);
    }

    @Nested
    class TestConvertCurrencyAmount {

        @Test
        @DisplayName("Test Convert Currency With Incorrect Date - Should Throw DataNotFoundException")
        void testNoDataForDate() {
            String dateString = "2024-05-19";
            String targetCurrency = "USD";
            String sourceCurrency = "JPY";
            Double amount = 1.0;

            assertThrows(DataNotFoundException.class, () -> currencyConverterService.convertCurrencyAmount(dateString, Optional.empty(), sourceCurrency, targetCurrency, amount));
        }

        @Test
        @DisplayName("Test Convert Currency With N/A Exchange Rate - Should Throw ExchangeRateUnavailableException")
        void testExchangeRateUnavailable() {

            String dateString = "2023-05-19";
            String targetCurrency = "USD";
            String sourceCurrency = "CYP";
            Double amount = 1.0;

            assertThrows(ExchangeRateUnavailableException.class, () -> currencyConverterService.convertCurrencyAmount(dateString, Optional.empty(), sourceCurrency, targetCurrency, amount));
        }

        @Test
        @DisplayName("Test Convert Currency With Unknown Currency - Should Throw CurrencyNotFoundException")
        void testCurrencyNotFound() {

            String dateString = "2023-05-19";
            String targetCurrency = "USD";
            String sourceCurrency = "UNKNOWN";
            Double amount = 1.0;

            assertThrows(CurrencyNotFoundException.class, () -> currencyConverterService.convertCurrencyAmount(dateString, Optional.empty(), sourceCurrency, targetCurrency, amount));
        }

        @Test
        @DisplayName("Test Convert Currency With Valid Arguments - Should Return ConvertedAmount Value")
        void testConvertCurrencyAmount() {
            String dateString = "2023-05-19";
            String targetCurrency = "USD";
            String sourceCurrency = "JPY";
            Double amount = 1.0;

            Double convertedAmount = currencyConverterService.convertCurrencyAmount(dateString, Optional.empty(), sourceCurrency, targetCurrency, amount);
            assertNotNull(convertedAmount);
            assertTrue(convertedAmount > 0);
        }

    }

}