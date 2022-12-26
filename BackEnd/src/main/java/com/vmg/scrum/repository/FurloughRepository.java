package com.vmg.scrum.repository;

import com.vmg.scrum.model.furlough.Furlough;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FurloughRepository extends JpaRepository<Furlough,Long> {
    List<Furlough> findByYearAndUserId(Long year , Long userId);

    @Query(value = "select f from Furlough f \n" +
            "where f.user.id = ?2 and f.year = ?1 and f.monthInYear = ?3")
    Furlough findByYearAndUserIdAndMonthInYear(Long year , Long userId , Long month);
}
