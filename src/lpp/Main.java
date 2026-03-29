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
				
				String username = input.next();
				
				if (username.equals("!signup")) {
					System.out.println("! Sorry, signup is temporarily unavaliable, please try again later.");
					// TODO: Implement menu for signup, requires User account to be finished
					return;
				}
				
				System.out.println();
				System.out.println("╭────────────────────────────────────────╮");
				System.out.println("│ Login:                                 │");
				System.out.println("│  Username: " + username.substring(0, Math.min(username.length(), 27)) + " ".repeat(Math.max(0, 27 - username.length())) + " │");
				System.out.println("│  Password: [░░░░░░░░░░░░]              │");
				System.out.println("╰────────────────────────────────────────╯");
				System.out.println();
				System.out.print("» Enter password: ");
				
				String password = input.next();
				
				if (!accountManager.login(username, password)) {
					System.out.println("✘ Incorrect username/password, please try again !");
					System.out.println();
					
					continue;
				}
				
				System.out.println("Welcome " + accountManager.getCurrentAccount().getUsername() + " to library !");
				System.out.println();
			} else if (account instanceof Admin) {
				Admin admin = (Admin) account;
				
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
					break;
				case 2:
					boolean tryAgain = false;
					
					do {
						tryAgain = false;
						
						System.out.println("╭────────────────────────────────────────╮");
						System.out.println("│ Upgrade User to Author:                │");
						System.out.println("│  Username: [░░░░░░░░░░░░]              │");
						System.out.println("│                                        │");
						System.out.println("│ Type '!cancel' to go back              │");
						System.out.println("╰────────────────────────────────────────╯");
						System.out.println();
						System.out.print("» Enter username: ");
						
						String username = input.next();
						
						if (username.equals("!cancel")) {
							break;
						}
						
						switch(admin.upgradeUser(username)) {
						case 0:
							System.out.println("✔ User '" + username + "' has been upgraded successfully to author !");
							break;
						case -1:
							System.out.println("✘ No user with that username, please try again !");
							System.out.println();
							
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
	}
}
