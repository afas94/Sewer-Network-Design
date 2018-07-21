package Hydraulic_Design;

public class Layout_Section {

	public Layout_Node loNode_Up;
	public Layout_Node loNode_Down;
	public Section parent_section;
	public String loNode_type;
	public double loSection_FlowRate;
	
	
	/**
	 * 
	 * @param New_Manhole_Up
	 * @param New_Manhole_Down
	 * @param initial
	 */
	public Layout_Section(Layout_Node New_Manhole_Up, Layout_Node New_Manhole_Down, Section parent_sec, String initial,double nQd) {
		
		loNode_Up=New_Manhole_Up;
		loNode_Down=New_Manhole_Down;
		loNode_type=initial;
		loSection_FlowRate=nQd;
		parent_section = parent_sec;
	
						
	}
	
	
	@Override
	public String toString() {
		
		return "Layout Section: " + loNode_Up + "->" + loNode_Down + " Type: " + loNode_type;
	}
	
}
