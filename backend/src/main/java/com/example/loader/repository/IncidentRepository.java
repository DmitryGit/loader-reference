package com.example.loader.repository;

import com.example.loader.model.Incident;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidentRepository extends CrudRepository<Incident, Long> {

    @Query("SELECT * FROM incidents WHERE loader_id = :loaderId ORDER BY start_time DESC")
    List<Incident> findByLoaderIdOrderByStartTimeDesc(@Param("loaderId") Long loaderId);

}