package com.appstore.client.ui;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;


public class SrchCond_txtIn extends JPanel {
	public JTextField textField;

	/**
	 * Create the panel.
	 */
	public SrchCond_txtIn(String strTitle) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		setBackground(Color.WHITE);
		setSize(188,28);
		
		JLabel lblTitle = new JLabel(strTitle);
		lblTitle.setForeground(Color.BLACK);
		lblTitle.setFont(new Font("±¼¸²", Font.BOLD, 10));
		add(lblTitle);
		
		textField = new JTextField();
		textField.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		add(textField);
		textField.setColumns(10);
	}

	public String GetValue1()
	{
		return textField.getText();
	}
}
