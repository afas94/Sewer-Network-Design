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

import Hydraulic_Design.Manhole;

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
	public ArrayList<Integer> newSectionIdUp;
	public ArrayList<Integer> newSectionIdDown;
	public ArrayList<Integer> befSectionIdUp;
	public ArrayList<Integer> befSectionIdDown;
	public ArrayList<Double> depths;
	public ArrayList<Double> newDepths;
	public ArrayList <Double> depthsAvgBef; 
	public ArrayList <Double> slopesBef;
	
	
	public Regression_LayOut(File file) throws FileNotFoundException{

		this.file = file; 
		Lector = new BufferedReader(new FileReader(file));
	}
	
	public ArrayList<Double> getElevations(){
		return elevation;
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
		newSectionIdDown=new ArrayList<>();
		newSectionIdUp= new ArrayList<>();
		depths =new ArrayList<>();
		newDepths= new ArrayList<>();
		depthsAvgBef=new ArrayList<>();
		befSectionIdDown= new ArrayList<>();
		befSectionIdUp= new ArrayList<>();
		slopesBef= new ArrayList<>();
				
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
		 * Calculate the slopes and the distance between manholes
		 */
			
		
		
		ArrayList<Double> slopes = new ArrayList<>();
		ArrayList<Double> distancias= new ArrayList<>();

		for(int i=0; i<numSections;i++){
			int idup= sectionIdUp.get(i)-1;
			int iddown= sectionIdDown.get(i)-1;

			double eleUp=elevation.get(idup);
			double eleDown= elevation.get(iddown);

			double distance= distanciaPitagoras(posX.get(idup), posY.get(idup), posX.get(iddown), posY.get(iddown));
			double dif=Math.abs(eleUp-eleDown);

			double nSlope=dif/distance;
			
			distancias.add(distance);
			slopes.add(nSlope);			
		}
		
		
		/*
		 * Write the coeficient(bt) and the exponent(al) to design with Darcy-Weisbach (Regresion)
		 */

		
		double ks=0.0000015;  //PVC
		double v=0.000001003;
		bw.write("bt:[");bw.newLine();
		ArrayList<Double> alphas = new ArrayList<>();
		for(int j=0; j<numSections;j++){
			double slop=slopes.get(j);
			SimpleRegression sr= new SimpleRegression();
			for(int i=1;i<20;i++){
				double d=0.01+0.1*i;

				double a= ks/(18.692*d);
				double b=(2.51*v)/(50.2716*Math.pow(d, 1.5)*Math.sqrt(slop));
				double Q= -12.558*Math.sqrt(slop)*Math.log10(a+b)*Math.pow(d, 2.5);
				double logQ = Math.log10(Q);
				double logD= Math.log10(d);
				sr.addData(logD,logQ );
				

			}

			double bt= sr.getSlope();
			double alp= Math.pow(10,sr.getIntercept());
			alphas.add(alp);
			bw.write("("+sectionIdUp.get(j)+","+ sectionIdDown.get(j)+")"+ bt);bw.newLine();
			bw.write("("+sectionIdDown.get(j)+","+ sectionIdUp.get(j)+")"+ bt);bw.newLine();

		
		}
		bw.write("]");
		bw.newLine();
		
		
		bw.write("al:[");bw.newLine();
		for(int i=0;i<numSections;i++){
			bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ alphas.get(i));bw.newLine();
			bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ alphas.get(i));bw.newLine();
		}
		bw.write("]");
		bw.newLine();
		//----------------------------------------------------------------------------------------------------------
		
		/*
		 * Write the depth average in each section
		 */
				
		bw.write("h:[");
		calculateDepths(slopes,distancias);
	
		for(int i=0;i<numSections;i++){
			double hup=depths.get(sectionIdUp.get(i)-1);
			double hdown=depths.get(sectionIdDown.get(i)-1);
			double hprom= (hup+hdown)/2;
			bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+  hprom);bw.newLine();
			bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ hprom);bw.newLine();
		
		}
		bw.write("]");
		bw.newLine();
		
		/*
		 * Write the distance in each section
		 */
		bw.write("lg:[");bw.newLine();
		for(int i=0; i < numSections;i++)
		{
			double dista=distancias.get(i);
			bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ dista );bw.newLine();
			bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ dista );bw.newLine();
		}
		bw.write("]");bw.newLine();
	
		
		/*
		 * Write the slope in each section
		 */
		bw.write("p:[");bw.newLine();
		for(int i=0;i<numSections;i++){
			bw.write("("+sectionIdUp.get(i)+","+ sectionIdDown.get(i)+")"+ slopes.get(i));bw.newLine();
			bw.write("("+sectionIdDown.get(i)+","+ sectionIdUp.get(i)+")"+ slopes.get(i));bw.newLine();
		}
		bw.write("]");
		bw.newLine();
		
		bw.close();

	
	}
	

	
	
	
	
	
	public double distanciaPitagoras (double x1,double y1,double x2,double y2)
	{
		double dista=Math.pow(Math.pow(x1-x2, 2)+Math.pow(y2-y1, 2), 0.5);
		return dista;
	}

	
	
	
	
	public void calculateDepths(ArrayList<Double> slopes, ArrayList<Double> distances){
		for(int i =0;i<numManholes;i++)
		{
			depths.add(1.2);
		}
		
			
				
		for(int i=0; i<numSections;i++){
			double depthUp= depths.get(sectionIdUp.get(i)-1);
			double depthDown = depths.get(sectionIdDown.get(i)-1);
			double slope =slopes.get(i);
			double distance= distances.get(i);
			double prof= depthUp + slope*distance;
			
			if(prof>depthDown){
				depths.set(sectionIdDown.get(i)-1, prof);
				for(int j=0;j<numSections;j++){
					double depthUp1= depths.get(sectionIdUp.get(j)-1);
	
					double prof1= depthUp1 + slopes.get(j)*distances.get(j);
					int id= sectionIdDown.get(j);
					depths.set(id-1, prof1);
					
					for(int k=0;k<numSections;k++){
						if(id==sectionIdUp.get(k)){
							double prof2= depths.get(id-1)+slopes.get(k)*distances.get(k);
							if(prof2>depths.get(sectionIdDown.get(k)-1)){
								depths.set(sectionIdDown.get(k)-1,prof2);
							}
						}
					}
					
					
				}
			}
		}
	}

}
