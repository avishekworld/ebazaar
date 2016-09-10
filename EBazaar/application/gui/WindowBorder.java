package application.gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;
    
/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description: This class provides the main border that
 * is used on the screens in the E-Bazaar gui.
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
public class WindowBorder implements Border {
        private Color color;
        public WindowBorder(Color color) {
            this.color = color;
        }
        public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
            Insets insets = getBorderInsets(component);
            g.setColor(color);
            g.fillRect(x,y,3,height);
            g.fillRect(x,y,width,3);
            g.setColor(color.darker());
            g.fillRect(x+width-insets.right, y, 3, height);
            g.fillRect(x,y+height-insets.bottom, width, 3);
        }
        public Insets getBorderInsets(Component component) {
            return new Insets(3,3,3,3);
        }
        public boolean isBorderOpaque() {
            return false;
        }
    }
