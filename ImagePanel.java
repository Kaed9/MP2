import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.image.ImageObserver;

public class ImagePanel extends JLabel {
	
	Image image;
	ImageObserver imageObserver;
	
	public ImagePanel(String file) {
		
		ImageIcon icon = new ImageIcon(file);
		image = icon.getImage();
		imageObserver = icon.getImageObserver();
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), imageObserver);
	}
}