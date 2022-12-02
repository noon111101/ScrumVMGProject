package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRequestRepository extends JpaRepository<Request, Long> {
    @Override
    Page<Request> findAll(Pageable pageable);


}
