package lpp.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import lpp.gui.IUpdateable;
import lpp.item.LibraryItem;

public class RateButtonHandler implements ActionListener {
	private LibraryItem libraryItem;
	private Container contentPane;
	private IUpdateable updateable;

	public RateButtonHandler(LibraryItem libraryItem, Container contentPane, IUpdateable updateable) {
		this.libraryItem = libraryItem;
		this.contentPane = contentPane;
		this.updateable = updateable;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Integer rating = (Integer) JOptionPane.showInputDialog(contentPane, "Enter your rating out of 5",
				"Rating " + libraryItem.getName(), JOptionPane.PLAIN_MESSAGE, null, new Integer[] { 1, 2, 3, 4, 5 }, 3);
	
		if (rating != null && libraryItem.reviewItem(rating)) {
			JOptionPane.showMessageDialog(contentPane, "Thank you for your rating !");
			
			if (updateable != null) {
				updateable.update();
			}
		}
	}
}
