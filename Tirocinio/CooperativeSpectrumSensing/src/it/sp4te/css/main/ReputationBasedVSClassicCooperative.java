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
	
	/**Classe per il confronto tra i metodi di simulazione Cooperativi tramite tecniche and,or,majority e
	 * il metodo di reputazione. Realizza 4 tipi di simulazione: un caso ideale (senza utenti malevoli), un caso con utente malevolo che dichiara sempre la presenza
	 * dell'utente primario, un caso con utente malevolo che dichiara sempre l'assenza dell'utente primario e un ultimo caso con utente malevolo**/
	public class ReputationBasedVSClassicCooperative {
		
		
		
		public static void main(String args[]) throws Exception {
			//------------------------------------------------Presenza utente primario-------------------//
			ArrayList<Double> CooperativeEnergyDetectionAndFusion = new ArrayList<Double>();;
			ArrayList<Double> CooperativeEnergyDetectionOrFusion = new ArrayList<Double>();;
			ArrayList<Double> CooperativeEnergyDetectionMajorityFusion = new ArrayList<Double>();;
			ArrayList<Double> reputationBasedCSS = new ArrayList<Double>();;


			HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();
	        HashMap<String, ArrayList<Double>> DetectionGraph = new HashMap<String, ArrayList<Double>>();

	        ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers;
	        ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers;

	        
			// Setto i parametri
			int length = 1000; // poi 10000
			int attempts = 200;
			int inf = -30;
			int sup = 5;
			int block=10; //blocchi energy Detector
			double pfa=0.01; //probabilit� di falso allarme
			int numberTSU=15;
			int numberMSU=10;//numero di utenti fidati

			//Creo il Fusion center
			FusionCenter FC=new FusionCenter();
			//Creo l'utente primario
			PrimaryUser PU= new PrimaryUser();
			//creo il segnale
			Signal s = PU.createAndSend(length);
			
			
			
			//ABSENCE
					
			
			TrustedSecondaryUsers= Utils.createTrustedSecondaryUsers(numberTSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
			MaliciousSecondaryUsers=Utils.createMaliciousSecondaryUsers(numberMSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
			
			userToBinaryDecision=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
			userToBinaryDecision.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
			
			CooperativeEnergyDetectionAndFusion=FC.andDecision(inf, sup,userToBinaryDecision);
			CooperativeEnergyDetectionOrFusion=FC.orDecision(inf, sup,userToBinaryDecision);
			CooperativeEnergyDetectionMajorityFusion=FC.majorityDecision(inf, sup,userToBinaryDecision);
			reputationBasedCSS= FC.reputationBasedDecision(inf, sup, userToBinaryDecision, attempts);


			DetectionGraph.put("CED AND fusion", CooperativeEnergyDetectionAndFusion);
			DetectionGraph.put("CED OR fusion", CooperativeEnergyDetectionOrFusion);
			DetectionGraph.put("CED MAJORITY fusion", CooperativeEnergyDetectionMajorityFusion);
			DetectionGraph.put("Reputation Based", reputationBasedCSS);
			GraphGenerator.drawGraph("Cooperative CSS VS Reputation Based CSS: Absence Malicious User",DetectionGraph, inf, sup);
			
			//PRESENCE
			TrustedSecondaryUsers.clear();
			MaliciousSecondaryUsers.clear();
			
			userToBinaryDecision.clear();
			DetectionGraph.clear();
			CooperativeEnergyDetectionAndFusion.clear();
			CooperativeEnergyDetectionOrFusion.clear();
			CooperativeEnergyDetectionMajorityFusion.clear();
			reputationBasedCSS.clear();
			
			
			TrustedSecondaryUsers= Utils.createTrustedSecondaryUsers(numberTSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
			MaliciousSecondaryUsers=Utils.createMaliciousSecondaryUsers(numberMSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
			
			userToBinaryDecision=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
			userToBinaryDecision.putAll(Utils.generetePresenceBinaryDecisionVectors(MaliciousSecondaryUsers));
			
			CooperativeEnergyDetectionAndFusion=FC.andDecision(inf, sup,userToBinaryDecision);
			CooperativeEnergyDetectionOrFusion=FC.orDecision(inf, sup,userToBinaryDecision);
			CooperativeEnergyDetectionMajorityFusion=FC.majorityDecision(inf, sup,userToBinaryDecision);
			reputationBasedCSS= FC.reputationBasedDecision(inf, sup, userToBinaryDecision, attempts);


			DetectionGraph.put("CED AND fusion", CooperativeEnergyDetectionAndFusion);
			DetectionGraph.put("CED OR fusion", CooperativeEnergyDetectionOrFusion);
			DetectionGraph.put("CED MAJORITY fusion", CooperativeEnergyDetectionMajorityFusion);
			DetectionGraph.put("Reputation Based", reputationBasedCSS);
			GraphGenerator.drawGraph("Cooperative CSS VS Reputation Based CSS: Presence Malicious User",DetectionGraph, inf, sup);
			
			//OPPOSITE
			TrustedSecondaryUsers.clear();
			MaliciousSecondaryUsers.clear();
			userToBinaryDecision.clear();
			DetectionGraph.clear();
			CooperativeEnergyDetectionAndFusion.clear();
			CooperativeEnergyDetectionOrFusion.clear();
			CooperativeEnergyDetectionMajorityFusion.clear();
			reputationBasedCSS.clear();
			
			TrustedSecondaryUsers= Utils.createTrustedSecondaryUsers(numberTSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
			MaliciousSecondaryUsers=Utils.createMaliciousSecondaryUsers(numberMSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
			
			userToBinaryDecision=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
			userToBinaryDecision.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
			
			CooperativeEnergyDetectionAndFusion=FC.andDecision(inf, sup,userToBinaryDecision);
			CooperativeEnergyDetectionOrFusion=FC.orDecision(inf, sup,userToBinaryDecision);
			CooperativeEnergyDetectionMajorityFusion=FC.majorityDecision(inf, sup,userToBinaryDecision);
			reputationBasedCSS= FC.reputationBasedDecision(inf, sup, userToBinaryDecision, attempts);


			DetectionGraph.put("CED AND fusion", CooperativeEnergyDetectionAndFusion);
			DetectionGraph.put("CED OR fusion", CooperativeEnergyDetectionOrFusion);
			DetectionGraph.put("CED MAJORITY fusion", CooperativeEnergyDetectionMajorityFusion);
			DetectionGraph.put("Reputation Based", reputationBasedCSS);
			GraphGenerator.drawGraph("Cooperative CSS VS Reputation Based CSS: Opposite Malicious User",DetectionGraph, inf, sup);			
			
			

	}}
