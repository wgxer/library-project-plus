package lpp.item;

public class Manuscript extends LibraryItem {
	
	private int age;
	private String[] feedbacks;
	private int feedbacksCount;
	
	
	
	public Manuscript(int pages, String name, String authorName, int age) {
		super(pages, name, authorName);
		this.age= age;
		feedbacks= new String[100];
		feedbacksCount=0;
	}
	
	public boolean addFeedback(String comment) {
		if (feedbacksCount == 100)
			return false;
		feedbacks[feedbacksCount]= comment+"\n-left by"+usedBy.getUsername();
		feedbacksCount++;
		return true;
	}
	/* show comment based on given index
	  1 = comment printed successfuly
	 -1 = no comments available
	 -2 = index out of bounds  */
	                                     
	public int printComment(int index) {
		if (feedbacksCount==0)
			return -1;
		if (index<1 || index>100)
			return -2;              // This method prints the first 20 characters of the comment, then makes a new line. Needs testing
		for(int i=0; i<feedbacks[index].length(); i+=20) {
			System.out.println(feedbacks[index].substring(i, Math.min(i+20, feedbacks[index].length())));
			
		} return 1;
	}
	
	// Displays list of comments registred, best if there was an array of objects instead of Strings
	public boolean displayCommentsList() {
		if (feedbacksCount == 0)
			return false;
		for (int i=1; i<=feedbacksCount; i++) {
			System.out.println("Comment -"+i);
			
		} return true;
	}
	
	public void display() {
		// TODO: Implement Manuscript::display
	}
	
	public double calculatePrice() {
		double price = pages/15+age*5;
		return price; 
	}

}
