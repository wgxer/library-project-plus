package lpp;

import java.util.Scanner;

import lpp.account.Account;
import lpp.account.Admin;
import lpp.account.User;
import lpp.item.Book;

public class Main {
	public static void main(String[] args) {
		AccountManager accountManager = AccountManager.getInstance();
		
		accountManager.addAccount(new Admin("admin", "admin"));
		accountManager.addAccount(new User("user", "user123", 1.0));
		
//		accountManager.login("admin", "admin");
		
		Scanner input = new Scanner(System.in);
		new Book(3, "ABC", "Ahmed", 2024).display();
		
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│                                        │");
		System.out.println("│ Welcome to Library Management System ! │");
		System.out.println("│                                        │");
		System.out.println("╰────────────────────────────────────────╯");

		while (true) {
			Account account = AccountManager.getInstance().getCurrentAccount();
			
			if (account == null) {
				showLoginMenu(input);
			} else if (account instanceof Admin) {
				showAdminMenu(input, (Admin) account);
			} else if (account instanceof User) {
				System.out.println("! Sorry, users temporarily can't login, please try again later.");
				accountManager.logout();
			}
		}
	}
	
	private static void showLoginMenu(Scanner input) {
		AccountManager accountManager = AccountManager.getInstance();
		
		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ Login:                                 │");
		System.out.println("│  Username: [░░░░░░░░░░░░]              │");
		System.out.println("│  Password: [            ]              │");
		System.out.println("│                                        │");
		System.out.println("│ Don't have an account? Type '!signup'  │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter username (or '!signup'): ");
		
		String loginUsername = input.next();
		
		if (loginUsername.equals("!signup")) {
			showSignupMenu(input);
			return;
		}
		
		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ Login:                                 │");
		System.out.println("│  Username: " + loginUsername.substring(0, Math.min(loginUsername.length(), 27)) + " ".repeat(Math.max(0, 27 - loginUsername.length())) + " │");
		System.out.println("│  Password: [░░░░░░░░░░░░]              │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter password: ");
		
		String password = input.next();
		
		if (!accountManager.login(loginUsername, password)) {
			System.out.println("✘ Incorrect username/password, please try again !");
			System.out.println();
			
			return;
		}
		
		System.out.println("✔ Welcome " + accountManager.getCurrentAccount().getUsername() + " to library !");
	}
	
	private static void showSignupMenu(Scanner input) {
		AccountManager accountManager = AccountManager.getInstance();

		boolean tryAgain = false;
		String username;
		
		do {
			System.out.println();
			System.out.println("╭────────────────────────────────────────────╮");
			
			if (tryAgain) {
				System.out.println("│ ✘ A user with that username already        │");
				System.out.println("│   exists, please choose another username   │");
				System.out.println("│                                            │");
				
				tryAgain = false;
			}
			
			System.out.println("│ Signup:                                    │");
			System.out.println("│  Username: [░░░░░░░░░░░░]                  │");
			System.out.println("│                                            │");
			System.out.println("│ Type '!cancel' to go back                  │");
			System.out.println("╰────────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter username (or '!cancel'): ");
			
			username = input.next();
			
			if (username.equals("!cancel")) {
				return;
			} else if (accountManager.findAccount(username) != null) {
				tryAgain = true;
				continue;
			}
		} while(tryAgain);
		
		String password;
		
		do {
			System.out.println();
			System.out.println("╭────────────────────────────────────────────╮");
			
			if (tryAgain) {
				System.out.println("│ ✘ Password doesn't match with confirm      │");
				System.out.println("│   password, please ensure they're equal    │");
				System.out.println("│                                            │");
				
				tryAgain = false;
			}
			
			System.out.println("│ Signup:                                    │");
			System.out.println("│  Password: [░░░░░░░░░░░░]                  │");
			System.out.println("│  Confirm Password: [            ]          │");
			System.out.println("│                                            │");
			System.out.println("│ Type '!cancel' to go to login menu         │");
			System.out.println("╰────────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter password (or '!cancel'): ");
			
			password = input.next();
			
			if (password.equals("!cancel")) return;
			
			System.out.println();
			System.out.println("╭────────────────────────────────────────────╮");
			System.out.println("│ Signup:                                    │");
			System.out.println("│  Password: [************]                  │");
			System.out.println("│  Confirm Password: [░░░░░░░░░░░░]          │");
			System.out.println("╰────────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter your password again: ");
			
			if (!password.equals(input.next())) {
				tryAgain = true;
			}
		} while (tryAgain);
		
		switch (accountManager.addAccount(new User(username, password, 0))) {
		case 0:
			System.out.println("✔ Welcome " + username + " to library for first time!");
			accountManager.login(username, password);

			break;
		case -2:
			System.out.println("! System cannot handle more accounts, please contact program developers !");
			break;
		default:
			System.out.println("! Something went wrong, please contact program developers !");
			break;
		}
	}
	
	private static void showAdminMenu(Scanner input, Admin admin) {
		AccountManager accountManager = AccountManager.getInstance();

		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ Admin Operations:                      │");
		System.out.println("│  1. Approve Book                       │");
		System.out.println("│  2. Upgrade a user to author           │");
		System.out.println("│  3. Statistics                         │");
		System.out.println("│  4. Logout                             │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter the number of operation: ");
		
		int operation = input.nextInt();
		
		switch (operation) {
		case 1:
			System.out.println("! Sorry, this service is temporarily unavaliable, please try again later.");
			return;
		case 2:
			boolean tryAgain = false;
			
			do {
				System.out.println("╭────────────────────────────────────────────╮");
				
				if (tryAgain) {
					System.out.println("│ ✘ Incorrect username, please try again !   │");
					System.out.println("│                                            │");
					
					tryAgain = false;
				}
				
				System.out.println("│ Upgrade User to Author:                    │");
				System.out.println("│  Username: [░░░░░░░░░░░░]                  │");
				System.out.println("│                                            │");
				
				
				System.out.println("│ Type '!cancel' to go back                  │");
				System.out.println("╰────────────────────────────────────────────╯");
				System.out.println();
				System.out.print("» Enter username (or '!cancel'): ");
				
				String username = input.next();
				
				if (username.equals("!cancel")) {
					break;
				}
				
				switch(admin.upgradeUser(username)) {
				case 0:
					System.out.println("✔ User '" + username + "' has been upgraded successfully to author !");
					break;
				case -1:
					tryAgain = true;
					break;
				case -2:
					System.out.println("✘ Account '" + username + "' is not a user !");
					break;
				case -3:
					System.out.println("✘ Account '" + username + "' is already an author !");
					break;
				default:
					System.out.println("! Something went wrong, please report to program authors !");
					break;
				}
			} while (tryAgain);
			
			break;
		case 3:
			System.out.println("╭────────────────────────────────────────╮");
			System.out.println("│ Statistics:                            │");
			System.out.printf("│  Total Borrows: %-22d │%n", Admin.getTotalBorrows());
			System.out.printf("│  Total Returns: %-22d │%n", Admin.getTotalReturns());
			System.out.printf("│  Total Revenue: %-22.2f │%n", Admin.getTotalRevenue());
			System.out.println("╰────────────────────────────────────────╯");
			
			break;
		case 4:
			System.out.println("- Goodbye, " + admin.getUsername() + " !");
			accountManager.logout();
			
			break;
		default:
			System.out.println("✘ Invalid operation, please try again !");
			break;
		}
	}
}
