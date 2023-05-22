package com.formedix.dmedelacruz.fileprocessor;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.exception.constant.ErrorMessage;
import com.formedix.dmedelacruz.exception.FileReadException;
import com.formedix.dmedelacruz.service.ExchangeRateWriteService;
import com.formedix.dmedelacruz.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
class CsvFileProcessor implements FileProcessor {

    private final ExchangeRateWriteService exchangeRateWriteService;

    private final Map<Integer, String> headerMap = new HashMap<>();

    @Override
    public void processData(MultipartFile file) {
        Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMap = new TreeMap<>();
        try(Scanner scanner = new Scanner(file.getInputStream())) {
            processHeaderMap(scanner.nextLine());
            while (scanner.hasNextLine()) {
                processRecordLine(exchangeRateMap, scanner.nextLine());
            }
            exchangeRateWriteService.saveExchangeRate(exchangeRateMap);
        } catch (IOException e) {
            throw new FileReadException(ErrorCode.REQ_004, ErrorMessage.FILE_READ_ERROR);
        } catch (NoSuchElementException n) {
            throw new FileReadException(ErrorCode.REQ_005, ErrorMessage.FILE_EMPTY_ERROR);
        }
    }

    private void processHeaderMap(String headersLine) {
        String[] headers = headersLine.split(",");
        for(int x = 1; x < headers.length; x++) {
            headerMap.put(x, headers[x]);
        }
    }

    private void processRecordLine(Map<LocalDate, Map<String, CurrencyRate>> exchangeRateMap, String recordLine) {

        String[] values = recordLine.split(",");
        String dateString = values[0];
        LocalDate date = DateUtil.parseDate(dateString);


        if (exchangeRateMap.containsKey(date)) {
            return;
        }

        Map<String, CurrencyRate> currenciesExchangeRate = new HashMap<>();
        exchangeRateMap.put(date, currenciesExchangeRate);

        for(int x = 1; x < values.length; x++) {
            CurrencyRate currencyRate;
            try {
                Double exchangeRate = Double.valueOf(values[x]);
                currencyRate = new CurrencyRate(headerMap.get(x), exchangeRate);
            } catch (NumberFormatException nfe) {
                currencyRate = new CurrencyRate(headerMap.get(x), 0.0);
            }
            currenciesExchangeRate.put(headerMap.get(x), currencyRate);
        }

    }

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }
}
