package com.javatechie.config;

import com.javatechie.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UpgradeSecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new EmployeeUserDetailsService();
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user = User.withUsername("Basant")
//                .password(passwordEncoder.encode("Pwd1"))
//                .roles("EMPLOYEE")
//                .build();
//
//        UserDetails admin = User.withUsername("Santosh")
//                .password(passwordEncoder.encode("Pwd2"))
//                .roles("MANAGER", "HR")
//                .build();
//
//        UserDetails hr = User.withUsername("Shruti")
//                .password(passwordEncoder.encode("Pwd3"))
//                .roles("HR")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin, hr);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf().disable()
//                .authorizeRequests().antMatchers("/text").permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/greeting", "/text/**").authenticated()
//                .and().httpBasic()
//                .and().build();

//        return http.csrf().disable()
//                .authorizeRequests().antMatchers("/employees/welcome",
//                        "/employees/create","/employees/authenticate").permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/employees/**").authenticated()
//                .and().httpBasic()
//                //.and().formLogin()
//                .and().build();

        http.csrf().disable()
                .authorizeRequests().antMatchers("/employees/welcome",
                        "/employees/create", "/employees/authenticate").permitAll()
                .and()
                .authorizeRequests().antMatchers("/employees/**").authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
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

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
