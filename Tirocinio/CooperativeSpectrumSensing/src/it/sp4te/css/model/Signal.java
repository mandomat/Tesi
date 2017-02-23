package it.sp4te.css.model;

import java.util.ArrayList;
import java.util.Random;

/** <p>Titolo: Signal</p>
 * <p>Descrizione: Classe per la generazione del segnale</p>
 * @author Pietro Coronas **/

public class Signal extends AbstractSignal{

	/**
	 * Costruttore del segnale modulato QPSK e con potenza unitaria. L'oggetto segnale contiene 2 array,
	 * rispettivamente relativi a parte reale e parte immaginaria.
	 * 
	 * @param signalLenght Lunghezza del segnale
	 **/

	public Signal(int signalLength) {
		length = signalLength;
		samplesRe = new ArrayList<Double>();
		samplesIm = new ArrayList<Double>();
		for (int i = 0; i < length; i++) {
			double v = Math.random();
			if (v < 0.5) {
				samplesRe.add(i, 1 / Math.sqrt(2));
			} else {
				samplesRe.add(i, -1 / Math.sqrt(2));
			}
			double p = Math.random();
			if (p < 0.5) {
				samplesIm.add(i, 1 / Math.sqrt(2));
			} else {
				samplesIm.add(i, -1 / Math.sqrt(2));
			}

		}
	}
	
	public Signal(int signalLength, double power) {
		double x;
		 x=2;
		length = signalLength;
		samplesRe = new ArrayList<Double>();
		samplesIm = new ArrayList<Double>();
		if(power == 0.8)
			x=2.5;
		if(power == 1.2)
			x=1.6665;
		if(power == 1.1)
			x=1.8181;
		if(power == 0.9)
			x=2.2222;
		if(power != 0.0){
		for (int i = 0; i < length; i++) {
			double v = Math.random();
			if (v < 0.5) {
				samplesRe.add(i, 1 / Math.sqrt(x));
			} else {
				samplesRe.add(i, -1 / Math.sqrt(x));
			}
			double p = Math.random();
			if (p < 0.5) {
				samplesIm.add(i, 1 / Math.sqrt(x));
			} else {
				samplesIm.add(i, -1 / Math.sqrt(x));
			}

		}
		}else
			for (int i = 0; i < length; i++) {
					samplesRe.add(0.0);
					samplesIm.add(0.0);
				}
	}
	
	public  Signal(int signalLength, int y){
		Random r = new Random();
		double x = 0.8 + (1.2 - 0.8) * r.nextDouble(); 
		length = signalLength;
		samplesRe = new ArrayList<Double>();
		samplesIm = new ArrayList<Double>();
		for (int i = 0; i < length; i++) {
			double v = Math.random();
			if (v < 0.5) {
				samplesRe.add(i, 1 / Math.sqrt(x));
			} else {
				samplesRe.add(i, -1 / Math.sqrt(x));
			}
			double p = Math.random();
			if (p < 0.5) {
				samplesIm.add(i, 1 / Math.sqrt(x));
			} else {
				samplesIm.add(i, -1 / Math.sqrt(x));
			}

		}
	}
	

	/** Metodo per dividere il segnale in una porzione.
	 * @param start Inizio della porzione di interesse
	 * @param end Fine della porzione di interesse
	 * @return Una porzione di segnale ottenuta specificando inizio e fine della parte di interesse
	 */
	
	public Signal splitSignal(int start,int end){
		Signal splittedSignal= new Signal(end-start);
		ArrayList<Double> samplesRea=new ArrayList<Double>();
		ArrayList<Double> samplesImm=new ArrayList<Double>();
		for(int i=start;i<end;i++){
			samplesRea.add(this.getSamplesRe().get(i));
			samplesImm.add(this.getSamplesIm().get(i));

		}
		splittedSignal.setSamplesRe(samplesRea);
		splittedSignal.setSamplesIm(samplesImm);
		return splittedSignal;
	}

}
