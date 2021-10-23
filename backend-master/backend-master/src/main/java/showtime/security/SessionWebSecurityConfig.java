package showtime.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SessionWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();
        http
                // restricts access to all but the following pages
                .authorizeRequests()
                    .antMatchers("/", "/home", "/home.html").permitAll()
                    .antMatchers("/login", "/login.html").permitAll()
                    .antMatchers("/register", "/register.html").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/fonts/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/api/**").permitAll()
                    .anyRequest().authenticated();
        http
                // login page, call to "/auth/login", onSuccess redirect:/timeline
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/api/auth/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    //TODO: success and failure handlers
                    .permitAll();
        http
                // logout page, call to "/auth/logout"
                .logout()
                    .logoutUrl("/api/auth/logout")
                    .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // configure to use database to verify user credentials
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select email, password, 'true' from user where email = ?")
                .authoritiesByUsernameQuery("select email, 'DEFAULT_ROLE' from user where email = ?")
                .passwordEncoder(noop());
    }

    // no password encoding in DB is strongly unrecommended, temporary measures
    @SuppressWarnings("deprecation")
    @Bean
    static NoOpPasswordEncoder noop() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
