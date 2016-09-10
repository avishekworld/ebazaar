
package application;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import middleware.DatabaseException;
import middleware.EBazaarException;
import business.DbClassLogin;
import business.Login;
import business.SessionContext;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.*;
import application.gui.EbazaarMainFrame;
import application.gui.LoginWindow;
import application.gui.ParentWindow;

/**
 * @author pcorazza
 * @since Nov 11, 2004
 * Class Description:
 * NOTE: LoginWindow is disposed of every time
 * it is used, so we do not require LoginControl
 * to implement CleanupControl.
 * 
 */
public class LoginControl {
    
    private SessionContext context;
    private LoginWindow loginWindow;
    private Controller controller;
    
    Component currWindow;
    Component parentWindow;
    
    //If parent and current windows are same, we don't make one vanish
    //while the other is displayed.
    private boolean parentIsOuterFrame;
    
    public LoginControl(Component currWindow, Component parentWindow){
        this.currWindow = currWindow;
        this.parentWindow = parentWindow;
        parentIsOuterFrame = (parentWindow.getClass() == EbazaarMainFrame.class);
    }
    public LoginControl(Component currWindow, Component parentWindow, Controller controller){
        this(currWindow,parentWindow);
        this.controller=controller;
    }    
    
    public void startLogin() {
        context = SessionContext.getInstance();
        loginWindow = new LoginWindow(this);
        
        EbazaarMainFrame.getInstance().getDesktop().add(loginWindow);
        //loginWindow.setVisible(true);
        //loginWindow.show();
        if(!parentIsOuterFrame) parentWindow.setVisible(false);
        loginWindow.setVisible(true);
    }
    private void loadCustomer(Integer custId) throws DatabaseException{
        ICustomerSubsystem customer = new CustomerSubsystemFacade();
        customer.initializeCustomer(custId);
        SessionContext context = SessionContext.getInstance();
        context.add(CustomerConstants.LOGGED_IN, Boolean.TRUE);
        context.add(CustomerConstants.CUSTOMER, customer);

        
    }
    private void authenticate(Integer id, String pwd) {
        try {
            //authenticate
            Login login = new Login(id,pwd);
            DbClassLogin dbClass = new DbClassLogin(login);
            boolean authenticated = dbClass.authenticate();
        
            //if authenticated, load customer subsystem
            if(authenticated){
                loadCustomer(id);
                JOptionPane.showMessageDialog(loginWindow,                                                    
                        "Login successful",
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);                       	    
            }
            else {
                throw new UserException("Either id or password is incorrect.");
            }
        }
        catch(EBazaarException e){
            JOptionPane.showMessageDialog(loginWindow,                                                    
                        "Error: "+e.getMessage(),
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                
            loginWindow.setVisible(true);
        }
    }
    //////// event handling code
    
    public SubmitListener getSubmitListener(LoginWindow w) {
        return new SubmitListener();
    }
    public CancelListener getCancelListener(LoginWindow w) {
        return new CancelListener();
    }
    
	class SubmitListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            loginWindow.setVisible(false);
        	Integer id = Integer.parseInt(loginWindow.getCustId());
        	String pwd = loginWindow.getPassword();
       	    authenticate(id,pwd);
       	    loginWindow.dispose();
       	    if(controller != null){
       	    	Boolean loggedIn = (Boolean)SessionContext.getInstance().get(CustomerConstants.LOGGED_IN);
       	    	if(loggedIn==Boolean.TRUE) controller.doUpdate();
       	    	else parentWindow.setVisible(true);
       	    }
       	    else {
       	    	Boolean loggedIn = (Boolean)SessionContext.getInstance().get(CustomerConstants.LOGGED_IN);
       	    	if(loggedIn==Boolean.TRUE) currWindow.setVisible(true);
       	    	else parentWindow.setVisible(true);
       	    	
       	    }   	    
        }
	}
	class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	
        	if(parentWindow != null) {
        		parentWindow.setVisible(true);
        	}
        	loginWindow.dispose();

        }
	}
    

}
