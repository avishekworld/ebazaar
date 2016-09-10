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
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import application.ApplicationCleanup;
import application.CheckoutController;
import application.GuiUtil;

/**
 * Class Description: The TermsWindow class displays the terms
 * under which products are sold and shipped. The implementation
 * is a TextArea widget containing the terms information.
 */
public class TermsWindow extends JInternalFrame implements ParentWindow {
    CheckoutController control;
	private Component parent;
	private static final String MAIN_LABEL = "Terms and Conditions";
	private final String PROCEED_BUTN = "Accept Terms And Conditions";
	private final String EXIT_BUTN = "Exit E-Bazaar";
	private JTextArea termsText;

	//JPanels	
	JPanel mainPanel;
	JPanel upper, middle, lower;
	public TermsWindow() {
		super(MAIN_LABEL,false,true,false,true);
	    control = CheckoutController.getInstance();
	    control.setTermsWindow(this);
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
		
			
	}
		
	 public void setTermsText(String text){
	        if(termsText!= null){
	            termsText.setText(text);
	        }
	    }	
	private void initializeWindow() {
		setTitle("Terms and Conditions");
		setSize(GuiUtil.SCREEN_WIDTH,
				Math.round(.6f*GuiUtil.SCREEN_HEIGHT));		
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
		GridLayout gl = new GridLayout(1,1);
		gridPanel.setLayout(gl);
		gridPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));

		//add text area
		termsText = new JTextArea(8,30);
		termsText.setBackground(GuiUtil.SCREEN_BACKGROUND);
		termsText.setFont(GuiUtil.makeDialogFont(termsText.getFont()));
		termsText.setLineWrap(true);
		termsText.setWrapStyleWord(true);
		termsText.setEditable(false);
		gridPanel.add(termsText);
	}
	
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton proceedButton = new JButton(PROCEED_BUTN);
		proceedButton.addActionListener(control.getAcceptTermsListener(this));
		
		
		//back to cart button
		JButton backToCartButton = new JButton(EXIT_BUTN);
		backToCartButton.addActionListener(new ExitButtonListener(this));
		
		//create lower panel
		JButton [] buttons = {proceedButton,backToCartButton};
		lower = GuiUtil.createStandardButtonPanel(buttons);
	}
	


	public void setParentWindow(Component parentWindow) {
		parent = parentWindow;
	}
	
	public Component getParentWindow() {
		return parent;
	}	
					
	public static void main(String[] args) {
		
		(new TermsWindow()).setVisible(true);
	}		


	private static final long serialVersionUID = 3258412811485853745L;

}
