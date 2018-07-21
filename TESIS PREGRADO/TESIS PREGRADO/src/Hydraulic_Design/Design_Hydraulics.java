package Hydraulic_Design;

//import Utilities.Rounder;

public class Design_Hydraulics {
	//	

	public static final int TotalCost =1; 
	public static final int PipesCost =2; 
	public static final int ExcavationCost =3; 

	public double nu; 	// Atribute that represents the kinematic viscosity
	public double nManning;
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
	public Design_Hydraulics(double nNu, double newManning, int nCostsType) {
		nu = nNu;
		//		ks = nKs;
		nManning = newManning;
		costsType=nCostsType;

	}


	/**
	 * 
	 * @param d pipe's diameter
	 * @return
	 */
	public double maximumFillingRatio(double d) {

		double maxfillingRate = 0.85;	

		if (d <= 0.6) {

			maxfillingRate = 0.7;

		}

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

		double nManning= 0.010;
		//Manning
		//speed = Math.pow(radius, 0.66666666666667)*Math.pow(nSlope, 0.5)*(1/nManning);
		double ks=	0.0000015;
		//Darcy Weisbach
		 speed= -2* Math.sqrt(8*9.81*radius*nSlope)*Math.log10(ks/(14.8*radius)+((2.51*nu)/(4*radius*Math.sqrt(8*9.81*radius*nSlope))));

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


	public boolean verificarRestricciones (double d, double nSlope){


		/**
		 * Maximum speed restriction
		 */

		boolean cumple_restricciones = true;

		if (nSlope < 0.0005) {

			return false;
		}


		if (speed > 5) {
			return false;

		}



		/**
		 * Minimum speed and minimum shear restriction
		 */

		if ( speed < 0.45) {
			return false;
		}


		/**
		 * Froude's number and filling rate restrictions 
		 */ 
		//		 If Froude's number is between 0.7 and 1.5 then the flow is quasi-critical
		if (yn/d > 0.7 && Froude > 0.7 && Froude < 1.5){
			return false;
		}


		return cumple_restricciones;
	}

	public double darEspesor (double d){

		double espesor =0;

		//Material Liso

		espesor = 0.0869*Math.pow(d, 0.935);

		//Fuente: Manuales técnicos PAVCO Novaloc/Novafort

		//Material Rugoso



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
		return costo/4;
	}







	public double costo (double d, double l, double h, double h2, int inicios){

		//	double volumenExc= volumenExcavacion(l, d, h, h2);

		//		 double costoTotal=costoTuberia(l, d)+costoExcavacion(h, h2, volumenExc)+costoRelleno(volumenExc, d, l)+costoEntibado(l, h2, h)+costoCamaras(h2);
		//		 return costoTotal;
		//___________________________________________________________________________________________________
		//		double B = 0.2; // Ancho (m) a lado y lado de la tubería en zanja
		//		double E = 0;
		//		double Hm=0;
		//		double volumen=0;
		//		double costo=0;

		//		E=darEspesor(d);
		//		Hm = (0.15 + d + 2 * E + (h + h2) * 0.5);
		//		volumen = Hm * (2 * B + d) * l;


		//		costo = 1.32 * (9579.31 * l * Math.pow(d*1000, 0.5737) + 1163 * Math.pow(volumen, 1.31)); 


		//		return costo;

		//______________________________________________________________________________________		

		//ECUACIÓN MAURER	
		double hprom=(h+h2)/2;
		double alpha= 1.02*(Math.pow(10,-3))*(2*d+2)+127;
		double beta=0.11*(Math.pow(10,-3))*(2*d+2)+37;

		return (alpha*hprom+beta)*l;

		//______________________________________________________________________________________________		

		//    double cinsta=0;
		//	double hp=(h+h2)/2;
		//	cinsta=(-203.311+0.154*d*1000+131.4391*hp+0.044*d*hp*1000)*l;

		//  double cman= (1.6928+3.6231*h2)/(inicios+1);
		// return cinsta+cman;	

	}
}


