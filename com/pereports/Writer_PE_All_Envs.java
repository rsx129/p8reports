package com.pereports;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import com.base.ReportsBase;

public class Writer_PE_All_Envs extends ReportsBase
{

	static Object [] arrKeys = null;
	

	
	public Writer_PE_All_Envs(Object [] arr)
	{
		arrKeys = arr;
		
		for (Object object : arrKeys)
		{
			System.out.println("################################");
			System.out.println(object.toString());
		}

		try
		{
		    if (!reportDir.exists()) {
		        reportDir.mkdirs();
		    }
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void writeEntityReport (Map <String, Map<String, String>> dataMap_Entity, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_PE_Entity_Multi_Envs.csv")));

		String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap_Entity.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap_Entity.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}
	
	public void writePermissionReport (Map <String, Map<String, String>> dataMap_Permission, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_PE_Permission_Multi_Envs.csv")));

	    String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap_Permission.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap_Permission.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}
	
	public void writeCountReport (Map <String, Map<String, String>> dataMap_Count, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_PE_Count_Multi_Envs.csv")));

	    String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap_Count.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap_Count.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}

}
