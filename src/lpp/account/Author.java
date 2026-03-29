package lpp.account;

import lpp.item.LibraryItem;
import lpp.item.Manuscript;

public class Author extends User {
	
	private int publishedBooks;
	
	
	public Author(String username, String password, double balance) {
		super(username, password, balance);
		publishedBooks= 0;
	}
	
	public Author(User user) {
		super(user);
		publishedBooks= 0;
	}
	
	// publish a book, and then send request to administator to process
	public void publishBook(int pages, String name) {
     
	}
	/* borrowing for authors is mostly similar to normal users except they can borrow manuscripts
	  1 = Item succefully borrowed
	 -1 = Reached the borrowing limit
	 -2 = Item is not available
	 -3 = insufficient balance */
	@Override
	public int borrowItem(LibraryItem i) {
		if (itemsCount == 5)
			return -1;;
		if(!i.isAvailable())
			return -2;
		double price = i.calculatePrice();
		
		if(balance < price)
			return -3;
		
		borrowedItems[itemsCount] = i;
		itemsCount++;
		sessionBorrows++;
		
		balance -= price;
		sessionFees += price;
		
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
	        System.out.printf ("│ %-30s : %-30d │\n", "Items borrowed during session", sessionBorrows);
	        System.out.printf ("│ %-30s : %-30d │\n", "Items returned during session", sessionReturns);
	        System.out.printf ("│ %-30s : %-30.2f │\n", "Fees incurred during session", sessionFees);
	        System.out.printf ("│ %-30s : %-30d │\n", "Books published", publishedBooks);
	        System.out.println("╰─────────────────────────────────────────────────────────────────╯");
	}
}
