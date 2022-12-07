package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.CategoryReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReasonRepository extends JpaRepository<CategoryReason, Long> {
    List<CategoryReason> findAll();

    Optional<CategoryReason> findById(Long id);
}
