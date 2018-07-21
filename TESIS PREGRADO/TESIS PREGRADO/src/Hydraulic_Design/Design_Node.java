package Hydraulic_Design;

import Utilities.Rounder;

public class Design_Node {
	
	public int dNode_id; 			//The ID of a Design Node that belongs to an specific Layout Node.
	public Layout_Node parental_loNode; 	//The Layout Node where the Design Node belongs.
	public double diameter;   				//Diameter that describes the Design Node.
	public double elevation;		// Elevation of the Design Node.
	
	/**
	 *  Desing Node's cummulative cost
	 */
	public double label; 		
	/**
	 * Desing Node's parent 
	 */
	public Design_Node Pj; 	
	
	/**
	 * design arcs coming in
	 */
	public int dArcs_in=0; 	
	/**
	 *  design arcs going out
	 */
	public int dArcs_out=0;
	
	
	/**
	 *  Desing Node's normal depth
	 */
	public double yNormal;	
		
	
	/**
	 * 
	 * @param nId node general id
	 * @param nLoNodeId manhole 
	 * @param nD diameter
	 * @param nElevation elevation
	 */
	public Design_Node(int nId, Layout_Node nLoNodeId, double nD, double nElevation, int arcs_in) {
		
		dNode_id=nId;
		parental_loNode=nLoNodeId;
		diameter=nD;
		elevation=Rounder.round(nElevation);
		label= Double.POSITIVE_INFINITY;
		Pj=null;
		
		dArcs_in=arcs_in;
		if(dArcs_in==-1){
			label = 0;
		}
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  "DN: " + dNode_id +"= ("+ diameter+ " , " +elevation+") "+ dArcs_in;
	}

}
