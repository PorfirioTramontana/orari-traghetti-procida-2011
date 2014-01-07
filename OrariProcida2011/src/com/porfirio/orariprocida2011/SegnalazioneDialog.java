package com.porfirio.orariprocida2011;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SegnalazioneDialog extends Dialog implements OnClickListener{
	private Mezzo mezzo;
	private TextView txtMezzo;
	private TextView txtPartenza;
	private TextView txtArrivo;
	private Context callingContext;
	Spinner spnRagioni;
    private int ragione;
	private EditText txtDettagli;
	private Calendar orarioRef;
	
	public SegnalazioneDialog(Context context, Calendar c) {
		super(context);
		callingContext=context;
		orarioRef=c;
		setContentView(R.layout.segnalazione);
		txtMezzo = (TextView) findViewById(R.id.txtMezzo);
		txtPartenza = (TextView) findViewById(R.id.txtPartenza);
		txtArrivo = (TextView) findViewById(R.id.txtArrivo);
		txtDettagli= (EditText) findViewById(R.id.txtDettagli);
		
    	spnRagioni = (Spinner)findViewById(R.id.spnRagioni);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.strRagioni, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRagioni.setAdapter(adapter);

        spnRagioni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        		ragione=pos;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

	    Button btnInvia = (Button)findViewById(R.id.btnInvia);    
	    btnInvia.setOnClickListener(new View.OnClickListener(){
	    	@Override
	    	public void onClick(View v) {
	    		//Qui il codice per salvare la segnalazione in coda al file delle segnalazioni
	    		String resp=scriviSegnalazione(true);
	    		Toast.makeText(v.getContext(),R.string.ringraziamentoSegnalazione, Toast.LENGTH_SHORT).show();
	    		dismiss();
	    	}
	    });

	    Button btnConferma = (Button)findViewById(R.id.btnConferma);    
	    btnConferma.setOnClickListener(new View.OnClickListener(){
	    	@Override
	    	public void onClick(View v) {
	    		//Qui il codice per salvare la segnalazione in coda al file delle segnalazioni
	    		String resp=scriviSegnalazione(false);
	    		Toast.makeText(v.getContext(),R.string.ringraziamentoSegnalazione, Toast.LENGTH_SHORT).show();
	    		dismiss();
	    	}
	    });

	}
	
	//Aggiunta anche la possibilitÓ di confermare (con un extra button)

	private String scriviSegnalazione(boolean problema){
		HttpClient Client = new DefaultHttpClient();
        String URL = "http://unoprocidaresidente.altervista.org/segnala.php?data=";
        //Pare funzionino le segnalazioni relative al giorno successivo!        
        if (mezzo.getGiornoSeguente())
        	orarioRef.add(Calendar.DAY_OF_YEAR, 1);
        URL+=orarioRef.get(Calendar.DAY_OF_MONTH)+","+(1+orarioRef.get(Calendar.MONTH))+","+orarioRef.get(Calendar.YEAR)+"&s=";
        if (mezzo.getGiornoSeguente()) //rimettiamo a posto!
        	orarioRef.add(Calendar.DAY_OF_YEAR, -1);
        String dettagli=txtDettagli.getText().toString().replaceAll ("\r\n|\r|\n", " ");
        try {
			URL+=URLEncoder.encode((mezzo.nave+","+mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)+","+mezzo.oraPartenza.get(Calendar.MINUTE)
			   +","+mezzo.oraArrivo.get(Calendar.HOUR_OF_DAY)+","+mezzo.oraArrivo.get(Calendar.MINUTE)+","
			   +mezzo.portoPartenza+","+mezzo.portoArrivo+","
			   +mezzo.inizioEsclusione.get(Calendar.DAY_OF_MONTH)+","+mezzo.inizioEsclusione.get(Calendar.MONTH)+","+mezzo.inizioEsclusione.get(Calendar.YEAR)+","
			   +mezzo.fineEsclusione.get(Calendar.DAY_OF_MONTH)+","+mezzo.fineEsclusione.get(Calendar.MONTH)+","+mezzo.fineEsclusione.get(Calendar.YEAR)+","
			   +mezzo.giorniSettimana), "UTF-8");
			if (problema)
				URL=URL+"&motivo="+ragione+"&dettagli="+URLEncoder.encode(dettagli, "UTF-8");	
			else
				URL=URL+"&motivo=99"; //99 convenzionalmente sta per Conferma
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
        
        
       try
        {
             String SetServerString = "";
             HttpGet httpget = new HttpGet(URL);
             ResponseHandler<String> responseHandler = new BasicResponseHandler();
             SetServerString = Client.execute(httpget, responseHandler);
             return SetServerString;
         }
       catch(Exception ex)
          {
    	   return "Fail";
           }
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
		s+=callingContext.getString(R.string.parteAlle)+" "+mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)+":"+mezzo.oraPartenza.get(Calendar.MINUTE);
		s+=" "+callingContext.getString(R.string.del)+" "+mezzo.oraPartenza.get(Calendar.DAY_OF_MONTH)+"/"+(mezzo.oraPartenza.get(Calendar.MONTH)+1)+"/"+mezzo.oraPartenza.get(Calendar.YEAR);
		s+=" "+callingContext.getString(R.string.da)+" "+mezzo.portoPartenza;
		txtPartenza.setText(s);
		s=new String();
		s+=callingContext.getString(R.string.arrivaAlle)+" "+mezzo.oraArrivo.get(Calendar.HOUR_OF_DAY)+":"+mezzo.oraArrivo.get(Calendar.MINUTE);
		s+=" "+callingContext.getString(R.string.del)+" "+mezzo.oraArrivo.get(Calendar.DAY_OF_MONTH)+"/"+(mezzo.oraArrivo.get(Calendar.MONTH)+1)+"/"+mezzo.oraArrivo.get(Calendar.YEAR);		
		s+=" "+callingContext.getString(R.string.a)+" "+mezzo.portoArrivo;
		txtArrivo.setText(s);
		
		
        //trova compagnia c
        Compagnia c=null;
        for (int i=0;i<listCompagnia.size();i++){
        	if (mezzo.nave.contains(listCompagnia.get(i).nome))
        		c=listCompagnia.get(i);
        } 
        
        
	}

}
