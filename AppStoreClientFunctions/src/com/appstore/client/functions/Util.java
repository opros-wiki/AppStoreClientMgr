package com.appstore.client.functions;



import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Util {
	public static void PrintXMLDoc(Document doc)
	{
		PrintNodeList(doc.getChildNodes());
	}
	
	public static void PrintNodeList(NodeList nl)
	{
		for ( int i = 0; i < nl.getLength(); i++ ) {
            Node n = nl.item( i );
            if (n.hasChildNodes())
            {
            	NodeList _child = n.getChildNodes();
            	PrintNodeList(_child);
            }
            if (n.getNodeName()!=null)
            {
            	System.out.println(n.getNodeName()+":"+n.getTextContent());
            }
        }
	}
	
}
