package com.ehme.michael.components;

import com.ehme.michael.config.ReCaptchaSettings;
import com.ehme.michael.records.ReCaptchaToken;
import com.ehme.michael.records.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ReCaptchaService {

    private final RestClient restClient;

    @Autowired
    ReCaptchaSettings reCaptchaSettings;

    @Autowired
    public ReCaptchaService() {
        restClient = RestClient.builder().baseUrl(VERIFY_URI).build();
    }

    public ReCaptchaService(RestClient.Builder restClientBuilder, ReCaptchaSettings reCaptchaSettings) {
        this.restClient = restClientBuilder.baseUrl(VERIFY_URI).build();
        this.reCaptchaSettings = reCaptchaSettings;
    }

    private static final String VERIFY_URI = "https://www.google.com/recaptcha/api/siteverify";

    public boolean validate(ReCaptchaToken reCaptchaToken) {
        ReCaptchaResponse reCaptchaResponse = restClient.post().uri(uriBuilder ->uriBuilder
                        .queryParam("secret", "test")
                        .queryParam("response", reCaptchaToken.response())
                        .build()
        ).retrieve().body(ReCaptchaResponse.class);
        return reCaptchaResponse != null
                && reCaptchaResponse.success()
                && reCaptchaResponse.action().equals("SUBMIT")
                && reCaptchaResponse.score() >= reCaptchaSettings.getThreshold();

    }

}
