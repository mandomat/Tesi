package it.sp4te.CooperativeSpectrumSensing.Agents;

import java.util.*;


import it.sp4te.CooperativeSpectrumSensing.Detection.Detection;
import it.sp4te.CooperativeSpectrumSensing.Detection.Threshold;
import it.sp4te.CooperativeSpectrumSensing.DomainModel.Signal;
import it.sp4te.CooperativeSpectrumSensing.Functions.Moment;
import it.sp4te.CooperativeSpectrumSensing.Functions.Pr;
import it.sp4te.CooperativeSpectrumSensing.Functions.SignalFunctions;



public class SecondaryUser {
	ArrayList<Moment> MomentsSignal;
	ArrayList<Moment> MomentsNoise;
	ArrayList<ArrayList<Double> > MomentSignalEnergy;
	ArrayList<ArrayList<Double> > MomentNoiseEnergy;
	ArrayList<Pr> PrSignal;
	ArrayList<Pr> PrNoise;
	
	
	public SecondaryUser(Signal s,int length,double energy,int attempts,int inf,int sup){
		//Genero i momenti nelle due ipotesi h0 e h1
		MomentsSignal =SignalFunctions.momentGenerator(s, length, energy, attempts, inf, sup);
		MomentsNoise =SignalFunctions.momentGenerator(null, length, energy, attempts, inf, sup);
	    
		//Calcolo l'energia
		MomentSignalEnergy = SignalFunctions.momentEnergy(MomentsSignal);
		MomentNoiseEnergy = SignalFunctions.momentEnergy(MomentsNoise);	
		
		//Calcolo pr
		PrSignal= SignalFunctions.prGenerators(MomentsSignal);
		PrNoise= SignalFunctions.prGenerators(MomentsNoise);
		
	}
	
	
	//SPECTRUM SENSING ENERGY DETECTOR
	public ArrayList<Double> spectrumSensingEnergyDetector() throws Exception {	 
	HashMap<Double,Double> EnergyDetection= new HashMap<Double,Double>();	
	
	for(int i=0;i<this.MomentSignalEnergy.size();i++){
	   Double ED= Detection.energyDetection(Threshold.energyDetectorThreshold(0.01, this.MomentNoiseEnergy.get(i)), MomentSignalEnergy.get(i));
	   EnergyDetection.put(this.MomentsSignal.get(i).getSnr(),ED);}
	
	//Ordino in base all'SNR e ritorno
	return orderSignal(EnergyDetection);
	}
	
	
	
	//SPECTRUM SENSING PROPOSED DETECTOR
	public ArrayList<Double> spectrumSensingProposedDetector() throws Exception {
		HashMap<Double,Double> ProposedDetection= new HashMap<Double,Double>();
		
		for(int i=0;i<this.MomentSignalEnergy.size();i++){
			Double PD=Detection.proposedMethodDetection(Threshold.proposedThreshold(0.01, this.PrNoise.get(i)), this.PrSignal.get(i));	
			ProposedDetection.put(this.MomentsSignal.get(i).getSnr(),PD);
		}
		return orderSignal(ProposedDetection);
}
	
	
	
	
	//Data una mappa SNR->DETECTION, ritorna la lista delle detection ordinate
	public static ArrayList<Double> orderSignal(HashMap<Double,Double> signalmapToOrder){
	
		ArrayList<Double> snr= new ArrayList<Double>();
	for (Double key : signalmapToOrder.keySet()) {
		snr.add(key);}
	Collections.sort(snr);
	
	ArrayList<Double> Edetection= new ArrayList<Double>();
	for (Double key : snr) {
		Edetection.add(signalmapToOrder.get(key));}
	return Edetection;
	}

}