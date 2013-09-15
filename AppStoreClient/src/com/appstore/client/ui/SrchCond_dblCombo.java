package com.appstore.client.ui;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JToggleButton;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;


public class SrchCond_dblCombo extends JPanel {

	/**
	 * Create the panel.
	 */
	String[] strValues1;
	String[] strValues2;
	public JComboBox comboBox;
	private JComboBox comboBox_1;
	public JButton btnSearch;
	public SrchCond_dblCombo(String[] strDisp, String[] strVal) {
		setBackground(Color.WHITE);
		setBorder(null);
		setSize(235,28);
		
		JLabel lblOrder = new JLabel("Order");
		lblOrder.setForeground(Color.BLACK);
		lblOrder.setFont(new Font("±¼¸²", Font.BOLD, 10));
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		comboBox.setModel(new DefaultComboBoxModel(strDisp));
		
		comboBox_1 = new JComboBox();
		comboBox_1.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Asc", "Desc"}));
		
		ImageIcon srchIcon = new ImageIcon(SrchCond_dblCombo.class.getResource("/images/Search.png"));
		btnSearch = new JButton(srchIcon);
		btnSearch.setBackground(Color.WHITE);
		btnSearch.setSize(30, 23);
		//btnSearch.setIcon(new ImageIcon(SrchCond_dblCombo.class.getResource("/images/Search.png")));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(lblOrder)
					.addGap(5)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(8)
							.addComponent(lblOrder))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 23, 23)))
					.addContainerGap())
		);
		setLayout(groupLayout);
		
		strValues1=strVal;
		strValues2=new String[] {"asc", "desc"};
	}
	
	public String GetValue1()
	{
		return strValues1[comboBox.getSelectedIndex()];
	}
	
	public String GetValue2()
	{
		return strValues2[comboBox_1.getSelectedIndex()];
	}
}
