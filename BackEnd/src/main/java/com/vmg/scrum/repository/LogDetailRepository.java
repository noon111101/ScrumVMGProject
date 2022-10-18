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

    @Query(value = "select *, DATE(date_log), dayofweek(date_log)," +
            " time(time_in),time(time_out), time(total_work)," +
            " exception, signs_id,user_id from log_detail;\n", nativeQuery = true)
//    @Query("select DATE(date_log), dayofweek(date_log), time(timeIn), " +
//            "time(timeOut), time(totalWork), exception," +
//            " from LogDetail l")
    List<LogDetail> getLogs();

    Page<LogDetail> findByUserCode(Pageable pageable,Double code);
    Page<LogDetail> findByUserDepartmentsId(Pageable pageable,Long id);

    List<LogDetail> findByUserDepartmentsId(Long id);


}
