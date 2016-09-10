package application.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import application.ApplicationCleanup;
import application.BrowseAndSelectController;
import application.GuiUtil;
import application.ManageProductsController;
import application.ViewOrdersController;
/**

 * Class Description: This is the entry point into the E-Bazaar application.
 * It is implemented as a JFrame and provides a splash screen. Navigation
 * is by way of a menu bar. This class represents the starting point
 * for all use cases. <p>
 */
public class EbazaarMainFrame extends javax.swing.JFrame implements ParentWindow {
	private BrowseAndSelectController control;
	private ViewOrdersController orderControl;
    private ManageProductsController prodControl;
    private static EbazaarMainFrame mainframe;
    
	//ebazaar application title
	private final String EBAZAAR_APP_NAME = "Ebazaar Online Shopping Application";
	private final JLabel FOOTER = 
		new JLabel("Copyright © 2012 MUM. All Rights Reserved. ", JLabel.CENTER);
	
	//menu item names
	private final String LOGIN = "Login";
	private final String CUSTOMER = "Customer";
	private final String ADMINISTRATOR = "Administrator";
	private final String ONLINE_PURCHASE = "Online Purchase";
	private final String REVIEW_ORDERS = "Review Orders";
	private final String EXIT = "Exit";
	private final String RETRIEVE_CART = "Retrieve Saved Cart";
	private final String MAINTAIN_CATALOGUE = "Maintain Product Catalog";
	private final String MAINTAIN_CAT_TYPES = "Maintain Catalog Types";
	
	//menu button bar
	private final String FILE = "File";
	private final String HELP = "Help";
	private final String ABOUT = "About EBazaar";
	
	// for sizing outer screen
	Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
	
	// inner pane to hold internal windows
	JDesktopPane desktop;
	
	// splash screen setup
	SplashScreen splashWindow = new SplashScreen();
	Thread splashThread = new Thread(splashWindow);
	
	JPanel mainPanel;
	JMenuBar menuBar;
	JMenu menuCustomer, menuAdministrator,menuFile,menuHelp;
    JMenuItem menuItemLogin, menuItemPurchaseOnline, menuItemMaintainProduct, 
      menuItemMaintainCatalogTypes,menuItemExit, menuItemRevOrders,menuItemAbout,
      menuItemRetrieveSavedCart;  

    
	public EbazaarMainFrame() {
		//controllers
	    control = BrowseAndSelectController.getInstance();
	    control.setMainFrame(this);
	    orderControl = ViewOrdersController.getInstance();
	    orderControl.setMainFrame(this);
        prodControl = ManageProductsController.getInstance();
        prodControl.setMainFrame(this);		
        
		//gui
		setLookAndFeel();
		handleWindowClosing();
		setTitle(EBAZAAR_APP_NAME);
		//forces framing window to be maximized
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//sets icon that shows in tray
		setIconImage(new ImageIcon("images/BASKET.png").getImage());
		
		//starting position of upper left corner
		setLocation(0,0);
		
		//dimension is full screen dimension
		setSize(screenDimension);
		setResizable(true);

		//sets up a desktop pane for adding internal windows
		formatContentPane();		
		setBackgroundColor();
		
		//no longer a need for this
		//insertLogo();
		
		createMenus();	
		
		//do not pack() -- this will override frame size selection
		
		GuiUtil.centerFrameOnDesktop(this);
		
		//show splash window for a few seconds
		loadSplashScreen();
		
		//dispose of splash screen -- no longer needed
		splashWindow.dispose();	
		setVisible(true);
	}
	
	
	private void setLookAndFeel() {
		try {
			//Alternative look and feel: getSystemLookAndFeelClassName());		
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception e) {}
	}
	
