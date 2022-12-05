package com.vmg.scrum.repository;

import com.vmg.scrum.model.furlough.Furlough;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FurloughRepository extends JpaRepository<Furlough,Long> {
    List<Furlough> findByYearAndUserId(Long year , Long userId);


}
