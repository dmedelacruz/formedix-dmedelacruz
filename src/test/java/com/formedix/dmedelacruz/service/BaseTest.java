package com.formedix.dmedelacruz.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseTest {

    private final MockMvc mockMvc;

    protected BaseTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /*
     * Sets Up Data For Testing
     */
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

}
