package com.ehme.michael.components;

import com.ehme.michael.PortfolioController;
import com.ehme.michael.config.CaptchaSettings;
import com.ehme.michael.records.ReCaptchaToken;
import com.ehme.michael.records.ReCaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CaptchaService {

    RestClient restClient = RestClient.builder().baseUrl(VERIFY_URI).build();

    @Autowired
    CaptchaSettings captchaSettings;

    Logger logger = LoggerFactory.getLogger(CaptchaService.class);


    private static final String VERIFY_URI = "https://www.google.com/recaptcha/api/siteverify";

    public void validate(ReCaptchaToken reCaptchaToken) throws Exception {
        ReCaptchaResponse reCaptchaResponse = restClient.post().uri(uriBuilder ->uriBuilder
                        .queryParam("secret", captchaSettings.getKey())
                        .queryParam("response", reCaptchaToken.response())
                        .build()
        ).retrieve().body(ReCaptchaResponse.class);
        if (reCaptchaResponse != null && reCaptchaResponse.success() && reCaptchaResponse.action().equals("SUBMIT") &&reCaptchaResponse.score() >= captchaSettings.getThreshold()) {
            logger.info("reCaptcha passes with score: {}", reCaptchaResponse.score());
            return;
        }

        if (reCaptchaResponse != null) {
            logger.info("reCaptcha fails with score: {}", reCaptchaResponse.score());
        }

        throw new Exception("Bad Captcha Response");
    }

}
