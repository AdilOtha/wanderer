package ca.dal.cs.wanderer.Config;

import ca.dal.cs.wanderer.filter.TokenAuthenticationFilter;
import ca.dal.cs.wanderer.handler.OAuthSuccessHandler;
import ca.dal.cs.wanderer.services.UserProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

    private ObjectMapper objectMapper;

    @Autowired
    private OidcUserService oidcUserService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Autowired
    private UserProfileService profileService;

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests()
                .antMatchers("/api/v1/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(oidcUserService)
                .and()
                .successHandler(oAuthSuccessHandler)
                .and()
                .exceptionHandling()
                .and()
                .csrf().disable()
                .addFilterAfter(new TokenAuthenticationFilter("/api/v1/**", clientId, profileService), LogoutFilter.class);

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin","Authorization", "Cache-Control", "Content-Type", "xsrfheadername","xsrfcookiename"
                ,"X-Requested-With","XSRF-TOKEN","Accept", "x-xsrf-token","withcredentials","x-csrftoken"));
        configuration.setExposedHeaders(Arrays.asList("custom-header1", "custom-header2"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
