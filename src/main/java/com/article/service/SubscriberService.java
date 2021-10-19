package com.article.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.article.entity.Subscribe;
import com.article.repository.SubscribeRepository;

@Service
public class SubscriberService {

	@Autowired
	private SubscribeRepository subscribeRepository;

	@Autowired
	private EmailService emailService;
	
	public String subscribe(String email) {
		if(this.subscribeRepository.existsByEmail(email.trim())) {
			return "done";
		}
		Subscribe subscribe = new Subscribe();
		subscribe.setEmail(email.trim());
		this.subscribeRepository.save(subscribe);
		this.emailService.sendMailToSubscriber(email.trim());
		return "done";
	}
	
	public List<Subscribe> getAllSubscriber(){
		List<Subscribe> findAll = this.subscribeRepository.findAll();
		return findAll;
	}
	
	public void deleteAllSubscriber() {
		this.subscribeRepository.deleteAll();
	}
	
	public String deleteByEmail(String email) {
		this.subscribeRepository.deleteAllByEmail(email.trim());
		return "done";
	}

	public long count() {
		return this.subscribeRepository.count();
	}
}
