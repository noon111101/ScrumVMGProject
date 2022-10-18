package com.vmg.scrum.repository;

import com.vmg.scrum.model.option.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    List<Department> findAll();

    Department findByName(String name);
}
