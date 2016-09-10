package application.gui; 

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;

import business.externalinterfaces.CustomerConstants;

import application.GuiUtil;
import application.IComboObserver;
import application.ManageProductsController;

/**
 * Class Description: This class displays all available products
 * for a particular catalog group. When a catalog group is selected,
 * the table is updated to display the products in this group. 
 * The screen provides Add, Edit and Delete buttons for modifying
 * the choices of products.
 */
public class MaintainProductCatalog extends JInternalFrame implements ParentWindow, IComboObserver {
	private String DEFAULT_CATALOG = "Books";
	ManageProductsController control;
	private Component parent;
	CustomTableModel model;
	JTable table;
	JScrollPane tablePane;
	
	//JPanels
	JPanel mainPanel;
	JPanel upper, middle, comboPanel, lower;
	
	//widgets
	JComboBox catalogTypeCombo;	
	
	//catalog type (books, clothes, etc); set default to Books
	String catalogGroup=DEFAULT_CATALOG;
	
	//constants
	private final boolean USE_DEFAULT_DATA = true;

    private final String NAME = "Name";
    private final String PRICE = "Unit Price";
    private final String MFG_DATE = "Mfg. Date";
    private final String QUANTITIES = "Quantities";
    
    private static final String MAIN_LABEL = "Maintain Product Catalog";
    
    //widget labels
    private final String CAT_GROUPS = "Catalog Groups";
    private final String ADD_BUTN = "Add";
    private final String EDIT_BUTN = "Edit";
    private final String DELETE_BUTN = "Delete";
    private final String SEARCH_BUTN = "Search";
    private final String BACK_TO_MAIN = "Back to Main";
    
    
    //table config
	private final String[] DEFAULT_COLUMN_HEADERS = {NAME,PRICE,MFG_DATE,QUANTITIES};
	private final int TABLE_WIDTH = GuiUtil.SCREEN_WIDTH;
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);

    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{0.4f, 0.2f, 0.2f, 0.2f};

    	
    	
	public MaintainProductCatalog() {
		super(MAIN_LABEL,false,true,false,true);
	    control = ManageProductsController.getInstance();
	    control.setMaintainProductCatalog(this);		
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
	}
	public String getCatalogGroup(){
		return catalogGroup;
	}
	public JTable getTable() {
		return table;
		
	}
	public CustomTableModel getModel() {
		return model;
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
	

	//middle -- table and combo box
	public void defineMiddlePanel(){
		
		middle = new JPanel();
		middle.setLayout(new BorderLayout());
		
		defineComboPanel();
		middle.add(comboPanel,BorderLayout.NORTH);
		
		//table
		createTableAndTablePane();
		GuiUtil.createCustomColumns(table, 
		                               TABLE_WIDTH,
		                               COL_WIDTH_PROPORTIONS,
		                               DEFAULT_COLUMN_HEADERS);
		                   		
		middle.add(GuiUtil.createStandardTablePanePanel(table,tablePane),
				   BorderLayout.CENTER);
				
	}
	
	//upper middle -- the combo panel
	public void defineComboPanel() {
		comboPanel = new JPanel();
		comboPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//label
		JLabel comboLabel = new JLabel(CAT_GROUPS);
		comboPanel.add(comboLabel);
		
		//combo box
		catalogTypeCombo = new JComboBox();
		catalogTypeCombo.addItem(DefaultData.BOOKS);
		catalogTypeCombo.addItem(DefaultData.CLOTHES);
		//catalogTypeCombo.addActionListener(new ComboBoxListener());
		Action comboAction = control.getComboAction(this);
		comboAction.putValue(CustomerConstants.COMBO,this.getClass().getName() );
		//catalogTypeCombo.addActionListener(control.getComboActionListener());
		catalogTypeCombo.addActionListener(comboAction);
		comboPanel.add(catalogTypeCombo);

		
	}	
	
	//buttons
	public void defineLowerPanel(){
		//add button
		JButton addButton = new JButton(ADD_BUTN);
		addButton.addActionListener(control.getAddProductListener(this));
		
		
		//edit button
		JButton editButton = new JButton(EDIT_BUTN);
		editButton.addActionListener(control.getEditProductListener(this));
		
		//delete button
		JButton deleteButton = new JButton(DELETE_BUTN);
		deleteButton.addActionListener(control.getDeleteProductListener(this));
		
		//search button
		JButton searchButton = new JButton(SEARCH_BUTN);
		searchButton.addActionListener(control.getSearchProductListener(this));
		searchButton.setEnabled(false);
		
		//exit button
		JButton backToMainButton = new JButton(BACK_TO_MAIN);
		backToMainButton.addActionListener(control.getBackToMainFromProdsListener(this));		
		
		//create lower panel
		JButton [] buttons = {addButton,editButton,deleteButton,searchButton,backToMainButton};
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
		//if(model==null){
			model = new CustomTableModel();
		//}
		model.setTableValues(list);	
		if(table != null) updateTable();
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
			theData = dd.getProductCatalogChoices(catalogGroup);
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
	
	//data for Listeners
	
	final String ERROR_MESSAGE = "Please select a row.";
	final String ERROR = "Error";
	/*
	class ComboBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			catalogGroup = (String)catalogTypeCombo.getSelectedItem();
			updateModel();
			updateTable();
		}
	}*/
	
	public static void main(String[] args) {
		(new MaintainProductCatalog()).setVisible(true);
	}	
	private static final long serialVersionUID = 3257569511937880631L;



	public void setCatalogGroup(String catalogGroup) {
		this.catalogGroup = catalogGroup;
	}
	public void refreshData() {
		//updateComboBox();
		updateTable();
		repaint();
	}
	private void updateComboBox() {
		catalogTypeCombo.setSelectedItem(catalogGroup);
	}
	
	
	
}
