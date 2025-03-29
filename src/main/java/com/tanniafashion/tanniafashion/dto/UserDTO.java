package com.tanniafashion.tanniafashion.dto;

import java.util.Set;

import com.tanniafashion.tanniafashion.models.enums.RoleName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private Set<RoleName> roles;
}
