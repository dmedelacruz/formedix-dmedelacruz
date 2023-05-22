package com.formedix.dmedelacruz.controller;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.io.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String root = "/forex";

    @BeforeEach
    public void init() throws IOException {
        File file = ResourceUtils.getFile("classpath:eurofxref-hist.csv");
        try(FileInputStream inputStream = new FileInputStream(file)) {
            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "eurofxref-hist.csv", null, inputStream);
            MockMultipartHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.multipart(HttpMethod.POST, "/forex/load").file(mockMultipartFile);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nested
    class TestLoadData {

        private final String URL_PATH = root + "/load";

        @Test
        @DisplayName("Test Without File Field - Should Return 400 With Errors")
        void testMissingFileField() throws Exception {
            MockMultipartFile mockMultipartFile = new MockMultipartFile("NON", InputStream.nullInputStream());
            MockMultipartHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.multipart(HttpMethod.POST, URL_PATH).file(mockMultipartFile);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.REQ_001.getMessage(), "file")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.REQ_001.getDetails()));
        }

        @Test
        @DisplayName("Test Without File Value - Should Return 400 With Errors")
        void testNullFile() throws Exception {
            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", null, null, InputStream.nullInputStream());
            MockMultipartHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.multipart(HttpMethod.POST, URL_PATH).file(mockMultipartFile);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.FILE_002.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.FILE_002.getMessage(), null)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.FILE_002.getDetails()));
        }

        @Test
        @DisplayName("Test With Unsupported File - Should Return 400 With Errors")
        void testUnsupportedFile() throws FileNotFoundException {
            File file = ResourceUtils.getFile("classpath:test.pdf");
            try(FileInputStream inputStream = new FileInputStream(file)) {
                MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.pdf", null, inputStream);
                MockMultipartHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.multipart(HttpMethod.POST, URL_PATH).file(mockMultipartFile);
                mockMvc.perform(servletRequest)
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.FILE_001.toString()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.FILE_001.getMessage(), "PDF")))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.FILE_001.getDetails()));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("Test With Empty File - Should Return 422 With Errors")
        void testEmptyFile() throws FileNotFoundException {
            File file = ResourceUtils.getFile("classpath:empty.csv");
            try(FileInputStream inputStream = new FileInputStream(file)) {
                MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "empty.csv", null, inputStream);
                MockMultipartHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.multipart(HttpMethod.POST, URL_PATH).file(mockMultipartFile);
                mockMvc.perform(servletRequest)
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.FILE_003.toString()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.FILE_003.getMessage())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.FILE_003.getDetails()));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("Test With Valid File - Should Return 200 With Success Status And No Errors")
        void testValidFile() throws FileNotFoundException {
            File file = ResourceUtils.getFile("classpath:eurofxref-hist.csv");
            try(FileInputStream inputStream = new FileInputStream(file)) {
                MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "eurofxref-hist.csv", null, inputStream);
                MockMultipartHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.multipart(HttpMethod.POST, URL_PATH).file(mockMultipartFile);
                mockMvc.perform(servletRequest)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content.status").value("Success"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").doesNotExist());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Nested
    class TestGetExchangeRateForDate {

        private final String URL_PATH = root + "/rates";

        @Test
        @DisplayName("Test Get Exchange Rates For Specific Date With Missing Required Fields - Should Return 422 With Errors")
        void testMissingDateField() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.REQ_001.getMessage(), "date")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.REQ_001.getDetails()));
        }

        @Test
        @DisplayName("Test Get Exchange Rates For Specific Date With Blank Required Fields - Should Return 422 With Errors")
        void testBlankDateFieldValue() throws Exception {
            MockHttpServletRequestBuilder servletRequest1 = MockMvcRequestBuilders.get(URL_PATH).param("date", "");
            mockMvc.perform(servletRequest1)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.REQ_001.getMessage(), "date")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.REQ_001.getDetails()));

            MockHttpServletRequestBuilder servletRequest2 = MockMvcRequestBuilders.get(URL_PATH).param("date", "    ");
            mockMvc.perform(servletRequest2)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.REQ_001.getMessage(), "date")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.REQ_001.getDetails()));
        }

        @Test
        @DisplayName("Test Get Exchange Rates For Specific Date With Invalid Date Value - Should Return 200 With Empty Array")
        void testDataNotFound() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).param("date", "2040-05-25");
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.DATA_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(ErrorCode.DATA_001.getMessage()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.DATA_001.getDetails()));
        }

        @Test
        @DisplayName("Test Get Exchange Rates For Specific Date With Valid Date Value - Should Return 200 With Non-Empty Array")
        void testDataHasResults() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).param("date", "2015-05-19");
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").doesNotExist());
        }

    }

    @Nested
    class TestConvertCurrency {

        private final String URL_PATH = root + "/convert";

        @Test
        @DisplayName("Test Convert Currency Amount With Missing Required Fields - Should Return 422 With Errors")
        void testMissingRequiredFields() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(4))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());
       }

        @Test
        @DisplayName("Test Convert Currency Amount With Blank Required Fields - Should Return 422 With Errors")
        void testBlankRequiredFieldValues() throws Exception {
            MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
            params1.add("targetCurrency", "");
            params1.add("sourceCurrency", "");
            params1.add("date", "");
            params1.add("amount", "");

            MockHttpServletRequestBuilder servletRequest1 = MockMvcRequestBuilders.get(URL_PATH).params(params1);
            mockMvc.perform(servletRequest1)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(4))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());

            MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
            params2.add("targetCurrency", "     ");
            params2.add("sourceCurrency", "     ");
            params2.add("date", "     ");
            params2.add("amount", "     ");

            MockHttpServletRequestBuilder servletRequest2 = MockMvcRequestBuilders.get(URL_PATH).params(params2);
            mockMvc.perform(servletRequest2)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(4))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());

        }

        @Test
        @DisplayName("Test Convert Currency Amount With NonNumeric Value - Should Return 400 With Errors")
        void testNotNumericAmountValue() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "JPY");
            params.add("date", "2023-05-19");
            params.add("amount", "amount");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_002.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.REQ_002.getMessage(), "amount")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.REQ_002.getDetails()));
        }

        @Test
        @DisplayName("Test Convert Currency Amount With Negative Value - Should Return 422 With Errors")
        void testNegativeNumericAmountValue() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "JPY");
            params.add("date", "2023-05-19");
            params.add("amount", "-10.0");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_003.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.REQ_003.getMessage(), "amount")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.REQ_003.getDetails()));
        }

        @Test
        @DisplayName("Test Convert Currency Amount With Invalid Date - Should Return 400 With Errors")
        void testIncorrectDate() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "JPY");
            params.add("date", "2024-05-19");
            params.add("amount", "10.0");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.DATA_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.DATA_001.getMessage())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.DATA_001.getDetails()));
        }

        @Test
        @DisplayName("Test Convert Currency Amount With Unavailable Exchange Rate - Should Return 400 With Errors")
        void testUnavailableExchangeRate() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "CYP");
            params.add("date", "2023-05-19");
            params.add("amount", "10.0");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.CURR_002.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.CURR_002.getMessage(), "CYP")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.CURR_002.getDetails()));

        }

        @Test
        @DisplayName("Test Convert Currency Amount With Unknown Currency - Should Return 400 With Errors")
        void testUnknownCurrency() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "UNKNOWN");
            params.add("date", "2023-05-19");
            params.add("amount", "10.0");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.CURR_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.CURR_001.getMessage(), "UNKNOWN")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.CURR_001.getDetails()));

        }

        @Test
        @DisplayName("Test Convert Currency Amount With Valid Arguments - Should Return 200 With Correct Conversion Amount")
        void testConvertSuccess() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "JPY");
            params.add("date", "2023-05-19");
            params.add("amount", "100");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.convertedAmount").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").doesNotExist());
        }

    }

    @Nested
    class TestGetHighestReferenceRate {

        private final String URL_PATH = root + "/analytics/max";

        @Test
        @DisplayName("Test Get Highest Reference Rate With Missing Required Fields - Should Return 422 With Errors")
        void testMissingRequiredFields() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(3))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());
        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With Blank Required Fields - Should Return 422 With Errors")
        void testBlankRequiredFieldValues() throws Exception {
            MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
            params1.add("startDate", "");
            params1.add("endDate", "");
            params1.add("currency", "");

            MockHttpServletRequestBuilder servletRequest1 = MockMvcRequestBuilders.get(URL_PATH).params(params1);
            mockMvc.perform(servletRequest1)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(3))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());

            MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
            params2.add("startDate", "     ");
            params2.add("endDate", "     ");
            params2.add("currency", "     ");

            MockHttpServletRequestBuilder servletRequest2 = MockMvcRequestBuilders.get(URL_PATH).params(params2);
            mockMvc.perform(servletRequest2)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(3))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());

        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With Invalid Date Ranges - Should Return 400 With Errors")
        void testIncorrectDateRange() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2024-05-19");
            params.add("endDate", "2024-07-21");
            params.add("currency", "USD");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.DATA_002.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.DATA_002.getMessage())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.DATA_002.getDetails()));
        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With Start Date After End Date - Should Return 400 With Errors")
        void testInvalidDateRange() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2020-05-19");
            params.add("endDate", "2019-07-21");
            params.add("currency", "USD");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_004.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.REQ_004.getMessage())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.REQ_004.getDetails()));
        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With Unknown Currency - Should Return 400 With Errors")
        void testUnknownCurrency() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2023-05-19");
            params.add("endDate", "2023-05-21");
            params.add("currency", "UNKNOWN");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.CURR_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.CURR_001.getMessage(), "UNKNOWN")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.CURR_001.getDetails()));
        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With Unavailable Exchange Rate - Should Return 200 With Zero Value")
        void testUnavailableExchangeRate() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2023-05-19");
            params.add("endDate", "2023-05-21");
            params.add("currency", "CYP");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.highestReferenceRate").value(0.0))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").doesNotExist());
        }

        @Test
        @DisplayName("Test Get Highest Reference Rate With Valid Request - Should Return 200 With Non-Zero Value")
        void testHighestReferenceRateSuccess() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2023-05-19");
            params.add("endDate", "2023-05-21");
            params.add("currency", "USD");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.highestReferenceRate").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").doesNotExist());
        }

    }

    @Nested
    class TestGetAverageReferenceRate {

        private final String URL_PATH = root + "/analytics/average";

        @Test
        @DisplayName("Test Get Average Reference Rate With Missing Required Fields - Should Return 422 With Errors")
        void testMissingRequiredFields() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(3))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());
        }

        @Test
        @DisplayName("Test Get Average Reference Rate With Blank Required Fields - Should Return 422 With Errors")
        void testBlankRequiredFieldValues() throws Exception {
            MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
            params1.add("startDate", "");
            params1.add("endDate", "");
            params1.add("currency", "");

            MockHttpServletRequestBuilder servletRequest1 = MockMvcRequestBuilders.get(URL_PATH).params(params1);
            mockMvc.perform(servletRequest1)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(3))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());

            MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
            params2.add("startDate", "     ");
            params2.add("endDate", "     ");
            params2.add("currency", "     ");

            MockHttpServletRequestBuilder servletRequest2 = MockMvcRequestBuilders.get(URL_PATH).params(params2);
            mockMvc.perform(servletRequest2)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()").value(3))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").exists());

        }

        @Test
        @DisplayName("Test Get Average Reference Rate With Invalid Date Ranges - Should Return 400 With Errors")
        void testIncorrectDateRange() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2024-05-19");
            params.add("endDate", "2024-07-21");
            params.add("currency", "USD");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.DATA_002.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.DATA_002.getMessage())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.DATA_002.getDetails()));
        }

        @Test
        @DisplayName("Test Get Average Reference Rate With Start Date Greater Than End Date - Should Return 400 With Errors")
        void testInvalidDateRange() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2020-05-19");
            params.add("endDate", "2019-07-21");
            params.add("currency", "USD");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_004.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.REQ_004.getMessage())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.REQ_004.getDetails()));
        }

        @Test
        @DisplayName("Test Get Average Reference Rate With Unknown Currency - Should Return 400 With Errors")
        void testUnknownCurrency() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2023-05-19");
            params.add("endDate", "2023-05-21");
            params.add("currency", "UNKNOWN");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.CURR_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorCode.CURR_001.getMessage(), "UNKNOWN")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorCode.CURR_001.getDetails()));
        }

        @Test
        @DisplayName("Test Get Average Reference Rate With Unavailable Exchange Rate - Should Return 200 With Zero Value")
        void testUnavailableExchangeRate() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2023-05-19");
            params.add("endDate", "2023-05-21");
            params.add("currency", "CYP");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.averageReferenceRate").value(0.0))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").doesNotExist());
        }

        @Test
        @DisplayName("Test Get Average Reference Rate With Valid Request - Should Return 200 With Non-Zero Value")
        void testHighestReferenceRateSuccess() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", "2023-05-19");
            params.add("endDate", "2023-05-21");
            params.add("currency", "USD");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(URL_PATH).params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.averageReferenceRate").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").doesNotExist());
        }

    }
}