package com.tanniafashion.tanniafashion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tanniafashion.tanniafashion.models.Role;
import com.tanniafashion.tanniafashion.models.enums.RoleName;
import com.tanniafashion.tanniafashion.repositories.RoleRepository;

@Component
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.findByName(RoleName.ROLE_USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(RoleName.ROLE_USER);
            roleRepository.save(userRole);
            System.out.println("ROLE_USER created");
        }

        if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(RoleName.ROLE_ADMIN);
            roleRepository.save(adminRole);
            System.out.println("ROLE_ADMIN created");
        }
    }

}
