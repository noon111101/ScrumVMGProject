package com.vmg.scrum.repository;

import com.vmg.scrum.model.ESign;
import com.vmg.scrum.model.Sign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignRepository extends JpaRepository<Sign,Long> {

    Sign findByName(ESign name);
}
