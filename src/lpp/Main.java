package lpp;

import java.util.Scanner;

import javax.swing.JFrame;

import lpp.account.Account;
import lpp.account.Admin;
import lpp.account.Author;
import lpp.account.User;
import lpp.gui.AccountFrame;
import lpp.item.Book;
import lpp.item.Manuscript;

public class Main {
	public static void main(String[] args) {
		AccountManager accountManager = AccountManager.getInstance();
		Library library = new Library("Library", 1000);

		library.addItem(new Book(56, "abcdef", "Unknown", 2016));
		library.addItem(new Book(105, "Ahmed's ABC book", "Ahmed", 2019));
		library.addItem(new Manuscript(5, "My Manuscript", "author", 1));

		accountManager.addAccount(new Admin("admin", "admin"));
		accountManager.addAccount(new User("user", "user123", 10.0));
		accountManager.addAccount(new Author("author", "author123", 30.0));

		JFrame frame = new AccountFrame();
		frame.setVisible(true);

		if (1 == 1) return;
		
		Scanner input = new Scanner(System.in);

		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│                                        │");
		System.out.println("│ Welcome to Library Management System ! │");
		System.out.println("│                                        │");
		System.out.println("╰────────────────────────────────────────╯");

		while (true) {
			Account account = AccountManager.getInstance().getCurrentAccount();

			if (account == null) {
				boolean exit = Menu.showLoginMenu(input);
				
				if (exit) {
					break;
				}
			} else if (account instanceof Admin) {
				Menu.showAdminMenu(input, library, (Admin) account);
			} else if (account instanceof User) {
				Menu.showUserMenu(input, library, (User) account);
			}
		}
	}
}
