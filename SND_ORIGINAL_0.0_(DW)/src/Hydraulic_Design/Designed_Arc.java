package Hydraulic_Design;

import java.awt.dnd.DnDConstants;

public class Designed_Arc {
	
	public Design_Node up;
	public Design_Node down;
	public Layout_Section parent_Layout_section;
	/**
	 *  Desing Arc's cost
	 */
	public double arcCost;
	
	
	/*
	 * Hydraulic 
	 */
	public double slope;
	public double lenght;
	public double yn;
	public double fillingRatio;
	public double theta;
	public double area;
	public double perimeter;
	public double radius;
	public double speed;
	public double tao;
	public double froude;
	public double flowRate;
	public double pu;
	
	
	
	
	
	/**
	 * 
	 * @param nUp
	 * @param nDown
	 * @param c
	 */
	public Designed_Arc (Design_Node nUp, Design_Node nDown, Layout_Section layoutSec, double c, double s, double l, double nYn,double nTheta, double nRadius, double nArea, double nSpeed, double nTao, double nFroude, double nQ )
	{
		up=nUp;
		down=nDown;
		parent_Layout_section = layoutSec;
		arcCost=c;
		
		
		slope=s;
		lenght=l;
		fillingRatio=nYn/down.diameter;
		yn=nYn;
		theta=nTheta;
		area=nArea;
		radius=nRadius;
		speed=nSpeed;
		tao=nTao;
		froude=nFroude;
		flowRate=nQ;
		
				
		
	}
	
		
	@Override
	public String toString() {
		String print = "";
		print =  "" + "Designed Section: " + (up.parental_loNode.myManhole.id+1) + " -> " + (down.parental_loNode.myManhole.id+1) + " Type: "+up.parental_loNode.type+" ( d= "+down.diameter+" , "+ up.elevation+" : "+ down.elevation+" )"+" cost: " + arcCost + " Qd:" + flowRate;
		print +=" "+ slope+" "+lenght+" "+fillingRatio+" "+yn+" "+theta+" "+radius+" "+area+" "+speed+" "+tao+" "+froude+" "+flowRate;
		return print;
	}
}
