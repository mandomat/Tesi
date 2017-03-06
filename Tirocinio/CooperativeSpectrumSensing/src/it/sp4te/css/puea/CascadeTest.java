package it.sp4te.css.puea;

import java.util.ArrayList;
import java.util.HashMap;

import it.sp4te.css.agents.FusionCenter;
import it.sp4te.css.agents.PrimaryUser;
import it.sp4te.css.agents.PrimaryUserEmulator;
import it.sp4te.css.model.Noise;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.MathFunctions;
import it.sp4te.css.signalprocessing.SignalProcessor;
import it.sp4te.css.signalprocessing.Utils;
import it.sp4te.css.agents.TrustedSecondaryUser;
import it.sp4te.css.graphgenerator.Chart4jGraphGenerator;

public class CascadeTest {
	static int length = 1000; // poi 10000
	static int attempts = 1000; //tentativi
	static double inf = -30; //da -20db
	static double sup = 0; //a 5.5db
	static int block=10; //blocchi energy Detector
	static double pfa=0.01; //probabilità di falso allarme
	static int SUs =5;
	static String H1_file = "thresholds0.01";
	static String H3_file = "thresholdsHs1_Hs";
	static String H1_H2_file = "thresholdsHs2_Hs30.01_0.7991999999999717";
	
	
	public static void findHypothesisCSS(Signal s) throws Exception{
		//Creo il Fusion center
		FusionCenter FC=new FusionCenter();
		//lista di utenti secondari 
		ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers;
		//mappa di decisioni di utenti secondari
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionPresence=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		//liste per le decisioni del FC
		ArrayList<Double> CooperativeEnergyDetectionAndFusionPresence = new ArrayList<Double>();
		ArrayList<Double> CooperativeEnergyDetectionOrFusionPresence = new ArrayList<Double>();
		ArrayList<Double> CooperativeEnergyDetectionMajorityFusionPresence = new ArrayList<Double>();
		//per il grafico
		HashMap<String, ArrayList<Double>> DetectionGraph = new HashMap<String, ArrayList<Double>>();
		
		//genero 5 utenti secondari
		TrustedSecondaryUsers= Utils.createTrustedSecondaryUsers(SUs,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		//gli utenti secondari fanno spectrum sensing con l'energy detector
		userToBinaryDecisionPresence=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa,H1_file);
		
		//Tutte le decisioni di tutti gli utenti secondari passano al fusion center che riporterà una decisione
				//globale secondo tre tecniche di fusione: AND OR e MAJORITY. 
				CooperativeEnergyDetectionAndFusionPresence=FC.andDecision(inf, sup,userToBinaryDecisionPresence);
				CooperativeEnergyDetectionOrFusionPresence=FC.orDecision(inf, sup,userToBinaryDecisionPresence);
				CooperativeEnergyDetectionMajorityFusionPresence=FC.majorityDecision(inf, sup,userToBinaryDecisionPresence);

				DetectionGraph.put("CED with AND fusion", CooperativeEnergyDetectionAndFusionPresence);
				DetectionGraph.put("CED with OR fusion", CooperativeEnergyDetectionOrFusionPresence);
				DetectionGraph.put("CED with MAJORITY fusion", CooperativeEnergyDetectionMajorityFusionPresence);

				Chart4jGraphGenerator presenceGraph= new Chart4jGraphGenerator();
				presenceGraph.drawSNRtoDetectionGraph("Presence of PU in Cooperative Energy Detection (CED) with Malicious User",DetectionGraph, inf, sup);
		
		
	}
	
