package com.formedix.dmedelacruz.controller;

import com.formedix.dmedelacruz.dto.ErrorDetail;
import com.formedix.dmedelacruz.dto.Response;
import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.exception.constant.ErrorMessage;
import com.formedix.dmedelacruz.exception.UnsupportedFileTypeException;
import com.formedix.dmedelacruz.fileprocessor.FileProcessorFactory;
import com.formedix.dmedelacruz.fileprocessor.FileType;
import com.formedix.dmedelacruz.service.CurrencyAnalyticsService;
import com.formedix.dmedelacruz.service.CurrencyConverterService;
import com.formedix.dmedelacruz.service.ExchangeRateReadService;
import com.formedix.dmedelacruz.validation.NotBlankPositive;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/forex")
@RequiredArgsConstructor
@Validated
public class ExchangeRateController {

    private final ExchangeRateReadService exchangeRateReadService;
    private final CurrencyConverterService currencyConverterService;
    private final CurrencyAnalyticsService currencyAnalyticsService;

    @PostMapping("/load")
    public ResponseEntity<Response<Map<String, String>>> loadData(
            @RequestParam("file") MultipartFile file
    ) {

        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if(filenameExtension == null) {
            throw new UnsupportedFileTypeException(ErrorCode.FILE_002, ErrorMessage.UNKNOWN_FILE_TYPE, null);
        }
        try {
            FileProcessorFactory.getFileProcessor(FileType.valueOfIgnoreCase(filenameExtension)).processData(file);
            return ResponseEntity.ok(Response.<Map<String, String>>builder().content(Map.of("status", "Success")).build());
        } catch (IllegalArgumentException i) {
            throw new UnsupportedFileTypeException(ErrorCode.FILE_002, ErrorMessage.UNKNOWN_FILE_TYPE, filenameExtension);
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<Response<Set<CurrencyRate>>> getExchangeRateForDate(
            @RequestParam(value = "date", required = false)  @NotBlank String date,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat
    ) {
        try {
            Set<CurrencyRate> exchangeRates = exchangeRateReadService.getExchangeRates(date, dateFormat);
            return ResponseEntity.ok(Response.<Set<CurrencyRate>>builder().content(exchangeRates).build());
        } catch (DataNotFoundException e) {
            ErrorCode errorCode = e.getErrorCode();
            ErrorMessage errorMessage = e.getErrorMessage();
            ErrorDetail errorDetail = new ErrorDetail(errorCode, errorMessage.getMessage(), errorMessage.getDetails());
            return ResponseEntity.ok(Response.<Set<CurrencyRate>>builder().content(Set.of()).errors(List.of(errorDetail)).build());
        }
    }

    @GetMapping("/convert")
    public ResponseEntity<Response<Map<String, Double>>> convertCurrency(
            @RequestParam(value = "date", required = false) @NotBlank String date,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam(value = "sourceCurrency", required = false) @NotBlank String sourceCurrency,
            @RequestParam(value = "targetCurrency", required = false) @NotBlank String targetCurrency,
            @RequestParam(value = "amount", required = false) @NotBlankPositive Double amount
    ) {
        Double convertedAmount = currencyConverterService.convertCurrencyAmount(date, dateFormat, sourceCurrency, targetCurrency, amount);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("convertedAmount", convertedAmount)).build());
    }

    @GetMapping("/analytics/max")
    public ResponseEntity<Response<Map<String, Double>>> getHighestReferenceRate(
            @RequestParam(value = "startDate", required = false)  @NotBlank String startDate,
            @RequestParam(value = "endDate", required = false)  @NotBlank String endDate,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam(value = "currency", required = false)  @NotBlank String currency
    ) {
        Double highestReferenceRate = currencyAnalyticsService.getHighestReferenceRate(startDate, endDate, dateFormat, currency);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("highestReferenceRate", highestReferenceRate)).build());

    }

    @GetMapping("/analytics/average")
    public ResponseEntity<Response<Map<String, Double>>> getAverageReferenceRate(
            @RequestParam(value = "startDate", required = false)  @NotBlank String startDate,
            @RequestParam(value = "endDate", required = false)  @NotBlank String endDate,
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @RequestParam(value = "currency", required = false)  @NotBlank String currency
    ) {
        Double averageReferenceRate = currencyAnalyticsService.getAverageReferenceRate(startDate, endDate, dateFormat, currency);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("averageReferenceRate", averageReferenceRate)).build());
    }

}
