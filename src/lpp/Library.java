package lpp;

import lpp.item.LibraryItem;
import lpp.item.UnavailableItemException;
import java.io.*; 
import java.util.NoSuchElementException;
import lpp.account.Author;

public class Library {
	
	private String name;
	private LinkedList<LibraryItem> items;
	private LinkedList<LibraryItem> requests;
	
	public Library(String name, int capacity) {
		this.name = name;
		this.items = new LinkedList<LibraryItem>();
		this.requests = new LinkedList<LibraryItem>();
	}
	
	public void writeItems(String fileName) {
		ObjectOutputStream itemsOOS = null;
		try {
			File itemsFile = new File(fileName);
			FileOutputStream itemsFOS = new FileOutputStream(itemsFile);
			itemsOOS = new ObjectOutputStream(itemsFOS);
			
			itemsOOS.writeInt(items.size());
			for(int i = 0; i < items.size(); i++) {
				itemsOOS.writeObject(items.get(i));
			}
	
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(itemsOOS != null) itemsOOS.close();
			} catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void readItems(String fileName) {
		ObjectInputStream itemsOIS = null;
		try {
			File itemsFile = new File(fileName);
			FileInputStream itemsFIS = new FileInputStream(itemsFile);
			itemsOIS = new ObjectInputStream(itemsFIS);
		
			while(true) {
			     try {
				      LibraryItem readItem = (LibraryItem)itemsOIS.readObject();
				      this.addItem(readItem);
			 
			     } catch (EOFException e) {
			    	break;
			     } catch (ClassNotFoundException e) {
				  System.out.println(e.getMessage());
			    	continue;
			    }
			  } 
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
				if(itemsOIS != null) itemsOIS.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		} 
		 
	public boolean addItem(LibraryItem i) {
		items.add(i.copyItem());
		return true;
	}
	
	//Removes an item from list based on passed index
	public void removeItem(int index) throws UnavailableItemException {
		if (items.isEmpty())
			throw new NoSuchElementException("There are no items to remove!");
		if (index < 0 || index >= items.size())
			throw new IllegalArgumentException("Invalid Input!");
		if (!items.get(index).isAvailable())
			throw new UnavailableItemException("This item is currently borrowed! Please try again later");

		items.remove(index);
	}
	
	public boolean displayItemsList() {
		if (items.isEmpty())
			return false;
		for(int i = 1; i <= items.size(); i++) {
			System.out.println(i + "- " + items.get(i-1).getName());
		}
		return true;
	}

	// Method that searches an item based on the names of the items, returns an array with items that qualify the criteria
	public LinkedList<LibraryItem> searchItem(String input) {
		input = input.toLowerCase();
		
		LinkedList<LibraryItem> searchResult = new LinkedList<LibraryItem>();
		
		for(int i = 0; i < items.size(); i++) {
			String itemName = items.get(i).getName().toLowerCase();
			
			if(itemName.contains(input)) {
				searchResult.add(items.get(i));
			}
		}
		
		return searchResult;
	}

	// A recursive method that finds the index of the item in passed parameter, mainly developed to use after search
	public int findIndex(LibraryItem a, int from) {
		if (items.isEmpty())
			return -1;
		if (from == items.size())
			return -2;
		if (items.get(from).equals(a))
			return from;
		else
			return findIndex(a, from + 1);
	}

	// Method that receives manuscript showcasing requests from authors
	public boolean addRequest(LibraryItem request) {
		if(requests.size() == 10) 
			return false;
		requests.add(request.copyItem());
		return true;
	}

	// Method to deny and remove request to showcase manuscript based on passed index
	public void denyRequest(int index) {
		if (requests.isEmpty())
			throw new NoSuchElementException("There are no requests to deny at the moment!");
		if (index < 0 || index >= requests.size())
			throw new IllegalArgumentException("Invalid input!");
		
		requests.remove(index);
	}

	// Method to approve and remove request to showcase manuscript based on passed index
	public void approveRequest(int index) {
		if (requests.isEmpty())
			throw new NoSuchElementException("There are no requests to approve at the moment!");
		if (index < 0 || index >= requests.size())
			throw new IllegalArgumentException("Invalid input!");

		String authorName = requests.get(index).getAuthorName();
		Author author = ((Author)AccountManager.getInstance().findAccount(authorName));
		
		items.add(requests.get(index).copyItem());
		
		author.showcaseManuscript();
		author.modifyBalance(requests.get(index).calculatePrice() / 10);
		denyRequest(index);
	}
	
	public boolean displayRequestsList() {
		if (requests.isEmpty())
			return false;
		for(int i = 0; i < requests.size(); i++) {
			System.out.println(i + "- Showcase request by: " + requests.get(i).getAuthorName());
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public LinkedList<LibraryItem> getItems() {
		return items;
	}

	public LinkedList<LibraryItem> getRequests() {
		return requests;
	}
}