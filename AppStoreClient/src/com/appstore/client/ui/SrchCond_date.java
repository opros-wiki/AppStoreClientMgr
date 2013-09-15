package com.appstore.client.ui;
import java.awt.*;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.LineBorder;


public class SrchCond_date extends JPanel {
	
	/**
	 * Create the panel.
	 */
	JComboBox cbYear,cbMonth,cbDay;
	public SrchCond_date(String strTitle) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		setBackground(Color.WHITE);
		setSize(210,28);
		
		JLabel lblTitle = new JLabel(strTitle);
		lblTitle.setForeground(Color.BLACK);
		lblTitle.setFont(new Font("±¼¸²", Font.BOLD, 10));
		add(lblTitle);
		
		cbYear = new JComboBox();
		cbYear.setModel(new DefaultComboBoxModel(new String[] {"2010", "2011", "2012", "2013", "2014", "2015"}));
		cbYear.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		add(cbYear);
		
		cbMonth = new JComboBox();
		cbMonth.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
		cbMonth.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		add(cbMonth);
		
		cbDay = new JComboBox();
		cbDay.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		cbDay.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 10));
		add(cbDay);
	}

	public String GetValue1()
	{
		return cbYear.getSelectedItem().toString()+cbMonth.getSelectedItem().toString()+cbDay.getSelectedItem().toString();
	}
	
	public boolean SetToday()
	{
		Calendar date=Calendar.getInstance();
		cbYear.setSelectedItem(""+date.get(Calendar.YEAR));
		cbMonth.setSelectedIndex(date.get(Calendar.MONTH));
		cbDay.setSelectedIndex(date.get(Calendar.DAY_OF_MONTH)-1);
		return true;
	}
}
