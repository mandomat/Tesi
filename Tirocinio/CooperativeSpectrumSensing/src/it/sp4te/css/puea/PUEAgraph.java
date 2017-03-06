package it.sp4te.css.puea;

import java.util.ArrayList;
import java.util.HashMap;

import it.sp4te.css.agents.PrimaryUser;
import it.sp4te.css.agents.PrimaryUserEmulator;
import it.sp4te.css.agents.TrustedSecondaryUser;
import it.sp4te.css.graphgenerator.Chart4jGraphGenerator;
import it.sp4te.css.model.Noise;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.MathFunctions;
import it.sp4te.css.signalprocessing.SignalProcessor;
import it.sp4te.css.puea.PUEA;

public class PUEAgraph {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int length = 1000; // poi 10000
		int attempts = 1000; //tentativi
		double inf = -30; //da -20db
		double sup = -10; //a 5.5db
		int block=10; //blocchi energy Detector
		double pfa=0.01; //probabilità di falso allarm
		String fileNameHs2_Hs3_08="thresholdsHs2_Hs30.01_08";
		String fileNameHs2_Hs3_09="thresholdsHs2_Hs30.01_09";
		String fileNameHs2_Hs3_10="thresholdsHs2_Hs30.01_10";
		String fileNameHs2_Hs3_11="thresholdsHs2_Hs30.01_11";
		String fileNameHs2_Hs3_12="thresholdsHs2_Hs30.01_12";
		String fileNameHs0_Hs1="thresholds0.01";

		ArrayList<Double> TraditionalEnergyDetectionHs2_Hs3_08 = new ArrayList<Double>();
		ArrayList<Double> TraditionalEnergyDetectionHs2_Hs3_09 = new ArrayList<Double>();
		ArrayList<Double> TraditionalEnergyDetectionHs2_Hs3_10 = new ArrayList<Double>();
		ArrayList<Double> TraditionalEnergyDetectionHs2_Hs3_11 = new ArrayList<Double>();
		ArrayList<Double> TraditionalEnergyDetectionHs2_Hs3_12 = new ArrayList<Double>();
		ArrayList<Double> TraditionalEnergyDetectionHs0_Hs1 = new ArrayList<Double>();
		
		// Creo una mappa per creare il grafico. La mappa deve essere formata da
				// nomeDetection->valori
		HashMap<String, ArrayList<Double>> DetectionGraph = new HashMap<String, ArrayList<Double>>();
		
		//Creo l'utente primario e ne genero il segnale
		PrimaryUser PU= new PrimaryUser();
		Signal PUsignal = PU.createAndSend(length);
		
		//Creo l'attaccante e ne genero segnale a potenza 1
		PrimaryUserEmulator PUE = new PrimaryUserEmulator();
		Signal PUEsignal = PUE.createAndSend(length, 1);
		
		//Genero il rumore
		Noise noise = new Noise(0,length,1);
		
		//Mi metto nella situazione Hs3, ovvero sommo il segnale dell'attaccante col segnale del primario e il rumore
		Signal PUE_noise =  new Signal(length,0.0); //mi serve per immagazzinare la somma attaccante+rumore
		PUE_noise.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUsignal.getSamplesIm()));
		PUE_noise.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUsignal.getSamplesRe())); //Aggiungo il rumore a uno dei due
		Signal PU_PUE_noise =  new Signal(length,0.0);//mi serve per immagazzinare la somma (attaccante+rumore)+primario
		PU_PUE_noise.setSamplesIm(MathFunctions.SumVector(PUE_noise.getSamplesIm(), PUsignal.getSamplesIm()));
		PU_PUE_noise.setSamplesRe(MathFunctions.SumVector(PUE_noise.getSamplesRe(), PUsignal.getSamplesRe()));
		
		
		//Utente secondario fidato che farà lo spectrum sensing
		TrustedSecondaryUser SU=new TrustedSecondaryUser();
		//L'utente secondario si mette in ascolto sul canale nel caso Hs3
	    SU.listenChannel(PU_PUE_noise, PU_PUE_noise.getLenght(), SignalProcessor.computeEnergy(PU_PUE_noise), attempts, inf, sup, block);

		TraditionalEnergyDetectionHs2_Hs3_08=SU.spectrumSensingTraditionalEnergyDetector(pfa,fileNameHs2_Hs3_08);
		DetectionGraph.put("Traditional Energy Detection Hs2/Hs3 0.8", TraditionalEnergyDetectionHs2_Hs3_08);
		TraditionalEnergyDetectionHs2_Hs3_09=SU.spectrumSensingTraditionalEnergyDetector(pfa,fileNameHs2_Hs3_09);
		DetectionGraph.put("Traditional Energy Detection Hs2/Hs3 0.9", TraditionalEnergyDetectionHs2_Hs3_09);
		TraditionalEnergyDetectionHs2_Hs3_10=SU.spectrumSensingTraditionalEnergyDetector(pfa,fileNameHs2_Hs3_10);
		DetectionGraph.put("Traditional Energy Detection Hs2/Hs3 1.0", TraditionalEnergyDetectionHs2_Hs3_10);
		TraditionalEnergyDetectionHs2_Hs3_11=SU.spectrumSensingTraditionalEnergyDetector(pfa,fileNameHs2_Hs3_11);
		DetectionGraph.put("Traditional Energy Detection Hs2/Hs3 1.1", TraditionalEnergyDetectionHs2_Hs3_11);
		TraditionalEnergyDetectionHs2_Hs3_12=SU.spectrumSensingTraditionalEnergyDetector(pfa,fileNameHs2_Hs3_12);
		DetectionGraph.put("Traditional Energy Detection Hs2/Hs3 1.2", TraditionalEnergyDetectionHs2_Hs3_12);
		
		//L'utente secondario si mette in ascolto sul canale nel caso Hs1
	    SU.listenChannel(PUE_noise, PUE_noise.getLenght(), SignalProcessor.computeEnergy(PUE_noise), attempts, inf, sup, block);
		TraditionalEnergyDetectionHs0_Hs1=SU.spectrumSensingTraditionalEnergyDetector(pfa,fileNameHs0_Hs1);
		DetectionGraph.put("Traditional Energy Detection Hs0/Hs1", TraditionalEnergyDetectionHs0_Hs1);

		Chart4jGraphGenerator SpectrumSensingGraph= new Chart4jGraphGenerator();
		SpectrumSensingGraph.drawSNRtoDetectionGraph("Detection Methods",DetectionGraph, inf, sup);

	}

}
