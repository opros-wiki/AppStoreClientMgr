package com.appstore.client.functions;




import java.io.*;
import java.net.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.*;

import javax.swing.JOptionPane;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xeustechnologies.jtar.*;


public class BaseFunctions {
	
	static final int BUFFER = 2048;
	
	
	// Get XML Document
	public static Document GetXMLDocument(String strURL, String paramStr, String method)
	{
		HttpURLConnection con;
		try
		{
			URL url = new URL(strURL); 
			
			con = (HttpURLConnection) url.openConnection();           
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setInstanceFollowRedirects(false); 
			con.setRequestMethod(method); 
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			con.setRequestProperty("charset", "utf-8");
			con.setRequestProperty("Content-Length", "" + Integer.toString(paramStr.getBytes().length));
			con.setUseCaches (false);
	
			DataOutputStream wr = new DataOutputStream(con.getOutputStream ());
			wr.writeBytes(paramStr);
			wr.flush();
			wr.close();
			
			InputStream is = con.getInputStream();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(is);
            return doc;
		}
		catch (Exception e)
		{
			System.out.println("Error occured in XML Request:"+e.toString());
			return null;
		}
	}
	
	
	// Get tag content
	public static String GetTagContentFromNode(Node n, String strTagName)
	{
		String ret="";
        
		if (n.getNodeName().equals(strTagName))
        	ret=n.getTextContent().trim();
        else
        {
        	NodeList _child = n.getChildNodes();
        	for ( int i = 0; i < _child.getLength(); i++ )
        	{
        		ret=GetTagContentFromNode(_child.item( i ), strTagName);
        		if (!ret.equals(""))
        			break;
        	}
        }
        return ret;
	}
	
	
	// Get Node list
	public static List<Node> GetListFromXMLDoc(Document doc, String strTagName)
	{
		return GetListFromNodeList(doc.getChildNodes(),strTagName);
	}
	
	public static List<Node> GetListFromNodeList(NodeList nl, String strTagName)
	{
		List<Node> ret=new ArrayList<Node>();
        for ( int i = 0; i < nl.getLength(); i++ ) {
            Node n = nl.item( i );
            if (n.getNodeName().equals(strTagName))
            {
            	ret.add(n);
            }
        }

        if (ret.isEmpty())
        {
        	for ( int i = 0; i < nl.getLength(); i++ ) {        	
        		Node n = nl.item( i );            
	        	NodeList _child = n.getChildNodes();
	            ret=GetListFromNodeList(_child, strTagName);
	            if (!ret.isEmpty())
	            	break;
	        }
        }
		return ret;
	}
	
