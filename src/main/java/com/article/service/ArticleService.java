package com.article.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.article.entity.Article;
import com.article.entity.Tag;
import com.article.entity.Views;
import com.article.repository.ArticleRepository;
import com.article.repository.SubscribeRepository;
import com.article.repository.TagRepository;
import com.article.repository.ViewsRepository;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private ViewsRepository viewsRepository;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SubscribeRepository subscribeRepository;
	
	public Article getArticleById(int id) {
		return this.articleRepository.getById(id);
	} 
	
	public synchronized void  increaseViewsByArticleId(Article article) {
		Views views = this.viewsRepository.getByArticle(article);
		views.setCount(views.getCount()+1);
		this.viewsRepository.save(views);
	}
	
	public List<Article> getRelatedTag(int id){
		return this.articleRepository.getRelatedTagByArticle(id);
	}
	
	public String saveArticle(MultipartFile file, Article article, String tags) {
		//check data validate
		if(file.isEmpty()
				|| article.getArticleName().trim().equals("")
				|| article.getArticleAuthor().trim().equals("")
				|| article.getContent().trim().equals("")) {
			return "noDetail";
		}
		if(!(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png"))) {
			return "invalidFile";
		}
		//file extension check
		String extension = null;
		if(file.getContentType().equals("image/png")) {
			extension = ".png";
		} else if(file.getContentType().equals("image/jpeg")) {
			extension = ".jpg";
		}
		//default attribute of article
		Date date = new Date();
		article.setEdit(false);
		article.setPublishDate(date);
		//article views
		Views views = new Views();
		views.setCount(0);
		views.setArticle(article);
		article.setViews(views);
		//article tag
		List<Tag> list = new ArrayList<Tag>();
		String[] split = tags.split(",");
		for(int i = 0; i<split.length; i++) {
			if(!split[i].trim().equals("")) {
				Tag tag = new Tag();
				tag.setArticle(article);
				tag.setTagName(split[i].trim());
				list.add(tag);
			}
		}
		article.setTag(list);
		//save article
		Article article2 = this.articleRepository.save(article);
		try {
			Files.copy(file.getInputStream(),
					Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
							java.io.File.separator+"img"+
							java.io.File.separator+"article"+
							java.io.File.separator+article.getId()+"_"+date.getTime()+extension),
					StandardCopyOption.REPLACE_EXISTING);
			article2.setImagePath(article.getId()+"_"+date.getTime()+extension);
			this.articleRepository.save(article2);
		} catch (IOException e) {
			this.articleRepository.delete(article2);
			return "fileError";
		}
		if(!article.isActive()) {
			this.subscribeRepository.findAll().forEach(subs->{
				this.emailService.mailSendAfterArticle(subs.getEmail(), article.getArticleName(), article.getId());
			});
		}
		return "done";
	}
	
	public String updateArticle(Article article, String tags) {
		//check data validate
		if(article.getArticleName().trim().equals("")
				|| article.getArticleAuthor().trim().equals("")
				|| article.getContent().trim().equals("")) {
			return "noDetail";
		}
		Article oldArticle = this.articleRepository.getById(article.getId());
		oldArticle.setEdit(true);
		oldArticle.setActive(article.isActive());
		oldArticle.setArticleAuthor(article.getArticleAuthor());
		oldArticle.setArticleName(article.getArticleName());
		oldArticle.setContent(article.getContent());
		//article tag
		this.tagRepository.deleteWithSql(oldArticle.getId());
		List<Tag> list = new ArrayList<Tag>();
		String[] split = tags.split(",");
		for(int i = 0; i<split.length; i++) {
			if(!split[i].trim().equals("")) {
				Tag tag = new Tag();
				tag.setArticle(oldArticle);
				tag.setTagName(split[i].trim());
				list.add(tag);
			}
		}
		System.out.println(list);
		oldArticle.setTag(list);
		//save article
		this.articleRepository.save(oldArticle);
		return "done";
	}
	
	public Page<Article> getShowArticleByPageNo(int no){
		Page<Article> findAll = this.articleRepository.findAll(PageRequest.of(no, 10, Sort.by(Sort.Direction.DESC, "id")));
		return findAll;
	}

	public String deleteArticle(int id) {
		try {
			Article article = this.articleRepository.getById(id);
			Files.deleteIfExists(Path.of(new ClassPathResource("static").getFile().getAbsolutePath()+
											File.separator+"img"+
											File.separator+"article"+
											File.separator+article.getImagePath()));
			this.articleRepository.delete(article);
			return "done";
		} catch (Exception e) {
			return "no";
		}
	}

	public List<Article> showRecentList(){
		return this.articleRepository.showRecentList();
	}

	public List<Article> showViewedList(){
		List<Views> views = this.viewsRepository.getDescOrderList();
		List<Article> articles = new ArrayList<Article>();
		for(Views view : views) {
			if(view.getArticle().isActive()) {
				continue;
			}
			articles.add(view.getArticle());
			if(articles.size()==3) {
				break;
			}
		}
		return articles;
	}

	public long countPublic() {
		return this.articleRepository.countPublic();
	}
	
	public long countPrivate() {
		return this.articleRepository.countPrivate();
	}

	public long count() {
		return this.articleRepository.count();
	}
	
	public long countTag() {
		return this.tagRepository.countDistinct();
	}
	
	public List<Views> getViews(){
		return this.viewsRepository.findAll();
	}

	public List<Article> getArticleBySearch(String search) {
		return this.articleRepository.findTop5ByArticleNameContainingAndActive(search,false);
	}
	
}
