package com.porfirio.orariprocida2011;


import java.util.ArrayList;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DettagliMezzoDialog extends Dialog implements OnClickListener{
	private Mezzo mezzo;
	private TextView txtMezzo;
//	private TextView txtOrario;
//	private TextView txtOraArrivo;
//	private TextView txtOraPartenza;
//	private TextView txtPortoPartenza;
//	private TextView txtPortoArrivo;
	private TextView txtPeriodo;
	private TextView txtGiorniSettimana;
	private TextView txtPartenza;
//	private TextView txtTelefonoCompagnia;
//	private TextView txtNomeCompagnia;
	private TextView txtArrivo;
	private TextView txtCosto;
	
	public ArrayAdapter<String> aalvNumeri;
	public ListView lvNumeri;
	public ArrayList <String> listNumeri;
	public BiglietterieDialog biglietterieDialog;
	public TaxiDialog taxiDialog;
	

	public DettagliMezzoDialog(Context context) {
		super(context);
		setContentView(R.layout.dettaglimezzo);
		txtMezzo = (TextView) findViewById(R.id.txtMezzo);
		txtPartenza = (TextView) findViewById(R.id.txtPartenza);
		txtArrivo = (TextView) findViewById(R.id.txtArrivo);
//		txtOrario = (TextView) findViewById(R.id.txtOrario);
//		txtOraPartenza = (TextView) findViewById(R.id.txtOraPartenza);
//		txtOraArrivo = (TextView) findViewById(R.id.txtOraArrivo);
//		txtPortoPartenza = (TextView) findViewById(R.id.txtPortoPartenza);
//		txtPortoArrivo = (TextView) findViewById(R.id.txtPortoArrivo);
		txtPeriodo = (TextView) findViewById(R.id.txtPeriodo); txtPeriodo.setText("");
		txtGiorniSettimana = (TextView) findViewById(R.id.txtGiorniSettimana); txtGiorniSettimana.setText("");
//		txtNomeCompagnia = (TextView) findViewById(R.id.txtNomeCompagnia);
//		txtTelefonoCompagnia = (TextView) findViewById(R.id.txtTelefonoCompagnia);
		txtCosto= (TextView) findViewById(R.id.txtCosto);
		
	    Button btnReturnToHome = (Button)findViewById(R.id.btnReturnToHome);    
	    btnReturnToHome.setOnClickListener(new View.OnClickListener(){
	    	@Override
	    	public void onClick(View v) {
	    		dismiss();
	    	}
	    });

	    Button btnTaxi = (Button)findViewById(R.id.btnTaxi);    
	    btnTaxi.setOnClickListener(new View.OnClickListener(){
	    	@Override
	    	public void onClick(View v) {
	            taxiDialog.show();
	    	}
	    });
	    
	    Button btnBiglietterie = (Button)findViewById(R.id.btnBiglietterie);    
	    btnBiglietterie.setOnClickListener(new View.OnClickListener(){
	    	@Override
	    	public void onClick(View v) {
	            biglietterieDialog.show();
	    	}
	    });
	}

	public void setMezzo(Mezzo m){
		mezzo=m;
	}
	
	@Override
	public void onClick(View arg0) {
		this.dismiss();		
	}


	
	public void fill(ArrayList<Compagnia> listCompagnia) {
		txtMezzo.setText("    "+mezzo.nave+"    ");
		String s=new String();
		s+="Parte alle "+mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)+":"+mezzo.oraPartenza.get(Calendar.MINUTE);
		s+=" del "+mezzo.oraPartenza.get(Calendar.DAY_OF_MONTH)+"/"+(mezzo.oraPartenza.get(Calendar.MONTH)+1)+"/"+mezzo.oraPartenza.get(Calendar.YEAR);
		s+=" da "+mezzo.portoPartenza;
		txtPartenza.setText(s);
		s=new String();
		s+="Arriva alle "+mezzo.oraArrivo.get(Calendar.HOUR_OF_DAY)+":"+mezzo.oraArrivo.get(Calendar.MINUTE);
		s+=" del "+mezzo.oraArrivo.get(Calendar.DAY_OF_MONTH)+"/"+(mezzo.oraArrivo.get(Calendar.MONTH)+1)+"/"+mezzo.oraArrivo.get(Calendar.YEAR);		
		s+=" a "+mezzo.portoArrivo;
		txtArrivo.setText(s);
		
		//TODO Da COmpletare
//		if (mezzo.isEsclusione())
//			txtPeriodo.setText(mezzo.inizioEsclusione.get(Calendar.DAY_OF_MONTH));
//		txtGiorniSettimana.setText(mezzo.giorniSettimana);

//        listNumeri = new ArrayList <String>();
//        lvNumeri=(ListView)findViewById(R.id.listViewNumeri);
//        aalvNumeri = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1);       
//        lvNumeri.setAdapter(aalvNumeri);
//		
		s=new String();
		s+="Costo : "+mezzo.getCostoResidente()+" euro ";
		if (mezzo.isCircaResidente())
			s+="circa ";
		s+="(residente) o "+mezzo.getCostoIntero()+" euro";
		if (mezzo.isCircaIntero())
			s+="circa ";		
		s+="(intero)";
		txtCosto.setText(s);
		
        //trova compagnia c
        Compagnia c=null;
        for (int i=0;i<listCompagnia.size();i++){
        	if (mezzo.nave.contains(listCompagnia.get(i).nome))
        		c=listCompagnia.get(i);
        } 
        
        biglietterieDialog = new BiglietterieDialog(this.getContext());
        biglietterieDialog.fill(c);
        
        taxiDialog = new TaxiDialog(this.getContext());
        taxiDialog.fill(mezzo.portoPartenza);
        
//        
//        for (int i=0;i<c.nomeNumeroTelefono.size();i++){        	
//        	aalvNumeri.add(c.nomeNumeroTelefono.get(i)+" : "+c.numeroTelefono.get(i));
////        	Linkify.addLinks( (TextView) lvNumeri.getChildAt(i), Linkify.PHONE_NUMBERS);
//        }
	}

}
