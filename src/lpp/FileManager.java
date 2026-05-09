package lpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lpp.account.Account;
import lpp.item.LibraryItem;

public class FileManager {
	public static void writeLibrary(String fileName, Library library, AccountManager accountManager) {
		ObjectOutputStream libraryOOS = null;

		try {
			File libraryFile = new File(fileName);
			FileOutputStream libraryFOS = new FileOutputStream(libraryFile);
			libraryOOS = new ObjectOutputStream(libraryFOS);

			LinkedList<LibraryItem> itemsToWrite = library.getItems();
			LinkedList<Account> accountsToWrite = accountManager.getAccounts();
			
			int itemsCount = itemsToWrite.size();
			int accountsCount = accountsToWrite.size();

			libraryOOS.writeInt(itemsCount);

			for (int i = 0; i < itemsCount; i++) {
				libraryOOS.writeObject(itemsToWrite.get(i));
			}
			libraryOOS.writeInt(accountsCount);

			for (int i = 0; i < accountsCount; i++) {
				libraryOOS.writeObject(accountsToWrite.get(i));
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

	public static void readLibrary(String fileName, Library library, AccountManager accountManager) {
		ObjectInputStream libraryOIS = null;

		try {
			File libraryFile = new File(fileName);
			FileInputStream libraryFIS = new FileInputStream(libraryFile);
			libraryOIS = new ObjectInputStream(libraryFIS);

			int itemsCount = libraryOIS.readInt();
			int i = 0;

			while (i < itemsCount) {

				try {
					LibraryItem item = (LibraryItem) libraryOIS.readObject();
					library.addItem(item);
					i++;

				} catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
					continue; // We can't read stream anymore

				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println(e.getMessage());
					continue;

				}
			}

			int accountsCount = libraryOIS.readInt();
			int j = 0;

			while (j < accountsCount) {
				try {
					Account account = (Account) libraryOIS.readObject();
					accountManager.addAccount(account);
					j++;

				} catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
					break; // We can't read stream anymore

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
}
