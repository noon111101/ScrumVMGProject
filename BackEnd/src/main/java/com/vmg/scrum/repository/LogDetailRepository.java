package com.vmg.scrum.repository;

import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface LogDetailRepository extends JpaRepository<LogDetail,Long> {
    @Override
    List<LogDetail> findAll();

    @Override
    Page<LogDetail> findAll(Pageable pageable);

    Page<LogDetail> findByUserCode(Pageable pageable,Double code);

    @Query(value = "select l from LogDetail l\n" +
            " join l.user u " +
            "where u.code = ?1 and l.date_log between ?2 and ?3")
    Page<LogDetail> findByDate(Double code,LocalDate from, LocalDate to, Pageable pageable);


    Page<LogDetail> findByUserDepartmentsId(Pageable pageable,Long id);

    List<LogDetail> findByUserDepartmentsId(Long id);

    List<LogDetail> findByUserCode(Double code);





}
