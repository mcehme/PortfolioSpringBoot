package com.ehme.michael.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class OAuth2Service extends DefaultOAuth2UserService {

    @Value("${personal.admin.username}")
    private String username;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        if (username.equals(attributes.get("login"))) {
            authorities = customAdd(authorities, "ADMIN");
        } else {
            authorities = customAdd(authorities, "USER");
        }
        return new DefaultOAuth2User(authorities, attributes,"login");
    }
    private static Collection<? extends GrantedAuthority> customAdd(Collection<? extends  GrantedAuthority> authorities, String role) {
        return Stream.concat(
                Stream.of(new SimpleGrantedAuthority("ROLE_" + role)),
                authorities.stream().map(
                        grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority())
                )
        ).toList();
    }
}
