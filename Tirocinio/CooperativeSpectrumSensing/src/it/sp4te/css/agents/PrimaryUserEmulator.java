package it.sp4te.css.agents;

import it.sp4te.css.model.Signal;

public class PrimaryUserEmulator {
	
	public Signal createAndSend(int length,double power){
		Signal s = new Signal(length,power);
	
		return s;
	}

}
