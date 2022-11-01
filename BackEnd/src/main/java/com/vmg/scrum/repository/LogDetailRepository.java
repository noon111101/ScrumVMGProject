package com.vmg.scrum.repository;

import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.response.UserLogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    Page<LogDetail> findByDate_UserCode(Double code, LocalDate from, LocalDate to, Pageable pageable);

    @Query(value = "select l from LogDetail l\n" +
            " join l.user u " +

            "where u.departments.id = ?1 and l.date_log between ?2 and ?3")
    Page<LogDetail> findByDate_DepartmentId(long id, LocalDate from, LocalDate to, Pageable pageable);

    @Query(value = "select l from LogDetail l\n" +
            "where l.date_log between ?1 and ?2")
    Page<LogDetail> findByDate_AllDepartment(LocalDate from, LocalDate to,Pageable pageable);


    @Query(value = " select l from LogDetail l\n " +
            " join l.user u " +
            " where u.departments.id = ?1 " +
            " order by l.date_log desc "
    )
    Page<LogDetail> findByDepartmentId(long id, Pageable pageable);

    @Query(value = "select l from LogDetail l\n " +
            " join l.user u " +
            " order by l.date_log desc "
    )
    Page<LogDetail> findByAllDepartmentId(Pageable pageable);




    @Query(value = "select l from LogDetail l\n" +
        " join l.user u " +
        "where u.departments.id= ?1 and MONTH (l.date_log) = ?2  ")
    List<LogDetail> findByMonthAndDepartment(Long id,Integer month);

    @Query(value = "select l from LogDetail l\n" +
            " join l.user u " +
            "where u.departments.id= ?1 and MONTH (l.date_log) = ?2 " +
            "order by l.date_log asc ")
    List<LogDetail> findByMonthAndDepartmentSortDate(Long id,Integer month);

    @Query(value = "select l from LogDetail l\n" +
            " join l.user u " +
            "where  MONTH (l.date_log) = ?1 " +
            "order by l.date_log asc ")
    List<LogDetail> findByMonthSortDate(Integer month);


    @Query(value = "select l from LogDetail l\n" +
            " join l.user u " +
            "where MONTH (l.date_log) = ?1 ")
    List<LogDetail> findByMonth(Integer month);

    Page<LogDetail> findByUserDepartmentsId(Pageable pageable,Long id);

    List<LogDetail> findByUserDepartmentsId(Long id);

    List<LogDetail> findByUserCode(Double code);

    @Query(value = "select * from log_detail l \n" +
            "join user u on l.user_id = u.id \n " +
            "join department d on d.id = u.department_id\n " +
            "where u.department_id = ?1 " +
            "and l.date_log = ?2 ", nativeQuery = true)
    Page<LogDetail> findDateandDepartment(Integer key, LocalDate date, Pageable pageable);

    @Query(value = "select * from log_detail l \n" +
            "join user u on l.user_id = u.id \n " +
            "join department d on d.id = u.department_id\n " +
            "where l.date_log = ?1 ", nativeQuery = true)
    Page<LogDetail> findByDate(LocalDate date,Pageable pageable);

    @Query(value = "select * from log_detail l \n" +
            "join user u on l.user_id = u.id \n " +
            "join department d on d.id = u.department_id\n " +
            "where u.department_id = ?1 ", nativeQuery = true)
    Page<LogDetail> findByDepartment(Integer key,Pageable pageable);

    @Query(value = "select * from log_detail l \n" +
            "join user u on l.user_id = u.id \n " +
            "join department d on d.id = u.department_id " , nativeQuery = true)
    Page<LogDetail> findAllUser(Pageable pageable);
//
    @Query(value = "select * from log_detail l \n" +
            "join user u on l.user_id = u.id \n " +
            "where u.code = ?1 " +
            "and l.date_log = ?2 ", nativeQuery = true)
    LogDetail findByUserCodeAndDate(Double code , LocalDate date);



}
