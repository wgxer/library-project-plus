package lpp.gui;

import lpp.Library;
import lpp.item.LibraryItem;

public class LibraryItemListEntry {
	private Library library;
	private int index;
	
	public LibraryItemListEntry(Library library, int index) {
		this.library = library;
		this.index = index;
	}
	
	public String toString() {
		LibraryItem libraryItem = getLibraryItem();
		return "   " + libraryItem.getName() + " by " + libraryItem.getAuthorName();
	}
	
	public LibraryItem getLibraryItem() {
		return library.getItems().get(index);
	}
	
	public int getIndex() {
		return index;
	}
}
