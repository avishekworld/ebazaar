package application.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JWindow;

import application.CheckoutController;
import application.GuiUtil;


/**
 * Class Description: This class displays a list of all the
 * customer's addresses on record (on initial creation, just
 * one address is displayed, from fake data in DefaultData).
 * This class is invoked by ShippingBillingWindow as a means
 * to fill in the shipping address on its screen.  
 */
public class ShipAddressesWindow extends JInternalFrame implements ParentWindow {
	CheckoutController control;
	private Component parent;
	
	CustomTableModel model;
	JTable table;
	JScrollPane tablePane;
	
	//JPanels
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	//constants
	private final boolean USE_DEFAULT_DATA = false;

    private final String STREET = "Street";
    private final String CITY = "City";
    private final String STATE = "State";
    private final String ZIP = "ZIP";
    private final static String MAIN_LABEL = "Shipping Addresses";
    
    //button labels
    private final String SELECT_BUTN = "Select";
    private final String CANCEL = "Cancel";
    
    
    //table config
	private final String[] DEFAULT_COLUMN_HEADERS = {STREET,CITY,STATE, ZIP};
	private final int TABLE_WIDTH = Math.round(0.75f*GuiUtil.SCREEN_WIDTH);
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);

    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{0.4f, 0.2f, 0.2f, 0.2f};

    	
    	
	public ShipAddressesWindow() {
		super(MAIN_LABEL,false,true,false,true);
		control = CheckoutController.getInstance();
		control.setShipAddressesWindow(this);
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
		
		
			
	}
	public JTable getTable(){
		return table;
	}
	public CustomTableModel getModel(){
		return model;
	}
	private void initializeWindow() {
		
		setSize(GuiUtil.SCREEN_WIDTH,
				Math.round(GuiUtil.SCREEN_HEIGHT));		
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
				
	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton selectButton = new JButton(SELECT_BUTN);
		selectButton.addActionListener(control.getSelectAddressesListener(this));
		
		
		//continue button
		JButton cancelButton = new JButton(CANCEL);
		cancelButton.addActionListener(control.getCancelShipAddrListener(this));
		
		
		//create lower panel
		JButton [] buttons = {selectButton,cancelButton};
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
		if(model == null) {
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
			DefaultData dd = DefaultData.getInstance();
			theData = dd.getShipAddresses();
        }
		updateModel(theData);
 	}	
	
	

    private void updateTable() {
        
        table.setModel(model);
        table.updateUI();
        repaint();
        
    }	
	
	public void setParentWindow(Component parentWindow) {
		parent = parentWindow;
	}
	
	public Component getParentWindow() {
		return parent;
	}
	
	
	final String ERROR_MESSAGE = "Please select a row.";
	final String ERROR = "Error";
	private static final long serialVersionUID = 3256442521008944436L;

}
