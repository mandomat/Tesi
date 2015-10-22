package it.sp4te.css.agents;

import java.util.*;

import it.sp4te.css.detection.Detector;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.Moment;
import it.sp4te.css.signalprocessing.SignalProcessor;

/**
 * <p>Titolo: SecondaryUser</p>
 * <p>Descrizione della classe: Questa classe si occupa di effettuare tutte le operazione relative allo
 * Spectrum sensing: Calcolo dei momenti del secondo e quarto ordine nelle due
 * ipotesi,calcolo dell'energia dei momenti,calcolo dei vettori PR,spectrum
 * sensing con Energy Detector,spectrum sensing con Detector proposto</p>
 * @author Pietro Coronas
 **/

public class SecondaryUser {
	Signal s;
	int attempts,length,inf,sup,block;
	double energy;
	ArrayList<Moment> MomentsSignal;
	ArrayList<Moment> MomentsNoise;


	/**
	 * Questo metodo inizializza i valori che saranno usati nei diversi tipi di
	 * detection.
	 * 
	 * @param s Segnale su cui effettuare la Detection
	 * @param length lunghezza del segnale
	 * @param energy Energia del segnale
	 * @param attempts Numero di prove su cui viene effettuata la simulazione
	 * @param inf Estremo inferiore di SNR su cui � stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui � stata effettuata la simulazione
	 * @param block Blocchi in cui dividere il segnale per l'energy Detector
	 * @see SignalProcessor#computeMoment
	 * @see SignalProcessor#computeEnergy
	 * @see SignalProcessor#computePr
	 * @see SignalProcessor#computeMediumEnergy
	 * 
	 **/


	public void listenChannel(Signal s, int length, double energy, int attempts, int inf, int sup,int block){
		//Oggetto momento nell'ipotesi Segnale pi� rumore
		this.MomentsSignal = SignalProcessor.computeMoment(s, length, energy, attempts, inf, sup);
		//Oggetto momento nell'ipotesi di solo Rumore
		this.MomentsNoise =SignalProcessor.computeMoment(null, length, energy, attempts, inf, sup);
		//Setto i parametri
		this.s=s;
		this.length=length;
		this.energy=energy;
		this.attempts=attempts;
		this.inf=inf;
		this.sup=sup;
		this.block=block;

	}
	/**
	 * Metodo per lo Spectrum Senging dell'energy Detector. Il confronto viene fatto tra la soglia e un vettore di energie ottenuto
	 * dividendo il segnale in M blocchi da N campioni e facendo la media delle energie.
	 * 
	 * @param pfa Probabilit� di falso allarme
	 * @return Array con le percentuali di detection ordinate per SNR
	 * @throws Exception Pfa deve essere scelto in modo che 1-2pfa sia compreso tra -1 e 1
	 * @see Detector#energyDetection
	 * @see #orderSignal
	 * @see  SignalProcessor#computeEnergyDetectorThreshold
	 **/

	public ArrayList<Double> spectrumSensingBlockEnergyDetector(Double pfa) throws Exception{
		HashMap<Double, Double> EnergyDetection = new HashMap<Double, Double>();
		ArrayList<ArrayList<Double>> MediumSignalEnergy=SignalProcessor.computeMediumEnergy(s, length, energy, attempts, inf, sup, block);
		ArrayList<ArrayList<Double>> MediumNoiseEnergy=SignalProcessor.computeMediumEnergy(null, length, energy, attempts, inf, sup, block);
		
		for (int i = 0; i < MediumSignalEnergy.size(); i++) {
			Double ED = Detector.energyDetection(
					SignalProcessor.computeEnergyDetectorThreshold(pfa, MediumNoiseEnergy.get(i)), MediumSignalEnergy.get(i));
			EnergyDetection.put(this.MomentsSignal.get(i).getSnr(), ED);
		}

		return orderSignal(EnergyDetection);
	}
	
	
	
