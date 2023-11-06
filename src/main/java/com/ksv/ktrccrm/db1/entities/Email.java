package com.ksv.ktrccrm.db1.entities;

import java.util.Random;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.service.Impl.EmailAndOTPServiceImpl;

//@Component
//@Configuration
@Service
public class Email implements Callable<Boolean> {
	@Autowired
	private JavaMailSender javaMailSender;

	private String from;
	private String to;
	private String subject;
	private String body;

	public Email() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Email(String from, String to, String subject, String body) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	@Override
	public Boolean call() {
		return sendEmailThreads();
	}

	public Boolean sendEmailThreads() {
		Boolean b = false;
		try {
			Random random = new Random();
			int randomSleepTime = random.nextInt(200 - 1 + 1) + 1;
			Thread.sleep(randomSleepTime);

//			System.out.println("Boolen Sender: " + email.getFrom() + " Boolen Receiver: " + email.getTo()
//					+ " Boolen Subject: " + email.getSubject());

			System.out.println("Boolen Sender: " + from + " Boolen Receiver: " + to + " Boolen Subject: " + subject);

//			Boolean b = emailAndOTPService.emailsend(email.getFrom(), email.getTo(), email.getSubject(),
//					email.getBody());

			b = sendSimpleMail(from, to, subject, body);
			System.out.println("SENDEMAILTHREADS CHECK ------ " + b);
			return b;
		} catch (InterruptedException ex) {
			System.out.println("Email cannot sent !!!!!!!! " + ex.getMessage());
			return b;
		} catch (Exception e) {
			System.out.println("emailAndOTPService CATCH ======= " + b);
			System.out.println("emailAndOTPService NOT CALLING +++++++  " + e.getMessage());
			return b;
		}

	}

	public Boolean sendSimpleMail(String from, String to, String subject, String body) {
		try {

			// Creating a simple mail message
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			// Setting up necessary details
			mailMessage.setFrom(from);
			mailMessage.setTo(to);
			mailMessage.setText(body);
			mailMessage.setSubject(subject);

			System.out.println("CHECK MIL INFO ::: " + mailMessage.toString());
			// Sending the mail
			javaMailSender.send(mailMessage);
			System.out.println("Mail Sent Successfully...");
			return true;
		}

		// Catch block to handle the exceptions
		catch (Exception e) {
			System.out.println("Error while Sending Mail..... " + e);
			return false;
		}
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}