	// Display list
	public static void DisplayList(List<Node> list, ArrayList<String> targetTags, HashMap referenceMap)
	{
		List<String> dl=new ArrayList<String>();
		
		for (int i=0; i<list.size();i++)
		{
			NodeList childs = list.get(i).getChildNodes();
			for (int j=0; j<childs.getLength();j++)
			{
				String nn=childs.item(j).getNodeName();
				if (targetTags.indexOf(nn)>-1)
					System.out.println(referenceMap.get(nn)+":"+childs.item(j).getTextContent());
			}
			
			System.out.println("-----------");
		}
	}
	
	
	// Downloader
	public static int Downloader(String strURL, String strTargetPath)
	{
		try
		{
	        // URL Connection
	        URL url = new URL(strURL);
	        url.openConnection();
	        InputStream reader = url.openStream();
	  
	        // Download destination
	        FileOutputStream writer = new FileOutputStream(strTargetPath);
	        
	        // Prepare buffer
	        byte[] buffer = new byte[153600];
	        int totalBytesRead = 0;
	        int bytesRead = 0;
	  
	        // Start download
	        while ((bytesRead = reader.read(buffer)) > 0)
	        {  
	           writer.write(buffer, 0, bytesRead);
	           buffer = new byte[153600];
	           totalBytesRead += bytesRead;
	        }
	  	        
	        writer.close();
	        reader.close();
	        return totalBytesRead;
		}
	    catch (MalformedURLException e)
	    {
	        e.printStackTrace();
	        return 0;
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
	
	// Untar
	public static boolean Untar(String zipFilePath, String unZipDir)
	{
		String pkgHeaderName="";
		//JOptionPane.showMessageDialog(null, "unZipDir:"+unZipDir );
		try {
			String destFolder = unZipDir;
	        File zf = new File( zipFilePath );
	
	        TarInputStream tis;
		
			tis = new TarInputStream( new BufferedInputStream( new FileInputStream(zf) ) );     
        
			BufferedOutputStream dest = null;
	
	        TarEntry entry;
	        //JOptionPane.showMessageDialog(null, "entry start." );
	        int entryCnt=0;
	        new File( destFolder ).mkdirs();
	        while (( entry = tis.getNextEntry() ) != null) {
	            //System.out.println( "Extracting: " + entry.getName() );
	        	entryCnt++;
	            int count;
	            byte data[] = new byte[BUFFER];
	
	            if (entry.isDirectory()) {
	                new File( destFolder+"\\"+entry.getName() ).mkdirs();
	                //JOptionPane.showMessageDialog(null, "MakeDir:"+destFolder+"\\"+entry.getName() );
	                continue;
	            } else {
	                int di = Math.max(entry.getName().lastIndexOf( '\\' ), entry.getName().lastIndexOf( '/' ));
	                if (di != -1) {
	                    new File( destFolder+"\\"+entry.getName().substring( 0, di ) ).mkdirs();
	                    //JOptionPane.showMessageDialog(null, "MakeDir:"+destFolder+"\\"+entry.getName().substring( 0, di ) );
	                }
	            }
	
	            //JOptionPane.showMessageDialog(null, "FileCopy:"+destFolder+"\\"+entry.getName() );
	            FileOutputStream fos = new FileOutputStream( destFolder+"\\"+entry.getName() );
	            dest = new BufferedOutputStream( fos );
	
	            while (( count = tis.read( data ) ) != -1) {
	                dest.write( data, 0, count );
	            }
	
	            dest.flush();
	            dest.close();
	        }
	        //JOptionPane.showMessageDialog(null, "entry end:"+entryCnt);
	        tis.close();
	        return (entryCnt > 0)?true:false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	// Delete
	public static boolean DeletePath(String strFolderPath)
	{
		File dir = new File(strFolderPath);
		if (dir.isDirectory())
		{
			String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = DeletePath(dir.getAbsolutePath()+"\\"+children[i]);
	            if (!success) {
	                return false;
	            }
	        }
		}
		return dir.delete();
	}

	// File copy
	public static boolean FileCopy(String source, String target) {
	  //복사 대상이 되는 파일 생성 
	  File sourceFile = new File( source );

	  //스트림, 채널 선언
	  FileInputStream inputStream = null;
	  FileOutputStream outputStream = null;
	  FileChannel fcin = null;
	  FileChannel fcout = null;

	  boolean ret=true;
	  try {
	   //스트림 생성
	   inputStream = new FileInputStream(sourceFile);
	   outputStream = new FileOutputStream(target);
	   //채널 생성
	   fcin = inputStream.getChannel();
	   fcout = outputStream.getChannel();
	   
	   //채널을 통한 스트림 전송
	   long size = fcin.size();
	   fcin.transferTo(0, size, fcout);
	   
	  } catch (Exception e) {
	   e.printStackTrace();
	   ret=false;
	  } finally {
	   //자원 해제
	   try{
	    fcout.close();
	   }catch(IOException ioe){
		   ret=false;
	   }
	   try{
	    fcin.close();
	   }catch(IOException ioe){
		   ret=false;
	   }
	   try{
	    outputStream.close();
	   }catch(IOException ioe){
		   ret=false;
	   }
	   try{
	    inputStream.close();
	   }catch(IOException ioe){
		   ret=false;
	   }
	  }
	  return ret;
	 }	
	
	public static boolean RenameAll(File fromFile, File toFile) throws IOException 
	{
		 if (fromFile.isDirectory()) 
		 {
		      File[] files = fromFile.listFiles();
		      if (files == null) {
		        //디렉토리 내 아무것도 없는 경우
		        return fromFile.renameTo(toFile);
		      } else {
		        //디렉토리내 파일 또는 디렉토리가 존재하는 경우
		        if(!toFile.mkdirs()) {
		          return false;
		        }
		        for (File eachFile : files) {
		          File toFileChild = new File(toFile, eachFile.getName());
		          if (eachFile.isDirectory()) {
		            if(!RenameAll(eachFile, toFileChild)) {
		              return false;
		            }
		          } else {
		            if(!eachFile.renameTo(toFileChild)) {
		              return false;
		            }
		          }
		        }
		        return fromFile.delete();
		      }
		} else {
		      //파일인 경우
		      if(fromFile.getParent() != null) {
		        if(!toFile.mkdirs()) {
		          return false;
		        }
		      }
		      return fromFile.renameTo(toFile);
		}
	}
	
}
