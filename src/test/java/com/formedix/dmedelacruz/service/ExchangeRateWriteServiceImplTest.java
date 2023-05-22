package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.data.ExchangeRateRepository;
import com.formedix.dmedelacruz.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateWriteServiceImplTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    private ExchangeRateWriteService exchangeRateWriteService;

    @BeforeEach
    void init() {
        this.exchangeRateWriteService = new ExchangeRateWriteServiceImpl(exchangeRateRepository);
    }

    @Nested
    class TestSaveExchangeRate {
        @Test
        @DisplayName("Test Saving Exchange Rate Data")
        void testExchangeRate() {
            String dateString = "2099-01-01";
            String currencyCode = "TEST";
            Double rate = 1.0;
            LocalDate localDate = DateUtil.parseDate(dateString);
            CurrencyRate currencyRate = new CurrencyRate(currencyCode, rate);
            Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMap = Map.of(localDate, Map.of(currencyCode, currencyRate));

            assertDoesNotThrow(() -> exchangeRateWriteService.saveExchangeRate(exchangeRateMap));
            Mockito.verify(exchangeRateRepository, Mockito.times(1)).insertData(exchangeRateMap);
        }

    }

}