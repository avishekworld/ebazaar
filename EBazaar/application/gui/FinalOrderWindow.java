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

import application.CheckoutController;
import application.GuiUtil;

/**
 * Class Description: This window represents the user's final order.
 * It consists of a table that provide detailed information about
 * each of the user's selected products.
 */
public class FinalOrderWindow extends JInternalFrame implements ParentWindow {

    CheckoutController control;
	private Component parent;


	private static final String MAIN_LABEL = "Final Order";
	private final String SUBMIT_BUTN = "Submit Order";
	private final String CANCEL_BUTN = "Cancel";
	
	private CustomTableModel model;
	private JTable table;
	private JScrollPane tablePane;
	
	
	//table
	private final boolean USE_DEFAULT_DATA = false;
		
	private final String ITEM = "Item";
    private final String QUANTITY = "Quantity";
    private final String UNIT_PRICE = "Unit Price";
    private final String TOTAL = "Total Price";
	private final String[] DEFAULT_COLUMN_HEADERS = {ITEM,QUANTITY,UNIT_PRICE,TOTAL};
	private final int TABLE_WIDTH = Math.round(0.75f*GuiUtil.SCREEN_WIDTH);
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);
    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{0.4f, 0.2f, 0.2f, 0.2f};
 
	//JPanels
		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	/**
	 * Constructor - builds the gui.
	 * 
	 */
	public FinalOrderWindow() {
		super(MAIN_LABEL,false,true,false,true);
	    control = CheckoutController.getInstance();
	    control.setFinalOrderWindow(this);		
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
			
	}
	private void initializeWindow() {
		
		setSize(GuiUtil.SCREEN_WIDTH,
				GuiUtil.SCREEN_HEIGHT);		
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
		JButton submitButton = new JButton(SUBMIT_BUTN);
		submitButton.addActionListener(control.getSubmitFinalOrderListener(this));
		
		
		//back to cart button
		JButton cancelButton = new JButton(CANCEL_BUTN);
		cancelButton.addActionListener(control.getCancelFinalOrderListener(this));
		
		//create lower panel
		JButton [] buttons = {submitButton,cancelButton};
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
			theData = dd.getFinalOrderData();
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
		
		(new FinalOrderWindow()).setVisible(true);
	}

	private static final long serialVersionUID = 3906648600670122544L;
	
}
