package lpp.item;

public class Manuscript extends LibraryItem {
	
	private int age;
	private Comment[] comments;
	private int commentsCount;
	
	
	
	public Manuscript(int pages, String name, String authorName, int age) {
		super(pages, name, authorName);
		this.age= age;
		comments= new Comment[100];
		commentsCount=0;
	}
	/* add comment if there is available space and the comment doesn't exceed character limit
	   1 = comment added successfully
	  -1 = no space to add comment
	  -2 = comment exceeds character limit   */
	public int addComment(String comment) {
		if (commentsCount == 100)
			return -1;
		if (comment.length() == 100)
			return -2;
		comments[commentsCount]= new Comment(usedBy.getUsername(), comment);
		commentsCount++;
		return 1;
	}
	/* show comment based on given index
	  1 = comment printed successfully
	 -1 = no comments available
	 -2 = index out of bounds  */
	                                     
	public int printComment(int index) {
		if (commentsCount == 0)
			return -1;
		if (index<1 || index>100)
			return -2;
		comments[index-1].display();
		return 1;
	}
	
	
	public boolean displayCommentsList() {
		if (commentsCount == 0)
			return false;
		for (int i=1; i<=commentsCount; i++) {
			System.out.println(i+"- Comment left by "+comments[i-1].getCommenter());
			
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
		System.out.printf ("│ %-14s : %-30d  │\n", "Comments", commentsCount);
		System.out.printf ("│ %-14s : %-30.2f  │\n", "Rating", reviews);
		System.out.printf ("│ %-14s : %-30d  │\n", "Times borrowed", timesUsed);
		System.out.println("╰──────────────────────────────────────────────────╯");
	} 
	
	public double calculatePrice() {
		double price = pages/15+age*5;
		return price; 
	}

}
