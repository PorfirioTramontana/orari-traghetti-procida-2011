package com.porfirio.orariprocida2011;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class Mezzo {
	public String nave;
	public Date oraPartenza;
	public Date oraArrivo;
	public String portoPartenza;
	public String portoArrivo;
	
	public Mezzo(String n,int op, int mp, int oa, int ma, String pp, String pa){
		nave=n;	
		oraPartenza=new Date();
		oraPartenza.setHours(op);
		oraPartenza.setMinutes(mp);
		oraArrivo=new Date();
		oraArrivo.setHours(oa);
		oraArrivo.setMinutes(ma);
		portoPartenza=pp;
		portoArrivo=pa;
	
	
	}
}
