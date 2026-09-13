package it.uniroma3.siw.photoblog.authentication;

import static it.uniroma3.siw.photoblog.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.photoblog.model.Credentials.USER_ROLE;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final DataSource dataSource;

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
            "SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, role FROM credentials WHERE username=?");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {

        RequestMatcher apiRequests = request -> request.getRequestURI().startsWith("/api/");

        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/login", "/403",
                    "/css/**", "/fonts/**", "/images/**", "/uploads/**", "/react/**", "/favicon.ico").permitAll();
            authorize.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll();
            //la pagina di dettaglio mostra solo la copertina a chi non è registrato
            authorize.requestMatchers(HttpMethod.GET, "/events", "/events/*").permitAll();
            authorize.requestMatchers(HttpMethod.GET, "/api/events", "/api/events/*").permitAll();
            authorize.requestMatchers("/events/*/gallery", "/photos/**", "/cart/**", "/purchases/**", "/api/**")
                    .hasAnyAuthority(USER_ROLE, ADMIN_ROLE);
            authorize.requestMatchers("/admin/**").hasAnyAuthority(ADMIN_ROLE);
            authorize.anyRequest().authenticated();
        });

        httpSecurity.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/", true);
            form.failureUrl("/login?error=true");
        });

        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/");
            logout.invalidateHttpSession(true);
            logout.deleteCookies("JSESSIONID");
            logout.clearAuthentication(true);
            logout.permitAll();
        });

        // le API chiamate da React ricevono 401 in JSON invece del redirect alla pagina di login
        httpSecurity.exceptionHandling(exceptions -> {
            exceptions.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), apiRequests);
            exceptions.accessDeniedPage("/403");
        });

        // il frontend React chiama le API con fetch e non invia il token CSRF
        httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        return httpSecurity.build();
    }
}
