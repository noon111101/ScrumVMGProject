package com.vmg.scrum.repository;

import com.vmg.scrum.model.ENoteCatergory;
import com.vmg.scrum.model.NoteCatergory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteCatergoryRepository extends JpaRepository<NoteCatergory,Long> {
    NoteCatergory findByName(ENoteCatergory name);
}
