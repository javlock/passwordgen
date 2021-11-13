
package com.github.javlock.passwordgen;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordgenMain extends JFrame {
	public enum OPTIONS {
		NOGUI, GUI
	}

	private static final long serialVersionUID = 3439511423843559218L;

	private static final Logger LOGGER = LoggerFactory.getLogger("PasswordgenMain");

	public static final SecureRandom RANDOM = new SecureRandom();
	private static final String NOGUI = "--nogui";

	public static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	public static final String NUMERIC = "0123456789";
	public static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

	private static int lenMin = 10;
	private static int lenMax = 100;

	private static OPTIONS gui = OPTIONS.GUI;
	private static int count = 10;

	// private static final Random r = new Random();

	public static int gen(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		return RANDOM.nextInt((max - min) + 1) + min;
	}

	public static String generatePassword(int len, String dic) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int index = RANDOM.nextInt(dic.length());
			result.append(dic.charAt(index));
		}
		return result.toString();
	}

	public static void main(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase(NOGUI)) {
				gui = OPTIONS.NOGUI;
			}
		}

		if (gui == OPTIONS.NOGUI) {
			for (int i = 0; i < count; i++) {
				int len = gen(lenMin, lenMax);
				String pass = generatePassword(len, ALPHA_CAPS + ALPHA + SPECIAL_CHARS);
				LOGGER.info("[{}]", pass);
			}
		} else if (gui == OPTIONS.GUI) {
			new PasswordgenMain().setVisible(true);
		} else {
			LOGGER.warn("aa");
		}

	}

	private JTextField textField;
	private JTextField tfLen;

	public PasswordgenMain() {
		setResizable(false);
		setSize(447, 162);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.NORTH, textField, 27, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField, 30, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textField, -30, SpringLayout.EAST, getContentPane());
		getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnCopy = new JButton("Copy");
		springLayout.putConstraint(SpringLayout.NORTH, btnCopy, 42, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, btnCopy, 0, SpringLayout.WEST, textField);
		btnCopy.addActionListener(copy -> {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(textField.getText());
			clipboard.setContents(selection, selection);
		});
		getContentPane().add(btnCopy);

		JButton btnGenerate = new JButton("Generate password");
		springLayout.putConstraint(SpringLayout.NORTH, btnGenerate, 42, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, btnGenerate, 27, SpringLayout.EAST, btnCopy);
		springLayout.putConstraint(SpringLayout.SOUTH, btnCopy, 0, SpringLayout.SOUTH, btnGenerate);
		btnGenerate.addActionListener(l -> {
			try {
				int len = gen(lenMin, lenMax);
				String passwd = generatePassword(len, ALPHA_CAPS + ALPHA + SPECIAL_CHARS);
				tfLen.setText(Integer.toString(len));
				textField.setText(passwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		getContentPane().add(btnGenerate);

		tfLen = new JTextField();
		tfLen.setHorizontalAlignment(SwingConstants.CENTER);
		tfLen.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, tfLen, 6, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, tfLen, 0, SpringLayout.WEST, textField);
		getContentPane().add(tfLen);
		tfLen.setColumns(10);
	}
}
