package com.formedix.dmedelacruz.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ExchangeRate {

    private static ExchangeRate INSTANCE = null;

    private static final SortedMap<Date, Map<String, CurrencyRate>> exchangeRateMap = new TreeMap<>();

    public static SortedMap<Date, Map<String, CurrencyRate>> getExchangeRateMap() {
        return exchangeRateMap;
    }
}
