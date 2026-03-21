package lpp.item;

public class Book extends LibraryItem {
	
	private int publcationYear;

	public Book(int pages,String name,String aoutherName, int publcationYear) {
		super(pages,name,aoutherName);
		this.publcationYear = publcationYear;
	}

	
	public void display() {
		System.out.println("╭───────────────────────────────╮");
		System.out.println("│        Book Information       │");
		System.out.println("├───────────────────────────────┤");
		System.out.printf ("│ %-14s : %-12s │\n", "Name", name);
		System.out.printf ("│ %-14s : %-12s │\n", "Author", authorName);
		System.out.printf ("│ %-14s : %-12d │\n", "Pages", pages);
		System.out.printf ("│ %-14s : %-12d │\n", "Year", publcationYear);
		System.out.printf ("│ %-14s : %-12.2f │\n", "Rating", reviews);
		System.out.printf ("│ %-14s : %-12d │\n", "Times borrowed", timesUsed);
		System.out.println("╰───────────────────────────────╯");
	}

	
	public double calculatePrice() {
		
		return (getPages() / 15) ;
	}


	public int getPublcationYear() {
		return publcationYear;
	}
}
