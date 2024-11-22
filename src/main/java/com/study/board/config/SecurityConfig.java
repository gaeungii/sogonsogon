package com.study.board.config;

import com.study.board.service.MemberDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberDetailService memberDetailService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/static/**"),
                                 new AntPathRequestMatcher("/css/**"), // CSS 경로 허용
                                 new AntPathRequestMatcher("/images/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/member/login"),
                                new AntPathRequestMatcher("/member/join"),
                               new AntPathRequestMatcher("/board/post/**")
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/board/post").authenticated()  // POST 요청은 인증 필요
                        .requestMatchers(HttpMethod.PUT, "/board/post/edit/**").authenticated()  // PUT 요청은 인증 필요
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/member/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                        .loginProcessingUrl("/member/login-process")
                        .usernameParameter("email")  // 이거때매 3시간 걸림
                        .passwordParameter("password")
                        .failureHandler(customAuthenticationFailureHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .invalidateHttpSession(true)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(memberDetailService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorMessage = "로그인 실패! ";

            // 예외에 따라 다른 메시지 설정
            if (exception.getMessage().contains("Bad credentials")) {
                errorMessage += "사용자명 또는 비밀번호가 틀렸습니다.";
            } else {
                errorMessage += "다시 시도해 주세요.";
            }

            request.getSession().setAttribute("loginError", errorMessage);
            response.sendRedirect(request.getContextPath() + "/member/login");
        };
    }

}