	/**
	 * Metodo per lo Spectrum Senging dell'energy Detector effettuato senza dividere il segnale in blocchi,
	 * calcolando l'energia a partire dai momenti del secondo e quarto ordine. 
	 * 
	 * @param pfa Probabilit� di falso allarme
	 * @return Array con le percentuali di detection ordinate per SNR
	 * @throws Exception  Pfa deve essere scelto in modo che 1-2pfa sia compreso tra -1 e 1
	 * @see Detector#energyDetection
	 * @see #orderSignal
	 * @see  SignalProcessor#ComputeEnergyDetectorThreshold
	 **/
	
	public ArrayList<Double> spectrumSensingMomentEnergyDetector(double pfa) throws Exception {
		HashMap<Double, Double> EnergyDetection = new HashMap<Double, Double>();
		ArrayList<ArrayList<Double>> MomentSignalEnergy = SignalProcessor.computeMomentEnergy(MomentsSignal);
		ArrayList<ArrayList<Double>> MomentNoiseEnergy = SignalProcessor.computeMomentEnergy(MomentsNoise);
		
		for (int i = 0; i < MomentSignalEnergy.size(); i++) {
			Double ED = Detector.energyDetection(
					SignalProcessor.computeEnergyDetectorThreshold(pfa, MomentNoiseEnergy.get(i)), MomentSignalEnergy.get(i));
			EnergyDetection.put(this.MomentsSignal.get(i).getSnr(), ED);
		}

		return orderSignal(EnergyDetection);
	}

	/**
	 * Metodo per lo Spectrum sensing del metodo proposto. Il procedimento � simile a quello dell'energy Detector ma con
	 * la differenza che utilizza gli oggetti PR al posto dei momenti del secondo e quarto ordine.
	 * 
	 * @param pfa Probabilit� di falso allarme
	 * @return Array con le percentuali di detection ordinate per SNR
	 * @throws Exception Pfa deve essere scelto in modo che 1-2pfa sia compreso tra -1 e 1
	 * @see Detector#proposedMethodDetection
	 * @see #orderSignal
	 * @see SignalProcessor#computeProposedThreshold
	 * 
	 **/

	public ArrayList<Double> spectrumSensingProposedDetector(Double pfa) throws Exception {
		HashMap<Double, Double> ProposedDetection = new HashMap<Double, Double>();
		ArrayList<ArrayList<Double>> PrSignal = SignalProcessor.computePr(MomentsSignal);
		ArrayList<ArrayList<Double>> PrNoise = SignalProcessor.computePr(MomentsNoise);
		
		for (int i = 0; i < PrSignal.size(); i++) {
			Double PD = Detector.proposedMethodDetection(SignalProcessor.computeProposedThreshold(pfa, PrNoise.get(i)),
					PrSignal.get(i));
			ProposedDetection.put(this.MomentsSignal.get(i).getSnr(), PD);
		}
		return orderSignal(ProposedDetection);
	}

	
	/**Questo metodo rappresenta l'energy Detector tradizionale. Effetua il calcolo dell'energia del solo rumore su cui
	 * calcola la soglia. Successivamente calcola l'energia del segnale+rumore ed effettua il confronto con la soglia
	 * calcolata.**/
	public ArrayList<Double> spectrumSensingTraditionalEnergyDetector(double pfa) throws Exception{
		HashMap<Double, Double> EnergyDetection = new HashMap<Double, Double>();
		ArrayList<ArrayList<Double>> VectorSignalEnergy=SignalProcessor.computeVectorEnergy(s, length, energy, attempts, inf, sup);
		ArrayList<ArrayList<Double>> VectorNoiseEnergy=SignalProcessor.computeVectorEnergy(null, length, energy, attempts, inf, sup);	
		
		for (int i = 0; i < VectorSignalEnergy.size(); i++) {
			Double ED = Detector.energyDetection(
					SignalProcessor.computeEnergyDetectorThreshold(pfa, VectorNoiseEnergy.get(i)), VectorSignalEnergy.get(i));
			EnergyDetection.put(this.MomentsSignal.get(i).getSnr(), ED);
		}

		return orderSignal(EnergyDetection);
	}
	
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

}