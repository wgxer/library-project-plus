package lpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//import java.util.Scanner;

import javax.swing.JFrame;

import lpp.account.Account;
import lpp.account.Admin;
import lpp.account.Author;
import lpp.account.User;
import lpp.gui.frame.AccountFrame;
import lpp.item.Book;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;



public class Main {
	
	
	public static void writeInput(String fileName, Library library, AccountManager accountManager) {
		ObjectOutputStream libraryOOS = null;
		try {
			File libraryFile = new File(fileName);
			FileOutputStream libraryFOS = new FileOutputStream(libraryFile);
			libraryOOS = new ObjectOutputStream(libraryFOS);
			
			LibraryItem[] itemsToWrite = library.getItems();
			Account[] accountsToWrite = accountManager.getAccounts();
			int itemsCount = library.getItemsCount();
			int accountsCount = accountManager.getAccountsCount();
			
			libraryOOS.writeInt(itemsCount);
			
				for (int i = 0; i<itemsCount; i++) {
					libraryOOS.writeObject(itemsToWrite[i]);
			}
				libraryOOS.writeInt(accountsCount);
				
				for(int i = 0; i<accountsCount; i++) {
					libraryOOS.writeObject(accountsToWrite[i]);
				}
	
		} catch (IOException e) {
			System.out.println(e.getMessage());
		
		} finally {
			
			try {
				libraryOOS.close();
			
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void readInput(String fileName, Library library, AccountManager accountManager) {
		ObjectInputStream libraryOIS = null;
		
		try {
			File libraryFile = new File(fileName);
			FileInputStream libraryFIS = new FileInputStream(libraryFile);
			libraryOIS = new ObjectInputStream(libraryFIS);
			
			int itemsCount = libraryOIS.readInt();
			int i = 0;
			
			while(i < itemsCount) {
			
				try {
				LibraryItem item = (LibraryItem)libraryOIS.readObject();
				library.addItem(item);
				i++;
				
				} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
				continue;
		
				} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println(e.getMessage());
				continue;
			  
				}
			}
			
			int accountsCount = libraryOIS.readInt();
			int j = 0;
			
			while(j < accountsCount) {
				try {
					Account account = (Account)libraryOIS.readObject();
					accountManager.addAccount(account);
					j++;
			
				} catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
					continue;
				
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println(e.getMessage());
					continue;
				  }
				}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
	
		} finally {
			
			try { 
				libraryOIS.close();
			
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	
	public static void main(String[] args) {
		AccountManager accountManager = AccountManager.getInstance();
		Library library = new Library("Library", 1000);
		readInput("library.dat", library, accountManager);

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
