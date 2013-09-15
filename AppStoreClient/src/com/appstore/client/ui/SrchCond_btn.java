package com.appstore.client.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class SrchCond_btn extends JPanel {
	public JButton btnSearch;
	public SrchCond_btn(String strTitle) {
		setBackground(Color.WHITE);
		setSize(235,28);
		
		btnSearch = new JButton(strTitle);
		btnSearch.setHorizontalAlignment(SwingConstants.LEFT);
		btnSearch.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		add(btnSearch);
		
	}

}
