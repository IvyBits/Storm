package com.github.StormTeam.Storm.Earthquake;

import com.github.StormTeam.Storm.Database.Database;
import com.github.StormTeam.Storm.Database.drivers.iDriver;

import java.util.HashMap;

public class dbInit {

	public dbInit() {
		iDriver db = Database.Obtain().getEngine();
		
		HashMap<String, HashMap<String, String>> fields = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("TYPE", "VARCHAR");
		data.put("LENGTH", "100");
		data.put("NULL", "false");
		fields.put("id", data);
		
		data = new HashMap<String, String>();
		data.put("TYPE", "TEXT");
		data.put("LENGTH", null);
		data.put("NULL", "false");
		fields.put("locone", data);
		
		data = new HashMap<String, String>();
		data.put("TYPE", "TEXT");
		data.put("LENGTH", null);
		data.put("NULL", "false");
		fields.put("loctwo", data);
		
		db.create("#___quake_quake");
		
	}
	
}
