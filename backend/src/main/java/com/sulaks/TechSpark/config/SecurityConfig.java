package com.sulaks.TechSpark.config;

import com.sulaks.TechSpark.security.JwtAccessDeniedHandler;
import com.sulaks.TechSpark.security.JwtAuthFilter;
import com.sulaks.TechSpark.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/verify-otp", "/api/auth/resend-otp").permitAll()
                        .requestMatchers("/api/auth/me").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/brands/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/product-images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/product-images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/product-images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/product-images/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/product-spec-attributes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/product-spec-attributes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/product-spec-attributes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/product-spec-attributes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/variant-attributes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/variant-attributes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/variant-attributes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/variant-attributes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/variant-attribute-values/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/variant-attribute-values").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/variant-attribute-values/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/variant-attribute-values/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/product-variants/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/product-variants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/product-variants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/product-variants/**").hasRole("ADMIN")

                        .requestMatchers("/api/variant-inventories/**").hasRole("ADMIN")
                        .requestMatchers("/api/stock-movements/**").hasRole("ADMIN")

                        .requestMatchers("/api/wishlist/**").authenticated()
                        .requestMatchers("/api/cart/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/coupons/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/coupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/coupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/coupons/**").hasRole("ADMIN")

                        .requestMatchers("/api/addresses/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/orders").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/orders/my/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/my/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/status").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/payments/my/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/payments/my/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/payments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payments/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/payments/*/status").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/shipments/my/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/shipments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/shipments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/shipments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/shipments/order/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/shipments/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/shipments/*/events").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/reviews/product/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/reviews").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/reviews/my").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/reviews").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/reviews/pending").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/reviews/*/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/returns").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/returns/my").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/returns/my/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/returns").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/returns/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/returns/*/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/returns/*/reject").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/returns/*/picked-up").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/returns/*/received").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/returns/*/close").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/refunds/my/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/refunds/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/refunds").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/refunds/return/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/refunds/*").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}