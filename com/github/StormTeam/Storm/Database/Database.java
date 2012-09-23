package com.github.StormTeam.Storm.Database;


import org.bukkit.plugin.Plugin;

import com.github.StormTeam.Storm.Database.drivers.SQLite;
import com.github.StormTeam.Storm.Database.drivers.iDriver;

import java.util.HashMap;

/**
 *
 * @author Giant
 */
public class Database {
	
	private enum dbType {
		SQLite("SQLite");
		
		String value;
		
		private dbType(String s) {
			this.value = s;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	private static HashMap<String, Database> instance = new HashMap<String, Database>();
	private iDriver dbDriver;
	private dbType t;
	
	private Database(Plugin p, String instance) {
		if(instance == null)
			instance = "0";
		
		t = dbType.SQLite;
		this.dbDriver = SQLite.Obtain(p, instance);
	}
	
	public iDriver getEngine() {
		return this.dbDriver;
	}
	
	public String getType() {
		return t.toString();
	}
	
	public static Database Obtain() {
		String instance = "0";
		
		if(!Database.instance.containsKey(instance))
			return null;
		
		return Database.instance.get(instance);
	}
	
	public static Database Obtain(String instance) {
		if(instance == null)
			instance = "0";
		
		if(!Database.instance.containsKey(instance))
			return null;
		
		return Database.instance.get(instance);
	}
	
	public static Database Obtain(Plugin p, String instance) {
		if(instance == null)
			instance = "0";
		
		if(!Database.instance.containsKey(instance))
			Database.instance.put(instance, new Database(p, instance));
		
		return Database.instance.get(instance);
	}
}
