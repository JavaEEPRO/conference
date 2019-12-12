package si.inspirited.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.csrf.CsrfFilter;
import si.inspirited.security.CustomAuthenticationProvider;
import si.inspirited.security.CustomRememberMeService;
import si.inspirited.security.CustomUserDetailsService;
import static org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.requestContextFilter;

@Configuration
@ComponentScan(basePackages = { "si.inspirited.security" })
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService userDetailsService;

    public SecurityConfig() {
        super();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(requestContextFilter(), CsrfFilter.class)
                .authorizeRequests()

                .antMatchers("/login**", "/h2-console", "/").permitAll()
                .antMatchers( "/conferences").hasAuthority("ROLE_USER")
                .antMatchers("/conferences**", "/rooms**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/roles**").denyAll()
                .anyRequest().access("isAnonymous()")

                .and()
                .formLogin()
                .defaultSuccessUrl("/conferences")
                .permitAll()

                .and()
                .sessionManagement()
                .invalidSessionUrl("/invalidSess")
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and()
                .sessionFixation().none()
                .and()
                .logout()
                .invalidateHttpSession(false)
                .logoutSuccessUrl("/logout")
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .rememberMe().rememberMeServices(rememberMeServices());
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        CustomRememberMeService rememberMeService = new CustomRememberMeService("theKey", userDetailsService, new InMemoryTokenRepositoryImpl());
        return rememberMeService;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}
