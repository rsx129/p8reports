package com.envs;

public class Base_Envs
{
	public static final String key_URI = "URI";
	public static final String key_SERVERNAME = "SERVERNAME";
	public static final String key_OBJECTSTORE = "OBJECTSTORE";
	public static final String key_USERNAME = "USERNAME";
	public static final String key_PASSWORD = "PASSWORD";
	public static final String key_CONNECTIONPOINT = "CONNECTIONPOINT";
	
	public static final String http = "http://";
	public static final String https = "https://";
	
	public static final String mtom = "/wsi/FNCEWS40MTOM/";
	
	public static String getKey (String serverName) 
	{
		return serverName;
	}
	
	public static String getKey (String serverName, String nextLevelString) 
	{
		return serverName + " > " + nextLevelString;
	}
}
