package Hydraulic_Design;

//import Utilities.Rounder;

public class Design_Hydraulics {
	//	
	public static final double ksPVC = 0.0000015;
	public static final String pvc = "PVC";
	public static final double ksConcrete = 0.0003;
	public static final String concrete = "Concrete";
	//	public static final double ksGRP = 0.0003;
	//	public static final String grp = "GRP";
	public static final String[] materials = {pvc, concrete};


	public static final int TotalCost =1; 
	public static final int PipesCost =2; 
	public static final int ExcavationCost =3; 

	public double nu; 	// Atribute that represents the kinematic viscosity
	public double ks; 	// Atribute that represents the roughness 
	//	public double nManning;
	public double theta;
	public double yn;
	public double y;
	public double area;
	public double perimeter;
	public double T;
	public double radius;
	public double speed;
	public double tao;
	public double Froude;
	public double flowRate;
	public double pu;
	public int costsType;

	/**
	 * 
	 * @param nNu Kinematic viscosity
	 * @param nKs Ks
	 * @param nCostsType Type of costs that is been evaluated
	 */
	public Design_Hydraulics(double nNu, double nKs, int nCostsType) {
		nu = nNu;
		ks = nKs;
		//		nManning = newManning;
		costsType=nCostsType;

	}


	/**
	 * 
	 * @param d pipe's diameter
	 * @return
	 */
	public double maximumFillingRatio(double d) {

		//		double maxfillingRate=0.80;
		//		
		double maxfillingRate = 0.85;


		if (d <= 0.6) {

			maxfillingRate = 0.7;

		}
		//		else if(d > 0.30 && d <= 0.45){
		//		
		//			maxfillingRate = 0.7;
		//		
		//		} else if (d > 0.45 && d <= 0.9) {
		//			maxfillingRate = 0.75;
		//		}
		//	

		return maxfillingRate;

	}

	/**
	 * 
	 * @param d pipe's diameter
	 */
	public void theta (double d) {

		y=maximumFillingRatio(d)*d;

		double alpha= Math.asin((2*y-d)/d);

		theta = Math.PI+ 2 *alpha;

	}


	/**
	 * 
	 * @param d pipe's diameter
	 * @param nYn Normal depth
	 */
	public void thetaYn (double d, double nYn) {

		double alpha= Math.asin((2*nYn-d)/d);

		theta = Math.PI+ 2 *alpha;

	}

	/**
	 * 
	 * @param d pipe's diameter
	 */
	public void wetArea (double d){

		area =Math.pow(d, 2)/8 * (theta-Math.sin(theta));


	}

	/**
	 * 
	 * @param d pipe's diameter
	 */
	public void wetPerimeter (double d){

		perimeter = theta*d/2;


	}

	/**
	 * 
	 * @param d pipe's diameter
	 * @param y flow's depth 
	 */
	public void upperWidth (double d,double y){


		T= (d*Math.cos(Math.asin((2*y-d)/d)));


	}


	public void hydraulicRadius (double d){

		radius= d/4*(1-Math.sin(theta)/theta);

	}

	public void tao (double nSlope){

		tao = 9.81*1000*radius*nSlope;

	}

	public void speed (double nSlope){


		//		speed = Math.pow(radius, 0.66666666666667)*Math.pow(nSlope, 0.5)*(1/nManning);

		speed= -2* Math.sqrt(8*9.81*radius*nSlope)*Math.log10(ks/(14.8*radius)+((2.51*nu)/(4*radius*Math.sqrt(8*9.81*radius*nSlope))));
		//	
	}

	public void froudeNumber (){

		Froude = ( speed / Math.pow((9.81 *  area/ T),0.5)) ;

	}

	public void flowRate (){

		flowRate=area*speed;


	}


	/**
	 * 
	 * @param d
	 * @param Q 
	 * @param nSlope
	 * @return
	 */
	public double yNormal (double d, double Q, double nSlope){

		double yni=0;
		double ynf=d*maximumFillingRatio(d);


		yn= (ynf + yni)/2;

		while (Math.abs(yni-ynf)>0.000001 ){
			thetaYn(d, yn);
			wetArea(d);
			hydraulicRadius(d);
			speed(nSlope);
			flowRate();
			double caudal= getFlowRate();
			if ( caudal > Q ) {
				ynf=yn;
			}
			else{
				yni=yn;
			}
			yn=(ynf + yni)/2;
		}

		// Actualización de los atributos para las condiciones de flujo con el caudal de diseño

		thetaYn(d, yn);
		wetArea(d);
		wetPerimeter(d);
		hydraulicRadius(d);
		upperWidth(d,yn);
		speed(nSlope);
		tao(nSlope);
		froudeNumber();
		flowRate();


		return yn;

	}