	private void formatContentPane() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,1));
	   
		
		desktop = new JDesktopPane();
		desktop.setBackground(Color.LIGHT_GRAY);
		desktop.setBorder(BorderFactory.createEmptyBorder());
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
			
		mainPanel.setBackground(Color.LIGHT_GRAY);
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		mainPanel.add(new JScrollPane(desktop));//,BorderLayout.CENTER);
		
		getContentPane().add(mainPanel);	
		getContentPane().add(FOOTER,BorderLayout.PAGE_END);
	}
	
	private void insertLogo() {
		ImageIcon image = new ImageIcon(GuiUtil.SPLASH_IMAGE);
		
		mainPanel.add(new JLabel(image));	
	}
	
	/** 
	 * To switch off background color, and to use default instead, comment
	 * out the reference to this method
	 */
	private void setBackgroundColor() {
		mainPanel.setBackground(GuiUtil.MAIN_SCREEN_COLOR);
	}
	
	private void createMenus() {
		menuBar = new JMenuBar();
		//menuBar.setBorder(BorderFactory.createRaisedBevelBorder());
		addMenuItems(menuBar);
		setJMenuBar(menuBar);	
		getContentPane().add(createJToolBar(), BorderLayout.WEST);		
	}


	static public void main(String args[]) {
	
	    mainframe = new EbazaarMainFrame();

	}
	static public EbazaarMainFrame getInstance(){
		if(mainframe == null) {
			mainframe = new EbazaarMainFrame();
        }
       
		return mainframe;	
		
	
	}

	protected JToolBar createJToolBar (){
		JToolBar newJToolBar = new JToolBar(JToolBar.VERTICAL);

		newJToolBar.setMargin(new Insets(0,0,0,0));
		
		//Create a toolbar buttons
		newJToolBar.add(createJToolbarButton("Login",GuiUtil.BTN_LOGIN,control.getLoginListener(this)));
		newJToolBar.addSeparator();
		
		newJToolBar.add(createJToolbarButton("Browse Products",GuiUtil.BTN_BROWSE,
				control.getNewOnlinePurchaseListener(this)));
		newJToolBar.add(createJToolbarButton("View Order History",GuiUtil.BTN_ORDER,
				orderControl.getSelectOrderActionListener(this)));
		newJToolBar.add(createJToolbarButton("Retrieve Shopping Cart",GuiUtil.BTN_CART,
				control.getRetrieveCartActionListener(this)));
		newJToolBar.addSeparator();
		newJToolBar.add(createJToolbarButton("Maintain Products",GuiUtil.BTN_PRODUCT,
				prodControl.getMaintainProductActionListener(this)));
		newJToolBar.add(createJToolbarButton("Maintain Catalogues",GuiUtil.BTN_CATALOGS,
				prodControl.getMaintainCatalogTypesActionListener(this)));
		newJToolBar.addSeparator();
		
		return newJToolBar;
	}
	//End create a tool bar

	protected JButton createJToolbarButton(String srcToolTipText,String srcImageLocation,ActionListener srcActionCommand){
		JButton newJButton = new JButton(new ImageIcon(srcImageLocation));

		
		newJButton.setToolTipText(srcToolTipText);
		newJButton.addActionListener(srcActionCommand);

		return newJButton;
	}
	
	void addMenuItems(JMenuBar menuBar) {
		//these instructions belong somewhere...
	   //2)add the following 6 lines to the AddMenuItems method of EbazaarMainFrame 
		//(before the exit menu item is added to the menu)		
    
       //create and add the menus to the menu bar
		menuFile = new JMenu(FILE);  
		menuCustomer = new JMenu(CUSTOMER);  
	   menuAdministrator = new JMenu(ADMINISTRATOR);
	   menuHelp = new JMenu(HELP);
	   menuBar.add(new JMenu(""));
	   menuBar.add(menuFile);
	   menuBar.add(menuCustomer);
	   menuBar.add(menuAdministrator);
	   menuBar.add(menuHelp);
       //login menu item
       menuItemLogin = new JMenuItem(LOGIN);    
       menuItemLogin.addActionListener(control.getLoginListener(this));
       menuItemLogin.setIcon(new ImageIcon("images/SECURITY.png"));
       menuFile.add(menuItemLogin);
       menuFile.addSeparator();
//     exit menu item
	   menuItemExit = new JMenuItem(EXIT);
	   menuItemExit.addActionListener(new ExitButtonListener(this));
	   menuItemExit.setIcon(new ImageIcon("images/exit.png"));
	   menuFile.add(menuItemExit);
	   //purchase online menu item     
	   menuItemPurchaseOnline = new JMenuItem(ONLINE_PURCHASE);
	   menuItemPurchaseOnline.addActionListener(control.getNewOnlinePurchaseListener(this));
	   menuItemPurchaseOnline.setIcon(new ImageIcon("images/newexpense.png"));
	   menuCustomer.add(menuItemPurchaseOnline);
	   menuCustomer.addSeparator();
	   //review orders menu item
	   menuItemRevOrders = new JMenuItem(REVIEW_ORDERS);
	   menuItemRevOrders.addActionListener(orderControl.getSelectOrderActionListener(this));	   
	   menuItemRevOrders.setIcon(new ImageIcon("images/INVOICE.png"));
	   menuCustomer.add(menuItemRevOrders);
	   menuCustomer.addSeparator();
	   //retrieve saved cart item
	   menuItemRetrieveSavedCart = new JMenuItem(RETRIEVE_CART);	   
	   menuItemRetrieveSavedCart.addActionListener(control.getRetrieveCartActionListener(this)); 
	   menuItemRetrieveSavedCart.setIcon(new ImageIcon("images/newexpense.png"));
	   menuCustomer.add(menuItemRetrieveSavedCart);	    
	   
	   
	   
	   
	   
	   //maintain catalogue menu item
	   menuItemMaintainProduct = new JMenuItem(MAINTAIN_CATALOGUE);
	   menuItemMaintainProduct.addActionListener(prodControl.getMaintainProductActionListener(this));
	   menuItemMaintainProduct.setIcon(new ImageIcon("images/adjustment.png"));
	   menuAdministrator.add( menuItemMaintainProduct);
	   menuAdministrator.addSeparator();
	   //maintain catalogue type menu item
       menuItemMaintainCatalogTypes = new JMenuItem(MAINTAIN_CAT_TYPES);
	   menuItemMaintainCatalogTypes.addActionListener(prodControl.getMaintainCatalogTypesActionListener(this));
	   menuItemMaintainCatalogTypes.setIcon(new ImageIcon("images/categories.png"));
	   menuAdministrator.add( menuItemMaintainCatalogTypes);
//	 maintain catalogue type menu item
	   menuItemAbout = new JMenuItem(ABOUT);
	  // menuItemMaintainCatalogTypes.addActionListener(prodControl.getMaintainCatalogTypesActionListener(this));
	   menuItemAbout.setIcon(new ImageIcon("images/RESET.png"));
	   menuHelp.add(  menuItemAbout);

	}
	
	//method to load slash screen
	protected void loadSplashScreen(){
		//Start the thread
		splashThread.start();
		while(!splashWindow.isShowing()){
			try{
				//Display the FormSplash for 10 seconds
				Thread.sleep(5000);
			}catch(InterruptedException e){
			}
		}
	}
	/** This method makes sure that the frame is cleaned up and 
	 *  exits when the upper X is clicked 
	 */
	private void handleWindowClosing() {
        addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent w) {
                dispose();
				(new ApplicationCleanup()).cleanup();
                System.exit(0);
           }
        }); 		
		
	}	 
    //for delegation hierarchy, this is necessary:
    Window parentWindow = null;
    public void setParentWindow(Component w){
        parentWindow = null;
    }
    public Component getParentWindow() {
        return parentWindow;
        
    }
    public JDesktopPane getDesktop() {
    	return desktop;
    }
	private static final long serialVersionUID = 3618418228695610676L;
   
    
}