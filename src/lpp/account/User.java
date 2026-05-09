package lpp.account;

import java.util.NoSuchElementException;
import lpp.LinkedList; 

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
		this.balance = balance;
		
		borrowedItems = new LinkedList<LibraryItem>(); 
		itemsCount = 0;
		
		borrows = 0;
		returns = 0;
		fees = 0;
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
	
	// Regular users may only borrow up to 3 items at a time
	public void borrowItem(LibraryItem item) throws UnavailableItemException {
		if (itemsCount == 3)
			throw new IllegalStateException("You have reached the maximum number of items borrowed! Please return an item and try again.");
		if (item.calculatePrice() > balance)
			throw new IllegalStateException("Insufficient balance! Please top up your account and try again.");
		if (!item.isAvailable())
			throw new UnavailableItemException("This item is currently unavailable! Please try again later.");
		
		borrowedItems.add(item);
		item.setAvailable(false);
		modifyBalance(-item.calculatePrice());
		itemsCount++;
		borrows++;
	}
	
	public void returnItem(int index) {
		if (borrowedItems.isEmpty())
			throw new NoSuchElementException("You have no items to return!");
		if (index < 0 || index >= itemsCount)
			throw new IllegalArgumentException("Invalid input!");
		
		borrowedItems.get(index).setAvailable(true);
		borrowedItems.remove(index);
		itemsCount--;
		returns++;
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
		for(int i = 1; i <= itemsCount; i++) {
			System.out.println(i + "- " + borrowedItems.get(i-1).getName()); 
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