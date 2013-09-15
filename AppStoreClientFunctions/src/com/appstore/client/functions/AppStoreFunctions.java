package com.appstore.client.functions;



import java.awt.HeadlessException;
import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;
import javax.xml.parsers.*;

import org.w3c.dom.*;


public class AppStoreFunctions {
	
	static final String type="xml";
	static final String strConfFile="ini/Config.ini";
	static final String strVerInfoFile="ini/ProductVersionInfo.xml";
	static final String strTagMappingFile="ini/tagmapping.ini";
	public Properties prop_conf = new Properties();
	public Properties prop_productVersionInfo = new Properties();

	public LinkedHashMap map_purchasedListTag= new LinkedHashMap();
	public LinkedHashMap map_searchedListTag = new LinkedHashMap();
	public LinkedHashMap map_detailedInfoTag = new LinkedHashMap();
	
	public ArrayList<String> displayingTags_PurchasedList=new ArrayList<String>();
	public ArrayList<String> displayingTags_SearchedList=new ArrayList<String>();
	public ArrayList<String> displayingTags_DetailedInfo=new ArrayList<String>();
	
	public String baseURL = "http://211.238.244.71:9091/";
	public String pkey="";		
	public String baseDownloadPath="Download\\";
	public String baseCompRepoPath="C:\\OPRoS\\OprosDevelopment\\Repository\\";
	public String basePkgPath="C:\\OPRoS\\OprosRobot\\Package\\";
	public String baseBinPath="C:\\OPRoS\\OprosRobot\\Binary\\";

	// Package run process
	public Process proc = null;
	
	public boolean ReadConfigFile()
	{
		// Read keys from properties
		baseURL=prop_conf.getProperty("baseURL");
		if (pkey.equals(""))
			pkey=prop_conf.getProperty("pkey");
		baseDownloadPath=prop_conf.getProperty("baseDownloadPath");
		baseCompRepoPath=prop_conf.getProperty("baseCompRepoPath");
		basePkgPath=prop_conf.getProperty("basePkgPath");
		baseBinPath=prop_conf.getProperty("baseBinPath");
		
		String[] arrTag1=prop_conf.getProperty("DisplayingTags_PurchasedList").split(",");
		String[] arrTag2=prop_conf.getProperty("DisplayingTags_SearchedList").split(",");
		String[] arrTag3=prop_conf.getProperty("DisplayingTags_DetailedInfo").split(",");
		int i=0;
		displayingTags_PurchasedList.clear();
		displayingTags_SearchedList.clear();
		displayingTags_DetailedInfo.clear();
		for (i=0;i<arrTag1.length;i++)
			displayingTags_PurchasedList.add(arrTag1[i]);
		for (i=0;i<arrTag2.length;i++)
			displayingTags_SearchedList.add(arrTag2[i]);
		for (i=0;i<arrTag3.length;i++)
			displayingTags_DetailedInfo.add(arrTag3[i]);
		
		return true;
	}
	
