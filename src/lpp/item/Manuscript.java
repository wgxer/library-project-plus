package lpp.item;

import lpp.LinkedList;

public class Manuscript extends LibraryItem {
	
	private int age;
	private LinkedList<Comment> comments;
	
	public Manuscript(int pages, String name, String authorName, int age) {
		super(pages, name, authorName);
		this.age= age;
		// Initializing our manual LinkedList
		comments= new LinkedList<Comment>();
	}
	
	public Manuscript(Manuscript m) {
		super(m);
		this.age = m.age;
		
		this.comments= new LinkedList<Comment>();
		for(int i = 0; i < m.comments.size(); i++) {
			// Using get(i) from our LinkedList class
			this.comments.add(new Comment(m.comments.get(i)));
		}
	}
	
	public LibraryItem copyItem() {
		return new Manuscript(this);
	}
	/* add comment if there is available space and the comment doesn't exceed character limit
	   1 = comment added successfully
	  -1 = no space to add comment
	  -2 = comment exceeds character limit   */
	public int addComment(String comment) {
		if (comment.length() > 120)
			return -2;
		// Using our manual add method
		comments.add(new Comment(usedBy.getUsername(), comment));
		
		return 1;
	}
	/* show comment based on given index
	  1 = comment printed successfully
	 -1 = no comments available
	 -2 = index out of bounds  */
	                                     
	public int printComment(int index) {
		if (comments.isEmpty())
			return -1;
		
		if (index<1 || index>100)
			return -2;
		
		comments.get(index-1).display();
		return 1;
	}
	
	
	public boolean displayCommentsList() {
		if (comments.isEmpty())
			return false;
		
		for (int i = 1; i <= comments.size(); i++) {
			System.out.println(i + "- Comment left by "+comments.get(i - 1).getCommenter());
			
		} return true;
	}
	
	public void display() {
		System.out.println("╭──────────────────────────────────────────────────╮");
		System.out.println("│              Manuscript Information              │");
		System.out.println("├──────────────────────────────────────────────────┤");
		System.out.printf ("│ %-14s : %-30s  │\n", "Name", name);
		System.out.printf ("│ %-14s : %-30s  │\n", "Author", authorName);
		System.out.printf ("│ %-14s : %-30d  │\n", "Pages", pages);
		System.out.printf ("│ %-14s : %-30d  │\n", "Age", age);
		System.out.printf ("│ %-14s : %-30.2f  │\n", "Borrowing fee", calculatePrice());
		System.out.printf ("│ %-14s : %-30d  │\n", "Comments", comments.size());
		System.out.printf ("│ %-14s : %-30.2f  │\n", "Rating", reviews);
		System.out.printf ("│ %-14s : %-30d  │\n", "Times borrowed", timesUsed);
		System.out.println("╰──────────────────────────────────────────────────╯");
	} 
	
	public int getAge() {
		return age;
	}
	
	public LinkedList<Comment> getComments() {
		return comments;
	}
	
	public double calculatePrice() {
		double price = ((double)pages)/15+age*2;
		return price; 
	}

}