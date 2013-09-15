package com.appstore.client.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.w3c.dom.Node;

import com.appstore.client.functions.AppStoreFunctions;

public class MenuButtonPane extends JPanel {

	public ListInfoPane selectedPane = null;
	///public String productCode=null;
	public AppStoreClientUI ascui = null;
	
	/**
	 * Create the panel.
	 */
	public MenuButtonPane(AppStoreClientUI _ascui) {
		ascui = _ascui;
		
		setSize(280, 40);
		setForeground(Color.DARK_GRAY);
		setBackground(Color.WHITE);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		
		// Button Creation
		JButton btnPlay = new JButton("");
		btnPlay.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/play.png")));
		
		JButton btnStop = new JButton("");
		btnStop.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/stop.png")));
		
		JButton btnDownload = new JButton("");	
		btnDownload.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Download.png")));
		
		JButton btnInstall = new JButton("");
		btnInstall.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Install.png")));
		
		JButton btnWebPage = new JButton("");
		btnWebPage.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Web.png")));
		
		JButton btnDelete = new JButton("");
		btnDelete.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Del.png")));
		
		JButton btnUninstall = new JButton("");
		btnUninstall.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/Uninstall.png")));
		
		JButton btnDetail = new JButton("");
		btnDetail.setIcon(new ImageIcon(ListInfoPane.class.getResource("/images/DetailedInfo.png")));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnPlay, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
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
							.addGap(2)
							.addComponent(btnDetail, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))))
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnDetail, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnDownload, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnInstall, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnUninstall, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnWebPage, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnPlay, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))))
					.addGap(5))
		);
		setLayout(groupLayout);
		
		// Button events
		btnWebPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selectedPane == null)
					return;
				
				 java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

			        if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
			            System.err.println( "Desktop doesn't support web browser action" );
			        }
			        try {
			        	String baseWebURL="";
			        	if (selectedPane.productCode.substring(0, 3).equals("SDK"))
			        	{
			        		baseWebURL=ascui.asf.baseURL+"store/component/";
			        	}
			        	else
			        	{
			        		baseWebURL=ascui.asf.baseURL+"store/application/";
			        	}
			            java.net.URI uri = new java.net.URI(baseWebURL+"?productId="+selectedPane.productCode);
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
				
		btnDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selectedPane == null)
					return;
				
				ascui.ShowProgressMonitor(Messages.WAIT_DETAILEDINFO);
				SwingWorker aWorker = new SwingWorker() 
				{
					@Override
					protected Object doInBackground() throws Exception {
						List<Node> list=ascui.asf.GetDetailedInfo(selectedPane.productCode);
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
				if (selectedPane == null)
					return;
				
				ascui.ShowProgressMonitor(Messages.WAIT_DOWNLOADING);
				SwingWorker aWorker = new SwingWorker() 
				{
					boolean ret;
					@Override
					protected Object doInBackground() throws Exception {
						ret = ascui.asf.DownloadProduct(selectedPane.productCode);
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
				if (selectedPane == null)
					return;
				
				if (ascui.asf.DeleteProduct(selectedPane.productCode))
					JOptionPane.showMessageDialog(null, Messages.SUCCESS_DELETING );
			}
		});
				
		btnInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedPane == null)
					return;
				
				ascui.ShowProgressMonitor(Messages.WAIT_INSTALLING);
				SwingWorker aWorker = new SwingWorker() 
				{
					boolean ret;
					@Override
					protected Object doInBackground() throws Exception {
						ret = ascui.asf.InstallProduct(selectedPane.productCode);
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
				if (selectedPane == null)
					return;
				
				if (ascui.asf.UninstallProduct(selectedPane.productCode))
					JOptionPane.showMessageDialog(null, Messages.SUCCESS_UNINSTALLING );
			}
		});

		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selectedPane == null)
					return;
				
				// Run package
				if (!ascui.asf.RunProduct(selectedPane.productCode))
				{
					//JOptionPane.showMessageDialog(null, "Run failed" );
				}
			}
		});
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedPane == null)
					return;
				
				// Stop package
				if (!ascui.asf.StopRunningProduct())
				{
					//JOptionPane.showMessageDialog(null, "Failed to sop" );
				}
			}
		});
	}

}
