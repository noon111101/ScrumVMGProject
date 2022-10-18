package com.vmg.scrum.controller;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    @Autowired
    DepartmentRepository departmentRepository;
    @GetMapping
    public List<Department> getDepartments(){
        return departmentRepository.findAll();
    }
}
