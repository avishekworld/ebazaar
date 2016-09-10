package application.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import application.ApplicationCleanup;
import application.CheckoutController;
import application.GuiUtil;

/**
 * Class Description: This class presents a screen that displays
 * shipping and billing fields. The screen provides a Select Shipping
 * Address button that permits the user to pick, from another
 * screen, one of the customer's addresses, which is then loaded
 * automatically into the shipping fields on the current screen.
 */
public class ShippingBillingWindow extends JInternalFrame implements ParentWindow {
	
	CheckoutController control;
	private Component parent;
	
	//constants
	private final String MAIN_LABEL = "Shipping And Billing Information";
	private final String SHIP_LABEL = "Shipping Address";
	private final String BILL_LABEL = "Billing Address";
	private final String SHIP_METHOD_LABEL = "Shipping Method";
	private final String NAME = "Name";
	private final String ADDRESS = "Address";
	private final String CITY = "City";
	private final String STATE = "State";
	private final String ZIP = "Zip";
	
	//button labels
	private final String SELECT_SHIP_ADDR = "Select Shipping Address";
	private final String PROCEED_WITH_CHECKOUT = "Proceed With Checkout"; 
	private final String BACK_TO_CART = "Back To Cart";
	private final String GROUND = "Pigeon-carrier Ground";
	private final String AIR = "Pigeon-carrier Air";
	private final String OVERNIGHT = "Pigeon-carrier Overnight";
	private final String CHECK_IF_SAME = "Billing Address Same as Shipping?";
	private final String NEW_SHIP_ADDR = "New Shipping Address?";
	
	//JPanels
	JPanel mainPanel;
	JPanel upper;
	JPanel middle;
	
	   //button row
	JPanel lower;
	
	//upper divisions
	  //contains main label for screen
	JPanel upperTop;
	  //contains address panels
	JPanel upperMiddle;
	  //contains checkbox
	JPanel upperBottom;
	
	//upperMiddle divisions
	  //shipping address
	JPanel upperMiddleLeft;
	  //billing address
	JPanel upperMiddleRight;
	
	//upperMiddleLeft divisions
	  //label for shipping
	JPanel upperMiddleLeftLabel;
	  //address fields
	JPanel upperMiddleLeftFields;
	
	//upperMiddleRight divisions
	  //label for billing
	JPanel upperMiddleRightLabel;
	  //address fields
	JPanel upperMiddleRightFields;
	
	//middle divisions
	   //shipping method label
	JPanel middleTop;
	   //radio button panel
	JPanel middleBottom;
	
	//widgets
	
	
	private JTextField shipNameField;
	private JTextField shipAddressField;
	
	private JTextField shipCityField;
	private JTextField shipStateField;
	private JTextField shipZipField;
	
	private JTextField billNameField;
	private JTextField billAddressField;
	
	private JTextField billCityField;
	private JTextField billStateField;
	private JTextField billZipField;	
	
	private JRadioButton groundButton;
	private JRadioButton airButton;
	private JRadioButton overnightButton;
	
	private JCheckBox addressesSameCheckbox;
	
