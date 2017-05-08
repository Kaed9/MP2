import javax.swing.*;
import javax.swing.JFrame.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.image.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
// import java.util.Calendar;
// import java.lang.Math;
import javax.swing.text.DefaultCaret;
import javax.swing.BorderFactory;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame implements ActionListener {
	
	private JMenuBar menuBar;
	private JMenu file, open, compress, edit;
	private JMenuItem exit, pngImage, huffImage, newT, exist, train, comp;
	private JPanel image, himage, time;
	private JLabel first, second, finale;
	private JTextField firstf, secondf, finalef;
	private JLabel imageLabel, himageLabel;
	private JTabbedPane imageTab, himageTab;
	private JPanel statusTab, timeTab;
	private ImageReader imageReader = new ImageReader();
	private ImageLoader imageLoader = new ImageLoader();
	private String fileName, imageName;
	private JToolBar toolBar;
	private JButton popen, hopen, trainhuff, newhuff, updatehuff, compressimage;
	private JTextArea message = new JTextArea();
	private int imageTabsCtr = 0, himageTabsCtr = 0;
	private boolean aFunctionRunning = false;
	
	public GUI() {
		
		super("Multi Channel Image Compressor");
		setLayout(null);
		getContentPane().setBackground(Color.lightGray);
		
		try {
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
		open.setMnemonic(KeyEvent.VK_O);
		exit.setMnemonic(KeyEvent.VK_X);
		
		pngImage = new JMenuItem("PNG image");
		huffImage = new JMenuItem("Huffman coded image");
		pngImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		huffImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		//huffImage.setEnabled(false);
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
		file.add(exit);
		
		//edit.add(train);
		//edit.addSeparator();
		edit.add(compress);
		edit.addSeparator();
		edit.add(comp);
		
		statusTab = new JPanel(new BorderLayout());
		message.setEditable(false);
		DefaultCaret caret = (DefaultCaret)message.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		statusTab.add(new JLabel("Status"), BorderLayout.NORTH);
		statusTab.add((new JScrollPane(message, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)), BorderLayout.CENTER);
		add(statusTab);
		imageTab = new JTabbedPane();
		add(imageTab);
		himageTab = new JTabbedPane();
		add(himageTab);
		timeTab = new JPanel(new BorderLayout());
		add(timeTab);
		
		time = new JPanel();
		time.setLayout(new GridLayout(6, 1));
		time.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		first = new JLabel("STARTED", JLabel.CENTER);
		second = new JLabel("FINISHED", JLabel.CENTER);
		finale = new JLabel("DURATION", JLabel.CENTER);
		
		firstf = new JTextField();
		secondf = new JTextField();
		finalef = new JTextField();
		firstf.setHorizontalAlignment(JTextField.CENTER);
		secondf.setHorizontalAlignment(JTextField.CENTER);
		finalef.setHorizontalAlignment(JTextField.CENTER);
		firstf.setEditable(false);
		secondf.setEditable(false);
		finalef.setEditable(false);
		
		time.add(first);
		time.add(firstf);
		time.add(second);
		time.add(secondf);
		time.add(finale);
		time.add(finalef);
		timeTab.add(new JLabel("Time"), BorderLayout.NORTH);
		timeTab.add(time, BorderLayout.CENTER);
		
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
		//hopen.setEnabled(false);
		//trainhuff.setEnabled(false);
		newhuff.setEnabled(false);
		updatehuff.setEnabled(false);
		compressimage.setEnabled(false);
		toolBar.add(popen);
		toolBar.add(hopen);
		toolBar.addSeparator();
		//toolBar.add(trainhuff);
		//toolBar.addSeparator();
		toolBar.add(newhuff);
		toolBar.add(updatehuff);
		toolBar.addSeparator();
		toolBar.add(compressimage);
		toolBar.setFloatable(false);
		add(toolBar);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
		
		Dimension dimension = getPreferredSize();
		toolBar.setBounds(0, 0, getSize().width - 16, 30);
		imageTab.setBounds(0, 30, (getSize().width / 2) - 14, getSize().height - 310);
		himageTab.setBounds(getSize().width / 2, 30, (getSize().width / 2) - 14, getSize().height - 310);
		statusTab.setBounds(200, 508, getSize().width - 215, 215);
		timeTab.setBounds(0, 508, 195, 215);
		repaint();
		revalidate();
		
		pngImage.addActionListener(this);
		huffImage.addActionListener(this);
		exit.addActionListener(this);
		train.addActionListener(this);
		exist.addActionListener(this);
		comp.addActionListener(this);
		popen.addActionListener(this);
		hopen.addActionListener(this);
		trainhuff.addActionListener(this);
		newT.addActionListener(this);
		newhuff.addActionListener(this);
		updatehuff.addActionListener(this);
		compressimage.addActionListener(this);
		
		imageReader.setGUI(this);
		imageLoader.setGUI(this);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(aFunctionRunning){
			return;
		}
		
		if(event.getSource() == pngImage || event.getSource() == popen) {
			aFunctionRunning = true;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG (*.PNG)", "PNG"));
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showOpenDialog(GUI.this);
			
			if (result == JFileChooser.APPROVE_OPTION) {
				image = new JPanel();
				image.setLayout(new FlowLayout(FlowLayout.LEFT));
				image.setBackground(Color.WHITE);
				imageLabel = new JLabel();
				// himageLabel = new JLabel();
				image.add(imageLabel);
				
				File selectedFile = fileChooser.getSelectedFile();
				Image pImage = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath()).getImage();
				imageLabel.setIcon(new ImageIcon(pImage));
				fileName = "" + selectedFile;
				imageName = "" + fileChooser.getSelectedFile().getName();
				exist.setEnabled(true);
				updatehuff.setEnabled(true);
				newT.setEnabled(true);
				newhuff.setEnabled(true);
				comp.setEnabled(true);
				compressimage.setEnabled(true);
				/*train.setEnabled(true);
				trainhuff.setEnabled(true);
				pngImage.setEnabled(false);
				popen.setEnabled(false);*/
				imageTab.addTab(imageName, null, (new JScrollPane(image, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)), fileName);
				JPanel pnlTab = new JPanel(new BorderLayout());
				pnlTab.setOpaque(false);
				JButton btnClose = new JButton("X");
				btnClose.setForeground(Color.RED);
				btnClose.setOpaque(false);
				btnClose.setContentAreaFilled(false);
				btnClose.setBorderPainted(false);
				btnClose.setMargin(new Insets(0,0,0,0));
				pnlTab.add(new JLabel(imageName + " "), BorderLayout.CENTER);
				pnlTab.add(btnClose, BorderLayout.EAST);
				imageTab.setTabComponentAt(imageTabsCtr, pnlTab);
				btnClose.addActionListener(new TabCloser(imageTab, pnlTab));
				imageTab.setSelectedIndex(imageTabsCtr);
				imageTabsCtr++;
			}
			aFunctionRunning = false;
		} else if(event.getSource() == huffImage || event.getSource() == hopen) {
			aFunctionRunning = true;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("IJK (*.IJK)", "IJK"));
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showOpenDialog(GUI.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				himage = new JPanel();
				himage.setLayout(new FlowLayout(FlowLayout.LEFT));
				himage.setBackground(Color.WHITE);
				himageLabel = new JLabel();
				himage.add(himageLabel);
				
				File selectedFile = fileChooser.getSelectedFile();
				//pngImage.setEnabled(true);
				//popen.setEnabled(true);
				//huffImage.setEnabled(false);
				//hopen.setEnabled(false);
				String filename = "" + selectedFile;
				imageName = "" + fileChooser.getSelectedFile().getName();
				himageTab.addTab(imageName, (new JScrollPane(himage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)));
				JPanel pnlTab = new JPanel(new BorderLayout());
				pnlTab.setOpaque(false);
				JButton btnClose = new JButton("X");
				btnClose.setForeground(Color.RED);
				btnClose.setOpaque(false);
				btnClose.setContentAreaFilled(false);
				btnClose.setBorderPainted(false);
				btnClose.setMargin(new Insets(0,0,0,0));
				pnlTab.add(new JLabel(imageName + " "), BorderLayout.CENTER);
				pnlTab.add(btnClose, BorderLayout.EAST);
				himageTab.setTabComponentAt(himageTabsCtr, pnlTab);
				btnClose.addActionListener(new TabCloser(himageTab, pnlTab));
				himageTab.setSelectedIndex(himageTabsCtr);
				himageTabsCtr++;
				new Thread() { 
					public void run() {
						secondf.setText(null);
						finalef.setText(null);
						DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
						Date date = new Date();
						firstf.setText(dateFormat.format(date));
						
						BufferedImage hImage = imageLoader.getImage(filename);
						himageLabel.setIcon(new ImageIcon(hImage));
						
						DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss:SS");
						Date date1 = new Date();
						secondf.setText(dateFormat1.format(date1));

						long duration  = date1.getTime() - date.getTime();

						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
						long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
						long diffInMilli = TimeUnit.MILLISECONDS.toMillis(duration);
						
						finalef.setText(diffInHours + ":" + diffInMinutes + ":" + diffInSeconds + ":" + diffInMilli);
						aFunctionRunning = false;
					}
				}.start();
			}
		}/* else if(event.getSource() == train || event.getSource() == trainhuff) {
			int i = 0;
			new Thread() {
				public void run() {
					train.setEnabled(false);
					trainhuff.setEnabled(false);
					
					secondf.setText(null);
					finalef.setText(null);
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
					Date date = new Date();
					firstf.setText(dateFormat.format(date));
					
					imageReader.loadImage(fileName);
					imageReader.countFreq();
					imageReader.assignTree(fileName);
					
					exist.setEnabled(true);
					updatehuff.setEnabled(true);
					newT.setEnabled(true);
					newhuff.setEnabled(true);
					
					DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss:SS");
					Date date1 = new Date();
					secondf.setText(dateFormat1.format(date1));
					
					long duration  = date1.getTime() - date.getTime();

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
					long diffInMilli = TimeUnit.MILLISECONDS.toMillis(duration);
					
					finalef.setText(diffInHours + ":" + diffInMinutes + ":" + diffInSeconds + ":" + diffInMilli);
				}
			}.start();
		}*/ else if(event.getSource() == exist || event.getSource() == updatehuff) {
			aFunctionRunning = true;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("HUFF (*.HUFF)", "HUFF"));
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showOpenDialog(GUI.this);
			
			if (result == JFileChooser.APPROVE_OPTION) {
				new Thread() {
					public void run() {
						File selectedFile = fileChooser.getSelectedFile();
						String huffFileName = "" + selectedFile;
						//exist.setEnabled(false);
						//updatehuff.setEnabled(false);
						//newT.setEnabled(false);
						//newhuff.setEnabled(false);
						
						secondf.setText(null);
						finalef.setText(null);
						DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
						Date date = new Date();
						firstf.setText(dateFormat.format(date));
						
						int selectedIndex = imageTab.getSelectedIndex();
						String filename = imageTab.getToolTipTextAt(selectedIndex);
						imageReader.loadImage(filename);
						try{
							huffFileName = huffFileName.substring(0, huffFileName.length()-1);
							imageReader.counters = ImageLoader.getCounters(huffFileName);
						}catch(IOException e){}
						imageReader.countFreq();
						imageReader.assignTree(filename);
						imageReader.createHuffFile(fileName);
						//comp.setEnabled(true);
						//compressimage.setEnabled(true);
						
						DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss:SS");
						Date date1 = new Date();
						secondf.setText(dateFormat.format(date1));
						
						long duration  = date1.getTime() - date.getTime();

						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
						long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
						long diffInMilli = TimeUnit.MILLISECONDS.toMillis(duration);
						
						finalef.setText(diffInHours + ":" + diffInMinutes + ":" + diffInSeconds + ":" + diffInMilli);
					}
				}.start();
			}
			aFunctionRunning = false;
		} else if(event.getSource() == comp || event.getSource() == compressimage) {
			aFunctionRunning = true;
			new Thread() { 
				public void run() {
					//comp.setEnabled(false);
					//compressimage.setEnabled(false);
					
					secondf.setText(null);
					finalef.setText(null);
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
					Date date = new Date();
					firstf.setText(dateFormat.format(date));
					
					try{
						imageReader.createNewImageFile(fileName);
						//huffImage.setEnabled(true);
						//hopen.setEnabled(true);
						int reply = JOptionPane.showConfirmDialog(null, "Would you like to open compressed image?", "Compressed Image Generated", JOptionPane.YES_NO_OPTION);
						if(reply == JOptionPane.YES_OPTION){
							aFunctionRunning = true;
							himage = new JPanel();
							himage.setLayout(new FlowLayout(FlowLayout.LEFT));
							himage.setBackground(Color.WHITE);
							himageLabel = new JLabel();
							himage.add(himageLabel);
							String filename = fileName.substring(0, fileName.length()-3) + "IJK";
							imageName = imageName.substring(0, imageName.length()-3) + "IJK";
							himageTab.addTab(imageName, (new JScrollPane(himage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)));
							JPanel pnlTab = new JPanel(new BorderLayout());
							pnlTab.setOpaque(false);
							JButton btnClose = new JButton("X");
							btnClose.setForeground(Color.RED);
							btnClose.setOpaque(false);
							btnClose.setContentAreaFilled(false);
							btnClose.setBorderPainted(false);
							btnClose.setMargin(new Insets(0,0,0,0));
							pnlTab.add(new JLabel(imageName + " "), BorderLayout.CENTER);
							pnlTab.add(btnClose, BorderLayout.EAST);
							himageTab.setTabComponentAt(himageTabsCtr, pnlTab);
							btnClose.addActionListener(new TabCloser(himageTab, pnlTab));
							himageTab.setSelectedIndex(himageTabsCtr);
							himageTabsCtr++;
							new Thread() { 
								public void run() {
									secondf.setText(null);
									finalef.setText(null);
									DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
									Date date = new Date();
									firstf.setText(dateFormat.format(date));
									
									BufferedImage hImage = imageLoader.getImage(filename);
									himageLabel.setIcon(new ImageIcon(hImage));
									
									DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss:SS");
									Date date1 = new Date();
									secondf.setText(dateFormat1.format(date1));

									long duration  = date1.getTime() - date.getTime();

									long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
									long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
									long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
									long diffInMilli = TimeUnit.MILLISECONDS.toMillis(duration);
									
									finalef.setText(diffInHours + ":" + diffInMinutes + ":" + diffInSeconds + ":" + diffInMilli);
									aFunctionRunning = false;
								}
							}.start();
						}
					}catch(NullPointerException e){
						display("Huffman Tree not yet generated!");
					}finally{
						DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss:SS");
						Date date1 = new Date();
						secondf.setText(dateFormat1.format(date1));
						
						long duration  = date1.getTime() - date.getTime();

						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
						long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
						long diffInMilli = TimeUnit.MILLISECONDS.toMillis(duration);
						
						finalef.setText(diffInHours + ":" + diffInMinutes + ":" + diffInSeconds + ":" + diffInMilli);
						aFunctionRunning = false;
					}
				}
			}.start();
		} else if(event.getSource() == newT || event.getSource() == newhuff) {
			aFunctionRunning = true;
			new Thread() {
				public void run() {
					//exist.setEnabled(false);
					//updatehuff.setEnabled(false);
					//newT.setEnabled(false);
					//newhuff.setEnabled(false);
					
					secondf.setText(null);
					finalef.setText(null);
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
					Date date = new Date();
					firstf.setText(dateFormat.format(date));
					// long start = System.currentTimeMillis();
					long start = System.nanoTime();
					long milliseconds0 = start / 1000000;
					long seconds0 = start / 1000000000;
					long minutes0 = seconds0 / 60;
					long hour0 = seconds0 / 3600;
					display(hour0 + ":" + minutes0 + ":" + seconds0 + ":" + milliseconds0);
					
					int selectedIndex = imageTab.getSelectedIndex();
					String filename = imageTab.getToolTipTextAt(selectedIndex);
					imageReader.loadImage(filename);
					imageReader.countFreq();
					imageReader.assignTree(filename);
					imageReader.createHuffFile(filename);
					//comp.setEnabled(true);
					//compressimage.setEnabled(true);
					
					DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss:SS");
					Date date1 = new Date();
					secondf.setText(dateFormat1.format(date1));
					// long end = System.currentTimeMillis();
					long end = System.nanoTime();
					long milliseconds1 = end / 1000000;
					long seconds1 = end / 1000000000;
					long minutes1 = seconds1 / 60;
					long hour1 = seconds1 / 3600;
					display(hour1 + ":" + minutes1 + ":" + seconds1 + ":" + milliseconds1);
					
					/*long duration  = date1.getTime() - date.getTime();

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
					long diffInMilli = TimeUnit.MILLISECONDS.toMillis(duration);*/
					long duration = end - start;
					long miduration = milliseconds1 - milliseconds0;
					long sduration = seconds1 - seconds0;
					long mduration = minutes1 - minutes0;
					long hduration = hour1 - hour0;
					
					// finalef.setText(diffInHours + ":" + diffInMinutes + ":" + diffInSeconds + ":" + diffInMilli);
					long durMilli = TimeUnit.MILLISECONDS.convert(duration, TimeUnit.NANOSECONDS);
					long durSec = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
					display("" + duration + " " + start + " " + end);
					display(hduration + ":" + mduration + ":" + sduration + ":" + miduration);
					finalef.setText(durSec + ":" + durMilli);
					aFunctionRunning = false;
				}
			}.start();
		} else if(event.getSource() == exit) {
			System.exit(0);
		}
	}
	
	public void display(String stats) {	
		message.append(stats + "\n");
	}
	
	public static void main(String[] args) {
		new GUI();
	}
	
	private class TabCloser implements ActionListener{
		JTabbedPane pane = null;
		JPanel cmpTab = null;
		
		public TabCloser(JTabbedPane pane, JPanel cmpTab){
			this.pane = pane;
			this.cmpTab = cmpTab;
		}
		
		public void actionPerformed(ActionEvent e){
			int index = pane.indexOfTabComponent(cmpTab);
			pane.remove(index);
			if(pane == imageTab){
				imageTabsCtr--;
				if(imageTabsCtr == 0){
					exist.setEnabled(false);
					updatehuff.setEnabled(false);
					newT.setEnabled(false);
					newhuff.setEnabled(false);
					comp.setEnabled(false);
					compressimage.setEnabled(false);
				}
			}else if(pane == himageTab){
				himageTabsCtr--;
			}
			System.gc();
		}
	}
}