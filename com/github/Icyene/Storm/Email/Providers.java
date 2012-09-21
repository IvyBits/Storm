package com.github.Icyene.Storm.Email;

public enum Providers {
	YAHOO("smtp.mail.yahoo.com"),
	GMAIL("smtp.gmail.com");

	private String host;

	Providers(String host) {
		this.host = host;

	}

	public String getHost() {
		return host;
	}
}
