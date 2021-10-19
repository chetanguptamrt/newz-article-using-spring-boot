package com.article.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.article.entity.Article;
import com.article.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

	public List<Tag> findAllByArticle(Article article);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM tag WHERE article_article_id = :id", nativeQuery = true)
	public void deleteWithSql(@Param("id") int id);
	
	@Query(value = "SELECT COUNT(DISTINCT article_tag) FROM tag", nativeQuery = true)
	public long countDistinct();
	
}
