package dev.ted.tddgame;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD, DispatcherType.REQUEST).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails blueUser = createUser("Blue", "blue", "USER");
        UserDetails redUser = createUser("Red", "red", "USER");
        UserDetails greenUser = createUser("Green", "green", "USER");
        UserDetails yellowUser = createUser("Yellow", "yellow", "USER");
        UserDetails blackUser = createUser("Black", "black", "USER");
        UserDetails whiteUser = createUser("White", "white", "USER");

        return new InMemoryUserDetailsManager(blueUser, redUser, greenUser, yellowUser, blackUser, whiteUser);
    }

    private static UserDetails createUser(String username, String password, String... roles) {
        return User.withDefaultPasswordEncoder()
                   .username(username)
                   .password(password)
                   .roles(roles)
                   .build();
    }

}