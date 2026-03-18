package lpp.item;

public class Book extends LibraryItem {
	
	private int publcationYear;

	public Book(int pages,String name,String aoutherName, int publcationYear) {
		super(pages,name,aoutherName);
		this.publcationYear = publcationYear;
	}

	
	public void display() {
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│              Book Information          │");
		System.out.println("├────────────────────────────────────────┤");
		System.out.printf ("│ %-12s : %-20s │\n", "Name", name);
		System.out.printf ("│ %-12s : %-20s │\n", "Author", authorName);
		System.out.printf ("│ %-12s : %-20d │\n", "Pages", pages);
		System.out.printf ("│ %-12s : %-20d │\n", "Year", publcationYear);
		System.out.printf ("│ %-12s : %-20.1f │\n", "Rating", reviews);
		System.out.printf ("│ %-12s : %-20d │\n", "Review Count", reviewsCount);
		System.out.println("╰────────────────────────────────────────╯");
	}

	
	public double calculatePrice() {
		
		return (getPages() / 15) ;
	}


	public int getPublcationYear() {
		return publcationYear;
	}
}
