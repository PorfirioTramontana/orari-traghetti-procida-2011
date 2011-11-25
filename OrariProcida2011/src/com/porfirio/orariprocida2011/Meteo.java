package com.porfirio.orariprocida2011;

import java.util.Calendar;

public class Meteo {
	private double windBeaufort;
	private int windDirection;
	
	public Meteo (double wb,int wd){
		setWindBeaufort(wb);
		setWindDirection(wd);
	}
	
	public Meteo(){
		windBeaufort=0.0;
		windDirection=0;
	}

	public void setWindBeaufort(double windBeaufort) {
		this.windBeaufort = windBeaufort;
	}

	public double getWindBeaufort() {
		return windBeaufort;
	}

	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}

	public int getWindDirection() {
		return windDirection;
	}

	public String condimeteoString(OrariProcida2011Activity orariProcida2011Activity, Mezzo mezzo) {
		String result=new String("");
		Double actualBeaufort=getWindBeaufort();
		Double limitBeaufort=0.0;
		
		if (mezzo.nave.equals("Procida Lines") || mezzo.nave.equals("Gestur"))
			limitBeaufort-=1; //penalizzazione per mezzi piccoli
		else if (mezzo.nave.equals("Aliscafo SNAV"))
			limitBeaufort-=0.5; //penalizzazione per compagnia privata
		if (mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)==7 && mezzo.oraPartenza.get(Calendar.MINUTE)==40)
			limitBeaufort+=1; // incremento per corsa fondamentale
		else if (mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)==19 && mezzo.oraPartenza.get(Calendar.MINUTE)==30)
			limitBeaufort+=1; // incremento per corsa fondamentale
		else if (mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)==6 && mezzo.oraPartenza.get(Calendar.MINUTE)==25)
			limitBeaufort+=1; // incremento per corsa fondamentale
		else if (mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)==20 && mezzo.oraPartenza.get(Calendar.MINUTE)==30)
			limitBeaufort+=1; // incremento per corsa fondamentale
		//Non metto aggiustamenti per l'orario perchè ho dati solo su base giornaliera
		//Non metto aggiustamenti in base ai porti perchè ho dati per tutto il golfo
		
		if ((getWindDirection()==0 || getWindDirection()==315) && (mezzo.portoArrivo.contains("Ischia")||mezzo.portoPartenza.contains("Ischia")))
			limitBeaufort+=4;
		else if ((getWindDirection()==0 || getWindDirection()==315) && (mezzo.portoArrivo.contains("Napoli")||mezzo.portoPartenza.contains("Napoli")||mezzo.portoArrivo.contentEquals("Pozzuoli")||mezzo.portoPartenza.contentEquals("Pozzuoli")))
			limitBeaufort+=5;
		else if ((getWindDirection()==45 || getWindDirection()==90))
			limitBeaufort+=4;
		else if ((getWindDirection()==135 || getWindDirection()==180 || getWindDirection()==225)&&(!(mezzo.nave.contains("Aliscafo"))))
			limitBeaufort+=4;
		else if ((getWindDirection()==135 || getWindDirection()==180 || getWindDirection()==225)&&(mezzo.nave.contains("Aliscafo")))
			limitBeaufort+=3;
		else if ((getWindDirection()==270))
			limitBeaufort+=3;
		
		double extraWind=actualBeaufort-limitBeaufort;
		if (extraWind<=0)
			result="";
		else if (extraWind<=1)
			result=" - Poco probabile rischio Maltempo!!!";
		else if (extraWind<=2)
			result=" - A rischio Maltempo!!!";
		else if (extraWind<=3)
			result=" - Corsa quasi sicuramente sospesa !!!";
		return result;
	
	}
}
