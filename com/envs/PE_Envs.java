package com.envs;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PE_Envs extends Base_Envs
{
	private static Map <String, Map<String, String>> allEnvsMap = new LinkedHashMap<String, Map<String, String>>();
	
	public static String env_DEV = prepareDEV();
	public static String env_ST2_Old = prepareST2_Old();
	public static String env_ST2_Cld = prepareST2_Cld();
	public static String env_ST3_Old = prepareST3_Old();
	public static String env_ST3_Cld = prepareST3_Cld();
	public static String env_ST4 = prepareST4();
	public static String env_ST5 = prepareST5();
	public static String env_PERF = preparePERF();
	public static String env_Pre= preparePre();
	public static String env_ = prepare();
	
	public PE_Envs () 
	{

	}
	
	
	public static Map <String, Map <String, String>> getEnvsMap ()
	{
		return Collections.unmodifiableMap(allEnvsMap);
	}
	

	
	private static String prepareDEV ()
	{
		Map <String, String> envMap = new HashMap<String, String>();

		final String serverName = "";
		envMap.put(key_URI, https + serverName + mtom);
		
		final String cpName =  "";

		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String prepareST2_Old ()
	{
		Map <String, String> envMap = new HashMap<String, String>();

		final String serverName = "";
		envMap.put(key_URI, https + serverName + mtom);
		//*/

		final String cpName =  "";

		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String prepareST3_Old ()
	{
		Map <String, String> envMap = new HashMap<String, String>();

		final String serverName = "";
		envMap.put(key_URI, https + serverName + mtom);

		final String cpName =  "";

		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String prepareST2_Cld ()
	{
		Map <String, String> envMap = new HashMap<String, String>();
		//*/
		final String serverName = "";
		envMap.put(key_URI, http + serverName + ":" + mtom);
		/*/
		final String serverName = "ST2";
		envMap.put(key_URI, https + serverName + mtom);
		//*/
		final String cpName =  "";
		
		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String prepareST3_Cld ()
	{
		Map <String, String> envMap = new HashMap<String, String>();
		//*/
		final String serverName = "";
		envMap.put(key_URI, http + serverName + ":" + mtom);
		/*/
		final String serverName = "ST3";
		envMap.put(key_URI, https + serverName + mtom);
		//*/
		final String cpName =  "";
		
		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String prepareST4 ()
	{
		Map <String, String> envMap = new HashMap<String, String>();
		final String serverName = "";
		final String cpName =  "";

		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_URI, http + serverName + mtom);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String prepareST5 ()
	{
		Map <String, String> envMap = new HashMap<String, String>();
		final String serverName = "";
		final String cpName =  "";
		
		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_URI, http + serverName + mtom);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String preparePERF ()
	{
		Map <String, String> envMap = new HashMap<String, String>();
		final String serverName = "";
		final String cpName =  "";

		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_URI, https + serverName + mtom);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String preparePre ()
	{
		Map <String, String> envMap = new HashMap<String, String>();
		final String serverName = "";
		final String cpName =  "";

		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_URI, https + serverName + mtom);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}
	
	private static  String prepare ()
	{
		Map <String, String> envMap = new HashMap<String, String>();
		final String serverName = "";
		final String cpName =  "";

		envMap.put(key_SERVERNAME, serverName);
		envMap.put(key_URI, https + serverName + mtom);
		envMap.put(key_USERNAME, "");
		envMap.put(key_PASSWORD, "");
		envMap.put(key_CONNECTIONPOINT, cpName);
		
		String key = getKey(serverName);
		allEnvsMap.put(key, Collections.unmodifiableMap(envMap));
		return key;
	}


}
