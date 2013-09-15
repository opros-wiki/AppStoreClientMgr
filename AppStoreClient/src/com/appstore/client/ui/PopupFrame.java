package com.appstore.client.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingUtilities;

import java.awt.Color;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.Window.Type;

public class PopupFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private JProgressBar progressBar;
	public static PopupFrame frame;
	/**
	 * Launch the application.
	 */
	public static void Create() {
		frame = new PopupFrame();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public PopupFrame() {
		setType(Type.POPUP);
		setResizable(false);
		setAutoRequestFocus(false);
		setAlwaysOnTop(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(PopupFrame.class.getResource("/javax/swing/plaf/metal/icons/Inform.gif")));
		setBounds(100, 100, 170, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textArea = new JTextArea("Please Wait...");
		textArea.setEditable(false);
		textArea.setFont(new Font("MS Gothic", Font.ITALIC, 13));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(textArea)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}

	public void ShowWaitMsg(String msg)
	{
		textArea.setText(msg);
    	setVisible(true);
	}
	
	public static void HideWaitMsg()
	{
		frame.setVisible(false);
	}
}
