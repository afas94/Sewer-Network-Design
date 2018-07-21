package Hydraulic_Design;

//import java.io.IOException;
import java.util.ArrayList;

//import Utilities.Rounder;

public class Manhole {
	

	/**
	 * Manholes id
	 */
	public int id;
	
	/**
	 * x, y and z coordinates
	 */
	public double coordinate_x;
	public double coordinate_y;
	public double coordinate_z;
	
	
//	public ArrayList<Manhole> upStream;
//	public ArrayList<Manhole> downStream;
	public ArrayList<Section> sections_out;
	public ArrayList<Layout_Node>layout_nodes;
	public Layout_Node theContinuos;
	
	
//	public DataHandler dh;
	
	
	
	/**
	 * Constructor method
	 * @param nID
	 * @param new_coordenada_x
	 * @param new_coordenada_y
	 * @param new_coordenada_z
	 */
	public Manhole(int nID, double new_coordenada_x, double new_coordenada_y, double new_coordenada_z) {
		// constructor stub
		
		id=nID;
		coordinate_x=new_coordenada_x;
		coordinate_y=new_coordenada_y;
		coordinate_z=new_coordenada_z;
				
		sections_out = new ArrayList<>();
		layout_nodes = new ArrayList<>();
		
		
			
		
	}
	
	
	/**
	 * Gives the sections out from each manhole
	 */
	public ArrayList<Section> get_sections_out ()
	{
		return sections_out;
	}
	


	/**
	 * gives id of the manhole
	 */
	public int get_id() {
		return id;
	}

	/**
	 * gives the x, y and z coordinates
	 */
	public double get_x_coordinate() {
		return coordinate_x;
	}

	public double get_y_coordinate() {
		return coordinate_y;
	}

	public double get_z_coordinate() {
		return coordinate_z;
	}
	
	
	
	/**
	 * gives the x, y and z coordinates
	 */
	public ArrayList<Layout_Node> get_layout_nodes() {
		
		
		return layout_nodes;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Manhole: " + id + " out: " + sections_out.size();
	}
	

}
