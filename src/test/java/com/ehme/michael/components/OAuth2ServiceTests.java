package com.ehme.michael.components;

import com.ehme.michael.config.OAuth2Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class OAuth2ServiceTests {
    @InjectMocks
    OAuth2Service oAuth2Service;
    OAuth2User oAuth2User = new DefaultOAuth2User(List.of(), Map.of("login", "not-test"), "login");
    OAuth2User oAuth2Admin = new DefaultOAuth2User(List.of(), Map.of("login", "test"), "login");
    @Mock
    OAuth2UserRequest oAuth2UserRequest;
    @Mock
    DefaultOAuth2UserService defaultOAuth2UserService;
    @Mock
    Function<OAuth2UserRequest, String> unwrapper;
    @Mock
    OAuth2Config oAuth2Config;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserWithUser() {
        Mockito.when(defaultOAuth2UserService.loadUser(Mockito.any())).thenReturn(oAuth2User);
        Mockito.when(unwrapper.apply(Mockito.any())).thenReturn("github");
        Mockito.when(oAuth2Config.getUsernames()).thenReturn(Map.of("github", "test"));
        OAuth2User result = oAuth2Service.loadUser(oAuth2UserRequest);
        Assertions.assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        Assertions.assertFalse(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    public void testLoadUserWithAdmin() {
        Mockito.when(defaultOAuth2UserService.loadUser(Mockito.any())).thenReturn(oAuth2Admin);
        Mockito.when(unwrapper.apply(Mockito.any())).thenReturn("github");
        Mockito.when(oAuth2Config.getUsernames()).thenReturn(Map.of("github", "test"));
        OAuth2User result = oAuth2Service.loadUser(oAuth2UserRequest);
        Assertions.assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Assertions.assertFalse(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
