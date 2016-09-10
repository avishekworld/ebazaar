package application.gui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import application.GuiUtil;
import application.ManageProductsController;

/**
 * Class Description: This class presents a table of all catalogs
 * available in the database, and allows the user to add
 * edit or delete groups. Not all functionality for these buttons
 * is implemented on initial creation of this class.
 */
public class MaintainCatalogTypes extends JInternalFrame implements ParentWindow {
	ManageProductsController control;

	/** Parent is used to return to main screen */
	private Component parent;

	
	/////////////constants
	
	//should be set to 'false' if data for table is obtained from a database
	//or some external file
	private final boolean USE_DEFAULT_DATA = true;
	private final String ADD = "Add";
	private final String EDIT = "Edit";
	private final String DELETE = "Delete";
	private final String BACK = "Back";	
	
	private static final String MAIN_LABEL = "Maintain Catalog Types";
	
	//table configuration
	private Properties headers;  
	
	private final int TABLE_WIDTH = Math.round(0.75f*GuiUtil.SCREEN_WIDTH);
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);
    

	//JPanels
	private JPanel mainPanel;
	private JPanel upperSubpanel;
	private JPanel lowerSubpanel;
	
	//other widgets
	private String title;
	private String tableLabelText;
	private JLabel tableLabel;
	private JScrollPane tablePane;
	private JTable table;
	private final String[] DEFAULT_COLUMN_HEADERS = {"Name of Catalog Group"};
    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{1.0f};	
	
    private CustomTableModel model;
 

 
	public MaintainCatalogTypes() {
		super(MAIN_LABEL,false,true,false,true);
	    control = ManageProductsController.getInstance();
	    control.setMaintainCatalogTypes(this);		
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
	}
	
	public JTable getTable(){
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
		defineLowerPanel();
		mainPanel.add(upperSubpanel,BorderLayout.NORTH);
		mainPanel.add(lowerSubpanel,BorderLayout.SOUTH);
			
	}
	private void defineUpperPanel() {
		upperSubpanel = new JPanel();
		upperSubpanel.setLayout(new BorderLayout());
		upperSubpanel.setBackground(GuiUtil.FILLER_COLOR);
		
		//create and add label
		createTableLabel();
		upperSubpanel.add(tableLabel,BorderLayout.NORTH);
		
		//create and add table
		createTableAndTablePane();
		GuiUtil.createCustomColumns(table, 
                TABLE_WIDTH,
                COL_WIDTH_PROPORTIONS,
                DEFAULT_COLUMN_HEADERS);		
		JPanel tablePanePanel = GuiUtil.createStandardTablePanePanel(table,tablePane);
	
		upperSubpanel.add(tablePanePanel,BorderLayout.CENTER);
		
		
		
	}
	private void createTableLabel() {
		tableLabel = new JLabel(MAIN_LABEL);
		tableLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		Font f = GuiUtil.makeVeryLargeFont(tableLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		tableLabel.setFont(f);
		
		
		
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
		updateTable();
	}	
	

	private void updateModel() {

        if(USE_DEFAULT_DATA) {			        	
			List<String[]> defaultData = DefaultData.getCatalogTypes();
			updateModel(defaultData);
        }

	}	

	
	
    private void updateTable() {
    	if(model != null && table !=null) {
        	if(table==null) System.out.println("null");
        	table.setModel(model);
        	table.updateUI();
        }
        repaint();

        
    }	

	private void defineLowerPanel() {
		 
		//add button
		JButton addButton = new JButton(ADD);
		addButton.addActionListener(control.getAddCatalogListener(this));
		
		//edit button
		JButton editButton = new JButton(EDIT);
		editButton.addActionListener(control.getEditCatalogListener(this));
		
		
		//delete button
		JButton deleteButton = new JButton(DELETE);
		deleteButton.addActionListener(control.getDeleteCatalogListener(this));
		
		
		//back button
		JButton backButton = new JButton(BACK);
		backButton.addActionListener(control.getBackToMainListener(this));
		

		
		
		//create lower panel
		JButton [] buttons = {addButton,editButton,deleteButton,backButton};
		lowerSubpanel = GuiUtil.createStandardButtonPanel(buttons);
		
		
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
		
	
	public static void main(String args[]) {
	
		(new MaintainCatalogTypes()).setVisible(true);
	}

	private static final long serialVersionUID = 3258410629793592632L;


}