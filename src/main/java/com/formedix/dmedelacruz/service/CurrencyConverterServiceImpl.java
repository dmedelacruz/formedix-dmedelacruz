package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private final ExchangeRateReadService exchangeRateReadService;

    @Override
    public Double convertCurrencyAmount(String dateString, Optional<String> dateFormat, String sourceCurrency, String targetCurrency, Double amount) {
        CurrencyRate sourceCurrencyRate = exchangeRateReadService.getCurrencyRate(dateString, dateFormat, sourceCurrency);
        CurrencyRate targetCurrencyRate = exchangeRateReadService.getCurrencyRate(dateString, dateFormat, targetCurrency);
        return amount * (targetCurrencyRate.exchangeRate() / sourceCurrencyRate.exchangeRate());
    }

}
