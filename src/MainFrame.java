import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;


public class MainFrame extends JFrame {

	public static void log(String string) {
		U.log("(MainFrame) " + string);
	}

	private App app;
	private Session session;

	private Listener listener = new Listener();
	
	private LeftSidePanel leftSidePanel;
	private ImagesPanel imagesPanel;
	private UpperPanel upperPanel;

	MainFrame(App app_, Session session_){
		app = app_;
		session = session_;
		
		setTitle("ImgLibrary - - - " + session.title());
		setSize(1000,800);
		setLocationRelativeTo(null); // center the frame on screen
		addWindowListener(listener);
		
		imagesPanel = new ImagesPanel(session_, 3, 20, 20);
		leftSidePanel = new LeftSidePanel(session_, this);
		
		JSplitPane center = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT,
			leftSidePanel.scroller,
			imagesPanel);
		
		add(center, BorderLayout.CENTER);
		
		upperPanel = new UpperPanel(session_);
		add(upperPanel, BorderLayout.NORTH);

		setVisible(true);

		readSession();
	}

	public void readSession() {}


	private class Listener
	extends WindowAdapter {
		public void log(String string) {
			U.log("(MainFrame.Listener) " + string);
		}

		@Override
		public void windowClosing(WindowEvent ev) {
			app.closeSession(MainFrame.this);
		}
	}

}
