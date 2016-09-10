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
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;

import application.GuiUtil;
import application.ViewOrdersController;

/**
 * Class Description: This class is used to display the details
 * of an order. It is invoked from SelectOrderWindow. The 
 * product name, unit price,
 * quantity,  and total price are displayed for the order.
 */
public class ViewOrderDetailsWindow extends JInternalFrame implements ParentWindow {
	private ViewOrdersController control;
	private Component parent;
	CustomTableModel model;
	JTable table;
	JScrollPane tablePane;
	
	//JPanels
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	//constants
	private final boolean USE_DEFAULT_DATA = false;

    private final String ITEM = "Product";
    private final String QUANTITY = "Quantity";
    private final String UNIT_PRICE = "Unit Price";
    private final String TOTAL = "Total Price";
    private final static String MAIN_LABEL = "Order Detail";
    
    //button labels
    private final String OK_BUTN = "OK";
    
    //table data and config
	private final String[] DEFAULT_COLUMN_HEADERS = {ITEM,QUANTITY,UNIT_PRICE,TOTAL};
	private final int TABLE_WIDTH = Math.round(0.75f*GuiUtil.SCREEN_WIDTH);
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);

    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{0.4f, 0.2f, 0.2f, 0.2f};

    	
    	
	public ViewOrderDetailsWindow() {
		super(MAIN_LABEL,false,true,false,true);
		control = ViewOrdersController.getInstance();
	    control.setViewOrderDetailsWindow(this);		
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
		
		
			
	}
	private void initializeWindow() {
		
		setSize(GuiUtil.SCREEN_WIDTH,GuiUtil.SCREEN_HEIGHT);		
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
		JButton okButton = new JButton(OK_BUTN);
		okButton.addActionListener(control.getOrderDetailsOkListener(this));
		
		//create lower panel
		JButton [] buttons = {okButton};
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
			theData = dd.getViewOrderDetailsDefaultData();
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

	public static void main(String[] args) {
		ViewOrderDetailsWindow w = new ViewOrderDetailsWindow();
		w.setVisible(true);
	}	

	private static final long serialVersionUID = 3258415049298031927L;
	
}
