package com.formedix.dmedelacruz.controller;

import com.formedix.dmedelacruz.exception.constant.ErrorCode;
import com.formedix.dmedelacruz.exception.constant.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    class TestGetExchangeRateForDate {

        @Test
        void testMissingDateField() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(root + "/rates");
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorMessage.REQUIRED_PARAMETER_MISSING.getMessage(), "date")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorMessage.REQUIRED_PARAMETER_MISSING.getDetails()));
        }

        @Test
        void testBlankDateFieldValue() throws Exception {
            MockHttpServletRequestBuilder servletRequest1 = MockMvcRequestBuilders.get(root + "/rates").param("date", "");
            mockMvc.perform(servletRequest1)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorMessage.REQUIRED_PARAMETER_MISSING.getMessage(), "date")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorMessage.REQUIRED_PARAMETER_MISSING.getDetails()));

            MockHttpServletRequestBuilder servletRequest2 = MockMvcRequestBuilders.get(root + "/rates").param("date", "    ");
            mockMvc.perform(servletRequest2)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorMessage.REQUIRED_PARAMETER_MISSING.getMessage(), "date")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorMessage.REQUIRED_PARAMETER_MISSING.getDetails()));
        }

        @Test
        void testDataNotFound() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(root + "/rates").param("date", "2040-05-25");
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.DATA_001.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(ErrorMessage.NO_DATA_FOR_DATE.getMessage()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorMessage.NO_DATA_FOR_DATE.getDetails()));
        }

        @Test
        void testDataHasResults() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(root + "/rates").param("date", "2015-05-19");
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

        @Test
        void testConvertCurrencyMissingRequiredFields() throws Exception {
            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(root + "/convert");
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

//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value(ErrorCode.REQ_001.toString()))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value(String.format(ErrorMessage.REQUIRED_PARAMETER_MISSING.getMessage(), "sourceCurrency")))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].details").value(ErrorMessage.REQUIRED_PARAMETER_MISSING.getDetails()))
//
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1].code").value(ErrorCode.REQ_001.toString()))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1].message").value(String.format(ErrorMessage.REQUIRED_PARAMETER_MISSING.getMessage(), "date")))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1].details").value(ErrorMessage.REQUIRED_PARAMETER_MISSING.getDetails()))
//
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[2].code").value(ErrorCode.REQ_001.toString()))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[2].message").value(String.format(ErrorMessage.REQUIRED_PARAMETER_MISSING.getMessage(), "targetCurrency")))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[2].details").value(ErrorMessage.REQUIRED_PARAMETER_MISSING.getDetails()))
//
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[3].code").value(ErrorCode.REQ_003.toString()))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[3].message").value(String.format(ErrorMessage.REQUIRED_POSITIVE_NUMERIC_PARAMETER.getMessage(), "amount")))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[3].details").value(ErrorMessage.REQUIRED_POSITIVE_NUMERIC_PARAMETER.getDetails()));
        }

        @Test
        void testConvertCurrencyBlankRequiredFieldValues() throws Exception {
            MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
            params1.add("targetCurrency", "");
            params1.add("sourceCurrency", "");
            params1.add("date", "");
            params1.add("amount", "");

            MockHttpServletRequestBuilder servletRequest1 = MockMvcRequestBuilders.get(root + "/convert").params(params1);
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

            MockHttpServletRequestBuilder servletRequest2 = MockMvcRequestBuilders.get(root + "/convert").params(params2);
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
        void testNotNumericAmountValue() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "JPY");
            params.add("date", "2023-05-19");
            params.add("amount", "amount");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(root + "/convert").params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_002.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorMessage.REQUIRED_NUMERIC_PARAMETER.getMessage(), "amount")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorMessage.REQUIRED_NUMERIC_PARAMETER.getDetails()));
        }

        @Test
        void testNotPositiveNumericAmountValue() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "JPY");
            params.add("date", "2023-05-19");
            params.add("amount", "-10.0");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(root + "/convert").params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(ErrorCode.REQ_003.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(String.format(ErrorMessage.REQUIRED_POSITIVE_NUMERIC_PARAMETER.getMessage(), "amount")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].details").value(ErrorMessage.REQUIRED_POSITIVE_NUMERIC_PARAMETER.getDetails()));
        }

        @Test
        void testConvertSuccess() throws Exception {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("targetCurrency", "USD");
            params.add("sourceCurrency", "JPY");
            params.add("date", "2023-05-19");
            params.add("amount", "100");

            MockHttpServletRequestBuilder servletRequest = MockMvcRequestBuilders.get(root + "/convert").params(params);
            mockMvc.perform(servletRequest)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.convertedAmount").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errors").doesNotExist());
        }

    }
}