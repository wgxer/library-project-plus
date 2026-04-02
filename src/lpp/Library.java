package lpp;

import lpp.item.LibraryItem;
import lpp.AccountManager;
import lpp.account.Author;

public class Library {
	
	private String name;
	private LibraryItem[] items;
	private int itemsCount;
	private LibraryItem[] requests;
	private int requestsCount;
	
	public Library(String name, int capacity) {
		this.name = name;
		items = new LibraryItem[capacity];
		itemsCount = 0;
		requests = new LibraryItem[10];
		requestsCount = 0;
		
	}
	
	public boolean addItem(LibraryItem i) {
		if(itemsCount == items.length)
			return false;
		items[itemsCount++]= i.copyItem();
		return true;
	}
	/* Removes an item from list based on passed index that is from 1 to count
	   1 = Item removed successfully
	  -1 = No items to remove
	  -2 = Invalid input
	  -3 = Cannot remove an item that is currently borrowed */
	public int removeItem(int index) {
		if (itemsCount == 0)
			return -1;
		if (index<0 || index>=itemsCount)
			return -2;
		if (!items[index].isAvailable())
			return -3;
		for(int i= index; i<itemsCount-1; i++) {
			items[i]=items[i+1];
		}
		items[--itemsCount]= null;
		return 1;
		
	}
	
	public boolean displayItemsList() {
		if (itemsCount == 0)
			return false;
		for(int i=1; i<=itemsCount; i++) {
			System.out.println(i+"- "+items[i-1].getName());
		}
		return true;
					
	}
	// Method that searches an item based on the names of the items, returns an array with items that qualify the criteria
	public LibraryItem[] searchItem(String input) {
		String itemName;
		input = input.toLowerCase();
		LibraryItem[] searchResult;
		int size=0;
		int count=0;
		for(int i=0; i<itemsCount; i++) {
			itemName = items[i].getName().toLowerCase();
			if(itemName.contains(input))
				size++;
		}
		searchResult= new LibraryItem[size];
		for(int i=0; i<itemsCount; i++) {
			itemName = items[i].getName().toLowerCase();
			if(itemName.contains(input)) {
				searchResult[count] = items[i];
				count++;
			}
		}
		return searchResult;
	}
	/* A recursive method that finds the index of the item in passed parameter, mainly devoloped to use after search
	 -1 = No items on list
	 -2 = Item could not be found */
	public int findIndex(LibraryItem a, int from) {
		if (itemsCount == 0)
			return -1;
		if (from == itemsCount)
			return -2;
		if (items[from] == a)
			return from;
		else
			return findIndex(a, from+1);
			
	}
	// Method that recieves manuscript showcasing requests from authors
	public boolean addRequest(LibraryItem request) {
		if(requestsCount == 10) 
			return false;
		requests[requestsCount++] = request.copyItem();
		return true;
	}
	/* Method to deny and remove request to showcase manuscript based on passed index
	 1 = Request denied and removed
	-1 = No requests to deny
	-2 = Invalid input  */
	public int denyRequest(int index) {
		if (requestsCount == 0)
			return -1;
		if (index<0 || index>=requestsCount)
			return -2;
		for (int i=index; i<requestsCount-1; i++) {
			requests[i] = requests[i+1];
		}
		requests[--requestsCount] = null;
		return 1;
	}
	/* Method to approve and remove request to showcase manuscript based on passed index
	 1 = Request approved and removed
	-1 = No requests to approve
	-2 = Invalid input
	-3 = Items list is full  */
	public int approveRequest(int index) {
		if (requestsCount == 0)
			return -1;
		if (index<0 || index>=requestsCount)
			return -2;
		if (itemsCount == items.length)
			return -3;
		String authorName = requests[index].getAuthorName();
		Author author = ((Author)AccountManager.getInstance().findAccount(authorName));
		items[itemsCount++] = requests[index].copyItem();
		author.showcaseManuscript();
		author.modifyBalance(requests[index].calculatePrice()/10);
		denyRequest(index);
		return 1;
	}
	
	public boolean displayRequestsList() {
		if (requestsCount == 0)
			return false;
		for(int i=0; i<requestsCount; i++) {
			System.out.println(i+"- Showcase request by: "+requests[i].getAuthorName());
		}
		return true;
		
	}

	public String getName() {
		return name;
	}

	public LibraryItem[] getItems() {
		return items;
	}

	public int getItemsCount() {
		return itemsCount;
	}

	public LibraryItem[] getRequests() {
		return requests;
	}

	public int getRequestsCount() {
		return requestsCount;
	}
}
