package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.ApproveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestStatusRepository extends JpaRepository<ApproveStatus, Long> {

}
