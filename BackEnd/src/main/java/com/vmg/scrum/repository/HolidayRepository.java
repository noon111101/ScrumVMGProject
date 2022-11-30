package com.vmg.scrum.repository;

import com.vmg.scrum.model.Holiday;

import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.vmg.scrum.payload.response.MessageResponse;

import java.util.Optional;

@Repository

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    @Override
    Page<Holiday> findAll(Pageable pageable);

    @Query(value = "select h from Holiday h\n" +
            " order by h.id desc")
    Page<Holiday> findAllHolidays(Pageable pageable);

    @Query(value = "select h from Holiday h\n" +
            " where YEAR(h.dateFrom) = ?1 " +
            " order by h.id desc")
    Page<Holiday> findAllHolidaysByYear(int year, Pageable pageable);
    @Query(value = "select h from Holiday h\n" +
            " where h.holidayName LIKE %?1% " +
            " order by h.id desc")
    Page<Holiday> findAllSearch(String key, Pageable pageable);
    @Query(value = "select h from Holiday h\n" +
            " where h.holidayName LIKE %?1% " +
            " and YEAR(h.dateFrom) = ?2 " +
            " order by h.id desc")
    Page<Holiday> findAllSearchAndYear(String key,int year, Pageable pageable);
    
    Optional<Holiday> findByHolidayName(String name);

    Boolean existsByHolidayName(String username);

    @Query(value = "SELECT * FROM holiday h \n" +
            "where h.date_from <= ?1"+"and h.date_to>= ?1",nativeQuery = true)
    Holiday findCurrentDate(String date);


}
