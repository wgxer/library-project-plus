package lpp.gui.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import lpp.AccountManager;
import lpp.Library;
import lpp.account.Admin;
import lpp.account.User;
import lpp.gui.GUIUtils;
import lpp.gui.ViewFactory;

public class AccountFrame extends JFrame implements ActionListener {
	private JLabel topLabel;
	
	private JTextField usernameField;
	private JTextField passwordField;
	
	private JTextField confirmPasswordField;
	private JPanel confirmPasswordContainer;
	
	private JLabel switchLabel;
	private JButton switchButton;
	private JButton actionButton;
	
	private Library library;
	private boolean signupMode;
	
	public AccountFrame(Library library) {
		this.library = library;
		signupMode = false;
		
		setTitle("Library Management System - Login");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		setSize(500, 300);
		
		Container contentPane = getContentPane();
		topLabel = new JLabel("Login:");
		
		JLabel usernameLabel = new JLabel("Username: ");
		usernameField = GUIUtils.textField(null);
		
		JLabel passwordLabel = new JLabel("Password: ");
		passwordField = GUIUtils.textField(null);
		
		JLabel confirmPasswordLabel = new JLabel("Confirm Password: ");
		confirmPasswordField = GUIUtils.textField(null);
		
		confirmPasswordContainer = ViewFactory.horizontal().gap(12).build(confirmPasswordLabel, confirmPasswordField);
		confirmPasswordContainer.setVisible(false);
		
		actionButton = new JButton("Login");
		actionButton.addActionListener(this);

		switchLabel = new JLabel("Don't have an account?");
		
		switchButton = GUIUtils.buttonAsLink("Signup");
		switchButton.addActionListener(this);

		contentPane.add(
			ViewFactory.vertical().padding(10).gap(10).build(
				topLabel,
				ViewFactory.vertical().padding(10, 0).gap(10).build(
					ViewFactory.horizontal().gap(12).build(usernameLabel, usernameField),
					ViewFactory.horizontal().gap(16).build(passwordLabel, passwordField),
					confirmPasswordContainer
				),
				ViewFactory.vertical().fillWidth().alignX(Component.CENTER_ALIGNMENT).build(actionButton),
				ViewFactory.horizontal().gap(12).build(switchLabel, switchButton)
			)
		);
	}
	
	public void actionPerformed(ActionEvent e) {
		AccountManager accountManager = AccountManager.getInstance();
		Container contentPane = getContentPane();
		
		if (e.getSource() instanceof JButton) {
			String buttonText = e.getActionCommand();
			String username = usernameField.getText();
			String password = passwordField.getText();
			
			if (!signupMode) {				
				if (buttonText.equals("Login")) {	
					if (accountManager.login(username, password)) {
						usernameField.setText("");
						passwordField.setText("");
						
						if (accountManager.getCurrentAccount() instanceof User) {
							setVisible(false);
							
							UserFrame userFrame = new UserFrame(this, library, (User) accountManager.getCurrentAccount());
							userFrame.setVisible(true);
						} else if (accountManager.getCurrentAccount() instanceof Admin) {
							setVisible(false);
							
							AdminFrame adminFrame = new AdminFrame(this, library, (Admin) accountManager.getCurrentAccount());
							adminFrame.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(contentPane, "Login was succesful !");
						}
					} else {
						JOptionPane.showMessageDialog(contentPane, "Incorrect username/password, please try again !", "Login failed !", JOptionPane.ERROR_MESSAGE);
					}
				} else if (buttonText.equals("Signup")) {
					setTitle("Library Management System - Signup");
					topLabel.setText("Signup:");
					
					signupMode = true;
					confirmPasswordContainer.setVisible(true);
					
					actionButton.setText("Signup");

					switchLabel.setText("Already have an account?");
					switchButton.setText("Login");
				}
			} else {
				 if (buttonText.equals("Signup")) {
					String confirmPassword = confirmPasswordField.getText();
					
					if (username.isBlank()) {
						JOptionPane.showMessageDialog(
							contentPane, 
							"No username was given, please enter a username.", 
							"Signup failed !", 
							JOptionPane.ERROR_MESSAGE
						);

						return;
					}
					
					if (password.isBlank()) {
						JOptionPane.showMessageDialog(
							contentPane, 
							"No password was given, please enter a password.", 
							"Signup failed !", 
							JOptionPane.ERROR_MESSAGE
						);

						return;
					}
					
					if (!password.equals(confirmPassword)) {
						JOptionPane.showMessageDialog(
							contentPane, 
							"Password doesn't match with confirm password, please ensure they're equal.", 
							"Signup failed !", 
							JOptionPane.ERROR_MESSAGE
						);

						return;
					}
					
					try {
						accountManager.addAccount(new User(username, password, 0.0));
						
						usernameField.setText("");
						passwordField.setText("");

						setVisible(false);
						
						accountManager.login(username, password);
						
						UserFrame userFrame = new UserFrame(this, library, (User) accountManager.getCurrentAccount());
						userFrame.setVisible(true);
					} catch(IllegalArgumentException exception) {
						JOptionPane.showMessageDialog(
							contentPane, 
							"A user with that username already exists, please choose another username.", 
							"Signup failed !", 
							JOptionPane.ERROR_MESSAGE
						);
					}
				} else if (buttonText.equals("Login")) {
					setTitle("Library Management System - Login");
					topLabel.setText("Login:");
					
					signupMode = false;
					confirmPasswordContainer.setVisible(false);
					
					actionButton.setText("Login");

					switchLabel.setText("Don't have an account?");
					switchButton.setText("Signup");
				}
			}
		}
	}
}
