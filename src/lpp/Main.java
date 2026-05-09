package lpp;

//import java.util.Scanner;

import javax.swing.JFrame;

import lpp.account.Admin;
import lpp.account.User;
import lpp.gui.frame.AccountFrame;

public class Main {
	public static void main(String[] args) {
		AccountManager accountManager = AccountManager.getInstance();
		Library library = new Library("Library", 1000);
		
		FileManager.readLibrary("library.dat", library, accountManager);
		accountManager.addAccount(new Admin("admin", "admin")); // If admin account doesn't exist
		
		User user = new User("User created at compile time", "new", 10000);
		accountManager.addAccount(user);
		
		
		try {
			user.borrowItem(library.getItems()[0]);
			user.borrowItem(library.getItems()[1]);
		} catch(Exception e) {
			// Code is fine, this shouldn't happen
		}
		


		JFrame frame = new AccountFrame(library);
		frame.setVisible(true);
		
		// Menu code
//		Scanner input = new Scanner(System.in);
//
//		System.out.println("╭────────────────────────────────────────╮");
//		System.out.println("│                                        │");
//		System.out.println("│ Welcome to Library Management System ! │");
//		System.out.println("│                                        │");
//		System.out.println("╰────────────────────────────────────────╯");
//
//		while (true) {
//			Account account = AccountManager.getInstance().getCurrentAccount();
//
//			if (account == null) {
//				boolean exit = Menu.showLoginMenu(input);
//				
//				if (exit) {
//					break;
//				}
//			} else if (account instanceof Admin) {
//				Menu.showAdminMenu(input, library, (Admin) account);
//			} else if (account instanceof User) {
//				Menu.showUserMenu(input, library, (User) account);
//			}
//		}
	}
}
