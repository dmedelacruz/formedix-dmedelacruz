package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ExchangeRateReadService {

    Set<CurrencyRate> getExchangeRates(String date, Optional<String> dateFormat);
    Map<LocalDate, Map<String, CurrencyRate>> getExchangeRatesRange(String startDate, String endDate, Optional<String> dateFormat);
    CurrencyRate getCurrencyRate(String dateString, Optional<String> dateFormat, String code);
}
