package com.vmg.scrum.repository;

import com.vmg.scrum.model.excel.LogDetailTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDetailTotalRepository extends JpaRepository<LogDetailTotal,Long> {

}
