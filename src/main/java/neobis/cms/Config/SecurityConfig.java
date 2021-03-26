package neobis.cms.Config;


import neobis.cms.Filter.JwtFilter;
import neobis.cms.Service.Bishkek.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(encoder());
    }

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
            // other public endpoints of your API may be appended to this array
    };
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/bishkek/**").hasAnyRole("ADMIN", "BISHKEK_MANAGEMENT", "BISHKEK_MARKETING")
                .antMatchers("/osh/**").hasAnyRole("ADMIN", "OSH_MANAGEMENT", "OSH_MARKETING")
                .antMatchers("/register/admin/**").hasRole("ADMIN")
                .antMatchers("/register/**").permitAll()
                .antMatchers(HttpMethod.POST, "/status").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/status").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/utm").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/utm").hasRole("ADMIN")
                .antMatchers("/bishkek/history").hasRole("ADMIN")
                .antMatchers("/osh/history").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/occupation").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/occupation").hasRole("ADMIN")


                .anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().httpStrictTransportSecurity().disable();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
