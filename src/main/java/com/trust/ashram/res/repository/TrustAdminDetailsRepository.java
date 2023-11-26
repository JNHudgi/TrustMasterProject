package com.trust.ashram.res.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.AdminDetails;

public interface TrustAdminDetailsRepository extends JpaRepository<AdminDetails, Integer> {
	
	Optional<AdminDetails> findByAemail(String aemail);

	@Query("SELECT COUNT(aemail) FROM AdminDetails WHERE aemail=:aemail")
	public Integer getEmailCount(String aemail);
	
}
