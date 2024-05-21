package com.ehme.michael.components;

import com.ehme.michael.config.CaptchaSettings;
import com.ehme.michael.records.ReCaptchaToken;
import com.ehme.michael.records.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CaptchaService {

    RestClient restClient = RestClient.builder().baseUrl(VERIFY_URI).build();

    @Autowired
    CaptchaSettings captchaSettings;

    private static final String VERIFY_URI = "https://www.google.com/recaptcha/api/siteverify";

    public void validate(ReCaptchaToken reCaptchaToken) throws Exception {
        ReCaptchaResponse reCaptchaResponse = restClient.post().uri(uriBuilder ->uriBuilder
                        .queryParam("secret", captchaSettings.getKey())
                        .queryParam("response", reCaptchaToken.response())
                        .build()
        ).retrieve().body(ReCaptchaResponse.class);

        if (reCaptchaResponse != null && reCaptchaResponse.success() && reCaptchaResponse.action().equals("SUBMIT") &&reCaptchaResponse.score() >= captchaSettings.getThreshold()) {
            return;
        }

        throw new Exception("Bad Captcha Response");
    }

}
