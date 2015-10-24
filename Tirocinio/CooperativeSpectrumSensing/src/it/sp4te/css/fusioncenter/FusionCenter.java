package it.sp4te.css.fusioncenter;

import java.util.ArrayList;
import java.util.HashMap;
import it.sp4te.css.detection.Detector;

public class FusionCenter {
	
	/**Metodo di fusione con la tecnica and. Ritorna 1 (l'utente primario � presente) se tutte le decisioni
	 * di tutti i dispositivi sono uguali ad 1, 0 altrimenti
	 * @param decisions Un array contenente le decisioni binarie sulla presenza o assenza dell'utente primario per un dato SNR di
	 * ogni utente secondario. La tenica di fusione AND prevede che sia dichiarata la presenza dell'utente primario se tutti gli utenti secondari 
	 * ne verificano la presenza.
	 * Questo metodo verr� iterato per ogni SNR
	 * @return una decisione binaria risultato della fusione secondo la tecnica AND**/
	
	public static int andFusion(ArrayList<Integer> decisions){
		int fusionDecision=1;
		for(int i=0;i<decisions.size();i++){
			if(decisions.get(i)==0){
				fusionDecision=0;
			}
		}
		return fusionDecision;
	}
	
	
	/**Metodo di fusione con la tecnica or. Ritorna 1 (l'utente primario � presente) se almeno una delle decisioni
	 * dei dispositivi sono uguali ad 1, 0 altrimenti
	 *  @param decisions un array contenente le decisioni binarie sulla presenza o assenza dell'utente primario per un dato SNR di
	 * ogni utente secondario. La tecnica di fusione OR prevede che sia dichiarata la presenza dell'utente se almeno un utente secondario
	 * ne verifica la presenza
	 * Questo metodo verr� iterato per ogni SNR
	 * @return una decisione binaria risultato della fusione secondo la tecnica OR**/
	
	public static int orFusion(ArrayList<Integer> decisions){
		int fusionDecision=0;
		
		for(int i=0;i<decisions.size();i++){
			if(decisions.get(i)==1){
				fusionDecision=1;
			}
		}
		return fusionDecision;
	}
	
	
	/**Metodo di fusione con la tecnica or. Ritorna 1 (l'utente primario � presente) se almeno una delle decisioni
	 * dei dispositivi sono uguali ad 1, 0 altrimenti
	 *  @param decisions un array contenente le decisioni binarie sulla presenza o assenza dell'utente primario per un dato SNR di
	 * ogni utente secondario. La tecnica di fusione MAJORITY prevede che sia dichiarata la presenza dell'utente se la met� pi� uno degli
	 * utenti secondari ne verifica la presenza
	 * Questo metodo verr� iterato per ogni SNR
	 * @return una decisione binaria risultato della fusione secondo la tecnica MAJORITY**/
	
	public static int majorityFusion(ArrayList<Integer> decisions){
		int majority=(decisions.size()/2)+1;
		int contPresence=0;
		int fusionDecision = 0;
		for(int i=0;i<decisions.size();i++){
			if(decisions.get(i)==1){
				contPresence++;
			}
		}
		if(contPresence>=majority){
			fusionDecision=1;
		}
		
	
	return fusionDecision;}
	
	/**
	 * Questo metodo prende in input una mappa, contenente per ogni utente secondario (chiave) una lista di decisioni binarie calcolate
	 * per ogni SNR su un numero di prove P. Per ogni SNR prende le decisioni degli utenti secondari, li inserisce in un vettore e richiama il metodo
	 * di detection secondo la tecnica AND. Ritorner� la percentuale di Detection da parte Fusion Center
	 * @param inf Estremo inferiore dell'SNR
	 * @param sup Estremo superiore di SNR
	 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente primario. Come valore ha una lista di liste: per ogni SNR ha una lista
	 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
	 * @return La percentuale di Detection da parte del Fusion Center dopo il metodo di fusione AND**/
	
	 public  ArrayList<Double> decisionAndFusion(int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision){
		 ArrayList<Double> EnergyDetection = new  ArrayList<Double>();
	  	 
	 for(int i=0;i<(sup-inf);i++){
		 ArrayList<ArrayList<Integer>> decisions=new ArrayList<ArrayList<Integer>>();
     	for(String SU: userToBinaryDecision.keySet()){
		 ArrayList<Integer>snrDecisionVector= userToBinaryDecision.get(SU).get(i);
     	  decisions.add(snrDecisionVector);}

     	EnergyDetection.add(Detector.andFusionDetection(decisions));
     	}
		return EnergyDetection;
	 }
	 
	 
	 /**
		 * Questo metodo prende in input una mappa, contenente per ogni utente secondario (chiave) una lista di decisioni binarie calcolate
		 * per ogni SNR su un numero di prove P. Per ogni SNR prende le decisioni degli utenti secondari, li inserisce in un vettore e richiama il metodo
		 * di detection secondo la tecnica OR. Ritorner� la percentuale di Detection da parte Fusion Center
		 * @param inf Estremo inferiore dell'SNR
		 * @param sup Estremo superiore di SNR
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente primario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @return La percentuale di Detection da parte del Fusion Center dopo il metodo di fusione OR**/
	 
	 public  ArrayList<Double> decisionOrFusion(int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision){
		 ArrayList<Double> EnergyDetection = new  ArrayList<Double>();
	  	 
	 for(int i=0;i<35;i++){
		 ArrayList<ArrayList<Integer>> decisions=new ArrayList<ArrayList<Integer>>();
     	for(String SU: userToBinaryDecision.keySet()){
		 ArrayList<Integer>snrDecisionVector= userToBinaryDecision.get(SU).get(i);
     	  decisions.add(snrDecisionVector);}

     	EnergyDetection.add(Detector.orFusionDetection(decisions));
     	}
		return EnergyDetection;
	 }
	 
	 
	 /**
		 * Questo metodo prende in input una mappa, contenente per ogni utente secondario (chiave) una lista di decisioni binarie calcolate
		 * per ogni SNR su un numero di prove P. Per ogni SNR prende le decisioni degli utenti secondari, li inserisce in un vettore e richiama il metodo
		 * di detection secondo la tecnica MAJORITY. Ritorner� la percentuale di Detection da parte Fusion Center
		 * @param inf Estremo inferiore dell'SNR
		 * @param sup Estremo superiore di SNR
		 * @param userToBinaryDecision Mappa che ha come chiave il nome dell'uente primario. Come valore ha una lista di liste: per ogni SNR ha una lista
		 * di lunghezza pari al numero di prove contenente la decisione binaria sulla presenza o assenza dell'utente primario da parte dell'utente secondario
		 * @return La percentuale di Detection da parte del Fusion Center dopo il metodo di fusione MAJORITY**/
	 
	 public  ArrayList<Double> decisionMajorityFusion(int inf,int sup,HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision){
		 ArrayList<Double> EnergyDetection = new  ArrayList<Double>();
	  	 
	 for(int i=0;i<35;i++){
		 ArrayList<ArrayList<Integer>> decisions=new ArrayList<ArrayList<Integer>>();
     	for(String SU: userToBinaryDecision.keySet()){
		 ArrayList<Integer>snrDecisionVector= userToBinaryDecision.get(SU).get(i);
     	  decisions.add(snrDecisionVector);}

     	EnergyDetection.add(Detector.majorityFusionDetection(decisions));
     	}
		return EnergyDetection;
	 }
	 
	}
