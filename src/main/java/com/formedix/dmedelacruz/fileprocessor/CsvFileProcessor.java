package com.formedix.dmedelacruz.fileprocessor;

import com.formedix.dmedelacruz.data.CurrencyRate;
import com.formedix.dmedelacruz.service.ExchangeRateWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
class CsvFileProcessor implements FileProcessor {

    private final ExchangeRateWriteService exchangeRateWriteService;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Map<Integer, String> headerMap = new HashMap<>();

    @Override
    public void processData(MultipartFile file) {

        Map<Date, Map<String, CurrencyRate>> exchangeRateMap = new TreeMap<>();
        try(Scanner scanner = new Scanner(file.getInputStream())) {
            processHeaderMap(scanner.nextLine());
            while (scanner.hasNextLine()) {
                processRecordLine(exchangeRateMap, scanner.nextLine());
            }
            exchangeRateWriteService.saveExchangeRate(exchangeRateMap);
        } catch (IOException ioe) {
            //TODO
        } catch (ParseException pe) {
            //TODO
        }
    }

    private void processHeaderMap(String headersLine) {
        String[] headers = headersLine.split(",");
        for(int x = 1; x < headers.length; x++) {
            headerMap.put(x, headers[x]);
        }
    }

    private void processRecordLine(Map<Date, Map<String, CurrencyRate>> exchangeRateMap, String recordLine) throws ParseException {

        String[] values = recordLine.split(",");
        String date = values[0];

        if (exchangeRateMap.containsKey(transformToDate(date))) {
            return;
        }

        Map<String, CurrencyRate> currenciesExchangeRate = new HashMap<>();
        exchangeRateMap.put(transformToDate(date), currenciesExchangeRate);

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

    private Date transformToDate(String dateString) throws ParseException {
        return simpleDateFormat.parse(dateString);
    }

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }
}
