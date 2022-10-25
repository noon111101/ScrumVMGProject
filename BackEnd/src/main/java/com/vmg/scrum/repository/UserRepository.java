package com.vmg.scrum.repository;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    List<User> findAllByDepartments_Id(long id);

    User findByCode(Double code);
    @Override
    Page<User> findAll(Pageable pageable);

    Page<User> getUsersByDepartments_Id(long id, Pageable pageable);



}
