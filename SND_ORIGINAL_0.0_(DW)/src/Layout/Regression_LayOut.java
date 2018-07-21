package Layout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class Regression_LayOut {

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
	
	public Regression_LayOut(File file) throws FileNotFoundException{
		
		this.file = file; 
		Lector = new BufferedReader(new FileReader(file));
	}
	
	
	public void generate_LR_Costs(HashMap<String, SimpleRegression> myRegressions, SimpleRegression GnlRegresion) throws IOException {
		// TODO Auto-generated method stub
		
		Lector = new BufferedReader(new FileReader(file));
//		double mycost = myRndGenerator.nextDouble()*1000000.0;
		manholeId  = new ArrayList<>();
		Qd = new ArrayList<>();
		posX = new ArrayList<>();
		posY = new ArrayList<>();
		elevation = new ArrayList<>();
		sectionIdDown = new ArrayList<>();
		sectionIdUp = new ArrayList<>();
		
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
		// if file doesnt exists, then create it
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
//			String key  = sectionIdUp.get(i)<sectionIdDown.get(i)?""+(sectionIdUp.get(i)-1)+","+(sectionIdDown.get(i)-1):""+(sectionIdDown.get(i)-1)+","+(sectionIdUp.get(i)-1);
//			if((myRegressions.get(key).getSlope()+"").equals("NaN")){
//				bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ GnlRegresion.getSlope());bw.newLine();
//				bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ GnlRegresion.getSlope());bw.newLine();		
//			}else{
//			bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ myRegressions.get(key).getSlope());bw.newLine();
//			bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ myRegressions.get(key).getSlope());bw.newLine();
//			}
			
			   String key1  = (sectionIdUp.get(i)-1)+","+(sectionIdDown.get(i)-1);
               String key2  = (sectionIdDown.get(i)-1)+","+(sectionIdUp.get(i)-1);
               double cij,cji=0;
               if(myRegressions.get(key1)!=null && !(myRegressions.get(key1).getSlope()+"").equals("NaN")){
                     cij = myRegressions.get(key1).getSlope();
                     if(cij<0){
                            cij  =GnlRegresion.getSlope();
                     }
               }else{
                     cij =GnlRegresion.getSlope();
               }
               if(myRegressions.get(key2)!=null && !(myRegressions.get(key2).getSlope()+"").equals("NaN")){
                     cji = myRegressions.get(key2).getSlope();
                     if (cji<0) {
                            cji=GnlRegresion.getSlope();
                     }
               }else{
                     cji = GnlRegresion.getSlope();
               }
               
               bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ cij);bw.newLine();
               bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ cji);bw.newLine();

			
		}
		bw.write("]");bw.newLine();
		
		/*
		 * Writes the offer/demand of each manhole
		 */
		bw.write("intercept:[");bw.newLine();
		for (int i = 0; i < numSections; i++) {
//			String key  = sectionIdUp.get(i)<sectionIdDown.get(i)?""+(sectionIdUp.get(i)-1)+","+(sectionIdDown.get(i)-1):""+(sectionIdDown.get(i)-1)+","+(sectionIdUp.get(i)-1);
//			if((myRegressions.get(key).getSlope()+"").equals("NaN")){
//				bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ GnlRegresion.getIntercept());bw.newLine();
//				bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ GnlRegresion.getIntercept());bw.newLine();		
//			}else{
//			bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ myRegressions.get(key).getIntercept());bw.newLine();
//			bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ myRegressions.get(key).getIntercept());bw.newLine();
//			}
			
			   String key1  = (sectionIdUp.get(i)-1)+","+(sectionIdDown.get(i)-1);
               String key2  = (sectionIdDown.get(i)-1)+","+(sectionIdUp.get(i)-1);
               double cij,cji=0;
               if(myRegressions.get(key1)!=null && !(myRegressions.get(key1).getIntercept()+"").equals("NaN")){
                     cij = myRegressions.get(key1).getIntercept();
                     if(cij<0){
                            cij  =GnlRegresion.getIntercept();
                     }
               }else{
                     cij =GnlRegresion.getIntercept();
               }
               if(myRegressions.get(key2)!=null && !(myRegressions.get(key2).getIntercept()+"").equals("NaN")){
                     cji = myRegressions.get(key2).getIntercept();
                     if (cji<0) {
                            cji=GnlRegresion.getIntercept();
                     }
               }else{
                     cji = GnlRegresion.getIntercept();
               }
               
               bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ cij);bw.newLine();
               bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ cji);bw.newLine();

		
		}
		bw.write("]");bw.newLine();
		
		bw.close();
		
		
		
	}
}