	private JCheckBox isNewShipAddrCheckbox;	
	
	
	public boolean isNewShipAddress() {
	    return (isNewShipAddrCheckbox != null ? isNewShipAddrCheckbox.isSelected() : false);
	}
    public boolean billingSameAsShipping() {
        return (addressesSameCheckbox != null ? addressesSameCheckbox.isSelected() : false);
    }
	/** Used by ShipAddressesWindow and by CheckoutController to fill in the shipping address */
	public void setShippingAddress(String name, String addr, String city, String state, String zip) {
		if(shipNameField != null) {
			shipNameField.setText(name);
		}
		if(shipAddressField != null) {
			shipAddressField.setText(addr);
		}

		if(shipCityField != null) {
			shipCityField.setText(city);
		}
		if(shipStateField != null) {
			shipStateField.setText(state);
		}
		if(shipZipField != null) {
			shipZipField.setText(zip);
		}
	}
    /** Used by CheckoutController to fill in the billing address */
    public void setBillingAddress(String name, String addr, String city, String state, String zip) {
        if(billNameField != null) {
            billNameField.setText(name);
        }
        if(billAddressField != null) {
            billAddressField.setText(addr);
        }

        if(billCityField != null) {
            billCityField.setText(city);
        }
        if(billStateField != null) {
            billStateField.setText(state);
        }
        if(billZipField != null) {
            billZipField.setText(zip);
        }
    }
    public String[] getShipAddressFields() {
        String[] fields = new String[4];
        fields[0]= shipAddressField.getText();
        fields[1] =  shipCityField.getText();
        fields[2] = shipStateField.getText();
        fields[3] = shipZipField.getText();
        return fields;
    } 
        
    
    public void setAddressFields(String[] data) {
    	
    	shipAddressField.setText(data[0]);
    	shipCityField.setText(data[1]);
        shipStateField.setText(data[2]);
    	shipZipField.setText(data[3]);
    	
    }
	public String[] getShipAddressHeaders() {
	    String[] headers = {ADDRESS,CITY,STATE,ZIP};
	    return headers;
	}   	
    public String[] getBillAddressFields() {
        String[] fields = new String[4];
        fields[0]= billAddressField.getText();
        fields[1] =  billCityField.getText();
        fields[2] = billStateField.getText();
        fields[3] = billZipField.getText();
        return fields;
    } 
	public ShippingBillingWindow() {
	    control = CheckoutController.getInstance();
	    control.setShippingBillingWindow(this);		
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
		pack();
		
		
			
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
		defineUpper();
		defineMiddle();
		defineLower();
		mainPanel.add(upper,BorderLayout.NORTH);
		mainPanel.add(middle,BorderLayout.CENTER);
		mainPanel.add(lower,BorderLayout.SOUTH);
			
	}
	/** Use BorderLayout */
	private void defineUpper(){
		upper = new JPanel();
		upper.setLayout(new BorderLayout());
		upper.setBackground(GuiUtil.FILLER_COLOR);
		
		defineUpperTop();
		defineUpperMiddle();
		defineUpperBottom();
		
		upper.add(upperTop,BorderLayout.NORTH);
		upper.add(upperMiddle,BorderLayout.CENTER);
		upper.add(upperBottom,BorderLayout.SOUTH);		
		
	}
	
