package com.formedix.dmedelacruz.controller;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.fileprocessor.FileProcessorFactory;
import com.formedix.dmedelacruz.fileprocessor.FileType;
import com.formedix.dmedelacruz.service.CurrencyAnalyticsService;
import com.formedix.dmedelacruz.service.CurrencyConverterService;
import com.formedix.dmedelacruz.service.ExchangeRateReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateReadService exchangeRateReadService;
    private final CurrencyConverterService currencyConverterService;
    private final CurrencyAnalyticsService currencyAnalyticsService;

    //TODO ADD validations on requests

    @PostMapping
    public ResponseEntity<Map<String, String>> loadData(@RequestParam("file") MultipartFile file) {
        file.getOriginalFilename(); //TODO get FileType from File Extension
        FileProcessorFactory.getFileProcessor(FileType.CSV).processData(file);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @GetMapping
    public Set<CurrencyRate> getExchangeRateForData(
            @RequestParam("date") String date,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat
    ) {

        //Maybe we can add sorting and pagination
        Set<CurrencyRate> exchangeRates = exchangeRateReadService.getExchangeRates(date, dateFormat);
        return exchangeRates;
    }

    @GetMapping("/convert")
    public Double convertCurrency(
            @RequestParam("date") String date,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam("sourceCurrency") String sourceCurrency,
            @RequestParam("targetCurrency") String targetCurrency,
            @RequestParam("amount") Double amount
    ) {
        return currencyConverterService.convertCurrencyAmount(date, dateFormat, sourceCurrency, targetCurrency, amount);
    }

    @GetMapping("/analytics/highest-reference-rate")
    public Double getHighestReferenceRate(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam("currency") String currency
    ) {
        return currencyAnalyticsService.getHighestReferenceRate(startDateString, endDateString, dateFormat, currency);
    }

    @GetMapping("/analytics/average-reference-rate")
    public Double getAverageReferenceRate(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam("currency") String currency
    ) {
        return currencyAnalyticsService.getAverageReferenceRate(startDateString, endDateString, dateFormat, currency);
    }

}
