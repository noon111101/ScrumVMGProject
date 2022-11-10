package com.vmg.scrum.repository;

import com.vmg.scrum.model.option.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    List<Department> findAll();

    Department findByName(String name);
}
