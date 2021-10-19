package com.article.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.article.entity.Article;
import com.article.service.ArticleService;

@Controller
public class HomeController {

	@Autowired
	private ArticleService articleService;
	
	@ModelAttribute
	public void defaultModel(Model model) {
		model.addAttribute("viewList", this.articleService.showViewedList());
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("title", "Newz Article");
		model.addAttribute("recentList", this.articleService.showRecentList());
		return "normal/index";
	}
	
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about(Model model) {
		model.addAttribute("title", "About | Newz Article");
		return "normal/about";
	}
	
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact(Model model) {
		model.addAttribute("title", "Contact us | Newz Article");
		return "normal/contact";
	}
	
	@RequestMapping(value = "/service", method = RequestMethod.GET)
	public String service(Model model) {
		model.addAttribute("title", "Service | Newz Article");
		return "normal/service";
	}
	
	@RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
	public String article(@PathVariable("id") int id,
							Principal principal,
							Model model) {
		try {
			Article article = this.articleService.getArticleById(id);
			if(!article.isActive() || principal!=null) {
				model.addAttribute("title", article.getArticleName()+" | Newz Article");
				model.addAttribute("article", article);
				model.addAttribute("relatedTag", this.articleService.getRelatedTag(article.getId()));
				this.articleService.increaseViewsByArticleId(article);
				return "normal/article";
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			model.addAttribute("msg","Article not found!");
			return "/error-page";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/search-article", method = RequestMethod.GET)
	public ResponseEntity<List<Article>> search(@RequestParam("q") String search){
		List<Article> articleBySearch = this.articleService.getArticleBySearch(search);
		return ResponseEntity.of(Optional.of(articleBySearch));
	}

	@RequestMapping(value = "/unsubscribe", method = RequestMethod.GET)
	public String unsubscribe(Model model) {
		model.addAttribute("title", "Unsubscribe | Newz Article");
		return "normal/unsubscribe";
	}
	
}