	/** FlowLayout center, to place label */
	private void defineUpperTop() {
		upperTop = new JPanel();
		upperTop.setLayout(new FlowLayout(FlowLayout.CENTER));
		upperTop.setBackground(GuiUtil.FILLER_COLOR);
		JLabel mainLabel = new JLabel(MAIN_LABEL);
		Font f = GuiUtil.makeVeryLargeFont(mainLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		mainLabel.setFont(f);
		upperTop.add(mainLabel);		
	}
	
	/** FlowLayout center, to place address panels */
	private void defineUpperMiddle() {
		upperMiddle = new JPanel();
		upperMiddle.setLayout(new FlowLayout(FlowLayout.CENTER));
		upperMiddle.setBackground(GuiUtil.FILLER_COLOR);
		defineUpperMiddleLeft();
		defineUpperMiddleRight();
		upperMiddle.add(upperMiddleLeft);
		upperMiddle.add(upperMiddleRight);
				
	}
	
	/** BorderLayout to place label and address fields */
	private void defineUpperMiddleLeft() {
		upperMiddleLeft = new JPanel();
		upperMiddleLeft.setLayout(new BorderLayout());
		upperMiddleLeft.setBackground(GuiUtil.FILLER_COLOR);	
		defineUpperMiddleLeftLabelPanel();
		defineUpperMiddleLeftFieldsPanel();

		upperMiddleLeft.add(upperMiddleLeftLabel,BorderLayout.NORTH);
		upperMiddleLeft.add(upperMiddleLeftFields,BorderLayout.CENTER);			
		
	}
	/** FlowLayout center, for label */
	private void defineUpperMiddleLeftLabelPanel() {
		upperMiddleLeftLabel = new JPanel();
		upperMiddleLeftLabel.setLayout(new FlowLayout(FlowLayout.CENTER));
		upperMiddleLeftLabel.setBackground(GuiUtil.FILLER_COLOR);		
		
		JLabel shipLabel = new JLabel(SHIP_LABEL);
		Font f = GuiUtil.makeLargeFont(shipLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		shipLabel.setFont(f);
		upperMiddleLeftLabel.add(shipLabel);	
	}
	
	private void makeLabel(JPanel p, String s) {
		JLabel l = new JLabel(s);
		p.add(leftPaddedPanel(l));
	}
	
	/** GridLayout for address fields */
	private void defineUpperMiddleLeftFieldsPanel(){
		upperMiddleLeftFields = new JPanel();
		upperMiddleLeftFields.setLayout(new FlowLayout(FlowLayout.CENTER));
		upperMiddleLeftFields.setBackground(GuiUtil.FILLER_COLOR);
		
		
		JPanel gridPanel = new JPanel();
		gridPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));
		gridPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
		GridLayout gl = new GridLayout(5,2);
		gl.setHgap(10);
		gridPanel.setLayout(gl);
		
		upperMiddleLeftFields.add(gridPanel);
		
		JPanel paddedPanel = null;
		
		//ship name
		makeLabel(gridPanel,NAME);
		shipNameField = new JTextField(10);		
		gridPanel.add(shipNameField);				

		//shipping address 
		makeLabel(gridPanel,ADDRESS);
		shipAddressField = new JTextField(10);
		gridPanel.add(shipAddressField);	
				


		//shipping city
		makeLabel(gridPanel,CITY);
		shipCityField = new JTextField(10);
		gridPanel.add(shipCityField);	

		//shipping state
		makeLabel(gridPanel,STATE);
		shipStateField = new JTextField(10);
		gridPanel.add(shipStateField);	


		//shipping zip
		makeLabel(gridPanel,ZIP);
		shipZipField = new JTextField(10);
		gridPanel.add(shipZipField);	

	}
	/** BorderLayout to place label and address fields */
	private void defineUpperMiddleRight() {
		upperMiddleRight = new JPanel();
		upperMiddleRight.setLayout(new BorderLayout());
		upperMiddleRight.setBackground(GuiUtil.FILLER_COLOR);	
		defineUpperMiddleRightLabelPanel();
		defineUpperMiddleRightFieldsPanel();


		upperMiddleRight.add(upperMiddleRightLabel,BorderLayout.NORTH);
		upperMiddleRight.add(upperMiddleRightFields,BorderLayout.CENTER);		
	}
	
	/** FlowLayout center, for label */
	private void defineUpperMiddleRightLabelPanel() {
		upperMiddleRightLabel = new JPanel();
		upperMiddleRightLabel.setLayout(new FlowLayout(FlowLayout.CENTER));
		upperMiddleRightLabel.setBackground(GuiUtil.FILLER_COLOR);		
		
		JLabel billLabel = new JLabel(BILL_LABEL);
		Font f = GuiUtil.makeLargeFont(billLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		billLabel.setFont(f);
		upperMiddleRightLabel.add(billLabel);	
	}
	/** GridLayout for address fields */
	private void defineUpperMiddleRightFieldsPanel(){
		upperMiddleRightFields = new JPanel();
		upperMiddleRightFields.setLayout(new FlowLayout(FlowLayout.CENTER));
		upperMiddleRightFields.setBackground(GuiUtil.FILLER_COLOR);
		
		
		JPanel gridPanel = new JPanel();
		gridPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));
		gridPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
		GridLayout gl = new GridLayout(5,2);
		gl.setHgap(10);
		gridPanel.setLayout(gl);
		
		upperMiddleRightFields.add(gridPanel);
		
		JPanel paddedPanel = null;
		
		//billing name
		makeLabel(gridPanel,NAME);
		billNameField = new JTextField(10);		
		gridPanel.add(billNameField);				

		//billing address 
		makeLabel(gridPanel,ADDRESS);
		billAddressField = new JTextField(10);
		gridPanel.add(billAddressField);	
				


		//billing city
		makeLabel(gridPanel,CITY);
		billCityField = new JTextField(10);
		gridPanel.add(billCityField);	

		//billing state
		makeLabel(gridPanel,STATE);
		billStateField = new JTextField(10);
		gridPanel.add(billStateField);	


