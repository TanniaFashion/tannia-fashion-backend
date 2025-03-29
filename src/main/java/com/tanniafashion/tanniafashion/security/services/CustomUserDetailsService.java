package com.tanniafashion.tanniafashion.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tanniafashion.tanniafashion.models.User;
import com.tanniafashion.tanniafashion.repositories.UserRepository;
import com.tanniafashion.tanniafashion.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        System.out.println("Username found: " + user.getUsername());
        System.out.println("Password in DB: " + user.getPassword());
        return new CustomUserDetails(user);
    }
}
