package com.formedix.dmedelacruz.controller;

import com.formedix.dmedelacruz.dto.ErrorDetail;
import com.formedix.dmedelacruz.dto.Response;
import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.exception.DataNotFoundException;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.exception.UnsupportedFileTypeException;
import com.formedix.dmedelacruz.fileprocessor.FileProcessorFactory;
import com.formedix.dmedelacruz.fileprocessor.FileType;
import com.formedix.dmedelacruz.service.CurrencyAnalyticsService;
import com.formedix.dmedelacruz.service.CurrencyConverterService;
import com.formedix.dmedelacruz.service.ExchangeRateReadService;
import com.formedix.dmedelacruz.validation.NotBlankPositive;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Load Exchange Rate Data From File. Must Not Be Empty and Currently Only Supports CSV")
    @PostMapping(value = "/load", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Map<String, String>>> loadData(
            @Parameter(description = "File to be uploaded")
                @RequestPart("file") final MultipartFile file
    ) {

        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if(filenameExtension == null) {
            throw new UnsupportedFileTypeException(ErrorCode.FILE_002, null);
        }
        try {
            FileProcessorFactory.getFileProcessor(FileType.valueOfIgnoreCase(filenameExtension)).processData(file);
            return ResponseEntity.ok(Response.<Map<String, String>>builder().content(Map.of("status", "Success")).build());
        } catch (IllegalArgumentException i) {
            throw new UnsupportedFileTypeException(ErrorCode.FILE_002, filenameExtension);
        }
    }

    @Operation(summary = "Get A List of Exchange Rates For A Given Date")
    @GetMapping("/rates")
    public ResponseEntity<Response<Set<CurrencyRate>>> getExchangeRateForDate(
            @Parameter(description = "Specific Date For Exchange Rate Values", example = "yyyy-MM-dd")
                @RequestParam(value = "date", required = false)  @NotBlank String date,
            @Parameter(description = "Date Format Of Provided Date", allowEmptyValue = true, example = "yyyy-MM-dd")
                @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat
    ) {
        try {
            Set<CurrencyRate> exchangeRates = exchangeRateReadService.getExchangeRates(date, dateFormat);
            return ResponseEntity.ok(Response.<Set<CurrencyRate>>builder().content(exchangeRates).build());
        } catch (DataNotFoundException e) {
            ErrorCode errorCode = e.getErrorCode();
            ErrorDetail errorDetail = new ErrorDetail(errorCode, errorCode.getMessage(), errorCode.getDetails());
            return ResponseEntity.ok(Response.<Set<CurrencyRate>>builder().content(Set.of()).errors(List.of(errorDetail)).build());
        }
    }

    @Operation(summary = "Convert A Given Amount From A Specific Currency To A Target Currency")
    @GetMapping("/convert")
    public ResponseEntity<Response<Map<String, Double>>> convertCurrency(
            @Parameter(description = "Specific Date For Exchange Rate Value", example = "yyyy-MM-dd")
                @RequestParam(value = "date", required = false) @NotBlank String date,
            @Parameter(description = "Date Format Of Provided Date", allowEmptyValue = true, example = "yyyy-MM-dd")
                @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @Parameter(description = "Currency Of Given Amount", example = "USD")
                @RequestParam(value = "sourceCurrency", required = false) @NotBlank String sourceCurrency,
            @Parameter(description = "Resulting Currency Of Resulting Amount", example = "JPY")
                @RequestParam(value = "targetCurrency", required = false) @NotBlank String targetCurrency,
            @Parameter(description = "Amount To Be Converted. Must Be A Positive Value", example = "100")
                @RequestParam(value = "amount", required = false) @NotBlankPositive Double amount
    ) {
        Double convertedAmount = currencyConverterService.convertCurrencyAmount(date, dateFormat, sourceCurrency, targetCurrency, amount);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("convertedAmount", convertedAmount)).build());
    }

    @Operation(summary = "Get The Highest Exchange Rate Of A Specific Currency For A Given Date Range")
    @GetMapping("/analytics/max")
    public ResponseEntity<Response<Map<String, Double>>> getHighestReferenceRate(
            @Parameter(description = "Start Date. Must Be Greater Than End Date", example = "yyyy-MM-dd")
                @RequestParam(value = "startDate", required = false)  @NotBlank String startDate,
            @Parameter(description = "End Date. Must Be Greater Than Start Date", example = "yyyy-MM-dd")
                @RequestParam(value = "endDate", required = false)  @NotBlank String endDate,
            @Parameter(description = "Date Format Of Provided Date", allowEmptyValue = true, example = "yyyy-MM-dd")
                @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @Parameter(description = "Specific Currency In Question", example = "USD")
                @RequestParam(value = "currency", required = false)  @NotBlank String currency
    ) {
        Double highestReferenceRate = currencyAnalyticsService.getHighestReferenceRate(startDate, endDate, dateFormat, currency);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("highestReferenceRate", highestReferenceRate)).build());

    }

    @Operation(summary = "Get The Average Exchange Rate Of A Specific Currency For A Given Date Range")
    @GetMapping("/analytics/average")
    public ResponseEntity<Response<Map<String, Double>>> getAverageReferenceRate(
            @Parameter(description = "Start Date. Must Be Greater Than End Date", example = "yyyy-MM-dd")
            @RequestParam(value = "startDate", required = false)  @NotBlank String startDate,
            @Parameter(description = "End Date. Must Be Greater Than Start Date", example = "yyyy-MM-dd")
            @RequestParam(value = "endDate", required = false)  @NotBlank String endDate,
            @Parameter(description = "Date Format Of Provided Date", allowEmptyValue = true, example = "yyyy-MM-dd")
            @RequestParam(value = "dateFormat", required = false) Optional<String> dateFormat,
            @Parameter(description = "Specific Currency In Question", example = "USD")
            @RequestParam(value = "currency", required = false)  @NotBlank String currency
    ) {
        Double averageReferenceRate = currencyAnalyticsService.getAverageReferenceRate(startDate, endDate, dateFormat, currency);
        return ResponseEntity.ok(Response.<Map<String, Double>>builder().content(Map.of("averageReferenceRate", averageReferenceRate)).build());
    }

}
