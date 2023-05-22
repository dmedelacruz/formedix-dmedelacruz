package com.formedix.dmedelacruz.service;

import java.util.Optional;
public interface CurrencyAnalyticsService {

    Double getHighestReferenceRate(String startDateString, String endDateString, Optional<String> dateFormat, String code);
    Double getAverageReferenceRate(String startDateString, String endDateString, Optional<String> dateFormat, String code);
}
