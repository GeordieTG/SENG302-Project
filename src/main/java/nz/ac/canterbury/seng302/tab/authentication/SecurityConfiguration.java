package nz.ac.canterbury.seng302.tab.authentication;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Custom Security Configuration
 * Such functionality was previously handled by WebSecurityConfigurerAdapter
 */
@Configuration
@EnableWebSecurity
@ComponentScan("com.baeldung.security")
public class SecurityConfiguration {

    /**
     * Request matchers for things to access without authority
     */
    private static final RequestMatcher[] REQUESTMATCHERS = {
        AntPathRequestMatcher.antMatcher("/h2/**"),
        AntPathRequestMatcher.antMatcher("/css/**"),
        AntPathRequestMatcher.antMatcher("/JavaScript/**"),
        AntPathRequestMatcher.antMatcher("/images/**"),
        AntPathRequestMatcher.antMatcher("/public/**")
    };

    private static final String LOGIN = "/login";

    /**
     * Create an Authentication Manager with our {@link CustomAuthenticationProvider}
     *
     * @param http http security configuration object from Spring
     * @return a new authentication manager
     * @throws Exception if the AuthenticationManager can not be built
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http,
                                             CustomAuthenticationProvider authProvider)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }


    /**
     * Definition of authority
     *
     * @param http http security configuration object from Spring (beaned in)
     * @return Custom SecurityFilterChain
     * @throws Exception if the SecurityFilterChain can not be built
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Allow h2 console through security. Note: Spring 6 broke the nicer way to do this
        // (i.e. how the authorisation is handled below)
        // See https://github.com/spring-projects/spring-security/issues/
        http.authorizeHttpRequests(auth -> auth.requestMatchers(REQUESTMATCHERS).permitAll())
            .headers(headers -> headers.frameOptions().disable())
            .csrf(csrf -> csrf.ignoringRequestMatchers(REQUESTMATCHERS))
            .authorizeHttpRequests()
            // Allow "/", "/register", and "/login" to anyone (permitAll)
            .requestMatchers("/", "/lostPassword", "/resetPassword", LOGIN, "/demo",
                "/registrationConfirmation", "/sendemail", "/css/**", "/JavaScript/**",
                "/registration", "/country", "/address", "/images/**", "/webjars/**")
            .permitAll()
            // Any other request requires authentication
            .anyRequest()
            .authenticated()
            .and()
            // Define logging in, a POST "/login" endpoint now exists under the hood,
            // after login redirect to user page
            .formLogin().loginPage(LOGIN).loginProcessingUrl(LOGIN)
            .defaultSuccessUrl("/profilePage", true)
            .and()
            // Define logging out, a POST "/logout" endpoint now exists under the hood,
            // redirect to "/login", invalidate session and remove cookie
            .logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
            .deleteCookies("JSESSIONID");

        return http.build();
    }
}
