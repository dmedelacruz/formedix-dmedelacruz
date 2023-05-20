package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.data.ExchangeRateRepository;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
import com.formedix.dmedelacruz.exception.DateFormatException;
import com.formedix.dmedelacruz.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
class ExchangeRateReadServiceImpl implements ExchangeRateReadService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Set<CurrencyRate> getExchangeRates(String dateString, Optional<String> dateFormat) {
        Date date = dateFormat.isPresent() ? DateUtil.parseDate(dateString, dateFormat.get()) : DateUtil.parseDate(dateString);
        Map<String, CurrencyRate> exchangeRates = exchangeRateRepository.getExchangeRates(date);
        return new HashSet<>(exchangeRates.values());
    }

    @Override
    public Map<Date, Map<String, CurrencyRate>> getExchangeRatesRange(String startDateString, String endDateString, Optional<String> dateFormat) {
        Date startDate = dateFormat.isPresent() ? DateUtil.parseDate(startDateString, dateFormat.get()) : DateUtil.parseDate(startDateString);
        Date endDate = dateFormat.isPresent() ? DateUtil.parseDate(endDateString, dateFormat.get()) : DateUtil.parseDate(endDateString);
        return exchangeRateRepository.getExchangeRates(startDate, endDate);
    }

    @Override
    public CurrencyRate getCurrencyRate(String dateString, Optional<String> dateFormat, String code) {
        Date date = dateFormat.isPresent() ? DateUtil.parseDate(dateString, dateFormat.get()) : DateUtil.parseDate(dateString);
        return exchangeRateRepository.getCurrencyRate(date, code);
    }
}
