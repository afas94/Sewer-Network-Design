package DiseñoRed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Hydraulic_Design.Design_Node;
import Hydraulic_Design.Designed_Arc;

public class Solution {

	public int solutionId;
	public ArrayList<Designed_Arc> design_sections;

	public Solution(int id, ArrayList<Designed_Arc> solution2) {

		solutionId = id;
		design_sections = new ArrayList<>();
		design_sections.addAll(solution2);
		try {
			writeIteration(design_sections);
			writeLay(design_sections);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeLay(ArrayList<Designed_Arc> sections) throws IOException {
		// TODO Auto-generated method stub
		File file3 = new File("XPRESS/Datos_de_Entrada/Trazado.txt");
		// if file doesnt exists, then create it
		if (!file3.exists()) {
			file3.createNewFile();
		}

		FileWriter fw1= new FileWriter(file3.getAbsoluteFile());
		BufferedWriter bw1 = new BufferedWriter(fw1);
		
		
		
		bw1.write("SECTIONS");bw1.newLine();
		
		
		for(int i=0; i<sections.size();i++){
			int idup= sections.get(i).up.parental_loNode.myManhole.id+1;
			int iddown = sections.get(i).down.parental_loNode.myManhole.id+1;
			
			bw1.write(idup+" "+iddown);bw1.newLine();
		}


		bw1.close();
	}

	public void writeIteration(ArrayList<Designed_Arc> sections) throws IOException{
		File file2 = new File("XPRESS/Datos_de_Entrada/Iteration.txt");
		// if file doesnt exists, then create it
		if (!file2.exists()) {
			file2.createNewFile();
		}

		FileWriter fw= new FileWriter(file2.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		ArrayList<Design_Node> agregados= new ArrayList<>();
		
		bw.write("Manholes");bw.newLine();
		

		
		for(int i=0; i<sections.size();i++){

			Design_Node up=sections.get(i).up;
			Design_Node down=sections.get(i).down;
			
			if(isAgregated(agregados, up.parental_loNode.myManhole.id)==false){
				bw.write(up.parental_loNode.myManhole.id+1+" "+up.elevation);bw.newLine();
				agregados.add(up);
			}
			else if(isAgregated(agregados, down.parental_loNode.myManhole.id)==false ){
				bw.write(down.parental_loNode.myManhole.id+1+" "+down.elevation);bw.newLine();
				agregados.add(down);
			}
		}
		bw.close();
		
		
		//File file3 = new File("XPRESS/Datos_de_Entrada/Trazado.txt");
		// if file doesnt exists, then create it
		//if (!file3.exists()) {
		//	file3.createNewFile();
		//}

		//FileWriter fw1= new FileWriter(file3.getAbsoluteFile());
		//BufferedWriter bw1 = new BufferedWriter(fw1);
		
		
		
		//bw1.write("SECTIONS");bw.newLine();
		
		
		//for(int i=0; i<sections.size();i++){
		//	int idup= sections.get(i).up.parental_loNode.myManhole.id+1;
		//	int iddown = sections.get(i).down.parental_loNode.myManhole.id+1;
			
		//	bw1.write(idup+" "+iddown);bw.newLine();
		//}


		//bw1.close();
	}

	public boolean isAgregated(ArrayList<Design_Node> nodes, int id){
		boolean agregado=false;
		if(nodes.size()>0){
			for(int i=0; i<nodes.size();i++){
			     if(nodes.get(i).parental_loNode.myManhole.id==id)
			     {
			    	 agregado=true;
			     }
			}
		}
		return agregado; 
	}

	
	@Override
	public String toString() {
		String print = "";
		print  = ""+ solutionId +" / ";
		print  += design_sections;
		return print;
	}
	

}
