package com.appstore.client.ui;

import java.awt.*;
import java.awt.event.*;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.*;
import javax.swing.event.*;

import org.w3c.dom.Node;

import com.appstore.client.functions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class AppStoreClientUI {
	public AppStoreFunctions asf=new AppStoreFunctions();
	private String[][] confbaseInfo=new String[][] {
			{"baseURL","Base URL"},
			{"baseDownloadPath","Download Folder"},
			{"basePkgPath","Package Folder(Robot)"},
			{"baseBinPath","Binary Folder(Robot)"},
			{"baseCompRepoPath","Repository Folder(Dev.)"},
			{"DefaultBoundary.x","Default Left"},
			{"DefaultBoundary.y","Default Top"},
			{"DefaultBoundary.width","Default Width"},
			{"DefaultBoundary.height","Default Height"}/*,
			{"baseWebInfoURL_COMP","Web Info URL(COMP)"},
			{"baseWebInfoURL_APL","Web Info URL(APL)"},*/};
	private String[] codeList_DL,meaningList_DL,codeList_PF,meaningList_PF;
		
	public JFrame frmAppstoreclient;
	JDialog dialog;
	//Sequencer sequencer;
	//Sequence seq;
	//ProgressMonitor pm = new ProgressMonitor(frmAppstoreclient, "Please wait...", null, 0, 100);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					AppStoreClientUI window = new AppStoreClientUI();
					window.frmAppstoreclient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppStoreClientUI() {		
		// Variable links
		ListInfoPane.ascui=this;
		ListInfoPane.asf=asf;
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	JTabbedPane tabbedPane;
	JPanel pan_detail;
	JScrollPane scrollPane_purList;
	JPanel pan_srchCond_purList;
	JPanel pan_srchCond_compSrch;
	JPanel pan_srchCond_appSrch;
	JPanel listPanel_purList;
	JPanel listPanel_compSrch;
	JPanel listPanel_appSrch;
	JScrollPane scrollPane_detail;
	
	SrchCond_combo srchCond_platform_compSrch;
	SrchCond_combo srchCond_lang_compSrch;
	SrchCond_combo srchCond_platform_appSrch;
	SrchCond_combo srchCond_lang_appSrch;
	
	// Configuration Tab
	final JPanel pan_conf = new JPanel();
	final ArrayList<Object> chkBxArr=new ArrayList<Object>();
	final JPanel pan_conf_top = new JPanel();
	final SrchCond_txtIn srchCond_id = new SrchCond_txtIn("ID    : ");
	final SrchCond_pwdIn srchCond_pwd = new SrchCond_pwdIn("PWD : ");
	final JButton srchCond_btnLogin = new JButton("Log In");
	final SrchCond_btn srchCond_btnLogout = new SrchCond_btn("Log Out");
	final JCheckBox chckbxKeepLoggedIn = new JCheckBox("Keep Logged In");
	final JScrollPane scrollPane_conf = new JScrollPane();
	final JPanel listPanel_conf = new JPanel();
	final SrchCond_combo selConfCategory = new SrchCond_combo(
			"",
			new String[] {"Base Settings","Purchased List","Searched List", "Detailed Info"},
			new String[] {"Base Settings","DisplayingTags_PurchasedList","DisplayingTags_SearchedList", "DisplayingTags_DetailedInfo"});
	
	private void initialize() {
		//
		// Main Frame
		//
		frmAppstoreclient = new JFrame();
		frmAppstoreclient.setBounds(
				Integer.parseInt(asf.prop_conf.getProperty("DefaultBoundary.x","0")),
				Integer.parseInt(asf.prop_conf.getProperty("DefaultBoundary.y","0")),
				Integer.parseInt(asf.prop_conf.getProperty("DefaultBoundary.width","300")),
				Integer.parseInt(asf.prop_conf.getProperty("DefaultBoundary.height","480")));
		frmAppstoreclient.setIconImage(Toolkit.getDefaultToolkit().getImage(AppStoreClientUI.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		frmAppstoreclient.setBackground(Color.WHITE);
		frmAppstoreclient.getContentPane().setBackground(Color.WHITE);	
		frmAppstoreclient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frmAppstoreclient.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				Dimension sz=frmAppstoreclient.getSize();
				int colNum=sz.width / 280;
				if (colNum<1)
					colNum=1;
				//panel_3.setSize(sz.width-50,panel_3.getSize().height);
				pan_srchCond_purList.setLayout(new GridLayout(0, colNum, 0, 0));
				pan_srchCond_compSrch.setLayout(new GridLayout(0, colNum, 0, 0));
				pan_srchCond_appSrch.setLayout(new GridLayout(0, colNum, 0, 0));
				
				listPanel_compSrch.setLayout(new GridLayout(0, colNum, 0, 0));
				listPanel_appSrch.setLayout(new GridLayout(0, colNum, 0, 0));
				listPanel_purList.setLayout(new GridLayout(0, colNum, 0, 0));
			}
		});

		//
		// Tab
		//
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);	
		GroupLayout groupLayout = new GroupLayout(frmAppstoreclient.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
					.addGap(4))
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(5)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
						.addGap(5))
			);
			frmAppstoreclient.getContentPane().setLayout(groupLayout);
			
		//
		// Component Search
		//
		// 1. Tab pane
		JPanel pan_compSrch = new JPanel();
		pan_compSrch.setBackground(Color.WHITE);
		tabbedPane.addTab("Component", null, pan_compSrch, null);
		pan_compSrch.setLayout(new BorderLayout(0, 0));
		
		// 2. Outer split pane
		JSplitPane splitPane_compSrch = new JSplitPane();
		splitPane_compSrch.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_compSrch.setDividerSize(2);
		pan_compSrch.add(splitPane_compSrch);
		
		// 3. Inner split pane
		JSplitPane innerSplitPane_compSrch = new JSplitPane();
		innerSplitPane_compSrch.setOrientation(JSplitPane.VERTICAL_SPLIT);
		innerSplitPane_compSrch.setDividerSize(2);
		splitPane_compSrch.setLeftComponent(innerSplitPane_compSrch);
		
		// 4. Search condition panel
		pan_srchCond_compSrch = new JPanel();
		pan_srchCond_compSrch.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Conditions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));
		pan_srchCond_compSrch.setBackground(Color.WHITE);
		pan_srchCond_compSrch.setLayout(new GridLayout(0, 1, 0, 0));
		innerSplitPane_compSrch.setLeftComponent(pan_srchCond_compSrch);
		
		// 5. Components of search condition panel
		srchCond_platform_compSrch = new SrchCond_combo("Platform");
		pan_srchCond_compSrch.add(srchCond_platform_compSrch);
		
		final SrchCond_txtIn srchCond_compName_compSrch = new SrchCond_txtIn("Name");
		pan_srchCond_compSrch.add(srchCond_compName_compSrch);
		
		srchCond_lang_compSrch = new SrchCond_combo("Language");
		pan_srchCond_compSrch.add(srchCond_lang_compSrch);
		
		final SrchCond_date srchCond_dateFrom_compSrch = new SrchCond_date("Date From");
		pan_srchCond_compSrch.add(srchCond_dateFrom_compSrch);
		
		final SrchCond_date srchCond_dateTo_compSrch = new SrchCond_date("Date To");
		srchCond_dateTo_compSrch.SetToday();
		pan_srchCond_compSrch.add(srchCond_dateTo_compSrch);
		
		final SrchCond_dblCombo srchCond_order_compSrch = new SrchCond_dblCombo(
				new String[] {"Product Name","Registered Date"}, 
				new String[] {"thname","regdate"});
		pan_srchCond_compSrch.add(srchCond_order_compSrch);
				
		// 6. Button menu
		final MenuButtonPane menuBtnPane_compSrch = new MenuButtonPane(this);
		innerSplitPane_compSrch.setRightComponent(menuBtnPane_compSrch);
		
		// 7. Scroll panel for result
		JScrollPane scrollPane_compSrch = new JScrollPane();
		scrollPane_compSrch.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane_compSrch.setViewportBorder(null);
		scrollPane_compSrch.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane_compSrch.setRightComponent(scrollPane_compSrch);
		
		// 8. Result panel on scroll panel
		listPanel_compSrch = new JPanel();
		listPanel_compSrch.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Result", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		listPanel_compSrch.setBackground(Color.WHITE);
		listPanel_compSrch.setLayout(new GridLayout(0, 1, 0, 0));
		scrollPane_compSrch.setViewportView(listPanel_compSrch);
		
		// 9. Add search event
		srchCond_order_compSrch.btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ShowProgressMonitor(Messages.WAIT_COMPONENTLIST);
				SwingWorker aWorker = new SwingWorker() 
				{
					@Override
					protected Object doInBackground() throws Exception {
						// TODO Auto-generated method stub
						List<Node> list=asf.GetComponentList(
								srchCond_order_compSrch.GetValue1(),
								srchCond_order_compSrch.GetValue2(), 
								srchCond_lang_compSrch.GetValue1(),
								srchCond_platform_compSrch.GetValue1(),
								srchCond_compName_compSrch.GetValue1(),
								srchCond_dateFrom_compSrch.GetValue1(),
								srchCond_dateTo_compSrch.GetValue1());
						ListInfoPane.DisplayListOnPane(
								list, asf.displayingTags_SearchedList, asf.map_searchedListTag, listPanel_compSrch, menuBtnPane_compSrch);
						menuBtnPane_compSrch.selectedPane = null;
						return null;
					}
					
					@Override
					public void done()
					{
						HideProgressMonitor();
					}
				};
				aWorker.execute();
			}
		});
		
		//
		// App Search
		//
		// 1. Tab pane
		JPanel pan_appSrch = new JPanel();
		pan_appSrch.setBackground(Color.WHITE);
		tabbedPane.addTab("Application", null, pan_appSrch, null);
		pan_appSrch.setLayout(new BorderLayout(0, 0));
		
		// 2. Outer split pane
		JSplitPane splitPane_appSrch = new JSplitPane();
		splitPane_appSrch.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_appSrch.setDividerSize(2);
		pan_appSrch.add(splitPane_appSrch, BorderLayout.CENTER);
		
		// 3. Inner split pane
		JSplitPane innerSplitPane_appSrch = new JSplitPane();
		innerSplitPane_appSrch.setOrientation(JSplitPane.VERTICAL_SPLIT);
		innerSplitPane_appSrch.setDividerSize(2);
		splitPane_appSrch.setLeftComponent(innerSplitPane_appSrch);
		
		// 4. Search condition panel
		pan_srchCond_appSrch = new JPanel();
		pan_srchCond_appSrch.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Conditions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));
		pan_srchCond_appSrch.setBackground(Color.WHITE);
		pan_srchCond_appSrch.setLayout(new GridLayout(0, 1, 0, 0));
		innerSplitPane_appSrch.setLeftComponent(pan_srchCond_appSrch);
		
		// 5. Components of search condition panel
		srchCond_platform_appSrch = new SrchCond_combo("Platform");
		pan_srchCond_appSrch.add(srchCond_platform_appSrch);
		
		final SrchCond_txtIn srchCond_appName_appSrch = new SrchCond_txtIn("Name");
		pan_srchCond_appSrch.add(srchCond_appName_appSrch);
		
		srchCond_lang_appSrch = new SrchCond_combo("Language");
		pan_srchCond_appSrch.add(srchCond_lang_appSrch);
		
		final SrchCond_date srchCond_dateFrom_appSrch = new SrchCond_date("Date From");
		pan_srchCond_appSrch.add(srchCond_dateFrom_appSrch);
		
		final SrchCond_date srchCond_dateTo_appSrch = new SrchCond_date("Date To");
		srchCond_dateTo_appSrch.SetToday();
		pan_srchCond_appSrch.add(srchCond_dateTo_appSrch);
		
		final SrchCond_dblCombo srchCond_order_appSrch = new SrchCond_dblCombo(
				new String[] {"Product Name","Registered Date"}, 
				new String[] {"thname","regdate"});
		pan_srchCond_appSrch.add(srchCond_order_appSrch);
		
		// 6. Button menu
		final MenuButtonPane menuBtnPane_appSrch = new MenuButtonPane(this);
		innerSplitPane_appSrch.setRightComponent(menuBtnPane_appSrch);
		
		// 7. Scroll panel for result
		JScrollPane scrollPane_appSrch = new JScrollPane();
		scrollPane_appSrch.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane_appSrch.setViewportBorder(null);
		scrollPane_appSrch.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane_appSrch.setRightComponent(scrollPane_appSrch);
		
		// 8. Result panel on scroll panel
		listPanel_appSrch = new JPanel();
		listPanel_appSrch.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Result", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		listPanel_appSrch.setBackground(Color.WHITE);
		listPanel_appSrch.setLayout(new GridLayout(0, 1, 0, 0));
		scrollPane_appSrch.setViewportView(listPanel_appSrch);
		
		// 9. Add search event
		srchCond_order_appSrch.btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShowProgressMonitor(Messages.WAIT_APPLICATIONLIST);
				SwingWorker aWorker = new SwingWorker() 
				{
					@Override
					protected Object doInBackground() throws Exception {
						List<Node> list=asf.GetAppList(
								srchCond_order_appSrch.GetValue1(),
								srchCond_order_appSrch.GetValue2(), 
								srchCond_lang_appSrch.GetValue1(),
								srchCond_platform_appSrch.GetValue1(),
								srchCond_appName_appSrch.GetValue1(),
								srchCond_dateFrom_appSrch.GetValue1(),
								srchCond_dateTo_appSrch.GetValue1());
						ListInfoPane.DisplayListOnPane(
								list, asf.displayingTags_SearchedList, asf.map_searchedListTag, listPanel_appSrch, menuBtnPane_appSrch);
						menuBtnPane_appSrch.selectedPane = null;
						return null;
					}
					
					@Override
					public void done()
					{
						HideProgressMonitor();
					}
				};
				aWorker.execute();
			}
		});
		
		//
		// Purchased List
		//
		// 1. Tab pane
		JPanel pan_purList = new JPanel();
		pan_purList.setBackground(Color.WHITE);
		pan_purList.setLayout(new BorderLayout(0, 0));
		tabbedPane.addTab("Purchased", null, pan_purList, null);
		
		// 2. Outer split pane
		JSplitPane splitPane_purList = new JSplitPane();
		splitPane_purList.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_purList.setDividerSize(2);
		pan_purList.add(splitPane_purList);
		
		// 3. Inner split pane
		JSplitPane innerSplitPane_purList = new JSplitPane();
		innerSplitPane_purList.setOrientation(JSplitPane.VERTICAL_SPLIT);
		innerSplitPane_purList.setDividerSize(2);
		splitPane_purList.setLeftComponent(innerSplitPane_purList);
				
		// 4. Search condition panel
		pan_srchCond_purList = new JPanel();
		pan_srchCond_purList.setAlignmentY(Component.TOP_ALIGNMENT);
		pan_srchCond_purList.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Conditions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));
		pan_srchCond_purList.setBackground(Color.WHITE);
		innerSplitPane_purList.setLeftComponent(pan_srchCond_purList);
		
		// 5. Components of search condition panel
		final SrchCond_dblCombo srchCond_order_purList = new SrchCond_dblCombo(
				new String[] {"Product Name","Product Type","Purchased Date" },
				new String[] {"thname","thgbn","senddate"});
		
		pan_srchCond_purList.setLayout(new GridLayout(0, 1, 0, 0));
		pan_srchCond_purList.add(srchCond_order_purList);

		// 6. Button menu
		final MenuButtonPane menuBtnPane_purList = new MenuButtonPane(this);
		innerSplitPane_purList.setRightComponent(menuBtnPane_purList);
		
		// 7. Scroll panel for result
		scrollPane_purList = new JScrollPane();
		scrollPane_purList.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane_purList.setViewportBorder(null);
		splitPane_purList.setRightComponent(scrollPane_purList);
		
		// 8. Result panel on scroll panel
		listPanel_purList = new JPanel();
		listPanel_purList.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Result", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		listPanel_purList.setBackground(Color.WHITE);
		scrollPane_purList.setViewportView(listPanel_purList);
		
		// 9. Add search event
		srchCond_order_purList.btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ShowProgressMonitor(Messages.WAIT_PURCHASEDLIST);
				SwingWorker aWorker = new SwingWorker() 
				{
					@Override
					protected Object doInBackground() throws Exception {
						List<Node> list=asf.GetPurchasedList(srchCond_order_purList.GetValue1(),srchCond_order_purList.GetValue2());
						ListInfoPane.DisplayListOnPane(
								list, asf.displayingTags_PurchasedList, asf.map_purchasedListTag, listPanel_purList, menuBtnPane_purList);
						menuBtnPane_purList.selectedPane = null;
						return null;
					}
					
					@Override
					public void done()
					{
						HideProgressMonitor();
					}
				};
				aWorker.execute();
			}
		});
		
		//
		// Detailed info tab
		//
		// 1. Tab pane
		pan_detail = new JPanel();
		pan_detail.setBackground(Color.WHITE);
		tabbedPane.addTab("Detailed", null, pan_detail, null);
		pan_detail.setLayout(new BorderLayout(0, 0));
		
		// 2. Outer split pane
		JSplitPane splitPane_detail = new JSplitPane();
		splitPane_detail.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_detail.setDividerSize(2);
		pan_detail.add(splitPane_detail);
		
		// 3. Button menu
		final MenuButtonPane menuBtnPane_detail = new MenuButtonPane(this);
		splitPane_detail.setLeftComponent(menuBtnPane_detail);
		ListInfoPane.mbpDetail = menuBtnPane_detail;
			
		// 4. Scroll panel for result
		scrollPane_detail = new JScrollPane();
		scrollPane_detail.getVerticalScrollBar().setUnitIncrement(16);
		splitPane_detail.setRightComponent(scrollPane_detail);

		//
		// Configuration Tab
		//
		pan_conf.setSize(300,300);
		tabbedPane.addTab("Config", null, pan_conf, null);
		pan_conf.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane_conf = new JSplitPane();
		splitPane_conf.setBackground(Color.GRAY);
		splitPane_conf.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_conf.setDividerSize(2);
		splitPane_conf.setDividerLocation(68);
		
		// Login Panel
		scrollPane_conf.getVerticalScrollBar().setUnitIncrement(16);
		splitPane_conf.setRightComponent(scrollPane_conf);
		
		listPanel_conf.setBackground(Color.WHITE);
		scrollPane_conf.setViewportView(listPanel_conf);
		listPanel_conf.setLayout(new BoxLayout(listPanel_conf, BoxLayout.Y_AXIS));
		chckbxKeepLoggedIn.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxKeepLoggedIn.setSelected(true);
		chckbxKeepLoggedIn.setBackground(Color.WHITE);
		chckbxKeepLoggedIn.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (chckbxKeepLoggedIn.isSelected())
				{
					asf.prop_conf.setProperty("pkey", asf.pkey);
					asf.SaveSettings();
				}
				else
				{
					asf.prop_conf.setProperty("pkey", "");
					asf.SaveSettings();
				}
			}
		});
		
		
		selConfCategory.comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listPanel_conf.removeAll();
				String selVal=selConfCategory.GetValue1();
				chkBxArr.clear();
				if (selVal.equals("Base Settings"))
				{
					for (int i=0;i<confbaseInfo.length;i++)
					{
						final SrchCond_txtIn txtIn=new SrchCond_txtIn(confbaseInfo[i][1]);
						txtIn.textField.setText(asf.prop_conf.getProperty(confbaseInfo[i][0]));
						
						if (confbaseInfo[i][0].trim().toLowerCase().endsWith("path"))
						{
						JButton btnSelFolder = new JButton("Dir");
						btnSelFolder.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								JFileChooser chooser = new JFileChooser(txtIn.textField.getText());
								chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								chooser.setFileFilter(
										new javax.swing.filechooser.FileNameExtensionFilter("folder", "."));
								int retVal = chooser.showOpenDialog(null);
								if (retVal==JFileChooser.APPROVE_OPTION)
								{
									txtIn.textField.setText(chooser.getSelectedFile().getPath()+"\\");
								}
							}
						});
						btnSelFolder.setFont(new Font("±¼¸²", Font.PLAIN, 10));
						txtIn.add(btnSelFolder);
						}
						listPanel_conf.add(txtIn);
						chkBxArr.add(txtIn);
					}
				}
				else
				{
					HashMap tagMap=null;
					ArrayList<String> tagSet=null;
					
					if (selVal.equals("DisplayingTags_PurchasedList"))
					{
						tagMap=asf.map_purchasedListTag;
						tagSet=asf.displayingTags_PurchasedList;
					}
					else if (selVal.equals("DisplayingTags_SearchedList"))
					{
						tagMap=asf.map_searchedListTag;
						tagSet=asf.displayingTags_SearchedList;
					}
					else if (selVal.equals("DisplayingTags_DetailedInfo"))
					{
						tagMap=asf.map_detailedInfoTag;
						tagSet=asf.displayingTags_DetailedInfo;
					}
					
					
					Iterator e_key=tagMap.keySet().iterator();
			    	while(e_key.hasNext())
			    	{
			    		Object oKey=e_key.next();
			    		JCheckBox chkBx=new JCheckBox((String)tagMap.get(oKey));
			    		chkBx.setBackground(Color.WHITE);
			    		chkBx.setSelected(tagSet.contains(oKey));
			    		listPanel_conf.add(chkBx);
			    		chkBxArr.add(chkBx);
			    	}
				}
		    	listPanel_conf.updateUI();
			}
		});
		
		final JButton btnConfigure = new JButton("Config");
		btnConfigure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selVal=selConfCategory.GetValue1();
				if (selVal.equals("Base Settings"))
				{
					for (int i=0;i<confbaseInfo.length;i++)
					{
						asf.prop_conf.setProperty(confbaseInfo[i][0], ((SrchCond_txtIn)chkBxArr.get(i)).GetValue1());
					}
				}
				else
				{
					HashMap tagMap=null;
					//ArrayList<String> tagSet=null;
					
					if (selVal.equals("DisplayingTags_PurchasedList"))
					{
						tagMap=asf.map_purchasedListTag;
						//tagSet=asf.displayingTags_PurchasedList;
					}
					else if (selVal.equals("DisplayingTags_SearchedList"))
					{
						tagMap=asf.map_searchedListTag;
						//tagSet=asf.displayingTags_SearchedList;
					}
					else if (selVal.equals("DisplayingTags_DetailedInfo"))
					{
						tagMap=asf.map_detailedInfoTag;
						//tagSet=asf.displayingTags_DetailedInfo;
					}
					
					Iterator e_key=tagMap.keySet().iterator();
					int i=0;
					String newDispTag="";
					String comma="";
					//tagSet.clear();
					
			    	while(e_key.hasNext())
			    	{
			    		Object oKey=e_key.next();
			    		JCheckBox chkBx=(JCheckBox) chkBxArr.get(i++);
			    		if (chkBx.isSelected())
			    		{
			    			//tagSet.add((String) oKey);
			    			newDispTag+=comma+oKey;
			    			comma=",";
			    		}
			    	}
			    	asf.prop_conf.setProperty(selVal, newDispTag);
				}
				
		    	asf.SaveSettings();
		    	JOptionPane.showMessageDialog(null, Messages.SUCCESS_CONFIGURATIONCHANGE );
			}
		});
		btnConfigure.setFont(new Font("±¼¸²", Font.PLAIN, 10));
		selConfCategory.add(btnConfigure);
		
		srchCond_btnLogout.btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				asf.pkey="";
				asf.prop_conf.setProperty("pkey", "");
				asf.SaveSettings();

				setLoginPane(false);
			}
		});
		pan_conf_top.setBackground(Color.WHITE);
		pan_conf_top.setLayout(new BoxLayout(pan_conf_top, BoxLayout.Y_AXIS));
		splitPane_conf.setLeftComponent(pan_conf_top);
		
		srchCond_btnLogin.setFont(new Font("±¼¸²", Font.PLAIN, 10));
		srchCond_pwd.add(srchCond_btnLogin);
		srchCond_btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int retLogin = asf.login(srchCond_id.GetValue1(), srchCond_pwd.GetValue1());
				switch(retLogin)
				{
				case 1:	
					setLoginPane(true);
					break;
				case 0:
					JOptionPane.showMessageDialog(pan_conf_top, Messages.FAILED_LOGIN);
					break;
				case -1:
					JOptionPane.showMessageDialog(pan_conf_top, Messages.FAILED_CONNECTION);
					break;
				}
			}
		});
		
		srchCond_btnLogout.add(chckbxKeepLoggedIn);
		FlowLayout flowLayout = (FlowLayout) srchCond_btnLogout.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		
		pan_conf.add(splitPane_conf);
		selConfCategory.comboBox.setSelectedIndex(0);
		
		SwingWorker aWorker = new SwingWorker() 
		{
			@Override
			protected Object doInBackground() throws Exception {
				setLoginPane(!asf.pkey.equals(""));
				return null;
			}
			
			@Override
			public void done()
			{
				//HideProgressMonitor();
			}
		};
		aWorker.execute();
	}
	
	public void setLoginPane(boolean loggedIn)
	{
		pan_conf_top.removeAll();
		if (loggedIn)
		{
			ShowProgressMonitor(Messages.WAIT_LOGIN);
			SwingWorker aWorker = new SwingWorker() 
			{
				@Override
				protected Object doInBackground() throws Exception {
					String userID=asf.prop_conf.getProperty("id");
					frmAppstoreclient.setTitle("Client - "+userID);
					pan_conf_top.add(srchCond_btnLogout);
					pan_conf_top.add(selConfCategory);
					chckbxKeepLoggedIn.setSelected(true);
					
					if (GetCodeList() == -1)
					{
						JOptionPane.showMessageDialog(pan_conf_top, Messages.FAILED_CONNECTION);
						setLoginPane(false);
						return null;
					}
					srchCond_platform_compSrch.SetComboItems(meaningList_PF, codeList_PF);
					srchCond_platform_compSrch.comboBox.setSelectedItem("OPRoS");
					srchCond_lang_compSrch.SetComboItems(meaningList_DL, codeList_DL);
					//srchCond_lang_compSrch.comboBox.setSelectedItem("C++");
					srchCond_platform_appSrch.SetComboItems(meaningList_PF, codeList_PF);
					srchCond_platform_appSrch.comboBox.setSelectedItem("OPRoS");
					srchCond_lang_appSrch.SetComboItems(meaningList_DL, codeList_DL);
					//srchCond_lang_appSrch.comboBox.setSelectedItem("C++");
					return null;
				}
				
				@Override
				public void done()
				{
					HideProgressMonitor();
					pan_conf_top.updateUI();
				}
			};
			aWorker.execute();
		}
		else
		{
			frmAppstoreclient.setTitle("Client");
			pan_conf_top.add(srchCond_id);
			pan_conf_top.add(srchCond_pwd);
			//pan_conf_top.add(srchCond_btnLogin);
			tabbedPane.setSelectedComponent(pan_conf);
			tabbedPane.updateUI();		
		}
		pan_conf_top.updateUI();
	}
	
	int GetCodeList()
	{
		// Get code list
		List<Node> nl_DL=asf.GetCodeList("DL");
		if (nl_DL == null)
			return -1;
		List<Node> nl_PF=asf.GetCodeList("PF");
		if (nl_PF == null)
			return -1;
		
		ArrayList<String> codeList=new ArrayList<String>();
		ArrayList<String> meaningList=new ArrayList<String>();
		codeList.add("all");
		meaningList.add("all");
		for (int i=0;i<nl_DL.size();i++)
		{
			Node node=nl_DL.get(i);
			String code=BaseFunctions.GetTagContentFromNode(node, "ccode");
			String meaning=BaseFunctions.GetTagContentFromNode(node, "cname_kor");
			codeList.add(code);
			meaningList.add(meaning);
		}
		codeList_DL=codeList.toArray(new String[] {""});
		meaningList_DL=meaningList.toArray(new String[] {""});	
		
		codeList.clear();
		meaningList.clear();
		codeList.add("all");
		meaningList.add("all");
		for (int i=0;i<nl_PF.size();i++)
		{
			Node node=nl_PF.get(i);
			String code=BaseFunctions.GetTagContentFromNode(node, "ccode");
			String meaning=BaseFunctions.GetTagContentFromNode(node, "cname_kor");
			codeList.add(code);
			meaningList.add(meaning);
		}
		codeList_PF=codeList.toArray(new String[] {""});
		meaningList_PF=meaningList.toArray(new String[] {""});	
				
		return 0;
	}
	
	public void ShowProgressMonitor(String msg)
	{
		/*pm.setProgress(0);
		pm.setNote(msg);*/
		
		if (PopupFrame.frame == null)
		{
			PopupFrame.Create();
			/*
			//
			// Groove style bgm
			//
	        final int ACOUSTIC_BASS = 35;
	        final int ACOUSTIC_SNARE = 38;
	        final int HAND_CLAP = 39;
	        final int PEDAL_HIHAT = 44;
	        final int LO_TOM = 45;
	        final int CLOSED_HIHAT = 42;
	        final int CRASH_CYMBAL1 = 49;
	        final int HI_TOM = 50;
	        final int RIDE_BELL = 53;
		
	        final int NOTEON = 144;
	        final int NOTEOFF = 128;
	        
			try {
				sequencer = MidiSystem.getSequencer();
				
				seq = new Sequence(Sequence.PPQ, 4);
				Track track = seq.createTrack();
							
				for (int i =0;i<16;i+=4)
				{
					setNote(track, RIDE_BELL, i);
					setNote(track, PEDAL_HIHAT, i);
				}
				
				//setNote(track, HAND_CLAP, 4);
	            //setNote(track, HAND_CLAP, 12);
	            setNote(track, HI_TOM, 13);
	            setNote(track, LO_TOM, 14);
	            int bass3[] = { 0, 3, 6, 9, 15 };
	            for (int i = 0; i < bass3.length; i++) {
	                setNote(track, ACOUSTIC_BASS+1, bass3[i]); 
	            }
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/	
		}
		PopupFrame.frame.ShowWaitMsg(msg);
		/*
		if (dialog==null)
		{
			dialog = new JDialog();
			JLabel label = new JLabel("Please wait...");
			label.setBounds(0, 0, 300, 200);
			dialog.setLocationRelativeTo(frmAppstoreclient);
			dialog.setTitle("Please Wait...");
			dialog.add(label);
			dialog.pack();
		}
		dialog.setVisible(true);*/
		
		/*
		// Bgm play
		try {
			sequencer.open();
			sequencer.setSequence(seq);
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sequencer.start();
		sequencer.setTempoInBPM(100);
		*/
	}
	/*
	void setNote(Track track, int id, int tick)
	{
		try {
			for (int phrase = 0; phrase < 5; phrase++)
			{
				ShortMessage smOn = new ShortMessage();
				smOn.setMessage(144, 9, id, 100);
				MidiEvent noteOn = new MidiEvent(smOn, phrase * 16 + tick);
				track.add(noteOn);
				
				ShortMessage smOff = new ShortMessage();
				smOff.setMessage(128, 9, id, 100);
				MidiEvent noteOff = new MidiEvent(smOff, phrase * 16 + tick+1);
				track.add(noteOff);
			}
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	void HideProgressMonitor()
	{
		//pm.setProgress(100);
		//dialog.setVisible(false);	
		PopupFrame.HideWaitMsg();
		//sequencer.close();
	}
}
