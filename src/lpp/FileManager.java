package lpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lpp.account.Account;
import lpp.account.Admin;
import lpp.item.LibraryItem;

public class FileManager {
	public static void writeLibrary(String fileName, Library library, AccountManager accountManager) {
		ObjectOutputStream libraryOOS = null;

		try {
			File libraryFile = new File(fileName);
			FileOutputStream libraryFOS = new FileOutputStream(libraryFile);
			libraryOOS = new ObjectOutputStream(libraryFOS);

			LinkedList<LibraryItem> itemsToWrite = library.getItems();
			LinkedList<LibraryItem> requestsToWrite = library.getRequests();
			LinkedList<Account> accountsToWrite = accountManager.getAccounts();
			
			libraryOOS.writeInt(Admin.getTotalBorrows());
			libraryOOS.writeInt(Admin.getTotalReturns());
			libraryOOS.writeDouble(Admin.getTotalRevenue());
			
			libraryOOS.writeObject(itemsToWrite);
			libraryOOS.writeObject(requestsToWrite);
			libraryOOS.writeObject(accountsToWrite);
		} catch (IOException e) {
			System.out.println(e.getMessage());

		} finally {

			try {
				if (libraryOOS != null) libraryOOS.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static void readLibrary(String fileName, Library library, AccountManager accountManager) throws FileNotFoundException {
		ObjectInputStream libraryOIS = null;
		
		library.clear();
		AccountManager.getInstance().clear();

		try {
			File libraryFile = new File(fileName);
			FileInputStream libraryFIS = new FileInputStream(libraryFile);
			libraryOIS = new ObjectInputStream(libraryFIS);

			Admin.setTotalBorrows(libraryOIS.readInt());
			Admin.setTotalReturns(libraryOIS.readInt());
			Admin.setTotalRevenue(libraryOIS.readDouble());
			
			LinkedList<LibraryItem> libraryItems = (LinkedList<LibraryItem>) readObject(libraryOIS);
			LinkedList<LibraryItem> libraryRequests = (LinkedList<LibraryItem>) readObject(libraryOIS);
			LinkedList<Account> accounts = (LinkedList<Account>) readObject(libraryOIS);
			
			if (libraryItems != null && libraryRequests != null && accounts != null) {
				for (int i = 0; i < libraryItems.size(); i++) {
					library.addItem(libraryItems.get(i));
				}
				
				for (int i = 0; i < libraryRequests.size(); i++) {
					library.addRequest(libraryRequests.get(i));
				}
				
				for (int i = 0; i < accounts.size(); i++) {
					accountManager.addAccount(accounts.get(i));
				}
			}
		} catch(FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				if (libraryOIS != null) libraryOIS.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	private static Object readObject(ObjectInputStream ois) throws IOException {
		try {
			return ois.readObject();

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return null; // We can't read stream anymore
		}
	}
}
