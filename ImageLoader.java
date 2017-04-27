import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class ImageLoader extends JPanel{
	HuffmanNode[] counters = null;
	HuffmanNode tree = null;
	HuffmanNode node = null;
	String binaryCode = "";
	int height , width ;
	int ctr = 0;
	//JPanel[][] pixels = null;
	BufferedImage image = null;
	
	/*public ImageLoader(String file){
		try{
			String file1 = file.substring(0, file.length()-3);;
			file1 += "SAMPLE";
			BufferedReader reader = new BufferedReader(new FileReader(file1));
			String s = reader.readLine();
			height = Integer.parseInt(s);
			s = reader.readLine();
			width = Integer.parseInt(s);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// pixels = new JPanel[height][width];
			// setLayout(new GridLayout(height, width));
			// System.out.println("Creating pixels");
			// for(int i = 0 ; i < height;i++){
				// for(int j = 0; j < width; j++){
					// pixels[i][j] = new JPanel();
					pixels[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GREEN));
					// add(pixels[i][j]);
				// }
			// }
			// width2 = 650;
			// height2 = (int)(((float)this.height/(float)this.width)*650.0);
			System.out.println("Creating pixels Done");
			System.out.println("Loading Image");
			getCounters(file);
			getBinaryCode(file);
		}catch(IOException e){}
	}*/
	
	public BufferedImage getImage(String file) {
		
		try{
			String file1 = file.substring(0, file.length()-3);;
			file1 += "IJK";
			BufferedReader reader = new BufferedReader(new FileReader(file1));
			String s = reader.readLine();
			height = Integer.parseInt(s);
			s = reader.readLine();
			width = Integer.parseInt(s);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// pixels = new JPanel[height][width];
			// setLayout(new GridLayout(height, width));
			// System.out.println("Creating pixels");
			// for(int i = 0 ; i < height;i++){
				// for(int j = 0; j < width; j++){
					// pixels[i][j] = new JPanel();
					// pixels[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GREEN));
					// add(pixels[i][j]);
				// }
			// }
			// width2 = 650;
			// height2 = (int)(((float)this.height/(float)this.width)*650.0);
			System.out.println("Creating pixels Done");
			System.out.println("Loading Image");
			getCounters(file);
			getBinaryCode(file);
		}catch(IOException e){}
		
		return image;
	}
	
	public void getCounters(String file) throws IOException{
		file = file.substring(0, file.length()-3);
		file += "HUFF";
		String data = "";
		int line, fileLength = 0;
		HuffmanNode[] counters = null;
		short[] dataByte = null;
		//BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file, Charset.forName("UTF-8"))));
		InputStreamReader reader = null;
		try{
			System.out.println("Initiating Huff File Reading");
			reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			while((line = reader.read()) != -1){fileLength++;}
			reader.close();
			dataByte = new short[fileLength];
			this.counters = new HuffmanNode[(int)((float)fileLength/5.0)];
			reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			int i = 0;
			while((line = reader.read()) != -1){
				dataByte[i++] = (short)line;
			}
		}finally{
			reader.close();
		}
		System.out.println("Reading Huff File");
		for(int i = 0; i < dataByte.length-2; ){
			//System.out.println("Ctr = " + ctr  + ", i = " + i);
			this.counters[ctr] = new HuffmanNode(new Pixel((int)dataByte[i++], (int)dataByte[i++], (int)dataByte[i++], 0, 0));
			String ctr1 = Integer.toBinaryString((int)dataByte[i++]), ctr2 = Integer.toBinaryString((int)dataByte[i++]), zeroes = "00000000";
			//System.out.print("Ctr1 = |" + ctr1 + "|, Ctr2 = |" + ctr2 + "| ");
			ctr1 = zeroes.substring(0, (ctr1.length()<8?8-ctr1.length():0)) + ctr1;
			ctr2 = zeroes.substring(0, (ctr2.length()<8?8-ctr2.length():0)) + ctr2;
			this.counters[ctr].ctr = Integer.parseInt(ctr1+ctr2, 2);
			/*int codeLength = Integer.parseInt((Integer.toBinaryString((int)dataByte[i++]) + Integer.toBinaryString((int)dataByte[i++]) + Integer.toBinaryString((int)dataByte[i++])), 2);
			int x = 0;
			String code = "";
			while(dataByte[i+x] != 46){
				code += ""+(char)dataByte[i+x];
				x++;
			}
			code = Integer.toBinaryString(Integer.parseInt(code));
			while(code.length()< codeLength){
				code = "0" + code;
			}
			this.counters[ctr].code = code;
			//System.out.println("ColorNo."+ctr+"("+this.counters[ctr].key.getRed()+","+this.counters[ctr].key.getGreen()+","+this.counters[ctr].key.getBlue()+") ctr = "+ this.counters[ctr].ctr + ", code = |" +this.counters[ctr].code+"|");
			i += x+1;*/
			ctr++;
		}
		tree = ImageReader.createHuffmanTree(this.counters);
		/*for(int i = 0; i < ctr; i++){
			System.out.println("ColorNo."+i+"("+counters[i].key.getRed()+","+counters[i].key.getGreen()+","+counters[i].key.getBlue()+") ctr = "+ counters[i].ctr + ", code = |" +counters[i].code+"|");
		}*/
		System.out.println("Huff File Reading Done");
	}
	public void getBinaryCode(String file) throws IOException{
		/*JFrame frame = new JFrame("Try");
		frame.setSize(700,700);
		frame.add(this, BorderLayout.CENTER);
		frame.setVisible(true);*/
		file = file.substring(0, file.length()-3);
		file += "IJK";
		int line;
		InputStreamReader reader = null;
		int x = 0, y = 0;
		Pixel rgb = null;
		//System.out.println("Ctr = " + ctr);
		System.out.println("Reading Image File");
		try{
			reader = new InputStreamReader(new FileInputStream(file));
			int lineNo = 0;
			while(lineNo != 2){
				int g = reader.read();
				//System.out.print((char)g);
				if(g == '\n'){
					lineNo++;
				}
			}
			node = tree;
			int pixNo = 0;
			while((line = reader.read()) != -1){
				String character = Integer.toBinaryString(line);
				while(character.length()<7){
					character = "0" + character;
				}
				//binaryCode += character;
				for(int i = 0; i < character.length(); i++){
					if(character.charAt(i) == '1'){
						node = node.right;
					}else if(character.charAt(i) == '0'){
						node = node.left;
					}
					if(!node.isKeyNull()){
						//System.out.println("found equal! " + pixNo++);
						rgb = node.key;
						try{
							image.setRGB(x++, y, rgb.getRGB());
							//pixels[y][x++].setBackground(node.key);
						}catch(ArrayIndexOutOfBoundsException e){
							//System.out.println("Error! (" + (x-1) + ", " + y + ")");
							//e.printStackTrace();
						}
						if(x >= width){
							x = 0;
							y++;
						}
						repaint();
						node = tree;
					}
				}
				/*for(int j = ctr-1; j >= 0; j--){
					if(this.counters[j].code.equals(binaryCode)){
						rgb = this.counters[j].key;
						//System.out.println("Pixel set at (" + x + ", " + y + "), rgb = (" + rgb.getRed() + ", " + rgb.getGreen() + ", " + rgb.getBlue() + ") code = " + this.counters[j].code);
						try{
							pixels[y][x++].setBackground(this.counters[j].key);
						}catch(ArrayIndexOutOfBoundsException e){
							System.out.println("Error! (" + (x-1) + ", " + y + ")");
							e.printStackTrace();
						}
						if(x >= width){
							x = 0;
							y++;
						}
						binaryCode = "";
						//System.out.println(i);
						repaint();
						break;
					}
				}*/
			}
		}finally{
			reader.close();
		}
		System.out.println("Reading Image File Done");
		/*for(int i = 0; i < data.length()-1;i++){
			String character = Integer.toBinaryString((int)data.charAt(i));
			while(character.length()<8){
				character = "0" + character;
			}
			System.out.println(i);
			binaryCode += character;
		}*/
		//System.out.println(binaryCode + "\nLength = " + ((float)binaryCode.length()/1.0));
		System.out.println("Printing Image File");
		/*for(int i = 0, start = 0; i <= binaryCode.length(); i++){
			for(int j = ctr-1; j >= 0; j--){
				if(this.counters[j].code.equals(binaryCode.substring(start, i))){
					rgb = this.counters[j].key;
					//System.out.println("Pixel set at (" + x + ", " + y + "), rgb = (" + rgb.getRed() + ", " + rgb.getGreen() + ", " + rgb.getBlue() + ") code = " + this.counters[j].code);
					try{
						pixels[y][x++].setBackground(this.counters[j].key);
					}catch(ArrayIndexOutOfBoundsException e){
						System.out.println("Error!");
					}
					if(x >= width){
						x = 0;
						y++;
					}
					start = i;
					//System.out.println(i);
					repaint();
					break;
				}
			}
		}*/
		/*for(int i = 0; i < binaryCode.length(); i++){
			if(binaryCode.charAt(i) == '1'){
				node = tree.right;
			}else if(binaryCode.charAt(i) == '0'){
				node = tree.left;
			}
			if(!(node.key == null)){
				rgb = node.key;
				try{
					image.setRGB(x++, y, rgb.getRGB());
					//pixels[y][x++].setBackground(node.key);
				}catch(ArrayIndexOutOfBoundsException e){
					System.out.println("Error! (" + (x-1) + ", " + y + ")");
					e.printStackTrace();
				}
				if(x >= width){
					x = 0;
					y++;
				}
				repaint();
				node = tree;
				binaryCode = binaryCode.substring(i);
				System.out.println(binaryCode);
			}
		}*/
		System.out.println("Printing Image File Done");
	}
	/*
	int width2, height2;
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Graphics2D g2 = (Graphics2D)g;
		g.drawImage(image, 0, 0, 650, height2, null);
	}*/
}