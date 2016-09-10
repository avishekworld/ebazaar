package application.gui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.ApplicationCleanup;
import application.BrowseAndSelectController;
import application.GuiUtil;
public class QuantityWindow extends JInternalFrame {
	private BrowseAndSelectController control;
	private Component parent;
    private final String MAIN_LABEL = "Quantity Desired";
    private final String OK_BUTN = "OK";
    private final String CANCEL_BUTN = "Cancel";
    private JTextField quantityField;
    private boolean edit = false;
    private Integer positionOfEdit = null;
    //JPanels
    JPanel mainPanel;
    JPanel upper, middle, lower;
    public QuantityWindow(boolean edit, Integer posOfEdit) {
    	this.edit = edit;
    	this.positionOfEdit = posOfEdit;
        control = BrowseAndSelectController.getInstance();
        control.setQuantityWindow(this);
        initializeWindow();
        defineMainPanel();
        getContentPane().add(mainPanel);
    }
	
    private void initializeWindow() {
        setTitle("Quantity Desired");
        setSize(Math.round(.5f*GuiUtil.SCREEN_WIDTH),
                Math.round(.4f*GuiUtil.SCREEN_HEIGHT));      
        GuiUtil.centerFrameOnDesktop(this);
        
    }
    
    private void defineMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(GuiUtil.QUANTITY_SCREEN_BGRND);
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
        upper.setBackground(GuiUtil.QUANTITY_SCREEN_BGRND);
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
        middle.setBackground(GuiUtil.QUANTITY_SCREEN_BGRND);
        middle.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel gridPanel = new JPanel();
        gridPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
        middle.add(gridPanel);
        GridLayout gl = new GridLayout(1,1);
        gridPanel.setLayout(gl);
        gridPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));

        //add text area
        quantityField = new JTextField(4);
        quantityField.setBackground(GuiUtil.SCREEN_BACKGROUND);
        quantityField.setFont(GuiUtil.makeDialogFont(quantityField.getFont()));
        quantityField.setText("1");
        gridPanel.add(quantityField);
    }
    
    //buttons
    public void defineLowerPanel(){
        //proceed button
        JButton okButton = new JButton(OK_BUTN);
        okButton.addActionListener(control.getQuantityOkListener(this, edit, positionOfEdit));
        
        
        //back to cart button
        JButton cancelButton = new JButton(CANCEL_BUTN);
        cancelButton.addActionListener(new CancelListener());
        
        //create lower panel
        JButton [] buttons = {okButton,cancelButton};
        lower = GuiUtil.createStandardButtonPanel(buttons, 
                                 GuiUtil.QUANTITY_SCREEN_BGRND);
    }
    

    public String getQuantityDesired(){
        return (quantityField==null ? "1" : quantityField.getText());
    }
    public void setParentWindow(Component parentWindow) {
        parent = parentWindow;
    }
    
    public Component getParentWindow() {
        return parent;
    }   
                    

    class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            setVisible(false);
            if(parent != null){
                parent.setVisible(true);
            }
 
        }
    }      
    public static void main(String[] args) {
        
        (new QuantityWindow(false, null)).setVisible(true);
    }       
	private static final long serialVersionUID = 3618135641289078841L;



}
