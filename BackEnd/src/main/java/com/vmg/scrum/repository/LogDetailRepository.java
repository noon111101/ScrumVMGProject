package com.vmg.scrum.repository;

import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogDetailRepository extends JpaRepository<LogDetail,Long> {
    @Override
    List<LogDetail> findAll();

    @Override
    Page<LogDetail> findAll(Pageable pageable);


}