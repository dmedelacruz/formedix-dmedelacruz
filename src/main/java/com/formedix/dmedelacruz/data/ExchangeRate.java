package com.formedix.dmedelacruz.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ExchangeRate {
    private static final SortedMap<LocalDate, Map<String, CurrencyRate>> exchangeRateMap = new TreeMap<>();
    public static SortedMap<LocalDate, Map<String, CurrencyRate>> getExchangeRateMap() {
        return exchangeRateMap;
    }
}
