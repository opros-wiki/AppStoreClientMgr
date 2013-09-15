package com.appstore.client.ui;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;


public class SrchCond_pwdIn extends JPanel {
	private JPasswordField textField;
	/**
	 * Create the panel.
	 */
	public SrchCond_pwdIn(String strTitle) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		setBackground(Color.WHITE);
		setSize(188,28);
		
		JLabel lblTitle = new JLabel(strTitle);
		lblTitle.setForeground(Color.BLACK);
		lblTitle.setFont(new Font("±¼¸²", Font.BOLD, 10));
		add(lblTitle);
		
		textField = new JPasswordField();
		textField.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		add(textField);
		textField.setColumns(10);
	}

	public String GetValue1()
	{
		String ret=new String(textField.getPassword());
		textField.setText("");
		return ret;
	}
}