	public AppStoreFunctions()
	{
		BufferedReader in;
		
		// Read tag mapping info
		try {
			// Read configure property file
			prop_conf.load(new FileInputStream(strConfFile));
			
			// Read version informations XML file
			prop_productVersionInfo.loadFromXML(new FileInputStream(strVerInfoFile));
			
			in = new BufferedReader(
					new InputStreamReader(new FileInputStream(strTagMappingFile)));
		
		    String s;
		    HashMap map_processing=map_searchedListTag;
	    	while ((s = in.readLine()) != null) {
				if (s.substring(0, 2).equals("::"))
				{
					s=s.replace(":", "");
	
					if (s.equalsIgnoreCase("PURCHASEDLIST"))
						map_processing=map_purchasedListTag;
					else if (s.equalsIgnoreCase("SEARCHEDLIST"))
						map_processing=map_searchedListTag;
					else if (s.equalsIgnoreCase("DETAILEDINFO"))
						map_processing=map_detailedInfoTag;
				}
				else
				{
					String[] tag_explain=s.split(",", 2);
					map_processing.put(tag_explain[0], tag_explain[1]);
					//System.out.println(tag_explain[0]+":"+map_processing.get(tag_explain[0]));
				}
			}
			in.close();
			
			// Read keys from properties
			ReadConfigFile();
						
			/*
			// Write keys
			prop_conf.setProperty("baseURL", baseURL);
			prop_conf.setProperty("pkey", pkey);
			prop_conf.setProperty("baseDownloadPath", baseDownloadPath);
			prop_conf.setProperty("baseCompRepoPath", baseCompRepoPath);
			prop_conf.setProperty("basePkgPath", basePkgPath);
			
			// Store to ini file
			SaveSettings();
			prop_productVersionInfo.storeToXML(new FileOutputStream(strVerInfoFile), "Updated");
			*/
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void SaveSettings()
	{
		
		try {
			prop_conf.store( new FileOutputStream(strConfFile), "changed.");
			ReadConfigFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Return value:
	// 1  : Success
	// 0  : ID and/or pwd mismatch
	// -1 : Connection error
	public int login(String id, String pwd)
	{
		String strURL=baseURL+"/openapi/apiUserLogin.do";
		String params = "memid="+id+"&pass="+pwd+"&type=xml";
		String method = "POST";
		Document doc=BaseFunctions.GetXMLDocument(strURL, params, method);
		
		if (doc != null)
		{
			List<Node> nodeArr=BaseFunctions.GetListFromXMLDoc(doc, "result");
			pkey=BaseFunctions.GetTagContentFromNode(nodeArr.get(0), "pkey").trim();
			prop_conf.setProperty("pkey", pkey);
			prop_conf.setProperty("id", id.trim());
			SaveSettings();
			return (pkey.equals("")?0:1);
		}
		else
			return -1;
	}
	
	public List<Node> GetPurchasedList(String orderBy, String ascDesc)
	{
		String strURL=baseURL+"/openapi/apiSaleThingList.do";
		String params = "pkey="+pkey+"&type=xml&idx1="+orderBy+"&idx2="+ascDesc;
		String method = "GET";
		Document doc=BaseFunctions.GetXMLDocument(strURL, params, method);
		//Util.PrintXMLDoc(doc);
		
		return BaseFunctions.GetListFromXMLDoc(doc, "list");
	}
	
	public List<Node> GetComponentList(String orderBy, String ascDesc, 
			String strLang, String strPlatform, String name, String startDate, String endDate)
	{
		String strURL=baseURL+"/openapi/apiSdkThingList.do";
		String name_encoded=null;
		try
		{
			name_encoded=URLEncoder.encode(name, "utf-8");
		}
		catch(UnsupportedEncodingException e)
		{
			JOptionPane.showMessageDialog(null, "Please check if there be some wrong characters in the name field!");
			return null;
		}
		String params = "pkey="+pkey.trim()+"&type=xml&idx1="+orderBy+"&idx2="+ascDesc
				+"&lang="+strLang+"&pfcd="+strPlatform+"&tname="+name_encoded+"&gigan1="+startDate+"&gigan2="+endDate;
		
		String method = "GET";
		Document doc=BaseFunctions.GetXMLDocument(strURL, params, method);
		//Util.PrintXMLDoc(doc);
		if (doc==null)
			return null;
		
		return BaseFunctions.GetListFromXMLDoc(doc, "list");
	}
	
	public List<Node> GetAppList(String orderBy, String ascDesc, 
			String strLang, String strPlatform, String name, String startDate, String endDate)
	{
		String strURL=baseURL+"/openapi/apiAplThingList.do";
		String name_encoded=null;
		try
		{
			name_encoded=URLEncoder.encode(name, "utf-8");
		}
		catch(UnsupportedEncodingException e)
		{
			JOptionPane.showMessageDialog(null, "Please check if there be some wrong characters in the name field!");
			return null;
		}
		String params = "pkey="+pkey.trim()+"&type=xml&idx1="+orderBy+"&idx2="+ascDesc
				+"&lang="+strLang+"&pfcd="+strPlatform+"&tname="+name_encoded+"&gigan1="+startDate+"&gigan2="+endDate;
		
		String method = "GET";
		Document doc=BaseFunctions.GetXMLDocument(strURL, params, method);
		//Util.PrintXMLDoc(doc);
		if (doc==null)
			return null;
		
		return BaseFunctions.GetListFromXMLDoc(doc, "list");
	}
	
	public List<Node> GetDetailedInfo(String strCode)
	{
		String strURL=baseURL+"/openapi/apiThingDetail.do";
		String params = "pkey="+pkey.trim()+"&type=xml&tcode="+strCode;
		String method = "GET";
		Document doc=BaseFunctions.GetXMLDocument(strURL, params, method);
		//Util.PrintXMLDoc(doc);
		if (doc==null)
			return null;
		
		return BaseFunctions.GetListFromXMLDoc(doc, "list");
	}

	public List<Node> GetVersionList(String strCode)
	{
		String strURL=baseURL+"/openapi/apiThingVerList.do";
		String params = "pkey="+pkey.trim()+"&type=xml&tcode="+strCode;
		String method = "GET";
		Document doc=BaseFunctions.GetXMLDocument(strURL, params, method);
		//Util.PrintXMLDoc(doc);
		
		return BaseFunctions.GetListFromXMLDoc(doc, "list");
	}

	public List<Node> GetCodeList(String strCodeType)
	{
		String strURL=baseURL+"/openapi/apiCodeList.do";
		String params = "pkey="+pkey.trim()+"&type=xml&cdgbn="+strCodeType;
		String method = "GET";
		Document doc=BaseFunctions.GetXMLDocument(strURL, params, method);
		//Util.PrintXMLDoc(doc);
		if (doc!=null)
			return BaseFunctions.GetListFromXMLDoc(doc, "list");
		else
			return null;
	}
	
	public List<Node> PurchaseFreeProduct(String strProductID)
	{
		String strURL=baseURL+"/openapi/apiThingFreeSale.do";
		String params = "pkey="+pkey.trim()+"&type=xml&thingid="+strProductID;
		String method = "GET";
		Document doc=BaseFunctions.GetXMLDocument(strURL, params, method);
		//Util.PrintXMLDoc(doc);
		
		return BaseFunctions.GetListFromXMLDoc(doc, "result");
	}	
	
	public boolean DownloadProduct(String strProductID)
	{
		Node node=null;
		for (int loop=0;loop<2;loop++)
		{
			List<Node> list=GetPurchasedList("","");
			
			// Search product information among purchased list
			for (int i=0;i<list.size();i++)
			{
				Node tmp=list.get(i);
				if (BaseFunctions.GetTagContentFromNode(tmp, "tcode").equalsIgnoreCase(strProductID))
				{
					node=tmp;
					break;
				}
			}
			
			if (node!=null)
			{
				break;
			}
			
			// For the case that user have not purchased this product yet. 
			// Check if the product is free.
			List<Node> detailed=GetDetailedInfo(strProductID);
			String strPrice=BaseFunctions.GetTagContentFromNode(detailed.get(0), "coin");
			double price=Double.parseDouble(strPrice.replace("^", "<></>").split("<></>")[1]);
			if (price > 0)
				break;

			// If The product is free, user can purchase by the next command.   
			List<Node> purRes=PurchaseFreeProduct(strProductID);
		}
		
		// Check if product is downloadable
		if (node==null)
		{
			JOptionPane.showMessageDialog(null, "<html>You must buy this product on web site<br/>before download!</html>");
			//System.out.println("You must buy this product first");
			return false;
		}
		
		// Get type, download version and file url
		String type=BaseFunctions.GetTagContentFromNode(node,  "tgubun");
    	String lastVer=BaseFunctions.GetTagContentFromNode(node,  "lastver");
    	String lastVer_url=BaseFunctions.GetTagContentFromNode(node,  "lastver_url");
    	
    	// Check download url
    	if (lastVer_url.trim().equals(""))
    	{
    		JOptionPane.showMessageDialog(null, "This product is not uploaded yet!");
    		//System.out.println("This product is not uploaded yet!");
			return false;
    	}
    	
    	// Get previously downloaded version info
    	String[] verinfo=GetVersionInfo(strProductID);

    	// Check version collision
    	if (verinfo[5].equals(baseDownloadPath) && CompareVersions(lastVer,verinfo[2])<=0)
    	{
    		/*
    		System.out.println("You already downloaded latest version!");
    		System.out.print("Do you want to downloaded old version anyway(y/n)?");
    		Scanner sc = new Scanner(System.in);
    		String line = sc.nextLine();
    		if (!line.equalsIgnoreCase("y"))
    			return false;
    		*/
    		int ret = JOptionPane.showConfirmDialog(null, 
    				"<html>You already downloaded latest version.<br/>Do you want to redownload anyway?</html>");
    		if (ret!=JOptionPane.YES_OPTION)
    			return false;
    	}
    	
    	// Download tar file
    	String targetFilename=lastVer_url.substring(lastVer_url.lastIndexOf("/")+1);
    	//System.out.println(lastVer_url+","+baseDownloadPath+targetFilename);
    	if (BaseFunctions.Downloader(lastVer_url, baseDownloadPath+targetFilename) > 0)
    	{
	    	// Update version info
	    	verinfo[0]=type;
	    	verinfo[1]=targetFilename;
	    	verinfo[2]=lastVer;
	    	verinfo[5]=baseDownloadPath;
	    	
	    	return UpdateProductVersionInfo(strProductID, verinfo);
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(null, "Download failed. Please contact webstore master." );
    		return false;
    	}
	}
	
	public boolean DeleteProduct(String strProductID)
	{
		String[] verinfo=GetVersionInfo(strProductID);
		if (verinfo[2].trim().equals(""))
		{
			JOptionPane.showMessageDialog(null, "You have not downloaded this product yet." );
			return false;
		}
		BaseFunctions.DeletePath(baseDownloadPath+verinfo[1]);
    	
    	// Update version info
    	verinfo[2]="";
    	verinfo[5]="";
    	return UpdateProductVersionInfo(strProductID, verinfo);
	}
	
	public boolean InstallProduct(String strProductID)
	{
		
    	// Get current version info
    	// (key:Product ID, value:(type, tar filename, downloaded version, installed version))
    	String[] verinfo=GetVersionInfo(strProductID);

    	if (verinfo[2].trim().equals(""))
    	{
    		//System.out.println("You have not downloaded this product yet!");
    		JOptionPane.showMessageDialog(null, "You have not downloaded this product yet." );
			return false;
    	}
    	
    	// Check version collision
    	String baseDir="";
    	if (verinfo[0].equals("sdk"))
    		baseDir=baseCompRepoPath;
    	else
    		baseDir=basePkgPath;
    	if (verinfo[6].equals(baseDir) && CompareVersions(verinfo[2],verinfo[3])<=0)
    	{
    		int ret = JOptionPane.showConfirmDialog(null, 
    				"<html>You already installed latest version.<br/>Do you want to reinstall anyway?</html>");
    		if (ret==JOptionPane.YES_OPTION)
    		{
    			UninstallProduct(strProductID);
    		}
    		else
    			return false;
    	}

    	// Untar
    	String tarpath=baseDownloadPath+verinfo[1];
    	
    	String untarDir=baseDir+verinfo[1].substring(0, verinfo[1].indexOf(".tar"))+"\\";
    	boolean ret=BaseFunctions.Untar(tarpath, untarDir);
    	if (!ret)
    	{
    		JOptionPane.showMessageDialog(null, 
    				"<html>Install Failed.<br/>Maybe tar file is incorrect!</html>");
    		BaseFunctions.DeletePath(untarDir);
    		return false;
    	}
    	// Get installation name from manifest file
    	String installDir=GetInstallNameFromUntaredPath(baseDir,untarDir);
    	
    	// Quit installation when same named product was installed already 
    	// and user select not to overwrite that.
    	if (installDir.equals(""))
    	{
    		JOptionPane.showMessageDialog(null, 
    				"<html>Install Failed.<br/>Maybe OPRoS.mf in tar file is incorrect!</html>");
    		BaseFunctions.DeletePath(untarDir);
    		return false;
    	}
    	
    	// For sdk, rename folder. For package, reorganize folder and files
    	if (verinfo[0].equals("sdk"))
    	{
    		try {
				if (!BaseFunctions.RenameAll(new File(untarDir), new File(installDir)))
					JOptionPane.showConfirmDialog(null, "rename failed");
			} catch (HeadlessException e) {
				//e.printStackTrace();
				JOptionPane.showConfirmDialog(null, "rename failed with HeadlessException");
			} catch (IOException e) {
				//e.printStackTrace();
				JOptionPane.showConfirmDialog(null, "rename failed with IOException");
			}
    	}
    	else
    	{
	    	new File( installDir ).mkdirs();
	    	
	    	// Reorganize package
	    	File aplDir = new File(untarDir);
	    	File pkgDir = new File(aplDir.getAbsolutePath()+"\\"+aplDir.list()[0]);
	    	
	    	String[] strPkgContent=pkgDir.list();
	    	for (int i=0; i<strPkgContent.length; i++) {
	            File pkgContent=new File(pkgDir.getAbsolutePath()+"\\"+strPkgContent[i]);	     
	            if (pkgContent.isDirectory())
	            {
	            	String strCompContent[]=pkgContent.list();
	            	for (int j=0; j<strCompContent.length; j++) {
	            		BaseFunctions.FileCopy(pkgContent.getAbsolutePath()+"\\"+strCompContent[j],
	            				installDir+strCompContent[j]);
	            	}
	            }
	            else
	            {
	            	if (pkgContent.getName().contains(".xml"))
	            	   	BaseFunctions.FileCopy(pkgContent.getAbsolutePath(), basePkgPath+pkgContent.getName());
	            	else
	            		BaseFunctions.FileCopy(pkgContent.getAbsolutePath(), installDir+pkgContent.getName());
	            }
	        	
	        }
		
	    	BaseFunctions.DeletePath(untarDir);
            
    	}

    	// Update version info
    	verinfo[3]=verinfo[2];
    	verinfo[4]=installDir;
    	verinfo[6]=baseDir;
    	return UpdateProductVersionInfo(strProductID, verinfo);
    	
	}
	
	public boolean UninstallProduct(String strProductID)
	{
    	// Get current version info
    	// (key:Product ID, value:(type, tar filename, downloaded version, installed version))
    	String[] verinfo=GetVersionInfo(strProductID);

    	if (verinfo[3].trim().equals(""))
    	{
    		//System.out.println("You have not installed this product yet!");
    		JOptionPane.showMessageDialog(null, "You have not installed this product yet!" );
			return false;
    	}
    	
    	// Delete
    	String deleteDir="";
   		deleteDir=verinfo[4];
    	BaseFunctions.DeletePath(deleteDir);
    	if (!verinfo[0].equals("sdk"))
    	{
    		BaseFunctions.DeletePath(deleteDir.substring(0, deleteDir.length()-1)+".xml");
    	}
    	// Update version info
    	verinfo[3]="";
    	verinfo[4]="";
    	verinfo[6]="";
    	return UpdateProductVersionInfo(strProductID, verinfo);
	}
	
	public boolean RunProduct(String strProductID)
	{
		// Get current version info
    	// (key:Product ID, value:(type, tar filename, downloaded version, installed version))
    	String[] verinfo=GetVersionInfo(strProductID);

    	if (verinfo[3].trim().equals(""))
    	{
    		//System.out.println("You have not installed this product yet!");
    		JOptionPane.showMessageDialog(null, "You have not installed this product yet!" );
			return false;
    	}
    	
    	// Check product type
    	if (verinfo[0].equals("sdk"))
    	{
    		//System.out.println("You have not installed this product yet!");
    		JOptionPane.showMessageDialog(null, "This is not a runnable package!" );
			return false;
    	}
    	    	
    	// Get package name
    	String pkgName = verinfo[4].replace(basePkgPath, "");
    	if (pkgName.endsWith("\\"))
    		pkgName = pkgName.substring(0, pkgName.length() - 1);
    	
    	// Get run.bat path
		StopRunningProduct();
		List<String> command = new ArrayList<String>();
		command.add("rundll32");
	    command.add("SHELL32.DLL,ShellExec_RunDLL");
	    command.add(baseBinPath + "runByClient.bat");
	    try
	    {
	    	
		    ProcessBuilder builder = new ProcessBuilder(command);
		    builder.directory(new File(baseBinPath));
		    proc = builder.start();
		    
		    Socket c=null;
		    for (int retry=0;retry < 15;retry++)
		    {
			    try
			    {
			    	c = new Socket("localhost",2003);
			    	break;
			    }
			    catch(Exception e)
			    {
			    	System.out.println("reconnect");
			    	Thread.sleep(100);
			    }
		    }
		    String line;

		    PrintStream out = System.out;   	
		            
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
               c.getOutputStream()));
            BufferedReader r = new BufferedReader(new InputStreamReader(
               c.getInputStream()));
            String m = null;
            w.write("ver=1.0;target=monitor;cmd=app.run;app.name="+pkgName+";payloadSize=0\n");	
            w.flush();
            m=r.readLine();
            out.println(m);
            
            w.close();
            r.close();
            c.close();
	    }
	    catch(Exception e)
	    {
	    	//JOptionPane.showMessageDialog(null, e.toString() );
	    	JOptionPane.showMessageDialog(null, "Failed to start application" );
	    	return false;
	    }
		return true;
	}
	
	public boolean StopRunningProduct()
	{
		if (proc==null)
			return true;
		
		try
	    {
			Socket c=null;
		    for (int retry=0;retry < 3;retry++)
		    {
			    try
			    {
			    	c = new Socket("localhost",2003);
			    	break;
			    }
			    catch(Exception e)
			    {
			    	System.out.println("reconnect");
			    	Thread.sleep(100);
			    }
		    }
		    String line;
	
		    PrintStream out = System.out;   	
		            
	        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
	           c.getOutputStream()));
	        BufferedReader r = new BufferedReader(new InputStreamReader(
	           c.getInputStream()));
	        String m = null;
	           
	        w.write("ver=1.0;target=monitor;cmd=app.stop;app.name=;payloadSize=0\n");	
            w.flush();
            m=r.readLine();
            out.println(m);
	        
	        w.close();
	        r.close();
	        c.close();
	        
	        Thread.sleep(2000);
	        
	        Runtime rt = Runtime.getRuntime();
	        if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) 
	           rt.exec("taskkill /F /IM ComponentEngine.exe");
	         else
	           rt.exec("kill -9 ComponentEngine.exe");
	        
	        proc=null;
	    }
		catch(Exception e)
		{
			//JOptionPane.showMessageDialog(null, e.toString() );
			return false;
		}
	
