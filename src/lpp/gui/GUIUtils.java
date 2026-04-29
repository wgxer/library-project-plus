package lpp.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIUtils {
	public static JTextField textField(String defaultValue) {
		JTextField textField = new JTextField(defaultValue);
		textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
		
		return textField;
	}

	public static JButton buttonAsLink(String text) {
		JButton button = new JButton(text);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setForeground(Color.BLUE);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setBorder(BorderFactory.createEmptyBorder());
		
		return button;
	}
	
	public static JPanel fieldWithLabel(String fieldName, String defaultValue) {
		JPanel fieldPanel = new JPanel();

		JLabel fieldLabel = new JLabel(fieldName);
		JTextField textField = new JTextField(defaultValue);

		textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));

		fieldPanel.add(fieldLabel);
		fieldPanel.add(textField);

		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.X_AXIS));
		fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		return fieldPanel;
	}
}
