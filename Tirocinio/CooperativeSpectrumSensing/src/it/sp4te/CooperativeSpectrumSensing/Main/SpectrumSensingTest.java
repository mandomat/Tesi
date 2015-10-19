package it.sp4te.CooperativeSpectrumSensing.Main;

import java.util.ArrayList;
import java.util.HashMap;

import it.sp4te.CooperativeSpectrumSensing.Agents.SecondaryUser;
import it.sp4te.CooperativeSpectrumSensing.DomainModel.Signal;
import it.sp4te.CooperativeSpectrumSensing.Functions.SignalFunctions;
import it.sp4te.CooperativeSpectrumSensing.GraphGenerator.GraphGenerator;

public class SpectrumSensingTest {
	public static void main(String args[]) throws Exception {

		int length = 1000; // poi 10000
		int attempts = 1000;
		int inf = -30;
		int sup = 5;

		// blocchi energy Detector
		int block = 10;
		int block2 = 100;
		int block3 = 300;

		ArrayList<Double> EnergyDetection10 = new ArrayList<Double>();
		ArrayList<Double> EnergyDetection100 = new ArrayList<Double>();
		ArrayList<Double> EnergyDetection300 = new ArrayList<Double>();
		HashMap<String, ArrayList<Double>> DetectionGraph = new HashMap<String, ArrayList<Double>>();

		Signal s = new Signal(length);
		SecondaryUser SU = new SecondaryUser(s, length, SignalFunctions.signalEnergy(s), attempts, inf, sup);

		EnergyDetection10 = SU.spectrumSensingEnergyDetector(block);
		EnergyDetection100 = SU.spectrumSensingEnergyDetector(block2);
		EnergyDetection300 = SU.spectrumSensingEnergyDetector(block3);

		DetectionGraph.put("Energy Detection 10 block", EnergyDetection10);
		DetectionGraph.put("Energy Detection 100 block ", EnergyDetection100);
		DetectionGraph.put("Energy Detection 300 block ", EnergyDetection300);

		GraphGenerator.drawGraph("Energy Detection Methods", DetectionGraph, inf, sup);

	}
}