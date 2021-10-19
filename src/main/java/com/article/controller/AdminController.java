package com.article.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.article.entity.Admin;
import com.article.entity.Article;
import com.article.entity.Views;
import com.article.service.AdminService;
import com.article.service.ArticleService;
import com.article.service.SubscriberService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ArticleService articleService;

	@Autowired
	private SubscriberService subscriberService;
	
	@ModelAttribute
	public void defaultModel(Model model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("admin", this.adminService.getAdminById(name));
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String dashBoard(Model model) {
		model.addAttribute("title", "Dashboard | Newz Article");
		model.addAttribute("publicArticle", this.articleService.countPublic());
		model.addAttribute("privateArticle", this.articleService.countPrivate());
		model.addAttribute("totalArticle", this.articleService.count());
		long totalViews=0, minViews=0, maxViews=0;
		List<Views> views = this.articleService.getViews();
		if(views.size()>0) {
			minViews = views.get(0).getCount();
		}
		for(Views view : views) {
			if(view.getCount()>maxViews) {
				maxViews = view.getCount();
			}
			if(view.getCount()<minViews) {
				minViews = view.getCount();
			}
			totalViews+=view.getCount();
		}
		model.addAttribute("maxViews", maxViews);
		model.addAttribute("minViews", minViews);
		model.addAttribute("totalViews", totalViews);
		model.addAttribute("totalTag", this.articleService.countTag());
		model.addAttribute("totalSubscriber", this.subscriberService.count());
		model.addAttribute("totalAdmin", this.adminService.count());
		return "admin/index";
	}
	
	@RequestMapping(value = "/addArticle", method = RequestMethod.GET)
	public String addArticle(Model model) {
		model.addAttribute("title", "Add Article | Newz Article");
		return "admin/add";
	}
	
	@RequestMapping(value = "/editArticle/{id}", method = RequestMethod.GET)
	public String editArticle(@PathVariable("id") int id,
								Model model) {
		Article articleById = this.articleService.getArticleById(id);
		StringBuffer stag = new StringBuffer("");
		try {
			articleById.getTag().forEach(tag->{
				stag.append(tag.getTagName()+",");
			});
		} catch(EntityNotFoundException exception) {
			model.addAttribute("msg", "Page not found!");
			return "error-page";
		}
		model.addAttribute("article", articleById);
		model.addAttribute("tag",stag);
		model.addAttribute("title", "Edit Article | Newz Article");
		return "admin/edit";
	}
	
	@RequestMapping(value = "/showArticle", method = RequestMethod.GET)
	public String showArticle() {
		return "redirect:/admin/showArticle/1";
	}
	
	@RequestMapping(value = "/showArticle/{no}", method = RequestMethod.GET)
	public String showArticleByNo(@PathVariable("no") int no,
								Model model) {
		Page<Article> showArticleByPageNo = this.articleService.getShowArticleByPageNo(no-1);
		model.addAttribute("no", no);
		model.addAttribute("totalPage", showArticleByPageNo.getTotalPages());
		model.addAttribute("totalArticle",showArticleByPageNo.getTotalElements());
		model.addAttribute("articles", showArticleByPageNo.get().collect(Collectors.toList()));
		model.addAttribute("title", "Show Article | Newz Article");
		return "admin/show";
	}
	
	@RequestMapping(value = "/subscriber", method = RequestMethod.GET)
	public String subscriber(Model model) {
		model.addAttribute("title", "Subscriber | Newz Article");
		model.addAttribute("subscribers",this.subscriberService.getAllSubscriber());
		return "admin/subscriber";
	}
	
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String manager(Model model) {
		model.addAttribute("title", "Admin Manager | Newz Article");
		List<Admin> list = this.adminService.getAllAdmin();
		model.addAttribute("adminList",list);
		return "admin/manager";
	}
	
	@ResponseBody
	@RequestMapping(value = "/saveArticle", method = RequestMethod.POST)
	public String saveArticle(@ModelAttribute Article article,
								@RequestParam("tags") String tags,
								@RequestParam(value = "actives", required = false) String active,
								@RequestParam("image") MultipartFile file) {
		if(active!=null)
			article.setActive(true);
		else
			article.setActive(false);
		String done = this.articleService.saveArticle(file, article, tags);
		return done;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
	public String updateArticle(@ModelAttribute Article article,
								@RequestParam("tags") String tags,
								@RequestParam(value = "actives", required = false) String active) {
		if(active!=null)
			article.setActive(true);
		else
			article.setActive(false);
		String done = this.articleService.updateArticle(article, tags);
		return done;
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteArticle", method = RequestMethod.POST)
	public String deleteArticle(@RequestParam("id") int id) {
		String done = this.articleService.deleteArticle(id);
		return done;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteAllSubscriber", method = RequestMethod.POST)
	public String deleteAllSubscriber() {
		this.subscriberService.deleteAllSubscriber();
		return "done";
	}

	@ResponseBody
	@RequestMapping(value = "/changeAdminPassword", method = RequestMethod.POST)
	public String changeAdminPassword(@RequestParam("opassword") String oldPassword, 
										@RequestParam("npassword") String newPassword,
										Principal principal) {
		String name = principal.getName();
		Admin admin = this.adminService.getAdminById(name);
		String done = this.adminService.changeAdminPassword(admin,oldPassword,newPassword);
		return done;
	}
	
	@ResponseBody
	@RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
	public String addAdmin(@RequestParam("name") String name, 
							@RequestParam("email") String email,
							@RequestParam("password") String password ) {
		Admin admin = new Admin();
		admin.setName(name);
		admin.setEmail(email);
		admin.setPassword(password);
		String done = this.adminService.addAdmin(admin);
		return done;
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteAdmin", method = RequestMethod.POST)
	public String deleteAdmin(@RequestParam("id") int id, 
								Principal principal) {
		String name = principal.getName();
		Admin admin = this.adminService.getAdminById(name);
		if(admin.getId()==id) {
			return "no";
		}
		this.adminService.deleteAdmin(id);
		return "done";
	}
	
}
