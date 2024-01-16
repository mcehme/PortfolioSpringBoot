package com.ehme.michael.repositories;

import com.ehme.michael.DTO.Resume;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends CrudRepository<Resume, Long> {
}