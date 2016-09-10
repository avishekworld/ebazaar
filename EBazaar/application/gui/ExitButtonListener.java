package application.gui;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JInternalFrame;

import application.ApplicationCleanup;

/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description:  This class provides a common listener class for all
  *   screens that need to provide an exit button.
 * <p>
 * <table border="1">
 * <tr>
 * 		<th colspan="3">Change Log</th>
 * </tr>
 * <tr>
 * 		<th>Date</th> <th>Author</th> <th>Change</th>
 * </tr>
 * <tr>
 * 		<td>Oct 22, 2004</td>
 *      <td>klevi, pcorazza</td>
 *      <td>New class file</td>
 * </tr>
 * </table>
 *
 */
public class ExitButtonListener implements ActionListener {
	private Component w;
	
	public ExitButtonListener(Component w) {
		this.w = w;
	}
	
    public void actionPerformed(ActionEvent evt) {
    	disposeComponent();
		(new ApplicationCleanup()).cleanup();
    	System.exit(0);
    
    }
    private void disposeComponent() {
    	if(w instanceof Window) {
    		((Window)w).dispose();
    	}
    	else if(w instanceof JInternalFrame) {
    		((JInternalFrame)w).dispose();
    	}
    	else {
    		w = null;
    	
    	}
    }

}
