package com.article.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.article.entity.Article;
import com.article.entity.Views;

@Repository
public interface ViewsRepository extends JpaRepository<Views, Integer> {

	@Query(value = "SELECT * FROM views ORDER BY article_count DESC", nativeQuery = true)
	public List<Views> getDescOrderList();

	public Views getByArticle(Article article);
	
}
