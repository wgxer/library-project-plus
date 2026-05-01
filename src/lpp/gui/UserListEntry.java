package lpp.gui;

import lpp.account.User;

public class UserListEntry {
	private User user;
	
	public UserListEntry(User user) {
		this.user = user;
	}
	
	public String toString() {
		return "   " + user.getUsername();
	}
	
	public User getUser() {
		return user;
	}
}
