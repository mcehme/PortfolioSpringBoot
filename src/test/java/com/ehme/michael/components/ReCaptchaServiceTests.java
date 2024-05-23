package com.ehme.michael.components;

import com.ehme.michael.config.ReCaptchaSettings;
import com.ehme.michael.records.ReCaptchaResponse;
import com.ehme.michael.records.ReCaptchaToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestClientTest
@ContextConfiguration(classes = ReCaptchaSettings.class)
public class ReCaptchaServiceTests {

    @Autowired
    ReCaptchaSettings reCaptchaSettings;

    ReCaptchaService reCaptchaService;

    MockRestServiceServer mockRestServiceServer;

    static final String VERIFY_URI = "https://www.google.com/recaptcha/api/siteverify";

    @BeforeEach
    void setup() {
        RestClient.Builder builder = RestClient.builder();
        MockitoAnnotations.openMocks(this);
        mockRestServiceServer = MockRestServiceServer.bindTo(builder).build();
        reCaptchaService = new ReCaptchaService(builder, reCaptchaSettings);

    }

    @Test
    void validReCaptcha() throws JsonProcessingException {
        ReCaptchaResponse response = new ReCaptchaResponse(true,"","", 0.9, "SUBMIT", List.of());
        String json = new ObjectMapper().writeValueAsString(response);

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(Matchers.containsStringIgnoringCase(VERIFY_URI))).andExpect(MockRestRequestMatchers.method(HttpMethod.POST)).andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));
        boolean result = reCaptchaService.validate(new ReCaptchaToken("",""));
        Assertions.assertTrue(result);
    }

    @Test
    void invalidReCaptcha() throws JsonProcessingException {
        ReCaptchaResponse response = new ReCaptchaResponse(false,null,null, null, null, List.of());
        String json = new ObjectMapper().writeValueAsString(response);

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(Matchers.containsStringIgnoringCase(VERIFY_URI))).andExpect(MockRestRequestMatchers.method(HttpMethod.POST)).andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));
        boolean result = reCaptchaService.validate(new ReCaptchaToken("",""));
        Assertions.assertFalse(result);
    }

    @Test
    void invalidReCaptchaScore() throws JsonProcessingException {
        ReCaptchaResponse response = new ReCaptchaResponse(true,"","", 0.3, "SUBMIT", List.of());
        String json = new ObjectMapper().writeValueAsString(response);

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(Matchers.containsStringIgnoringCase(VERIFY_URI))).andExpect(MockRestRequestMatchers.method(HttpMethod.POST)).andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));
        boolean result = reCaptchaService.validate(new ReCaptchaToken("",""));
        Assertions.assertFalse(result);
    }


}
