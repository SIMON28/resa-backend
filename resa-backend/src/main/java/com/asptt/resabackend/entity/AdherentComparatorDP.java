package com.asptt.resabackend.entity;

import java.util.Comparator;

public class AdherentComparatorDP implements Comparator<Adherent> {

	@Override
	public int compare(Adherent o1, Adherent o2) {
		String e1 = o1.getEncadrement().name();
		String e2 = o2.getEncadrement().name();
		int comp = -1;
		if(null == e1){
			comp = 1;
		}
		if(null == e2){
			comp = -1;
		}
		if(null != e1 && null != e2){
			if(e1.equals(e2) ){
				comp = 0;
			}
			if(e1.equals("E2") && e2.equals("E3")){
				comp = 1;
			}
			if(e1.equals("E2") && e2.equals("E4")){
				comp = 1;
			}
			if(e1.equals("E3") && e2.equals("E4")){
				comp = 1;
			}
			if(e1.equals("E3") && e2.equals("E2")){
				comp = -1;
			}
			if(e1.equals("E4") && e2.equals("E2")){
				comp = -1;
			}
			if(e1.equals("E4") && e2.equals("E3")){
				comp = -1;
			}
		}
		return comp;
	}

}
