package ipsen2.groep8.werkplekkenreserveringsappbackend.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JWTFilter filter;
    @Autowired
    private MyUserDetailsService uds;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/api/building/**").hasRole("User")
                .antMatchers(
                    Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH}),
                    "/api/building/**"
                ).hasRole("Admin")
                .antMatchers("/api/auth/profile").hasRole("User")
                .antMatchers("/api/auth/verify-email").hasRole("User")
                .antMatchers("/api/auth/send-verify-email").hasRole("User")
                .antMatchers(HttpMethod.GET, "/api/wing/**").hasRole("User")
                .antMatchers(
                    Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH}), 
                    "/api/wing/**"
                ).hasRole("Admin")
                .antMatchers(HttpMethod.GET, "/api/meetingroom/**").hasRole("User")
                .antMatchers(
                    Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH}), 
                    "/api/meetingroom/**"
                ).hasRole("Admin")
                .antMatchers(HttpMethod.GET, "/api/role/**").hasRole("User")
                .antMatchers(HttpMethod.GET, "/api/user/**").hasRole("User")
                .antMatchers(
                    Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH}), 
                    "/api/user/**"
                ).hasRole("User")
                .antMatchers("/api/reservation/**").hasRole("User")
                .antMatchers("/api/auth/**").permitAll()
                .and()
                .userDetailsService(uds)
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .enableSessionUrlRewriting(true);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}