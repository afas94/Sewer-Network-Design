package Layout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import Hydraulic_Design.DataHandler;
import Hydraulic_Design.Manhole;
import Hydraulic_Design.Section;

public class Random_Layout {
	
	public BufferedReader Lector;
	public File file;

	
	
	
	public int numManholes;
	public int numSections;
	
	
	public ArrayList<Integer> manholeId;
	public ArrayList<Double> Qd;
	public ArrayList<Double> posX;
	public ArrayList<Double> posY;
	public ArrayList<Double> elevation;
	public ArrayList<Integer> sectionIdUp;
	public ArrayList<Integer> sectionIdDown;
	
	
	
	
	public Random_Layout(File file) throws FileNotFoundException{
		this.file = file; 
		Lector = new BufferedReader(new FileReader(file));
		manholeId  = new ArrayList<>();
		Qd = new ArrayList<>();
		posX = new ArrayList<>();
		posY = new ArrayList<>();
		elevation = new ArrayList<>();
		sectionIdDown = new ArrayList<>();
		sectionIdUp = new ArrayList<>();
		
	} 
	
	
	
	/**
	 * Generator of the file with random costs
	 * @param myRndGenerator
	 * @throws IOException
	 */
	public void generateRandomCosts(Random myRndGenerator) throws IOException {
		
//		Lector = new BufferedReader(new FileReader(file));
		
		
		//Archivo inicial
		String line = Lector.readLine();
		
			
		/*
		 * Reads the first line with the number of Manholes of the network
		 */
		if (line != null) {
			String[] matriz = line.split(" ");
			numManholes = Integer.parseInt(matriz[1]);
		}

		/*
		 * Reads the input data for all the manholes (id, x, y, z)
		 */
		for (int i = 0; i < numManholes; i++) {

			line = Lector.readLine();

			if (line != null) {
				String[] matriz = line.split(" ");
				
				manholeId.add(Integer.parseInt(matriz[0]));
			
				Qd.add(Double.parseDouble(matriz[1]));
				posX.add(Double.parseDouble(matriz[2]));
				posY.add(Double.parseDouble(matriz[3]));
				elevation.add(Double.parseDouble(matriz[4]));

				
			}

		}
		
		/*
		 * Reads the number of Sections of the network
		 */
		line = Lector.readLine();
		if (line != null) {
			String[] matriz = line.split(" ");
			numSections = Integer.parseInt(matriz[1]);
		}

		/*
		 * Reads the input data for all the sections (id_up, id_down,ini, Qd)
		 */
		for (int i = 0; i < numSections; i++) {

			line = Lector.readLine();

			if (line != null) {

				String[] matriz = line.split(" ");

				/**
				 * Ids of the manholes forming the arc (i,j)
				 */
				sectionIdUp.add(Integer.parseInt(matriz[0]));
				sectionIdDown.add(Integer.parseInt(matriz[1]));
							
			}
		}
		
		/*
		 * Writes the input data for all the sections and manholes
		 */
		File file2 = new File("XPRESS/Datos_de_Entrada/InputData.txt");
		// if the file does not exist, then create it
		if (!file2.exists()) {
			file2.createNewFile();
		}
		
		FileWriter fw= new FileWriter(file2.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("K:[");bw.newLine();
		bw.write("I");bw.newLine();
		bw.write("C");bw.newLine();
		bw.write("]");bw.newLine();
		
		/*
		 * Writes the offer/demand of each manhole
		 */
		bw.write("b:[");bw.newLine();
		for (int i = 0; i < numManholes; i++) {
			bw.write("("+(i+1)+")"+ Qd.get(i));bw.newLine();
		}
		bw.write("]");bw.newLine();
		
		/*
		 * Writes the x coordinate of each manhole 
		 */
		bw.write("posX:[");bw.newLine();
		for (int i = 0; i < numManholes; i++) {
			bw.write("("+(i+1)+")"+ posX.get(i));bw.newLine();
		}
		bw.write("]");bw.newLine();
		
		/*
		 * Writes the y coordinate of each manhole 
		 */
		bw.write("posY:[");bw.newLine();
		for (int i = 0; i < numManholes; i++) {
			bw.write("("+(i+1)+")"+ posY.get(i));bw.newLine();
		}
		bw.write("]");bw.newLine();
		
		/*
		 * Writes the z coordinate of each manhole 
		 */
		bw.write("e:[");bw.newLine();
		for (int i = 0; i < numManholes; i++) {
			bw.write("("+(i+1)+")"+ elevation.get(i));bw.newLine();
		}
		bw.write("]");bw.newLine();
		
		
		/*
		 * Writes the x coordinate of each manhole 
		 */
		bw.write("c:[");bw.newLine();
		for (int i = 0; i < numSections; i++) {
			
			double randomN=  myRndGenerator.nextDouble();
			double randomN2=  myRndGenerator.nextDouble();
			bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ randomN );bw.newLine();
			bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ randomN2);bw.newLine();
		}
		bw.write("]");bw.newLine();
		
		/*
		 * Writes the offer/demand of each manhole
		 */
		bw.write("intercept:[");bw.newLine();
		for (int i = 0; i < numSections; i++) {
			
			double randomN= myRndGenerator.nextDouble();
			double randomN2= myRndGenerator.nextDouble();
			bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ randomN);bw.newLine();
			bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ randomN2);bw.newLine();
		}
		bw.write("]");bw.newLine();
		
		bw.close();
		
	}

	
	
	
}
