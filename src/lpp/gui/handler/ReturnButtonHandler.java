package lpp.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import lpp.account.User;
import lpp.gui.IUpdateable;
import lpp.item.LibraryItem;

public class ReturnButtonHandler implements ActionListener {
	private User user;
	private LibraryItem libraryItem;
	private Container contentPane;
	private IUpdateable updateable;
	
	public ReturnButtonHandler(User user, LibraryItem libraryItem, Container contentPane, IUpdateable updateable) {
		this.user = user;
		this.libraryItem = libraryItem;
		this.contentPane = contentPane;
		this.updateable = updateable;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			user.returnItem(libraryItem);

			if (updateable != null) {
				updateable.update();
			}
		} catch (IllegalArgumentException exception) {
			JOptionPane.showMessageDialog(
				contentPane, 
				"This library item is not borrowed.", 
				"Return failed !", 
				JOptionPane.ERROR_MESSAGE
			);
		} catch(NoSuchElementException exception) { 
			JOptionPane.showMessageDialog(
				contentPane, 
				"This library item couldn't be found in the library.", 
				"Return failed !", 
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
}
