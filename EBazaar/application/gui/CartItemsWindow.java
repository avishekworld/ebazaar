package application.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;

import application.BrowseAndSelectController;
import application.CheckoutController;
import application.GuiUtil;

/**
 * Class Description: This window represents a user's shopping cart. 
 * Students:  See the readdata method for where data is put into the table.
 */
public class CartItemsWindow extends JInternalFrame implements ParentWindow {
	private Component parent;
	CustomTableModel model;
	JTable table;
	JScrollPane tablePane;
	JLabel totalValue;
	
	//controllers
    private BrowseAndSelectController browseControl;
    private CheckoutController checkoutControl;
    
	//JPanels
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	//constants
	private final boolean USE_DEFAULT_DATA = true;

    private final String GRANDTOTAL = "Total Price: $";
    private final String EDIT_ITEM = "Edit";
    private final String DELETE_ITEM = "Delete";
	
    private final String ITEM = "Item";
    private final String QUANTITY = "Quantity";
    private final String UNIT_PRICE = "Unit Price";
    private final String TOTAL = "Total Price";
    private final static String MAIN_LABEL = "Cart Items";
    
    //button labels
    private final String PROCEED_BUTN = "Proceed To Checkout";
    private final String CONTINUE = "Continue Shopping";
    private final String SAVE_CART = "Save Cart";
    
    //table data and config
	private final String[] DEFAULT_COLUMN_HEADERS = {ITEM,QUANTITY,UNIT_PRICE,TOTAL};
	private final int TABLE_WIDTH = Math.round(0.75f*GuiUtil.SCREEN_WIDTH);
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);

    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{0.4f, 0.2f, 0.2f, 0.2f};

    	
    	
	public CartItemsWindow() {
		super(MAIN_LABEL,false,true,false,true);
	    browseControl = BrowseAndSelectController.getInstance();
	    browseControl.setCartItemsWindow(this);
	    checkoutControl =  CheckoutController.getInstance();
	    checkoutControl.setCartItemsWindow(this);
		//build gui
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);		
	}
	private void initializeWindow() {
		
		setSize(GuiUtil.SCREEN_WIDTH,GuiUtil.SCREEN_HEIGHT + 22);		
		GuiUtil.centerFrameOnDesktop(this);
		
	}
	
	private void defineMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(GuiUtil.FILLER_COLOR);
		mainPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));
		defineUpperPanel();
		defineMiddlePanel();
		defineLowerPanel();
		mainPanel.add(upper,BorderLayout.NORTH);
		mainPanel.add(middle,BorderLayout.CENTER);
		mainPanel.add(lower,BorderLayout.SOUTH);
			
	}
	//label
	public void defineUpperPanel(){
		upper = new JPanel();
		upper.setBackground(GuiUtil.FILLER_COLOR);
		upper.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel mainLabel = new JLabel(MAIN_LABEL);
		Font f = GuiUtil.makeVeryLargeFont(mainLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		mainLabel.setFont(f);
		upper.add(mainLabel);					
	}
	//table
	public void defineMiddlePanel(){
		createTableAndTablePane();
		GuiUtil.createCustomColumns(table, 
		                               TABLE_WIDTH,
		                               COL_WIDTH_PROPORTIONS,
		                               DEFAULT_COLUMN_HEADERS);
		                   		
		middle = GuiUtil.createStandardTablePanePanel(table,tablePane);
		
		JPanel totalPanel = new JPanel();
		totalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		totalPanel.setBackground(GuiUtil.FILLER_COLOR);
		JLabel totalLabel = new JLabel(GRANDTOTAL);
		 totalValue = new JLabel(GRANDTOTAL);

	
		//totalValue.setForeground(GuiControl.DARK_BLUE);
		
		//middle.setLayout(new BorderLayout());
		totalPanel.add(totalLabel);
		totalPanel.add(totalValue);
		
		// you may want to have an edit button for Cart Items
		JButton editButton = new JButton(EDIT_ITEM,new ImageIcon(GuiUtil.BTN_EDIT));
	
		// and a delete button
		JButton deleteButton = new JButton(DELETE_ITEM,new ImageIcon(GuiUtil.BTN_DELETE));
		// you'll need to add listeners to these
		
		JButton [] buttons = {editButton, deleteButton};
		JPanel buttonsPanel = GuiUtil.createStandardButtonPanel(buttons);
		
		middle.add(buttonsPanel, BorderLayout.WEST);
		middle.add(totalPanel, BorderLayout.LINE_END);
				
	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton proceedButton = new JButton(PROCEED_BUTN,new ImageIcon(GuiUtil.BTN_BASKET));
		proceedButton.addActionListener(checkoutControl.getProceedToCheckoutListener(this));
		
		
		//continue button
		JButton continueButton = new JButton(CONTINUE,new ImageIcon(GuiUtil.BTN_CONTINUE));
		continueButton.addActionListener(browseControl.getContinueShoppingListener(this));
		
		//save button
		JButton saveButton = new JButton(SAVE_CART,new ImageIcon(GuiUtil.BTN_SAVE));
		saveButton.addActionListener(browseControl.getSaveCartListener(this));
		
		
		//create lower panel
		JButton [] buttons = {proceedButton,continueButton,saveButton};
		lower = GuiUtil.createStandardButtonPanel(buttons);		
	}
	
	private void createTableAndTablePane() {
		updateModel();
		table = new JTable(model);
		tablePane = new JScrollPane();
		tablePane.setPreferredSize(new Dimension(TABLE_WIDTH, DEFAULT_TABLE_HEIGHT));
		tablePane.getViewport().add(table);
		
	}
	public void updateModel(List<String[]> list){
		if(model==null){
			model = new CustomTableModel();
		}
  		model.setTableValues(list);		
	}
	
	/**
	 * If default data is being used, this method obtains it
	 * and then passes it to updateModel(List). If real data is
	 * being used, the public updateModel(List) should be called by
	 * the controller class.
	 */
	private void updateModel() {
		List<String[]> theData = new ArrayList<String[]>();
        if(USE_DEFAULT_DATA) {
        	DefaultData data = DefaultData.getInstance();			        	
			theData = data.getCartItemsData();
			
        }
		updateModel(theData);
 
	}

    private void updateTable() {
        
        table.setModel(model);
        table.updateUI();
        repaint();
        
    }

    public void setTotal(String val){
    	System.out.println("Value of val? " + val);
		this.totalValue.setText(val);
		
	}
    
    public JTable getTable() {
    	return table;
    }
	
	public void setParentWindow(Component parentWindow) {
		parent = parentWindow;
	}
	
	public Component getParentWindow() {
		return parent;
	}
	
	
	public static void main(String[] args) {
		(new CartItemsWindow()).setVisible(true);
	}	
	private static final long serialVersionUID = 1L;	
}
