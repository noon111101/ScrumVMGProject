package com.vmg.scrum.repository;

import com.vmg.scrum.model.furlough.FurloughHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FurloughHistoryRepository extends JpaRepository<FurloughHistory,Long> {
    FurloughHistory findByYearAndUserId(Long year,Long userId);
}
