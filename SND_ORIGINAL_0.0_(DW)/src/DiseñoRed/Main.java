package DiseñoRed;

import java.io.IOException;
import java.util.Random;

public class Main {

	static long time_start;
	static long time_end;
	
	
	public static void main(String[] args) {
		
		time_start = System.currentTimeMillis();
		
		
		//InputData
		double ks=0.0003;
		
		//Elevation changes (precision of the hydraulic design)
		int elevationChange=10;
		
		//Excavation Limits
		double minDepth=1.2;
		double maxDepth=5;
		
		/*
		 * TotalCost =1; 
		 * PipesCost =2; 
		 * ExcavationCost =3; 
		 */
		int typeCost=1;
		
		
		
		Manager manager = new Manager();
		try {
			manager.setFile("CedritosNorte.txt");
			int iter = 0;
			while (iter <100) {
				System.out.print(iter + "\t");
				manager.initialize(1,ks, elevationChange,minDepth, maxDepth, typeCost);
				iter++;
				
				System.out.print(iter + "\t");
				manager.execute(1, ks, elevationChange,minDepth, maxDepth, typeCost);
				iter++;
			}
			
			
			manager.writeSolution();
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		} 
		time_end= System.currentTimeMillis();
		
		System.out.println( "Excecution time: " + (time_end-time_start)/1000.0);
 	}
//	
	
}
