package it.sp4te.css.puea;

import it.sp4te.css.agents.PrimaryUser;
import it.sp4te.css.agents.PrimaryUserEmulator;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.SignalProcessor;
import it.sp4te.css.signalprocessing.Utils;


public class Thresholds {

	public static void main(String[] args) throws Exception {
		int length = 1000; // poi 10000
		int attempts = 1000; //tentativi
		double inf = -30; //da -20db
		double sup = -10; //a 5.5db
		int block=10; //blocchi energy Detector
		double pfa=0.01; //probabilità di falso allarme
		
		PUEA p = new PUEA();
		PrimaryUser PU= new PrimaryUser();
		Signal PUsignal = PU.createAndSend(length);
		
		//Creo l'attaccante e ne genero segnalim a potenza compresa tra 0.8 e 1.2
		PrimaryUserEmulator PUE = new PrimaryUserEmulator();
		Signal PUEsignal_08 = PUE.createAndSend(length,0.8);
		Signal PUEsignal_12 = PUE.createAndSend(length,1.2);
		Signal PUEsignal_09 = PUE.createAndSend(length,0.9);
		Signal PUEsignal_11 = PUE.createAndSend(length,1.1);

		double PUenergy = SignalProcessor.computeEnergy(PUsignal);
		double PUEenergy_08 = SignalProcessor.computeEnergy(PUEsignal_08);
		double PUEenergy_12 = SignalProcessor.computeEnergy(PUEsignal_12);
		double PUEenergy_09 = SignalProcessor.computeEnergy(PUEsignal_09);
		double PUEenergy_11 = SignalProcessor.computeEnergy(PUEsignal_11);
	
//        
//		p.generateThresholdHs2_Hs3(PUsignal, length, PUenergy, attempts, inf, sup, pfa);//che sarebbe come attaccante a energia 1
//		p.generateThresholdHs2_Hs3(PUEsignal_08, length, PUEenergy_08, attempts, inf, sup, pfa);
//		p.generateThresholdHs2_Hs3(PUEsignal_09, length, PUEenergy_09, attempts, inf, sup, pfa);
//		p.generateThresholdHs2_Hs3(PUEsignal_11, length, PUEenergy_11, attempts, inf, sup, pfa);
//		p.generateThresholdHs2_Hs3(PUEsignal_12, length, PUEenergy_12, attempts, inf, sup, pfa);

       Utils.generateThreshold(length, PUenergy, attempts, inf, sup, pfa);


	}

}
