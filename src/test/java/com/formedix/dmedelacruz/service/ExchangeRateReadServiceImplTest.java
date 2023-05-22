package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.exception.CurrencyNotFoundException;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
import com.formedix.dmedelacruz.exception.ExchangeRateUnavailableException;
import com.formedix.dmedelacruz.exception.InvalidDateRangeException;
import com.formedix.dmedelacruz.util.DateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateReadServiceImplTest extends BaseTest {

    @Autowired
    private ExchangeRateReadService exchangeRateReadService;

    protected ExchangeRateReadServiceImplTest(@Autowired MockMvc mockMvc) {
        super(mockMvc);
    }

    @Nested
    class TestGetExchangeRates {

        @Test
        @DisplayName("Test Getting Exchange Rates With Correct Date - Should Return NonEmpty Set Result")
        void testGetExchangeRates() {
            String dateString = "2015-05-19";
            Set<CurrencyRate> exchangeRates = exchangeRateReadService.getExchangeRates(dateString, Optional.empty());
            assertNotNull(exchangeRates);
            assertNotEquals(0, exchangeRates.size());
        }

        @Test
        @DisplayName("Test Getting Exchange Rates With Incorrect Date - Should Throw DataNotFoundException")
        void testGetExchangeRatesNoData() {
            String dateString = "2024-05-19";
            assertThrows(DataNotFoundException.class, () -> exchangeRateReadService.getExchangeRates(dateString, Optional.empty()));
        }

    }

    @Nested
    class TestGetExchangeRatesRange {

        @Test@DisplayName("Test Getting Exchange Rates With Incorrect Date Range - Should Throw DataNotFoundException")
        void testGetExchangeRatesRangeNoData() {
            String startDateString = "2024-05-19";
            String endDateString = "2024-07-21";
            assertThrows(DataNotFoundException.class, () -> exchangeRateReadService.getExchangeRatesRange(startDateString, endDateString, Optional.empty()));
        }

        @Test@DisplayName("Test Getting Exchange Rates With Incorrect Date Range - Should Throw DataNotFoundException")
        void testGetExchangeRatesRangeInvalidDateRange() {
            String startDateString = "2020-05-19";
            String endDateString = "2019-07-21";
            assertThrows(InvalidDateRangeException.class, () -> exchangeRateReadService.getExchangeRatesRange(startDateString, endDateString, Optional.empty()));
        }

        @Test
        @DisplayName("Test Getting Exchange Rates With Correct Date Range - Should Return NonEmpty Map Result")
        void testGetExchangeRatesRange() {
            String startDateString = "2023-05-15";
            String endDateString = "2023-05-19";
            Map<LocalDate, Map<String, CurrencyRate>> exchangeRatesRange = exchangeRateReadService.getExchangeRatesRange(startDateString, endDateString, Optional.empty());
            assertNotNull(exchangeRatesRange);
            assertNotEquals(0, exchangeRatesRange.size());
            assertEquals(Set.of(DateUtil.parseDate("2023-05-15"), DateUtil.parseDate("2023-05-16"), DateUtil.parseDate("2023-05-17"), DateUtil.parseDate("2023-05-18"), DateUtil.parseDate("2023-05-19")), exchangeRatesRange.keySet());
            assertEquals(5, exchangeRatesRange.values().size());
            assertNotNull(exchangeRatesRange.get(DateUtil.parseDate(startDateString)));
            assertNull(exchangeRatesRange.get(DateUtil.parseDate(startDateString).minusDays(1)));
            assertNotNull(exchangeRatesRange.get(DateUtil.parseDate(startDateString).plusDays(1)));
            assertNotNull(exchangeRatesRange.get(DateUtil.parseDate(startDateString).plusDays(2)));
            assertNotNull(exchangeRatesRange.get(DateUtil.parseDate(startDateString).plusDays(3)));
            assertNotNull(exchangeRatesRange.get(DateUtil.parseDate(startDateString).plusDays(4)));
            assertNull(exchangeRatesRange.get(DateUtil.parseDate(startDateString).plusDays(5)));
        }

    }

    @Nested
    class TestGetCurrencyRate {

        @Test
        @DisplayName("Test Getting Currency Exchange Rate With Incorrect Date - Should Throw DataNotFoundException")
        void testNoDataForDate() {
            String date = "2024-05-30";
            String code = "USD";
            assertThrows(DataNotFoundException.class, () -> exchangeRateReadService.getCurrencyRate(date, Optional.empty(), code));
        }

        @Test
        @DisplayName("Test Getting Currency Exchange Rate With N/A Rate - Should Throw ExchangeRateUnavailableException")
        void testExchangeRateUnavailable() {
            String date = "2023-05-19";
            String code = "CYP";
            assertThrows(ExchangeRateUnavailableException.class, () -> exchangeRateReadService.getCurrencyRate(date, Optional.empty(), code));
        }

        @Test
        @DisplayName("Test Getting Currency Exchange Rate With Unknown Currency - Should Throw CurrencyNotFoundException")
        void testCurrencyNotFound() {
            String date = "2023-05-19";
            String code = "UNKNOWN";
            assertThrows(CurrencyNotFoundException.class, () -> exchangeRateReadService.getCurrencyRate(date, Optional.empty(), code));
        }

        @Test
        @DisplayName("Test Getting Currency Exchange Rate With Valid Arguments - Should Return NonNull CurrencyRate")
        void testGetCurrencyRateSuccess() {
            String date = "2023-05-19";
            String code = "USD";
            CurrencyRate currencyRate = exchangeRateReadService.getCurrencyRate(date, Optional.empty(), code);
            assertNotNull(currencyRate);
            assertEquals(code, currencyRate.code());
            assertNotNull(currencyRate.exchangeRate());
            assertNotEquals(0.0, currencyRate.exchangeRate());
        }

    }

}