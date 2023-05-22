package com.formedix.dmedelacruz.data;

import java.time.LocalDate;
import java.util.Map;

public interface ExchangeRateRepository {
    void insertData(Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMap);
    Map<String, CurrencyRate> getExchangeRatesRange(LocalDate date);
    Map<LocalDate, Map<String, CurrencyRate>> getExchangeRatesRange(LocalDate startDate, LocalDate endDate);
    CurrencyRate getCurrencyRate(LocalDate date, String code);
}
