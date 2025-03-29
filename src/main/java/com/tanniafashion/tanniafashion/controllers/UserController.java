package com.tanniafashion.tanniafashion.controllers;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanniafashion.tanniafashion.dto.UserDTO;
import com.tanniafashion.tanniafashion.models.Role;
import com.tanniafashion.tanniafashion.models.User;
import com.tanniafashion.tanniafashion.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "User Operations")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);

        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User foundUSer = user.get();
        UserDTO userDto = new UserDTO(
            foundUSer.getUsername(),
            foundUSer.getEmail(),
            foundUSer.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );

        return ResponseEntity.ok(userDto);
    }
}
