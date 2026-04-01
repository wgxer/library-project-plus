package lpp.account;

import lpp.Library;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;

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
		this.balance += manuscript.calculatePrice()/10;
		return true;
     
	}
	/* Borrowing for authors is mostly similar to normal users except they can borrow manuscripts
	  1 = Item succefully borrowed
	 -1 = Reached the borrowing limit
	 -3 = Item is not available
	 -4 = Insufficient balance */
	@Override
	public int borrowItem(LibraryItem i) {
		if (itemsCount == 5)
			return -1;;
		if(!i.isAvailable())
			return -3;
		double price = i.calculatePrice();
		
		if(balance < price)
			return -4;
		
		borrowedItems[itemsCount] = i;
		itemsCount++;
		borrows++;
		
		balance -= price;
		fees += price;
		
		Admin.recordBorrow();
		Admin.recordRevenue(price);
		
		i.useItem(this);
		return 1;
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
