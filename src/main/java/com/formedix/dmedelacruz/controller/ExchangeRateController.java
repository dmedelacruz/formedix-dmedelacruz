package com.formedix.dmedelacruz.controller;

import com.formedix.dmedelacruz.dao.ErrorDetail;
import com.formedix.dmedelacruz.dao.Response;
import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
import com.formedix.dmedelacruz.exception.ErrorCode;
import com.formedix.dmedelacruz.exception.ErrorMessage;
import com.formedix.dmedelacruz.exception.UnsupportedFileTypeException;
import com.formedix.dmedelacruz.fileprocessor.FileProcessorFactory;
import com.formedix.dmedelacruz.fileprocessor.FileType;
import com.formedix.dmedelacruz.service.CurrencyAnalyticsService;
import com.formedix.dmedelacruz.service.CurrencyConverterService;
import com.formedix.dmedelacruz.service.ExchangeRateReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
    public ResponseEntity<Response<Map<String, String>>> loadData(@RequestParam("file") MultipartFile file) {

        try {
            String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            if(filenameExtension == null) {
                throw new UnsupportedFileTypeException(ErrorCode.FILE_002, ErrorMessage.UNKNOWN_FILE_TYPE);
            }
            FileProcessorFactory.getFileProcessor(FileType.valueOfIgnoreCase(filenameExtension)).processData(file);
            return ResponseEntity.ok(Response.<Map<String, String>>builder().content(Map.of("status", "Success")).build());
        } catch (IllegalArgumentException i) {
            throw new UnsupportedFileTypeException(ErrorCode.FILE_002, ErrorMessage.UNKNOWN_FILE_TYPE);
        }
    }

    @GetMapping
    public ResponseEntity<Response<Set<CurrencyRate>>> getExchangeRateForData(
            @RequestParam("date") String date,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat
    ) {

        try {
            //TODO Maybe we can add sorting and pagination
            Set<CurrencyRate> exchangeRates = exchangeRateReadService.getExchangeRates(date, dateFormat);
            return ResponseEntity.ok(Response.<Set<CurrencyRate>>builder().content(exchangeRates).build());
        } catch (DataNotFoundException e) {
            ErrorCode errorCode = e.getErrorCode();
            ErrorMessage errorMessage = e.getErrorMessage();
            ErrorDetail errorDetail = new ErrorDetail(errorCode, errorMessage.getMessage(), errorMessage.getDetails());
            return ResponseEntity.ok(Response.<Set<CurrencyRate>>builder().content(Set.of()).error(errorDetail).build());
        }
    }

    @GetMapping("/convert")
    public ResponseEntity<Response<Map<String, Double>>> convertCurrency(
            @RequestParam("date") String date,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam("sourceCurrency") String sourceCurrency,
            @RequestParam("targetCurrency") String targetCurrency,
            @RequestParam("amount") Double amount
    ) {
        Double convertedAmount = currencyConverterService.convertCurrencyAmount(date, dateFormat, sourceCurrency, targetCurrency, amount);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("convertedAmount", convertedAmount)).build());
    }

    @GetMapping("/analytics/max")
    public ResponseEntity<Response<Map<String, Double>>> getHighestReferenceRate(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam("currency") String currency
    ) {
        Double highestReferenceRate = currencyAnalyticsService.getHighestReferenceRate(startDateString, endDateString, dateFormat, currency);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("highestReferenceRate", highestReferenceRate)).build());

    }

    @GetMapping("/analytics/average")
    public ResponseEntity<Response<Map<String, Double>>> getAverageReferenceRate(
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam("currency") String currency
    ) {
        Double averageReferenceRate = currencyAnalyticsService.getAverageReferenceRate(startDateString, endDateString, dateFormat, currency);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("averageReferenceRate", averageReferenceRate)).build());
    }

}
