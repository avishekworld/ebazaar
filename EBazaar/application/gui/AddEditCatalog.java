package application.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JTextField;

import application.ApplicationCleanup;
import application.GuiUtil;
import application.ManageProductsController;
/** 
 * Class Description: This class is responsible for building
 * the window for adding or editing a catalog group. 
 */
public class AddEditCatalog extends JInternalFrame implements ParentWindow {
	private Component parent;
	private ManageProductsController control;

	/** final value of label will be set in the constructor */
	private static String mainLabel = " Catalog Type";
	private final String SAVE_BUTN = "Save";
	private final String BACK_BUTN = "Close";
	private final String PRODUCT_NAME = "Product Name";
	private JTextField productNameField;

	/** catalogGroup has possible values "Books", "Clothes" etc */
	private String catalogGroup;
	
	/** value is "Add New" or "Edit" */
	private String addOrEdit = GuiUtil.ADD_NEW;
	
	private static String makeTitle(String addEdit) {
		return addEdit + mainLabel;
	}
	//JPanels		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	/** 
	 * Constructor sets addOrEdit and catalogGroup instance variables and
	 * then builds the gui. Made visible or invisible by calling classes.
	 * @param addOrEdit - value "add" means it's the window for adding; "edit" 
	 * means it's for editing.
	 * @param catalogGroup - catalogGroup is "Books" or "Clothes"
	 */
	public AddEditCatalog(String addOrEdit, String catalogGroup) {
		super(makeTitle(addOrEdit),false,true,false,true);
		control = ManageProductsController.getInstance();
	    control.setAddEditCatalog(this);		
		this.catalogGroup = catalogGroup;
		this.addOrEdit = addOrEdit;
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);			
	}

	private void initializeWindow() {
		setTitle(makeTitle(addOrEdit));
		setSize(Math.round(.7f*GuiUtil.SCREEN_WIDTH),
				Math.round(.5f*GuiUtil.SCREEN_HEIGHT));
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
		
		JLabel mainLabel = new JLabel(finalMainLabelName());
		Font f = GuiUtil.makeVeryLargeFont(mainLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		mainLabel.setFont(f);
		upper.add(mainLabel);					
	}
	
	private String finalMainLabelName() {
		return addOrEdit+" "+mainLabel;
	}
	
	public void defineMiddlePanel(){
		middle = new JPanel();
		middle.setBackground(GuiUtil.FILLER_COLOR);
		middle.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel label = new JLabel(PRODUCT_NAME);
		productNameField = new JTextField(10);
		productNameField.setText(catalogGroup);
		
		middle.add(label);
		middle.add(productNameField);

	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton saveButton = new JButton(SAVE_BUTN);
		saveButton.addActionListener(control.getSaveAddEditCatListener(this));
		
		
		//back to cart button
		JButton backButton = new JButton(BACK_BUTN);
		backButton.addActionListener(control.getBackFromAddEditCatListener(this));
		

		
		//create lower panel
		JButton [] buttons = {saveButton,backButton};
		lower = GuiUtil.createStandardButtonPanel(buttons);
	}
	

	public void setParentWindow(Component parentWindow) {
		parent = parentWindow;
	}
	
	public Component getParentWindow() {
		return parent;
	}					

	
	public static void main(String[] args) {
		
		
	}
	private static final long serialVersionUID = 1L;	
}
