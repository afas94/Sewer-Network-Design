package Hydraulic_Design;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//import javafx.application.*;

public class MainDesignOnly {

	
	
	public ArrayList main_design(double ks, int elevationChange, double minDepth, double maxDepth, int typeCost ) throws IOException{
		
			//Archivo desde Xpress
			File file = new File("XPRESS/Resultados.txt");
			
			
			DataHandler dh = new DataHandler(file, elevationChange, minDepth, maxDepth);
			dh.readFile();

			Layout_Graphbuilder layout_builder = new Layout_Graphbuilder(dh);
			layout_builder.build();
						
			Design_GraphBuilder design_builder = new Design_GraphBuilder(dh, ks, typeCost, dh.precision);
			design_builder.build_and_solve();
			return design_builder.solution;

	}
}