		return true;
	}
	
	public boolean UpdateProductVersionInfo(String strProductID, String[] verinfo)
	{
		String versionStr="";
		String comma="";
		for (int i=0;i<verinfo.length;i++)
		{
			String tmpStr=verinfo[i].trim();
			if (tmpStr.equals(""))
				tmpStr=" ";
			versionStr+=comma+tmpStr;
			comma=",";
		}
		try {
			prop_productVersionInfo.setProperty(strProductID, versionStr);
			prop_productVersionInfo.storeToXML(new FileOutputStream(strVerInfoFile), "Updated");
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public String[] GetVersionInfo(String strProductID)
	{
		// key:Product ID
		// value:(type, tar filename, downloaded version, installed version, installed path,download folder,base component/package folder))
		String[] verInfo=prop_productVersionInfo.getProperty(strProductID, " , , , , , , ").split(",");
		return verInfo;
	}
	
	public int CompareVersions(String strVer1, String strVer2)
	{
		int ret=0;
		String[] verInfo1=strVer1.trim().split(".");
		String[] verInfo2=strVer2.trim().split(".");
		if (strVer1.trim().equals(""))
		{
			if (strVer2.trim().equals(""))
				ret=0;
			else
				ret=1;
		}
		else
		{
			if (strVer2.trim().equals(""))
				ret=-1;
			else
			{
				for(int i=0;i<verInfo1.length;i++)
				{
					int verEle1=Integer.parseInt(verInfo1[i]);
					int verEle2=Integer.parseInt(verInfo2[i]);
					if (verEle1>verEle2)
					{
						ret=-1;
						break;
					}
					else if (verEle1<verEle2)
					{
						ret=1;
						break;
					}
				}
			}
		}
		
		return ret;
	}
	
	public String GetInstallNameFromUntaredPath(String baseDir, String untarredPath)
	{
		//JOptionPane.showConfirmDialog(null, untarredPath);
		String strRet="";
		String targetPrefix="Archive-name:";
		File dir = new File(untarredPath);
		if (dir.getName().equals("OPRoS.mf"))
		{
			//JOptionPane.showConfirmDialog(null, "OPRoS.mf");
			try {
				BufferedReader bfis=new BufferedReader(new FileReader(dir));
				String line;
				while ((line=bfis.readLine()) != null) {
					if (line.startsWith("Archive-name:"))
					{
						//JOptionPane.showConfirmDialog(null, line);
						String tmpPath=baseDir+line.replace(targetPrefix, "").replace(".tar", "").trim();
						String suffix="";
						String ext="";
						if (baseDir.equals(basePkgPath))
							ext=".xml";
						int cnt=0;
						File newDir;
						while((newDir=new File(tmpPath+suffix+ext)).exists())
						{
							//suffix="_"+(++cnt);
							int ret = JOptionPane.showConfirmDialog(null, 
				    				"<html>Target Path:"+tmpPath+
				    				"<br/>There is some product previously installed to the same path.<br/>"+
									"Do you want to overwrite them?</html>");
				    		if (ret!=JOptionPane.YES_OPTION)
				    		{
				    			bfis.close();
				    			return "";
				    		}
				    		else
				    		{
				    			BaseFunctions.DeletePath(tmpPath+suffix);
				    			if (!ext.equals(""))
				    				BaseFunctions.DeletePath(tmpPath+suffix+ext);
				    		}
						}
						strRet=tmpPath+suffix+"\\";
						break;
					}	
				}
				bfis.close();
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error Occured during opening manifest file!" );
			} catch (IOException e) {
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error Occured during reading manifest file!" );
			}
		}
		else if (dir.isDirectory())
		{
			String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            strRet = GetInstallNameFromUntaredPath(baseDir,dir.getAbsolutePath()+"\\"+children[i]);
	            if (!strRet.equals("")) {
	                break;
	            }
	        }
		}
		
			
		return strRet;
	}
}
