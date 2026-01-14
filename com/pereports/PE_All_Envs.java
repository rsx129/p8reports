package com.pereports;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.envs.PE_Envs;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.apiimpl.core.AccessPermissionImpl;

import filenet.vw.api.VWApplicationSpace;
import filenet.vw.api.VWAttributeInfo;
import filenet.vw.api.VWException;
import filenet.vw.api.VWLog;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWRole;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWSession;

public class PE_All_Envs
{
	static VWSession vwSession = null;
	static String SERVER_NAME = null;
	static String CONNECTION_POINT = null;
	static String innerMapKey = null;

	static SortedMap<String, Map<String, String>> dataMap_Entity = new TreeMap<String, Map<String, String>>();
	static SortedMap<String, Map<String, String>> dataMap_Permission = new TreeMap<String, Map<String, String>>();
	static SortedMap<String, Map<String, String>> dataMap_Counts = new TreeMap<String, Map<String, String>>();

	private static Map<String, String> prepareEmptyMap()
	{
		Map<String, String> map = new TreeMap<String, String>();

		Map<String, Map<String, String>> envs = PE_Envs.getEnvsMap();
		Iterator<String> i = (Iterator<String>) envs.keySet().iterator();

		while (i.hasNext())
		{
			String serverName = i.next();
			Map<String, String> env = envs.get(serverName);
			String cpName = env.get(PE_Envs.key_CONNECTIONPOINT);
			String key = PE_Envs.getKey(serverName, cpName);
			//System.out.println("Key: " + key);
			map.put(key, "No");
		}
		return map;
	}

	private static VWSession connectToEnv(Map<String, String> envMap)
	{
		if (vwSession != null)
		{
			vwSession = null;
		}

		SERVER_NAME = envMap.get(PE_Envs.key_SERVERNAME);
		CONNECTION_POINT = envMap.get(PE_Envs.key_CONNECTIONPOINT);

		innerMapKey = PE_Envs.getKey(SERVER_NAME, CONNECTION_POINT);

		String URI = envMap.get(PE_Envs.key_URI);
		System.out.println("Connecting to URI: " + URI);

		String USERNAME = envMap.get(PE_Envs.key_USERNAME);
		String PASSWORD = envMap.get(PE_Envs.key_PASSWORD);

		vwSession = new VWSession();

		try
		{
			vwSession.setBootstrapCEURI(URI);
			vwSession.logon(USERNAME, PASSWORD, CONNECTION_POINT);
			System.out.println("VW Session: " + vwSession);
		} 
		catch (VWException e)
		{
			System.err.println(e.getMessage());

		}
		return vwSession;
	}

	public void processEntityCounts(String str, int count)
	{
		String dataMap_Count_Key = str;

		if (dataMap_Counts.containsKey(dataMap_Count_Key))
		{
			Map<String, String> innerMap = dataMap_Counts.get(dataMap_Count_Key);
			innerMap.put(innerMapKey, Integer.toString(count));
		} else
		{

			Map<String, String> newInnerMap = prepareEmptyMap();
			newInnerMap.put(innerMapKey, Integer.toString(count));
			dataMap_Counts.put(dataMap_Count_Key, newInnerMap);
		}
	}

	public void iterateAPL(AccessPermissionList apl, int countEntity, String type, String displayName, String name,
			String containedItems)
	{
		String dataMap_Entity_Key = type + "," + displayName + "," + name;
		if (dataMap_Entity.containsKey(dataMap_Entity_Key))
		{
			Map<String, String> innerMap = dataMap_Entity.get(dataMap_Entity_Key);
			innerMap.put(innerMapKey, containedItems);
		} else
		{

			Map<String, String> newInnerMap = prepareEmptyMap();
			newInnerMap.put(innerMapKey, containedItems);
			dataMap_Entity.put(dataMap_Entity_Key, newInnerMap);
		}

		if (apl != null)
		{
			Iterator i2 = apl.iterator();
			while (i2.hasNext())
			{
				AccessPermissionImpl aplimpl = (AccessPermissionImpl) i2.next();
				String dataMap_Permission_Key = aplimpl.get_GranteeType().toString() + "," + aplimpl.get_GranteeName()
						+ "," + aplimpl.get_AccessType();

				if (dataMap_Permission.containsKey(dataMap_Permission_Key))
				{
					Map<String, String> innerMap = dataMap_Permission.get(dataMap_Permission_Key);
					innerMap.put(innerMapKey, "Yes");
				} else
				{

					Map<String, String> newInnerMap = prepareEmptyMap();
					newInnerMap.put(innerMapKey, "Yes");
					dataMap_Permission.put(dataMap_Permission_Key, newInnerMap);
				}
			}
		}
	}
	
