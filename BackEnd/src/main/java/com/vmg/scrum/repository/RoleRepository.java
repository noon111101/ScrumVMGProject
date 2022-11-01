package com.vmg.scrum.repository;


import com.vmg.scrum.model.ERole;
import com.vmg.scrum.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
        Optional<Role> findByName(ERole name);

        Set<Role> findById(long[] id);
}
