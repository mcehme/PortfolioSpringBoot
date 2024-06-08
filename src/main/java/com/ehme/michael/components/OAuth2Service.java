package com.ehme.michael.components;

import com.ehme.michael.config.OAuth2Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private OAuth2Config oAuth2Config;
    private DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
    private Function<OAuth2UserRequest, String> unwrapper = userRequest -> userRequest.getClientRegistration().getRegistrationId();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        if ("github".equals(unwrapper.apply(userRequest))) {
            return generator(oAuth2Config.getUsernames().get("github"), "login").loadUser(userRequest);
        }
        throw new UnsupportedOperationException();
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> generator(String username, String nameAttributeKey) {
        return userRequest -> {
            OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
            Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            if (attributes.get(nameAttributeKey) instanceof String && username.equals(attributes.get(nameAttributeKey))) {
                authorities = customAdd(authorities, "ADMIN");
            } else {
                authorities = customAdd(authorities, "USER");
            }
            return new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
        };
    }
    private Collection<? extends GrantedAuthority> customAdd(Collection<? extends  GrantedAuthority> authorities, String role) {
        return Stream.concat(
                Stream.of(new SimpleGrantedAuthority("ROLE_" + role)),
                authorities.stream().map(
                        grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority())
                )
        ).toList();
    }
}
