package lpp.account;

import java.util.NoSuchElementException;
import java.util.LinkedList; 

import lpp.Displayable;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;
import lpp.item.UnavailableItemException;

public class User extends Account implements Displayable {

	protected double balance;
	protected LinkedList<LibraryItem> borrowedItems; 
	protected int itemsCount;
	protected int borrows;
	protected int returns;
	protected double fees;
	
	public User(String username, String password, double balance) {
		super(username, password);
		this.balance=balance;
		
		borrowedItems = new LinkedList<LibraryItem>(); 
		itemsCount=0;
		
		borrows=0;
		returns=0;
		fees=0;
	}
	
	public User(User other) {
		super(other);
		this.balance = other.balance;
		
		this.borrowedItems = new LinkedList<LibraryItem>();
		this.itemsCount = other.itemsCount;
		
		for (int i = 0; i < other.itemsCount; i++) {
			this.borrowedItems.add(other.borrowedItems.get(i)); 
		}
		
		this.borrows = other.borrows;
		this.returns = other.returns;
	}
	
	// Regular users may only borrow books
	public void borrowItem(LibraryItem b) throws UnavailableItemException, InsufficientBalanceException {
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
		
		borrowedItems.add(b); 
		itemsCount++;
		borrows++;
		
		balance -= price;
		fees += price;
		
		Admin.recordBorrow();
		Admin.recordRevenue(price);
		
		b.useItem(this);
	}

	// return method using item object as parameter, its arguably easier to do it by index
	public void returnItem(LibraryItem b) {
		if (b.isAvailable())
			throw new IllegalArgumentException("Item is already available!");
		
		int index=-3;
		for(int i=0; i<itemsCount; i++)  {
			if (b == borrowedItems.get(i)) 
			index= i;
		}
		if (index == -3)
			throw new NoSuchElementException("Item could not be found!");
		
		borrowedItems.remove(index); 
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
			System.out.println(i+"- "+borrowedItems.get(i-1).getName()); 
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

	public LinkedList<LibraryItem> getBorrowedItems() { 
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