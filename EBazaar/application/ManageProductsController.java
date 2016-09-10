package application;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import middleware.DatabaseException;

import application.gui.AddEditCatalog;
import application.gui.AddEditProduct;
import application.gui.CustomTableModel;
import application.gui.DefaultData;
import application.gui.EbazaarMainFrame;
import application.gui.MaintainCatalogTypes;
import application.gui.MaintainProductCatalog;
import business.SessionContext;
import business.externalinterfaces.CustomerConstants;
import business.externalinterfaces.IProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;

public class ManageProductsController implements CleanupControl {

	// ///////// EVENT HANDLERS -- new code goes here ////////////

	// // control MaintainCatalogTYpes
	class AddCatalogListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			addEditCatalog = new AddEditCatalog(GuiUtil.ADD_NEW, null);
			maintainCatalogTypes.setVisible(false);
			mainFrame.getDesktop().add(addEditCatalog);
			addEditCatalog.setVisible(true);
		}
	}

	class EditCatalogListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";

		public void actionPerformed(ActionEvent evt) {
			JTable table = maintainCatalogTypes.getTable();
			CustomTableModel model = maintainCatalogTypes.getModel();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				String selectedType = (String) model.getValueAt(selectedRow, 0);
				maintainCatalogTypes.setVisible(false);
				AddEditCatalog editType = new AddEditCatalog(GuiUtil.EDIT,
						selectedType);
				mainFrame.getDesktop().add(editType);
				editType.setVisible(true);

			} else {
				JOptionPane.showMessageDialog(maintainCatalogTypes,
						ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class DeleteCatalogListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";

		public void actionPerformed(ActionEvent evt) {
			JTable table = maintainCatalogTypes.getTable();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				// Students: code goes here.
				JOptionPane.showMessageDialog(maintainCatalogTypes,
						"Need to write code for this!", "Information",
						JOptionPane.INFORMATION_MESSAGE);

			} else {
				JOptionPane.showMessageDialog(maintainCatalogTypes,
						ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class MaintainProductActionListener implements ActionListener, Controller {
		/*
		 * this method is called when LoginControl needs this class to load
		 * products
		 */
		public void doUpdate() {

			// implement by requesting product catalog for selected
			// catalogtype from Product Subsystem

		}

		public void actionPerformed(ActionEvent e) {
			SessionContext ctx = SessionContext.getInstance();
			Boolean loggedIn = (Boolean) ctx.get(CustomerConstants.LOGGED_IN);
			if (maintainProductCatalog == null) {
				maintainProductCatalog = new MaintainProductCatalog();
			}
			if (!loggedIn.booleanValue()) {

				LoginControl loginControl = new LoginControl(
						maintainProductCatalog, mainFrame, this);
				loginControl.startLogin();
				System.out.println("hello");
			} else {

				mainFrame.getDesktop().add(maintainProductCatalog);
				maintainProductCatalog.setVisible(true);
			}
		}
	}

	class MaintainCatalogTypesActionListener implements ActionListener,
			Controller {
		/*
		 * this method is called when LoginControl needs this class to load
		 * catalogs
		 */
		public void doUpdate() {	
			// implement by requesting catalog list from Product Subsystem	
		}

		public void actionPerformed(ActionEvent e) {
			SessionContext ctx = SessionContext.getInstance();
			Boolean loggedIn = (Boolean) ctx.get(CustomerConstants.LOGGED_IN);
			if (!loggedIn.booleanValue()) {
				maintainCatalogTypes = new MaintainCatalogTypes();
				LoginControl loginControl = new LoginControl(
						maintainCatalogTypes, mainFrame, this);
				loginControl.startLogin();
			} else {
				maintainCatalogTypes = new MaintainCatalogTypes();
				mainFrame.getDesktop().add(maintainCatalogTypes);
				maintainCatalogTypes.setVisible(true);

			}
		}
	}

	class BackToMainListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			BrowseAndSelectController.getInstance().makeMainFrameVisible();
			maintainCatalogTypes.dispose();
		}
	}

	// // control MaintainProductCatalog
	class AddProductListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {

			// no field values need to be passed into AddEditProduct when adding
			// a new product
			// so we create an empty Properties instance
			Properties emptyProductInfo = new Properties();

			String catalogType = maintainProductCatalog.getCatalogGroup();
			addEditProduct = new AddEditProduct(GuiUtil.ADD_NEW, catalogType,
					emptyProductInfo);
			maintainProductCatalog.setVisible(false);
			mainFrame.getDesktop().add(addEditProduct);
			addEditProduct.setVisible(true);

		}

	}

	class EditProductListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";

		public void actionPerformed(ActionEvent evt) {
			JTable table = maintainProductCatalog.getTable();
			CustomTableModel model = maintainProductCatalog.getModel();
			String catalogType = maintainProductCatalog.getCatalogGroup();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				String[] fldNames = DefaultData.FIELD_NAMES;
				Properties productInfo = new Properties();

				// index for Product Name
				int columnIndex = DefaultData.PRODUCT_NAME_INT;
				productInfo.setProperty(fldNames[columnIndex],
						(String) model.getValueAt(selectedRow, columnIndex));

				// index for Price Per Unit
				columnIndex = DefaultData.PRICE_PER_UNIT_INT;
				productInfo.setProperty(fldNames[columnIndex],
						(String) model.getValueAt(selectedRow, columnIndex));

				// index for Mfg Date
				columnIndex = DefaultData.MFG_DATE_INT;
				productInfo.setProperty(fldNames[columnIndex],
						(String) model.getValueAt(selectedRow, columnIndex));

				// index for Quantity
				columnIndex = DefaultData.QUANTITY_INT;
				productInfo.setProperty(fldNames[columnIndex],
						(String) model.getValueAt(selectedRow, columnIndex));

				AddEditProduct editProd = new AddEditProduct(GuiUtil.EDIT,
						catalogType, productInfo);
				mainFrame.getDesktop().add(editProd);
				editProd.setVisible(true);

			} else {
				JOptionPane.showMessageDialog(maintainProductCatalog,
						ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class DeleteProductListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";

		public void actionPerformed(ActionEvent evt) {
			JTable table = maintainProductCatalog.getTable();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				// Students: code goes here.

				JOptionPane.showMessageDialog(maintainProductCatalog,
						"Need to write code for this!", "Information",
						JOptionPane.INFORMATION_MESSAGE);

			} else {
				JOptionPane.showMessageDialog(maintainProductCatalog,
						ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class SearchProductListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			// Students: code goes here

		}
	}

	class BackToMainFromProdsListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			BrowseAndSelectController.getInstance().makeMainFrameVisible();
			maintainProductCatalog.dispose();
		}
	}

	// control AddEditCatalog
	class SaveAddEditCatListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JOptionPane.showMessageDialog(addEditCatalog,
					"Need to write code for this!", "Information",
					JOptionPane.INFORMATION_MESSAGE);

		}
	}

	/**
	 * Returns the user to the previous screen
	 */
	class BackFromAddEditCatListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {

			maintainCatalogTypes.setVisible(true);
			addEditCatalog.dispose();

		}
	}

	// // control AddEditProduct

	class SaveAddEditProductListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JOptionPane.showMessageDialog(addEditProduct,
					"Need to write code for this!", "Information",
					JOptionPane.INFORMATION_MESSAGE);

		}
	}

	class BackFromAddEditProductListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {

			maintainProductCatalog.setVisible(true);
			addEditProduct.dispose();

		}
	}

	static class ComboAction extends AbstractAction {
		private static final long serialVersionUID = -6362862518012806665L;
		private static final ComboAction INSTANCE = new ComboAction();
		IComboObserver[] observers = { maintainProductCatalog, addEditProduct };

		public void actionPerformed(ActionEvent evt) {
			// if(mainFrame != null) mainFrame.setVisible(false);
			String selectedValue = (String) ((JComboBox) evt.getSource())
					.getSelectedItem();
			IProductSubsystem prodSS = new ProductSubsystemFacade();
			
			//IMPLEMENT
			/* will work when ProductSubsystemFacade is completed
			List<String[]> associatedProducts = business.util.ProductUtil
					.extractProductInfoForManager(prodSS
							.getProductList(selectedValue));*/
			List<String[]> associatedProducts = new ArrayList<String[]>();
			for (IComboObserver o : observers) {
				if (o != null) {
					o.setCatalogGroup(selectedValue);
					o.refreshData();
				}
			}
			if (maintainProductCatalog != null) {
				maintainProductCatalog.updateModel(associatedProducts);
				maintainProductCatalog.repaint();
			}
		
		}

	}

	// /////// PUBLIC INTERFACE -- for getting instances of listeners ///
	// mainFrame
	public ActionListener getMaintainProductActionListener(EbazaarMainFrame w) {
		return (new MaintainProductActionListener());
	}

	public ActionListener getMaintainCatalogTypesActionListener(
			EbazaarMainFrame w) {
		return (new MaintainCatalogTypesActionListener());
	}

	// MaintainCatalogTypes
	public ActionListener getAddCatalogListener(MaintainCatalogTypes w) {
		return (new AddCatalogListener());
	}

	public ActionListener getEditCatalogListener(MaintainCatalogTypes w) {
		return (new EditCatalogListener());
	}

	public ActionListener getDeleteCatalogListener(MaintainCatalogTypes w) {
		return (new DeleteCatalogListener());
	}

	public ActionListener getBackToMainListener(MaintainCatalogTypes w) {
		return (new BackToMainListener());
	}

	// MaintainProductCatalog
	public ActionListener getAddProductListener(MaintainProductCatalog w) {
		return (new AddProductListener());
	}

	public ActionListener getEditProductListener(MaintainProductCatalog w) {
		return (new EditProductListener());
	}

	public ActionListener getDeleteProductListener(MaintainProductCatalog w) {
		return (new DeleteProductListener());
	}

	public ActionListener getSearchProductListener(MaintainProductCatalog w) {
		return (new SearchProductListener());
	}

	public ActionListener getBackToMainFromProdsListener(
			MaintainProductCatalog w) {
		return (new BackToMainFromProdsListener());
	}

	public Action getComboAction(Component c) {
		return c == null ? null : ComboAction.INSTANCE;
	}

	// AddEditCatalog
	public ActionListener getSaveAddEditCatListener(AddEditCatalog w) {
		return (new SaveAddEditCatListener());
	}

	public ActionListener getBackFromAddEditCatListener(AddEditCatalog w) {
		return (new BackFromAddEditCatListener());
	}

	// AddEditProduct
	public ActionListener getSaveAddEditProductListener(AddEditProduct w) {
		return (new SaveAddEditProductListener());
	}

	public ActionListener getBackFromAddEditProductListener(AddEditProduct w) {
		return (new BackFromAddEditProductListener());
	}

	// ////// PUBLIC ACCESSORS to register screens controlled by this class////
	public void setMaintainCatalogTypes(MaintainCatalogTypes w) {
		maintainCatalogTypes = w;
	}

	public void setMaintainProductCatalog(MaintainProductCatalog w) {
		maintainProductCatalog = w;
	}

	public void setAddEditCatalog(AddEditCatalog w) {
		addEditCatalog = w;
	}

	public void setAddEditProduct(AddEditProduct w) {
		addEditProduct = w;
	}

	public void setMainFrame(EbazaarMainFrame f) {
		mainFrame = f;
	}

	// ///// screens -- private references
	private static MaintainCatalogTypes maintainCatalogTypes;
	private static MaintainProductCatalog maintainProductCatalog;
	private static AddEditCatalog addEditCatalog;
	private static AddEditProduct addEditProduct;
	private static EbazaarMainFrame mainFrame;
	private Window[] allWindows = { mainFrame };

	private JInternalFrame[] allInternalFrames = { maintainCatalogTypes,
			maintainProductCatalog, addEditCatalog, addEditProduct };

	public void cleanUp() {
		ApplicationUtil.cleanup(allWindows);
		ApplicationUtil.cleanup(allInternalFrames);

	}

	// ///// make this class a singleton
	private static ManageProductsController instance = new ManageProductsController();

	public static ManageProductsController getInstance() {
		return instance;
	}

	private ManageProductsController() {
	}

}
