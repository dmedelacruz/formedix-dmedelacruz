package com.formedix.dmedelacruz.data;

import com.formedix.dmedelacruz.exception.CurrencyNotFoundException;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

@Service
class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    @Override
    public void insertData(Map<Date, Map<String, CurrencyRate>> exchangeRateMap) {
        Map<Date, Map<String, CurrencyRate>> exchangeRateMapData = ExchangeRate.getExchangeRateMap();
        exchangeRateMapData.putAll(exchangeRateMap);
    }

    @Override
    public Map<String, CurrencyRate> getExchangeRates(Date date) {
        Map<Date, Map<String, CurrencyRate>> exchangeRateMap = ExchangeRate.getExchangeRateMap();
        if(exchangeRateMap.containsKey(date)) {
            return exchangeRateMap.get(date);
        } else {
            throw new DataNotFoundException();
        }
    }

    @Override
    public Map<Date, Map<String, CurrencyRate>> getExchangeRates(Date startDate, Date endDate) {
        SortedMap<Date, Map<String, CurrencyRate>> exchangeRateMap = ExchangeRate.getExchangeRateMap();
        return exchangeRateMap.subMap(startDate, endDate);
    }

    @Override
    public CurrencyRate getCurrencyRate(Date date, String code) {
        Map<String, CurrencyRate> exchangeRateMap = ExchangeRate.getExchangeRateMap().get(date);
        if(exchangeRateMap.containsKey(code)) {
            return exchangeRateMap.get(code);
        } else {
            throw new CurrencyNotFoundException();
        }
    }

}
