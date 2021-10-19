package com.article.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.article.entity.Admin;
import com.article.repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public Admin getAdminById(String email) {
		return this.adminRepository.getAdminByUserName(email);
	}
	
	public String addAdmin(Admin admin) {
		if(this.adminRepository.existsByEmail(admin.getEmail())) {
			return "alreadyUser";
		}
		admin.setRole("ROLE_ADMIN");
		admin.setPassword(this.bCryptPasswordEncoder.encode(admin.getPassword()));
		this.adminRepository.save(admin);
		return "done";
	}
	
	public List<Admin> getAllAdmin(){
		List<Admin> list = this.adminRepository.findAll();
		return list;
	}
	
	public void deleteAdmin(int id) {
		this.adminRepository.deleteById(id);
	}

	public String changeAdminPassword(Admin admin, String oldPassword, String newPassword) {
		if(this.bCryptPasswordEncoder.matches(oldPassword,admin.getPassword())) {
			admin.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.adminRepository.save(admin);
			return "done";
		} else {
			return "notMatched";
		}
	}
	
	public long count() {
		return this.adminRepository.count();
	}
	
}
