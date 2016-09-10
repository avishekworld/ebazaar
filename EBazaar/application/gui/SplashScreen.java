package application.gui;


import javax.swing.*;
import application.GuiUtil;

public class SplashScreen extends JWindow implements Runnable {
	private static final long serialVersionUID = -4077637765572518282L;

	public void run(){
		JLabel splashLabel = new JLabel(new ImageIcon(GuiUtil.SPLASH));
		getContentPane().add(splashLabel);	
		setSize(420,300);
		GuiUtil.centerFrameOnDesktop(this);
		setVisible(true);
	}
}