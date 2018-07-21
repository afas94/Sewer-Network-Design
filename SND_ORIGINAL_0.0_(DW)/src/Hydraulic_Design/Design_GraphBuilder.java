package Hydraulic_Design;

//import java.io.File;
//import java.io.IOException;
import java.awt.dnd.DnDConstants;
import java.util.ArrayList;

import Utilities.Rounder;

public class Design_GraphBuilder {

	
	public DataHandler dh;			// Instance of the Class DataHandler.It lets you have access to all the methods and attributes of that class.
	public Design_Hydraulics hydraulics;		// Instance of the Class Hydraulics.It lets you have access to all the methods and attributes of that class.
	public Layout_Node loNode;
	
//	public static final double milimeter = 1000;
//	public static final String mm = "mm";
	public static final double centimeter = 100;
	public static final String cm = "cm";
	public static final double decimeter = 10;
	public static final String dm = "dm";
	public static final String[] precision= {dm,cm};
	
	public ArrayList<Layout_Node> pendientes;
	public ArrayList<Designed_Arc> designedArcs;
	
	public ArrayList<Designed_Arc> solution;
	public long numAlternatives=0;
	
	/**
	 * Constructor Method where both instances are initialized.
	 * @param inputData 
	 */
	public Design_GraphBuilder(DataHandler data, double nKs,int nCostType,double precision) {
		
		dh=data;
		
		hydraulics = new Design_Hydraulics(1.14*Math.pow(10, -6), nKs, nCostType);
				
//		System.out.println("File: "+inputData.getName()+ " ks: " + nKs+ " Cost: " + getCostType(nCostType) + " precision: " + precision);
	}
	
//	private String getCostType(int ct) {
//		if (ct  == hydraulics.TotalCost) {
//			return "Total cost";
//		}else if(ct == hydraulics.PipesCost){
//			return "Pipe cost";
//		}else{
//			return "Excavation cost";
//		}
//	}

	/**
	 * Builts the graph and solves it
	 */

	public void build_and_solve() {
		generateNodes(dh);
		generateDesignGraph();
		getSolution();
	}

	/**
	 * Notes generator
	 * @param data
	 */
	private void generateNodes(DataHandler data) {
		
		for (int i = 0; i < dh.manholes.size(); i++) {
			int arc_in = 0;
			Manhole mh = dh.manholes.get(i);
			if(mh.id == dh.manholes.size()-1){
				arc_in =-1;
			}
			
			for (int j = 0; j < mh.layout_nodes.size(); j++) {
				
				Layout_Node ln= mh.layout_nodes.get(j);
				
				
				int id_design_node_in_layoutNode = 0;

				for (double k = ln.upper_bound; k > ln.lower_bound; k = Rounder.round(k- (1 / DataHandler.precision))) {

					for (int m = 0; m < DataHandler.diametros.length; m++) {

						double d = DataHandler.diametros[m];

						if (k <= ln.upper_bound - d) {

							DataHandler.nodeID++;
							Design_Node node_ini = new Design_Node(DataHandler.nodeID, ln, d, k,arc_in);
							ln.nodes[id_design_node_in_layoutNode] = node_ini;
							id_design_node_in_layoutNode++;

						}
							
					}

				}
				
			}
			
			
		}
		
		

	}

