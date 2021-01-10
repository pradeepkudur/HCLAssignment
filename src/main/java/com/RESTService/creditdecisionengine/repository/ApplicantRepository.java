package com.RESTService.creditdecisionengine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicantRepository extends JpaRepository<ApplicantDetails, Long>{

	@Query(value = "SELECT * FROM APPLICANT_DETAILS WHERE SSNID = ?1 AND APPLIED_DATE >sysdate-?2", nativeQuery = true)
	Optional<ApplicantDetails> findBySsnId(long ssnId,
			int daysNotAllowedToCheckCreditScore);

}