	public void appSpace()
	{
		int countEntity = 0;

		String[] appspaceNames = vwSession.fetchAppSpaceNames(true);
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + "> " + method);
		for (String name : appspaceNames)
		{
			countEntity++;
			VWApplicationSpace appSpace = vwSession.fetchApplicationSpace(name, VWSession.PRIVILEGES_CONFIGURATION);
			iterateAPL(null, countEntity, method, appSpace.getAuthoredName(), appSpace.getName(), "Yes");

			VWRole[] roles = appSpace.getRoles();
			for (VWRole role : roles)
			{
				iterateAPL(null, countEntity, "AppSpace-Role", "AppSpace ( " + appSpace.getAuthoredName() + " )",
						"Role ( " + role.getName() + " )", "Yes");
			}
		}
		processEntityCounts(method, countEntity);
	}

	public void eventLog()
	{
		int countEntity = 0;
		int counter = 0;
	
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.print(method + ": ");

		String[] eventLogNames = vwSession.fetchEventLogNames(); 
		for (String name : eventLogNames)
		{
			counter++;
			countEntity++;
			VWLog vwLog = vwSession.fetchEventLog(name);
			iterateAPL(null, countEntity, method, vwLog.getAuthoredName(), vwLog.getName(), "Yes");
		}
		System.out.println(" ( " + counter + " ) ");
		processEntityCounts(method, countEntity);
	}

	public void roster()
	{

		int countEntity = 0;
		int counter = 0;
		int fetchCount = 0;
		
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.print(method + ": ");
		
		String[] rosterNames = vwSession.fetchRosterNames(true);
		for (String name : rosterNames)
		{
			counter++;
			countEntity++;
			VWRoster roster = vwSession.getRoster(name);
			fetchCount += roster.fetchCount();
			iterateAPL(null, countEntity, method, roster.getAuthoredName(), roster.getName(),
					"Yes ( " + String.valueOf(fetchCount) + " )");

		}
		System.out.println(" ( " + counter + " ) , contains: " + fetchCount);
		iterateAPL(null, ++countEntity, "Count", "All Rosters", "All Workitems", String.valueOf(fetchCount));
		processEntityCounts(method, countEntity);
	}

	public void queue()
	{
		int countEntity = 0;
		int counter = 0;
		int fetchCount = 0;
		
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.print(method + ": ");

		String[] queueNames = vwSession.fetchQueueNames(VWSession.QUEUE_IGNORE_SECURITY | VWSession.QUEUE_PROCESS
				| VWSession.QUEUE_SYSTEM | VWSession.QUEUE_USER_CENTRIC | VWSession.QUEUE_USER_CENTRIC_FOR_USER_ONLY);
		for (String name : queueNames)
		{
			counter++;
			countEntity++;
			VWQueue queue = vwSession.getQueue(name);
			fetchCount += queue.fetchCount();
			iterateAPL(null, countEntity, method, queue.getAuthoredName(), queue.getName(),
					"Yes ( " + String.valueOf(fetchCount) + " )");
		}
		System.out.println(" ( " + counter + " ) , contains: " + fetchCount);
		iterateAPL(null, ++countEntity, "Count", "All Queues", "All Workitems", String.valueOf(fetchCount));
		processEntityCounts(method, countEntity);
		
	}
	
	


	public static void main(String[] args)
	{
		PE_All_Envs sr = new PE_All_Envs();
		Map<String, Map<String, String>> envs = PE_Envs.getEnvsMap();
		Iterator i = envs.keySet().iterator();
		int counter = 0;
		while (i.hasNext())
		{
			String envKey = (String) i.next();
			try
			{
				System.out.println("\n" + ++counter + ") Environment: " + envKey);
				vwSession = connectToEnv(envs.get(envKey));
				sr.eventLog();
				sr.roster();
				sr.queue();
				sr.appSpace();
			} catch (Exception e1)
			{
				System.out.println(e1.getMessage());
			}
		}

		Writer_PE_All_Envs report = new Writer_PE_All_Envs(prepareEmptyMap().keySet().toArray());

		try
		{
			report.writeEntityReport(dataMap_Entity, "EntityType,DisplayName, Name");
			// report.writePermissionReport(dataMap_Permission, "Grantee Type,Grantee,Access
			// Type");
			report.writeCountReport(dataMap_Counts, "Entity Type");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