	/**
	 * Method that generates the arcs of the Graph
	 */
	private void generateDesignGraph() {
		
		long counterFeasibleArcs = 0;
		pendientes = new ArrayList<>();
		designedArcs = new ArrayList<>();
		
		int numManholes = dh.manholes.size();
		
		//Starts at the last manhole (outfall)
		
		Manhole lastM = dh.manholes.get(numManholes-1);
		
		
		for (int i = 0; i < lastM.layout_nodes.size(); i++) {
			
			loNode = lastM.layout_nodes.get(i);
			pendientes. add(loNode);
		}
		 
		while (pendientes.size()>0)
		{
				
				Layout_Node loNodeDown =pendientes.remove(0);
				ArrayList<Layout_Section> sectionsIn=loNodeDown.layoutSections_in;
//				System.out.println("Evaluando a :"+ loNodeDown);
				
				for (int i = 0; i < sectionsIn.size(); i++) {
								
					Layout_Node loNodeUp = sectionsIn.get(i).loNode_Up;
					pendientes.add(loNodeUp);
//					System.out.println("--> " + loNodeUp);
					
					for (int j = 0; j < loNodeDown.nodes.length; j++) {
						
						
						if (loNodeDown.nodes[j]!= null && loNodeDown.nodes[j].dArcs_in != 0) {
							
							for (int j2 = 0; j2 < loNodeUp.nodes.length; j2++) {
								
								Design_Node dNodeDown =loNodeDown.nodes[j];
								Design_Node dNodeUp =loNodeUp.nodes[j2];
								
								if (dNodeUp != null){
								
									double elevationUp = dNodeUp.elevation;
									double elevationDown = dNodeDown.elevation;
									
									double diameterUp = dNodeUp.diameter;
									double diameterDown = dNodeDown.diameter;
									
									boolean e =true;
									
									if (loNodeUp.type.equals("I") && diameterUp != diameterDown){
										e=false;
									}
									if (e){
										Manhole mi=loNodeUp.myManhole;
										double xUp= mi.coordinate_x;
										double yUp=mi.coordinate_y;
										
//										Manhole mj=loNodeDown.myManhole;
										double xDown= loNodeDown.myManhole.coordinate_x;
										double yDown=loNodeDown.myManhole.coordinate_y;
										
												
										double distanceX = Math.pow(xDown-xUp,2);
										double distanceY = Math.pow(yDown-yUp,2);
										double distancePythagoras= Math.pow(distanceX + distanceY,0.5);
										
										double pipeLength= Math.pow((Math.pow(Rounder.round(-(elevationDown - elevationUp)),2) + Math.pow(distancePythagoras,2)),0.5);
										double slope= Rounder.round(-(elevationDown - elevationUp) / distancePythagoras);
										
										
										if(diameterDown>=diameterUp){
											
											if (slope>0){
//												System.out.print("De: "+ loNodeDown.nodes[j] + "\t Hacia:" + loNodeUp.nodes[j2]);System.out.println();
												
												double depthUp= loNodeUp.upper_bound +1.2-elevationUp-diameterDown;
												double depthDown=loNodeDown.upper_bound +1.2-elevationDown-diameterDown;
												double cost=0;	
												boolean hs=true;
												
//												System.out.println("DNodeUp: "+dNodeUp+" DNodeDown: "+dNodeDown);
												
												cost=hydraulics.costo(diameterDown, distancePythagoras, depthUp, depthDown);
												
												if (dNodeDown.label + cost < dNodeUp.label && hs) {
													
													double Qd =sectionsIn.get(i).loSection_FlowRate;
													
													if (Qd!=0 ){
														double flow = hydraulics.calculateHydraulics(diameterDown, slope);
														
														if ( flow >= Qd){
															dNodeDown.yNormal =  hydraulics.yNormal(diameterDown,Qd, slope);
															
															if (hydraulics.verificarRestricciones(diameterDown,slope)){
																counterFeasibleArcs++;
																dNodeUp.dArcs_in++;
																dNodeDown.dArcs_out++;
				
				
																dNodeUp.label= dNodeDown.label + cost;
																dNodeUp.Pj= dNodeDown;
//																System.out.print("De: "+ loNodeDown.nodes[j] + "\t Hacia:" + loNodeUp.nodes[j2]+ "\t Costo: "+ cost);System.out.println();
																
																Designed_Arc d_arc =new Designed_Arc(dNodeUp, dNodeDown, sectionsIn.get(i), cost, slope, pipeLength, hydraulics.getYn(),hydraulics.theta, hydraulics.getRadius(), hydraulics.getArea(), hydraulics.getSpeed(), hydraulics.getTao(),hydraulics.getFroude(), Qd);
																designedArcs.add(d_arc);
//																System.out.println(d_arc.toString());																		
															}
															
														}
														
									
														
													}
													
													
													
												}
												
												
											}
											
											
										}
									}
									
									
								}
							}
						}
					}
//					System.out.println("TERMINEEEE: hay " + counterFeasibleArcs);
			}
//				System.out.println(pendientes);
		}
		numAlternatives=numAlternatives+counterFeasibleArcs;
	}
		
