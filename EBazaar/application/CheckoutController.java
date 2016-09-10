package application;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import middleware.DatabaseException;
import middleware.EBazaarException;
import application.gui.CartItemsWindow;
import application.gui.CustomTableModel;
import application.gui.DefaultData;
import application.gui.EbazaarMainFrame;
import application.gui.FinalOrderWindow;
import application.gui.PaymentWindow;
import application.gui.ShipAddressesWindow;
import application.gui.ShippingBillingWindow;
import application.gui.TermsWindow;
import business.ParseException;
import business.RuleException;
import business.SessionContext;
import business.externalinterfaces.CustomerConstants;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.ICustomerSubsystem;
import business.externalinterfaces.IShoppingCartSubsystem;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.CustomerUtil;
import business.util.ShoppingCartUtil;
import business.util.StringParse;

public class CheckoutController implements CleanupControl {
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	private final String TERMS_MESSAGE_FILE = CustomerConstants.CURR_DIR
			+ "\\resources\\terms.txt";
	private final String GOODBYE_FILE = CustomerConstants.CURR_DIR
			+ "\\resources\\goodbye.txt";

	String extractGoodbyeMessage() throws ParseException {
		return StringParse.extractTextFromFile(GOODBYE_FILE);
	}

	String extractTermsText() throws ParseException {
		return StringParse.extractTextFromFile(TERMS_MESSAGE_FILE);
	}

	// ///////// EVENT HANDLERS -- new code goes here ////////////

	// /// control CartItemsWindow
	class ProceedToCheckoutListener implements ActionListener, Controller {
		SessionContext context = SessionContext.getInstance();

		public void doUpdate() {
			populateScreen();
		}

		/**
		 * This method populates the shipping billing window with the default
		 * address data for this customer. Retrieves the customer name and
		 * default shipping and billing addresses from customer subsystem
		 * interface. These values should have been loaded into the Customer
		 * subsystem at login.
		 */
		void populateScreen() {

			/*
			 * Get customer subsystem and read customer profile and default
			 * shipping and billing addresses here
			 */
			shippingBillingWindow = new ShippingBillingWindow();
			/*
			 * set default shipping and billing address info here
			 * shippingBillingWindow
			 * .setShippingAddress(custProfile.getFirstName() +
			 * " "+custProfile.getLastName(), defaultShipAddress.getStreet1(),
			 * defaultShipAddress.getCity(), defaultShipAddress.getState(),
			 * defaultShipAddress.getZip());
			 * 
			 * shippingBillingWindow.setBillingAddress(custName.getFirstName() +
			 * " "+custName.getLastName(), defaultBillAddress.getStreet1(),
			 * defaultBillAddress.getCity(), defaultBillAddress.getState(),
			 * defaultBillAddress.getZip());
			 */
			EbazaarMainFrame.getInstance().getDesktop()
					.add(shippingBillingWindow);
			shippingBillingWindow.setVisible(true);
		}

		public void actionPerformed(ActionEvent evt) {
			cartItemsWindow.setVisible(false);
			/* check that cart is not empty before going to next screen */
			Boolean loggedIn = (Boolean) context
					.get(CustomerConstants.LOGGED_IN);
			if (!loggedIn.booleanValue()) {
				shippingBillingWindow = new ShippingBillingWindow();
				LoginControl loginControl = new LoginControl(
						shippingBillingWindow, cartItemsWindow, this);

				loginControl.startLogin();

			} else {
				populateScreen();

			}
		}
	}

	// // control ShippingBillingWindow
	class SelectShipButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			shippingBillingWindow.setVisible(false);
			ShipAddressesWindow shipAddrs = new ShipAddressesWindow();

			// get customer from SessionContext
			ICustomerSubsystem cust = (ICustomerSubsystem) SessionContext
					.getInstance().get(CustomerConstants.CUSTOMER);

