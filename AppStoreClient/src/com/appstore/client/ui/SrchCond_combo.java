package com.appstore.client.ui;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;


public class SrchCond_combo extends JPanel {

	/**
	 * Create the panel.
	 */
	String[] strValues1;
	JComboBox comboBox;
	public SrchCond_combo(String strTitle) {
		init(strTitle);
	}
	
	public SrchCond_combo(String strTitle, String[] strDisp, String[] strVal) {
		init(strTitle);
		SetComboItems(strDisp, strVal);
	}
	
	private void init(String strTitle)
	{
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		/**
		 * Create the panel.
		 */
		setBackground(Color.WHITE);
		setSize(175,28);
		
		JLabel lblTitle = new JLabel(strTitle);
		lblTitle.setForeground(Color.BLACK);
		lblTitle.setFont(new Font("±¼¸²", Font.BOLD, 10));
		add(lblTitle);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		add(comboBox);
	}
	
	public void SetComboItems(String[] strDisp, String[] strVal)
	{
		comboBox.setModel(new DefaultComboBoxModel(strDisp));
		strValues1=strVal;
	}
	
	public String GetValue1()
	{
		return strValues1[comboBox.getSelectedIndex()];
	}

}