	/**
	 * 
	 * @return
	 */
	public double getTheta (){

		return theta;
	}	

	public double getYn(){

		return yn;
	}

	public double getPerimeter (){

		return perimeter;
	}

	public double getArea (){

		return area;
	}

	public double getRadius (){

		return radius;
	}

	public double getT(){

		return T;

	}

	public double getSpeed (){

		return speed;
	}

	public double getTao(){

		return tao;

	}

	public double getFroude(){

		return Froude;

	}

	public double getFlowRate (){

		return flowRate;
	}


	public double getPU(double cotaI, double cotaJ){

		pu = flowRate*(cotaI-cotaJ); 

		return pu;
	}

	public double calculateHydraulics (double d, double nSlope){


		theta(d);
		wetArea(d);
		hydraulicRadius(d);
		speed(nSlope);
		flowRate();

		return getFlowRate();


	}

	//public double hydraulicsYn (double yNormal, double d, double Q, double nSlope){
	//	
	//	
	//	thetaYn(d, yNormal);
	//	wetArea(d);
	//	wetPerimeter(d);
	//	hydraulicRadius(d);
	//	upperWidth(d,yNormal);
	//	speed(nSlope);
	//	tao(nSlope);
	//	froudeNumber();
	//	flowRate();
	//			
	//	return getFlowRate();
	//					
	//	 
	//}
	//	
	public boolean verificarRestricciones (double d, double nSlope){


		/**
		 * Maximum speed restriction
		 */

		boolean cumple_restricciones = true;

		if (ks > 0.0001) {
			if (speed > 5) {
				return false;
			}
		}
		else{
			if (speed > 10) {
				return false;
			}	
		}


		/**
		 * Minimum speed and minimum shear restriction
		 */


		if (d>= 0.45 && tao <= 2) {
			return false;
		}
		else if (d < 0.45 && speed <= 0.75) {
			return false;
		}
		//		
		//		else if (yn/d <= 0.1 && tao <2) {
		//			return false;
		//		}


		/**
		 * Froude's number and filling rate restrictions 
		 */ 
		// If Froude's number is between 0.7 and 1.5 then the flow is quasi-critical
		if (yn/d > 0.8 && Froude > 0.7 && Froude < 1.5){
			return false;
		}


		return cumple_restricciones;
	}

	public double darEspesor (double d){

		double espesor =0;

		//Material Liso
		if (ks < 0.00005) {
			espesor = 0.0869*Math.pow(d, 0.935);
		}      
		//Fuente: Manuales técnicos PAVCO Novaloc/Novafort

		//Material Rugoso
		else {
			espesor=0.1*Math.pow(d, 0.68);
		}                  
		//http://www.tubosgm.com/tubo_concretoref_jm.htm

		return espesor;
	}

public double costoTuberia(double l , double d){
		
		double tuberia= l*622749*Math.pow(d,1.9799);
		return tuberia;
	}
	
	public double volumenExcavacion(double l, double d,double h, double h2){
		double hm=(h+h2)/2;
		double s=(h2-h)/l;
		double ang= Math.atan(s);
		
		double volumen=((l*Math.sin(ang)+d+0.15)*(l*Math.cos(ang))*(0.4+d))/2 + hm*l*Math.cos(ang)*(d+0.4);
		
		return volumen;
	}
	
	public double costoExcavacion(double h, double h2, double volumenExc){
		
		double hm=(h+h2)/2;
		double costo=0;
		
		if(hm<=2){
			costo=20119.53*volumenExc;			
		}
		else{
			costo=25938.52*volumenExc;
		}
		
		return costo;
	}
	
	public double costoRelleno(double volumenExc, double d, double l){
		
		double costo=18125.89*(volumenExc-(Math.PI*Math.pow(d,2)/4*l));
		return costo;
	}
	
	
	public double costoEntibado(double l, double h2, double h){
		 double costo=0;
		
		if(h2>1.2)
		{
			double s=(h2-h)/l;
			double ang= Math.atan(s);
			costo= 23033.89*(l*Math.cos(ang)*h2*2);
		}
		return costo;
		
	}
	
	public double costoCamaras(double h2){
				
		double costo=1.043*(194014*h2*h2-194118*h2+856764);
		return costo;
	}
	
	
	
	public double costo(double d, double l, double h, double h2){
		
		 double volumenExc= volumenExcavacion(l, d, h, h2);
		 
		 double costoTotal=costoTuberia(l, d)+costoExcavacion(h, h2, volumenExc)+costoRelleno(volumenExc, d, l)+costoEntibado(l, h2, h)+costoCamaras(h2);
		 return costoTotal;
	   
	}

	
}


