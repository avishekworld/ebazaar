package application.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.ApplicationCleanup;
import application.CheckoutController;
import application.GuiUtil;

/**
 * Class Description: This window provides textfields to enter
 * information for payment by credit card. If the user clicks
 * the Proceed With Checkout button, the Terms and Conditions window
 * appears before the Final Order is displayed.<p>
 * <em>Reading values from the cardTypeField combo box:</em>
 * Use the following syntax to read item selected:
 * <code>String cardTypeSelected = 
 * 		(String)cardTypeField.getSelectedItem();</code>
 */
public class PaymentWindow extends JInternalFrame implements ParentWindow {

	CheckoutController control;
	private Component parent;


	private static final String MAIN_LABEL = "Payment Method";
	private final String PROCEED_BUTN = "Proceed With Checkout";
	private final String BACK_TO_CART_BUTN = "Back To Cart";
	
	private final String FIRST_NAME = "First Name";
	private final String LAST_NAME = "Last Name";
	private final String NAME_ON_CARD = "Name on Card";
	private final String CARD_NUMBER = "Card Number";
	private final String CARD_TYPE = "Card Type";
	private final String EXPIRATION = "Expiration Date";
	
	
	private JTextField nameOnCardField;
	private JTextField cardNumberField;
	private JComboBox cardTypeField;
	private JTextField expirationField;
	

	//JPanels
		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	public PaymentWindow() {
		super(MAIN_LABEL,false,true,false,true);
	    control = CheckoutController.getInstance();
	    control.setPaymentWindow(this);	
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
			
	}
    public String[] getPaymentFields() {
        String[] fields = new String[4];
        fields[0]= getNameOnCard();
        fields[1] =  getCardNumber();
        fields[2] = getCardType();
        fields[3] = getExpiration();
        return fields;
    }  
	
	public String[] getPaymentHeaders() {
	    String[] headers = {NAME_ON_CARD,CARD_NUMBER,CARD_TYPE,EXPIRATION};
	    return headers;
	}   		
    public String getNameOnCard(){
        if(nameOnCardField == null){
            return "";
            
        }
        return nameOnCardField.getText();
    }
    public String getCardNumber(){
        if(cardNumberField == null){
            return "";
            
        }
        return cardNumberField.getText();
    }
    public String getCardType(){
        if(cardTypeField == null){
            return "";
            
        }
        return cardTypeField.getSelectedItem().toString();
    }
    public String getExpiration(){
        if(expirationField == null){
            return "";
            
        }
        return expirationField.getText();
    }    
    public void setCredCardFields(String name, String num, String type, String exp){
        nameOnCardField.setText(name);
        cardNumberField.setText(num);
        cardTypeField.setSelectedItem(type);
        expirationField.setText(exp);
        
    }
	/** loads the cardTypeField combo box */
	private void loadCardTypeField() {
		String[] types = DefaultData.CARD_TYPES;
		if(cardTypeField != null) {
			for(int i = 0; i < types.length; ++i) {
				cardTypeField.addItem(types[i]);
			}
		}
		
		
	}
	private void initializeWindow() {
		
		setSize(GuiUtil.SCREEN_WIDTH,
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
		
		JLabel mainLabel = new JLabel(MAIN_LABEL);
		Font f = GuiUtil.makeVeryLargeFont(mainLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		mainLabel.setFont(f);
		upper.add(mainLabel);					
	}
	//table
	public void defineMiddlePanel(){
		middle = new JPanel();
		middle.setBackground(GuiUtil.FILLER_COLOR);
		middle.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel gridPanel = new JPanel();
		gridPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
		middle.add(gridPanel);
		GridLayout gl = new GridLayout(4,2);
		gl.setHgap(8);
		gl.setVgap(8);
		gridPanel.setLayout(gl);
		gridPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));

		//add fields
		makeLabel(gridPanel,NAME_ON_CARD);
		nameOnCardField = new JTextField(10);
		gridPanel.add(nameOnCardField);		
		
		makeLabel(gridPanel,CARD_NUMBER);
		cardNumberField = new JTextField(10);
		gridPanel.add(cardNumberField);		
		
		makeLabel(gridPanel,CARD_TYPE);
		cardTypeField = new JComboBox();
		cardTypeField.setBackground(GuiUtil.SCREEN_BACKGROUND);
		loadCardTypeField();
		gridPanel.add(cardTypeField);
		
		makeLabel(gridPanel,EXPIRATION);
		expirationField = new JTextField(10);
		gridPanel.add(expirationField);		
	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton proceedButton = new JButton(PROCEED_BUTN);
		proceedButton.addActionListener(control.getProceedFromPaymentListener(this));
		
		
		//back to cart button
		JButton backToCartButton = new JButton(BACK_TO_CART_BUTN);
		backToCartButton.addActionListener(control.getBackToCartFromPayListener(this));
		

		
		//create lower panel
		JButton [] buttons = {proceedButton,backToCartButton};
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
		
		(new PaymentWindow()).setVisible(true);
	}

	private static final long serialVersionUID = 3689071733583786041L;
	
}
