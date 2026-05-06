package lpp.account;

import lpp.Library;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;
import lpp.item.UnavailableItemException;

public class Author extends User {
	
	private int manuscriptsShowcased;
	
	
	public Author(String username, String password, double balance) {
		super(username, password, balance);
		manuscriptsShowcased= 0;
	}
	
	public Author(User user) {
		super(user);
		manuscriptsShowcased= 0;
	}
	
	// Method that makes a Manuscript and submit request for showcasing to admin
	public boolean submitManuscript(int pages, String name, Library library) {
		if (library.getRequestsCount() == 10)
			return false;
		LibraryItem manuscript = new Manuscript(pages, name, this.getUsername(), 1);
		library.addRequest(manuscript);
		return true;
     
	}
	// Borrowing for authors is mostly similar to normal users except they can borrow manuscripts
	@Override
	public void borrowItem(LibraryItem i) throws UnavailableItemException, InsufficientBalanceException  {
		if (itemsCount == 5)
			throw new IndexOutOfBoundsException("You have reached the borrow limit!");
		if(!i.isAvailable())
			throw new UnavailableItemException("Item is not available at the moment!");
		double price = i.calculatePrice();
		if(!(i.getAuthorName() == this.getUsername())) {
		if(balance < price)
			throw new InsufficientBalanceException("You do not have enough balance!");
		}
		borrowedItems[itemsCount] = i;
		itemsCount++;
		borrows++;
		if(!(i.getAuthorName() == this.getUsername())) {
		balance -= price;
		fees += price;
		Admin.recordRevenue(price);
	}
		Admin.recordBorrow();
		
		i.useItem(this);
	}
	@Override
	public void display() {
		    System.out.println("╭─────────────────────────────────────────────────────────────────╮");
		    System.out.println("│                       Author Information                        │");
		    System.out.println("├─────────────────────────────────────────────────────────────────┤");
	        System.out.printf ("│ %-30s : %-30s │\n", "Username", getUsername());
	        System.out.printf ("│ %-30s : %-30.2f │\n", "Balance", balance);
	        System.out.printf ("│ %-30s : %-30d │\n", "Items currently borrowed", itemsCount);
	        System.out.printf ("│ %-30s : %-30d │\n", "Items borrowed", borrows);
	        System.out.printf ("│ %-30s : %-30d │\n", "Items returned", returns);
	        System.out.printf ("│ %-30s : %-30.2f │\n", "Fees incurred", fees);
	        System.out.printf ("│ %-30s : %-30d │\n", "Manuscripts showcased", manuscriptsShowcased);
	        System.out.println("╰─────────────────────────────────────────────────────────────────╯");
	}
	
	public void showcaseManuscript() {
		manuscriptsShowcased++;
	}
	
	public int getManuscriptsShowcased() {
		return manuscriptsShowcased;
	}
}
