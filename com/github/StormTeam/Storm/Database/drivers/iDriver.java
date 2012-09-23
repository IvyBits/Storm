package com.github.StormTeam.Storm.Database.drivers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Giant
 */
public interface iDriver {
	
	public void close();
	
	public boolean isConnected();
	public boolean tableExists(String table);
	
	public void buildQuery(String string);
	public void buildQuery(String string, Boolean add);
	public void buildQuery(String string, Boolean add, Boolean finalize);
	public void buildQuery(String string, Boolean add, Boolean finalize, Boolean debug);
	public void buildQuery(String string, Boolean add, Boolean finalize, Boolean debug, Boolean table);
	
	public void buildQuery(String string, Integer add);
	public void buildQuery(String string, Integer add, Boolean finalize);
	public void buildQuery(String string, Integer add, Boolean finalize, Boolean debug);
	public void buildQuery(String string, Integer add, Boolean finalize, Boolean debug, Boolean table);
	
	public ArrayList<HashMap<String, String>> execQuery();
	public ArrayList<HashMap<String, String>> execQuery(Integer queryID);
	
	public void updateQuery();
	public void updateQuery(Integer queryID);
	
	public iDriver select(String field);
	public iDriver select(String... fields);
	public iDriver select(ArrayList<String> fields);
	public iDriver select(HashMap<String, String> fields);
	
	public iDriver from(String table);
	
	public iDriver where(HashMap<String, String> fields);
	public iDriver where(HashMap<String, HashMap<String, String>> fields, Boolean shite);
	
	public iDriver orderBy(HashMap<String, String> fields);
	
	public iDriver limit(int limit);
	public iDriver limit(int limit, Integer start);
	
	public iDriver update(String table);
	
	public iDriver set(HashMap<String, String> fields);
	public iDriver set(HashMap<String, HashMap<String, String>> fields, Boolean shite);
	
	public iDriver insert(String table, ArrayList<String> fields, HashMap<Integer, HashMap<String, String>> values);
	public iDriver insert(String table, ArrayList<String> fields, ArrayList<HashMap<Integer, HashMap<String, String>>> values);
	
	public iDriver delete(String table);
	
	public iDriver Truncate(String table);
	
	public iDriver create(String table);
	public iDriver fields(HashMap<String, HashMap<String, String>> fields);

	public iDriver alter(String table);
	public iDriver add(HashMap<String, HashMap<String, String>> fields);
	
	public iDriver debug(Boolean dbg);
	public iDriver Finalize();
	public iDriver debugFinalize(Boolean dbg);
}
