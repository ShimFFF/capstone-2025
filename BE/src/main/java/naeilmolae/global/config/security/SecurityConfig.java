package naeilmolae.global.config.security;


import lombok.RequiredArgsConstructor;
import naeilmolae.global.config.security.auth.CustomAccessDeniedHandler;
import naeilmolae.global.config.security.jwt.JwtAuthenticationFilter;
import naeilmolae.global.config.security.jwt.JwtExceptionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    @Profile("prod")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/actuator/health").permitAll()
                        .requestMatchers("/api/v1/auth/login/**", "/api/v1/auth/token/refresh","/api/v1/test-push/**").permitAll()
                        .requestMatchers("/api/v1/chatgpt/**").permitAll()
                        .requestMatchers("/api/v1/version").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtExceptionFilter, LogoutFilter.class) // filter 등록시 등록되어있는 필터와 순서를 정의해야함
                .addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
                .build();
    }

    @Bean
    @Profile("!prod")
    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/actuator/health").permitAll()
                        .requestMatchers("/api/v1/auth/login/**", "/api/v1/auth/token/refresh", "/api/v1/test-push/**").permitAll()
                        .requestMatchers("/api/v1/chatgpt/**").permitAll()
                        .requestMatchers("/api/v1/version").permitAll()
                        .requestMatchers("/h2-console/**", "/h2-console").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtExceptionFilter, LogoutFilter.class) // filter 등록시 등록되어있는 필터와 순서를 정의해야함
                .addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
                .build();
    }


}
