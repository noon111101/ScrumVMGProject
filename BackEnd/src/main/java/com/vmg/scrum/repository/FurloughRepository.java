package com.vmg.scrum.repository;

import com.vmg.scrum.model.furlough.Furlough;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FurloughRepository extends JpaRepository<Furlough,Long> {

}
