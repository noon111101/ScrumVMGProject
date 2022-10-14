package com.vmg.scrum.repository;


import com.vmg.scrum.model.ERole;
import com.vmg.scrum.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
        Optional<Role> findByName(ERole name);
}
