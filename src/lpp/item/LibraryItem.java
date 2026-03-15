package lpp.item;

import lpp.Displayable;
import lpp.account.User;

public abstract class LibraryItem implements Displayable{
	protected String name;
	protected int pages;
	protected boolean isAvailable;
	protected User usedBy;
	
	public LibraryItem(int pages) {
		this.pages = pages;
		isAvailable= true;
		usedBy= null;
	}

	public int getPages() {
		return pages;
	}
	
	public boolean isAvailable() {
		return isAvailable;
	}
	
	public void useItem() {
		if (isAvailable)
		isAvailable= false;
		else
		System.out.println(name+ " isn't available!");
	}
	
	public void returnItem() {
		if(isAvailable== false)
		isAvailable= true;
		else
			System.out.print(name+ " is already available!");
	}
	
	public void setUser(User u) {
		usedBy= u;
	}
	
	public User getUser() {
		return usedBy;
	}
	
	public abstract double calculatePrice();
}
