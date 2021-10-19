package com.article.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private int id;
	
	@Column(name = "article_name")
	private String articleName;
	
	@Column(name = "article_author")
	private String articleAuthor;
	
	@Column(name = "article_image_path")
	private String imagePath;
	
	@Lob
	@Column(name = "article_content")
	private String content;
	
	@Column(name = "article_publish_date")
	private Date publishDate;
	
	@Column(name = "article_edit_or_not")
	private boolean edit;
	
	@Column(name = "article_public_or_not")
	private boolean active;
	
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "article", cascade = CascadeType.ALL)
	private Views views;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "article", cascade = CascadeType.ALL)
	private List<Tag> tag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getArticleAuthor() {
		return articleAuthor;
	}

	public void setArticleAuthor(String articleAuthor) {
		this.articleAuthor = articleAuthor;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Views getViews() {
		return views;
	}

	public void setViews(Views views) {
		this.views = views;
	}

	public List<Tag> getTag() {
		return tag;
	}

	public void setTag( List<Tag> tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return "Article [id=" + id + ", articleName=" + articleName + ", articleAuthor=" + articleAuthor
				+ ", imagePath=" + imagePath + ", content=" + content + ", publishDate=" + publishDate + ", edit="
				+ edit + ", active=" + active + ", views=" + views.getCount() + "]";
	}

}
