package lpp.gui;

import lpp.item.LibraryItem;

public class LibraryItemListEntry {
	private LibraryItem libraryItem;
	
	public LibraryItemListEntry(LibraryItem libraryItem) {
		this.libraryItem = libraryItem;
	}
	
	public String toString() {
		return "   " + libraryItem.getName() + " by " + libraryItem.getAuthorName();
	}
	
	public LibraryItem getLibraryItem() {
		return libraryItem;
	}
}
