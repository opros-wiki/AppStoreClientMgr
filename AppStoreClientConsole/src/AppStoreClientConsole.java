


import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.appstore.client.functions.*;


public class AppStoreClientConsole {
	public static void main(String args[])
	{
		
		AppStoreFunctions asf=new AppStoreFunctions();
		Scanner sc = new Scanner(System.in);
		List<Node> list;
		String[] menuStr={"Menu",
						  "1.Login",
						  "2.View purchased list",
						  "3.Component Search",
						  "4.App search",
						  "5.View detailed info",
						  "6.Purchase free product",
						  "7.Download component/app",
						  "8.Install component/app",
						  "9.Uninstall component/app",
						  "11.Change settings",
						  "12.Switch to autologin",
						  "13.Switch to manuallogin",
						  "20.Exit"};
		boolean bLoop=true;
		while(bLoop)
		{
			System.out.println("Base URL:"+asf.baseURL);
			for (int i=0;i<menuStr.length;i++)
			{
				System.out.println(menuStr[i]);
			}
			
			// Command Input
		    System.out.print("Command: ");
		    String line = sc.nextLine();
		    int selMenu=Integer.parseInt(line);
		    System.out.println(menuStr[selMenu]);
		    switch(selMenu)
		    {
		    case 1:
		    	System.out.print("ID: ");
		    	String id=sc.nextLine();
		    	System.out.print("Password: ");
		    	String pwd=sc.nextLine();
		    	int ret=asf.login(id,pwd);
		    	if (ret==1)
		    		System.out.println(asf.pkey);
		    	else
		    		System.out.println("failed");
		    	break;
		    case 2:
		    	System.out.print("OrderBy(senddate,thgbn,thname): ");
		    	String orderBy=sc.nextLine();
		    	System.out.print("Sequence(asc,desc): ");
		    	String ascDesc=sc.nextLine();
		    	list=asf.GetPurchasedList(orderBy,ascDesc);
				BaseFunctions.DisplayList(list, asf.displayingTags_PurchasedList, asf.map_purchasedListTag);
				break;
		    case 3:
		    	System.out.print("OrderBy(regdate,thname): ");
		    	orderBy=sc.nextLine();
		    	System.out.print("Sequence(asc,desc): ");
		    	ascDesc=sc.nextLine();
		    	System.out.print("Language Code(all): ");
		    	String lang=sc.nextLine();
		    	if (lang.equals(""))
		    		lang="all";
		    	System.out.print("Platform Code(all): ");
		    	String pfcd=sc.nextLine();
		    	if (pfcd.equals(""))
		    		pfcd="all";
		    	System.out.print("Component name: ");
		    	String compName=sc.nextLine();
		    	System.out.print("Start date(YYYYMMDD): ");
		    	String stDate=sc.nextLine();
		    	if (stDate.equals(""))
		    		stDate="000000";
		    	System.out.print("End date(YYYYMMDD): ");
		    	String endDate=sc.nextLine();
		    	if (endDate.equals(""))
		    		endDate="999999";
		    	
		    	list=asf.GetComponentList(orderBy, ascDesc,lang,pfcd,compName,stDate,endDate);
		    	BaseFunctions.DisplayList(list, asf.displayingTags_SearchedList, asf.map_searchedListTag);
				break;
		    case 4:
		    	System.out.print("OrderBy(regdate,thname): ");
		    	orderBy=sc.nextLine();
		    	System.out.print("Sequence(asc,desc): ");
		    	ascDesc=sc.nextLine();
		    	System.out.print("Platform Code(all): ");
		    	pfcd=sc.nextLine();
		    	if (pfcd.equals(""))
		    		pfcd="all";
		    	System.out.print("robotid Code(all): ");
		    	String robotid=sc.nextLine();
		    	if (robotid.equals(""))
		    		robotid="all";
		    	System.out.print("App name: ");
		    	String appName=sc.nextLine();
		    	System.out.print("Start date(YYYYMMDD): ");
		    	stDate=sc.nextLine();
		    	if (stDate.equals(""))
		    		stDate="000000";
		    	System.out.print("End date(YYYYMMDD): ");
		    	endDate=sc.nextLine();
		    	if (endDate.equals(""))
		    		endDate="999999";
		    	
		    	list=asf.GetAppList(orderBy, ascDesc,pfcd,robotid,appName,stDate,endDate);
		    	BaseFunctions.DisplayList(list, asf.displayingTags_SearchedList, asf.map_searchedListTag);
				break;
		    case 5:
		    	System.out.print("Product Code: ");
		    	String tcode=sc.nextLine();
		    	
		    	list=asf.GetDetailedInfo(tcode);
		    	BaseFunctions.DisplayList(list, asf.displayingTags_DetailedInfo, asf.map_detailedInfoTag);
				break;
		    case 6:
		    	System.out.print("Product Code: ");
		    	tcode=sc.nextLine();
		    	
		    	list=asf.PurchaseFreeProduct(tcode);
		    	if (BaseFunctions.GetTagContentFromNode(list.get(0), "code").equals("000"))
		    		System.out.println("succeeded");
		    	else
		    		System.out.println("failed");
				break;
		    case 7:
		    	System.out.print("Product Code: ");
		    	tcode=sc.nextLine();
		    	asf.DownloadProduct(tcode);
		    	break;
		    case 8:
		    	System.out.print("Product Code: ");
		    	tcode=sc.nextLine();
		    	asf.InstallProduct(tcode);
		    	break;
		    case 11:
		    	Enumeration<Object> e_key=asf.prop_conf.keys();
		    	while(e_key.hasMoreElements())
		    	{
		    		String strKey=(String)e_key.nextElement();
		    		System.out.print(strKey+"("+asf.prop_conf.getProperty(strKey)+"):");
		    		String newStr=sc.nextLine();
		    		if (newStr.length()>0)
		    			asf.prop_conf.setProperty(strKey, newStr);
		    	}
		    	asf.SaveSettings();
		    	break;
		    case 12:
		    	asf.prop_conf.setProperty("pkey", asf.pkey);
		    	asf.SaveSettings();
		    	break;
		    case 13:
		    	asf.prop_conf.setProperty("pkey", "");
		    	asf.SaveSettings();
		    	break;
		    default:
		    	bLoop=false;
		    	break;
		    }
		}
	    
	    
		// test
		
		
		//String pkey = asf.login(baseURL, "yujintester2","a0000!!");
		//List list=asf.GetPurchasedList(baseURL, pkey, "", "");
		//System.out.println("Purchased List count:"+list.size());
		//list=asf.GetComponentList(baseURL, pkey, "", "","all","all","","00000000","99999999");
		//System.out.println("Component List count:"+list.size());
		//list=asf.GetAppList(baseURL, pkey, "", "","all","all","","00000000","99999999");
		//System.out.println("Component List count:"+list.size());
		//Document doc=asf.GetDetailedInfo(baseURL, pkey, "SDK20120529210136");
		//System.out.println("Got Detailed Info");		
		//list=asf.GetVersionList(baseURL, pkey, "SDK20120529210136");
		//System.out.println("Component List count:"+list.size());
		//list=asf.GetCodeList(baseURL, pkey, "BT");
		//System.out.println("Code List count:"+list.size());
		//asf.PurchaseFreeProduct(baseURL, pkey, "SDK20120529210136");
		//System.out.println("Purchased");		
		
		/*
		int downloadedBytes = bf.Downloader("http://www.getmura.com/currentversion/", "C:\\Users\\Maxdh\\Desktop\\mura.zip");
		if (downloadedBytes > 0)
			System.out.println("Downloaded!!");
		else
			System.out.println("Downloaded error!!");*/
		/*boolean res = bf.Untar("C:\\Users\\Maxdh\\Desktop\\AAA\\hhh.tar", "C:\\Users\\Maxdh\\Desktop\\AAA\\CCC\\");
		if (res)
			System.out.println("Unzipped!!");
		else
			System.out.println("Unzip failed!!");
		
		res = bf.DeletePath("C:\\Users\\Maxdh\\Desktop\\AAA\\CCC\\");
		if (res)
			System.out.println("Deleted!!");
		else
			System.out.println("Delete failed!!");*/
	}
}
