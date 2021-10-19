package com.article.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.article.helper.Email;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;

	class MailSendToSubscriber implements Runnable {
		
		private String to;
		
		public MailSendToSubscriber(String to){
			this.to = to;
		}
		
		@Override
		public void run() {
			this.sendMessage();
		}
		
		private void sendMessage() {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			try {
				helper.setTo(to);
				helper.setSubject(Email.SUBJECT);
				helper.setText(Email.HTML_SUBSCRIBER_BODY, true); // Use this or above line.
				helper.setFrom("Newz Article <no-reply@gmail.com>");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			javaMailSender.send(mimeMessage);
		}
	}

	class MailSendAfterArticle implements Runnable {
		
		private String to;
		private String subject;
		private int id;
		
		public MailSendAfterArticle(String to, String subject, int id){
			this.to = to;
			this.subject = subject;
			this.id = id;
		}
		
		@Override
		public void run() {
			this.sendMessage();
		}
		
		private void sendMessage() {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			try {
				helper.setTo(this.to);
				helper.setSubject(this.subject);
				helper.setText("Hi,<br/><br/>"
						+ "Read Article - <br/>"
						+ "<a href='http://localhost:8080/article/"+this.id+"'>"+subject+"</a><br/><br/>"
						+ "Thanks<br/>"
						+ "Newz Article",
						true); // Use this or above line.
				helper.setFrom("Newz Article <no-reply@gmail.com>");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			javaMailSender.send(mimeMessage);
		}
	}
	
	public void sendMailToSubscriber(String email) {
		Thread thread = new Thread( new MailSendToSubscriber(email));
		thread.start();
	}
	
	public void mailSendAfterArticle(String email, String subject, int id) {
		Thread thread = new Thread( new MailSendAfterArticle(email,subject,id));
		thread.start();
	}
	
}
