package DiseñoRed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import Hydraulic_Design.Designed_Arc;
import Hydraulic_Design.MainDesignOnly;
import Hydraulic_Design.Section;
import Layout.Random_Layout;
import Layout.Regression_LayOut;

public class Manager {
	
	public int iteration;
	public int initialIterations;
	public ArrayList<String> genericSections;
	public HashMap<String, SimpleRegression> myRegressions;
	public String instanceFile ;
	public ArrayList<Solution> solutions;
	public Solution bestSolution;
	public double bestCost;
	public double worstcost;
	SimpleRegression gnl_Regression;
	private Random myRndGenerator;
	
	/*
	 * String for results
	 */
	public ArrayList<String> r;
	public ArrayList<String> r2;
		
	public Manager() {
		solutions = new ArrayList<>();
		r= new ArrayList<>();
		r2=new ArrayList<>();
		myRegressions = new HashMap<String, SimpleRegression>();
		iteration = 0;
		initialIterations=0;
		bestCost=Double.POSITIVE_INFINITY;
		worstcost=0;
		myRndGenerator = new Random();
		myRndGenerator.setSeed(1);

	}

	

	/**
	 * Sets the inicial file
	 * @param string
	 */

	public void setFile(String string) {
		instanceFile = string;
		
	}
	
	/**
	 * Defines the layout and the hydraulic design
	 * @throws IOException
	 */
	public void designNetwork(int iter, double nManning, int elevationChange, double minDepth, double maxDepth, int typeCost) throws IOException {
		Process child;
        File f = new File("XPRESS/TESISVLOPES.mos");
        String dir = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf("\\") + 1);
        //System.out.println(dir);
        
        String cmd_line = "cmd /c start cmd.exe /c \"cd " + dir + "/ &&  mosel -c \"cl " + f.getAbsolutePath() + ";run;quit\"";
        //System.out.println(cmd_line);
        
        child = Runtime.getRuntime().exec(cmd_line);
        InputStream in = child.getInputStream();

        byte buff[] = new byte[1024];
        int cbRead;
        try {
               while ((cbRead = in.read(buff)) != -1) {
               }
               in.close();
               child.waitFor();
        } catch (IOException | InterruptedException e) {
               e.printStackTrace();
        }
       
