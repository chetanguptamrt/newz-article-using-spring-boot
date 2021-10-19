package com.article.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.article.entity.Admin;
import com.article.repository.AdminRepository;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Admin admin = this.adminRepository.getAdminByUserName(username);
		if(admin==null) {
			throw new UsernameNotFoundException("User doesn't exists");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(admin);
		return customUserDetails;
	}

}
