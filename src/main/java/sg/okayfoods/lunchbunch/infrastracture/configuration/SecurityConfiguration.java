package sg.okayfoods.lunchbunch.infrastracture.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import sg.okayfoods.lunchbunch.common.constant.UrlConstants;
import sg.okayfoods.lunchbunch.infrastracture.adapter.filter.JWTTokenFilter;

import java.util.List;


@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, JWTTokenFilter jwtTokenValidatorFilter) throws Exception {

        http
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->
                        cors.configurationSource(request -> {
                            var corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(List.of("*"));
                            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                            corsConfiguration.setAllowedHeaders(List.of("*"));
                            corsConfiguration.setExposedHeaders(List.of("Authorization"));
                            return corsConfiguration;
                        })
                )
                .addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(UrlConstants.REGISTER,UrlConstants.LOGIN).permitAll()
                        .requestMatchers(HttpMethod.GET, "/lunch-plan/**").permitAll()
                        .requestMatchers(UrlConstants.LUNCH_PLAN_WEBSOCKET).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
