package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
import com.formedix.dmedelacruz.exception.ErrorCode;
import com.formedix.dmedelacruz.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
class CurrencyAnalyticsServiceImpl implements CurrencyAnalyticsService {

    private final ExchangeRateReadService exchangeRateReadService;

    @Override
    public Double getHighestReferenceRate(String startDateString, String endDateString, Optional<String> dateFormat, String code) {
        Map<LocalDate, Map<String, CurrencyRate>> exchangeRatesRange = exchangeRateReadService.getExchangeRatesRange(startDateString, endDateString, dateFormat);
        Double highestReference = 0.0;
        for (Map<String, CurrencyRate> rateMap : exchangeRatesRange.values()) {
            Double rate = rateMap.get(code).exchangeRate();
            highestReference = rate > highestReference ? rate : highestReference;
        }
        return highestReference;
    }

    @Override
    public Double getAverageReferenceRate(String startDateString, String endDateString, Optional<String> dateFormat, String code) {
        Map<LocalDate, Map<String, CurrencyRate>> exchangeRatesRange = exchangeRateReadService.getExchangeRatesRange(startDateString, endDateString, dateFormat);
        Double totalReference = 0.0;
        for (Map<String, CurrencyRate> rateMap : exchangeRatesRange.values()) {
            totalReference += rateMap.get(code).exchangeRate();
        }
        return totalReference / exchangeRatesRange.values().size();
    }
}
