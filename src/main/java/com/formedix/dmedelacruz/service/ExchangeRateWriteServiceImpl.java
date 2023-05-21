package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.data.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
class ExchangeRateWriteServiceImpl implements ExchangeRateWriteService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public void saveExchangeRate(Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMap) {
        exchangeRateRepository.insertData(exchangeRateMap);
    }
}
