package com.porfirio.orariprocida2011;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

public class Mezzo {
	//gestite nel dettaglio segnalazioni per tipologia e motivi
	public String nave;
	public Calendar oraPartenza;
	public Calendar oraArrivo;
	public String portoPartenza;
	public String portoArrivo;
	public Calendar inizioEsclusione;
	public Calendar fineEsclusione;
	public String giorniSettimana;
	private boolean giornoSeguente;
	private boolean esclusione;
	private int orderInList;
	private int id;
	private double costoIntero;
	private double costoResidente;
	private boolean circaIntero=false;
	private boolean circaResidente=false;
	private Context callingContext;
	private String[] ragioni=new String[100];
	public int[] segnalazioni=new int[100];
	public int conferme=0;
	public int tot=0;
	public boolean conc=true;
			
	public String segnalazionePiuComune(){		
		int max=0;
		int spc=-1;
		for (int i=0;i<segnalazioni.length;i++){
			if (segnalazioni[i]>max){
				max=segnalazioni[i];
				spc=i;
				}
			}		
		if (spc>=0)
			return ragioni[spc];
		else 
			return "";
	}
	
	public Mezzo(Context c,String n,int op, int mp, int oa, int ma, String pp, String pa,int gie,int mie,int aie,int gfe,int mfe,int afe,String gs,OrariProcida2011Activity callingActivity){
		callingContext=c;
		ragioni= callingContext.getResources().getStringArray(R.array.strRagioni);
		for (int i=0;i<100;i++)
			segnalazioni[i]=0;		
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
		fineEsclusione=Calendar.getInstance();
		esclusione=false;
		if (gie!=0){
			esclusione=true;
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
		if (pp.contentEquals("Procida"))
			calcolaCosto(n,pa);
		else
			calcolaCosto(n,pp);
	
	
	}

	public Mezzo(Context c,String n,String op, String mp, String oa, String ma, String pp, String pa,String gie,String mie,String aie,String gfe,String mfe,String afe,String gs,OrariProcida2011Activity callingActivity){
		callingContext=c;
		ragioni = callingContext.getResources().getStringArray(R.array.strRagioni);
		for (int i=0;i<100;i++)
			segnalazioni[i]=0;		
		nave=n;	
		oraPartenza=Calendar.getInstance();
		oraPartenza.set(Calendar.HOUR_OF_DAY, Integer.parseInt(op));
		oraPartenza.set(Calendar.MINUTE, Integer.parseInt(mp));
		oraArrivo=Calendar.getInstance();
		oraArrivo.set(Calendar.HOUR_OF_DAY, Integer.parseInt(oa));
		oraArrivo.set(Calendar.MINUTE, Integer.parseInt(ma));
		portoPartenza=pp;
		portoArrivo=pa;
		inizioEsclusione=Calendar.getInstance();
		fineEsclusione=Calendar.getInstance();
		esclusione=false;
		if (Integer.parseInt(gie)!=0){
			esclusione=true;
			inizioEsclusione.set(Calendar.DAY_OF_MONTH, Integer.parseInt(gie));
			inizioEsclusione.set(Calendar.MONTH, Integer.parseInt(mie)-1); //i mesi sono contati da 0=gennaio
			inizioEsclusione.set(Calendar.YEAR, Integer.parseInt(aie)); //gli anni sono contati da 0=1900
			inizioEsclusione.set(Calendar.HOUR_OF_DAY,0);
			inizioEsclusione.set(Calendar.MINUTE,0);
			fineEsclusione.set(Calendar.DAY_OF_MONTH, Integer.parseInt(gfe));
			fineEsclusione.set(Calendar.MONTH, Integer.parseInt(mfe)-1); //i mesi sono contati da 0=gennaio
			fineEsclusione.set(Calendar.YEAR, Integer.parseInt(afe)); //gli anni sono contati da 0=1900
			fineEsclusione.set(Calendar.HOUR_OF_DAY,23);
			fineEsclusione.set(Calendar.MINUTE,59);
		}
		giorniSettimana=gs;
		if (pp.contentEquals("Procida"))
			calcolaCosto(n,pa);
		else
			calcolaCosto(n,pp);
	
	}
	private void calcolaCosto(String n,String p) {
		//TODO Mettere costi precisi
		if (n.contentEquals("Traghetto Caremar") && p.contentEquals("Pozzuoli")){
			costoIntero=10.0;
			setCircaIntero(true);
			costoResidente=2.40;
			return;
		}
		if (n.contentEquals("Medmar") && p.contentEquals("Pozzuoli")){
			costoIntero=10.0;
			costoResidente=2.30;
			setCircaIntero(true);
			return;
		}		
		if (n.contentEquals("Procida Lines") && p.contentEquals("Pozzuoli")){
			costoIntero=5;
			costoResidente=2.5;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		if (n.contentEquals("Gestur") && p.contentEquals("Pozzuoli")){
			costoIntero=5;
			costoResidente=2.5;
			setCircaIntero(true);
			return;
		}
		if (n.contentEquals("Traghetto Caremar") && p.contentEquals("Napoli Porta di Massa")){
			costoIntero=11;
			costoResidente=3.10;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		if (n.contentEquals("Aliscafo Caremar") && p.contentEquals("Napoli Beverello")){
			costoIntero=13;
			costoResidente=4.70;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}		
		if (n.contentEquals("Aliscafo SNAV") && p.contentEquals("Napoli Beverello")){
			costoIntero=15;
			costoResidente=5.0;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		if (n.contentEquals("Traghetto Caremar") && p.contentEquals("Ischia Porto")){
			costoIntero=6;
			costoResidente=1.90;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		if (n.contentEquals("Aliscafo Caremar") && p.contentEquals("Ischia Porto")){
			costoIntero=8;
			costoResidente=2.4;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		if (n.contentEquals("Aliscafo SNAV") && p.contentEquals("Casamicciola")){
			costoIntero=8;
			costoResidente=2.4;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		if (n.contentEquals("Medmar") && p.contentEquals("Ischia Porto")){
			costoIntero=6;
			costoResidente=2.00;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		if (n.contentEquals("Ippocampo") ){ //TODO Da verificare
			costoIntero=8;
			costoResidente=2.00;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		if (n.contentEquals("Aladino") ){ //TODO Da verificare
			costoIntero=0.00;
			costoResidente=0.00;
			setCircaIntero(true);
			setCircaResidente(true);
			return;
		}
		
	}

	public void setGiornoSeguente(boolean b) {
		giornoSeguente=b;
	}

	public boolean getGiornoSeguente() {
		return giornoSeguente;
	}

	public void setOrderInList(int orderInList) {
		this.orderInList = orderInList;
	}

	public int getOrderInList() {
		return orderInList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setEsclusione(boolean esclusione) {
		this.esclusione = esclusione;
	}

	public boolean isEsclusione() {
		return esclusione;
	}

	public double getCostoIntero() {
		return costoIntero;
	}

	public void setCostoIntero(double costoIntero) {
		this.costoIntero = costoIntero;
	}

	public double getCostoResidente() {
		return costoResidente;
	}

	public void setCostoResidente(double costoResidente) {
		this.costoResidente = costoResidente;
	}

	public boolean isCircaIntero() {
		return circaIntero;
	}

	public void setCircaIntero(boolean circaIntero) {
		this.circaIntero = circaIntero;
	}

	public boolean isCircaResidente() {
		return circaResidente;
	}

	public void setCircaResidente(boolean circaResidente) {
		this.circaResidente = circaResidente;
	}

	public void addMotivo(String rigaMotivo) {
		Integer motivo=Integer.parseInt(rigaMotivo);
		if (motivo==99){
			conferme++;
		}
		else {
			if (tot>segnalazioni[motivo])
				conc=false;
			segnalazioni[motivo]++;
			tot++;
		}
		return;
		
	}

}
