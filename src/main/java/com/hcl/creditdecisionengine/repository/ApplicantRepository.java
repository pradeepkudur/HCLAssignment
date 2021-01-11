package com.hcl.creditdecisionengine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<ApplicantDetails, Long>{

	@Query(value = "SELECT * FROM APPLICANT_DETAILS WHERE SSNID = :ssnID ", nativeQuery = true)
	Optional<ApplicantDetails> findBySsnId(@Param("ssnID") long ssnId);
	
}
