import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;


public class ImagesPanel extends JPanel implements Observer {
	public static void log(String string) {
		U.log("(ImagesPanel) " + string);
	}
	
	private Session session;
	private ArrayList<ImageView> images;
	public Listener listener = new Listener();
	private int hpadding;
	private int vpadding;
	public JScrollPane scroller;
	private int ncol;
	// ^ number of columns to use to display the image thumbnails

	private int imageSize = 50;

	public ImagesPanel(Session session_, int ncol_, int hpadding_, int vpadding_) {
		session = session_;
		hpadding = hpadding_;
		vpadding = vpadding_;
		ncol = ncol_;

		scroller = new JScrollPane(this);
		setLayout(null);
		setBackground(Color.WHITE);
		session.addObserver(this);
		
		readSession();

		scroller.addComponentListener(this.listener);
		this.addMouseWheelListener(listener);
		this.setPreferredSize(scroller.getPreferredSize());
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	public void readSession() {
		this.removeAll();
		this.repaint();
		getImages();
		refreshLayout();
	}
	
	public void getImages() {
		images = new ArrayList<ImageView>();
		ArrayList<ImageModel> imodels = session.getImages();
		if (imodels == null) log("imodels null !");
		else log("imodels not null");
		for (int i = 0; i < imodels.size(); i++) {
			ImageView iview = new ImageView(imodels.get(i));
			images.add(iview);
			this.add(iview);
		}
	}
	
	public void refreshLayout() {
		Dimension dim = scroller.getSize();
		int thisWidth = (int) dim.width - 18;
		int imgSize = ((thisWidth - hpadding) / ncol) - hpadding;
		
		for (int i = 0; i < images.size(); i++) {
			ImageView image = images.get(i);
			int row = i / ncol;
			int col = i % ncol;
			int x = hpadding + col*(imgSize + hpadding);
			int y = vpadding + row*(imgSize + vpadding);
			image.setBounds(x, y, imgSize, imgSize);
			image.revalidate();
			image.display(imgSize);
		}
		int newHeight;
		if (images.size() % ncol == 0) // last row of images is full
			newHeight = (images.size() / ncol)*(vpadding + imgSize) + vpadding; 
		else // last row of images is only partially filled
			newHeight = ((images.size() / ncol) + 1)*(vpadding + imgSize) + vpadding; 
		setPreferredSize(new Dimension(dim.width - 18, newHeight));
		revalidate();
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		readSession();
		
	}
	
	private class Listener
	implements ComponentListener, MouseWheelListener
	{

		@Override
		public void componentHidden(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentResized(ComponentEvent arg0) {
			refreshLayout();
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent ev) {
			int notches = ev.getWheelRotation();
			ncol = Math.max(ncol + notches, 1);
			refreshLayout();
			log(notches + " - " + ncol);
		}
		
	}

}
