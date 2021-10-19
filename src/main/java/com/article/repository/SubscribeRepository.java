package com.article.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.article.entity.Subscribe;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

	public boolean existsByEmail(String email);
	
	@Transactional
	public void deleteAllByEmail(String email);
	
}
