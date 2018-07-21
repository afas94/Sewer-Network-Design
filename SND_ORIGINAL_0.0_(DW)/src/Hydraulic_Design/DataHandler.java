package Hydraulic_Design;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataHandler {

	public ArrayList<Manhole> manholes;
	public ArrayList<Section> sections;
	public BufferedReader Lector;

	/**
	 * Listas de diámetros disponibles
	 */

//	public static double[] diametros = { 0.2, 0.35, 0.45, 0.6, 0.8, 1.2, 1.55, 1.8 }; // TUNJA 1
//    public static double[] diametros = { 0.2, 0.35, 0.45, 0.6, 0.8, 1.2, 1.55, 1.8 }; // TUNJA 2
//	  public static double[] diametros = {0.2, 0.25, 0.3, 0.35, 0.38, 0.4,0.45, 0.5, 0.53, 0.6, 0.7, 0.80, 0.9, 1, 1.05, 1.20, 1.35, 1.4, 1.5 ,1.6,1.8, 2, 2.2, 2.4}; //TUNJA 3 y 4
    public static double[] diametros = {0.2, 0.3, 0.4, 0.5, 0.6, 0.70, 0.9, 1.05, 1.20, 1.3, 1.55 ,1.6 ,1.8, 2.2}; // TUNJA 5 y 6 Vector that contains the list of commercial diameters.
	
    
    static int nodeID = 0; // Attribute that establishes each node's ID.
	static int nodesPerManhole; // Attribute that establishes the number of node
								// in one specific Manhole, according with the
								// slope delta value.
	public static double precision; // Precision of the slope delta value.

	public File workingfile;

	/**
	 * number of manholes and section of the network
	 */
	public int numManholes;
	public int numSections;
	
	public double minDepth;
	public double maxDepth;

	/**
	 * input data of the nodes (manholes)
	 */
	public int id;
	public double x;
	public double y;
	public double z;

	/**
	 * Constructor Method where both array lists are initialized.
	 * 
	 * @param inputData
	 */
	public DataHandler(File inputData, double nPrecision, double nMinDepth, double nMaxDepth ) {
		
		minDepth=nMinDepth;
		maxDepth=nMaxDepth;
		
		workingfile = inputData;
		precision = nPrecision;
		nodesPerManhole = (int) ((nMaxDepth-nMinDepth) * precision) + 1;

		manholes = new ArrayList<>();
		sections = new ArrayList<>();
		
	

	}

	/**
	 * Method that reads the file and organizes the data a user gives.
	 */
	public void readFile() throws IOException {

		Lector = new BufferedReader(new FileReader(workingfile));
		String linea = Lector.readLine();

		/**
		 * Reads the first line with the number of Manholes of the network
		 */
		if (linea != null) {
			String[] matriz = linea.split(" ");
			numManholes = Integer.parseInt(matriz[1]);
		}

		/**
		 * Reads the input data for all the manholes (id, x, y, z)
		 */
		for (int i = 0; i < numManholes; i++) {

			linea = Lector.readLine();

			if (linea != null) {
				String[] matriz = linea.split(" ");
				//IF ID STARTS IN 1
				id = Integer.parseInt(matriz[0])-1;
				//IF ID STARTS IN 0
//				id = Integer.parseInt(matriz[0]);
				
				x = Double.parseDouble(matriz[1]);
				y = Double.parseDouble(matriz[2]);
				z = Double.parseDouble(matriz[3]);

				Manhole m = new Manhole(id, x, y, z);
				manholes.add(m);
				
			}

		}
//		System.out.println(manholes);

		/**
		 * Reads the number of Sections of the network
		 */
		linea = Lector.readLine();
		if (linea != null) {
			String[] matriz = linea.split(" ");
			numSections = Integer.parseInt(matriz[1]);
		}

		/**
		 * Reads the input data for all the sections (id_up, id_down,ini, Qd)
		 */
		for (int i = 0; i < numSections; i++) {

			linea = Lector.readLine();

			if (linea != null) {

				String[] matriz = linea.split(" ");
				/**
				 * Ids of the manholes forming the arc (i,j)
				 */
				//IF ID STARTS IN 1
				int id_up = Integer.parseInt(matriz[0])-1;
				int id_down = Integer.parseInt(matriz[1])-1;
				//IF ID STARTS IN 0
//				int id_up = Integer.parseInt(matriz[0]);
//				int id_down = Integer.parseInt(matriz[1]);
				
				/**
				 * Categorical variable I for initial pipes and C for non
				 * initial pipes
				 */
				String initial = matriz[2];

				/**
				 * Saves the design flow rate for each section (pipe)
				 */
				double Qd = Double.parseDouble(matriz[3]);

				Section newSection = new Section(id_up, id_down, initial, Qd);
				sections.add(newSection);
				manholes.get(id_up).get_sections_out().add(newSection);
				
			}
		}

	}

}
