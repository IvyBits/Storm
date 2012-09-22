package com.github.Icyene.Storm.Email;

import org.bukkit.plugin.Plugin;

import com.github.Icyene.Storm.ReflectConfiguration;

public class EmailVariables extends ReflectConfiguration {

	public EmailVariables(Plugin storm, String name) {
		super(storm, name);
	}

	public boolean Email_Enabled = true;
	public String Email_To = "toEmail@email.com";
	public String Email_From = "toEmail@email.com";
	public String Email_From__Password = "Your Password";
	public long Email_Ticks__Between__Emails = 18000;
	public boolean Email_Email__Developers__On__Crash = true;

}
