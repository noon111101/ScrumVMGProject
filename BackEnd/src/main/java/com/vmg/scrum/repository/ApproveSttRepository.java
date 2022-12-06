package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.ApproveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApproveSttRepository extends JpaRepository<ApproveStatus,Long> {
    ApproveStatus findByName(String name);
}
