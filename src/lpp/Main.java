package lpp;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		AccountManager accountManager = AccountManager.getInstance();
		//accountManager.addAccount(new Admin("admin", "admin"));
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│                                        │");
		System.out.println("│ Welcome to Library Management System ! │");
		System.out.println("│                                        │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();

		while (true) {
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
			break;
		}
	}
}
