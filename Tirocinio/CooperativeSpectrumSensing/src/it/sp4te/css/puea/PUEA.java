package it.sp4te.css.puea;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
	
	public static void writeEnergyVectorOnFile(ArrayList<Double> vector,String fileName) throws Exception{
		FileWriter w=new FileWriter(""+fileName+".txt");
		BufferedWriter b=new BufferedWriter(w);
		for (int i = 0; i < vector.size(); i++) {
			b.write(Double.toString(vector.get(i)));
			b.write(System.lineSeparator());
		}
		b.close();
	}
	
	public static void generateThresholdHs2_Hs3(Signal s,int length,double energy,int attempts,double inf,double sup,double pfa) throws Exception{
		FileWriter w=new FileWriter("thresholdsHs2_Hs3"+pfa+"_"+energy+".txt");
		BufferedWriter b=new BufferedWriter(w);
		ArrayList<ArrayList<Double>> VectorNoiseAndSignalEnergy=SignalProcessor.computeVectorsEnergy(s, length, energy, attempts, inf, sup);	
		double snr=inf;
		for (int i = 0; i < VectorNoiseAndSignalEnergy.size(); i++) {
			b.write(snr+" "+ SignalProcessor.computeEnergyDetectorThreshold(pfa, VectorNoiseAndSignalEnergy.get(i)));
			b.write(System.lineSeparator());
			snr+=0.5;
		}
		b.close();
	}
	
	public static void generateThresholdHs1_Hs2(Signal s,int length,double energy,int attempts,double inf,double sup,double pfa) throws Exception{
		FileWriter w=new FileWriter("thresholdsHs1_Hs2"+pfa+"_"+energy+".txt");
		BufferedWriter b=new BufferedWriter(w);
		ArrayList<ArrayList<Double>> VectorNoiseAndSignalEnergy=SignalProcessor.computeVectorsEnergy(s, length, energy, attempts, inf, sup);	
		double snr=inf;
		for (int i = 0; i < VectorNoiseAndSignalEnergy.size(); i++) {
			b.write(snr+" "+ SignalProcessor.computeEnergyDetectorThreshold(pfa, VectorNoiseAndSignalEnergy.get(i)));
			b.write(System.lineSeparator());
			snr+=0.5;
		}
		b.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		int length = 10000; // poi 10000
		int attempts = 1000; //tentativi
		double inf = -20; //da -20db
		double sup = 5.5; //a 5.5db
		int block=10; //blocchi energy Detector
		double pfa=0.01; //probabilità di falso allarme
		
		//Creo l'utente primario e ne genero il segnale
		PrimaryUser PU= new PrimaryUser();
		Signal PUsignal = PU.createAndSend(length);
		
		//Creo l'attaccante e ne genero segnalim a potenza compresa tra 0.8 e 1.2
		PrimaryUserEmulator PUE = new PrimaryUserEmulator();
		Signal PUEsignal_08 = PUE.createAndSend(length,0.8);
		Signal PUEsignal_12 = PUE.createAndSend(length,1.2);
		Signal PUEsignal_09 = PUE.createAndSend(length,0.9);
		Signal PUEsignal_11 = PUE.createAndSend(length,1.1);

		
		//Creo delle variabili per immagazzinare le somme tra i vari segnali
		Signal PU_noise = new Signal(length,0.0);
		Signal PUE_noise_08 =  new Signal(length,0.0);
		Signal PUE_noise_12 = new Signal(length,0.0);
		Signal PUE_noise_09 =  new Signal(length,0.0);
		Signal PUE_noise_11 = new Signal(length,0.0);
		Signal PU_PUE_noise_08 =  new Signal(length,0.0);
		Signal PU_PUE_noise_12 =  new Signal(length,0.0);
		Signal PU_PUE_noise_09 =  new Signal(length,0.0);
		Signal PU_PUE_noise_11 =  new Signal(length,0.0);
		Signal PU_PUE_08 =  new Signal(length,0.0); //Questi sono senza rumore perchè quando genero il vettore di energia l'algoritmo aggiunge il rumore
		Signal PU_PUE_12 =  new Signal(length,0.0);
		Signal PU_PUE_09 =  new Signal(length,0.0);
		Signal PU_PUE_11 =  new Signal(length,0.0);


	

		
		//Genero il rumore
		Noise noise = new Noise(0,length,1);
		
		//Aggiungo rumore al segnale del PU 
		PU_noise.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUsignal.getSamplesIm()));
		PU_noise.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUsignal.getSamplesRe()));
		
		
		//Aggiungo rumore al segnale del PUE
		PUE_noise_08.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignal_08.getSamplesIm()));
		PUE_noise_08.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignal_08.getSamplesRe()));
		
		PUE_noise_12.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignal_12.getSamplesIm()));
		PUE_noise_12.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignal_12.getSamplesRe()));
		
		PUE_noise_09.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignal_09.getSamplesIm()));
		PUE_noise_09.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignal_09.getSamplesRe()));
		
		PUE_noise_11.setSamplesIm(MathFunctions.SumVector(noise.getSamplesIm(), PUEsignal_11.getSamplesIm()));
		PUE_noise_11.setSamplesRe(MathFunctions.SumVector(noise.getSamplesRe(), PUEsignal_11.getSamplesRe()));
		
		//Sommo PU + PUE + noise
		PU_PUE_noise_08.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_08.getSamplesIm()));
		PU_PUE_noise_08.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_08.getSamplesRe()));
		
		PU_PUE_noise_12.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_12.getSamplesIm()));
		PU_PUE_noise_12.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_12.getSamplesRe()));
		
		PU_PUE_noise_09.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_09.getSamplesIm()));
		PU_PUE_noise_09.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_09.getSamplesRe()));
		
		PU_PUE_noise_11.setSamplesIm(MathFunctions.SumVector(PU_noise.getSamplesIm(), PUEsignal_11.getSamplesIm()));
		PU_PUE_noise_11.setSamplesRe(MathFunctions.SumVector(PU_noise.getSamplesRe(), PUEsignal_11.getSamplesRe()));
		
		//PU + PUE senza rumore
		PU_PUE_08.setSamplesIm(MathFunctions.SumVector(PUsignal.getSamplesIm(), PUEsignal_08.getSamplesIm()));
		PU_PUE_08.setSamplesRe(MathFunctions.SumVector(PUsignal.getSamplesRe(), PUEsignal_08.getSamplesRe()));
		
		PU_PUE_12.setSamplesIm(MathFunctions.SumVector(PUsignal.getSamplesIm(), PUEsignal_12.getSamplesIm()));
		PU_PUE_12.setSamplesRe(MathFunctions.SumVector(PUsignal.getSamplesRe(), PUEsignal_12.getSamplesRe()));
		
		PU_PUE_09.setSamplesIm(MathFunctions.SumVector(PUsignal.getSamplesIm(), PUEsignal_09.getSamplesIm()));
		PU_PUE_09.setSamplesRe(MathFunctions.SumVector(PUsignal.getSamplesRe(), PUEsignal_09.getSamplesRe()));
		
		PU_PUE_11.setSamplesIm(MathFunctions.SumVector(PUsignal.getSamplesIm(), PUEsignal_11.getSamplesIm()));
		PU_PUE_11.setSamplesRe(MathFunctions.SumVector(PUsignal.getSamplesRe(), PUEsignal_11.getSamplesRe()));


		
		//Calcolo l'energia dei segnali nelle 4 ipotesi (prima senza rumore poi col rumore
		double PUenergy = SignalProcessor.computeEnergy(PUsignal);
		double PUEenergy_08 = SignalProcessor.computeEnergy(PUEsignal_08);
		double PUEenergy_12 = SignalProcessor.computeEnergy(PUEsignal_12);
		double PUEenergy_09 = SignalProcessor.computeEnergy(PUEsignal_09);
		double PUEenergy_11 = SignalProcessor.computeEnergy(PUEsignal_11);
		double NoiseEnergy = SignalProcessor.computeEnergy(noise);
		double PU_PUEA_energy_08 = SignalProcessor.computeEnergy(PU_PUE_08);
        double PU_PUEA_energy_12 = SignalProcessor.computeEnergy(PU_PUE_12);
        double PU_PUEA_energy_09 = SignalProcessor.computeEnergy(PU_PUE_09);
        double PU_PUEA_energy_11 = SignalProcessor.computeEnergy(PU_PUE_11);
		
		
		double PU_noise_energy = SignalProcessor.computeEnergy(PU_noise);
		double PUE_noise_energy_08 = SignalProcessor.computeEnergy(PUE_noise_08);
		double PUE_noise_energy_12 = SignalProcessor.computeEnergy(PUE_noise_12);
		double PUE_noise_energy_09 = SignalProcessor.computeEnergy(PUE_noise_09);
		double PUE_noise_energy_11 = SignalProcessor.computeEnergy(PUE_noise_11);
		double PU_PUE_noise_energy_08 = SignalProcessor.computeEnergy(PU_PUE_noise_08);
		double PU_PUE_noise_energy_12 = SignalProcessor.computeEnergy(PU_PUE_noise_12);
		double PU_PUE_noise_energy_09 = SignalProcessor.computeEnergy(PU_PUE_noise_09);
		double PU_PUE_noise_energy_11 = SignalProcessor.computeEnergy(PU_PUE_noise_11);
		
		
		
		//Vettori di energia per dei vari segnali nelle 4 ipotesi 
		ArrayList<Double> noise_energy_vector = SignalProcessor.computeVectorEnergy(null, noise.getLenght(), NoiseEnergy, attempts, -5);
		ArrayList<Double> PU_noise_energy_vector = SignalProcessor.computeVectorEnergy(PUsignal, PUsignal.getLenght(), PUenergy, attempts, -5);
		ArrayList<Double> PUE_noise_energy_vector_08 = SignalProcessor.computeVectorEnergy(PUEsignal_08, PUEsignal_08.getLenght(), PUEenergy_08, attempts, -5);
		ArrayList<Double> PUE_noise_energy_vector_12 = SignalProcessor.computeVectorEnergy(PUEsignal_12, PUEsignal_12.getLenght(), PUEenergy_12, attempts, -5);
		ArrayList<Double> PUE_noise_energy_vector_09 = SignalProcessor.computeVectorEnergy(PUEsignal_09, PUEsignal_09.getLenght(), PUEenergy_09, attempts, -5);
		ArrayList<Double> PUE_noise_energy_vector_11 = SignalProcessor.computeVectorEnergy(PUEsignal_11, PUEsignal_11.getLenght(), PUEenergy_11, attempts, -5);
		ArrayList<Double> PU_PUE_noise_energy_vector_08 = SignalProcessor.computeVectorEnergy(PU_PUE_08, PU_PUE_08.getLenght(), PU_PUEA_energy_08, attempts, -5);
		ArrayList<Double> PU_PUE_noise_energy_vector_12 = SignalProcessor.computeVectorEnergy(PU_PUE_12, PU_PUE_12.getLenght(), PU_PUEA_energy_12, attempts, -5);
		ArrayList<Double> PU_PUE_noise_energy_vector_09 = SignalProcessor.computeVectorEnergy(PU_PUE_09, PU_PUE_09.getLenght(), PU_PUEA_energy_09, attempts, -5);
		ArrayList<Double> PU_PUE_noise_energy_vector_11 = SignalProcessor.computeVectorEnergy(PU_PUE_11, PU_PUE_11.getLenght(), PU_PUEA_energy_11, attempts, -5);
		
		double threshold_HS2 = SignalProcessor.computeEnergyDetectorThreshold(0.01, PU_noise_energy_vector);
		/////qui
		generateThresholdHs2_Hs3(PUsignal, length, PUenergy, attempts, inf, sup, pfa);//che sarebbe come attaccante a energia 1
		generateThresholdHs2_Hs3(PUEsignal_08, length, PUEenergy_08, attempts, inf, sup, pfa);
		generateThresholdHs2_Hs3(PUEsignal_09, length, PUEenergy_09, attempts, inf, sup, pfa);
		generateThresholdHs2_Hs3(PUEsignal_11, length, PUEenergy_11, attempts, inf, sup, pfa);
		generateThresholdHs2_Hs3(PUEsignal_12, length, PUEenergy_12, attempts, inf, sup, pfa);

		
		System.out.println("Soglia a 0:"+threshold_HS2);
		System.out.println("PU energy = "+ PUenergy);
		System.out.println("PUE energy a 0.8 = "+ PUEenergy_08);
		System.out.println("PUE energy a 0.9 = "+ PUEenergy_09);
		System.out.println("PUE energy a 1.1 = "+ PUEenergy_11);
		System.out.println("PUE energy a 1.2 = "+ PUEenergy_12);
		System.out.println("PU + PUE energy a 0.8 = "+ PU_PUEA_energy_08);
		System.out.println("PU + PUE energy a 0.9 = "+ PU_PUEA_energy_09);
		System.out.println("PU + PUE energy a 1.1 = "+ PU_PUEA_energy_11);
		System.out.println("PU + PUE energy a 1.2 = "+ PU_PUEA_energy_12+"\n\n");
		
		System.out.println("-------- Hs0 --------");
		System.out.println("Noise energy = "+ NoiseEnergy+"\n");
		System.out.println("Mean = "+MathFunctions.Mean(noise_energy_vector));
		System.out.println("Variance = " +MathFunctions.Variance(noise_energy_vector)+"\n");

		System.out.println("-------- Hs1 --------");
		System.out.println("PU + noise energy = "+PU_noise_energy+"\n");
		System.out.println("Mean = "+MathFunctions.Mean(PU_noise_energy_vector));
		System.out.println("Variance = " +MathFunctions.Variance(PU_noise_energy_vector)+"\n");


		
		System.out.println("-------- Hs2 --------");
		System.out.println("PUE a 0.8 + noise energy = "+PUE_noise_energy_08);
		System.out.println("PUE a 0.9 + noise energy = "+PUE_noise_energy_09);
		System.out.println("PUE a 1.1 + noise energy = "+PUE_noise_energy_11);
		System.out.println("PUE a 1.2 + noise energy = "+PUE_noise_energy_12+"\n");
		System.out.println("Mean 0.8 = "+MathFunctions.Mean(PUE_noise_energy_vector_08));
		System.out.println("Variance 0.8 = " +MathFunctions.Variance(PUE_noise_energy_vector_08)+"\n");
		System.out.println("Mean 0.9 = "+MathFunctions.Mean(PUE_noise_energy_vector_09));
		System.out.println("Variance 0.9 = " +MathFunctions.Variance(PUE_noise_energy_vector_09)+"\n");
		System.out.println("Mean 1.1 = "+MathFunctions.Mean(PUE_noise_energy_vector_11));
		System.out.println("Variance 1.1 = " +MathFunctions.Variance(PUE_noise_energy_vector_11)+"\n");
		System.out.println("Mean 1.2 = "+MathFunctions.Mean(PUE_noise_energy_vector_12));
		System.out.println("Variance 1.2 = " +MathFunctions.Variance(PUE_noise_energy_vector_12)+"\n");
		
		
		System.out.println("-------- Hs3 --------");
		System.out.println("PU + PUE a 0.8 + noise energy = "+PU_PUE_noise_energy_08);
		System.out.println("PU + PUE a 0.9 + noise energy = "+PU_PUE_noise_energy_09);
		System.out.println("PU + PUE a 1.1 + noise energy = "+PU_PUE_noise_energy_11);
		System.out.println("PU + PUE a 1.2 + noise energy = "+PU_PUE_noise_energy_12+"\n");
		System.out.println("Mean 0.8 = "+MathFunctions.Mean(PU_PUE_noise_energy_vector_08));
		System.out.println("Variance 0.8 = " +MathFunctions.Variance(PU_PUE_noise_energy_vector_08)+"\n");
		System.out.println("Mean 0.9 = "+MathFunctions.Mean(PU_PUE_noise_energy_vector_09));
		System.out.println("Variance 0.9 = " +MathFunctions.Variance(PU_PUE_noise_energy_vector_09)+"\n");
		System.out.println("Mean 1.1 = "+MathFunctions.Mean(PU_PUE_noise_energy_vector_11));
		System.out.println("Variance 1.1 = " +MathFunctions.Variance(PU_PUE_noise_energy_vector_11));
		System.out.println("Mean 1.2 = "+MathFunctions.Mean(PU_PUE_noise_energy_vector_12));
		System.out.println("Variance 1.2 = " +MathFunctions.Variance(PU_PUE_noise_energy_vector_12));




	}

}
