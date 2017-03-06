package it.sp4te.css.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import it.sp4te.css.agents.FusionCenter;
import it.sp4te.css.agents.MaliciousSecondaryUser;
import it.sp4te.css.agents.PrimaryUser;
import it.sp4te.css.agents.TrustedNode;
import it.sp4te.css.agents.TrustedSecondaryUser;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.SignalProcessor;
import it.sp4te.css.signalprocessing.Utils;

public class ProbDetectionVSattackers {
	
	public static void main(String args[]) throws Exception {
	int length = 800; // poi 10000
	int attempts =800;
	int inf=-14;
	int sup=-5 ;
	int block=10; //blocchi energy Detector
	double pfa=0.01; //probabilit� di falso allarme
	int numberTSU;
	int numberTSUTN;
	int numberMSU;
	int numberTN;//numero di utenti fidati
	int L= 28; //N+(delta*j);
	int K= 14; //M+delta;
	int M=8; //BIANCA->GRIGIA
	int N=10;//GRIGIA->NERA

	//Creo il Fusion center
	FusionCenter FC=new FusionCenter();
	//Creo l'utente primario
	PrimaryUser PU= new PrimaryUser();
	//creo il segnale
	Signal s = PU.createAndSend(length);	
	
	//Path per detection e utenti esclusi a tutte le prove di tutti i metodi
			//nome cartella xUtentiyMalevoli
			String path="C:/Users/Pietro/Desktop/Output/";
			//Path esclusivo per gli utenti esclusi dal metodo proposto
			//nome cartella xUtentiyMalevoliUtentiEsclusi
			String pathUE="C:/Users/Pietro/Desktop/OutputUE/";
			//Path per detection e utenti esclusi a tutte le prove di tutti i metodi con TN
			//nome cartella xUtentiyMalevoliTN
			String pathTN="C:/Users/Pietro/Desktop/OutputTN/";
			//Path esclusivo per gli utenti esclusi dal metodo proposto con NT
			//nome cartella xUtentiyMalevoliUtentiEsclusiTN
			String pathTNUE="C:/Users/Pietro/Desktop/OutputTNUE/";
			
int i=2;
	while(i<6){
	
		
		numberMSU=i;
		numberTSU=50-i;
		numberTN=5;	
		numberTSUTN=45-i;
		
		
		
		ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers;
		ArrayList<TrustedSecondaryUser> TrustedSecondaryUsersTN;

		ArrayList<TrustedNode> TrustedNode;
		ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers;

		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceReputation=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceListOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceReputationTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceListTNOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentReputation=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentListOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentReputationTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentListTNOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeReputation=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeListOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeReputationTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeListTNOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();


		ArrayList<Double> ListCooperativeOptEnergyDetectionAbsence= new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeTNOptEnergyDetectionAbsence= new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeTNOptEnergyDetectionOpposite= new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeEnergyDetectionOppositeOpt= new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeEnergyDetectionIntelligentOpt= new ArrayList<Double>();;			
		ArrayList<Double> ListCooperativeTNOptEnergyDetectionIntelligent= new ArrayList<Double>();;


		ArrayList<Double> ReputationEnergyDetectionAbsence = new ArrayList<Double>();;
		ArrayList<Double> ReputationTNEnergyDetectionAbsence = new ArrayList<Double>();;
		ArrayList<Double> ReputationEnergyDetectionOpposite = new ArrayList<Double>();;
		ArrayList<Double> ReputationTNEnergyDetectionOpposite = new ArrayList<Double>();;
		ArrayList<Double> ReputationEnergyDetectionIntelligent = new ArrayList<Double>();;
		ArrayList<Double> ReputationTNEnergyDetectionIntelligent = new ArrayList<Double>();;
		
		TrustedSecondaryUsers= Utils.createTrustedSecondaryUsers(numberTSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		TrustedSecondaryUsersTN= Utils.createTrustedSecondaryUsers(numberTSUTN,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		TrustedNode= Utils.createTrustedNode(numberTN,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		MaliciousSecondaryUsers=Utils.createMaliciousSecondaryUsers(numberMSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);

		///////////////////////////////////////////////////
		userToBinaryDecisionAbsenceReputation=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
		userToBinaryDecisionAbsenceReputation.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ReputationEnergyDetectionAbsence=FC.reputationBasedDecision(path,inf, sup, userToBinaryDecisionAbsenceReputation,attempts,"absence"+"MSU"+numberMSU);

		userToBinaryDecisionAbsenceReputationTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsersTN, pfa);
		userToBinaryDecisionAbsenceReputationTN.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ReputationTNEnergyDetectionAbsence=FC.reputationBasedWithTrustedNodeDecision(pathTN,inf, sup, userToBinaryDecisionAbsenceReputationTN,
				Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa),attempts,"absence"+"MSU"+numberMSU);


		userToBinaryDecisionAbsenceListOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionAbsenceListOpt.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ListCooperativeOptEnergyDetectionAbsence= FC.ListBasedDecisionOptimazed(path,pathUE,inf, sup, userToBinaryDecisionAbsenceListOpt, attempts, K, L, M, N,"MSU"+numberMSU+"_Absence");

		userToBinaryDecisionAbsenceListTNOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsersTN, pfa);
		userToBinaryDecisionAbsenceListTNOpt.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ListCooperativeTNOptEnergyDetectionAbsence=FC.ListBasedWithTrustedNodeDecisionOptimazed(pathTN,pathTNUE,inf, sup, userToBinaryDecisionAbsenceListTNOpt, 
				Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "absence"+"MSU"+numberMSU+"Optimazed");


		FileWriter w=new FileWriter(path+"Detection"+"_Absence"+numberMSU+".txt");
		BufferedWriter b=new BufferedWriter(w);
		b.write("\n");
		b.write("List Based Optimized" +"\n");
		for(int h=0;h<ListCooperativeOptEnergyDetectionAbsence.size();h++){
			b.write(ListCooperativeOptEnergyDetectionAbsence.get(h)+" ");
		}

		b.write("\n");
		b.write("List Based Optimized with TN" +"\n");
		for(int h=0;h<ListCooperativeTNOptEnergyDetectionAbsence.size();h++){
			b.write(ListCooperativeTNOptEnergyDetectionAbsence.get(h)+" ");
		}

		b.write("\n");
		b.write("Reputation" +"\n");
		for(int h=0;h<ReputationEnergyDetectionAbsence.size();h++){
			b.write(ReputationEnergyDetectionAbsence.get(h)+" ");
		}

		b.write("\n");
		b.write("Reputation with TN" +"\n");
		for(int h=0;h<ReputationTNEnergyDetectionAbsence.size();h++){
			b.write(ReputationTNEnergyDetectionAbsence.get(h)+" ");
		}

		b.close();
		//JFreeChartGraphGenerator graphAbsence= new JFreeChartGraphGenerator("List Based VS Reputation");
		//graphAbsence.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation", DetectionGraphAbsence, inf, sup, "C:/Users/Pietro/Desktop/Output/"+K+L+M+N+"_AbsenceMSU"+numberMSU+h+".jpg");

		//JFreeChartGraphGenerator graphAbsenceTN= new JFreeChartGraphGenerator("List Based VS Reputation with TN");
		//graphAbsenceTN.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation with TN", DetectionGraphAbsenceTN, inf, sup, "C:/Users/Pietro/Desktop/OutputTN/"+K+L+M+N+"_AbsenceMSU"+numberMSU+h+"_TN.jpg");

		////////////////////////////////////////

		userToBinaryDecisionOppositeReputation=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
		userToBinaryDecisionOppositeReputation.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ReputationEnergyDetectionOpposite=FC.reputationBasedDecision(path,inf, sup, userToBinaryDecisionOppositeReputation,attempts,"opposite"+"MSU"+numberMSU);

		userToBinaryDecisionOppositeReputationTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsersTN, pfa);
		userToBinaryDecisionOppositeReputationTN.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ReputationTNEnergyDetectionOpposite=FC.reputationBasedWithTrustedNodeDecision(pathTN,inf, sup, userToBinaryDecisionOppositeReputationTN,
				Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa),attempts,"Opposite"+"MSU"+numberMSU);

		userToBinaryDecisionOppositeListOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
		userToBinaryDecisionOppositeListOpt.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeEnergyDetectionOppositeOpt= FC.ListBasedDecisionOptimazed(path,pathUE,inf, sup, userToBinaryDecisionOppositeListOpt, attempts, K, L, M, N,"MSU"+numberMSU+"_Opposite");


		userToBinaryDecisionOppositeListTNOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsersTN, pfa);
		userToBinaryDecisionOppositeListTNOpt.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeTNOptEnergyDetectionOpposite=FC.ListBasedWithTrustedNodeDecisionOptimazed(pathTN,pathTNUE,inf, sup, userToBinaryDecisionOppositeListTNOpt, 
				Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "Opposite"+"MSU"+numberMSU+"_Optimazed");

		

		FileWriter w2=new FileWriter(path+"Detection"+"_Opposite"+numberMSU+".txt");
		BufferedWriter b2=new BufferedWriter(w2);
		b2.write("\n");
		b2.write("List Based Optimized" +"\n");
		for(int h=0;h<ListCooperativeEnergyDetectionOppositeOpt.size();h++){
			b2.write(ListCooperativeEnergyDetectionOppositeOpt.get(h)+" ");
		}


		b2.write("\n");
		b2.write("List Based Optimized with TN" +"\n");
		for(int h=0;h<ListCooperativeTNOptEnergyDetectionOpposite.size();h++){
			b2.write(ListCooperativeTNOptEnergyDetectionOpposite.get(h)+" ");
		}

		b2.write("\n");
		b2.write("Reputation" +"\n");
		for(int h=0;h<ReputationEnergyDetectionOpposite.size();h++){
			b2.write(ReputationEnergyDetectionOpposite.get(h)+" ");
		}

		b2.write("\n");
		b2.write("Reputation with TN" +"\n");
		for(int h=0;h<ReputationTNEnergyDetectionOpposite.size();h++){
			b2.write(ReputationTNEnergyDetectionOpposite.get(h)+" ");
		}

		b2.close();

		//JFreeChartGraphGenerator graphOpposite= new JFreeChartGraphGenerator("List Based VS Reputation - Opposite MSU");
		//graphOpposite.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation- Opposite MSU", DetectionGraphOpposite, inf, sup, "C:/Users/Pietro/Desktop/Output/"+K+L+M+N+"_OppositeMSU"+numberMSU+".jpg");

		//JFreeChartGraphGenerator graphOppositeTN= new JFreeChartGraphGenerator("List Based VS Reputation with TN - Opposite MSU");
		//graphOppositeTN.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation with TN - Opposite MSU", DetectionGraphOppositeTN, inf, sup, "C:/Users/Pietro/Desktop/OutputTN/"+K+L+M+N+"_OppositeMSU"+numberMSU+"_TN.jpg");

		///////////////////////////////////////////////////////////////////////////////////

		userToBinaryDecisionIntelligentReputation=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
		userToBinaryDecisionIntelligentReputation.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ReputationEnergyDetectionIntelligent=FC.reputationBasedDecision(path,inf, sup, userToBinaryDecisionIntelligentReputation,attempts,"Intelligent"+"MSU"+numberMSU);

		userToBinaryDecisionIntelligentReputationTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsersTN, pfa);
		userToBinaryDecisionIntelligentReputationTN.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ReputationTNEnergyDetectionIntelligent=FC.reputationBasedWithTrustedNodeDecision(pathTN,inf, sup, userToBinaryDecisionIntelligentReputationTN,
				Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa),attempts,"Intelligent"+"MSU"+numberMSU);


		userToBinaryDecisionIntelligentListOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);
		userToBinaryDecisionIntelligentListOpt.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeEnergyDetectionIntelligentOpt= FC.ListBasedDecisionOptimazed(path,pathUE,inf, sup, userToBinaryDecisionIntelligentListOpt, attempts, K, L, M, N,"MSU"+numberMSU+"_Intelligent");


		userToBinaryDecisionIntelligentListTNOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsersTN, pfa);
		userToBinaryDecisionIntelligentListTNOpt.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeTNOptEnergyDetectionIntelligent=FC.ListBasedWithTrustedNodeDecisionOptimazed(pathTN,pathTNUE,inf, sup, userToBinaryDecisionIntelligentListTNOpt, 
				Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "Intelligent"+"MSU"+numberMSU+"_Optimazed");



		FileWriter w3=new FileWriter(path+"Detection"+"_Intelligent"+numberMSU+".txt");
		BufferedWriter b3=new BufferedWriter(w3);

		b3.write("\n");
		b3.write("List Based Optimized" +"\n");
		for(int h=0;h<ListCooperativeEnergyDetectionIntelligentOpt.size();h++){
			b3.write(ListCooperativeEnergyDetectionIntelligentOpt.get(h)+" ");
		}


		b3.write("\n");
		b3.write("List Based Optimized with TN" +"\n");
		for(int h=0;h<ListCooperativeTNOptEnergyDetectionIntelligent.size();h++){
			b3.write(ListCooperativeTNOptEnergyDetectionIntelligent.get(h)+" ");
		}

		b3.write("\n");
		b3.write("Reputation" +"\n");
		for(int h=0;h<ReputationEnergyDetectionIntelligent.size();h++){
			b3.write(ReputationEnergyDetectionIntelligent.get(h)+" ");
		}

		b3.write("\n");
		b3.write("Reputation with TN" +"\n");
		for(int h=0;h<ReputationTNEnergyDetectionIntelligent.size();h++){
			b3.write(ReputationTNEnergyDetectionIntelligent.get(h)+" ");
			}

			b3.close();

	i=i+1;	
	}}
}
