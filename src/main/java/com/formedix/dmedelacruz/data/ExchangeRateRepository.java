package com.formedix.dmedelacruz.data;

import java.util.Date;
import java.util.Map;

public interface ExchangeRateRepository {
    void insertData(Map<Date, Map<String, CurrencyRate>> exchangeRateMap);
    Map<String, CurrencyRate> getExchangeRates(Date date);
    Map<Date, Map<String, CurrencyRate>> getExchangeRates(Date startDate, Date endDate);
    CurrencyRate getCurrencyRate(Date date, String code);
}
