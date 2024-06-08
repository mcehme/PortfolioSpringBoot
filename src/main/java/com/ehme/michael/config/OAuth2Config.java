package com.ehme.michael.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix="personal.admin")
public class OAuth2Config {
    private Map<String, String> usernames;

    public Map<String, String> getUsernames() {
        return usernames;
    }

    public void setUsernames(Map<String, String> usernames) {
        this.usernames = usernames;
    }
}
