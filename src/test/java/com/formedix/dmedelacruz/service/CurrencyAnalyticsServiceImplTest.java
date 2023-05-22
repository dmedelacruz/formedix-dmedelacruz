package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.exception.CurrencyNotFoundException;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
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
class CurrencyAnalyticsServiceImplTest extends BaseTest {

    @Autowired
    private CurrencyAnalyticsService currencyAnalyticsService;

    protected CurrencyAnalyticsServiceImplTest(@Autowired MockMvc mockMvc) {
        super(mockMvc);
    }

    @Nested
    class TestGetHighestReferenceRate {

        @Test
        @DisplayName("Test Get Highest Reference Rate With Incorrect Date Range - Should Throw DataNotFoundException")
        void testGetHighestReferenceRateNoData() {
            String startDateString = "2024-05-19";
            String endDateString = "2024-07-21";
            String code = "USD";

            assertThrows(DataNotFoundException.class, () -> currencyAnalyticsService.getHighestReferenceRate(startDateString, endDateString, Optional.empty(), code));
        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With Unknown Currency - Should Throw CurrencyNotFoundException")
        void testGetHighestReferenceRateUnknownCurrency() {
            String startDateString = "2023-05-19";
            String endDateString = "2023-07-21";
            String code = "UNKNOWN";

            assertThrows(CurrencyNotFoundException.class, () -> currencyAnalyticsService.getHighestReferenceRate(startDateString, endDateString, Optional.empty(), code));
        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With N/A Exchange Rate - Should Return Zero")
        void testGetHighestReferenceRateUnavailableExchangeRate() {
            String startDateString = "2023-05-19";
            String endDateString = "2023-07-21";
            String code = "CYP";

            Double highestReferenceRate = currencyAnalyticsService.getHighestReferenceRate(startDateString, endDateString, Optional.empty(), code);
            assertNotNull(highestReferenceRate);
            assertEquals(0.0, highestReferenceRate);
        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With Valid Arguments - Should Return Highest Reference Rate Value Greater Than Zero")
        void testGetHighestReferenceRate() {
            String startDateString = "2023-05-19";
            String endDateString = "2023-07-21";
            String code = "USD";

            Double highestReferenceRate = currencyAnalyticsService.getHighestReferenceRate(startDateString, endDateString, Optional.empty(), code);
            assertNotNull(highestReferenceRate);
            assertTrue(highestReferenceRate > 0);
        }

    }

    @Nested
    class TestGetAverageReferenceRate {

        @Test
        @DisplayName("Test Get Average Reference Rate With Incorrect Date Range - Should Throw DataNotFoundException")
        void testGetAverageReferenceRateNoData() {
            String startDateString = "2024-05-19";
            String endDateString = "2024-07-21";
            String code = "USD";

            assertThrows(DataNotFoundException.class, () -> currencyAnalyticsService.getAverageReferenceRate(startDateString, endDateString, Optional.empty(), code));
        }

        @Test
        @DisplayName("Test Get Average Reference Rate With Unknown Currency - Should Throw CurrencyNotFoundException")
        void testGetAverageReferenceRateUnknownCurrency() {
            String startDateString = "2023-05-19";
            String endDateString = "2023-07-21";
            String code = "UNKNOWN";

            assertThrows(CurrencyNotFoundException.class, () -> currencyAnalyticsService.getAverageReferenceRate(startDateString, endDateString, Optional.empty(), code));
        }

        @Test
        @DisplayName("Test Get Average Reference Rate With N/A Exchange Rate - Should Return Zero")
        void testGetAverageReferenceRateUnavailableExchangeRate() {
            String startDateString = "2023-05-19";
            String endDateString = "2023-07-21";
            String code = "CYP";

            Double highestReferenceRate = currencyAnalyticsService.getAverageReferenceRate(startDateString, endDateString, Optional.empty(), code);
            assertNotNull(highestReferenceRate);
            assertEquals(0.0, highestReferenceRate);
        }

        @Test
        @DisplayName("Test Get Average Reference Rate With Valid Arguments - Should Return Average Reference Rate Value Greater Than Zero")
        void testGetAverageReferenceRate() {
            String startDateString = "2023-05-19";
            String endDateString = "2023-07-21";
            String code = "USD";

            Double highestReferenceRate = currencyAnalyticsService.getAverageReferenceRate(startDateString, endDateString, Optional.empty(), code);
            assertNotNull(highestReferenceRate);
            assertTrue(highestReferenceRate > 0);
        }

    }

}