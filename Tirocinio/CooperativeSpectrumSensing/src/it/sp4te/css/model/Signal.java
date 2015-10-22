package it.sp4te.css.model;

import java.util.ArrayList;

import it.sp4te.css.signalprocessing.SignalProcessor;

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

	public Signal(int signalLenght) {
		lenght = signalLenght;
		samplesRe = new ArrayList<Double>();
		samplesIm = new ArrayList<Double>();
		for (int i = 0; i < lenght; i++) {
			double v = Math.random();
			if (v < 0.5) {
				samplesRe.add(i, v / Math.sqrt(lenght));
			} else {
				samplesRe.add(i, -v / Math.sqrt(lenght));
			}
			double p = Math.random();
			if (p < 0.5) {
				samplesIm.add(i, p / Math.sqrt(lenght));
			} else {
				samplesIm.add(i, -p / Math.sqrt(lenght));
			}

		}
	}
	
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
