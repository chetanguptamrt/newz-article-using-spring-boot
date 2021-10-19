package com.article.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.article.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query(value = "SELECT * FROM article WHERE not article_public_or_not ORDER BY article_id DESC LIMIT 5", nativeQuery = true)
	public List<Article> showRecentList();

	@Query(value = "SELECT COUNT(*) FROM article WHERE article_public_or_not = false", nativeQuery = true)
	public long countPublic();
	
	@Query(value = "SELECT COUNT(*) FROM article WHERE article_public_or_not = TRUE", nativeQuery = true)
	public long countPrivate();

	@Query(value = "SELECT * FROM article WHERE article_id IN (SELECT DISTINCT article_article_id FROM tag WHERE article_tag IN (SELECT article_tag FROM tag WHERE article_article_id = :id))AND article_id != :id AND article_public_or_not = false LIMIT 3", nativeQuery = true)
	public List<Article> getRelatedTagByArticle(@Param("id") int id);

	@Query("from Article as a where imagePath = :i")
	public Article getAdminByImagePath(@Param("i") String imagePath);
	
	public List<Article> findTop5ByArticleNameContainingAndActive(String search, boolean active);

}
