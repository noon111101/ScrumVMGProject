package com.vmg.scrum.repository;

import com.vmg.scrum.model.option.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ShiftRepository extends JpaRepository<Shift,Long> {
    Shift findByName(String name);

}
