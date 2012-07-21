package com.porfirio.orariprocida2011;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;

public class Meteo {
	private double windBeaufort;
	private int windDirection;
	private double windKmh;
	private String windDirectionString;
	private Activity callingActivity;
	
	public Meteo (double wb,int wd){
		setWindBeaufort(wb);
		setWindDirection(wd);
	}
	
	public Meteo(OrariProcida2011Activity orariProcida2011Activity){
		windBeaufort=0.0;
		windDirection=0;
		windKmh=0.0;
		windDirectionString="";
		callingActivity=orariProcida2011Activity;
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
		
		//Penalizzazione per le brezze estive
		if ((Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MONTH)>=5)&&(Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MONTH)<=7))
			limitBeaufort+=2;
		//Aggiunto Aladino
		if (mezzo.nave.equals("Procida Lines") || mezzo.nave.equals("Gestur")|| mezzo.nave.contains("Ippocampo")||mezzo.nave.contains("Aladino")) 
			limitBeaufort-=1; //penalizzazione per mezzi piccoli
		else if (mezzo.nave.equals(callingActivity.getString(R.string.aliscafo)+" SNAV"))
			limitBeaufort-=0.5; //penalizzazione per compagnia privata
		if (mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)==7 && mezzo.oraPartenza.get(Calendar.MINUTE)==40)
			limitBeaufort+=1; // incremento per corsa fondamentale
		else if (mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)==19 && mezzo.oraPartenza.get(Calendar.MINUTE)==25)
			limitBeaufort+=1; // incremento per corsa fondamentale
		else if (mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)==6 && mezzo.oraPartenza.get(Calendar.MINUTE)==25)
			limitBeaufort+=1; // incremento per corsa fondamentale
		else if (mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)==20 && mezzo.oraPartenza.get(Calendar.MINUTE)==0)
			limitBeaufort+=1; // incremento per corsa fondamentale
		//Non metto aggiustamenti per l'orario perchè ho dati solo su base giornaliera
		//Non metto aggiustamenti in base ai porti perchè ho dati per tutto il golfo
		
		if ((getWindDirection()==0 || getWindDirection()==315) && (mezzo.portoArrivo.contains("Ischia")||mezzo.portoPartenza.contains("Ischia")||mezzo.portoArrivo.contains("Casamicciola")||mezzo.portoPartenza.contains("Casamicciola")))
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
		else if (mezzo.portoPartenza.contentEquals("Monte di Procida")||mezzo.portoArrivo.contentEquals("Monte di Procida"))
			limitBeaufort+=4; //TODO Metto valore standard per il porto di Monte di Procida
		
		double extraWind=actualBeaufort-limitBeaufort;
		if (extraWind<=0)
			result="";
		else if (extraWind<=1)
			result=" - "+callingActivity.getString(R.string.pocoProbabile);
		else if (extraWind<=2)
			result=" - "+callingActivity.getString(R.string.aRischio);
		else if (extraWind<=3)
			result=" - "+callingActivity.getString(R.string.corsaQuasi);
		else
			result=" - "+callingActivity.getString(R.string.corsaImpossibile);
		return result;
	
	}

	public String getWindDirectionString() {
		return windDirectionString;
	}

	public Double getWindKmh() {		
		return windKmh;
	}

	public void setWindKmh(double wkmh) {
		windKmh=wkmh;
		return;
	}

	public void setWindDirectionString(String string) {
		windDirectionString=string;
		return;
	}
	
	public String getWindBeaufortString(){
		int forza=new Double(windBeaufort).intValue();
		switch (forza){
		case 0:
			return ""+callingActivity.getString(R.string.calma);
		case 1:
			return ""+callingActivity.getString(R.string.bavaDiVento);
		case 2:
			return ""+callingActivity.getString(R.string.brezzaLeggera);
		case 3:
			return ""+callingActivity.getString(R.string.brezzaTesa);
		case 4:
			return ""+callingActivity.getString(R.string.ventoModerato);
		case 5:
			return ""+callingActivity.getString(R.string.ventoTeso);
		case 6:
			return ""+callingActivity.getString(R.string.ventoFresco);
		case 7:
			return ""+callingActivity.getString(R.string.ventoForte);
		case 8:
			return ""+callingActivity.getString(R.string.burrasca);
		case 9:
			return ""+callingActivity.getString(R.string.burrascaForte);
		case 10:
			return ""+callingActivity.getString(R.string.tempesta);
		case 11:
			return ""+callingActivity.getString(R.string.fortunale);
		case 12:
			return ""+callingActivity.getString(R.string.uragano);
		}
		return ""+callingActivity.getString(R.string.errore);
		}
		
}
