package it.sp4te.css.signalprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import it.sp4te.css.agents.MaliciousSecondaryUser;
import it.sp4te.css.agents.TrustedSecondaryUser;
import it.sp4te.css.model.Signal;

public class Utils {

	
	/**
	 * Metodo per ordinare una mappa in base alla chiave.
	 * In questo caso � utilizzato su una mappa che ha come chiave l'SNR e come valore la % di Detection Relativa.
	 * 
	 * @param signalmapToOrder mappa con chiave SNR e valore la relativa % di detection 
	 * @return la mappa ordinata in base all'SNR
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
	
	
	/**
	 * Metodo per generare una lista di utenti secondari fidati
	 * @param number Numero di utenti secondari fidati da generare
	 * @param s Segnale su cui effettuare la Detection
	 * @param SignalLength lunghezza del segnale
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui viene effettuata la simulazione
	 * @param inf Estremo inferiore di SNR su cui � stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui � stata effettuata la simulazione
	 * @param block Blocchi in cui dividere il segnale per l'energy Detector
	 * @return una lista di utenti secondari fidati
	 * **/
	public static ArrayList<TrustedSecondaryUser> createTrustedSecondaryUsers(int number,Signal s,
			int SignalLength, double energy, int attempts, int inf, int sup,int block){
		
		ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers = new ArrayList<TrustedSecondaryUser>();
		for(int i=0;i<number;i++){
			TrustedSecondaryUser TSU=new TrustedSecondaryUser();
			TSU.listenChannel(s, SignalLength, energy, attempts, inf, sup, block);
			TrustedSecondaryUsers.add(TSU);
		}
		
		return TrustedSecondaryUsers;
		
	}
	
	/**
	 * Metodo per generare una lista di utenti secondari malevoli
	 * @param number Numero di utenti secondari fidati da generare
	 * @param s Segnale su cui effettuare la Detection
	 * @param SignalLength lunghezza del segnale
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui viene effettuata la simulazione
	 * @param inf Estremo inferiore di SNR su cui � stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui � stata effettuata la simulazione
	 * @param block Blocchi in cui dividere il segnale per l'energy Detector
	 * @return una lista di utenti secondari malevoli
	 * **/
	
	public static ArrayList<MaliciousSecondaryUser> createMaliciousSecondaryUsers(int number,Signal s,
			int SignalLength, double energy, int attempts, int inf, int sup,int block){
		
		ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers = new ArrayList<MaliciousSecondaryUser>();
		for(int i=0;i<number;i++){
			MaliciousSecondaryUser MSU=new MaliciousSecondaryUser();
			MSU.listenChannel(s, SignalLength, energy, attempts, inf, sup, block);
			MaliciousSecondaryUsers.add(MSU);
		}
		
		return MaliciousSecondaryUsers;
		
	}
	
	/**
	 * Prendendo in input una lista di utenti secondari fidati e la probabilit� di falso allarma, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * sulla presenza o assenza dell'utente primario per ogni valore di SNR
	 * @param TrustedSecondaryUsers Lista di utenti fidati
	 * @param pfa Probabilit� di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * sulla presenza o assenza dell'utente primario per ogni valore di SNR
	 * @throws Exception 
	 * **/
	
	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteBinaryDecisionVectors(
			ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<TrustedSecondaryUsers.size();i++){
			userToBinaryDecision.put(TrustedSecondaryUsers.get(i).toString(), TrustedSecondaryUsers.get(i).computeBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}
	
	/**
	 * Prendendo in input una lista di utenti malevoli, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente solo l'assenza dell'utente primario per ogni valore di SNR
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente solo l'assenza dell'utente primario per ogni valore di SNR
	 * @throws Exception 
	 * **/
	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteAbsenceBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put(MaliciousSecondaryUsers.get(i).toString(), MaliciousSecondaryUsers.get(i).computeAbsenceBinaryDecisionVector());	
		}
		return userToBinaryDecision;
	}
	
	/**
	 * Prendendo in input una lista di utenti malevoli, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente solo la presenza dell'utente primario per ogni valore di SNR
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente solo la presenza dell'utente primario per ogni valore di SNR
	 * @throws Exception 
	 * **/
	
	public static HashMap<String,ArrayList<ArrayList<Integer>>> generetePresenceBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put(MaliciousSecondaryUsers.get(i).toString(), MaliciousSecondaryUsers.get(i).computePresenceBinaryDecisionVector());	
		}
		return userToBinaryDecision;
	}
	
	/**
	 * Prendendo in input una lista di utenti malevol e la probabilit� di falso allarme, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente � assente e 0 se � presente) per ogni valore di SNR
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @param pfa Probabilit� di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente � assente e 0 se � presente) per ogni valore di SNR
	 * @throws Exception 
	 * **/
	
	
	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteOppositeBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put(MaliciousSecondaryUsers.get(i).toString(), MaliciousSecondaryUsers.get(i).computeOppositeBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}
	
}