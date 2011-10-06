package com.porfirio.orariprocida2011;

import java.util.Calendar;

public class Mezzo {
	public String nave;
	public Calendar oraPartenza;
	public Calendar oraArrivo;
	public String portoPartenza;
	public String portoArrivo;
	public Calendar inizioEsclusione;
	public Calendar fineEsclusione;
	public String giorniSettimana;
	private boolean giornoSeguente;
	
	public Mezzo(String n,int op, int mp, int oa, int ma, String pp, String pa,int gie,int mie,int aie,int gfe,int mfe,int afe,String gs){
		nave=n;	
		oraPartenza=Calendar.getInstance();
		oraPartenza.set(Calendar.HOUR_OF_DAY, op);
		oraPartenza.set(Calendar.MINUTE, mp);
		oraArrivo=Calendar.getInstance();
		oraArrivo.set(Calendar.HOUR_OF_DAY, oa);
		oraArrivo.set(Calendar.MINUTE, ma);
		portoPartenza=pp;
		portoArrivo=pa;
		inizioEsclusione=Calendar.getInstance();
		inizioEsclusione.set(0, 1, 1);
		fineEsclusione=Calendar.getInstance();
		fineEsclusione.set(0, 1, 1);
		if (gie!=0){
			inizioEsclusione.set(Calendar.DAY_OF_MONTH, gie);
			inizioEsclusione.set(Calendar.MONTH, mie-1); //i mesi sono contati da 0=gennaio
			inizioEsclusione.set(Calendar.YEAR, aie); //gli anni sono contati da 0=1900
			inizioEsclusione.set(Calendar.HOUR_OF_DAY,0);
			inizioEsclusione.set(Calendar.MINUTE,0);
			fineEsclusione.set(Calendar.DAY_OF_MONTH, gfe);
			fineEsclusione.set(Calendar.MONTH, mfe-1); //i mesi sono contati da 0=gennaio
			fineEsclusione.set(Calendar.YEAR, afe); //gli anni sono contati da 0=1900
			fineEsclusione.set(Calendar.HOUR_OF_DAY,23);
			fineEsclusione.set(Calendar.MINUTE,59);
		}
		giorniSettimana=gs;
	
	
	}

	public void setGiornoSeguente(boolean b) {
		// TODO Auto-generated method stub
		giornoSeguente=b;
	}

	public boolean getGiornoSeguente() {
		// TODO Auto-generated method stub
		return giornoSeguente;
	}

}
