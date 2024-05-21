package com.ehme.michael.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public record ReCaptchaResponse(Boolean success, String challenge_ts,
                                String hostname, Double score, String action, @JsonProperty("error-codes") List<String> errorCodes) {
}
