package com.vmg.scrum.repository;

import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDetailRepository extends JpaRepository<LogDetail,Long> {

}
