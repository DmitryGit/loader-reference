package com.example.loader.repository;

import com.example.loader.model.Loader;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoaderRepository extends CrudRepository<Loader, Long> {
    @Query("SELECT * FROM loaders WHERE (:number IS NULL OR LOWER(number) LIKE LOWER(CONCAT('%', :number, '%')))")
    List<Loader> findByNumberContainingIgnoreCase(@Param("number") String number);

    @Query("SELECT COUNT(*) > 0 FROM incidents WHERE loader_id = :loaderId")
    boolean hasIncidents(@Param("loaderId") Long loaderId);
}