package com.librarymanagementsystem.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity){

      return   httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(header->header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
              .exceptionHandling(exception->
                      exception.accessDeniedHandler(accessDeniedHandler)
                              .authenticationEntryPoint(authenticationEntryPoint))
              .authorizeHttpRequests(request->
                        request.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/members/me").hasAnyAuthority("ROLE_ADMIN","ROLE_MEMBER")
                                .requestMatchers(HttpMethod.POST,"/api/books/**").hasAnyAuthority("ROLE_ADMIN","ROLE_LIBRARIAN")
                                .requestMatchers(HttpMethod.PUT,"/api/books/**").hasAnyAuthority("ROLE_ADMIN","ROLE_LIBRARIAN")
                                .requestMatchers(HttpMethod.DELETE,"/api/books/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/loans/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/fines/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/members/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/reservations/**").hasAnyAuthority("ROLE_ADMIN")


                                .requestMatchers("/api/books/**").authenticated()


                                .anyRequest().authenticated())
              .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration configuration)throws Exception {
     return    configuration.getAuthenticationManager();
    }
}
