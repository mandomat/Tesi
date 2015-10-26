package it.sp4te.css.main;

import java.util.ArrayList;
import java.util.HashMap;

import it.sp4te.css.agents.BelievableSecondaryUser;
import it.sp4te.css.agents.MaliciousSecondaryUser;
import it.sp4te.css.agents.PrimaryUser;
import it.sp4te.css.fusioncenter.FusionCenter;
import it.sp4te.css.graphgenerator.GraphGenerator;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.SignalProcessor;

/**Questo classe modella 4 tipi di scenario : In uno � presente un numero di utenti malevoli che riportano sempre l'assenza dell'utente
 * primario in uno scenario in cui � presente. In un secondo scenario riportano sempre la presenza dell'utente primario in uno scenario in cui
 * � assente. Negli altri 2 scenari abbiamo un utente malevolo intelligente che genera l'opposto del risultato dell'energy Detector: riporta 1 se l'utente �
 * assente e 0 se � presente. Attiviamo questo utente malevolo sia in presenza che in assenza dell'utente primario**/

public class MaliciousCoopeartiveSpectrumSensing {

	public static void main(String args[]) throws Exception {
		//------------------------------------------------Presenza utente primario-------------------//	
		ArrayList<Double> CooperativeEnergyDetectionAndFusionPresence = new ArrayList<Double>();;
		ArrayList<Double> CooperativeEnergyDetectionOrFusionPresence = new ArrayList<Double>();;
		ArrayList<Double> CooperativeEnergyDetectionMajorityFusionPresence = new ArrayList<Double>();;
		
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionPresence=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		
		HashMap<String, ArrayList<Double>> DetectionGraph = new HashMap<String, ArrayList<Double>>();

		// Setto i parametri
				int length = 1000; // poi 10000
				int attempts = 100;
				int inf = -30;
				int sup = 5;
				int block=10; //blocchi energy Detector
				double pfa=0.01; //probabilit� di falso allarme
		
		//Creo il Fusion center
		FusionCenter FC=new FusionCenter();
		//Creo l'utente primario
		PrimaryUser PU= new PrimaryUser();
		//Creo gli utenti secondari
		BelievableSecondaryUser FirstSU=new BelievableSecondaryUser();
		BelievableSecondaryUser SecondSU=new BelievableSecondaryUser();
		BelievableSecondaryUser ThirdSU=new BelievableSecondaryUser();
		
		//Utenti malevoli
		MaliciousSecondaryUser firstMSU=new MaliciousSecondaryUser();
		MaliciousSecondaryUser secondSMU=new MaliciousSecondaryUser();
		
		 //creo il segnale
        Signal s = PU.createAndSend(length);
        
        //Gli utenti secondari si mettono in ascolto sul canale
        FirstSU.listenChannel(s, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
        SecondSU.listenChannel(s, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
        ThirdSU.listenChannel(s, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
        
        firstMSU.listenChannel(s, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
        secondSMU.listenChannel(s, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
        
        //Creo i vettori contenenti le decisioni binarie sulla presenza o assenza dell'utente primario.Le inserisco in una
        //mappa
        userToBinaryDecisionPresence.put(FirstSU.toString(), FirstSU.computeBinaryDecision(pfa));
        userToBinaryDecisionPresence.put(SecondSU.toString(), SecondSU.computeBinaryDecision(pfa));
        userToBinaryDecisionPresence.put(ThirdSU.toString(), ThirdSU.computeBinaryDecision(pfa));
        //Gli utenti malevoli generano un vettore di decisioni in cui l'utente primario � sempre assente
        userToBinaryDecisionPresence.put(firstMSU.toString(),  firstMSU.computeAbsenceBinaryDecision());
        userToBinaryDecisionPresence.put(secondSMU.toString(), secondSMU.computeAbsenceBinaryDecision());
        
      //Tutte le decisioni di tutti gli utenti secondari passano al fusion center che riporter� una decisione
        //globale secondo tre tecniche di fusione: AND OR e MAJORITY. 
        CooperativeEnergyDetectionAndFusionPresence=FC.decisionAndFusion(inf, sup,userToBinaryDecisionPresence);
        CooperativeEnergyDetectionOrFusionPresence=FC.decisionOrFusion(inf, sup,userToBinaryDecisionPresence);
        CooperativeEnergyDetectionMajorityFusionPresence=FC.decisionMajorityFusion(inf, sup,userToBinaryDecisionPresence);
        
        DetectionGraph.put("CED with AND fusion", CooperativeEnergyDetectionAndFusionPresence);
        DetectionGraph.put("CED with OR fusion", CooperativeEnergyDetectionOrFusionPresence);
        DetectionGraph.put("CED with MAJORITY fusion", CooperativeEnergyDetectionMajorityFusionPresence);

		GraphGenerator.drawGraph("Presence of PU in Cooperative Energy Detection (CED) with Malicious User",DetectionGraph, inf, sup);
		
		
		//------------------------------------------------Assenza utente primario-------------------//
		ArrayList<Double> CooperativeEnergyDetectionAndFusionAbsence = new ArrayList<Double>();;
		ArrayList<Double> CooperativeEnergyDetectionOrFusionAbsence = new ArrayList<Double>();;
		ArrayList<Double> CooperativeEnergyDetectionMajorityFusionAbsence = new ArrayList<Double>();;
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsence=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String, ArrayList<Double>> DetectionGraph2 = new HashMap<String, ArrayList<Double>>();


		BelievableSecondaryUser FirstSU2=new BelievableSecondaryUser();
		BelievableSecondaryUser SecondSU2=new BelievableSecondaryUser();
		BelievableSecondaryUser ThirdSU2=new BelievableSecondaryUser();
		
		MaliciousSecondaryUser firstMSU2=new MaliciousSecondaryUser();
		MaliciousSecondaryUser secondSMU2=new MaliciousSecondaryUser();
		
	     FirstSU2.listenChannel(null, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
	     SecondSU2.listenChannel(null, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
	     ThirdSU2.listenChannel(null, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
	     firstMSU2.listenChannel(null, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
	     secondSMU2.listenChannel(null, length, SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
        
	     userToBinaryDecisionAbsence.put(FirstSU2.toString(), FirstSU2.computeBinaryDecision(pfa));
	     userToBinaryDecisionAbsence.put(SecondSU2.toString(), SecondSU2.computeBinaryDecision(pfa));
	     userToBinaryDecisionAbsence.put(ThirdSU2.toString(), ThirdSU2.computeBinaryDecision(pfa));
	   //Gli utenti malevoli generano un vettore di decisioni in cui l'utente primario � sempre presente
	     userToBinaryDecisionAbsence.put(firstMSU2.toString(),  firstMSU2.computePresenceBinaryDecision());
	     userToBinaryDecisionAbsence.put(secondSMU2.toString(), secondSMU2.computePresenceBinaryDecision());
        
	     CooperativeEnergyDetectionAndFusionAbsence=FC.decisionAndFusion(inf, sup,userToBinaryDecisionAbsence);
	     CooperativeEnergyDetectionOrFusionAbsence=FC.decisionOrFusion(inf, sup,userToBinaryDecisionAbsence);
	     CooperativeEnergyDetectionMajorityFusionAbsence=FC.decisionMajorityFusion(inf, sup,userToBinaryDecisionAbsence);
	       
        
        DetectionGraph2.put("CED with AND fusion", CooperativeEnergyDetectionAndFusionAbsence);
        DetectionGraph2.put("CED with OR fusion", CooperativeEnergyDetectionOrFusionAbsence);
        DetectionGraph2.put("CED with MAJORITY fusion", CooperativeEnergyDetectionMajorityFusionAbsence);
        
       
      GraphGenerator.drawGraph("Absence of PU in Cooperative Energy Detection (CED) with Malicious User",DetectionGraph2, inf, sup);
		
      //------------------------------------------------Presenz autente primario con utente malevolo intelligente-------------------//
      CooperativeEnergyDetectionAndFusionPresence.clear() ;
	  CooperativeEnergyDetectionOrFusionPresence.clear(); ;
	  CooperativeEnergyDetectionMajorityFusionPresence.clear();
	  userToBinaryDecisionPresence.clear();
	  DetectionGraph.clear();
	  
	  userToBinaryDecisionPresence.put(FirstSU.toString(), FirstSU.computeBinaryDecision(pfa));
      userToBinaryDecisionPresence.put(SecondSU.toString(), SecondSU.computeBinaryDecision(pfa));
      userToBinaryDecisionPresence.put(ThirdSU.toString(), ThirdSU.computeBinaryDecision(pfa));
      
      //Questo utente malevolo riporta l'opposto dell'emergy detector: riporta 1 se l'utente � assente, 0 se � presente
      userToBinaryDecisionPresence.put(firstMSU.toString(),  firstMSU.computeOppositeBinaryDecision(pfa));
      userToBinaryDecisionPresence.put(secondSMU.toString(), secondSMU.computeOppositeBinaryDecision(pfa));
      
      CooperativeEnergyDetectionAndFusionPresence=FC.decisionAndFusion(inf, sup,userToBinaryDecisionPresence);
      CooperativeEnergyDetectionOrFusionPresence=FC.decisionOrFusion(inf, sup,userToBinaryDecisionPresence);
      CooperativeEnergyDetectionMajorityFusionPresence=FC.decisionMajorityFusion(inf, sup,userToBinaryDecisionPresence);
      
      DetectionGraph.put("CED with AND fusion", CooperativeEnergyDetectionAndFusionPresence);
      DetectionGraph.put("CED with OR fusion", CooperativeEnergyDetectionOrFusionPresence);
      DetectionGraph.put("CED with MAJORITY fusion", CooperativeEnergyDetectionMajorityFusionPresence);

		GraphGenerator.drawGraph("Presence of PU in Cooperative Energy Detection (CED) with Intelligent Malicious User",DetectionGraph, inf, sup);
	
	      //------------------------------------------------Assenza utente primario con utente malevolo intelligente-------------------//

		
		 CooperativeEnergyDetectionAndFusionAbsence.clear() ;
		 CooperativeEnergyDetectionOrFusionAbsence.clear(); ;
		 CooperativeEnergyDetectionMajorityFusionAbsence.clear();
		 userToBinaryDecisionAbsence.clear();
		 DetectionGraph2.clear();
		 
		 userToBinaryDecisionAbsence.put(FirstSU2.toString(), FirstSU2.computeBinaryDecision(pfa));
	     userToBinaryDecisionAbsence.put(SecondSU2.toString(), SecondSU2.computeBinaryDecision(pfa));
	     userToBinaryDecisionAbsence.put(ThirdSU2.toString(), ThirdSU2.computeBinaryDecision(pfa));
	     
	      //Questo utente malevolo riporta l'opposto dell'emergy detector: riporta 1 se l'utente � assente, 0 se � presente
	     userToBinaryDecisionAbsence.put(firstMSU2.toString(),  firstMSU2.computeOppositeBinaryDecision(pfa));
	     userToBinaryDecisionAbsence.put(secondSMU2.toString(), secondSMU2.computeOppositeBinaryDecision(pfa));
	     
	     CooperativeEnergyDetectionAndFusionAbsence=FC.decisionAndFusion(inf, sup,userToBinaryDecisionAbsence);
	     CooperativeEnergyDetectionOrFusionAbsence=FC.decisionOrFusion(inf, sup,userToBinaryDecisionAbsence);
	     CooperativeEnergyDetectionMajorityFusionAbsence=FC.decisionMajorityFusion(inf, sup,userToBinaryDecisionAbsence);
	     
	     DetectionGraph2.put("CED with AND fusion", CooperativeEnergyDetectionAndFusionAbsence);
	     DetectionGraph2.put("CED with OR fusion", CooperativeEnergyDetectionOrFusionAbsence);
	     DetectionGraph2.put("CED with MAJORITY fusion", CooperativeEnergyDetectionMajorityFusionAbsence);
	        
	       
	      GraphGenerator.drawGraph("Absence of PU in Cooperative Energy Detection (CED) with Intelligent Malicious User",DetectionGraph2, inf, sup);
	}
        
        
	}