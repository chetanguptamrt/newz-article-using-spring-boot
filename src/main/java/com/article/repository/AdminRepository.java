package com.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.article.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{

	@Query("from Admin as a where email = :e")
	public Admin getAdminByUserName(@Param("e") String email);
	
	public boolean existsByEmail(String email);
	
}
