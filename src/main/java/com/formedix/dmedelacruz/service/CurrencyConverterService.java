package com.formedix.dmedelacruz.service;

import java.util.Optional;

public interface CurrencyConverterService {

    Double convertCurrencyAmount(String dateString, Optional<String> dateFormat, String sourceCurrency, String targetCurrency, Double amount);

}
