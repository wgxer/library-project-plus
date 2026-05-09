package lpp;

import java.io.FileNotFoundException;

//import java.util.Scanner;

import javax.swing.JFrame;

import lpp.account.Admin;
import lpp.account.Author;
import lpp.account.User;
import lpp.gui.frame.AccountFrame;
import lpp.item.Book;
import lpp.item.Manuscript;

public class Main {
	public static void main(String[] args) {
		AccountManager accountManager = AccountManager.getInstance();
		Library library = new Library("Library");
		
		try {
			FileManager.readLibrary("library.dat", library, accountManager);
		} catch(FileNotFoundException e) {
			library.addItem(new Book(56, "abcdef", "Unknown", 2016));
			library.addItem(new Book(105, "Ahmed's ABC book", "Ahmed", 2019));
			library.addItem(new Manuscript(5, "My Manuscript", "author", 1));
			
			User user = new User("user", "user123", 100.0);
			
			try {
				user.borrowItem(library.getItems().get(0));
				user.borrowItem(library.getItems().get(1));
			} catch(Exception e2) {
				// No need to handle this, since it never throws because user has enough balance
			}
			
			accountManager.addAccount(new Admin("admin", "admin"));
			accountManager.addAccount(user);
			accountManager.addAccount(new Author("author", "author123", 30.0));
		}

		JFrame frame = new AccountFrame(library);
		frame.setVisible(true);
	}
}
