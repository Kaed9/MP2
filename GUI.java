import javax.swing.*;
import javax.swing.JFrame.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.image.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame implements ActionListener {
	
	private JMenuBar menuBar;
	private JMenu file, open, /*train,*/ compress, edit;
	private JMenuItem /*open,*/ exit, pngImage, huffImage, newT, exist, train, comp;
	private JPanel image, himage; // 
	private JLabel imageLabel, himageLabel; // 
	private JTabbedPane imageTab;
	private ImageReader imageReader = new ImageReader();
	private ImageLoader imageLoader = new ImageLoader();
	private String fileName = "", imageName = "";
	private JToolBar toolBar;
	private JButton popen, hopen, trainhuff, newhuff, updatehuff, compressimage;
	
	public GUI() {
		
		super("Multi Channel Image Compressor");
		setLayout(null);
		getContentPane().setBackground(Color.lightGray);
		
		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
		} catch(Exception ex) { }
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(file);
		
		edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);
		menuBar.add(edit);
		
		open = new JMenu("Open       ");
		exit = new JMenuItem("Exit");
		// open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		open.setMnemonic(KeyEvent.VK_O);
		exit.setMnemonic(KeyEvent.VK_X);
		
		pngImage = new JMenuItem("PNG image");
		huffImage = new JMenuItem("Huffman coded image");
		pngImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		huffImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		huffImage.setEnabled(false);
		open.add(pngImage);
		open.add(huffImage);
		
		train = new JMenuItem("Train Huffman tree");
		train.setMnemonic(KeyEvent.VK_T);
		train.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		train.setEnabled(false);
		
		compress = new JMenu("Create huff file");
		newT = new JMenuItem("New huff file");
		exist = new JMenuItem("Existing huff file");
		compress.setMnemonic(KeyEvent.VK_C);
		newT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		exist.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		newT.setEnabled(false);
		exist.setEnabled(false);
		compress.add(newT);
		compress.add(exist);
		
		comp = new JMenuItem("Compress image");
		comp.setMnemonic(KeyEvent.VK_I);
		comp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		comp.setEnabled(false);
		
		file.add(open);
		file.addSeparator();
		// file.add(train);
		// file.addSeparator();
		// file.add(compress);
		// file.addSeparator();
		file.add(exit);
		
		edit.add(train);
		edit.addSeparator();
		edit.add(compress);
		edit.addSeparator();
		edit.add(comp);
		// edit.addSeparator();
		
		// whole = new JPanel();
		// whole.setLayout(new GridLayout(1, 1));
		
		// image = new JPanel();
		// himage = new JPanel();
		// image.setLayout(new FlowLayout(FlowLayout.LEFT));
		// himage.setLayout(new FlowLayout(FlowLayout.LEFT));
		// image.setBackground(Color.WHITE);
		// himage.setBackground(Color.WHITE);
		/*image.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		himage.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		image.setSize(450, 450);
		himage.setSize(450, 450);
		image.setLocation(50, 75);
		himage.setLocation(580, 75);
		
		imageLabel = new JLabel();
		image.add(imageLabel);
		
		add(image);
		add(himage);*/
		
		// imageLabel = new JLabel();
		// himageLabel = new JLabel();
		// image.add(imageLabel);
		// himage.add(himageLabel);
		
		imageTab = new JTabbedPane();
		// imageTab.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		// himageTab = new JTabbedPane();
		// imageTab.addTab("PNG Image", (new JScrollPane(image, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)));
		// imageTab.addTab("Huffman Coded Image", (new JScrollPane(himage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)));
		// imageTab.addTab("PNG Image", image);
		// imageTab.addTab("Huffman Coded Image", himage);
		add(imageTab);
		// whole.add(himageTab);
		
		toolBar = new JToolBar();
		ImageIcon picon = new ImageIcon("openpng1.png");
		ImageIcon hicon = new ImageIcon("openijk1.png");
		ImageIcon hufftree = new ImageIcon("traintree1.png");
		ImageIcon newHuff = new ImageIcon("newhuff.png");
		ImageIcon update = new ImageIcon("updatehuff.png");
		ImageIcon compress = new ImageIcon("compress.png");
		popen = new JButton(picon);
		hopen = new JButton(hicon);
		trainhuff = new JButton(hufftree);
		newhuff = new JButton(newHuff);
		updatehuff = new JButton(update);
		compressimage = new JButton(compress);
		popen.setToolTipText("Open PNG Image");
		hopen.setToolTipText("Open IJK Image");
		trainhuff.setToolTipText("Train Huffman Tree");
		newhuff.setToolTipText("Create New Huff File");
		updatehuff.setToolTipText("Update Existing Huff File");
		compressimage.setToolTipText("Compress Image");
		hopen.setEnabled(false);
		trainhuff.setEnabled(false);
		newhuff.setEnabled(false);
		updatehuff.setEnabled(false);
		compressimage.setEnabled(false);
		toolBar.add(popen);
		toolBar.add(hopen);
		toolBar.addSeparator();
		toolBar.add(trainhuff);
		toolBar.addSeparator();
		toolBar.add(newhuff);
		toolBar.add(updatehuff);
		toolBar.addSeparator();
		toolBar.add(compressimage);
		toolBar.setFloatable(false);
		add(toolBar);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
		// setResizable(false);
		
		// add(whole);
		Dimension dimension = getPreferredSize();
		// imageTab.setBounds(0, 0, 1093, 600);
		toolBar.setBounds(0, 0, getSize().width - 16, 30);
		// imageTab.setBounds(0, 0, getSize().width - 16, getSize().height - 60);
		imageTab.setBounds(0, 30, getSize().width - 16, getSize().height - 90);
		// himageTab.setBounds(100, 0, 100, 25);
		repaint();
		revalidate();
		
		// open.addActionListener(this);
		pngImage.addActionListener(this);
		huffImage.addActionListener(this);
		exit.addActionListener(this);
		train.addActionListener(this);
		exist.addActionListener(this);
		comp.addActionListener(this);
		popen.addActionListener(this);
		hopen.addActionListener(this);
		trainhuff.addActionListener(this);
		// newhuff.addActionListener(this);
		updatehuff.addActionListener(this);
		compressimage.addActionListener(this);
		
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setVisible(true);
		// setSize(1100, 650);
		// setExtendedState(MAXIMIZED_BOTH); 
		// setResizable(false);
	}
	
	public void actionPerformed(ActionEvent event) {
		
		if(event.getSource() == pngImage || event.getSource() == popen) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG (*.PNG)", "PNG"));
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showOpenDialog(this);
			
			if (result == JFileChooser.APPROVE_OPTION) {
				image = new JPanel();
				image.setLayout(new FlowLayout(FlowLayout.LEFT));
				image.setBackground(Color.WHITE);
				imageLabel = new JLabel();
				himageLabel = new JLabel();
				image.add(imageLabel);
				
				File selectedFile = fileChooser.getSelectedFile();
				// System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				Image pImage = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath()).getImage();
				imageLabel.setIcon(new ImageIcon(pImage));
				fileName = "" + selectedFile;
				imageName = "" + fileChooser.getSelectedFile().getName();
				train.setEnabled(true);
				trainhuff.setEnabled(true);
				pngImage.setEnabled(false);
				popen.setEnabled(false);
				imageTab.addTab(imageName, (new JScrollPane(image, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)));
			}
			
			/*String fileName = "" + fileChooser.getSelectedFile();
			ImagePanel imagePanel = new ImagePanel(fileName);
			image.add(imagePanel);
			imagePanel.setBounds(0, 0, 500, 500);
			repaint();
			revalidate();*/
			// image.setSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
			// imageLabel.setIcon(new ImageIcon(pImage));
			// pImage.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
			// imageLabel.setIcon(new ImageIcon(pImage));
			
		} else if(event.getSource() == huffImage || event.getSource() == hopen) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("IJK (*.IJK)", "IJK"));
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				himage = new JPanel();
				himage.setLayout(new FlowLayout(FlowLayout.LEFT));
				himage.setBackground(Color.WHITE);
				himageLabel = new JLabel();
				himage.add(himageLabel);
				
				File selectedFile = fileChooser.getSelectedFile();
				// System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				// String fName = "" + selectedFile;
				pngImage.setEnabled(true);
				popen.setEnabled(true);
				imageName = "" + fileChooser.getSelectedFile().getName();
				imageTab.addTab(imageName, (new JScrollPane(himage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)));
				BufferedImage hImage = imageLoader.getImage(fileName);
				himageLabel.setIcon(new ImageIcon(hImage));
			}
		} else if(event.getSource() == train || event.getSource() == trainhuff) {
			imageReader.createNewHuffman(fileName);
			exist.setEnabled(true);
			updatehuff.setEnabled(true);
			// System.out.println("DONE");
		} else if(event.getSource() == exist || event.getSource() == updatehuff) {
			imageReader.createHuffFile(fileName);
			comp.setEnabled(true);
			compressimage.setEnabled(true);
			// System.out.println("DONE YAY");
		} else if(event.getSource() == comp || event.getSource() == compressimage) {
			imageReader.createNewImageFile(fileName);
			huffImage.setEnabled(true);
			hopen.setEnabled(true);
		}else if(event.getSource() == exit) {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		
		new GUI();
	}
}
