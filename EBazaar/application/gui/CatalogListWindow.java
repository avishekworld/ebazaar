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

import application.BrowseAndSelectController;
import application.GuiUtil;


/**
 * Class Description: This screen presents the list of all E-Bazaar
 * catalogs. As of creation date, there were just two catalogs in the 
 * default data: Books
 * and Clothes. Clicking the Browse button when one of the catalogs
 * has been selected invokes an instance
 * of ProductListWindow, displaying the available items for the selected
 * catalog.
 * Students:  See the readdata method for where data is put into the table.
 */
public class CatalogListWindow extends JInternalFrame implements ParentWindow {
	
	BrowseAndSelectController control;
	
	/** Parent is used to return to main screen */
	private Component parent;

	//////////////constants
	
	//should be set to 'false' if data for table is obtained from a database
	//or some external file
	private final boolean USE_DEFAULT_DATA = true;
	
	private static final String MAIN_LABEL = "Browse Catalog";
	private final String BROWSE = "Browse";
	private final String BACK_TO_MAIN = "Back To Main";
	private final int TABLE_WIDTH = Math.round(0.75f*GuiUtil.SCREEN_WIDTH);
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);
	private final String[] DEFAULT_COLUMN_HEADERS = {"Available Catalogs"};
    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{1.0f};

	//JPanels
	private JPanel mainPanel;
	private JPanel upperSubpanel;
	private JPanel lowerSubpanel;
	private JPanel labelPanel;
	
	//other widgets
	
	private JScrollPane tablePane;
	private JTable table;
	private CustomTableModel model;
	 
    public static CatalogListWindow instance;
    
	
	public CatalogListWindow() {
		super(MAIN_LABEL,false,true,false,true);
	    control = BrowseAndSelectController.getInstance();
	    control.setCatalogList(this);		
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
		defineLowerPanel();
		mainPanel.add(upperSubpanel,BorderLayout.NORTH);
		mainPanel.add(lowerSubpanel,BorderLayout.SOUTH);
			
	}
	
	private void defineUpperPanel() {
		upperSubpanel = new JPanel();
		upperSubpanel.setLayout(new BorderLayout());
		upperSubpanel.setBackground(GuiUtil.FILLER_COLOR);
		
		//create and add label
		createMainLabel();
		upperSubpanel.add(labelPanel,BorderLayout.NORTH);
		
		//create and add table
		createTableAndTablePane();
		GuiUtil.createCustomColumns(table, 
                TABLE_WIDTH,
                COL_WIDTH_PROPORTIONS,
                DEFAULT_COLUMN_HEADERS);
		
		JPanel tablePanePanel = GuiUtil.createStandardTablePanePanel(table,tablePane);
	
		upperSubpanel.add(tablePanePanel,BorderLayout.CENTER);
		
		
		
	}
	
	private void createMainLabel() {
		JLabel mainLabel = new JLabel(MAIN_LABEL);
		Font f = GuiUtil.makeVeryLargeFont(mainLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		mainLabel.setFont(f);
		labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		labelPanel.setBackground(GuiUtil.FILLER_COLOR);
		labelPanel.add(mainLabel);		
	}
	
	private void createTableAndTablePane() {
		updateModel();
		table = new JTable(model);
		tablePane = new JScrollPane();
		tablePane.setPreferredSize(new Dimension(TABLE_WIDTH, DEFAULT_TABLE_HEIGHT));
		tablePane.getViewport().add(table);
		
	}


	public void updateModel(List<String[]> list){
		if(model == null){
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
			theData = DefaultData.getCatalogTypes();
        }
		updateModel(theData);
 	}	
	
	
    private void updateTable() {      
        table.setModel(model);
        table.updateUI();
        repaint();     
    }	

	private void defineLowerPanel() {
		 
		//browse button
		JButton browseButton = new JButton(BROWSE);
		browseButton.addActionListener(control.getSelectCatalogListener(this));
		
		
		//back button
		JButton backButton = new JButton(BACK_TO_MAIN);
		backButton.addActionListener(control.getBackToMainFrameListener(this));
		
		
		//create lower panel
		JButton [] buttons = {browseButton,backButton};
		lowerSubpanel = GuiUtil.createStandardButtonPanel(buttons);
		
		
	}
	public void setParentWindow(Component parentWindow) {
		parent = parentWindow;
	}
	
	public Component getParentWindow() {
		return parent;
	}
	
	public JTable getTable() {
		return table;
	}
			
	public static void main(String[] args) {
		(new CatalogListWindow()).setVisible(true);	
	}	

	private static final long serialVersionUID = 3258411720664953398L;
	
}