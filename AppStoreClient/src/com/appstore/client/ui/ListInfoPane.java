package com.appstore.client.ui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.GroupLayout.Alignment;
import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.appstore.client.functions.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class ListInfoPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Class instances
	public static AppStoreClientUI ascui;
	public static AppStoreFunctions asf;
	
	// Tag definition
	public static List<String> mainInfoTags=Arrays.asList(new String[] {"tname","tnamee","lastver","coin","coine"});
	public static String xmlcontentTag = "xmlcontent";
	public static List<String> DivColorStr = Arrays.asList(new String[] {
			"#BBBBBB","#CCCCCC","#DDDDDD","#EEEEEE","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF"
	});
	
	// Menu button on detailed info tab
	public static MenuButtonPane mbpDetail = null;
	
	// Make layouted html to be displayed on label component
	private static String MakeHtml(String imgPath,String txtInfoMainHtml, String txtInfoHtml, String xmlcontentHtml)
	{
		String imgTag="<img src='"+imgPath+"' width='80' height='80' align='left' />";
		String retHtml="<html><table>" 
					+"<tr><td>"+imgTag+"</td><td>"+txtInfoMainHtml+"</td></tr></table>"
					+"<table cellpaddding='0' cellspacing='0'>"
					+"<tr><td>"+txtInfoHtml+"</td></tr>"
					+"<tr><td>"+xmlcontentHtml+"</td></tr></table></html>";
		return retHtml;
	}
	
	// Make HTML string to display XML
	private static String MakeXMLDepthString(NodeList nl, String curXMLStr, String depth)
	{
		String retStr = curXMLStr;
		for ( int i = 0; i < nl.getLength(); i++ ) {
            Node n = nl.item( i );
            NodeList nlChild = n.getChildNodes();
            String nn = n.getNodeName();
            if (nn.equalsIgnoreCase("#text") || nn.equalsIgnoreCase("#comment"))
            	continue;

            if (nlChild.getLength() > 1)
            {
            	String line = depth + "+" + nn;
            	retStr = MakeXMLDepthString(nlChild, 
            			retStr + GetDivTag(depth) + line.replace(" ", "&nbsp;") + "</div>", depth + "  ");
            }
            else
            {
            	String line = depth + "-" + nn + ": " + n.getTextContent().trim();
            	if (line.length() > 37)
            		line = line.substring(0, 37) + "<br/>" + line.substring(37);
            	retStr += GetDivTag(depth) + line.replace(" ", "&nbsp;") + "</div>";
            }
            NamedNodeMap nnm = n.getAttributes();
        	for (int j=0; j<nnm.getLength();j++ )
        	{
        		String depth2 = depth + "  ";
        		Node nAttr = nnm.item(j);
        		String line = depth2 + "#" + nAttr.getNodeName() + ": " + nAttr.getTextContent().trim();
        		if (line.length() > 37)
            		line = line.substring(0, 37) + "<br/>" + line.substring(37);
        		retStr += GetDivTag(depth2) + line.replace(" ", "&nbsp;") + "</div>";
        	}
        }

		return retStr;
	}
	
	private static String GetDivTag(String depth)
	{
		return "<div style='background-color:"+DivColorStr.get(depth.length() / 2)+"'>";
	}
	
	private  static ListInfoPane MakeListInfoPane(NodeList nl, ArrayList<String> targetTags, HashMap referenceMap, MenuButtonPane mbp)
	{
		String proCode="";
		String lastVer="";
		String txtInfoMain="";
		String txtInfo="";
		String imgTag="";
		String xmlcontent="";
		for (int j=0; j<nl.getLength();j++)
		{
			String nn=nl.item(j).getNodeName();
			String strContent=nl.item(j).getTextContent();
			if (nn.equalsIgnoreCase("tcode"))
				proCode=strContent;
			else if (nn.equalsIgnoreCase("lastver"))
				lastVer=strContent;
			if (targetTags.contains(nn) &&  !targetTags.equals("lastver_url"))
			{
				if (nn.equalsIgnoreCase("disimg"))
					imgTag=strContent;
				else
				{
					String[] strDisp=strContent.replace("^", "<></>").split("<></>");
					String infoLine=referenceMap.get(nn)+":";
					
					switch (strDisp.length)
					{
					case 3:
						//infoLine+=String.format("%s(%s,%s)",strDisp[1],strDisp[0],strDisp[2]);
						infoLine+=strDisp[1];
						break;
					case 2:
						//infoLine+=String.format("%s(%s)",strDisp[1],strDisp[0]);
						infoLine+=strDisp[1];
						break;
					case 1:
						infoLine+=strDisp[0].replace("<", "&lt;").replace(">", "&gt;").replace(""+(char)10, "<br/>");
						break;
					}
					infoLine+="<br/>";
					if (mainInfoTags.contains(nn))
						txtInfoMain+=infoLine;
					else if (xmlcontentTag.equals(nn))
					{
						try {
							strDisp[0] = strDisp[0].replace("<?", "<!--?").replace("?>", "?-->");
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				            DocumentBuilder builder;
							builder = factory.newDocumentBuilder();
							Document doc = builder.parse(new ByteArrayInputStream(strDisp[0].getBytes("UTF-8")));
							xmlcontent = referenceMap.get(nn)+":<br/>";
							xmlcontent += MakeXMLDepthString(doc.getChildNodes(), "", "");
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

			            
					}
					else
						txtInfo+=infoLine;
				}
			}
		}

		return new ListInfoPane(proCode, lastVer, MakeHtml(imgTag, txtInfoMain, txtInfo, xmlcontent), mbp);
	}
	
	// Display list
	public static void DisplayListOnPane(
			List<Node> list, ArrayList<String> targetTags, HashMap referenceMap, JPanel pane, MenuButtonPane mbp)
	{
		List<String> dl=new ArrayList<String>();
		pane.removeAll();
		if (list==null)
		{
			JLabel noRes=new JLabel("<html>No Response<br/>From Server</html>", JLabel.CENTER);
			noRes.setForeground(Color.LIGHT_GRAY);
			noRes.setFont(new Font("Courier New", Font.BOLD, 20));
			pane.add(noRes);
			pane.getLayout().addLayoutComponent("NoResponse", noRes);
		}
		else
		{
			int liSize=list.size();
			if (liSize == 0)
			{
				JLabel noRes=new JLabel("No Result", JLabel.CENTER);
				noRes.setForeground(Color.PINK);
				noRes.setFont(new Font("Courier New", Font.BOLD, 32));
				pane.add(noRes);
				pane.getLayout().addLayoutComponent("NoResult", noRes);
			}
			else
			{
				for (int i=0; i<liSize;i++)
				{
					ListInfoPane lip=MakeListInfoPane(list.get(i).getChildNodes(),targetTags,referenceMap, mbp);
					pane.add(lip);
					pane.getLayout().addLayoutComponent("list", lip);
				}
			}
		}
		pane.updateUI();
	}
	
	// Display detailed info
	public static void DisplayDetailedInfo(List<Node> list)
	{		
		ascui.scrollPane_detail.getViewport().removeAll();
		if (list==null)
		{
			JLabel noRes=new JLabel("<html>No Response<br/>From Server</html>", JLabel.CENTER);
			noRes.setForeground(Color.LIGHT_GRAY);
			noRes.setFont(new Font("Courier New", Font.BOLD, 20));
			ascui.scrollPane_detail.setViewportView(noRes);
		}
		else
		{
			List<String> dl=new ArrayList<String>();		
			ListInfoPane lip=MakeListInfoPane(list.get(0).getChildNodes(),asf.displayingTags_DetailedInfo,asf.map_detailedInfoTag, mbpDetail);
			mbpDetail.selectedPane = lip;		
			ascui.scrollPane_detail.setViewportView(lip);
		}
		ascui.tabbedPane.setSelectedComponent(ascui.pan_detail);
		ascui.tabbedPane.updateUI();

	}
	
	
	/**
	 * inner class : Mouse handler
	 */
	private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        	if (mbp.selectedPane != null)
        		mbp.selectedPane.setBackground(Color.WHITE );
        	mbp.selectedPane = (ListInfoPane)e.getComponent();
        	mbp.selectedPane.setBackground(Color.GREEN );
        }
    }
	
	/**
	 * Create the panel.
	 */
	public String productCode;
	public JLabel lblListInfo;
	private MenuButtonPane mbp = null;
	private MouseHandler mouseHandler = new MouseHandler();
	public ListInfoPane(String _productCode, String lastVer, String _productInfo, MenuButtonPane _mbp)
	{
		mbp = _mbp;
		this.addMouseListener(mouseHandler);
		
		setSize(280, 120);
		setForeground(Color.DARK_GRAY);
		setBackground(Color.WHITE);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		
		// Attach to the form
		lblListInfo = new JLabel("Information");
		lblListInfo.setVerticalAlignment(SwingConstants.TOP);
		
		// variables setting
		productCode=_productCode;
		lblListInfo.setText(_productInfo);
		
		/*// Button Creation
		JButton btnDetail = new JButton("");
		btnDetail.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/DetailedInfo.png")));
		
		JButton btnDownload = new JButton("");	
		btnDownload.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Download.png")));
		
		JButton btnInstall = new JButton("");
		btnInstall.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Install.png")));
		
		JButton btnWebPage = new JButton("");
		btnWebPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

			        if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
			            System.err.println( "Desktop doesn't support web browser action" );
			        }
			        try {
			        	String baseWebURL="";
			        	if (productCode.substring(0, 3).equals("SDK"))
			        	{
			        		baseWebURL=asf.baseURL+"store/component/";
			        	}
			        	else
			        	{
			        		baseWebURL=asf.baseURL+"store/application/";
			        	}
			            java.net.URI uri = new java.net.URI(baseWebURL+"?productId="+productCode);
			            desktop.browse( uri );
			        }        
			        catch (IOException _e) {
			            System.err.println( _e.getMessage() );
			        } 
			        catch (URISyntaxException _e) {
			            System.err.println( _e.getMessage() );
			        }
			}
		});
		btnWebPage.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Web.png")));
		
		JButton btnDelete = new JButton("");
		btnDelete.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Del.png")));
		
		JButton btnUninstall = new JButton("");
		btnUninstall.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Uninstall.png")));
				
		// Button Event
		btnDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ascui.ShowProgressMonitor(Messages.WAIT_DETAILEDINFO);
				SwingWorker aWorker = new SwingWorker() 
				{
					@Override
					protected Object doInBackground() throws Exception {
						List<Node> list=asf.GetDetailedInfo(productCode);
						ListInfoPane.DisplayDetailedInfo(list);
						return null;
					}
					
					@Override
					public void done()
					{
						ascui.HideProgressMonitor();
					}
				};
				aWorker.execute();				
			}
		});
		
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ascui.ShowProgressMonitor(Messages.WAIT_DOWNLOADING);
				SwingWorker aWorker = new SwingWorker() 
				{
					boolean ret;
					@Override
					protected Object doInBackground() throws Exception {
						ret = asf.DownloadProduct(productCode);
						return null;
					}
					
					@Override
					public void done()
					{
						ascui.HideProgressMonitor();
						if (ret)
							JOptionPane.showMessageDialog(null, Messages.SUCCESS_DOWNLOADING );
					}
				};
				aWorker.execute();
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (asf.DeleteProduct(productCode))
					JOptionPane.showMessageDialog(null, Messages.SUCCESS_DELETING );
			}
		});
				
		btnInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ascui.ShowProgressMonitor(Messages.WAIT_INSTALLING);
				SwingWorker aWorker = new SwingWorker() 
				{
					boolean ret;
					@Override
					protected Object doInBackground() throws Exception {
						ret = asf.InstallProduct(productCode);
						return null;
					}
					
					@Override
					public void done()
					{
						ascui.HideProgressMonitor();
						if (ret)
							JOptionPane.showMessageDialog(null, Messages.SUCCESS_INSTALLING );
					}
				};
				aWorker.execute();
			}
		});

		btnUninstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (asf.UninstallProduct(productCode))
					JOptionPane.showMessageDialog(null, Messages.SUCCESS_UNINSTALLING );
			}
		});
		
		JButton btnPlay = new JButton("");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Run package
				if (!asf.RunProduct(productCode))
					JOptionPane.showMessageDialog(null, "Run failed" );
			}
		});
		btnPlay.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/play.png")));
		
		JButton btnStop = new JButton("");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Stop package
				if (!asf.StopRunningProduct())
					JOptionPane.showMessageDialog(null, "Failed to sop" );
			}
		});
		btnStop.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/stop.png")));
		*/
		// Layout setting
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						/*.addGroup(groupLayout.createSequentialGroup()
							.addGap(8)
							.addComponent(btnDetail, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addComponent(btnDownload, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addComponent(btnInstall, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addComponent(btnUninstall, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addComponent(btnWebPage, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnPlay, GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE)
							.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED))*/
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(lblListInfo, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED))
					
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						/*.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))*/
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblListInfo, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							/*.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnDetail, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnDownload, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnInstall, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnUninstall, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnWebPage, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnPlay, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))*/))
					.addGap(5))
		);
		setLayout(groupLayout);
	}
}
