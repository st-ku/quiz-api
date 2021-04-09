package ru.project.quiz.config.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.project.quiz.service.ituser.Impl.ITUserServiceImpl;


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final ITUserServiceImpl itUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(ITUserServiceImpl itUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.itUserService = itUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/question/admin/**").hasAuthority("GENERATE_TESTS")
                .antMatchers("/admin/**").hasAuthority("GENERATE_TESTS")
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().logout().permitAll()
                .and().httpBasic()
                .and().sessionManagement().disable();
        http.headers().frameOptions().disable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(itUserService).passwordEncoder(bCryptPasswordEncoder);
    }
}
