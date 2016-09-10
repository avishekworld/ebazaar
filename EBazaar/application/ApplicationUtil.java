package application;

import java.awt.Window;

import javax.swing.JInternalFrame;

import java.util.logging.Logger;

public class ApplicationUtil {
	private static final Logger LOG = Logger.getLogger(ApplicationUtil.class.getName());
	public static void cleanup(Window[] windows) {
		for(Window w : windows){
			if(w != null){
				LOG.info("Disposing of window "+w.getClass().getName());
				w.dispose();
			}
		}
	}
	public static void cleanup(JInternalFrame[] internalFrames) {
		for(JInternalFrame w : internalFrames){
			if(w != null){
				LOG.info("Disposing of window "+w.getClass().getName());
				w.dispose();
			}
		}
	}
}
