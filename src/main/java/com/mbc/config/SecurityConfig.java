package com.mbc.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("----------SecurityFilterChain 진입 체크----------");

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/member/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("name")
                        .failureUrl("/login/error")
                )
                // OAuth2 로그인 추가 (Spring Security 6.1 이상 방식)
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/") // OAuth2 로그인 성공 시 리디렉션
//                        .failureUrl("/login/error") // 실패 시 리디렉션
//                        .clientRegistrationRepository(clientRegistrationRepository()) // 클라이언트 등록 정보 추가
//                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/item/delete") // delete 요청에 대한 CSRF 보호 비활성화
                );

<<<<<<< HEAD
        // 기존 로그인 방식 유지
=======
        // 이미 로그인된 사용자가 로그인 페이지에 접근하면 메인 페이지로 리다이렉트

>>>>>>> d04d0425fb001ff66bb4b409f4bd974d17ab780b
        http.formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied"); // 접근 거부 시 리다이렉트될 페이지

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

<<<<<<< HEAD
//    // OAuth2 클라이언트 등록 정보 저장소 (구글만 사용)
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(
//                googleClientRegistration() // 구글 OAuth2 클라이언트 등록 정보
//        );
//    }
//
//    // Google OAuth2 클라이언트 등록 정보
//    private ClientRegistration googleClientRegistration() {
//        return ClientRegistration.withRegistrationId("google")
//                .clientId("YOUR_GOOGLE_CLIENT_ID") // 구글 클라이언트 ID
//                .clientSecret("YOUR_GOOGLE_CLIENT_SECRET") // 구글 클라이언트 시크릿
//                .scope("profile", "email")
//                .redirectUri("http://localhost:8080/login/oauth2/code/google") // 리디렉션 URI
//                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
//                .tokenUri("https://oauth2.googleapis.com/token")
//                .clientName("Google")
//                .build();
//    }

=======
>>>>>>> d04d0425fb001ff66bb4b409f4bd974d17ab780b
}
