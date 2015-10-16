package it.sp4te.CooperativeSpectrumSensing.Detection;

import java.util.ArrayList;

import it.sp4te.CooperativeSpectrumSensing.Functions.MathFunctions;
import it.sp4te.CooperativeSpectrumSensing.Functions.Pr;



public class Threshold {

	//calcolo della soglia del metodo implementato.
	public static double proposedThreshold(double Pfa,Pr pr) throws Exception{
		double M = MathFunctions.Avarege(pr.getPr());
		double V= MathFunctions.Variance(pr.getPr());

		double implThreshold=M+ Math.sqrt(2*V)*MathFunctions.InvErf(1-2*Pfa);
		return implThreshold;
	}
	

	//Calcolo soglia energyDetector
    public static double energyDetectorThreshold(double Pfa,ArrayList<Double> energy) throws Exception{

    	double M = MathFunctions.Avarege(energy);
    	double V= MathFunctions.Variance(energy);

    	double edThreshold=M+ Math.sqrt(2*V)*MathFunctions.InvErf(1-2*Pfa);
    		return  edThreshold;
    }}

