package edu.ncsu.csc.CoffeeMaker.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Configures Spring security. Tells Spring how to find users in the system,
 * which API routes (don't) require authentication, and configures a few other
 * pieces of the security system.
 *
 * @author Kai Presler-Marshall
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity ( prePostEnabled = true )
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * DataSource used for connecting to and interacting with the database.
     */
    @Autowired
    DataSource dataSource;

    /**
     * Login configuration for iTrust2.
     *
     * @param auth
     *            AuthenticationManagerBuilder to use to configure the
     *            Authentication.
     * @throws Exception
     *             On DB-related errors
     */
    @Autowired
    public void configureGlobal ( final AuthenticationManagerBuilder auth ) throws Exception {
        final JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> dbManager = auth.jdbcAuthentication();

        // User query enabled flag also checks for locked or banned users. The
        // FailureHandler then
        // determines if the DisabledUser Exception was due to ban, lockout, or
        // true disable.
        // POSSIBLE FUTURE CHANGE: Refactor the UserSource here along the lines
        // of this:
        // http://websystique.com/springmvc/spring-mvc-4-and-spring-security-4-integration-example/
        dbManager.dataSource( dataSource ).passwordEncoder( passwordEncoder() )
                .usersByUsernameQuery( "select username,password,enabled from user WHERE username = ?;" )
                .authoritiesByUsernameQuery( "select user_username, roles from user_roles where user_username=?" );
        auth.authenticationEventPublisher( defaultAuthenticationEventPublisher() );

    }

    /**
     * Method responsible for the Login page. Can be extended to explicitly
     * override other automatic functionality as desired.
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {

        final String[] patterns = new String[] { "/login*", "/register" };

        http.authorizeRequests().antMatchers( patterns ).anonymous().anyRequest().authenticated().and().formLogin()
                .loginPage( "/login" ).defaultSuccessUrl( "/" ).and().csrf()

                .csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse() );

    }

    @Override
    public void configure ( final WebSecurity web ) throws Exception {
        // Allow anonymous access to the 3 mappings related to resetting a
        // forgotten password
        web.ignoring().antMatchers( "/api/v1/generateUsers", "/register", "/api/v1/users" );
    }

    /**
     * Bean used to generate a PasswordEncoder to hash the user-provided
     * password.
     *
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationEventPublisher used to assist with authentication
     *
     * @return The AuthenticationEventPublisher.
     */
    @Bean
    public DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher () {
        return new DefaultAuthenticationEventPublisher();
    }

}
