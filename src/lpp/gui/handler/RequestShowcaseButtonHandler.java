package lpp.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import lpp.Library;
import lpp.account.Author;
import lpp.gui.GUIUtils;
import lpp.gui.IUpdateable;

public class RequestShowcaseButtonHandler implements ActionListener {
	private Author author;
	private Library library;
	private Container contentPane;
	private IUpdateable updateable;
	
	public RequestShowcaseButtonHandler(Author author, Library library, Container contentPane, IUpdateable updateable) {
		this.author = author;
		this.library = library;
		this.contentPane = contentPane;
		this.updateable = updateable;
	}
	
	public void actionPerformed(ActionEvent e) {
		String manuscriptName = JOptionPane.showInputDialog(contentPane, "Enter manuscript name", "Request Manuscript Showcase", JOptionPane.QUESTION_MESSAGE);
		if (manuscriptName == null) return;
		
		int manuscriptPages = GUIUtils.showNumberDialog(contentPane, "Request Manuscript Showcase", "Enter manuscript pages", "Invalid number of pages, please try again.", true);
		if (manuscriptPages == -1) return;
		
		if(author.submitManuscript(manuscriptPages, manuscriptName, library)) {
			JOptionPane.showMessageDialog(contentPane, "Your request has been submitted successfully.", "Request Manuscript Showcase", JOptionPane.INFORMATION_MESSAGE);
			
			if (updateable != null) {
				updateable.update();
			}
		} else {
			JOptionPane.showMessageDialog(contentPane, "Sorry, the library has currently too many manuscript showcase requests, please try again later.", "Request Manuscript Showcase", JOptionPane.ERROR_MESSAGE);
		}
	}
}
