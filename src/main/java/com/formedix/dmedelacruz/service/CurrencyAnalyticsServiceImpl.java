package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.exception.CurrencyNotFoundException;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
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
        Map<LocalDate, Map<String, CurrencyRate>> exchangeRatesRange = getExchangeRatesRange(startDateString, endDateString, dateFormat, code);
        Double highestReference = 0.0;
        for (Map<String, CurrencyRate> rateMap : exchangeRatesRange.values()) {
            Double rate = rateMap.get(code).exchangeRate();
            highestReference = rate > highestReference ? rate : highestReference;
        }
        return highestReference;
    }

    @Override
    public Double getAverageReferenceRate(String startDateString, String endDateString, Optional<String> dateFormat, String code) {
        Map<LocalDate, Map<String, CurrencyRate>> exchangeRatesRange = getExchangeRatesRange(startDateString, endDateString, dateFormat, code);
        Double totalReference = 0.0;
        for (Map<String, CurrencyRate> rateMap : exchangeRatesRange.values()) {
            totalReference += rateMap.get(code).exchangeRate();
        }
        return totalReference / exchangeRatesRange.values().size();
    }

    private Map<LocalDate, Map<String, CurrencyRate>> getExchangeRatesRange(String startDateString, String endDateString, Optional<String> dateFormat, String code) {
        Map<LocalDate, Map<String, CurrencyRate>> exchangeRatesRange = exchangeRateReadService.getExchangeRatesRange(startDateString, endDateString, dateFormat);
        exchangeRatesRange.values().stream().findFirst().ifPresent(map -> {
            if(!map.containsKey(code)) {
                throw new CurrencyNotFoundException(ErrorCode.CURR_001, code);
            }
        });
        return exchangeRatesRange;
    }
}
