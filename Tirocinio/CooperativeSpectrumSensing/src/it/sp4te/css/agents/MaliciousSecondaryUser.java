package it.sp4te.css.agents;

import java.util.ArrayList;

import it.sp4te.css.signalprocessing.SignalProcessor;

public class MaliciousSecondaryUser extends SecondaryUser {

	
	/**Questo metodo rappresenta il comportamento malevolo di un utente secondario, riportando un vettore di decisioni
	 * binarie che afferma l'assenza dell'utente primario per ogni SNR in ogni prova
	 * @return Un vettore di decisioni
	 * binarie che afferma l'assenza dell'utente primario per ogni SNR in ogni prova**/
	
	public ArrayList<ArrayList<Integer>> computeAbsenceBinaryDecision() throws Exception{
		ArrayList<ArrayList<Integer>> decisions= SignalProcessor.makeAbsenceDecisionVector(attempts, inf, sup);
		return decisions;	
		}
	
	/**Questo metodo rappresenta il comportamento malevolo di un utente secondario, riportando un vettore di decisioni
	 * binarie che afferma la presenza dell'utente primario per ogni SNR in ogni prova
	 * * @return Un vettore di decisioni
	 * binarie che afferma la presenza dell'utente primario per ogni SNR in ogni prova**/
	public ArrayList<ArrayList<Integer>> computePresenceBinaryDecision() throws Exception{
		ArrayList<ArrayList<Integer>> decisions= SignalProcessor.makePresenceDecisionVector(attempts, inf, sup);
		return decisions;	
		}
	
	/**Questo metodo rappresenta il comportamento malevolo di un utente secondario, riportando un vettore di decisioni
	 * binarie che afferma l'assenza dell'utente primario (se � presente) o la presenza dell'utente primario (se � assente)
	 * per ogni SNR in ogni prova
	 * @param pfa Probabilit� di falso allarme
	 * @return Un vettore di decisioni
	 * binarie che afferma l'assenza dell'utente primario (se � presente) o la presenza dell'utente primario (se � assente)
	 * per ogni SNR in ogni prova**/
	
	public ArrayList<ArrayList<Integer>> computeOppositeBinaryDecision(double pfa) throws Exception{
		ArrayList<ArrayList<Integer>> decisions= SignalProcessor.makeOppositeDecisionVector(s, length, energy, attempts, inf, sup,pfa);
		return decisions;	
		}
}