package Hydraulic_Design;

import java.util.ArrayList;

public class Layout_Graphbuilder {

	public Layout_Node lNode;
	public Manhole mUp;
	public DataHandler dh;


	/**
	 * @param dh2
	 * 
	 */
	public Layout_Graphbuilder(DataHandler dh2) {
		dh = dh2;

	}

	public void build() {
		// TODO Auto-generated method stub
		// cree los LO nodos
		create_layout_nodes();

		// cree los LO sections.. es decir .. connect
		conect_layout_nodes();
//				test();
	}

	private void test() {
		System.out.println("TESTT");
		// TODO Auto-generated method stub
		for (int i = 0; i < dh.manholes.size(); i++) {
			for (int j = 0; j < dh.manholes.get(i).layout_nodes.size(); j++) {
				System.out.println(dh.manholes.get(i).layout_nodes.get(j));
				System.out.println("OUT");
				System.out.println(dh.manholes.get(i).layout_nodes.get(j).layoutSections_out);
				System.out.println("IN");
				System.out.println(dh.manholes.get(i).layout_nodes.get(j).layoutSections_in);
			}
		}
	}

	/**
	 * Creates the layout nodes per manhole, one per continuous outlet and one
	 * per initial point.
	 */
	public void create_layout_nodes() {

		int id_layout_nodes = 0;
		Manhole m;
		Section s;
		Layout_Node newLayoutManhole;

		for (int i = 0; i < dh.manholes.size(); i++) {

			m = dh.manholes.get(i);
//			System.out.println(m.toString());
			
			if (i != dh.manholes.size() - 1) {

				ArrayList<Section> sections_out_m = m.sections_out;

				// evaluates all the sections out from each manhole
				for (int j = 0; j < sections_out_m.size(); j++) {
					
					s=sections_out_m.get(j);
//					System.out.println(s.toString());				
					
					// If the section is an initial point creates a new manhole
					// per initial point with the same characteristics
					if (s.type.equals("I")) {

						newLayoutManhole = new Layout_Node(id_layout_nodes, "I", m, dh.minDepth,dh.maxDepth);
						m.layout_nodes.add(newLayoutManhole);
						id_layout_nodes++;
//						System.out.println(newLayoutManhole.toString());
					}
					// If the section out have other sections upstream
					// (continuous) creates one manhole with the same
					// characteristics
					else if (s.type.equals("C")) {

						newLayoutManhole = new Layout_Node(id_layout_nodes, "C", m, dh.minDepth,dh.maxDepth);
						m.layout_nodes.add(newLayoutManhole);
						m.theContinuos = newLayoutManhole;
						id_layout_nodes++;
//						System.out.println(newLayoutManhole.toString());
					}

				}
			}
			// creates a manhole for the outfall
			else if (i == dh.manholes.size() - 1) {
				newLayoutManhole = new Layout_Node(id_layout_nodes,"C", m, dh.minDepth,dh.maxDepth);
				m.layout_nodes.add(newLayoutManhole);
				m.theContinuos = newLayoutManhole;
				id_layout_nodes++;
//				System.out.println(newLayoutManhole.toString());
			}
			
			
		}
		
		
		
		
	}

	public void conect_layout_nodes() {

		// RECORRE LAS CÁMARAS
		for (int i = 0; i < dh.manholes.size(); i++) {

			mUp = dh.manholes.get(i);

			ArrayList<Layout_Node> mUp_loNodes = mUp.layout_nodes;

			ArrayList<Section> actual_m_sections_out = mUp.sections_out;

			// RECORRE SECTIONS OUT DE CADA MANHOLE
			for (int j = 0; j < actual_m_sections_out.size(); j++) {

				// section out in evaluation
				Section mUp_sectionOut = actual_m_sections_out.get(j);

				Manhole mDown = dh.manholes.get(mUp_sectionOut.Manhole_Down);

//				ArrayList<Layout_Node> mDown_loNodes = mDown.layout_nodes;

//				for (int k = 0; k < mUp_loNodes.size(); k++) {

					Layout_Node loNodeUp = mUp_loNodes.get(j);
					
//					if (loNodeUp.used==false || mUp_sectionOut.type.equals("C")){

//					for (int k2 = 0; k2 < mDown_loNodes.size(); k2++) {

						Layout_Node loNodeDown = mDown.theContinuos;

							if (loNodeDown != null) {

								Layout_Section lSection;

								if (mUp_sectionOut.type.equals("I")&& loNodeUp.type.equals("I")) {
								
//									if (!loNodeDown.used){
										lSection = new Layout_Section(loNodeUp,loNodeDown, mUp_sectionOut, mUp_sectionOut.type,mUp_sectionOut.sectionFlowRate);
										loNodeUp.layoutSections_out.add(lSection);
										loNodeDown.layoutSections_in.add(lSection);
//										System.out.println(lSection.toString());
//									}
								
								} else if (mUp_sectionOut.type.equals("C") && loNodeUp.type.equals("C")) {
									lSection = new Layout_Section(loNodeUp,loNodeDown, mUp_sectionOut,  mUp_sectionOut.type,mUp_sectionOut.sectionFlowRate);
									loNodeUp.layoutSections_out.add(lSection);
									loNodeDown.layoutSections_in.add(lSection);
//									System.out.println(lSection.toString());
					}
				}

				// }
				// }

				// }

			}

		}
	}

}
