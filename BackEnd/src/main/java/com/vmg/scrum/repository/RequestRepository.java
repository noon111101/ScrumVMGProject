package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = "select r from Request r order by r.id desc ")
    Page<Request> findAll(Pageable pageable);

    @Query(value = "select r from Request r " +
            " where (r.title LIKE %?1% or r.creator.fullName LIKE %?1%)" +
            " order by r.id desc ")
    Page<Request> findBySearch(String search, Pageable pageable);

    @Query(value = "select r from Request r " +
            " where r.approveStatus.id = ?1 " +
            " order by r.id desc ")
    Page<Request> findByStatus(Long status, Pageable pageable);

    @Query(value = "select r from Request r " +
            " where (r.title LIKE %?1% or r.creator.fullName LIKE %?1%) " +
            " and r.approveStatus.id = ?2" +
            " order by r.id desc ")
    Page<Request> findBySearchAndStatus(String search, Long status, Pageable pageable);

    @Query(value = "select r from Request r " +
            " where r.creator.departments.id = ?1 " +
            " order by r.id desc ")
    Page<Request> findByDepartmentId(Long departId, Pageable pageable);

    @Query(value = "select r from Request r " +
            " where r.creator.departments.id = ?1 " +
            " and (r.title LIKE %?2% or r.creator.fullName LIKE %?2%) " +
            " order by r.id desc ")
    Page<Request> findByDepartmentIdAndSearch(Long departId, String search, Pageable pageable);

    @Query(value = "select r from Request r " +
            " where r.creator.departments.id = ?1 " +
            " and r.approveStatus.id = ?2 " +
            " order by r.id desc ")
    Page<Request> findByByDepartmentIdAndStatus(Long departId, Long status, Pageable pageable);

    @Query(value = "select r from Request r " +
            " where r.creator.departments.id = ?1 " +
            " and r.approveStatus.id = ?3 " +
            " and (r.title LIKE %?2% or r.creator.fullName LIKE %?2%) " +
            " order by r.id desc ")
    Page<Request> findByByDepartmentIdAndSearchAndStatus(Long departId, String search, Long status, Pageable pageable);

    @Query(value = "select r from Request r " +
            " where r.id = ?1 ")
    Request findByRequestId(Long id);
}
