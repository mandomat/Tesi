package it.sp4te.css.main;

import java.util.ArrayList;
import java.util.HashMap;

import it.sp4te.css.agents.FusionCenter;
import it.sp4te.css.agents.MaliciousSecondaryUser;
import it.sp4te.css.agents.PrimaryUser;
import it.sp4te.css.agents.TrustedSecondaryUser;
import it.sp4te.css.graphgenerator.GraphGenerator;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.SignalProcessor;
import it.sp4te.css.signalprocessing.Utils;

public class ListCSS {

	
	public static void main(String args[]) throws Exception {
		ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers;
		ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers;

		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsence=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		ArrayList<Double> ListCooperativeEnergyDetection = new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeEnergyDetectionAbsence = new ArrayList<Double>();;
        HashMap<String, ArrayList<Double>> DetectionGraph = new HashMap<String, ArrayList<Double>>();


		// Setto i parametri
		int length = 1000; // poi 10000
		int attempts = 300;
		int inf = -30;
		int sup = 5;
		int block=10; //blocchi energy Detector
		double pfa=0.01; //probabilit� di falso allarme
		int numberTSU=20;
		int numberMSU=9;//numero di utenti fidati
		int K=2;
		int L=2;
		int M=2;
		int N=2;

		//Creo il Fusion center
		FusionCenter FC=new FusionCenter();
		//Creo l'utente primario
		PrimaryUser PU= new PrimaryUser();
		//creo il segnale
		Signal s = PU.createAndSend(length);

		//Creo gli utenti secondari
		TrustedSecondaryUsers= Utils.createTrustedSecondaryUsers(numberTSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);

		//Creo i vettori contenenti le decisioni binarie sulla presenza o assenza dell'utente primario.Le inserisco in una
		//mappa
		userToBinaryDecision=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
		ListCooperativeEnergyDetection= FC.ListBasedDecision(inf, sup, userToBinaryDecision, attempts, K, L, M, N);
		DetectionGraph.put("Without malicious users", ListCooperativeEnergyDetection);
		
		TrustedSecondaryUsers.clear();
		userToBinaryDecision.clear();
		
		TrustedSecondaryUsers= Utils.createTrustedSecondaryUsers(numberTSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		MaliciousSecondaryUsers=Utils.createMaliciousSecondaryUsers(numberMSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		
		userToBinaryDecisionAbsence=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
		//Gli utenti malevoli di questa tipologia generano un vettore di decisioni in cui l'utente primario � sempre assente
		userToBinaryDecisionAbsence.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ListCooperativeEnergyDetectionAbsence= FC.ListBasedDecision(inf, sup, userToBinaryDecisionAbsence, attempts, K, L, M, N);

		DetectionGraph.put("With Absence MSU", ListCooperativeEnergyDetectionAbsence);

		
		GraphGenerator.drawGraph("Presence of PU in List Cooperative Energy Detection and K=L=M=N=2 ",DetectionGraph, inf, sup);

	}
}
