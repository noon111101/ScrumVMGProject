package com.vmg.scrum.repository;

import com.vmg.scrum.entity.ERole;
import com.vmg.scrum.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
        Optional<Role> findByName(ERole name);
}
