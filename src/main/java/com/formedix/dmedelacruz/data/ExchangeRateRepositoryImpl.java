package com.formedix.dmedelacruz.data;

import com.formedix.dmedelacruz.exception.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.SortedMap;

@Service
class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    @Override
    public void insertData(Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMap) {
        Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMapData = ExchangeRate.getExchangeRateMap();
        exchangeRateMapData.putAll(exchangeRateMap);
    }

    @Override
    public Map<String, CurrencyRate> getExchangeRates(LocalDate date) {
        Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMap = ExchangeRate.getExchangeRateMap();
        if(exchangeRateMap.containsKey(date)) {
            return exchangeRateMap.get(date);
        } else {
            throw new DataNotFoundException(ErrorCode.DATA_001, ErrorMessage.NO_DATA_FOR_DATE);
        }
    }

    @Override
    public Map<LocalDate, Map<String, CurrencyRate>> getExchangeRates(LocalDate startDate, LocalDate endDate) {
        SortedMap<LocalDate, Map<String, CurrencyRate>> exchangeRateMap = ExchangeRate.getExchangeRateMap();
        SortedMap<LocalDate, Map<String, CurrencyRate>> subMap = exchangeRateMap.subMap(startDate, endDate.plusDays(1));
        if(subMap.isEmpty()) {
            throw new DataNotFoundException(ErrorCode.DATA_002, ErrorMessage.NO_DATA_FOR_DATE_RANGE);
        }
        return subMap;
    }

    @Override
    public CurrencyRate getCurrencyRate(LocalDate date, String code) {
        Map<String, CurrencyRate> exchangeRateMap = ExchangeRate.getExchangeRateMap().get(date);

        if(exchangeRateMap == null || exchangeRateMap.isEmpty()) {
            throw new DataNotFoundException(ErrorCode.DATA_001, ErrorMessage.NO_DATA_FOR_DATE);
        }

        if(exchangeRateMap.containsKey(code)) {
            if(exchangeRateMap.get(code).exchangeRate().equals(0.0)) {
                throw new ExchangeRateUnavailableException(ErrorCode.CURR_002, ErrorMessage.NO_EXCHANGE_RATE_AVAILABLE);
            }
            return exchangeRateMap.get(code);
        } else {
            throw new CurrencyNotFoundException(ErrorCode.CURR_001, ErrorMessage.CURRENCY_NOT_FOUND);
        }
    }

}
