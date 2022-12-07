package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.CategoryReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryReasonRepository extends JpaRepository<CategoryReason, Long> {

    CategoryReason findById(long id);
}
