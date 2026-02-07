package com.example.hrms_backend.repositories;

import com.example.hrms_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(String roleName);
}
