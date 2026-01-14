package com.cereports;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.security.auth.Subject;

import com.envs.CE_Envs;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.ClassDefinitionSet;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.MarkingSetSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import com.filenet.apiimpl.core.AccessPermissionImpl;
import com.filenet.apiimpl.core.AddOnImpl;
import com.filenet.apiimpl.core.ChoiceListImpl;
import com.filenet.apiimpl.core.ClassDefinitionImpl;
import com.filenet.apiimpl.core.ClassDescriptionImpl;
import com.filenet.apiimpl.core.CmQueueSweepImpl;
import com.filenet.apiimpl.core.CmSweepJobImpl;
import com.filenet.apiimpl.core.CodeModuleImpl;
import com.filenet.apiimpl.core.DocumentClassificationActionImpl;
import com.filenet.apiimpl.core.DocumentLifecycleActionImpl;
import com.filenet.apiimpl.core.DocumentLifecyclePolicyImpl;
import com.filenet.apiimpl.core.EventActionImpl;
import com.filenet.apiimpl.core.FolderImpl;
import com.filenet.apiimpl.core.MarkingSetImpl;
import com.filenet.apiimpl.core.PropertyTemplateImpl;
import com.filenet.apiimpl.core.ReplicableClassDefinitionImpl;
import com.filenet.apiimpl.core.SecurityPolicyImpl;
import com.filenet.apiimpl.core.SubscribedEventImpl;
import com.filenet.apiimpl.core.SubscriptionImpl;
import com.filenet.apiimpl.core.WorkflowDefinitionImpl;

public class Report_CE_All_Envs
{
	static ObjectStore os = null;
	static String SERVER_NAME = null;
	static String OBJECTSTORE_NAME = null;
	static String innerMapKey = null;
	
	static String NA  = "N/A";

	
    static SortedMap <String, Map <String, String>> dataMap = new TreeMap<String, Map<String,String>>();
    
    static SortedMap <String, Map <String, String>> dataMap_Permission = new TreeMap<String, Map<String,String>>();
    static SortedMap <String, Map <String, String>> dataMap_Counts = new TreeMap<String, Map<String,String>>();
    
    static SortedMap <String, Map <String, String>> dataMap_Time = new TreeMap<String, Map<String,String>>();
    
    static List <String> list = new LinkedList<String>();
    
    static Set <String> listOfAddOns = new HashSet <String>();
    
    
    static List <String> perfDocList = new LinkedList<String>();
    static String perfDate = "20250701";
    
    
	static long start;
    
    static void timerStart ( ) {
    	start = Calendar.getInstance().getTimeInMillis();
    }
    
    static void timerEnd (String method) {
    	long end = Calendar.getInstance().getTimeInMillis();
    	long time = (end - start);
    	
		if (dataMap_Time.containsKey(method)) 
		{
			Map <String, String> innerMap = dataMap_Time.get(method);
			innerMap.put(innerMapKey, Long.toString(time));
		} else {
			
			Map <String, String> newInnerMap = prepareEmptyMap();
			newInnerMap.put(innerMapKey,  Long.toString(time));
			dataMap_Time.put(method, newInnerMap);
		}
		System.out.println("Time taken for < " + method + " > is " + time + " ms.");
		timerStart();
    }

	private static Map <String, String> prepareEmptyMap ()
	{
		Map <String, String> map = new TreeMap<String, String>();
		Iterator<String> i = (Iterator<String>) new CE_Envs().getEnvsMap().keySet().iterator();
		while (i.hasNext()) {
			map.put(i.next(), "No");
		}
		return map;
	}
	
	private static ObjectStore connectToEnv (Map<String, String> envMap) 
	{
		if (os != null) 
		{
				os = null;
		}
		
		
		SERVER_NAME = envMap.get(CE_Envs.key_SERVERNAME);
		OBJECTSTORE_NAME = envMap.get(CE_Envs.key_OBJECTSTORE);
		innerMapKey = CE_Envs.getKey(SERVER_NAME, OBJECTSTORE_NAME);

		String URI = envMap.get(CE_Envs.key_URI);
		String USERNAME = envMap.get(CE_Envs.key_USERNAME);;
		String PASSWORD = envMap.get(CE_Envs.key_PASSWORD);;
		
		System.out.println("Connecting to URI: " + URI);
		
		timerStart();
		Connection conn = Factory.Connection.getConnection(URI);
		Subject subject = UserContext.createSubject(conn, USERNAME, PASSWORD, "FileNetP8WSI");
		UserContext uc = UserContext.get();
		uc.pushSubject(subject);
		Locale origLocale = uc.getLocale();
		uc.setLocale(origLocale);
		timerEnd("connection");
		Domain domain = Factory.Domain.getInstance(conn, null);
		os = Factory.ObjectStore.fetchInstance(domain, OBJECTSTORE_NAME, null);
		timerEnd("objectstore");
		System.out.println("OS: " + os);
		return os;
	}
	
