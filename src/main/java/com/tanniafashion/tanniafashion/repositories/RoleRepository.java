package com.tanniafashion.tanniafashion.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanniafashion.tanniafashion.models.Role;
import com.tanniafashion.tanniafashion.models.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(RoleName name);
}
