package application;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import middleware.DatabaseException;

import application.gui.CustomTableModel;
import application.gui.EbazaarMainFrame;
import application.gui.SelectOrderWindow;
import application.gui.ViewOrderDetailsWindow;
import business.SessionContext;
import business.externalinterfaces.CustomerConstants;
import business.externalinterfaces.ICustomerSubsystem;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;
import business.util.OrderUtil;

/**
 * @author pcorazza
 */
public class ViewOrdersController implements CleanupControl {

	// ///////// EVENT HANDLERS -- new code goes here ////////////

	// // control SelectOrderWindow
	class ViewOrderDetailsListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";

		public void actionPerformed(ActionEvent evt) {
			JTable table = selectOrderWindow.getTable();
			CustomTableModel model = selectOrderWindow.getModel();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				// start by reading order id from screen
				// String id = (String)table.getValueAt(selectedRow, 0);

				selectOrderWindow.setVisible(false);
				@SuppressWarnings("unused")
				// String selOrderId = (String)model.getValueAt(selectedRow,0);
				String selOrderId = "1";

				// now get customer from SessionContext, getOrderHistory
				// and then read the appropriate order from the history, using
				// order id

				// default implementation
				selectOrderWindow.setVisible(false);
				viewOrderDetailsWindow = new ViewOrderDetailsWindow();
				mainFrame.getDesktop().add(viewOrderDetailsWindow);
				viewOrderDetailsWindow.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(selectOrderWindow, ERROR_MESSAGE,
						ERROR, JOptionPane.ERROR_MESSAGE);

			}

		}

	}

	class CancelViewOrdersListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			selectOrderWindow.setVisible(false);
			BrowseAndSelectController.getInstance().makeMainFrameVisible();

		}
	}

	// /// control of ViewOrderDetailsWindow
	class OrderDetailsOkListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			selectOrderWindow.setVisible(true);

			viewOrderDetailsWindow.dispose();
		}
	}

	class SelectOrderActionListener implements ActionListener, Controller {
		/* this method is called when LoginControl needs this class to load order 
         * history data for newly logged in customer
         */
		public void doUpdate() {
			if(selectOrderWindow != null){
				SessionContext context = SessionContext.getInstance();
				ICustomerSubsystem customer = (ICustomerSubsystem)context.get(CustomerConstants.CUSTOMER);
				if(customer !=null) {
					List<IOrder> orderList = customer.getOrderHistory();
					List<String[]> displayableList = OrderUtil.extractOrderData(orderList);			
					selectOrderWindow.updateModel(displayableList);
					mainFrame.getDesktop().add(selectOrderWindow);
					selectOrderWindow.setVisible(true);
					
				}
			}
		}	    
		public void actionPerformed(ActionEvent e) {
			SessionContext context = SessionContext.getInstance();
			selectOrderWindow = new SelectOrderWindow();
			Boolean loggedIn = (Boolean)context.get(CustomerConstants.LOGGED_IN);
			ICustomerSubsystem customer = null;
			List<IOrder> orderList = null;
			//needs to log in
			if(!loggedIn.booleanValue()) {
	       	    LoginControl loginControl = 
        	        new LoginControl(selectOrderWindow,mainFrame,this);
        	    loginControl.startLogin();
 
			}
			else {//already logged in
			
				customer = (ICustomerSubsystem)context.get(CustomerConstants.CUSTOMER);
				orderList = customer.getOrderHistory();
				List<String[]> displayableList = OrderUtil.extractOrderData(orderList);
				
				selectOrderWindow.updateModel(displayableList);
				mainFrame.getDesktop().add(selectOrderWindow);
				selectOrderWindow.setVisible(true);
				
			}
		}
	}

	// /////// PUBLIC INTERFACE -- for getting instances of listeners ///
	public ActionListener getViewOrderDetailsListener(SelectOrderWindow w) {
		return (new ViewOrderDetailsListener());
	}

	public ActionListener getCancelViewOrdersListener(SelectOrderWindow w) {
		return (new CancelViewOrdersListener());
	}

	public ActionListener getOrderDetailsOkListener(ViewOrderDetailsWindow w) {
		return (new OrderDetailsOkListener());
	}

	public ActionListener getSelectOrderActionListener(EbazaarMainFrame f) {
		return (new SelectOrderActionListener());
	}

	// ////// PUBLIC ACCESSORS to register screens controlled by this class////
	public void setSelectOrderWindow(SelectOrderWindow w) {
		selectOrderWindow = w;
	}

	public void setViewOrderDetailsWindow(ViewOrderDetailsWindow w) {
		viewOrderDetailsWindow = w;
	}

	public void setMainFrame(EbazaarMainFrame w) {
		mainFrame = w;
	}

	// ///// screens -- private references
	private SelectOrderWindow selectOrderWindow;
	private ViewOrderDetailsWindow viewOrderDetailsWindow;
	private EbazaarMainFrame mainFrame;
	private Window[] allWindows = { mainFrame };
	private JInternalFrame[] internalFrames = { selectOrderWindow,
			viewOrderDetailsWindow };

	public void cleanUp() {
		for (Window w : allWindows) {
			if (w != null) {
				System.out.println("Disposing of window "
						+ w.getClass().getName());
				w.dispose();
			}
		}
	}

	// ///// make this class a singleton
	private static ViewOrdersController instance = new ViewOrdersController();

	public static ViewOrdersController getInstance() {
		return instance;
	}

	private ViewOrdersController() {
	}
}
