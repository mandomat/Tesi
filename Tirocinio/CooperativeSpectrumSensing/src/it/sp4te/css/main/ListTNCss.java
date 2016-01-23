package it.sp4te.css.main;

import java.util.ArrayList;
import java.util.HashMap;

import it.sp4te.css.agents.FusionCenter;
import it.sp4te.css.agents.MaliciousSecondaryUser;
import it.sp4te.css.agents.PrimaryUser;
import it.sp4te.css.agents.TrustedNode;
import it.sp4te.css.agents.TrustedSecondaryUser;
import it.sp4te.css.graphgenerator.JFreeChartGraphGenerator;
import it.sp4te.css.model.Signal;
import it.sp4te.css.signalprocessing.SignalProcessor;
import it.sp4te.css.signalprocessing.Utils;

public class ListTNCss {
	public static void main(String args[]) throws Exception {
		int length = 1000; // poi 10000
		int attempts =500;
		int inf=-14;
		int sup=-5 ;
		int block=10; //blocchi energy Detector
		double pfa=0.01; //probabilit� di falso allarme
		int numberTSU;
		int numberMSU;
		int numberTN;//numero di utenti fidati
		int L= 28; //N+(delta*j);
		int K= 14; //M+delta;
		int M=8; //BIANCA->GRIGIA
		int N=10;//GRIGIA->NERA
		
		
        //int j=3;
		//Creo il Fusion center
		FusionCenter FC=new FusionCenter();
		//Creo l'utente primario
		PrimaryUser PU= new PrimaryUser();
		//creo il segnale
		Signal s = PU.createAndSend(length);
		for(int h=1;h<8;h++){		
	   for(int i=1;i<4;i++){
						numberTSU=30-i;
						numberMSU=i;
						numberTN=3;	
		ArrayList<TrustedSecondaryUser> TrustedSecondaryUsers;
		ArrayList<TrustedNode> TrustedNode;
		ArrayList<MaliciousSecondaryUser> MaliciousSecondaryUsers;
		
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceReputation=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceList=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceListOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceReputationTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceListTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionAbsenceListTNOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		HashMap<String, ArrayList<Double>> DetectionGraphAbsence = new HashMap<String, ArrayList<Double>>();
        HashMap<String, ArrayList<Double>> DetectionGraphAbsenceTN = new HashMap<String, ArrayList<Double>>();


		
		ArrayList<Double> ListCooperativeEnergyDetectionAbsence= new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeOptEnergyDetectionAbsence= new ArrayList<Double>();;

		ArrayList<Double> ReputationEnergyDetectionAbsence = new ArrayList<Double>();;
		ArrayList<Double> ReputationTNEnergyDetectionAbsence = new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeTNEnergyDetectionAbsence= new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeTNOptEnergyDetectionAbsence= new ArrayList<Double>();;

		
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeReputation=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeList=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeListOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeReputationTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeListTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionOppositeListTNOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		HashMap<String, ArrayList<Double>> DetectionGraphOpposite = new HashMap<String, ArrayList<Double>>();
        HashMap<String, ArrayList<Double>> DetectionGraphOppositeTN = new HashMap<String, ArrayList<Double>>();
        
        ArrayList<Double> ListCooperativeEnergyDetectionOpposite= new ArrayList<Double>();;
        ArrayList<Double> ListCooperativeEnergyDetectionOppositeOpt= new ArrayList<Double>();;

		ArrayList<Double> ReputationEnergyDetectionOpposite = new ArrayList<Double>();;
		ArrayList<Double> ReputationTNEnergyDetectionOpposite = new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeTNEnergyDetectionOpposite= new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeTNOptEnergyDetectionOpposite= new ArrayList<Double>();;

		
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentReputation=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentList=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentListOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentReputationTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentListTN=new HashMap<String,ArrayList<ArrayList<Integer>>>();
		HashMap<String,ArrayList<ArrayList<Integer>>> userToBinaryDecisionIntelligentListTNOpt=new HashMap<String,ArrayList<ArrayList<Integer>>>();

		HashMap<String, ArrayList<Double>> DetectionGraphIntelligent = new HashMap<String, ArrayList<Double>>();
        HashMap<String, ArrayList<Double>> DetectionGraphIntelligentTN = new HashMap<String, ArrayList<Double>>();
        
        ArrayList<Double> ListCooperativeEnergyDetectionIntelligent= new ArrayList<Double>();;
        ArrayList<Double> ListCooperativeEnergyDetectionIntelligentOpt= new ArrayList<Double>();;

		ArrayList<Double> ReputationEnergyDetectionIntelligent = new ArrayList<Double>();;
		ArrayList<Double> ReputationTNEnergyDetectionIntelligent = new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeTNEnergyDetectionIntelligent= new ArrayList<Double>();;
		ArrayList<Double> ListCooperativeTNOptEnergyDetectionIntelligent= new ArrayList<Double>();;

	
		
		TrustedSecondaryUsers= Utils.createTrustedSecondaryUsers(numberTSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		TrustedNode= Utils.createTrustedNode(numberTN,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		MaliciousSecondaryUsers=Utils.createMaliciousSecondaryUsers(numberMSU,s,s.getLenght(), SignalProcessor.computeEnergy(s), attempts, inf, sup, block);
		HashMap<String,ArrayList<ArrayList<Integer>>> binaryVector=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		///////////////////////////////////////////////////
		userToBinaryDecisionAbsenceReputation=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionAbsenceReputation.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ReputationEnergyDetectionAbsence=FC.reputationBasedDecision(inf, sup, userToBinaryDecisionAbsenceReputation,attempts,"absence"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionAbsenceReputationTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionAbsenceReputationTN.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ReputationTNEnergyDetectionAbsence=FC.reputationBasedWithTrustedNodeDecision(inf, sup, userToBinaryDecisionAbsenceReputationTN,
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa),attempts,"absence"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionAbsenceList=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionAbsenceList.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ListCooperativeEnergyDetectionAbsence= FC.ListBasedDecision(inf, sup, userToBinaryDecisionAbsenceList, attempts, K, L, M, N,"MSU"+numberMSU+"_Absence"+h);

		userToBinaryDecisionAbsenceListOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionAbsenceListOpt.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ListCooperativeOptEnergyDetectionAbsence= FC.ListBasedDecisionOptimazed(inf, sup, userToBinaryDecisionAbsenceList, attempts, K, L, M, N,"MSU"+numberMSU+"_AbsenceOptimazed"+h);

		userToBinaryDecisionAbsenceListTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionAbsenceListTN.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ListCooperativeTNEnergyDetectionAbsence=FC.ListBasedWithTrustedNodeDecision(inf, sup, userToBinaryDecisionAbsenceListTN, 
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "absence"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionAbsenceListTNOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionAbsenceListTNOpt.putAll(Utils.genereteAbsenceBinaryDecisionVectors(MaliciousSecondaryUsers));
		ListCooperativeTNOptEnergyDetectionAbsence=FC.ListBasedWithTrustedNodeDecisionOptimazed(inf, sup, userToBinaryDecisionAbsenceListTN, 
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "absence"+"MSU"+numberMSU+"Optimazed"+h);
		
		
		DetectionGraphAbsence.put("ListBased", ListCooperativeEnergyDetectionAbsence);
		DetectionGraphAbsence.put("ListBasedOptimazed", ListCooperativeOptEnergyDetectionAbsence);
		DetectionGraphAbsenceTN.put("ListBased with TN", ListCooperativeTNEnergyDetectionAbsence);
		DetectionGraphAbsenceTN.put("ListBasedOptimazed with TN", ListCooperativeTNOptEnergyDetectionAbsence);
		DetectionGraphAbsence.put("Reputation", ReputationEnergyDetectionAbsence);
		DetectionGraphAbsenceTN.put("Reputation with TN", ReputationTNEnergyDetectionAbsence);
		
		JFreeChartGraphGenerator graphAbsence= new JFreeChartGraphGenerator("List Based VS Reputation");
		graphAbsence.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation", DetectionGraphAbsence, inf, sup, "C:/Users/Pietro/Desktop/Output/"+K+L+M+N+"_AbsenceMSU"+numberMSU+h+".jpg");
		
		JFreeChartGraphGenerator graphAbsenceTN= new JFreeChartGraphGenerator("List Based VS Reputation with TN");
		graphAbsenceTN.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation with TN", DetectionGraphAbsenceTN, inf, sup, "C:/Users/Pietro/Desktop/OutputTN/"+K+L+M+N+"_AbsenceMSU"+numberMSU+h+"_TN.jpg");
		
////////////////////////////////////////
		
		userToBinaryDecisionOppositeReputation=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionOppositeReputation.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ReputationEnergyDetectionOpposite=FC.reputationBasedDecision(inf, sup, userToBinaryDecisionOppositeReputation,attempts,"opposite"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionOppositeReputationTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionOppositeReputationTN.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ReputationTNEnergyDetectionOpposite=FC.reputationBasedWithTrustedNodeDecision(inf, sup, userToBinaryDecisionOppositeReputationTN,
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa),attempts,"Opposite"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionOppositeList=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionOppositeList.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeEnergyDetectionOpposite= FC.ListBasedDecision(inf, sup, userToBinaryDecisionOppositeList, attempts, K, L, M, N,"MSU"+numberMSU+"_Opposite"+h);

		userToBinaryDecisionOppositeListOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionOppositeListOpt.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeEnergyDetectionOppositeOpt= FC.ListBasedDecisionOptimazed(inf, sup, userToBinaryDecisionOppositeList, attempts, K, L, M, N,"MSU"+numberMSU+"_OppositeOptimazed"+h);

		
		userToBinaryDecisionOppositeListTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionOppositeListTN.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeTNEnergyDetectionOpposite=FC.ListBasedWithTrustedNodeDecision(inf, sup, userToBinaryDecisionOppositeListTN, 
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "Opposite"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionOppositeListTNOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionOppositeListTNOpt.putAll(Utils.genereteOppositeBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeTNOptEnergyDetectionOpposite=FC.ListBasedWithTrustedNodeDecisionOptimazed(inf, sup, userToBinaryDecisionOppositeListTN, 
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "Opposite"+"MSU"+numberMSU+"_Optimazed"+h);
		
		
		DetectionGraphOpposite.put("ListBased", ListCooperativeEnergyDetectionOpposite);
		DetectionGraphOpposite.put("ListBasedOptimazed", ListCooperativeEnergyDetectionOppositeOpt);
		DetectionGraphOppositeTN.put("ListBased with TN", ListCooperativeTNEnergyDetectionOpposite);
		DetectionGraphOppositeTN.put("ListBased Optimazed with TN", ListCooperativeTNOptEnergyDetectionOpposite);
		DetectionGraphOpposite.put("Reputation", ReputationEnergyDetectionOpposite);
		DetectionGraphOppositeTN.put("Reputation with TN", ReputationTNEnergyDetectionOpposite);
		
		JFreeChartGraphGenerator graphOpposite= new JFreeChartGraphGenerator("List Based VS Reputation - Opposite MSU");
		graphOpposite.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation- Opposite MSU", DetectionGraphOpposite, inf, sup, "C:/Users/Pietro/Desktop/Output/"+K+L+M+N+"_OppositeMSU"+numberMSU+h+".jpg");
		
		JFreeChartGraphGenerator graphOppositeTN= new JFreeChartGraphGenerator("List Based VS Reputation with TN - Opposite MSU");
		graphOppositeTN.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation with TN - Opposite MSU", DetectionGraphOppositeTN, inf, sup, "C:/Users/Pietro/Desktop/OutputTN/"+K+L+M+N+"_OppositeMSU"+numberMSU+h+"_TN.jpg");
		
		///////////////////////////////////////////////////////////////////////////////////
		
		userToBinaryDecisionIntelligentReputation=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionIntelligentReputation.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ReputationEnergyDetectionIntelligent=FC.reputationBasedDecision(inf, sup, userToBinaryDecisionIntelligentReputation,attempts,"Intelligent"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionIntelligentReputationTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionIntelligentReputationTN.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ReputationTNEnergyDetectionIntelligent=FC.reputationBasedWithTrustedNodeDecision(inf, sup, userToBinaryDecisionIntelligentReputationTN,
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa),attempts,"Intelligent"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionIntelligentList=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionIntelligentList.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeEnergyDetectionIntelligent= FC.ListBasedDecision(inf, sup, userToBinaryDecisionIntelligentList, attempts, K, L, M, N,"MSU"+numberMSU+"_Intelligent"+h);

		userToBinaryDecisionIntelligentListOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionIntelligentListOpt.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeEnergyDetectionIntelligentOpt= FC.ListBasedDecisionOptimazed(inf, sup, userToBinaryDecisionIntelligentList, attempts, K, L, M, N,"MSU"+numberMSU+"_OptimazedIntelligent"+h);

		
		userToBinaryDecisionIntelligentListTN=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionIntelligentListTN.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeTNEnergyDetectionIntelligent=FC.ListBasedWithTrustedNodeDecision(inf, sup, userToBinaryDecisionIntelligentListTN, 
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "Intelligent"+"MSU"+numberMSU+h);
		
		userToBinaryDecisionIntelligentListTNOpt=Utils.genereteBinaryDecisionVectors(TrustedSecondaryUsers, pfa);

		userToBinaryDecisionIntelligentListTNOpt.putAll(Utils.genereteIntelligentOppositeMaliciousBinaryDecisionVectors(MaliciousSecondaryUsers,pfa));
		ListCooperativeTNOptEnergyDetectionIntelligent=FC.ListBasedWithTrustedNodeDecisionOptimazed(inf, sup, userToBinaryDecisionIntelligentListTN, 
		Utils.genereteTrustedNodeBinaryDecisionVectors(TrustedNode, pfa), attempts, K, L, M, N, "Intelligent"+"MSU"+numberMSU+"_Optimazed"+h);
		
		
		DetectionGraphIntelligent.put("ListBased", ListCooperativeEnergyDetectionIntelligent);
		DetectionGraphIntelligent.put("ListBasedOptimazed", ListCooperativeEnergyDetectionIntelligentOpt);
		DetectionGraphIntelligentTN.put("ListBased with TN", ListCooperativeTNEnergyDetectionIntelligent);
		DetectionGraphIntelligentTN.put("ListBased Optimazed with TN", ListCooperativeTNOptEnergyDetectionIntelligent);
		DetectionGraphIntelligent.put("Reputation", ReputationEnergyDetectionIntelligent);
		DetectionGraphIntelligentTN.put("Reputation with TN", ReputationTNEnergyDetectionIntelligent);
		
		JFreeChartGraphGenerator graphIntelligent= new JFreeChartGraphGenerator("List Based VS Reputation - Intelligent MSU");
		graphIntelligent.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation- Intelligent MSU", DetectionGraphIntelligent, inf, sup, "C:/Users/Pietro/Desktop/Output/"+K+L+M+N+"_IntelligentMSU"+numberMSU+h+".jpg");
		
		JFreeChartGraphGenerator graphIntelligentTN= new JFreeChartGraphGenerator("List Based VS Reputation with TN - Intelligent MSU");
		graphIntelligentTN.drawAndSaveSNRtoDetectionGraph("List Based VS Reputation with TN - Intelligent MSU", DetectionGraphIntelligentTN, inf, sup, "C:/Users/Pietro/Desktop/OutputTN/"+K+L+M+N+"_IntelligentMSU"+numberMSU+h+"_TN.jpg");
		

	   }}
	}
}