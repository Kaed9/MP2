import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class ImageReader{
	BufferedImage image = null;
	Raster raster = null;
	//Pixel[][] pixel = null;
	HuffmanNode[] counters = null;
	int distinctColorCtr = 0;
	HuffmanNode tree = null;
	
	public void createNewHuffman(String file){
		loadImage(file);
		countFreq();
		tree = createHuffmanTree(counters);
		codeAssignment(tree, "");
		System.out.println("Code Assigned");
		// createHuffFile(file);
		// createNewImageFile(file);
		// ImageLoader load = new ImageLoader(file);
	}
	
	public void loadImage(String file){
		try{
			image = ImageIO.read(new File(file));
			raster = image.getRaster();
			//pixel = new Pixel[raster.getHeight()][raster.getWidth()];
		}catch(IOException e){return;}
		System.out.println("Image height = " + raster.getHeight() + ", width = " + raster.getWidth());
		/*for(int i = 0; i < pixel.length; i++){
			for(int j = 0; j < pixel[i].length; j++){
				int[] buffer = new int[4];
				buffer = raster.getPixel(j, i, buffer);
				pixel[i][j] = new Pixel(buffer[0], buffer[1], buffer[2], j, i);
			}
		}*/
	}
	
	public void countFreq(){
		System.out.println("Counting Distinct Colors");
		counters = new HuffmanNode[raster.getHeight()*raster.getWidth()];
		Pixel pixel = null;
		for(int i = 0; i < raster.getHeight(); i++){
			for(int j = 0; j < raster.getWidth(); j++){
				int[] buffer = new int[4];
				buffer = raster.getPixel(j, i, buffer);
				pixel = new Pixel(buffer[0], buffer[1], buffer[2], j, i);
				boolean distinct = true;
				if(distinctColorCtr > 0){
					for(int k = distinctColorCtr-1; k >= 0; k--){
						if(pixel.getRed() == counters[k].key.getRed() && pixel.getGreen() == counters[k].key.getGreen() && pixel.getBlue() == counters[k].key.getBlue()){
							counters[k].ctr++;
							distinct = false;
							break;
						}
					}
				}
				if(distinct){
					counters[distinctColorCtr] = new HuffmanNode(pixel);
					counters[distinctColorCtr].ctr = 1;
					distinctColorCtr++;
				}
			}
			System.out.println("Row " + i + "done");
		}
		System.out.println("Distinct Colors Counted");
		
		for(int i = 0; i < distinctColorCtr-1; i++){
			for(int j = 0; j < distinctColorCtr-i-1; j++){
				if(counters[j].ctr > counters[j+1].ctr){
					HuffmanNode buffer = new HuffmanNode(new Pixel(counters[j].key.getRed(),counters[j].key.getGreen(),counters[j].key.getBlue(),counters[j].key.x,counters[j].key.y));
					buffer.ctr = counters[j].ctr;
					counters[j] = counters[j+1];
					counters[j+1] = buffer;
					buffer = null;
				}
			}
		}
		System.out.println("Distinct Colors Sorted");
		HuffmanNode[] buffer = new HuffmanNode[distinctColorCtr];
		for(int i = 0; i < distinctColorCtr; i++){
			buffer[i] = counters[i];
		}
		counters = null;
		counters = new HuffmanNode[distinctColorCtr];
		for(int i = 0 ; i < distinctColorCtr; i++){
			counters[i] = buffer[i];
		}
	}
	
	public static HuffmanNode createHuffmanTree(HuffmanNode[] counters){
		PrioQueue prioQueue = new PrioQueue();
		for(int i = 0; i < counters.length; i++){
			prioQueue.enqueue(counters[i]);
		}
		while(prioQueue.list.length > 1){
			HuffmanNode emptyNode = new HuffmanNode(prioQueue.dequeue(), prioQueue.dequeue());
			emptyNode.ctr = emptyNode.left.ctr + emptyNode.right.ctr;
			prioQueue.enqueue(emptyNode);
		}
		
		System.out.println("Tree Generated");
		HuffmanNode tree = prioQueue.dequeue();
		return tree;
	}
	
	public void codeAssignment(HuffmanNode node, String traversalHistory){
		if(node.key == null){
			codeAssignment(node.left, traversalHistory+"0");
			codeAssignment(node.right, traversalHistory+"1");
		}else{
			for(int i = 0; i < distinctColorCtr; i++){
				if(counters[i].key.getRed() == node.key.getRed() && counters[i].key.getGreen() == node.key.getGreen() && counters[i].key.getBlue() == node.key.getBlue()){
					counters[i].code = traversalHistory;
				}
			}
		}
	}
	
	public void createHuffFile(String file){
		try{
			String huffName = file.substring(0, file.length()-3) + "HUFF";
			// String huffName = file.substring()
			PrintWriter writer = new PrintWriter(huffName, "UTF-8");
			for(int i = 0; i < counters.length; i++){
				String rgb = "" + (char)counters[i].key.getRed() + (char)counters[i].key.getGreen() + (char)counters[i].key.getBlue();
				String ctrBinary = Integer.toBinaryString(counters[i].ctr), zeroes = "00000000000000000000000000000000";
				ctrBinary = zeroes.substring(0, (ctrBinary.length()<16?16-ctrBinary.length():0)) + ctrBinary;
				//System.out.print("Ctr = |" + ctrBinary + "|" + Integer.parseInt(ctrBinary.substring(ctrBinary.length()-8, ctrBinary.length()),2) + " ");
				ctrBinary = "" + (char)(Integer.parseInt(ctrBinary.substring(0, ctrBinary.length()-8),2)) + (char)(Integer.parseInt(ctrBinary.substring(ctrBinary.length()-8, ctrBinary.length()),2));
				writer.print(rgb+ctrBinary);
				//System.out.println("RGB(" + counters[i].key.getRed() + ", " + counters[i].key.getGreen() + ", " + counters[i].key.getBlue() + ") = (" + rgb + "), ctr = " + counters[i].ctr + ", |" + ctrBinary + "| Codedbit = |" + codedBit + "| = " + counters[i].code);
			}
			writer.close();
			System.out.println("Huff File Generated");
		}catch(IOException e){}
	}
	
	// ----------
	public void createNewImageFile(String file){
		try{
			String huffName = file.substring(0, file.length()-3) + "IJK";
			PrintWriter writer = new PrintWriter(huffName);
			writer.println(raster.getHeight()+"\n"+raster.getWidth());
			String binaryImage = "";
			String zeroes = "00000000";
			Pixel pixel = null;
			System.out.println("Converting Image to Binary String");
			for(int i = 0; i < raster.getHeight(); i++){
				for(int j = 0; j < raster.getWidth(); j++){
					int[] buffer = new int[4];
					buffer = raster.getPixel(j, i, buffer);
					pixel = new Pixel(buffer[0], buffer[1], buffer[2], j, i);
					boolean foundCode = false;
					for(int k = counters.length-1; k >= 0; k--){
						if(pixel.getRed() == counters[k].key.getRed() && pixel.getGreen() == counters[k].key.getGreen() && pixel.getBlue() == counters[k].key.getBlue()){
							binaryImage += counters[k].code;
							foundCode = true;
							while(binaryImage.length()>=7){
								writer.print((char)Integer.parseInt(binaryImage.substring(0, 7), 2));
								binaryImage = binaryImage.substring(7);
							}
							break;
						}
					}
					if(!foundCode){
						System.out.println("Error! Pixel(" + j + ", " + i + ")");
					}
				}
			}
			while(binaryImage.length()%7!=0){
				binaryImage += "0";
			}
			System.out.println("Converting Image to Binary String Done");
			System.out.println("Converting Binary String to ASCII and Save to File");
			for(int i = 0; i < binaryImage.length();){
				writer.print((char)Integer.parseInt(binaryImage.substring(i, i+7), 2));
				i+=7;
			}
			System.out.println("Converting Binary String to ASCII and Save to File Done");
			writer.close();
			System.out.println("Image File Created");
			/*for(int i = 0; i < pixel.length; i++){
				for(int j = 0; j < pixel[i].length; j++){
					System.out.print("Pixel(" + i + ", " + j +") = (" + pixel[i][j].getRed() + ", " + pixel[i][j].getGreen() + ", " + pixel[i][j].getBlue() + ")");
					for(int k = counters.length-1; k >= 0; k--){
						if(pixel[i][j].getRed() == counters[k].key.getRed() && pixel[i][j].getGreen() == counters[k].key.getGreen() && pixel[i][j].getBlue() == counters[k].key.getBlue()){
							System.out.println(" Code = |" + counters[k].code + "|");
							break;
						}
					}
				}
			}*/
		}catch(IOException e){}
	}
	
	/*public static void main(String[] args){
		ImageReader r = new ImageReader();
		r.createNewHuffman("sample4.png");
	}*/
}