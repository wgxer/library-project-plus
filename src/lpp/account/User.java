package lpp.account;

import lpp.Displayable;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;

public class User extends Account implements Displayable{

	protected double balance;
	protected LibraryItem[] borrowedItems;
	protected int itemsCount;
	protected int sessionBorrows;
	protected int sessionReturns;
	protected double sessionFees;
	
	public User(String username, String password, double balance) {
		super(username, password);
		this.balance=balance;
		borrowedItems= new LibraryItem[5];
		itemsCount=0;
		sessionBorrows=0;
		sessionReturns=0;
		sessionFees=0;
	}

	/* Regular users may only borrow books
	  1 = book borrowed successfully
	 -1 = reached borrow limit
	 -2 = cannot borrow Manuscripts aka "Unpublished books"
	 -3 = book is not available */
	public int borrowItem(LibraryItem b) {
		if (itemsCount == 5)
			return -1;
		if(b instanceof Manuscript)
			return -2;
		if(!b.isAvailable())
			return -3;
		borrowedItems[itemsCount] = b;
		itemsCount++;
		sessionBorrows++;
		balance -= b.calculatePrice();
		sessionFees += b.calculatePrice();
		b.useItem(this);
		return 1;
		
	}
	/* return method using item object as parameter, its arguably easier to do it by index
	  1=  item returned successfully
	 -1 = there are no items in list to return
	 -2 = item is already available
	 -3 = item could not be found*/
	public int returnItem(LibraryItem b) {
		if (itemsCount == 0)
			return -1;
		if (b.isAvailable())
			return -2;
		int index=-3;
		for(int i=0; i<itemsCount; i++)  {
			if (b == borrowedItems[i])      // since User/item is an aggregation relationship, passed parameter could have the same reference as one of the entries
			index= i;
		}
		if (index == -3)
			return -3;
		for(int i= index; i<itemsCount-1; i++) {
			borrowedItems[i]= borrowedItems[i+1];
		}
		borrowedItems[itemsCount-1]= null;
		b.returnItem();
		sessionReturns++;
		itemsCount--;
		return 1;
		
	}
	
	public void display() {
		    
		    System.out.println("╭───────────────────────────────────────────────╮");
		    System.out.println("│                User Information               │");
		    System.out.println("├───────────────────────────────────────────────┤");
	        System.out.printf ("│ %-30s : %-12s │\n", "Username", getUsername());
	        System.out.printf ("│ %-30s : %-12.2f │\n", "Balance", balance);
	        System.out.printf ("│ %-30s : %-12d │\n", "Books currently borrowed", itemsCount);
	        System.out.printf ("│ %-30s : %-12d │\n", "Books borrowed during session", sessionBorrows);
	        System.out.printf ("│ %-30s : %-12d │\n", "Books returned during session", sessionReturns);
	        System.out.printf ("│ %-30s : %-12.2f │\n", "Fees incurred during session", sessionFees);
	        System.out.println("╰───────────────────────────────────────────────╯");
	}
	
	public void reset() {
		sessionBorrows=0;
		sessionReturns=0;
		sessionFees=0;
	}
	
	public double getBalance() {
		return balance;
	}

	public LibraryItem[] getBorrowedItems() {
		return borrowedItems;
	}

	public int getItemsCount() {
		return itemsCount;
	}
	
	
}
