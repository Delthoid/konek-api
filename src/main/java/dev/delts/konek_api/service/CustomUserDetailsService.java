package dev.delts.konek_api.service;

import dev.delts.konek_api.exception.RecordNotFoundException;
import dev.delts.konek_api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        dev.delts.konek_api.entity.User user = userRepository.findByUserName(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new RecordNotFoundException("Username and email not found")));
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
