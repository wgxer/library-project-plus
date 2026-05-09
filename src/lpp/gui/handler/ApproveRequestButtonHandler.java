package lpp.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import lpp.Library;
import lpp.gui.IUpdateable;

public class ApproveRequestButtonHandler implements ActionListener {
	private Library library;
	private int requestIndex;

	private Container contentPane;
	private IUpdateable updateable;
	
	public ApproveRequestButtonHandler(Library library, int requestIndex, Container contentPane, IUpdateable updateable) {
		this.library = library;
		this.requestIndex = requestIndex;
		this.contentPane = contentPane;
		this.updateable = updateable;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			library.approveRequest(requestIndex);
			
			if (updateable != null) {
				updateable.update();
			}
			
		} catch(NoSuchElementException exception) {
			JOptionPane.showMessageDialog(
				contentPane, 
				"No requests to approve.", 
				"Request Approval failed !", 
				JOptionPane.ERROR_MESSAGE
			);
		} catch(IllegalArgumentException exception) {
			JOptionPane.showMessageDialog(
				contentPane, 
				"Invalid request.", 
				"Request Approval failed !", 
				JOptionPane.ERROR_MESSAGE
			);
		} catch(IllegalStateException exception) {
			JOptionPane.showMessageDialog(
				contentPane, 
				"Library is full.", 
				"Request Approval failed !", 
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
}
