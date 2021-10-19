package com.article.service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.article.entity.Article;
import com.article.repository.ArticleRepository;

@Component
public class MyFilter implements Filter {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if(!httpServletRequest.getRequestURI().startsWith("/img/article/")) {
			chain.doFilter(httpServletRequest, response);
		} else {
			String requestURI = httpServletRequest.getRequestURI().substring(13);
			Article article = this.articleRepository.getAdminByImagePath(requestURI);
			if(!article.isActive()) {
				chain.doFilter(request, response);
			} else {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//				CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//				String username = customUserDetails.getUsername();
//				Admin admin = this.adminRepository.getAdminByUserName(username);
				if(authentication!=null) {
					chain.doFilter(request, response);
				} else {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.sendError(404);
				}
			} 
		}
	}

}
