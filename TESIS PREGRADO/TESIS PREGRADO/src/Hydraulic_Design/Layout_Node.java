package Hydraulic_Design;

import java.util.ArrayList;

//import Utilities.Rounder;

public class Layout_Node {

	/**
	 * Layout_Node id
	 */
	public int id; // The ID of a layout node that belongs to an specific manhole.

//	/**
//	 * Layout_Node status says if this LayoutNode is already conected
//	 */
//	public boolean used;

	/**
	 * Layout_Node type
	 */
	public String type; // defines if the node is an initial node.

	/**
	 * Parent Manhole
	 */
	public Manhole myManhole;

	/**
	 * upper and lower bounds for placing pipes
	 */
	public double upper_bound;
	public double lower_bound;

	ArrayList<Layout_Section> layoutSections_out;
	ArrayList<Layout_Section> layoutSections_in;
	
	/**
	 * Attribute from the class DataHandler
	 */
	public DataHandler dh;

	/**
	 * Attribute from the class Layout_Nodes
	 */
	Design_Node[] nodes;
	


	public Layout_Node(int id_layout_nodes, String nType, Manhole m , double minDepth, double maxDepth ) {
		// TODO Auto-generated constructor stub

		id = id_layout_nodes;
		type = nType;
		myManhole = m;
		layoutSections_in = new ArrayList<>();
		layoutSections_out = new ArrayList<>();
		
		// Defines the excavation limits
		upper_bound = m.coordinate_z - minDepth;
		lower_bound = m.coordinate_z - maxDepth;

		// Creates a vector of Design_Nodes
		nodes = new Design_Node[DataHandler.diametros.length* DataHandler.nodesPerManhole];

	}


	public Design_Node[] get_design_nodes()
	{
		return nodes;
	}
	
	@Override
	public String toString() {
		return "" +id;
//		return "Layo;ut Node: "+id+" My Manhole: "+myManhole.id +" type: "+ type;
	}
}
