package lpp.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ViewFactory {
	private boolean vertical;
	private int gap;
	
	private boolean fillWidth;
	private boolean fillHeight;
	
	private int paddingTop;
	private int paddingLeft;
	private int paddingBottom;
	private int paddingRight;

	private float alignmentX;
	private float alignmentY;
	
	private int roundedBorderThickness;
	private boolean visible;
	
	private ViewFactory(boolean vertical) {
		this.vertical = vertical;
		this.gap = 0;
		
		this.fillWidth = false;
		this.fillHeight = false;
		
		this.paddingTop = 0;
		this.paddingLeft = 0;
		this.paddingBottom = 0;
		this.paddingRight = 0;
		
		this.alignmentX = Component.LEFT_ALIGNMENT;
		this.alignmentY = Component.TOP_ALIGNMENT;
		
		this.roundedBorderThickness = 0;
		this.visible = true;
	}
	
	public static ViewFactory vertical() {
		return new ViewFactory(true);
	}
	
	public static ViewFactory horizontal() {
		return new ViewFactory(false);
	}
	
	public ViewFactory gap(int gap) {
		this.gap = gap;
		return this;
	}
	
	public ViewFactory fillWidth() {
		this.fillWidth = true;
		return this;
	}
	
	public ViewFactory fillHeight() {
		this.fillHeight = true;
		return this;
	}
	
	public ViewFactory roundedBorder(int thickness) {
		this.roundedBorderThickness = thickness;
		return this;
	}
	
	public ViewFactory padding(int padding) {
		return padding(padding, padding, padding, padding);
	}
	
	public ViewFactory padding(int paddingX, int paddingY) {
		return padding(paddingY, paddingX, paddingY, paddingX);
	}
	
	public ViewFactory padding(int paddingTop, int paddingLeft, int paddingBottom, int paddingRight) {
		this.paddingTop = paddingTop;
		this.paddingLeft = paddingLeft;
		this.paddingBottom = paddingBottom;
		this.paddingRight = paddingRight;
		
		return this;
	}
	
	public ViewFactory alignX(float alignmentX) {
		this.alignmentX = alignmentX;
		return this;
	}
	

	public ViewFactory alignY(float alignmentY) {
		this.alignmentY = alignmentY;
		return this;
	}
	
	public ViewFactory invisible() {
		this.visible = false;
		return this;
	}
	
	public JPanel build(Component ...components) {
		JPanel panel = new JPanel();
		
		for (int i = 0; i < components.length; i++) {
			Component component = components[i];
			if (component == null) continue;

			if (component instanceof JComponent) {
				JComponent jComponent = (JComponent) component;
				
				jComponent.setAlignmentX(alignmentX);
				jComponent.setAlignmentY(alignmentY);
			}
			
			panel.add(component);
			
			if (gap != 0) {
				if (vertical) {
					Component gapBox = Box.createVerticalStrut(gap);
					gapBox.setMaximumSize(new Dimension(1, gapBox.getMaximumSize().height));

					panel.add(gapBox);
				} else {
					Component gapBox = Box.createHorizontalStrut(gap);
					gapBox.setMaximumSize(new Dimension(gapBox.getMaximumSize().width, 1));

					panel.add(gapBox);
				}
			}
		}
		
		if (paddingTop != 0 || paddingRight != 0 || paddingBottom != 0 || paddingLeft != 0) {
			panel.setBorder(BorderFactory.createEmptyBorder(paddingTop, paddingLeft, paddingBottom, paddingRight));
		}
		
		if (roundedBorderThickness > 0) {
			panel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.black, roundedBorderThickness, true), panel.getBorder()));
		}
		
		if (fillWidth) {
			panel.add(Box.createHorizontalGlue());
		}
		
		if (fillHeight) {
			panel.add(Box.createVerticalGlue());
		}
		
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setLayout(new BoxLayout(panel, vertical ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS));
		
		panel.setVisible(visible);
		
		return panel;
	}
}