	public void getSolution(){
		
		solution=new ArrayList<>();
		
		for (int i = 0; i < dh.manholes.size(); i++) {
			
			Manhole m =dh.manholes.get(i);
			ArrayList<Layout_Node> arraylistLoNodes = m.layout_nodes;
			
			for (int j = 0; j < arraylistLoNodes.size(); j++) {
				
				Layout_Node loNode = arraylistLoNodes.get(j);
								
				if (loNode.type.equals("I")){
					
					Design_Node minCostNode =null;
					
					double minCost= Double.POSITIVE_INFINITY;
					
					for (int k = 0; k < loNode.nodes.length; k++) {
						
						Design_Node temporalDesignNode=loNode.nodes[k];
						
						
						if (temporalDesignNode!=null){
								
//								System.out.println(temporalDesignNode.toString());
							
								double nextDgNodeCost=temporalDesignNode.label;
								
								if (nextDgNodeCost < minCost) {
									
									minCost=nextDgNodeCost;
									minCostNode=loNode.nodes[k];
									
								}
							}
					}
					
//					System.out.println("Mínimo costo: " + minCost);
					
					Design_Node predecessorNode =minCostNode;
					
					//Prints the information for the first node of the shortest path
//					System.out.println("LoNodeI: "+predecessorNode.parental_loNode);
//					System.out.println(predecessorNode);
//					System.out.println("Layout Node "+ "CotaCorona "+ "CotaBatea ");
//					double crownElevation =predecessorNode.elevation+predecessorNode.Pj.diameter;
//					System.out.println(predecessorNode.parental_loNode.id + " "+crownElevation+" "+predecessorNode.elevation);
					
					
					while (predecessorNode != null){
						

						Design_Node antiguo =predecessorNode;
						
						predecessorNode =predecessorNode.Pj;
						
						
						if (predecessorNode != null) {
							
//							System.out.println(" predecesor: "+predecessorNode.parental_loNode);
//							System.out.println(predecessorNode);
//							System.out.println("Layout Node "+ "CotaCorona "+ "CotaBatea "+ "CostoTramo "+ "pendiente "+ "longitud "+"diámetro "+ "yn "  +"área "+ "perímetro "+ "radio "+ "velocidad "+ "tao " +"froude " + "caudal ");
							
							for (int k = 0; k < designedArcs.size(); k++) {
								
								Designed_Arc actual=designedArcs.get(k);
								
								Design_Node up= actual.up;
								Design_Node down=actual.down;
								
								if(antiguo==up && predecessorNode==down){
									
									solution.add(actual);
									
//									double crownElevationA = down.elevation+down.diameter;
//									System.out.println(down.parental_loNode.id+  " "+crownElevationA+" "+down.elevation+" " + actual.arcCost + " "+actual.slope+" "+actual.lenght+" " +actual.down.diameter +" "+actual.yn+" "+actual.area+" "+actual.perimeter+" "+actual.radius+" "+actual.speed+" "+actual.tao+" "+actual.froude+" "+actual.flowRate);										
								}
							}

						}

					}
				}
			}

		}
		
		
		for (int i = 0; i < solution.size(); i++) {
			for (int j = i+1; j< solution.size(); j++) {
				Designed_Arc sec_i = solution.get(i);
				Designed_Arc sec_j = solution.get(j);
				if(sec_i.parent_Layout_section.parent_section.equals(sec_j.parent_Layout_section.parent_section)){
					if(sec_i.up.elevation>=sec_j.up.elevation){
						solution.remove(i);
						j=i;
					}else {
						solution.remove(j);
						j--;
					}
					
				}
			}
			
		}
		
	}
		
	
}
