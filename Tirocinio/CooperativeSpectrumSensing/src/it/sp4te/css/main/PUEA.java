package it.sp4te.css.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import it.sp4te.css.agents.PrimaryUser;
import it.sp4te.css.agents.PrimaryUserEmulator;
import it.sp4te.css.agents.TrustedSecondaryUser;
import it.sp4te.css.graphgenerator.Chart4jGraphGenerator;
import it.sp4te.css.model.Noise;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.MathFunctions;
import it.sp4te.css.signalprocessing.SignalProcessor;

public class PUEA {

	public static void main(String[] args) throws Exception {
		
		int length = 1000; // poi 10000
		int attempts = 1000; //campioni
		double inf = -20; //da -20db
		double sup = 5.5; //a 5.5db
		int block=10; //blocchi energy Detector
		double pfa=0.01; //probabilità di falso allarme
		
		//Creo l'utente primario e ne genero il segnale
		PrimaryUser PU= new PrimaryUser();
		Signal PUsignal = PU.createAndSend(length);
		
		//Creo l'attaccante e ne genero un segnale a potenza 0.8 e un segnale a potenza 1.2
		PrimaryUserEmulator PUE = new PrimaryUserEmulator();
		Signal PUEsignalMin = PUE.createAndSend(length,0.8);
		Signal PUEsignalMax = PUE.createAndSend(length,1.2);
		
		//Creo delle variabili per immagazzinare le somme tra i vari segnali
		Signal PU_noise = new Signal(length,0.0);
		Signal PUE_noise_min =  new Signal(length,0.0);
		Signal PUE_noise_max = new Signal(length,0.0);
		Signal PU_PUEA_noise_min =  new Signal(length,0.0);
		Signal PU_PUEA_noise_max =  new Signal(length,0.0);
		
		
		//Genero il rumore
		Noise noise = new Noise(0,length,1);
		
		//Aggiungo rumore al segnale del PU 
		PU_noise.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUsignal.getSamplesIm()));
		PU_noise.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUsignal.getSamplesRe()));
		
		
		//Aggiungo rumore al segnale del PUE
		PUE_noise_min.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignalMin.getSamplesIm()));
		PUE_noise_min.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignalMin.getSamplesRe()));
		PUE_noise_max.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignalMax.getSamplesIm()));
		PUE_noise_max.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignalMax.getSamplesRe()));
		
		//Sommo PU + PUE + noise
		PU_PUEA_noise_min.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignalMin.getSamplesIm()));
		PU_PUEA_noise_min.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignalMin.getSamplesRe()));
		PU_PUEA_noise_max.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignalMax.getSamplesIm()));
		PU_PUEA_noise_max.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignalMax.getSamplesRe()));
		
		
		//Calcolo l'energia dei segnali
		double PUenergy = SignalProcessor.computeEnergy(PUsignal);
		double PUEenergyMin = SignalProcessor.computeEnergy(PUEsignalMin);
		double PUEenergyMax = SignalProcessor.computeEnergy(PUEsignalMax);
		double NoiseEnergy = SignalProcessor.computeEnergy(noise);
		
		double PU_noise_energy = SignalProcessor.computeEnergy(PU_noise);
		double PUE_noise_energy_min = SignalProcessor.computeEnergy(PUE_noise_min);
		double PUE_noise_energy_max = SignalProcessor.computeEnergy(PUE_noise_max);
		double PU_PUE_noise_energy_min = SignalProcessor.computeEnergy(PU_PUEA_noise_min);
		double PU_PUE_noise_energy_max = SignalProcessor.computeEnergy(PU_PUEA_noise_max);
		
		System.out.println("PU energy: "+ PUenergy);
		System.out.println("PUE energy a 0.8: "+ PUEenergyMin);
		System.out.println("PUE energy a 1.2: "+ PUEenergyMax+"\n\n\n");

		System.out.println("-------- Hs0 --------");
		System.out.println("Noise energy: "+ NoiseEnergy+"\n");
		System.out.println("-------- Hs1 --------");
		System.out.println("PU + noise energy: "+PU_noise_energy+"\n");
		System.out.println("-------- Hs2 --------");
		System.out.println("PUE a 0.8 + noise energy: "+PUE_noise_energy_min);
		System.out.println("PUE a 1.2 + noise energy: "+PUE_noise_energy_max+"\n");
		System.out.println("-------- Hs3 --------");
		System.out.println("PU + PUE a 0.8 + noise energy: "+PU_PUE_noise_energy_min);
		System.out.println("PU + PUE a 1.2 + noise energy: "+PU_PUE_noise_energy_max);



		
		
		
		
		

	}

}
