package lpp.item;

import java.io.Serializable;

import lpp.Displayable;
import lpp.account.User;

public abstract class LibraryItem implements Displayable, Serializable{
	protected double reviews;
	protected int reviewsCount;
	protected String authorName;
	protected String name;
	protected int pages;
	protected boolean isAvailable;
	protected User usedBy;
	protected int timesUsed;
	protected boolean available;
	
	public LibraryItem(int pages, String name, String authorName) {
		this.pages = pages;
		this.name= name;
		this.authorName= authorName;
		isAvailable= true;
		usedBy= null;
		reviews=0;
		reviewsCount=0;
		timesUsed=0;
	}
	
	public LibraryItem(LibraryItem i) {
		this.pages = i.pages;
		this.name= i.name;
		this.authorName= i.authorName;
		this.isAvailable= i.isAvailable;
		this.usedBy= i.usedBy;
		this.reviews= i.reviews;
		this.reviewsCount= i.reviewsCount;
		this.timesUsed= i.timesUsed;
	}
	
	public abstract LibraryItem copyItem();
	
	public boolean useItem(User u) {
		if (isAvailable) {
		isAvailable= false;
		usedBy= u;
		timesUsed++;
		return true;
		}
		else
			return false;
		
	}
	
	public boolean returnItem() {
		if(isAvailable == false) {
		isAvailable= true;
		usedBy= null;
		return true;
		}
		else
			return false;
	}
	
  // reviews are based on the star system, therefore variable "rating" must be from 1 to 5
	public boolean reviewItem(int rating) {        
		if (rating<1 || rating>5)
			return false;
		else { 	
			reviews= (reviewsCount++)*reviews;
		reviews= (reviews+rating)/reviewsCount;
		return true;
		}
	}
	
	public abstract double calculatePrice();

	public double getReviews() {
		return reviews;
	}

	public int getReviewsCount() {
		return reviewsCount;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getName() {
		return name;
	}

	public int getPages() {
		return pages;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public User getUsedBy() {
		return usedBy;
	}

	public int getTimesUsed() {
		return timesUsed;
	}
	
	public void setAvailable(boolean available) {
	    this.available = available;
	}
	
	
	
	
}