        MainDesignOnly mdo = new MainDesignOnly();
        saveSolution(iter, mdo.main_design(nManning, elevationChange, minDepth, maxDepth,  typeCost));
		
	}
	
	
	
	/**
	 * initializes the problem with random layouts
	 * @param ini_iterations
	 * @throws IOException
	 */
	public void initialize(int ini_iterations, double nManning, int elevationChange, double minDepth, double maxDepth, int typeCost) throws IOException {
		// TODO Auto-generated method stub
		initialIterations=ini_iterations;
				
		File file = new File("XPRESS/Datos_de_Entrada/InitialFile/"+instanceFile);
		Random_Layout rL = new Random_Layout(file);
		
		gnl_Regression = new SimpleRegression(); 
		String createMyKey="";
		
		while (iteration<initialIterations){
			
			
			rL.generateRandomCosts(myRndGenerator);
			designNetwork(iteration, nManning, elevationChange, minDepth, maxDepth,typeCost);

			if(rL.numSections==solutions.get(iteration).design_sections.size()){
				
				iteration++;
				 
				
//					System.out.println(solutions.get(solutions.size()-1).solutionId);
					r.add(""+solutions.get(solutions.size()-1).solutionId);
					
					double costo = 0;
					for (int j = 0; j < solutions.get(solutions.size()-1).design_sections.size(); j++) {
						
						Designed_Arc designedArc = solutions.get(solutions.size()-1).design_sections.get(j);
						
						
//						System.out.println((j+1)+ "->"+designedArc);
						r.add((j+1) + "->"+designedArc);
						
						costo += designedArc.arcCost;
						Section designedSection = designedArc.parent_Layout_section.parent_section;
//						createMyKey  = designedSection.Manhole_Up<designedSection.Manhole_Down?""+designedSection.Manhole_Up+","+designedSection.Manhole_Down:""+designedSection.Manhole_Down+","+designedSection.Manhole_Up;
						createMyKey  = designedSection.Manhole_Up+","+designedSection.Manhole_Down;

						
						//if (!myRegressions.containsKey(createMyKey)) {
						//	SimpleRegression sr= new SimpleRegression();
						//	myRegressions.put(createMyKey, sr);
						//}

						//gnl_Regression.addData(designedSection.sectionFlowRate, designedArc.arcCost);
						//myRegressions.get(createMyKey).addData(designedSection.sectionFlowRate, designedArc.arcCost); 
//						System.out.println(myRegressions.get(createMyKey).getIntercept());
//						System.out.println(myRegressions.get(createMyKey).getSlope());
//						System.out.println(myRegressions.get(createMyKey).getRSquare());
					}
					System.out.println(iteration +  " costo: " + costo);
					r.add("El costo total: " + costo);
					
					
					
					if(costo<bestCost){
						bestCost=costo;
						bestSolution= solutions.get(solutions.size()-1);
						
//						System.out.println("--------------------------------------------------------------------------------------------");
//						System.out.println( iteration+" -> "+bestCost);System.out.println("--------------------------------------------------------------------------------------------");
						
						r2.add(iteration+" -> "+bestCost);
						
						
					}
					else if(costo>worstcost){
						worstcost=costo;
						
					}
				
				
				
				
				
			}
			else{
				System.out.println(rL.numSections + "  "+ solutions.get(iteration).design_sections.size());
				solutions.remove(iteration); 
			}
			
			
		}
		
//		System.out.println(gnl_Regression.getIntercept()); r.add(""+gnl_Regression.getIntercept());
//		System.out.println(gnl_Regression.getSlope()); r.add(""+gnl_Regression.getSlope());
		//System.out.println("Gnrl: "+gnl_Regression.getRSquare()); r.add(""+gnl_Regression.getRSquare());
//		System.out.println("--------------------------------------------------------------------------------------------");
//		
		iteration= 0;
		
		
	}

	

	/**
	 * Iteration process until it reaches the stop criteria
	 * @throws IOException
	 */
	public void execute(int iter,double nManning, int elevationChange, double minDepth, double maxDepth, int typeCost) throws IOException {
		
			
		File file = new File("XPRESS/Datos_de_Entrada/InitialFile/"+instanceFile);
		Regression_LayOut regressLO = new Regression_LayOut(file);
		
		String createMyKey="";
		
			
		while (iteration<iter){
			regressLO.generate_LR_Costs(myRegressions, gnl_Regression);
			designNetwork(iteration, nManning, elevationChange, minDepth, maxDepth, typeCost);
			
			if(regressLO.numSections==solutions.get(iteration).design_sections.size()){
			
//			solutions.get(solutions.size()-1).toString();
			iteration++;
			
			double costo = 0;
				for (int j = 0; j < solutions.get(solutions.size()-1).design_sections.size(); j++) {
					
					Designed_Arc designedArc = solutions.get(solutions.size()-1).design_sections.get(j);
					
					
//					System.out.println((j+1) + "->" +  designedArc);
					costo += designedArc.arcCost;
					Section designedSection = designedArc.parent_Layout_section.parent_section;
//					createMyKey  = designedSection.Manhole_Up<designedSection.Manhole_Down?""+designedSection.Manhole_Up+","+designedSection.Manhole_Down:""+designedSection.Manhole_Down+","+designedSection.Manhole_Up;
					createMyKey  = designedSection.Manhole_Up+","+designedSection.Manhole_Down;
					
					//if (!myRegressions.containsKey(createMyKey)) {
					//	SimpleRegression sr= new SimpleRegression();
					//	myRegressions.put(createMyKey, sr);
					//}
	
					//gnl_Regression.addData(designedSection.sectionFlowRate, designedArc.arcCost);
					//myRegressions.get(createMyKey).addData(designedSection.sectionFlowRate, designedArc.arcCost);
//					System.out.println(myRegressions.get(createMyKey).getIntercept());
//					System.out.println(myRegressions.get(createMyKey).getSlope());
//					System.out.println(myRegressions.get(createMyKey).getRSquare());
					
				}
				System.out.println( iteration +  " costo: " + costo);
				r.add("El costo total: " + costo);
//				System.out.println(gnl_Regression.getIntercept()); r.add(""+gnl_Regression.getIntercept());
//				System.out.println(gnl_Regression.getSlope()); r.add(""+gnl_Regression.getSlope());
		//		System.out.println("Gnrl: "+gnl_Regression.getRSquare()); r.add(""+gnl_Regression.getRSquare());
//				System.out.println("--------------------------------------------------------------------------------------------"); r.add("--------------------------------------------------------------------------------------------");
				
				if(costo<bestCost){
					bestCost=costo;
					bestSolution= solutions.get(solutions.size()-1);
//					System.out.println("--------------------------------------------------------------------------------------------");
//					System.out.println( iteration+" -> "+bestCost);System.out.println("--------------------------------------------------------------------------------------------");
				    
					r2.add(iteration+" -> "+bestCost);
				}
				else if(costo>worstcost){
					worstcost=costo;
				}
					
		}
		else{
				
			solutions.remove(iteration);
				
		}
			
			
		}
			
		// end
		
		iteration= 0;
//		System.out.println("--------------------------------------------------------------------------------------------");
		
	}
	
	/**
	 * Saves the last layout and hydraulic design
	 * @param solution_arcs
	 */
	public  void saveSolution(int iter, ArrayList<Designed_Arc> solution_arcs) {
		
		Solution newSol = new Solution(iter+1,solution_arcs);
//		newSol.toString();System.out.println("--------------------------------------------------------------------------------------------");
		solutions.add(newSol);
		
	}
	
	public void writeSolution() throws IOException{
		System.out.println("MEJOR COSTO: "+bestCost); 
		System.out.println("MEJOR DISEÑO:");
		for (int j = 0; j < bestSolution.design_sections.size(); j++) {
			System.out.println((j+1)+" -> " +bestSolution.design_sections.get(j));
		}
		
		/*
		 * Writes the input data for all the sections and manholes
		 */
		File file2 = new File("XPRESS/SOLUTION.txt");
		// if the file does not exist, then create it
		if (!file2.exists()) {
			file2.createNewFile();
		}
		
		FileWriter fw= new FileWriter(file2.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
//		for (int i = 0; i < r.size(); i++) {
//			
//			bw.write(""+r.get(i));bw.newLine();
//			
//		}
//		
		for (int i = 0; i < r2.size(); i++) {
			
			bw.write(""+r2.get(i));bw.newLine();
			
		}
		
		bw.write("PEOR COSTO: "+worstcost);bw.newLine();
		bw.write("MEJOR COSTO: "+bestCost);bw.newLine();
		bw.write("MEJOR DISEÑO:");bw.newLine();
		for (int j = 0; j < bestSolution.design_sections.size(); j++) {
			bw.write((j+1)+" -> " +bestSolution.design_sections.get(j));;bw.newLine();
		}
		
		bw.close(); 
		
	}
	


}
