package it.sp4te.css.signalprocessing;

import java.util.ArrayList;

import it.sp4te.css.model.Noise;
import it.sp4te.css.model.Signal;

/** <p>Titolo:Moment</p>
 * <p>Descrizione: Classe relativa ai momenti del secondo e quarto ordine </p>
 * @author Pietro Coronas
 * **/

public class Moment {

	public double snr;
	public ArrayList<Double> secondOrder;
	public ArrayList<Double> fourthOrder;
	public ArrayList<Double> Energy;

	/**
	 * Costruttore dell'oggetto Momento. Ogni oggetto momento � composto da 4 attributi: I momenti del secondo ordine,
	 * i Momenti del quarto ordine, l'energia del segnale e l'SNR su cui viene effettuato il calcolo dei momenti
	 * 
	 * @param s Il segnale
	 * @param attempt_s Numero di prove su cui effettuare la simulazione
	 * @param energy Energia del segnale
	 * @param snr L'SNR di riferimento
	 * @param length Lunghezza del segnale
	 **/

	public Moment(Signal s, int attempt_s, double energy, double snr, int length) {
		// Inizializza i parametri
		ArrayList<Double> samplesRe;
		ArrayList<Double> samplesIm;
		int attempts = attempt_s;
		this.secondOrder = new ArrayList<Double>();
		this.fourthOrder = new ArrayList<Double>();
		this.Energy = new ArrayList<Double>();
		if (s != null) {

			samplesRe = s.getSamplesRe();
			samplesIm = s.getSamplesIm();
		} else {

			samplesRe = null;
			samplesIm = null;

		}

		// Generazione dell'oggetto momento. 
		this.snr = snr;

		for (int i = 0; i < attempts; i++) {
			double j = 0.0;
			double h = 0.0;

			Noise noise = new Noise(snr, length, energy);

			Signal signal = new Signal(noise.getLenght());

			signal.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), samplesRe));
			signal.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), samplesIm));

			this.Energy.add(i, SignalProcessor.computeEnergy(signal));

			for (int k = 1; k < noise.getLenght() - 1; k++) {

				j = (j + Math.pow(signal.getSamplesRe().get(k), 2) + Math.pow(signal.getSamplesIm().get(k), 2));
				h = (h + Math.pow(signal.getSamplesRe().get(k), 4) + Math.pow(signal.getSamplesIm().get(k), 4));
			}

			this.secondOrder.add(i, j * (1 / (double) signal.getLenght()));
			this.fourthOrder.add(i, h * (1 / (double) signal.getLenght()));
		}

	}

	
	public double getSnr() {
		return snr;
	}

	public void setSnr(double snr) {
		this.snr = snr;
	}


	public ArrayList<Double> getSecondOrder() {
		return secondOrder;
	}

	public void setMean(ArrayList<Double> secondOrder) {
		this.secondOrder = secondOrder;
	}

	public ArrayList<Double> getFourthOrder() {
		return fourthOrder;
	}

	public void setFourthOrder(ArrayList<Double> fourthOrder) {
		this.fourthOrder = fourthOrder;
	}

	public ArrayList<Double> getEnergy() {
		return Energy;
	}

	public void setEnergy(ArrayList<Double> energy) {
		Energy = energy;
	}

}

