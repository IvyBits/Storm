package com.github.Icyene.Storm.Email;

import com.github.Icyene.Storm.Storm;

public class EmailLogger {

	public String message;

	public void load() {

	}

	public void addMessage(String mes) {
		message = message + mes;
	}

	public void sendStackTrace(String e) {
		Email emailer = new Email();
		emailer.sendEmail(Providers.YAHOO, "chronowolf@yahoo.ca",
		        Storm.eV.Email_From, Storm.eV.Email_From__Password,
		        e, "[STORM] Storm Crash", 25); // <<Use port 25
	}

}
