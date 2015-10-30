package it.sp4te.css.detection;

import java.util.ArrayList;
import it.sp4te.css.signalprocessing.SignalProcessor;

/** 
 * <p>Titolo: Detection</p>
 * <p>Descrizione: Classe per il calcolo dei Diversi tipi di Detection </p>
 * @author Pietro Coronas
 **/

public class Detector {

	/**
	 * Metodo proposto per il calcolo della detection che � stato
	 * argomento di tesi triennale. Invece di effettuare il calcolo della soglia e il successivo confronto
	 * sull'energia calcolata a partire dai Momenti del secondo e quarto ordine, questo metodo effettua lo stesso
	 * procedimento utilizzando i parametri Pr invece dei momenti.
	 * 
	 * @param threshold Soglia utilizzata per la Detection
	 * @param pr Parametro Pr calcolato a partire dai momenti
	 * @return La percentuale di Detection calcolata ad uno specifico SNR
	 * @see  SignalProcessor#computePr
	 **/

	public static double proposedMethodDetection(double threshold, ArrayList<Double> pr) {
		double cont = 0;
		for (int i = 0; i < pr.size(); i++) {
			if (pr.get(i) >= threshold) {
				cont++;
			}
		}
		return (double) 100 / (double) (pr.size() / cont);
	}

	/**
	 * Metodo per l'Energy Detector . Prende in input la soglia e un array di
	 * energia. Per ogni valore dell'array che supera la soglia incrementa un
	 * contatore. Successivamente riporta la % di Detection per un dato SNR. 
	 * 
	 * @param threshold Soglia utilizzata per la Detection
	 * @param energy Array di energia. Nello specifico � un vettore di oggetti momento, contenente momenti del 
	 * secondo e quarto ordine
	 * @return Percentuale di Detection per un dato SNR
	 **/

	public static double energyDetection(double threshold, ArrayList<Double> energy) {
		double cont = 0;
		for (int i = 0; i < energy.size(); i++) {
			if (energy.get(i) > threshold) {
				cont++;
			}
		}
		return (double) 100 / (double) (energy.size() / cont);
	}

	/**
	 * Metodo la generazione della detection da parte Del fusion center secondo la tecnica AND.
	 * 
	 * @param decisionsVector Vettori di decisione degli utenti ad uno stesso SNR
	 * @return Percentuale di Detection per un dato SNR
	 **/

	public static  double andFusionDetection(ArrayList<ArrayList<Integer>> decisionsVector){
		int cont=0;
		for(int i=0;i<decisionsVector.get(0).size();i++){
			ArrayList<Integer> decisions=new ArrayList<Integer>();
			for(int j=0;j<decisionsVector.size();j++){		
				decisions.add(decisionsVector.get(j).get(i));

			}
			if(andFusionRule(decisions)==1){cont++;

			}

		}

		if(cont==0){return 0.0;}
		else{
			return (double) 100 / ((double) ((decisionsVector.get(0).size())  / (double) cont));}

	}

	/**
	 * Metodo la generazione della detection da parte Del fusion center secondo la tecnica Or.
	 * 
	 * @param decisionsVector Vettori di decisione degli utenti ad uno stesso SNR
	 * @return Percentuale di Detection per un dato SNR
	 **/


	public static  double orFusionDetection(ArrayList<ArrayList<Integer>> decisionsVector){
		int cont=0;
		for(int i=0;i<decisionsVector.get(0).size();i++){
			ArrayList<Integer> decisions=new ArrayList<Integer>();
			for(int j=0;j<decisionsVector.size();j++){

				decisions.add(decisionsVector.get(j).get(i));

			}
			if(orFusionRule(decisions)==1){cont++;

			}

		}
		if(cont==0){return 0.0;}
		else{
			return (double) 100 / ((double) ((decisionsVector.get(0).size())  / (double) cont));}

	}

	/**
	 * Metodo la generazione della detection da parte Del fusion center secondo la tecnica Majority.
	 * 
	 * @param decisionsVector Vettori di decisione degli utenti ad uno stesso SNR
	 * @return Percentuale di Detection per un dato SNR
	 **/

	public static  double majorityFusionDetection(ArrayList<ArrayList<Integer>> decisionsVector){
		int cont=0;
		for(int i=0;i<decisionsVector.get(0).size();i++){
			ArrayList<Integer> decisions=new ArrayList<Integer>();
			for(int j=0;j<decisionsVector.size();j++){

				decisions.add(decisionsVector.get(j).get(i));

			}
			if(majorityFusionRule(decisions)==1){cont++;
			}

		}
		if(cont==0){return 0.0;}
		else{
			return (double) 100 / ((double) ((decisionsVector.get(0).size())  / (double) cont));}

	}
	/**Metodo di fusione con la tecnica and. Ritorna 1 (l'utente primario � presente) se tutte le decisioni
	 * di tutti i dispositivi sono uguali ad 1, 0 altrimenti
	 * @param decisions Un array contenente le decisioni binarie sulla presenza o assenza dell'utente primario per un dato SNR di
	 * ogni utente secondario. La tenica di fusione AND prevede che sia dichiarata la presenza dell'utente primario se tutti gli utenti secondari 
	 * ne verificano la presenza.
	 * Questo metodo verr� iterato per ogni SNR
	 * @return una decisione binaria risultato della fusione secondo la tecnica AND**/

	public static int andFusionRule(ArrayList<Integer> decisions){
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

	public static int orFusionRule(ArrayList<Integer> decisions){
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

	public static int majorityFusionRule(ArrayList<Integer> decisions){
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

}
