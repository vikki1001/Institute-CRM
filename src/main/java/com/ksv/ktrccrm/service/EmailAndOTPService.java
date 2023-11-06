package com.ksv.ktrccrm.service;

public interface EmailAndOTPService {

	public void emailSend(String from, String to, String subject, String body ,String cc) throws Exception;
	
	public boolean emailsend(String from, String to, String subject, String body) throws Exception;

	public void emailSendForMoreEmployees(String from, String[] to, String subject, String body) throws Exception;

}
