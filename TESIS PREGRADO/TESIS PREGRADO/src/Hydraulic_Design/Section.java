package Hydraulic_Design;

public class Section {

	public int Manhole_Up;
	public int Manhole_Down;
	public String type;
	public double sectionFlowRate;

	/**
	 * 
	 * @param New_Manhole_Up
	 * @param New_Manhole_Down
	 * @param initial
	 */
	public Section(int New_Manhole_Up, int New_Manhole_Down, String initial,
			double nQd) {

		Manhole_Up = New_Manhole_Up;
		Manhole_Down = New_Manhole_Down;
		type = initial;
		sectionFlowRate = nQd;

	}

	public int get_Manhole_up() {

		return Manhole_Up;

	}

	public int get_Manhole_down() {

		return Manhole_Down;

	}

	public String get_initial() {

		return type;

	}

	public double get_design_flow_rate() {
		return sectionFlowRate;
	}

	@Override
	public String toString() {

		return "Section: " + Manhole_Up + "->" + Manhole_Down + " Type: " + type;
	}

}
