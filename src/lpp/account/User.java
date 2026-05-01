package lpp.account;

import java.util.NoSuchElementException;

import lpp.Displayable;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;
import lpp.item.UnavailableItemException;

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
		
		this.borrowedItems = new LibraryItem[5];
		this.itemsCount = other.itemsCount;
		
		for (int i = 0; i < other.itemsCount; i++) {
			this.borrowedItems[i] = other.borrowedItems[i];
		}
		
		this.borrows = other.borrows;
		this.returns = other.returns;
	}
	
	/* Regular users may only borrow books
	  1 = book borrowed successfully
	 -1 = reached borrow limit
	 -2 = cannot borrow Manuscripts aka "Unpublished books"
	 -3 = book is not available 
	 -4 = insufficient balance */
	public void borrowItem(LibraryItem b) {
		if (itemsCount == 5)
			throw new IndexOutOfBoundsException("You have reached the borrow limit!");
		if(b instanceof Manuscript)
			throw new IllegalArgumentException("Regular users cannot borrow Manuscripts!");
		if(!b.isAvailable())
			throw new UnavailableItemException("Item is not available at the moment!");
		double price = b.calculatePrice();
		
		if (balance < price) {
			throw new InsufficientBalanceException("You do not have enough balance!");
		}
		
		borrowedItems[itemsCount] = b;
		itemsCount++;
		borrows++;
		
		balance -= price;
		fees += price;
		
		Admin.recordBorrow();
		Admin.recordRevenue(price);
		
		b.useItem(this);
		
		
	}
	/* return method using item object as parameter, its arguably easier to do it by index
	  1=  item returned successfully
	 -1 = there are no items in list to return
	 -2 = item is already available
	 -3 = item could not be found*/
	public void returnItem(LibraryItem b) {
		if (b.isAvailable())
			throw new IllegalArgumentException("Item is already available!");
		
		int index=-3;
		for(int i=0; i<itemsCount; i++)  {
			if (b == borrowedItems[i])      // since User/item is an aggregation relationship, passed parameter could have the same reference as one of the entries
			index= i;
		}
		if (index == -3)
			throw new NoSuchElementException("Item could not be found!");
		
		for(int i= index; i<itemsCount-1; i++) {
			borrowedItems[i]= borrowedItems[i+1];
		}
		borrowedItems[itemsCount-1]= null;
		b.returnItem();
		returns++;
		itemsCount--;
		
		Admin.recordReturn();
		
		
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
	
	public void modifyBalance(double value) {
		if (balance + value < 0)
			balance = 0;
		else
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
	
	public int getBorrows() {
		return borrows;
	}
	
	public int getReturns() {
		return returns;
	}
	
	public double getFees() {
		return fees;
	}
}