	public void processEntityCounts (String str, int count) 
	{
		String dataMap_Count_Key = str;
		
		if (dataMap_Counts.containsKey(dataMap_Count_Key)) {
			Map <String, String> innerMap = dataMap_Counts.get(dataMap_Count_Key);
			innerMap.put(innerMapKey, Integer.toString(count));
		} else {
			
			Map <String, String> newInnerMap = prepareEmptyMap();
			newInnerMap.put(innerMapKey, Integer.toString(count));
			dataMap_Counts.put(dataMap_Count_Key, newInnerMap);
		}
	}
	
//	public void iterateAPL  (AccessPermissionList apl, String type, String displayName, String symbolicName) 
//	{
//		iterateAPL  (apl, type, displayName, symbolicName, null);
//
//	}
//	
//
//	
//	public void iterateAPL  (AccessPermissionList apl, String type, String displayName, String symbolicName, String securityType) 
//	{
//		String dataMap_Entity_Key = type + "," + displayName + "," + symbolicName;
//		if (dataMap.containsKey(dataMap_Entity_Key)) 
//		{
//			Map <String, String> innerMap = dataMap.get(dataMap_Entity_Key);
//			innerMap.put(innerMapKey,"Yes");
//		} else {
//			
//			Map <String, String> newInnerMap = prepareEmptyMap();
//			newInnerMap.put(innerMapKey, "Yes");
//			dataMap.put(dataMap_Entity_Key, newInnerMap);
//		}
//		
//		if (apl != null)
//		{
//			Iterator i2 = apl.iterator();
//			while (i2.hasNext())
//			{
//				AccessPermissionImpl aplimpl = (AccessPermissionImpl) i2.next();
//				String dataMap_Permission_Key = aplimpl.get_GranteeType().toString() + "," + aplimpl.get_GranteeName() + "," + aplimpl.get_AccessType();
//				
//				if (dataMap_Permission.containsKey(dataMap_Permission_Key)) {
//					Map <String, String> innerMap = dataMap_Permission.get(dataMap_Permission_Key);
//					innerMap.put(innerMapKey,"Yes");
//				} else {
//					
//					Map <String, String> newInnerMap = prepareEmptyMap();
//					newInnerMap.put(innerMapKey, "Yes");
//					dataMap_Permission.put(dataMap_Permission_Key, newInnerMap);
//				}
//				if (securityType != null) {
//					list.add(innerMapKey +"," + type +"," + displayName  +"," + symbolicName +"," + securityType + "," + dataMap_Permission_Key);
//				} 
//			}
//		}
//	}
	
