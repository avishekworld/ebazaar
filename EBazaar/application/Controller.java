
package application;

/** In this application, whenever a controller class
 *  needs to pass control to LoginControl, but also
 *  requires to regain control in order to populate
 *  a screen with data that was just made available
 *  during login, this interface is used. The doUpdate
 *  method is called by a LoginControl handler which,
 *  in effect, returns control to the main controller
 *  and allows it to get the necessary data and display.
 *  To get the right doUpdate method to work with the
 *  right listener class, the listener class itself
 *  (and not the concrete controller class) should
 *  implement Controller. See how it is done in 
 *  ViewOrdersController in the SelectOrderActionListener
 *  inner class.
 * 
 */
public interface Controller {
	public void doUpdate();

}