			try {
				List<IAddress> addresses = cust.getAllAddresses();
				List<String[]> addressesAsArrays = CustomerUtil
						.addrListToListOfArrays(addresses);
				shipAddrs.updateModel(addressesAsArrays);
				EbazaarMainFrame.getInstance().getDesktop().add(shipAddrs);
				shipAddrs.setVisible(true);
			} catch (DatabaseException e) {
				JOptionPane.showMessageDialog(shipAddressesWindow,
						"Unable to locate customer addresses", "Error",
						JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class ProceedFromBillingCheckoutListener implements ActionListener {
		ICustomerSubsystem cust;
		boolean rulesOk = true;
		String fullname;

		public void actionPerformed(ActionEvent evt) {
			boolean rulesOk = true;
			IAddress cleansedAddr = null;
			shippingBillingWindow.setVisible(false);
			cust = (ICustomerSubsystem) SessionContext.getInstance().get(
					CustomerConstants.CUSTOMER);
			fullname = cust.getCustomerProfile().getFirstName() + " "
					+ cust.getCustomerProfile().getLastName();
			if (shippingBillingWindow.isNewShipAddress()) {

				String[] addrFlds = shippingBillingWindow
						.getShipAddressFields();

				IAddress addr = cust.createAddress(addrFlds[0], addrFlds[1],
						addrFlds[2], addrFlds[3]);

				try {
					cleansedAddr = cust.runAddressRules(addr);
					cust.saveNewAddress(cleansedAddr);
					shippingBillingWindow.setAddressFields(new String[] {
							cleansedAddr.getStreet1(), cleansedAddr.getCity(),
							cleansedAddr.getState(), cleansedAddr.getZip() });
				} catch (RuleException e) {
					rulesOk = false;
					System.out.println(e.getMessage());
					JOptionPane.showMessageDialog(shipAddressesWindow,
							e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					shippingBillingWindow.setVisible(true);
				} catch (EBazaarException e) {
					rulesOk = false;
					JOptionPane
							.showMessageDialog(
									shipAddressesWindow,
									"An error has occurred that prevents further processing",
									"Error", JOptionPane.ERROR_MESSAGE);
					shippingBillingWindow.setVisible(true);
				}

			}
			// load into shopping cart and set up payment window
			if (rulesOk) {

				// load addresses into shopping cart
				String[] s = shippingBillingWindow.getShipAddressFields();

				String[] b = shippingBillingWindow.getBillAddressFields();
				if (b[0].isEmpty() || b[1].isEmpty() || b[2].isEmpty()
						|| b[3].isEmpty()) {
					b = s;
				}
				IAddress shipAddr = cust.createAddress(s[0], s[1], s[2], s[3]);
				IAddress billAddr = cust.createAddress(b[0], b[1], b[2], b[3]);
				// cust.setBillingAddressInCart(billAddr);
				// cust.setShippingAddressInCart(shipAddr);

				setupPaymentWindow();
			}
		}

		void setupPaymentWindow() {
			// get default payment info from customer object
			// ICreditCard cc = cust.getDefaultPaymentInfo();
			// String[] ccAsArray = CustomerUtil.creditCardToStringArray(cc);

			// fake data implementation
			String[] ccAsArray = new String[] { "name", "num", "type", "expir" };
			paymentWindow = new PaymentWindow();
			paymentWindow.setCredCardFields(ccAsArray[0], ccAsArray[1],
					ccAsArray[2], ccAsArray[3]);
			EbazaarMainFrame.getInstance().getDesktop().add(paymentWindow);
			paymentWindow.setVisible(true);
			paymentWindow.setParentWindow(shippingBillingWindow);

		}
	}

	class BackToCartButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			cartItemsWindow.setVisible(true);
			shippingBillingWindow.setVisible(false);
		}
	}

	// // control ShipAddressesWindow
	class SelectAddressesListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";

		public void actionPerformed(ActionEvent evt) {
			JTable table = shipAddressesWindow.getTable();
			CustomTableModel model = shipAddressesWindow.getModel();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				shipAddressesWindow.setVisible(false);
				// get cust name from customer subsystem -- for now we use fake
				// data
				String name = "Joe Smith";
				if (shippingBillingWindow != null) {
					shippingBillingWindow.setShippingAddress(name,
							(String) model.getValueAt(selectedRow,
									DefaultData.STREET_INT), (String) model
									.getValueAt(selectedRow,
											DefaultData.CITY_INT),
							(String) model.getValueAt(selectedRow,
									DefaultData.STATE_INT), (String) model
									.getValueAt(selectedRow,
											DefaultData.ZIP_INT));
					// EbazaarMainFrame.getInstance().getDesktop().add(shippingBillingWindow);
					shippingBillingWindow.setVisible(true);
				}

			} else {
				JOptionPane.showMessageDialog(shipAddressesWindow,
						ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class CancelShipAddrListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			shippingBillingWindow.setVisible(true);
			shipAddressesWindow.dispose();
		}
	}

	// // control PaymentWindow
	class ProceedFromPaymentListener implements ActionListener {
		ICustomerSubsystem cust = (ICustomerSubsystem) SessionContext
				.getInstance().get(CustomerConstants.CUSTOMER);

		public void actionPerformed(ActionEvent evt) {
			paymentWindow.setVisible(false);

			// check rules
			if (false) {
				// display error message
			}
			// rules passed, proceed
			else {
				// create a credit card instance and set in shopping cart
				termsWindow = new TermsWindow();
				try {
					String termsText = extractTermsText();
					termsWindow.setTermsText(termsText);
					EbazaarMainFrame.getInstance().getDesktop().add(termsWindow);
					termsWindow.setVisible(true);
				}
				catch (ParseException e) {
					displayError(paymentWindow, e.getMessage());
					(new ApplicationCleanup()).cleanup();
					System.exit(0);
				}
			}
		}
	}

	class BackToCartFromPayListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			paymentWindow.setVisible(false);
			cartItemsWindow.setVisible(true);
		}

	}

	// // controlTermsWindow
	class AcceptTermsListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			finalOrderWindow = new FinalOrderWindow();
			EbazaarMainFrame.getInstance().getDesktop().add(finalOrderWindow);
			finalOrderWindow.setVisible(true);
			termsWindow.dispose();

		}

	}

	// // control FinalOrderWindow
	class SubmitFinalOrderListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			finalOrderWindow.setVisible(false);

				try {
					String msg = extractGoodbyeMessage();

					JOptionPane.showMessageDialog(finalOrderWindow, msg,
							"E-Bazaar: Thank You", JOptionPane.PLAIN_MESSAGE);
				} catch(ParseException e) {
					LOG.severe("Unable to extract goodbye message: " + e.getMessage());
				}
			
		}

	}

	class CancelFinalOrderListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			finalOrderWindow.setVisible(false);
			if (cartItemsWindow != null) {
				cartItemsWindow.setVisible(true);
			}
		}
	}

	// /////// PUBLIC INTERFACE -- for getting instances of listeners ///

	// CartItemsWindow
	public ActionListener getProceedToCheckoutListener(CartItemsWindow w) {
		return (new ProceedToCheckoutListener());
	}

	// ShippingBillingWindow
	public ActionListener getSelectShipButtonListener(ShippingBillingWindow w) {
		return (new SelectShipButtonListener());
	}

	public ActionListener getProceedFromBillingCheckoutListener(
			ShippingBillingWindow w) {
		return (new ProceedFromBillingCheckoutListener());
	}

	public ActionListener getBackToCartButtonListener(ShippingBillingWindow w) {
		return (new BackToCartButtonListener());
	}

	// ShipAddressesWindow

	public ActionListener getSelectAddressesListener(ShipAddressesWindow w) {
		return (new SelectAddressesListener());
	}

	public ActionListener getCancelShipAddrListener(ShipAddressesWindow w) {
		return (new CancelShipAddrListener());
	}

	// PaymentWindow

	public ActionListener getProceedFromPaymentListener(PaymentWindow w) {
		return (new ProceedFromPaymentListener());
	}

	public ActionListener getBackToCartFromPayListener(PaymentWindow w) {
		return (new BackToCartFromPayListener());
	}

	// TermsWindow
	public ActionListener getAcceptTermsListener(TermsWindow w) {
		return (new AcceptTermsListener());
	}

	// FinalOrderWindow
	public ActionListener getSubmitFinalOrderListener(FinalOrderWindow w) {
		return (new SubmitFinalOrderListener());
	}

	public ActionListener getCancelFinalOrderListener(FinalOrderWindow w) {
		return (new CancelFinalOrderListener());
	}

	// ////// PUBLIC ACCESSORS to register screens controlled by this class////

	public void setCartItemsWindow(CartItemsWindow w) {
		cartItemsWindow = w;
	}

	public void setShippingBillingWindow(ShippingBillingWindow w) {
		shippingBillingWindow = w;
	}

	public void setShipAddressesWindow(ShipAddressesWindow w) {
		shipAddressesWindow = w;
	}

	public void setPaymentWindow(PaymentWindow w) {
		paymentWindow = w;
	}

	public void setTermsWindow(TermsWindow w) {
		termsWindow = w;
	}

	public void setFinalOrderWindow(FinalOrderWindow w) {
		finalOrderWindow = w;
	}

	// ///// screens -- private references
	private CartItemsWindow cartItemsWindow;
	private ShippingBillingWindow shippingBillingWindow;
	private ShipAddressesWindow shipAddressesWindow;
	private PaymentWindow paymentWindow;
	private TermsWindow termsWindow;
	private FinalOrderWindow finalOrderWindow;
	private Window[] allWindows = {};
	private JInternalFrame[] internalFrames = { cartItemsWindow,
			shipAddressesWindow, shippingBillingWindow, cartItemsWindow,
			paymentWindow, termsWindow, finalOrderWindow };

	public void cleanUp() {
		ApplicationUtil.cleanup(allWindows);
		ApplicationUtil.cleanup(internalFrames);
	}

	void displayError(Component w, String msg) {
		JOptionPane.showMessageDialog(w, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	// ///// make this class a singleton
	private static CheckoutController instance = new CheckoutController();

	public static CheckoutController getInstance() {

		return instance;
	}

	private CheckoutController() {
	}

}
