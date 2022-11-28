package com.vmg.scrum.repository;

import com.vmg.scrum.model.Holiday;
import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    @Override
    Page<Holiday> findAll(Pageable pageable);

    @Query(value = "select h from Holiday h\n" +
            " where (h.holidayName LIKE %?1%) " +
            " order by h.id desc ")
    Page<Holiday> findAllSearch(String key, Pageable pageable);


}
