package com.github.Icyene.Storm.Email.Tasks;

import org.bukkit.Bukkit;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Email.Email;
import com.github.Icyene.Storm.Email.Providers;

public class EmailTask
{

	private int id;
	private Storm storm;

	public EmailTask(Storm storm)
	{
		this.storm = storm;
	}

	public void run()
	{

		id = Bukkit.getScheduler()
		        .scheduleSyncRepeatingTask(
		                storm,
		                new Runnable()
		                {
			                @Override
			                public void run()
			                {

				                Email emailer = new Email();
				                emailer.sendEmail(Providers.YAHOO,
				                        Storm.eV.Email_To,
				                        Storm.eV.Email_From,
				                        Storm.eV.Email_From__Password,
				                        Storm.eL.message, "[STORM] Storm Log",
				                        25); // <<Use port 25

			                }

		                },
		                Storm.eV.Email_Ticks__Between__Emails,
		                Storm.eV.Email_Ticks__Between__Emails);

	}

	public void stop()
	{
		Bukkit.getScheduler().cancelTask(id);
	}

}
