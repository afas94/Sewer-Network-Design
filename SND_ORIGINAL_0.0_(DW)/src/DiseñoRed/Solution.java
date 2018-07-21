package DiseñoRed;

import java.util.ArrayList;

import Hydraulic_Design.Designed_Arc;

public class Solution {

	public int solutionId;
	public ArrayList<Designed_Arc> design_sections;
	
	public Solution(int id, ArrayList<Designed_Arc> solution2) {
		
		solutionId = id;
		design_sections = new ArrayList<>();
		design_sections.addAll(solution2);
		
	}
	
	@Override
	public String toString() {
		String print = "";
		print  = ""+ solutionId +" / ";
		print  += design_sections;
		return print;
	}
	

}