	public void iterateAPLPutIds  (AccessPermissionList apl, String type, String displayName, String symbolicName, String path, Id id, String securityType) 
	{
		String dataMap_Entity_Key = type + "," + displayName + "," + symbolicName + "," + path;
//		String dataMap_Entity_Key = type + "," + symbolicName;
		
		
		String val = id.toString();
		if (dataMap.containsKey(dataMap_Entity_Key)) 
		{
			Map <String, String> innerMap = dataMap.get(dataMap_Entity_Key);
			innerMap.put(innerMapKey, val);
		} else {
			
			Map <String, String> newInnerMap = prepareEmptyMap();
			newInnerMap.put(innerMapKey, val);
			dataMap.put(dataMap_Entity_Key, newInnerMap);
		}
		
		if (apl != null)
		{
			Iterator i2 = apl.iterator();
			while (i2.hasNext())
			{
				AccessPermissionImpl aplimpl = (AccessPermissionImpl) i2.next();
				String dataMap_Permission_Key = aplimpl.get_GranteeType().toString() + "," + aplimpl.get_GranteeName() + "," + aplimpl.get_AccessType();
				
				if (dataMap_Permission.containsKey(dataMap_Permission_Key)) {
					Map <String, String> innerMap = dataMap_Permission.get(dataMap_Permission_Key);
					innerMap.put(innerMapKey,"Yes");
				} else {
					
					Map <String, String> newInnerMap = prepareEmptyMap();
					newInnerMap.put(innerMapKey, "Yes");
					dataMap_Permission.put(dataMap_Permission_Key, newInnerMap);
				}
				if (securityType != null) {
					list.add(innerMapKey +"," + type +"," + displayName  +"," + symbolicName +"," + securityType + "," + dataMap_Permission_Key);
				} 
			}
		}
	}

	
	public void codeModule()
	{
		timerStart();
		int countEntity = 0;
		Iterator<FolderSet> iterator = (Iterator<FolderSet>) os.get_RootFolder().get_SubFolders().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{

			FolderImpl folder = (FolderImpl) iterator.next();
			if (folder.get_Name().equalsIgnoreCase("CodeModules"))
			{
				Iterator <DocumentSet> iter = (Iterator <DocumentSet>) folder.get_ContainedDocuments().iterator();
				while (iter.hasNext()) 
				{
					countEntity++;
					Document p8Obj = (Document) iter.next();
//					iterateAPL(p8Obj.get_Permissions(), p8Obj.get_ClassDescription().get_Name(), p8Obj.get_Name(), p8Obj.get_Name() + " ( " + p8Obj.get_Id() + " ) ");
					iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_Name(), p8Obj.get_Name() ,NA, p8Obj.get_Id(), null);
					
				}
				
			}
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	
	public void choiceList()
	{
		timerStart();
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_ChoiceLists().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		
		while (iterator.hasNext())
		{
			countEntity++;
			ChoiceListImpl p8Obj = (ChoiceListImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_Name() ,NA, p8Obj.get_Id(), null);
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}

	public void classDescription()
	{
		timerStart();
		int countEntity = 0;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_ClassDescriptions().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			ClassDescriptionImpl p8Obj = (ClassDescriptionImpl) iterator.next();

			String displayName = p8Obj.get_DisplayName();
			iterateAPLPutIds(p8Obj.get_DefaultInstancePermissions(), method, displayName , p8Obj.get_SymbolicName() , p8Obj.get_SymbolicName() , p8Obj.get_Id() , "Security");

			
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	
	public void classDefinition()
	{
		timerStart();
		int countEntity = 0;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		System.out.println(innerMapKey + " > " + method);
		
		ClassDefinitionSet cds = os.get_RootClassDefinitions();
		countEntity = classDefinition (cds, "", countEntity);
		
		for (String string : listOfAddOns)
		{
			System.out.println(string);
		}
		
//		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_RootClassDefinitions().iterator();
//		while (iterator.hasNext())
//		{
//			countEntity++;
//			ClassDefinitionImpl p8Obj = (ClassDefinitionImpl) iterator.next();
//			String displayName = p8Obj.get_DisplayName();
//			System.out.println(displayName);
//			if (p8Obj.get_AllowsSubclasses())
//			{
//				ClassDefinitionSet cds  = p8Obj.get_ImmediateSubclassDefinitions();
//				recur (cds, "");
//			}
//			
//
////			iterateAPL(p8Obj.get_Permissions(), method, displayName , p8Obj.get_SymbolicName());
//		}
		//System.out.println("Entity: " + countEntity);
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	
	private int classDefinition (ClassDefinitionSet cds, String path, int countEntity)
	{
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		if (cds != null) 
		{
			Iterator iter  = cds.iterator();
			while (iter.hasNext())
			{
				++countEntity;
				String currentPath = path;
				ClassDefinitionImpl p8Obj = (ClassDefinitionImpl) iter.next();
				currentPath = path + p8Obj.get_SymbolicName();
				//System.out.println(countEntity + ")" + currentSuffix);
				
				String symbolicName = p8Obj.get_SymbolicName();
				
				
				if (p8Obj.get_IsSystemOwned()) {
					currentPath += " $ ";
					symbolicName += " $ ";
				}
				
				AddOnImpl ado = (AddOnImpl) p8Obj.get_InstalledByAddOn();
				if (ado != null )
				{
					currentPath += " # ";
					symbolicName += " # ";
					listOfAddOns.add(ado.get_AddOnType() + " - " + ado.get_DisplayName());
				}
				
				if (currentPath.contains(">"))
				{
					String [] arr = currentPath.split(">");
					int tabsCount = arr.length - 1;
					
					String tabs = "";
					
					for (int i = 0; i < tabsCount; i++)
					{
						tabs += "\t";
					}
					String line = tabs + " > " + arr[tabsCount].trim();
//					System.out.println(line);
				} else {
//					System.out.println(currentPath);
				}
				iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), symbolicName, currentPath  , p8Obj.get_Id() , "Security");
				
				iterateAPLPutIds(p8Obj.get_DefaultInstancePermissions(), method, p8Obj.get_DisplayName(), symbolicName, currentPath  , p8Obj.get_Id() , "DefaultInstanceSecurity");
				if (p8Obj.get_AllowsSubclasses()) 
				{
					countEntity = classDefinition(p8Obj.get_ImmediateSubclassDefinitions(), currentPath + " > ", countEntity);
				}
				//--------
			}
		}

		return countEntity;
	}
	
	
//	public void propertyTemplate()
//	{
//		int countEntity = 0;
//		HashMap<String, Integer> map = new HashMap<String, Integer>();
//		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_PropertyTemplates().iterator();
//		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
//		System.out.println(innerMapKey + " > " + method);
//		while (iterator.hasNext())
//		{
//			countEntity++;
//			PropertyTemplateImpl p8Obj = (PropertyTemplateImpl) iterator.next(); 
//			String displayName = p8Obj.get_DisplayName();
//			iterateAPLPutIds(p8Obj.get_Permissions(), method, displayName , p8Obj.get_SymbolicName() ,NA , p8Obj.get_Id(), null);
////			if (map.containsKey(displayName)) {
////				int count = map.get(displayName).intValue();
////				map.put(displayName, ++count);
////			}
//		}
//		processEntityCounts(method , countEntity);
//	}
	
	public void propertyTemplate_Ids()
	{
		timerStart();
		int countEntity = 0;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_PropertyTemplates().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			PropertyTemplateImpl p8Obj = (PropertyTemplateImpl) iterator.next(); 
			String displayName = p8Obj.get_DisplayName();
			iterateAPLPutIds (p8Obj.get_Permissions(), method, displayName , p8Obj.get_SymbolicName(),NA, p8Obj.get_Id(), null);
//			if (map.containsKey(displayName)) {
//				int count = map.get(displayName).intValue();
//				map.put(displayName, ++count);
//			}
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	public void eventAction()
	{
		timerStart();
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_EventActions().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			EventActionImpl p8Obj = (EventActionImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_Name(),NA , p8Obj.get_Id(), null);
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	public void subscription() throws Exception
	{
		timerStart();
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_Subscriptions().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);		
		while (iterator.hasNext())
		{
			countEntity++;
			SubscriptionImpl p8Obj = (SubscriptionImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_Name(),NA, p8Obj.get_Id(), "Security");
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	public void sweepJob()
	{
		timerStart();
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_SweepJobs().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			CmSweepJobImpl p8Obj = (CmSweepJobImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_DisplayName(),NA, p8Obj.get_Id(), "Security");
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	public void workflowDefinition()
	{
		timerStart();
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_WorkflowDefinitions().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			WorkflowDefinitionImpl p8Obj = (WorkflowDefinitionImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_Name(), p8Obj.get_Name(),NA, p8Obj.get_Id(), "Security");
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	public void documentLifecycleAction()
	{
		timerStart();	
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_DocumentLifecycleActions().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			DocumentLifecycleActionImpl p8Obj = (DocumentLifecycleActionImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_Name(),NA, p8Obj.get_Id(), "Security");
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	public void documentLifecyclePolicy()
	{
		timerStart();
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_DocumentLifecyclePolicies().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			DocumentLifecyclePolicyImpl p8Obj = (DocumentLifecyclePolicyImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_Name(),NA, p8Obj.get_Id(), "Security");
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}

	public void documentClassificationAction()
	{
		timerStart();
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_DocumentClassificationActions().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			DocumentClassificationActionImpl p8Obj = (DocumentClassificationActionImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_Name(),NA, p8Obj.get_Id(), "Security");
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	public void securityPolicy()
	{
		timerStart();
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_SecurityPolicies().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			SecurityPolicyImpl p8Obj = (SecurityPolicyImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_Name(),NA, p8Obj.get_Id(), "Security");
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}

	public void queueSweep()
	{
		timerStart();
		
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_QueueSweeps().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			CmQueueSweepImpl p8Obj = (CmQueueSweepImpl) iterator.next();
			iterateAPLPutIds(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_DisplayName(),NA, p8Obj.get_Id(), "Security");
		}
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	

	public void folder()
	{
		timerStart();
		int countEntity = 0;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		System.out.println(innerMapKey + " > " + method);
		
		FolderSet fds = os.get_RootFolder().get_SubFolders();
		countEntity = folder (fds, "", countEntity);
		
		for (String string : listOfAddOns)
		{
			System.out.println(string);
		}
		
//		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_RootClassDefinitions().iterator();
//		while (iterator.hasNext())
//		{
//			countEntity++;
//			ClassDefinitionImpl p8Obj = (ClassDefinitionImpl) iterator.next();
//			String displayName = p8Obj.get_DisplayName();
//			System.out.println(displayName);
//			if (p8Obj.get_AllowsSubclasses())
//			{
//				ClassDefinitionSet cds  = p8Obj.get_ImmediateSubclassDefinitions();
//				recur (cds, "");
//			}
//			
//
////			iterateAPL(p8Obj.get_Permissions(), method, displayName , p8Obj.get_SymbolicName());
//		}
		//System.out.println("Entity: " + countEntity);
		timerEnd(method);
		processEntityCounts(method , countEntity);
	}
	
	
	private int folder (FolderSet fds, String path, int countEntity)
	{
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		if (fds != null) 
		{
			Iterator iter  = fds.iterator();
			while (iter.hasNext())
			{
				++countEntity;
				String currentPath = path;
				Folder folder = (Folder) iter.next();
				currentPath = path + folder.get_Name();
				//System.out.println(countEntity + ")" + currentSuffix);
				
//				if (folder.get_IsSystemOwned()) {
//					currentPath += " $ ";
//				}
//				
//				AddOnImpl ado = (AddOnImpl) folder.get_InstalledByAddOn();
//				if (ado != null )
//				{
//					currentPath += " # ";
//					listOfAddOns.add(ado.get_AddOnType() + " - " + ado.get_DisplayName());
//				}
				
				if (currentPath.contains(">"))
				{
					String [] arr = currentPath.split(">");
					int tabsCount = arr.length - 1;
					
					String tabs = "";
					
					for (int i = 0; i < tabsCount; i++)
					{
						tabs += "\t";
					}
					String line = tabs + " > " + arr[tabsCount].trim();
					System.out.println(line);
				} else {
					System.out.println(currentPath);
				}
				iterateAPLPutIds(folder.get_Permissions(), method, folder.get_Name(), folder.get_FolderName(), currentPath , folder.get_Id() , "Security");
				
//				iterateAPL(folder.get_DefaultInstancePermissions(), method, folder.get_DisplayName(), currentPath, "DefaultInstanceSecurity");
				
				int level = currentPath.split(">").length -1;
				FolderSet subFolders = folder.get_SubFolders();
				if (level <=5 && subFolders != null && !subFolders.isEmpty()) 
				{
					countEntity = folder(subFolders, currentPath + " > ", countEntity);
				}
				//--------
			}
		}
		return countEntity;
	}

	
	private static String getFileName (String character, String method)
	{
		String dateStr = Writer_CE_All_Envs.dateStr;
	
		String fileName = dateStr + "_Rpt";
		if (character != null) {
			fileName = fileName + "_" + character;
		}
		if(method != null) {
			fileName = fileName +  "_" + method;
		}
		//fileName = fileName + "_" + SERVER_NAME +"_" + OBJECTSTORE_NAME +  ".csv";
		fileName = fileName + "MultiEnv.csv";
		
		return fileName;
	}
	
	
	
	String reportsDir = "reports";
    File reportDir = new File(reportsDir);
    boolean freshFile = true;
	public void get_Subs_EAs_CMs_Targets() throws Exception
	{
		int countEntity = 0;
		Iterator<IndependentObjectSet> iterator = (Iterator<IndependentObjectSet>) os.get_Subscriptions().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		
		
		File file = new File(reportDir, getFileName(null, method));
		FileWriter fileWriter = new FileWriter (file, true);
		PrintWriter writer = new PrintWriter(fileWriter);
		
		if (freshFile)
		{
			writer.println("Environment,Subscription,Sub Created, Sub Id, Target,Target Created, Target Id, EventAction,EA Created, EA Id, CodeModule, CM Created, CM Id, Subscribed Events");
			freshFile = false;
		}
		
		int count = 0;
		while (iterator.hasNext())
		{
			countEntity++;
			SubscriptionImpl p8Obj = (SubscriptionImpl) iterator.next();
			
			//System.out.println(p8Obj.get_SubscriptionTarget().getClass().getCanonicalName());
			
			String subscribedTarget = "null,null,null" ;

			Object stObj = p8Obj.get_SubscriptionTarget();
			if (stObj != null)
			{
				if (stObj instanceof ReplicableClassDefinitionImpl)
				{
					ReplicableClassDefinitionImpl obj = (ReplicableClassDefinitionImpl)stObj;
					subscribedTarget = obj.get_SymbolicName() 
										+ ",\"" + obj.get_DateCreated() +"\""
										+ "," + obj.get_Id() ;
				}
				else if (stObj instanceof FolderImpl) 
				{
					FolderImpl obj = (FolderImpl)stObj;
					subscribedTarget = obj.get_Name()
										+ ",\"" + obj.get_DateCreated() +"\""
										+ "," + obj.get_Id();
				}
				else 
				{
					subscribedTarget = stObj.getClass().getCanonicalName() + "," + stObj.getClass().getCanonicalName() + "," + stObj.getClass().getCanonicalName();
				}				
			}

			
			
			int seCounter = 0;
			String subscribedEvents = "";
			if (subscribedEvents != null)
			{
				Iterator eListI  = p8Obj.get_SubscribedEvents().iterator();
				while (eListI.hasNext()) {
					SubscribedEventImpl se = (SubscribedEventImpl) eListI.next();
					subscribedEvents += "#" + (++seCounter) + ") " + se.get_EventClass().get_SymbolicName() + "  ";
				}
			}


			EventActionImpl eventAction = (EventActionImpl ) p8Obj.get_EventAction();
			CodeModuleImpl code_Module = (CodeModuleImpl)eventAction.get_CodeModule();
			
			String line = /*++count*/ SERVER_NAME + " > " + OBJECTSTORE_NAME + "," + p8Obj.get_DisplayName() + ",\"" + p8Obj.get_DateCreated() + "\"," + p8Obj.get_Id()
							+ "," + subscribedTarget
							+ "," + ((eventAction !=null) ? eventAction.get_Name() + ",\"" + eventAction.get_DateCreated() +"\","  + eventAction.get_Id(): "null,null,null") 
							+ "," + ((code_Module !=null) ? code_Module.get_Name() + ",\"" + code_Module.get_DateCreated() +"\","  + code_Module.get_Id(): "null,null,null") 
							+ "," + subscribedEvents;
			
			writer.println(line);
//			System.out.println(line);
			
			//iterateAPL(p8Obj.get_Permissions(), method, p8Obj.get_DisplayName(), p8Obj.get_Name());
		}
		writer.close();
		//addInsights(method + "," + countEntity);
	}

	public void markingSet()
	{
		int countEntity = 0;
		Iterator<MarkingSetSet> iterator = (Iterator<MarkingSetSet>) os.get_Domain().get_MarkingSets().iterator();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		while (iterator.hasNext())
		{
			countEntity++;
			MarkingSetImpl p8Obj = (MarkingSetImpl) iterator.next();
			iterateAPLPutIds(null, method, p8Obj.get_DisplayName(), p8Obj.get_DisplayName(),NA, p8Obj.get_Id(), null);
		}
		processEntityCounts(method , countEntity);
	}
	
	
	
	public void getDocuments()
	{
		long  countEntity = 0;
		long noContentCount = 0;

		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(innerMapKey + " > " + method);
		
		perfDate = "20251113";
		SearchScope search = new SearchScope(os);
		
		
		
//		String mySQLString= "SELECT Id, Name, DateCreated, Creator, ContentSize  "
//						  + "FROM Document "
////						  + "WHERE DateCreated >= " + perfDate +"T182200Z and DateCreated <= "+ perfDate +"T194700Z "
////						  + "WHERE DateCreated >= " + perfDate +"T101100Z and DateCreated <= "+ perfDate +"T113600Z "
//						  + "ORDER BY DateCreated";
		
		
		String mySQLString= "SELECT ContentSize FROM Document ";
		
		SearchSQL sql= new SearchSQL(mySQLString); 
		
		DocumentSet documents = (DocumentSet) search.fetchObjects(sql,Integer.valueOf("50"),null, Boolean.valueOf(true));
		com.filenet.api.core.Document doc; 

		Iterator it = documents.iterator();
		
		
		String key1 = "<= 1 MB";
		String key2 = "1 to 5 MB";
		String key3 = "5 to 10 MB";
		String key4 = "10 to 15 MB";
		String key5 = "15 to 20 MB";
		String key6 = "20 to 25 MB";
		String key7 = "> 25 MB";
		
		Map map = new LinkedHashMap<String, Integer>();
		
		map.put(key1, 0);
		map.put(key2, 0);
		map.put(key3, 0);
		map.put(key4, 0);
		map.put(key5, 0);
		map.put(key6, 0);
		map.put(key7, 0);
		
		while (it.hasNext())
		{
			doc = (Document)it.next();
//			doc.refresh();
			
			
//			String row = (++countEntity) 
//						+ "," + doc.getClassName()
//						+ "," + doc.get_Name()
//						+ "," + doc.get_Id()
//						+ "," + doc.get_ContentSize()
//						+ "," + doc.get_DateCreated()
//						+ "," + doc.get_Creator();
//			
////			perfDocList.add(row);
//			System.out.println(row);
			
			Double csd = doc.get_ContentSize();
			if (csd != null)
			{
				double cs = csd.doubleValue();
				
				if (cs <= 1000000) {
					addToMap(map, key1);
				}
				else if (cs <= 5000000 ) 
				{
					addToMap(map, key2);
				} 
				else if (cs <= 10000000 ) 
				{
					addToMap(map, key3);
				}
				else if (cs <= 15000000 ) 
				{
					addToMap(map, key4);
				} 
				else if (cs <= 20000000 ) 
				{
					addToMap(map, key5);
				}
				else if (cs <= 25000000 ) 
				{
					addToMap(map, key6);
				} 
				else {
					addToMap(map, key7);
				}
			} else {
				++noContentCount;
			}

			if (++countEntity % 10000 == 0) {
				System.out.println(map);
				System.out.println("Total Docs: " + countEntity);
				System.out.println("No Content Docs: " + noContentCount);
			}
			
		}
		
		
		System.out.println("#####################################################");
		System.out.println("Total Docs: " + countEntity);
		System.out.println("No Content Docs: " + noContentCount);
		
		System.out.println("Final: "  +map);
	}
	
	private void addToMap (Map <String, Integer>map, String key) 
	{
		map.put(key, map.get(key) + 1);

	}
	
	
	
	
	
	public static void main(String[] args)
	{
		Report_CE_All_Envs sr = new Report_CE_All_Envs();
		Map <String, Map<String, String>> envs = new CE_Envs().getEnvsMap();
		Iterator i = envs.keySet().iterator();
		int counter = 0;
		while (i.hasNext())
		{
			String envKey = (String) i.next();
			try
			{
				//TimeUnit.SECONDS.sleep(3);
				System.out.println(++counter + ") Environment:");
				os = connectToEnv(envs.get(envKey));
//				sr.folder();
//				sr.classDescription();
				sr.classDefinition();
				sr.choiceList();
				sr.codeModule();
				sr.eventAction();
				sr.propertyTemplate_Ids();
				sr.subscription();
				
//				sr.documentClassificationAction();
//				sr.documentLifecycleAction();
//				sr.documentLifecyclePolicy();

//				sr.get_Subs_EAs_CMs_Targets();

//				sr.markingSet();
//				sr.queueSweep();
//				sr.securityPolicy();

//				sr.sweepJob();
//				sr.workflowDefinition();
				
//				sr.getDocuments();

			} catch (Exception e1)
			{
				e1.printStackTrace();;
			}
		}
		
		Writer_CE_All_Envs report = new Writer_CE_All_Envs(envs);
		
		try
		{
			report.writePERFDocumentReport(perfDocList, "SN,Class,Name,ID,ContentSize,DateCreated,Creator", perfDate);
			
			report.writeOtherArtefactsReport(dataMap, "EntityType,DisplayName,Symbolic Name,Path");
//			report.writeTimeReport(dataMap_Time, "Method");
			
//			report.writePermissionReport(dataMap_Permission, "Grantee Type,Grantee,Access Type");
//			report.writeCountReport(dataMap_Counts, "Entity Type");
//			report.writePermissionByObjectReport(list, "Environment,Entity Type, Display Name,Symbolic Name,Security,Grantee Type, Grantee Name,Acces Type");
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
