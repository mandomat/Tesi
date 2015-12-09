package it.sp4te.css.signalprocessing;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import it.sp4te.css.agents.MaliciousSecondaryUser;
import it.sp4te.css.agents.TrustedSecondaryUser;
import it.sp4te.css.model.Signal;


/**
 * <p>Titolo: Utils</p>
 * <p>Descrizione: Classe che contiene funzioni utili necessarie alla generazione rapidaW di simulazioni. </p>
 * @author Pietro Coronas**/

public class Utils {

	/**
	 * Metodo per ordinare una mappa in base alla chiave.
	 * In questo caso � utilizzato su una mappa che ha come chiave l'SNR e come valore la % di Detection Relativa.
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
			userToBinaryDecision.put("TrustedSecodaryUser"+i, TrustedSecondaryUsers.get(i).computeBinaryDecisionVector(pfa));	
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
			userToBinaryDecision.put("MaliciousAbsenceSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeAbsenceBinaryDecisionVector());	
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
			userToBinaryDecision.put("MaliciousPresenceSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computePresenceBinaryDecisionVector());	
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
			userToBinaryDecision.put("MaliciousOppositeSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeOppositeBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}	

	/**
	 * Prendendo in input una lista di utenti malevol e la probabilit� di falso allarme, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente la deicisione errata, un numero di volte random, sulla presenza o assenza dell'utente primario
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @param pfa Probabilit� di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente � assente e 0 se � presente) per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteIntelligentOppositeMaliciousBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousIntelligentOppositeSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeIntelligentOppositeBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}
	
	/**
	 * Prendendo in input una lista di utenti malevol e la probabilit� di falso allarme, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente la decisione errata (in questo caso l'assenza del PU), un numero di volte random, sulla presenza o assenza dell'utente primario
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @param pfa Probabilit� di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente � assente e 0 se � presente) per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteIntelligentAbsenceMaliciousBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousIntelligentAbsenceSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeIntelligentAbsenceBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}
	
	/**
	 * Prendendo in input una lista di utenti malevol e la probabilit� di falso allarme, questo metodo riporta
	 * una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente la decisione errata (in questo caso la presenza del PU), un numero di volte random, sulla presenza o assenza dell'utente primario
	 * @param MaliciousSecondaryUsers lista di utenti malevoli
	 * @param pfa Probabilit� di falso allarme
	 * @return una mappa avente come chiave l'identificatore dell'utente secondario e come valore una lista di decisioni binarie
	 * contenente l'opposto del risultato dell'energy detector (1 se l'utente � assente e 0 se � presente) per ogni valore di SNR
	 * @throws Exception 
	 * **/

	public static HashMap<String,ArrayList<ArrayList<Integer>>> genereteIntelligentPresenceMaliciousBinaryDecisionVectors(
			ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers,double pfa) throws Exception {
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecision=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		for(int i=0;i<MaliciousSecondaryUsers.size();i++){
			userToBinaryDecision.put("MaliciousIntelligentPresenceSecondaryUser"+i, MaliciousSecondaryUsers.get(i).computeIntelligentPresenceBinaryDecisionVector(pfa));	
		}
		return userToBinaryDecision;
	}

	/** Metodo per la creazione di un file di testo contenente le soglie per l'energy detector. 
	 * @param length Lunghezza del rumore su cui calcolare le soglie
	 * @param energy Energia a cui generale il rumore
	 * @param attempts Numero di tentativi
	 * @param inf Estremo inferiore di SNR
	 * @param sup Estremo superiore di SNR
	 * @param pfa Probabilit� di falso allarme
	 * @throws Exception
	 */

	public static void generateThreshold(int length,double energy,int attempts,int inf,int sup,double pfa) throws Exception{
		FileWriter w=new FileWriter("thresholds.txt");
		BufferedWriter b=new BufferedWriter(w);
		ArrayList<ArrayList<Double>> VectorNoiseEnergy=SignalProcessor.computeVectorsEnergy(null, length, energy, attempts, inf, sup);	
		int snr=inf;
		b.write(" ");
		b.write(String.valueOf(pfa+" "));     
		b.write("\n");
		for (int i = 0; i < VectorNoiseEnergy.size(); i++) {
			b.write(snr+" "+ SignalProcessor.computeEnergyDetectorThreshold(pfa, VectorNoiseEnergy.get(i)));
			b.write("\n");
			snr++;
		}
		b.close();
	}

	/** Dato un array di decisioni binarie, il metodo riporta 1 se la media dell'array supera 0.5, 0 altrimenti
	 * @param binaryDecisions un array di Decisioni binarie
	 * @return 1 se la media dell'array supera 0.5,0 altrimenti.
	 */

	public static int getMediumDecision(ArrayList<Integer> binaryDecisions){
		if(binaryDecisions.size()==1){return binaryDecisions.get(0);}
		else{
			int tot=0;
			for(int i=0;i<binaryDecisions.size();i++){
				tot=tot+binaryDecisions.get(i);
			}
			double mediumValue=tot/binaryDecisions.size();
			if(mediumValue>=0.5){return 1;}
			else return 0;
		}}
	
	
	
	/**Dato un array di decisioni binarie, il metodo riporta una una lista di decisioni binarie dando peso 0.5 a ciascun utente.
	 * Occorrono due utenti concordi su una decisione per riportare la presenza o l'assenza dell'utente primario
	 
	 * @param binaryDecisions un array di Decisioni binarie
	 * @return una una lista di decisioni binarie dando peso 0.5 a ciascun utente.
	 */
	public static ArrayList<Integer> getGreyListDecision(ArrayList<Integer> binaryDecisions){
		ArrayList<Integer> result= new ArrayList<Integer>();
		int presence=0;
		int absence=0;
		if(binaryDecisions.size()==1){result.add(binaryDecisions.get(0));}
		else{
			for(int i=0;i<binaryDecisions.size();i++){
				if(binaryDecisions.get(i)==0){ absence++;}
				else if (binaryDecisions.get(i)==1){ presence++;}
			}
			
			if(absence%2==0 && presence%2==0){
				for(int i=0;i<(absence/2);i++){
					result.add(0);
				}
				for(int i=0;i<(presence/2);i++){
					result.add(1);
				}
			}
			else if(absence%2!=0 && presence%2==0){
				for(int i=0;i<((absence-1)/2);i++){
					result.add(0);
				}
				for(int i=0;i<(presence/2);i++){
					result.add(1);
				}
			}
			else if(absence%2==0 && presence%2!=0){
				for(int i=0;i<(absence/2);i++){
					result.add(0);
				}
				for(int i=0;i<((presence-1)/2);i++){
					result.add(1);
				}
			}
			
			else if(absence%2!=0 && presence%2!=0){
				for(int i=0;i<((absence-1)/2);i++){
					result.add(0);
				}
				for(int i=0;i<((presence-1)/2);i++){
					result.add(1);
				}
				result.add(0);
			}
			
		}
		
		return result;
	}

	/** Salva l'immagine all'url specificato e lo salva nella destinazione
	 * @param imageUrl Url dell'immagine da salvare
	 * @param destinationFile path della destinazione.
	 * @throws IOException
	 */

	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}


}
