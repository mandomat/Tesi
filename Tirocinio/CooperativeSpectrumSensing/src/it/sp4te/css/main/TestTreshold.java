package it.sp4te.css.main;

import java.io.FileNotFoundException;

import it.sp4te.css.signalprocessing.SignalProcessor;
import it.sp4te.css.signalprocessing.Utils;

public class TestTreshold {
	
	public static void main(String[] args) throws Exception{
		int length = 1000; // poi 10000
		int attempts = 1000;
		int inf = -10;
		double sup = 5.5;
		int block=10; //blocchi energy Detector
		double pfa=0.01; //probabilità di falso allarme
		int energy =4;

	 // Utils.generateThreshold(length, energy, attempts, inf, sup, pfa);
		
		double threshold = SignalProcessor.getEnergyDetectorThreshold(0.01, 4.0);
		System.out.println(""+threshold);
		SignalProcessor.getEnergyDetectorThreshold(0.01, 10);
		
	}

}
