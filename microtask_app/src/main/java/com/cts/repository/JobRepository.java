package com.cts.repository;


import com.cts.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> { 

    // You can add custom query methods here later if needed
}