	public static void findHypothesisSingleSU(Signal s) throws Exception{
		boolean H0 = true;
		boolean H3 = false;
		
		
		//Utente secondario fidato che farà lo spectrum sensing
				TrustedSecondaryUser SU=new TrustedSecondaryUser();
				//L'utente secondario si mette in ascolto sul canale nel caso Hs3
			    SU.listenChannel(s, s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
			  //per il grafico
				HashMap<String, ArrayList<Double>> DetectionGraph = new HashMap<String, ArrayList<Double>>();
				
				ArrayList<Double> TraditionalEnergyDetectionHs0_Hs1 = new ArrayList<Double>();
				ArrayList<Double> TraditionalEnergyDetectionHs1_Hs2_powerful = new ArrayList<Double>();
				ArrayList<Double> TraditionalEnergyDetectionHs1_Hs2_notPowerful = new ArrayList<Double>();
				ArrayList<Double> TraditionalEnergyDetectionHs2_Hs3 = new ArrayList<Double>();
				
				TraditionalEnergyDetectionHs0_Hs1=SU.spectrumSensingTraditionalEnergyDetector(pfa,H1_file);
				DetectionGraph.put("H1", TraditionalEnergyDetectionHs0_Hs1);
				
				TraditionalEnergyDetectionHs2_Hs3=SU.spectrumSensingTraditionalEnergyDetector(pfa,H3_file);
				DetectionGraph.put("H3", TraditionalEnergyDetectionHs2_Hs3);
				
				
				for (double ED: TraditionalEnergyDetectionHs0_Hs1){
					if(ED>=99.0){
						H0=false;
					System.out.println("non siamo in H0");
					}
					}
				
				for (double ED: TraditionalEnergyDetectionHs2_Hs3){
					if(ED>=99.0){
						H3=true;
					System.out.println("siamo in H3");
					}
					}
				if(H3 == false)
					System.out.println("non siamo in H3");
				
				if(H0==true && H3 == false)
					System.out.println("siamo in H0");
				
				if(H0==false && H3==false){
					System.out.println("Verifico se posso distinguere tra H1 e H2");
					TraditionalEnergyDetectionHs1_Hs2_powerful =SU.spectrumSensingTraditionalEnergyDetector(pfa,H1_H2_file);
					DetectionGraph.put("PUEA potenza maggiore", TraditionalEnergyDetectionHs1_Hs2_powerful);
					TraditionalEnergyDetectionHs1_Hs2_notPowerful =SU.spectrumSensingTraditionalEnergyDetectorPUEA(pfa,H1_H2_file);
					DetectionGraph.put("PUEA potenza minore", TraditionalEnergyDetectionHs1_Hs2_notPowerful);
					
				
				}
				Chart4jGraphGenerator SpectrumSensingGraph= new Chart4jGraphGenerator();
				SpectrumSensingGraph.drawSNRtoDetectionGraph("Detection Methods",DetectionGraph, inf, sup);
	}
	
	public static void main(String[] args) throws Exception {
		
		//Creo l'utente primario e ne genero il segnale
				PrimaryUser PU= new PrimaryUser();
				Signal PUsignal = PU.createAndSend(length);
				
		//Creo l'attaccante e ne genero segnale a potenza 1
				PrimaryUserEmulator PUE = new PrimaryUserEmulator();
				Signal PUEsignal_1 = PUE.createAndSend(length, 1);
				Signal PUEsignal_08 = PUE.createAndSend(length, 0.8);
				Signal PUEsignal_09 = PUE.createAndSend(length, 0.9);
				Signal PUEsignal_11 = PUE.createAndSend(length, 1.1);
				Signal PUEsignal_12 = PUE.createAndSend(length, 1.2);
		
		//Genero il rumore
				Noise noise = new Noise(0,length,1); //Che è anche il segnale per il test su H0
				
		//Genereo il segnale per H1 (PU+rumore)
				Signal H1_signal =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H1_signal.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUsignal.getSamplesIm()));
//				H1_signal.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUsignal.getSamplesRe()));
//	    //Genero il segnale per H2 (nei diversi casi di potenza dell'attaccante)
				Signal H2_signal_1 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
				H2_signal_1.setSamplesIm(MathFunctions.SumVector(PUsignal.getSamplesIm(), PUEsignal_1.getSamplesIm()));
				H2_signal_1.setSamplesRe(MathFunctions.SumVector(PUsignal.getSamplesRe(), PUEsignal_1.getSamplesRe()));
//				
//				Signal H2_signal_08 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H2_signal_08.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignal_08.getSamplesIm()));
//				H2_signal_08.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignal_08.getSamplesRe()));
//				
//				Signal H2_signal_09 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H2_signal_09.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignal_09.getSamplesIm()));
//				H2_signal_09.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignal_09.getSamplesRe()));
//				
//				Signal H2_signal_11 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H2_signal_11.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignal_11.getSamplesIm()));
//				H2_signal_11.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignal_11.getSamplesRe()));
//				
//				Signal H2_signal_12 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H2_signal_12.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignal_12.getSamplesIm()));
//				H2_signal_12.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignal_12.getSamplesRe()));
//       //Genero il segnale per H3 (nei diversi casi di potenza dell'attaccante)
//				Signal PU_noise = new Signal(length,0.0);
//				PU_noise.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUsignal.getSamplesIm()));
//				PU_noise.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUsignal.getSamplesRe()));
//				
//				Signal H3_signal_1 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H3_signal_1.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_1.getSamplesIm()));
//				H3_signal_1.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_1.getSamplesRe()));
//				
//				Signal H3_signal_08 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H3_signal_08.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_08.getSamplesIm()));
//				H3_signal_08.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_08.getSamplesRe()));
//				
//				Signal H3_signal_09 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H3_signal_09.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_09.getSamplesIm()));
//				H3_signal_09.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_09.getSamplesRe()));
//				
//				Signal H3_signal_11 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H3_signal_11.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_11.getSamplesIm()));
//				H3_signal_11.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_11.getSamplesRe()));
//				
//				Signal H3_signal_12 =  new Signal(length,0.0); //mi serve per immagazzinare le somme dei segnali
//				H3_signal_12.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_12.getSamplesIm()));
//				H3_signal_12.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_12.getSamplesRe()));
				
				findHypothesisSingleSU(PUEsignal_12);
				
				
	}
}
