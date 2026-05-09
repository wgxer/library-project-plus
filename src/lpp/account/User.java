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
	protected int borrows;
	protected int returns;
	protected double fees;
	
	public User(String username, String password, double balance) {
		super(username, password);
		this.balance = balance;
		
		borrowedItems = new LinkedList<LibraryItem>();
		
		borrows = 0;
		returns = 0;
		fees = 0;
	}
	
	public User(User other) {
		super(other);
		this.balance = other.balance;
		
		this.borrowedItems = new LinkedList<LibraryItem>();
		
		for (int i = 0; i < other.borrowedItems.size(); i++) {
			this.borrowedItems.add(other.borrowedItems.get(i)); 
		}
		
		this.borrows = other.borrows;
		this.returns = other.returns;
	}
	
	// Regular users may only borrow up to 3 items at a time
	public void borrowItem(LibraryItem item) throws UnavailableItemException, InsufficientBalanceException {
		if (borrowedItems.size() == 3)
			throw new IllegalStateException("You have reached the maximum number of items borrowed! Please return an item and try again.");
		if (item.calculatePrice() > balance)
			throw new InsufficientBalanceException("Insufficient balance! Please top up your account and try again.");
		if (!item.isAvailable())
			throw new UnavailableItemException("This item is currently unavailable! Please try again later.");
		
		borrowedItems.add(item);
		item.setAvailable(false);
		modifyBalance(-item.calculatePrice());
		borrows++;
	}
	
	public void returnItem(LibraryItem item) {
		if (item.isAvailable())
			throw new IllegalArgumentException("Item is already available!");
		
		int index = -3;
		
		for(int i = 0; i < borrowedItems.size(); i++)  {
			if (item == borrowedItems.get(i)) index = i;
		}
		
		if (index == -3)
			throw new NoSuchElementException("Item could not be found!");
		
		borrowedItems.remove(index); 
		item.returnItem();
		returns++;
		
		Admin.recordReturn();
	}

	public void display() {
		System.out.println("╭─────────────────────────────────────────────────────────────────╮");
		System.out.println("│                         User Information                        │");
		System.out.println("├─────────────────────────────────────────────────────────────────┤");
		System.out.printf ("│ %-30s : %-30s │\n", "Username", getUsername());
		System.out.printf ("│ %-30s : %-30.2f │\n", "Balance", balance);
		System.out.printf ("│ %-30s : %-30d │\n", "Items currently borrowed", borrowedItems.size());
		System.out.printf ("│ %-30s : %-30d │\n", "Items borrowed", borrows);
		System.out.printf ("│ %-30s : %-30d │\n", "Items returned", returns);
		System.out.printf ("│ %-30s : %-30.2f │\n", "Fees incurred", fees);
		System.out.println("╰─────────────────────────────────────────────────────────────────╯");
	}
	
	public boolean displayItemsList() {
		if(borrowedItems.isEmpty())
			return false;
		for(int i = 1; i <= borrowedItems.size(); i++) {
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