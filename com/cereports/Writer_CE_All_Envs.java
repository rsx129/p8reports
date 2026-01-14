package com.cereports;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.base.ReportsBase;

public class Writer_CE_All_Envs extends ReportsBase
{

	static Object [] arrKeys = null;
	

	public Writer_CE_All_Envs(Map envs)
	{
//		System.out.println(envs);
		arrKeys = (Object [] ) envs.keySet().toArray();

		System.out.println("Report writer processing for environments:");
		int count = 0;
		for (Object object : arrKeys)
		{

			System.out.println((++count) + ") Preparing report for: " + object.toString());
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
	
	
	public void writeDataMapReport (Map <String, Map<String, String>> dataMap, String firstLinePrefix, String method) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_" + method +".csv")));

		String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}

	
	public void writeOtherArtefactsReport (Map <String, Map<String, String>> dataMap_Entity, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_Other_Artefacts.csv")));

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
	
	
	public void writeTimeReport (Map <String, Map<String, String>> dataMap_Entity, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_Time.csv")));

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
	
	public void writeChoiceListReport (Map <String, Map<String, String>> dataMap, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_ChoiceLists.csv")));

		String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}
	
	public void writeCodeModuleReport (Map <String, Map<String, String>> dataMap, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_CodeModules.csv")));

		String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}
	
	
	public void writeMarkingSetReport (Map <String, Map<String, String>> dataMap, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_MarkingSets.csv")));

		String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}
	
	public void writeClassDefinitionReport (Map <String, Map<String, String>> dataMap, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_ClassDefinitions.csv")));

		String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}
	
	public void writePropertyTemplateReport (Map <String, Map<String, String>> dataMap, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_PropertyTemplates.csv")));

		String firstline = "SN," + firstLinePrefix;
		for (int i = 0; i < arrKeys.length; i++)
		{
			firstline = firstline + "," + arrKeys[i].toString();
		}
		writer.println(firstline);
		Iterator <String> iter = dataMap.keySet().iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			String line = ++counter + "," + key;
			for (int i = 0; i < arrKeys.length; i++)
			{
				line = line + "," +dataMap.get(key).get(arrKeys[i].toString()) ;
			}
			writer.println(line);
		}
		writer.close();
	}
	
	
	
	public void writePermissionReport (Map <String, Map<String, String>> dataMap_Permission, String firstLinePrefix) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_Permission_Multi_Envs.csv")));

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
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_Count_Multi_Envs.csv")));

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
	

	public void writePermissionByObjectReport (List <String> list, String firstLine) throws Exception
	{
		int counter = 0;
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_PermissionByObject.csv")));

	    String firstline = "SN," + firstLine;
		writer.println(firstline);
		Iterator <String> iter = list.iterator();
		while (iter.hasNext()) 
		{
			writer.println((++counter) + "," + iter.next());
		}
		writer.close();
	}

	
	
	public void writePERFDocumentReport (List <String> list, String firstLine, String suffix) throws Exception
	{
	    PrintWriter writer = new PrintWriter(new FileWriter (new File(reportDir, dateStr + "_Report_CE_" + suffix +".csv")));

		writer.println(firstLine);
		Iterator <String> iter = list.iterator();
		while (iter.hasNext()) 
		{
			String key = iter.next();
			writer.println(key);
		}
		writer.close();
	}
	

}