		//billing zip
		makeLabel(gridPanel,ZIP);
		billZipField = new JTextField(10);
		gridPanel.add(billZipField);	
			
	}
	/** Flow Layout, center, for checkbox */
	private void defineUpperBottom() {
		upperBottom = new JPanel();
		upperBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		upperBottom.setBackground(GuiUtil.FILLER_COLOR);
		
		addressesSameCheckbox = new JCheckBox();
		addressesSameCheckbox.setBackground(GuiUtil.FILLER_COLOR);
		JLabel addrSameLabel = new JLabel(CHECK_IF_SAME);
		
		
		isNewShipAddrCheckbox = new JCheckBox();
		isNewShipAddrCheckbox.setBackground(GuiUtil.FILLER_COLOR);
		JLabel newShipLabel = new JLabel(NEW_SHIP_ADDR);
		
		upperBottom.add(isNewShipAddrCheckbox);
		upperBottom.add(newShipLabel);					
		upperBottom.add(addressesSameCheckbox);
		upperBottom.add(addrSameLabel);
		
				
	}		
		
	/** Flow layout, center, for shipping method */
	private void defineMiddle(){
		middle = new JPanel();
		middle.setLayout(new FlowLayout(FlowLayout.CENTER));
		middle.setBackground(GuiUtil.FILLER_COLOR);
		
		JPanel shipMethodPanel = new JPanel();
		shipMethodPanel.setLayout(new BorderLayout());
		middle.add(shipMethodPanel);
		
		
		defineMiddleTop();
		defineMiddleBottom();	
		
		shipMethodPanel.add(middleTop,BorderLayout.NORTH);
		shipMethodPanel.add(middleBottom,BorderLayout.CENTER);			
	}
	/** Flow Layout center for shipping method label */
	private void defineMiddleTop(){
		middleTop = new JPanel();
		middleTop.setLayout(new FlowLayout(FlowLayout.CENTER));
		middleTop.setBackground(GuiUtil.FILLER_COLOR);	
		
		JLabel shipMethodLabel = new JLabel(SHIP_METHOD_LABEL);
		Font f = GuiUtil.makeLargeFont(shipMethodLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		shipMethodLabel.setFont(f);
		middleTop.add(shipMethodLabel);						
	}
	
	/** BoxLayout for radio button group */
	private void defineMiddleBottom(){
		middleBottom = new JPanel();
		
		middleBottom.setLayout(new BoxLayout(middleBottom, BoxLayout.Y_AXIS));
		middleBottom.setBackground(GuiUtil.SCREEN_BACKGROUND);
		middleBottom.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));
		
		groundButton = new JRadioButton(GROUND);
		groundButton.setBackground(GuiUtil.SCREEN_BACKGROUND);
		airButton  = new JRadioButton(AIR);
		airButton.setBackground(GuiUtil.SCREEN_BACKGROUND);
		overnightButton  = new JRadioButton(OVERNIGHT);
		overnightButton.setBackground(GuiUtil.SCREEN_BACKGROUND);
	    overnightButton.setSelected(true);

	    ButtonGroup group = new ButtonGroup();
    	group.add(groundButton);
	    group.add(airButton);
    	group.add(overnightButton);
    	
    	middleBottom.add(groundButton);
    	middleBottom.add(airButton);
    	middleBottom.add(overnightButton);
	    
	}
	

	/** standard button row */
	private void defineLower(){
		//select ship button
		JButton selectShipButton = new JButton(SELECT_SHIP_ADDR);
		selectShipButton.addActionListener(control.getSelectShipButtonListener(this));
		
		
		//proceed checkout button
		JButton proceedCheckoutButton = new JButton(PROCEED_WITH_CHECKOUT);
		proceedCheckoutButton.addActionListener(control.getProceedFromBillingCheckoutListener(this));
		
		//back to cart button
		JButton backButton = new JButton(BACK_TO_CART);
		backButton.addActionListener(control.getBackToCartButtonListener(this));		
		
		//create lower panel
		JButton [] buttons = {selectShipButton,proceedCheckoutButton,backButton};
		lower = GuiUtil.createStandardButtonPanel(buttons);		
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
		(new ShippingBillingWindow()).setVisible(true);
	}
	private static final long serialVersionUID = 3256445819661072690L;
	
}
