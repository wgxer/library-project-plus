package lpp.gui.handler;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ShowParentCloseHandler implements WindowListener {
	private Frame parent;
	
	public ShowParentCloseHandler(Frame parent) {
		this.parent = parent;
	}
	
	
	public void windowClosed(WindowEvent e) {
		parent.setLocation(e.getWindow().getLocation());
		parent.setVisible(true);
	}
	
	public void windowIconified(WindowEvent e) {}	
	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
