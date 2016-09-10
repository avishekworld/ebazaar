package application.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import application.BrowseAndSelectController;
import application.GuiUtil;


/**
 * Class Description: This window displays all known products belonging
 * to the selected catalog group. The catalog group is passed into
 * the constructor as  a parameter. In the final version of the
 * application, the products displayed should be all products in
 * the database having category group equal to this parameter. 
 */
public class ProductListWindow extends JInternalFrame implements ParentWindow {
	private BrowseAndSelectController control;

	//constants
	private final String AVAIL_BOOKS = "List of Available Books";
 	private final String AVAIL_CLOTHING = "List of Available Clothing";
    private final float [] COL_WIDTH_PROPORTIONS =	{1.0f};
 	
 	
 	/** Parent is used to return to main screen */
	private Component parent;

	
	/////////////constants
	
	//should be set to 'false' if data for table is obtained from a database
	//or some external file
	private final boolean USE_DEFAULT_DATA = true;
	private final String SELECT = "Select";
	private final String BACK = "Back";
	
	
	//table configuration
	private Properties headers; 
	private String[] header;
	
	private final int TABLE_WIDTH = Math.round(0.75f * GuiUtil.SCREEN_WIDTH);
    private final int DEFAULT_TABLE_HEIGHT = 
    	Math.round(0.75f * GuiUtil.SCREEN_HEIGHT);

	//JPanels
	private JPanel mainPanel;
	private JPanel upperSubpanel;
	private JPanel lowerSubpanel;
	private JPanel labelPanel;
	
	//other widgets
	private String title;
	private String mainLabelText;
	private JLabel mainLabel;
	private JScrollPane tablePane;
	private JTable table;
	
	private String catalogType;
    private CustomTableModel model;
 
 	private void setTitleAndTableLabel(){
 		title = makeTitle(catalogType);
 		mainLabelText = title;
 	}
 	
 	private static String makeTitle(String type) {
 		return "Available " + type;
 	}
 	/**
 	 * 
 	 * @param type - the category group (either books or clothes)
 	 */
	public ProductListWindow(String type) {
		super(makeTitle(type),false,true,false,true);
	    control = BrowseAndSelectController.getInstance();
	    control.setProductListWindow(this);		
		this.catalogType = type;
		initializeWindow();
		initializeTableHeaderTable();
		defineMainPanel();
		getContentPane().add(mainPanel);
		
		
	}
	
	//this method iniitializes the table of headers for the table columns
	private void initializeTableHeaderTable() {
		headers = new Properties(); 
		headers.setProperty(DefaultData.BOOKS,this.AVAIL_BOOKS);
		headers.setProperty(DefaultData.CLOTHES,this.AVAIL_CLOTHING);		
		
	}

	private void initializeWindow() {
		setTitleAndTableLabel();
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
		JPanel tablePanePanel = GuiUtil.createStandardTablePanePanel(table,tablePane);
	
		upperSubpanel.add(tablePanePanel,BorderLayout.CENTER);
		
		
		
	}

	private void createMainLabel() {
		JLabel mainLabel = new JLabel(mainLabelText);
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
		updateTable();
		

		
	}
	
	public void updateModel(List<String[]> list){
		if(model==null){
	        model = new CustomTableModel();
		}
		
		String colHeader = headers.getProperty(catalogType);
		header = new String[]{colHeader};
		model.setTableValues(list);	
		//if(table != null) updateTable();
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
			theData = dd.getCatalogWindowData(catalogType);
        }
		updateModel(theData);
 	}		
	
    private void updateTable() {
		GuiUtil.createCustomColumns(table, 
                TABLE_WIDTH,
                COL_WIDTH_PROPORTIONS,
                header);
		       
        table.setModel(model);
        table.updateUI();
        repaint();
        
    }	

	private void defineLowerPanel() {
		 
		//select button
		JButton selectButton = new JButton(SELECT);
		selectButton.addActionListener(control.getSelectProductListener(this,USE_DEFAULT_DATA));
		
		
		//back button
		JButton backButton = new JButton(BACK);
		backButton.addActionListener(control.getBackToCatalogListListener(this));
		
		
		//create lower panel
		JButton [] buttons = {selectButton,backButton};
		lowerSubpanel = GuiUtil.createStandardButtonPanel(buttons);
		
		
	}
	public void setParentWindow(Component parentWindow) {
		parent = parentWindow;
	}
	
	public Component getParentWindow() {
		return parent;
	}
	public JTable getTable(){
		return table;
	}
			
	public static void main(String args[]) {
	
		(new ProductListWindow("Clothes")).setVisible(true);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 3834024779601818169L;

}