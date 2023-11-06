package com.ksv.ktrccrm.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class SmsServiceConfiguration {
	private static final Logger LOGGER = LogManager.getLogger(SmsServiceConfiguration.class);

	@Value("${twilio.sms.accountSid}")
	private String ACCOUNTSID;

	@Value("${twilio.sms.authToken}")
	private String AUTHTOKEN;

	@Value("${twilio.sms.fromNumber}")
	private String FROMNUMBER;

	public boolean sendSMS(String toNumber, String message) {
		try {
			Twilio.init(ACCOUNTSID, AUTHTOKEN);

			Message msg = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(FROMNUMBER), message).create();
			System.out.println("HERE IS MY ID ::::::: - " + msg.getSid()); // Unique resource ID created to manage this transaction
			return true;
		} catch (Exception e) {
			LOGGER.error("Error occur while send SMS ... " + ExceptionUtils.getStackTrace(e));
			return false;
		}

	}

	public void receiveSMS(MultiValueMap<String, String> smscallback) {
	}

}
