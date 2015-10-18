package it.sp4te.CooperativeSpectrumSensing.Agents;

import java.util.*;

import it.sp4te.CooperativeSpectrumSensing.Detection.Detection;
import it.sp4te.CooperativeSpectrumSensing.Detection.Threshold;
import it.sp4te.CooperativeSpectrumSensing.DomainModel.Signal;
import it.sp4te.CooperativeSpectrumSensing.Functions.Moment;
import it.sp4te.CooperativeSpectrumSensing.Functions.Pr;
import it.sp4te.CooperativeSpectrumSensing.Functions.SignalFunctions;

/**
 * Questa classe si occupa di effetuare tutte le operazione relative allo
 * Spectrum sensing: -Calcolo dei momenti del secondo e quarto ordine nelle due
 * ipotesi -Calcolo dell'energia dei momenti -Calcolo dei vettori PR -Spectrum
 * sensing con Energy Detector -Spectrum sensing con Detector proposto
 **/

public class SecondaryUser {
	ArrayList<Moment> MomentsSignal;
	ArrayList<Moment> MomentsNoise;
	ArrayList<ArrayList<Double>> MomentSignalEnergy;
	ArrayList<ArrayList<Double>> MomentNoiseEnergy;
	ArrayList<Pr> PrSignal;
	ArrayList<Pr> PrNoise;

	/**
	 * Il costruttore inizializza i valori che saranno usati nei diversi tipi di
	 * detection: -Momenti nelle due ipotesi -Energia dei momenti nelle due
	 * ipotesi -Calcolo dei vettori PR nelle due ipotesi
	 **/

	public SecondaryUser(Signal s, int length, double energy, int attempts, int inf, int sup) {
		// Genero i momenti nelle due ipotesi h0 e h1
		MomentsSignal = SignalFunctions.momentGenerator(s, length, energy, attempts, inf, sup);
		MomentsNoise = SignalFunctions.momentGenerator(null, length, energy, attempts, inf, sup);

		// Calcolo l'energia
		MomentSignalEnergy = SignalFunctions.momentEnergy(MomentsSignal);
		MomentNoiseEnergy = SignalFunctions.momentEnergy(MomentsNoise);

		// Calcolo pr
		PrSignal = SignalFunctions.prGenerators(MomentsSignal);
		PrNoise = SignalFunctions.prGenerators(MomentsNoise);

	}

	/**
	 * Spectrum Senginf dell'energy Detector. Prende in input i momenti del
	 * secondo e quarto ordine calcolati sulle due ipotesi
	 **/

	public ArrayList<Double> spectrumSensingEnergyDetector() throws Exception {
		//EnergyDetection � una mappa snr->detection
		HashMap<Double, Double> EnergyDetection = new HashMap<Double, Double>();

		for (int i = 0; i < this.MomentSignalEnergy.size(); i++) {
			//pfa settata a 0.01. Segnale diviso in 10 blocchi da 100 campioni ciascuno
			Double ED = Detection.energyDetection(
					Threshold.energyDetectorThreshold(0.01, this.MomentNoiseEnergy.get(i)), MomentSignalEnergy.get(i),100);
			EnergyDetection.put(this.MomentsSignal.get(i).getSnr(), ED);
		}

		// Ordino in base all'SNR e ritorno
		return orderSignal(EnergyDetection);
	}
	
	public ArrayList<Double> spectrumSensingTraditionalEnergyDetector() throws Exception {
		//EnergyDetection � una mappa snr->detection
		HashMap<Double, Double> EnergyDetection = new HashMap<Double, Double>();

		for (int i = 0; i < this.MomentSignalEnergy.size(); i++) {
			//pfa settata a 0.01. Segnale diviso in 10 blocchi da 100 campioni ciascuno
			Double ED = Detection.TraditionalEnergyDetection(
					Threshold.energyDetectorThreshold(0.01, this.MomentNoiseEnergy.get(i)), MomentSignalEnergy.get(i));
			EnergyDetection.put(this.MomentsSignal.get(i).getSnr(), ED);
		}

		// Ordino in base all'SNR e ritorno
		return orderSignal(EnergyDetection);
	}

	/**
	 * Spectrum sensing del metodo proposto. Utilizza il calcolo metodo proposto
	 * per il calcolo prendendo in input gli oggetti PR calcolati nelle due
	 * ipotesi
	 **/

	public ArrayList<Double> spectrumSensingProposedDetector() throws Exception {
		//ProposedDetection � una mappa snr->detection
		HashMap<Double, Double> ProposedDetection = new HashMap<Double, Double>();

		for (int i = 0; i < this.MomentSignalEnergy.size(); i++) {
			Double PD = Detection.proposedMethodDetection(Threshold.proposedThreshold(0.01, this.PrNoise.get(i)),
					this.PrSignal.get(i));
			ProposedDetection.put(this.MomentsSignal.get(i).getSnr(), PD);
		}
		return orderSignal(ProposedDetection);
	}

	/**
	 * Data una mappa SNR->Detection, ritorna la lista delle Detection ordinate
	 * per SNR
	 **/

	public static ArrayList<Double> orderSignal(HashMap<Double, Double> signalmapToOrder) {

		ArrayList<Double> snr = new ArrayList<Double>();
		for (Double key : signalmapToOrder.keySet()) {
			snr.add(key);
		}
		Collections.sort(snr);

		ArrayList<Double> Edetection = new ArrayList<Double>();
		for (Double key : snr) {
			Edetection.add(signalmapToOrder.get(key));
		}
		return Edetection;
	}

}