package com.formedix.dmedelacruz.service;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.data.ExchangeRateRepository;
import com.formedix.dmedelacruz.exception.InvalidDateRangeException;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
class ExchangeRateReadServiceImpl implements ExchangeRateReadService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Set<CurrencyRate> getExchangeRates(String dateString, Optional<String> dateFormat) {
        LocalDate date = dateFormat.map(s -> DateUtil.parseDate(dateString, s)).orElseGet(() -> DateUtil.parseDate(dateString));
        Map<String, CurrencyRate> exchangeRates = exchangeRateRepository.getExchangeRatesRange(date);
        return new HashSet<>(exchangeRates.values());
    }

    @Override
    public Map<LocalDate, Map<String, CurrencyRate>> getExchangeRatesRange(String startDateString, String endDateString, Optional<String> dateFormat) {
        LocalDate startDate = dateFormat.map(s -> DateUtil.parseDate(startDateString, s)).orElseGet(() -> DateUtil.parseDate(startDateString));
        LocalDate endDate = dateFormat.map(s -> DateUtil.parseDate(endDateString, s)).orElseGet(() -> DateUtil.parseDate(endDateString));

        if(startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(ErrorCode.REQ_004);
        }

        return exchangeRateRepository.getExchangeRatesRange(startDate, endDate);
    }

    @Override
    public CurrencyRate getCurrencyRate(String dateString, Optional<String> dateFormat, String code) {
        LocalDate date = dateFormat.map(s -> DateUtil.parseDate(dateString, s)).orElseGet(() -> DateUtil.parseDate(dateString));
        return exchangeRateRepository.getCurrencyRate(date, code);
    }
}
