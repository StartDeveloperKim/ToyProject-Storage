package com.developer.smallRoom.config;

import com.developer.smallRoom.application.auth.jwt.TokenAuthenticationFilter;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshTokenRepository;
import com.developer.smallRoom.application.auth.oauth.OAuth2SuccessHandler;
import com.developer.smallRoom.application.auth.oauth.OAuth2UserCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedirectPathProperties redirectPathProperties;

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> {
            web.ignoring()
                    .requestMatchers("/image/**", "/css/**", "/js/**", "/favicon", "/api/token", "/logout");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
                .requestMatchers("/article/post/**").authenticated()
                .requestMatchers(POST, "/api/article").authenticated()
                .requestMatchers(DELETE, "/api/article").authenticated()
                .requestMatchers(PUT, "/api/article").authenticated()
                .requestMatchers("/api/image", "/api/like/**").authenticated()
                .anyRequest().permitAll();

        http
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/**")
                .and()
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService);

        http.addFilterBefore(new TokenAuthenticationFilter(tokenProvider, refreshTokenRepository, redirectPathProperties), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
