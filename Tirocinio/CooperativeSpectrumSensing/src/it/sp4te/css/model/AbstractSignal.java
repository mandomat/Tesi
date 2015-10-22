package it.sp4te.css.model;

import java.util.ArrayList;

public abstract class AbstractSignal {
	ArrayList<Double> samplesRe;
	ArrayList<Double> samplesIm;
	int lenght;
	
	
	public ArrayList<Double> getSamplesRe() {
		return samplesRe;
	}

	public void setSamplesRe(ArrayList<Double> samplesRe) {
		this.samplesRe = samplesRe;
	}

	public ArrayList<Double> getSamplesIm() {
		return samplesIm;
	}

	public void setSamplesIm(ArrayList<Double> samplesIm) {
		this.samplesIm = samplesIm;
	}
	
	public int getLenght() {
		return lenght;
	}

	public void setLenght(int signalLenght) {
		this.lenght = signalLenght;
	}
	
	/**
	 * Metodo per la Divisione di un Segnale. Dato un segnale (segnale o rumore), un indice di inizio
	 * e uno di fine, il metodo ritorna la porzione di segnale che va dall'indice di inizio all'indice di terminazione.
	 * @param signal Segnale o rumore da cui estrarre una sottoporzione
	 * @param start Indice di inizio della sottoporzione
	 * @param end Indice di terminazione della sottoporzione
	 * @return Sottoporzione del segnale passato come parametro
	 * **/
	

	
}