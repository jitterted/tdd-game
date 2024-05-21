package dev.ted.tddgame;

import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
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

import java.util.concurrent.atomic.AtomicLong;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static AtomicLong idGenerator = new AtomicLong(1L);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
        ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberStore memberStore) {
        UserDetails blueUser = createUser(memberStore, "Blue", "blue", "MEMBER");
        UserDetails redUser = createUser(memberStore, "Red", "red", "MEMBER");
        UserDetails greenUser = createUser(memberStore, "Green", "green", "MEMBER");
        UserDetails yellowUser = createUser(memberStore, "Yellow", "yellow", "MEMBER");
        UserDetails blackUser = createUser(memberStore, "Black", "black", "MEMBER");
        UserDetails whiteUser = createUser(memberStore, "White", "white", "MEMBER");

        return new InMemoryUserDetailsManager(blueUser, redUser, greenUser, yellowUser, blackUser, whiteUser);
    }

    private static UserDetails createUser(MemberStore memberStore, String username, String password, String... roles) {
        memberStore.save(new Member(new MemberId(idGenerator.getAndIncrement()), username + "Nickname", username));
        return User.withDefaultPasswordEncoder()
                   .username(username)
                   .password(password)
                   .roles(roles)
                   .build();
    }

}