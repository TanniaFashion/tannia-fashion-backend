package com.tanniafashion.tanniafashion.services;

import java.util.Optional;

import com.tanniafashion.tanniafashion.models.Role;
import com.tanniafashion.tanniafashion.models.User;
import com.tanniafashion.tanniafashion.models.enums.RoleName;

public interface UserService {
    public User saveUser(User user);
    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);
    void addRoleToUser(String username, RoleName roleName);
    Optional<Role> findRoleByName(RoleName roleName);
    Role saveRole(Role role);
}
