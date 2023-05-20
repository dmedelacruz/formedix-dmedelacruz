package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;

import java.util.Date;
import java.util.Map;

public interface ExchangeRateWriteService {
    void saveExchangeRate(Map<Date, Map<String, CurrencyRate>> exchangeRateMap);
}
