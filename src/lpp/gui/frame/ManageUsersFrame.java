package lpp.gui.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lpp.AccountManager;
import lpp.LinkedList;
import lpp.account.Admin;
import lpp.account.Author;
import lpp.account.User;
import lpp.gui.GUIUtils;
import lpp.gui.IUpdateable;
import lpp.gui.UserListEntry;
import lpp.gui.ViewFactory;
import lpp.gui.handler.ShowParentCloseHandler;

public class ManageUsersFrame extends JFrame implements IUpdateable, ActionListener, ListSelectionListener {
	private Admin admin;
	
	private JTextField searchField;
	private Container userContainer;
	private JScrollPane usersScrollPane;
	private JList<UserListEntry> usersList;
	
	private User selectedUser;
	private UserListEntry[] userListEntries;
	
	private String currentSearchEntry;

	public ManageUsersFrame(Frame parent, Admin admin) {
		this.admin = admin;
		currentSearchEntry = "";

		setTitle("Library Management System - Manage Users");
		
		if (parent != null) {
			addWindowListener(new ShowParentCloseHandler(parent));
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		} else {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		
		refreshUsers();
		Container contentPane = getContentPane();

		usersScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		usersScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(this);
	
		searchField = GUIUtils.textField();
		searchField.setMaximumSize(new Dimension(searchField.getMaximumSize().width, searchButton.getMaximumSize().height));
		
		userContainer = ViewFactory.vertical().build();
		
		contentPane.add(
			ViewFactory.vertical().gap(10).padding(10).fillWidth().fillHeight().build(
				userContainer,
				ViewFactory.horizontal().alignY(Component.CENTER_ALIGNMENT).gap(10).build(
					new JLabel("Search: "),
					searchField,
					searchButton
				),
				usersScrollPane
			)
		);

		setSize(800, 600);
		update();
	}

	public void update() {
		Container contentPane = getContentPane();
		
		int previousSelectedIndex = -1;
		int previousScrollY = 0;
		
		if (usersList != null) {
			previousSelectedIndex = usersList.getSelectedIndex();
			previousScrollY = usersScrollPane.getVerticalScrollBar().getValue();
		}
		
		Container buttonsContainer = null;
		
		if (selectedUser != null) {
			JButton upgradeToAuthorButton = new JButton("Upgrade to Author");
			upgradeToAuthorButton.addActionListener(this);
			
			Dimension buttonsPreferredSize = upgradeToAuthorButton.getPreferredSize();
			
			if (selectedUser instanceof Author) {
				upgradeToAuthorButton = null;
			}
			
			JButton modifyBalanceButton = new JButton("Modify Balance");
			modifyBalanceButton.addActionListener(this);
			
			modifyBalanceButton.setPreferredSize(buttonsPreferredSize);
			modifyBalanceButton.setMaximumSize(buttonsPreferredSize);
			
			JButton changePasswordButton = new JButton("Change Password");
			changePasswordButton.addActionListener(this);
			
			changePasswordButton.setPreferredSize(buttonsPreferredSize);
			changePasswordButton.setMaximumSize(buttonsPreferredSize);
			
			JButton deleteUserButton = new JButton("Delete");
			deleteUserButton.addActionListener(this);
			
			deleteUserButton.setPreferredSize(buttonsPreferredSize);
			deleteUserButton.setMaximumSize(buttonsPreferredSize);
			
			buttonsContainer = ViewFactory.vertical().gap(10).build(
				upgradeToAuthorButton,
				modifyBalanceButton,
				changePasswordButton,
				deleteUserButton
			);
		}
		
		userContainer.removeAll();
		userContainer.add(GUIUtils.displayUser(selectedUser, buttonsContainer, contentPane, this));
		
		usersList = new JList<UserListEntry>(userListEntries);
		usersList.setSelectedIndex(previousSelectedIndex);
		usersList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		usersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		usersList.addListSelectionListener(this);
		
		usersScrollPane.setViewportView(usersList);
		usersScrollPane.getVerticalScrollBar().setValue(previousScrollY);
		
		revalidate();
		repaint();
	}
	
	private void refreshUsers() {
		AccountManager accountManager = AccountManager.getInstance();
		
		LinkedList<User> users = accountManager.searchUsers(currentSearchEntry);
		userListEntries = new UserListEntry[users.size()];
		
		for (int i = 0; i < userListEntries.length; i++) {
			userListEntries[i] = new UserListEntry(users.get(i));
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Container contentPane = getContentPane();
		String action = e.getActionCommand();
		
		AccountManager accountManager = AccountManager.getInstance();
		
		if (action.equals("Search")) {
			currentSearchEntry = searchField.getText();
			
			refreshUsers();
			update();
		} else if(action.equals("Upgrade to Author")) { 
			Author author = new Author(selectedUser);
			
			try {
				admin.upgradeUser(selectedUser.getUsername());
				selectedUser = author;
				
				refreshUsers();
				update();

			} catch (NoSuchElementException exception) { 
				JOptionPane.showMessageDialog(contentPane, "User not found.", "Upgrade to Author failed !", JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(contentPane, "Account is not a user.", "Upgrade to Author failed !", JOptionPane.ERROR_MESSAGE);
			}
			
			if (accountManager.updateAccount(author)) {
				selectedUser = author;
				
				refreshUsers();
				update();
			} else {
				JOptionPane.showMessageDialog(contentPane, "User not found.", "User Deletion failed !", JOptionPane.ERROR_MESSAGE);
			}
		} else if (action.equals("Change Password")) {
			String newPassword = JOptionPane.showInputDialog(contentPane, "Enter new password for user", "Change User Password", JOptionPane.QUESTION_MESSAGE);
			if (newPassword == null) return;
			
			if (newPassword.isBlank()) {
				JOptionPane.showMessageDialog(
					contentPane, 
					"No password was given, please enter a password.", 
					"Changing user password failed !", 
					JOptionPane.ERROR_MESSAGE
				);

				return;
			}
			
			selectedUser.setPassword(newPassword);
			JOptionPane.showMessageDialog(contentPane, "User password has been changed successfully.", "Change User Password", JOptionPane.INFORMATION_MESSAGE);
		} else if (action.equals("Modify Balance")) {
			int amount = GUIUtils.showNumberDialog(contentPane, "Modify User Balance", String.format("User Balance: %.2f $\nEnter the amount to modify user balance with:", selectedUser.getBalance()), "Invalid amount.", false);
			if (amount == -1) return;
			
			selectedUser.modifyBalance(amount);
			JOptionPane.showMessageDialog(contentPane, "Balance has been modified successfully.", "Modify User Balance", JOptionPane.INFORMATION_MESSAGE);
			
			refreshUsers();
			update();
		} else if(action.equals("Delete")) {
			int selectedOption = JOptionPane.showConfirmDialog(
				contentPane, 
				"Are you sure you want to delete this user?\n"
					+ " Username: " + selectedUser.getUsername(),
				"Delete User Confirmation",
				JOptionPane.YES_NO_OPTION
			);
			
			if (selectedOption == JOptionPane.YES_OPTION) {
				if (accountManager.deleteAccount(selectedUser.getUsername())) {
					selectedUser = null;
					usersList.setSelectedIndices(new int[] {});

					refreshUsers();
					update();
				} else {
					JOptionPane.showMessageDialog(contentPane, "User not found.", "User Deletion failed !", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		int index = usersList.getSelectedIndex();
		
		if (index != -1) {
			selectedUser = userListEntries[index].getUser();
		} else {
			selectedUser = null;
		}
		
		update();
	}
}
