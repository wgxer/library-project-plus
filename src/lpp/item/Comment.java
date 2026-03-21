package lpp.item;

import lpp.Displayable;

public class Comment implements Displayable {

	private String commenter;
	private String body;
	
	public Comment (String commenter, String body) {
		this.commenter= commenter;
		this.body= body;
	}
	
	public void display() {
		System.out.println("╭──────────────────────────────╮");
		System.out.printf ("│%-30s│\n",commenter);
		System.out.println("├──────────────────────────────┤");
		for(int i=0; i<body.length(); i+=30) {
		System.out.printf ("│%-30s│\n",body.substring(i, Math.min(i+30, body.length())));
		}
		System.out.println("╰──────────────────────────────╯");
	}
	
	public String getCommenter() {
		return commenter;
	}
	
	public String getBody() {
		return body;
	}
}
