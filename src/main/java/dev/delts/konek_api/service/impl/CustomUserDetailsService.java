package dev.delts.konek_api.service.impl;

import dev.delts.konek_api.exception.RecordNotFoundException;
import dev.delts.konek_api.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    //TODO: Incorrect username and password isnt being returned by the API
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        dev.delts.konek_api.entity.User user = userRepository.findByUserName(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new RecordNotFoundException("Username and email not found")));
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
