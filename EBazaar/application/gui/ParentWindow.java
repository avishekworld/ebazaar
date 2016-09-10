package application.gui;

import java.awt.Component;

/**
 * Class Description: Each window that is accessed from another window
 * should implement this interface. This supports
 * navigation back to calling windows.
 */
public interface ParentWindow {
	public void setVisible(boolean b);
	public Component getParentWindow();
	public void setParentWindow(Component w);

}
