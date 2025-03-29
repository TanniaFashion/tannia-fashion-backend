package com.tanniafashion.tanniafashion.services.implementation;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tanniafashion.tanniafashion.models.Role;
import com.tanniafashion.tanniafashion.models.User;
import com.tanniafashion.tanniafashion.models.enums.RoleName;
import com.tanniafashion.tanniafashion.repositories.RoleRepository;
import com.tanniafashion.tanniafashion.repositories.UserRepository;
import com.tanniafashion.tanniafashion.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User saveUser(User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if(!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role existingRole = roleRepository.findByName(role.getName())
                .orElseGet(() -> roleRepository.save(role));
            roles.add(existingRole);
        }

        if (roles.isEmpty()) {
            Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(()-> new RuntimeException("Default role not found"));
            roles.add(defaultRole);
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, RoleName roleName) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found" + username));

        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new IllegalArgumentException("Role not found" + roleName));

        if (user.getRoles().contains(role)) {
            throw new RuntimeException("User already has role" + roleName);
        }
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public Optional<Role> findRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

