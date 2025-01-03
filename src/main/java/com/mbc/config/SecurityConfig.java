package com.mbc.config;

//import com.mbc.service.CustomOAuth2UserService;
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
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login") // OAuth2 로그인도 동일한 로그인 페이지 사용
//                        .defaultSuccessUrl("/")
//                        .failureUrl("/login/error")
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(customOAuth2UserService()) // 사용자 정보 처리
//                        )
//                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/item/delete") // delete 요청에 대한 CSRF 보호 비활성화
                );
        ;

        // 이미 로그인된 사용자가 로그인 페이지에 접근하면 메인 페이지로 리다이렉트
//        http.formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/access-denied"); // 접근 거부 시 리다이렉트될 페이지

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CustomOAuth2UserService customOAuth2UserService() {
//        return new CustomOAuth2UserService(); // 사용자 정의 OAuth2 서비스
//    }
}
