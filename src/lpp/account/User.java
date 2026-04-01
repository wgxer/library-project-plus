package lpp.account;

import lpp.Displayable;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;

public class User extends Account implements Displayable{

	protected double balance;
	protected LibraryItem[] borrowedItems;
	protected int itemsCount;
	protected int borrows;
	protected int returns;
	protected double fees;
	
	public User(String username, String password, double balance) {
		super(username, password);
		this.balance=balance;
		
		borrowedItems= new LibraryItem[5];
		itemsCount=0;
		
		borrows=0;
		returns=0;
		fees=0;
	}
	
	public User(User other) {
		super(other);
		this.balance = other.balance;
		
		this.borrowedItems = other.borrowedItems;
		this.itemsCount = other.itemsCount;
		
		this.borrows = other.borrows;
		this.returns = other.returns;
	}
	
	/* Regular users may only borrow books
	  1 = book borrowed successfully
	 -1 = reached borrow limit
	 -2 = cannot borrow Manuscripts aka "Unpublished books"
	 -3 = book is not available 
	 -4 = insufficient balance */
	public int borrowItem(LibraryItem b) {
		if (itemsCount == 5)
			return -1;
		if(b instanceof Manuscript)
			return -2;
		if(!b.isAvailable())
			return -3;
		
		double price = b.calculatePrice();
		
		if (balance < price) {
			return -4;
		}
		
		borrowedItems[itemsCount] = b;
		itemsCount++;
		borrows++;
		
		balance -= price;
		fees += price;
		
		Admin.recordBorrow();
		Admin.recordRevenue(price);
		
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
		returns++;
		itemsCount--;
		
		Admin.recordReturn();
		
		return 1;
		
	}
	
	public void display() {
		    
		    System.out.println("╭─────────────────────────────────────────────────────────────────╮");
		    System.out.println("│                         User Information                        │");
		    System.out.println("├─────────────────────────────────────────────────────────────────┤");
	        System.out.printf ("│ %-30s : %-30s │\n", "Username", getUsername());
	        System.out.printf ("│ %-30s : %-30.2f │\n", "Balance", balance);
	        System.out.printf ("│ %-30s : %-30d │\n", "Items currently borrowed", itemsCount);
	        System.out.printf ("│ %-30s : %-30d │\n", "Items borrowed", borrows);
	        System.out.printf ("│ %-30s : %-30d │\n", "Items returned", returns);
	        System.out.printf ("│ %-30s : %-30.2f │\n", "Fees incurred", fees);
	        System.out.println("╰─────────────────────────────────────────────────────────────────╯");
	}
	
	public boolean displayItemsList() {
		if(itemsCount == 0)
			return false;
		for(int i=1; i<=itemsCount; i++) {
			System.out.println(i+"- "+borrowedItems[i-1].getName());
		}
		return true;
			
	}
	
	public void modifyBalance(Double value) {
		balance += value;
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
