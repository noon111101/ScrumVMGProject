package com.vmg.scrum.repository;

import com.vmg.scrum.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Position findById(long id);

    Position findByName(String name);

    List<Position> findAll();
}
