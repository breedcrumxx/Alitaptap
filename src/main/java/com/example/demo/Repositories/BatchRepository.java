package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Batch;

public interface BatchRepository extends JpaRepository<Batch, Integer> {

    @Query(value = "select * from batch where id=:id", nativeQuery = true)
    Batch findBatchById(@Param("id") int id);

    @Query(value = "select * from batch where id=:batchid", nativeQuery = true)
    Batch getBatchById(@Param("batchid") int batchid);
    
}
