package br.com.ufc.palestrasufc.util;

import br.com.ufc.palestrasufc.model.Lecture;

public class AplicationContext {

	private static AplicationContext aplicationContext;

	private Lecture currentLecture;
	
	private AplicationContext() {}
	
	public static AplicationContext getInstance() {
		if (aplicationContext == null) {
			aplicationContext = new AplicationContext();
		}
		return aplicationContext;
	}

	public Lecture getCurrentLecture() {
		return currentLecture;
	}

	public void setCurrentLecture(Lecture currentLecture) {
		this.currentLecture = currentLecture;
	}

}
