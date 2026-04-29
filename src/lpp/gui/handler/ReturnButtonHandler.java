package lpp.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(user.returnItem(libraryItem)) {
		case 1:
			if (updateable != null) {
				updateable.update();
			}
			
			break; // Item is going to disappear from menu
		case -1:
			JOptionPane.showMessageDialog(
				contentPane, 
				"You don't have any borrowed books.", 
				"Return failed !", 
				JOptionPane.ERROR_MESSAGE
			);
			
			break;
		case -2:
			JOptionPane.showMessageDialog(
				contentPane, 
				"This library item is not borrowed.", 
				"Return failed !", 
				JOptionPane.ERROR_MESSAGE
			);
			
			break;
		case -3:
			JOptionPane.showMessageDialog(
				contentPane, 
				"This library item couldn't be found in the library.", 
				"Return failed !", 
				JOptionPane.ERROR_MESSAGE
			);
			
			break;
		default:
		}
	}
}
