package com.vmg.scrum.repository;

import com.vmg.scrum.model.ERole;
import com.vmg.scrum.model.Role;
import com.vmg.scrum.model.request.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface OfferRequestRepository extends JpaRepository<Request, Long> {
    @Override
    Page<Request> findAll(Pageable pageable);




}
