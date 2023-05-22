package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;

import java.time.LocalDate;
import java.util.Map;

public interface ExchangeRateWriteService {
    void saveExchangeRate(Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMap);
}
