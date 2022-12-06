package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.CatergoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRequestRepository extends JpaRepository<CatergoryRequest,Long> {
    CatergoryRequest findByName(String name);
}
