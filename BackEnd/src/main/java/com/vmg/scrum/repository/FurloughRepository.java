package com.vmg.scrum.repository;

import com.vmg.scrum.model.furlough.Furlough;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FurloughRepository extends JpaRepository<Furlough,Long> {
    List<Furlough> findByYearAndUserId(Long year , Long userId);


}
