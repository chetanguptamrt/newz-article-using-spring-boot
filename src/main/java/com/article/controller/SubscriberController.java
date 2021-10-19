package com.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.article.service.SubscriberService;

@Controller
public class SubscriberController {

	@Autowired
	private SubscriberService subscriberService;
	
	@ResponseBody
	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public String subscribe(@RequestParam("email") String email) {
		String done = this.subscriberService.subscribe(email);
		return done;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteSubscriberByEmail", method = RequestMethod.POST)
	public String deleteSubscriberByEmail(@RequestParam("email") String email) {
		this.subscriberService.deleteByEmail(email);
		return "done";
	}
	
}
