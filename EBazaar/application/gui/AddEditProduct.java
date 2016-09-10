package application.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.externalinterfaces.CustomerConstants;

import application.ApplicationCleanup;
import application.GuiUtil;
import application.IComboObserver;
import application.ManageProductsController;

/**
 * Class Description: This class is responsible for building
 * the window for adding or editing a product. 
 */
public class AddEditProduct extends JInternalFrame implements ParentWindow, IComboObserver {

	private Component parent;
	private ManageProductsController control;

	/** final value of label will be set in the constructor */
	private static String mainLabel = " Product";
	private final String SAVE_BUTN = "Save";
	private final String BACK_BUTN = "Close";
	
	private JTextField productNameField;
	private JComboBox catalogGroupField;
	private JTextField pricePerUnitField;
	private JTextField mfgDateField;	
	private JTextField quantityField;
	String[] fldNames = DefaultData.FIELD_NAMES;
	
	/** group is "Books", "Clothes" etc */
	private String catalogGroup;
	
	/** value is "Add New" or "Edit" */
	private String addOrEdit = GuiUtil.ADD_NEW;
	
	/** convenience method to determine which mode,add or edit */
	private boolean isEditMode() {
		return addOrEdit.equals(GuiUtil.EDIT);
	}
	
	/** map of initial field values */
	private Properties fieldValues;
	
	private static String makeTitle(String addEditString) {
		return addEditString + mainLabel;
	}

	//JPanels		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	/**
	 * Constructor sets instance variables and builds gui. 
	 * @param addOrEdit - has value "add" or "edit", indicating which gui window will be built
	 * @param catalogGroup - has value "Books" or "Clothes"
	 * @param fieldValues - values to be set in data fields of gui
	 */
	public AddEditProduct(String addOrEdit, String catalogGroup, Properties fieldValues) {
		super(makeTitle(addOrEdit),false,true,false,true);
	    control = ManageProductsController.getInstance();
	    control.setAddEditProduct(this);
		this.catalogGroup = catalogGroup;
		this.addOrEdit = addOrEdit;
		this.fieldValues = fieldValues;
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
			
	}

	
	private void initializeWindow() {
		setTitle(makeTitle(addOrEdit));
		setSize(Math.round(.7f*GuiUtil.SCREEN_WIDTH),
				Math.round(.7f*GuiUtil.SCREEN_HEIGHT));
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
	//table
	public void defineMiddlePanel(){
		middle = new JPanel();
		middle.setBackground(GuiUtil.FILLER_COLOR);
		middle.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel gridPanel = new JPanel();
		gridPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
		middle.add(gridPanel);
		GridLayout gl = new GridLayout(5,2);
		gl.setHgap(8);
		gl.setVgap(8);
		gridPanel.setLayout(gl);
		gridPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));


		//add fields
		
		
		String labelName = fldNames[DefaultData.PRODUCT_NAME_INT];
		makeLabel(gridPanel,labelName);
		productNameField = new JTextField(10);
		productNameField.setText(fieldValues.getProperty(labelName));
		gridPanel.add(productNameField);
		
		//catalog group is different from the other fields
		//because it plays a different role in MaintainCatalog
		//so it is set differently
		labelName = "Catalog";
		makeLabel(gridPanel,labelName);
		catalogGroupField = new JComboBox();
		catalogGroupField.addItem(DefaultData.BOOKS);
		catalogGroupField.addItem(DefaultData.CLOTHES);
		catalogGroupField.setSelectedItem(catalogGroup);
		if(isEditMode()) catalogGroupField.setEnabled(false);
		
		Action comboAction = control.getComboAction(this);
		comboAction.putValue(CustomerConstants.COMBO,this.getClass().getName() );
		//catalogTypeCombo.addActionListener(control.getComboActionListener());
		catalogGroupField.addActionListener(comboAction);
		gridPanel.add(catalogGroupField);
		
		labelName = fldNames[DefaultData.PRICE_PER_UNIT_INT];
		makeLabel(gridPanel,labelName);
		pricePerUnitField = new JTextField(10);
		pricePerUnitField.setText(fieldValues.getProperty(labelName));
		gridPanel.add(pricePerUnitField);		
		
		labelName = fldNames[DefaultData.MFG_DATE_INT];
		makeLabel(gridPanel,labelName);
		mfgDateField = new JTextField(10);
		mfgDateField.setText(fieldValues.getProperty(labelName));
		gridPanel.add(mfgDateField);
						
		labelName = fldNames[DefaultData.QUANTITY_INT];
		makeLabel(gridPanel,labelName);
		quantityField = new JTextField(10);
		quantityField.setText(fieldValues.getProperty(labelName));
		gridPanel.add(quantityField);
		

	}
	/*
	void loadFieldValues() {
		productNameField.setText(fieldValues.getProperty(fldNames[DefaultData.PRODUCT_NAME_INT]));
		catalogGroupField.setSelectedItem(catalogGroup);
		pricePerUnitField.setText(fieldValues.getProperty(fldNames[DefaultData.PRICE_PER_UNIT_INT]));
		mfgDateField.setText(fieldValues.getProperty(fldNames[DefaultData.MFG_DATE_INT]));
		quantityField.setText(fieldValues.getProperty(fldNames[DefaultData.QUANTITY_INT]));
	}*/
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton saveButton = new JButton(SAVE_BUTN);
		saveButton.addActionListener(control.getSaveAddEditProductListener(this));
		
		
		//back to cart button
		JButton backButton = new JButton(BACK_BUTN);
		backButton.addActionListener(control.getBackFromAddEditProductListener(this));
		

		
		//create lower panel
		JButton [] buttons = {saveButton,backButton};
		lower = GuiUtil.createStandardButtonPanel(buttons);
	}
	
	private void makeLabel(JPanel p, String s) {
		JLabel l = new JLabel(s);
		p.add(leftPaddedPanel(l));
	}
	private JPanel leftPaddedPanel(JLabel label) {
		JPanel paddedPanel = new JPanel();
		paddedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		paddedPanel.add(GuiUtil.createHBrick(1));
		paddedPanel.add(label);
		paddedPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
		return paddedPanel;		
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

	public void setCatalogGroup(String catalogGroup) {
		this.catalogGroup = catalogGroup;
	}	
	public void refreshData() {
		//loadFieldValues();
		//repaint();
	}
}
