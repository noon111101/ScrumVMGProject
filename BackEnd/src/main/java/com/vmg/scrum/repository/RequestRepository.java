package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = "select * from request r \n" +
            "join request_approvers ra on r.request_id=ra.request_id\n" +
            "left join request_followers rf on ra.request_id=rf.request_id\n" +
            "where (rf.user_id = ?1 or ra.user_id= ?1)\n" +
            "group by r.request_id\n" +
            "order by r.request_id desc;", nativeQuery = true)
    List<Request> findAll(long user_id);

    @Query(value = "select * from request r \n" +
            "join request_approvers ra on r.request_id=ra.request_id\n" +
            "left join request_followers rf on ra.request_id=rf.request_id\n" +
            "join user u on u.user_id=r.creator_id\n" +
            "where (ra.user_id = ?1 or rf.user_id= ?1)\n" +
            "and (r.title LIKE %?2% or u.full_name LIKE %?2%)\n" +
            "group by r.request_id\n" +
            "order by r.request_id desc;", nativeQuery = true)
    List<Request> findBySearch(long user_id, String search);

    @Query(value = "select * from request r \n" +
            "join request_approvers ra on r.request_id=ra.request_id\n" +
            "left join request_followers rf on ra.request_id=rf.request_id\n" +
            "join user u on u.user_id=r.creator_id\n" +
            "where (ra.user_id = ?1 or rf.user_id= ?1)\n" +
            "and  (r.approve_status_id=?2 )\n" +
            "group by r.request_id\n" +
            "order by r.request_id desc;", nativeQuery = true)
    List<Request> findByStatus(long user_id,Long status);

    @Query(value = "select r from Request r " +
            " where r.approveStatus.id = ?1 " +
            " order by r.id desc ")
    List<Request> findByStatusList(Long status);

    @Query(value = "select * from request r \n" +
            "join request_approvers ra on r.request_id=ra.request_id\n" +
            "left join request_followers rf on ra.request_id=rf.request_id\n" +
            "join user u on u.user_id=r.creator_id\n" +
            "where (ra.user_id = ?1 or rf.user_id= ?1)\n" +
            "and  (u.department_id=?2)\n" +
            "group by r.request_id\n" +
            "order by r.request_id desc;", nativeQuery = true)
    List<Request> findByDepartmentId(long user_id, Long departId);

    @Query(value = "select * from request r \n" +
            "join request_approvers ra on r.request_id=ra.request_id\n" +
            "left join request_followers rf on ra.request_id=rf.request_id\n" +
            "join user u on u.user_id=r.creator_id\n" +
            "where (ra.user_id = ?1 or rf.user_id= ?1)\n" +
            "and  (u.department_id=?2)\n" +
            "and (r.title LIKE %?3% or u.full_name LIKE %?3%)\n" +
            "group by r.request_id\n" +
            "order by r.request_id desc;", nativeQuery = true)
    List<Request> findByDepartmentIdAndSearch(long user_id, Long departId, String search);

    @Query(value = "select * from request r \n" +
            "join request_approvers ra on r.request_id=ra.request_id\n" +
            "left join request_followers rf on ra.request_id=rf.request_id\n" +
            "join user u on u.user_id=r.creator_id\n" +
            "where (ra.user_id = ?1 or rf.user_id= ?1)\n" +
            "and  (u.department_id=?2)\n" +
            "and  (r.approve_status_id=?3 )\n" +
            "group by r.request_id\n" +
            "order by r.request_id desc;", nativeQuery = true)
    List<Request> findByByDepartmentIdAndStatus(long userId, Long departId, Long status);

    @Query(value = "select * from request r \n" +
            "join request_approvers ra on r.request_id=ra.request_id\n" +
            "left join request_followers rf on ra.request_id=rf.request_id\n" +
            "join user u on u.user_id=r.creator_id\n" +
            "where (ra.user_id = ?1 or rf.user_id= ?1)\n" +
            "and  (u.department_id=?2)\n" +
            "and (r.title LIKE %?3% or u.full_name LIKE %?3%)\n" +
            "and  (r.approve_status_id=?4)\n" +
            "group by r.request_id\n" +
            "order by r.request_id desc;", nativeQuery = true)
    List<Request> findByByDepartmentIdAndSearchAndStatus(long userId, Long departId, String search, Long status);


    @Query(value = "select r from Request r " +
            " where r.id = ?1 ")
    Request findByRequestId(Long id);

    @Query(value = "select r from Request r " +
            " where r.creator.id = ?1 " +
            " order by r.id desc ")
    List<Request> findByRequestCreatorId(long id);

    @Query(value = "select r from Request r " +
            " where r.approveStatus.id = ?2 " +
            " and r.creator.id = ?1 " +
            " order by r.id desc ")
    List<Request> findByRequestCreatorIdAndStatus(long id, long status);


}
