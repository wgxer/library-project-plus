package lpp;

import lpp.item.LibraryItem;

public class Library {
	
	private String name;
	private LibraryItem[] items;
	private int itemsCount;
	
	public Library(String name, int capacity) {
		this.name= name;
		items= new LibraryItem[capacity];
		itemsCount= 0;
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
		if (index<0 || index>itemsCount)
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
		LibraryItem[] searchResult;
		int size=0;
		int count=0;
		for(int i=0; i<itemsCount; i++) {
			if(items[i].getName().length()<input.length())
				continue;
			if(items[i].getName().substring(0,input.length()).equalsIgnoreCase(input))
				size++;
		}
		searchResult= new LibraryItem[size];
		for(int i=0; i<itemsCount; i++) {
			if(items[i].getName().length()<input.length())
				continue;
			if(items[i].getName().substring(0,input.length()).equalsIgnoreCase(input)) {
				searchResult[count]= items[i];
				count++;
			}
		}
		return searchResult;
	}
	/* Method that finds the index of the item in passed parameter, mainly devoloped to use after search
	 -1 = No items on list
	 -2 = Item could not be found */
	public int findIndex(LibraryItem a) {
		if(itemsCount == 0)
			return -1;
		int index = -2;
		for(int i=0; i<itemsCount; i++) {
			if(items[i] == a)          // Since this method is mainly used after the search method, the passed parameter will definitely share the same reference with one of the items
			index = i;	
		}
		return index;
			
	}
}
