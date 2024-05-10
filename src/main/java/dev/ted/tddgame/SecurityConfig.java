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
import org.springframework.security.crypto.password.PasswordEncoder;
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
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    private static UserDetails createUser(String username, String password, PasswordEncoder encoder, String... roles) {
        return User.withUsername(username)
                   .password(encoder.encode(password))
                   .roles(roles)
                   .build();
    }

    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails blueUser = createUser("Blue", "blue", encoder, "USER");
        UserDetails redUser = createUser("Red", "red", encoder, "USER");
        UserDetails greenUser = createUser("Green", "green", encoder, "USER");
        UserDetails yellowUser = createUser("Yellow", "yellow", encoder, "USER");
        UserDetails blackUser = createUser("Black", "black", encoder, "USER");
        UserDetails whiteUser = createUser("White", "white", encoder, "USER");

        return new InMemoryUserDetailsManager(blueUser, redUser, greenUser, yellowUser, blackUser, whiteUser);
    }

}