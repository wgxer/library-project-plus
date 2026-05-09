package lpp.gui.handler;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import lpp.AccountManager;
import lpp.FileManager;
import lpp.Library;
import lpp.gui.frame.AccountFrame;

public class ShowParentCloseHandler implements WindowListener {
	private Frame parent;
	private Library library;
	
	public ShowParentCloseHandler(Frame parent) {
		this(parent, null);
	}
	
	public ShowParentCloseHandler(Frame parent, Library library) {
		this.parent = parent;
		this.library = library;
	}
	
	
	public void windowClosed(WindowEvent e) {
		parent.setLocation(e.getWindow().getLocation());
		parent.setVisible(true);
		
		if (library != null) {
			FileManager.writeLibrary("library.dat", library, AccountManager.getInstance());
		}
	}
	
	public void windowIconified(WindowEvent e) {}	
	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
