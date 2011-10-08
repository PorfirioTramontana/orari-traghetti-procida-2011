package com.porfirio.orariprocida2011;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class OrariProcida2011Activity extends Activity {
	/** Called when the activity is first created. */
    

    public String nave;
    public String portoPartenza;
    public String portoArrivo;
//    public Date orario;
    public Calendar c;
	public ArrayAdapter<String> aalvMezzi;
	public ListView lvMezzi;
	public Button buttonMinusMinus;
	public Button buttonMinus;
	public Button buttonPlusPlus;
	public Button buttonPlus;
	public TextView txtOrario;
	private AlertDialog aboutDialog;
	public int finestraTemporale=24;
	private DettagliMezzoDialog dettagliMezzoDialog;
	private ArrayList <Mezzo> selectMezzi;
	
    static final int ABOUT_DIALOG_ID = 0;

	private ArrayList<Mezzo> listMezzi;
	private ArrayList<Compagnia> listCompagnia;
    
    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.about:
            showDialog(ABOUT_DIALOG_ID);
            return true;
        case R.id.esci:
        	OrariProcida2011Activity.this.finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Gli orari sono quelli resi noti dalle compagnie di navigazione alle biglietterie o sui loro siti web. L'autore non e' in alcun modo responsabile di ogni eventuale loro cambiamento. Orari aggiornati al 3 ottobre 2011. By Porfirio Tramontana 2011. In licenza GPL3. http://code.google.com/p/orari-traghetti-procida-2011/")
               .setCancelable(false)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        aboutDialog = builder.create();

        // get the current time
        
        c = Calendar.getInstance(TimeZone.getDefault());
        
//        orario=c.getTime();
        
        
        txtOrario = (TextView)findViewById(R.id.txtOrario);
        
		String s=new String("Dalle ");
		if (c.get(Calendar.HOUR_OF_DAY)<10)
			s+="0";
		s+=c.get(Calendar.HOUR_OF_DAY)+":";
		if (c.get(Calendar.MINUTE)<10)
			s+="0";
		s+=c.get(Calendar.MINUTE)+" del ";
		s+=c.get(Calendar.DAY_OF_MONTH)+"/";
		s+=(c.get(Calendar.MONTH)+1)+"";
		txtOrario.setText(s);
        
        buttonMinusMinus = (Button)findViewById(R.id.button1);    
        buttonMinusMinus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		
//        		orario.setHours(orario.getHours()-1);
        		c.add(Calendar.HOUR, -1);
        		String s=new String("Dalle ");
        		if (c.get(Calendar.HOUR_OF_DAY)<10)
        			s+="0";
        		s+=c.get(Calendar.HOUR_OF_DAY)+":";
        		if (c.get(Calendar.MINUTE)<10)
        			s+="0";
        		s+=c.get(Calendar.MINUTE)+" del ";
        		s+=c.get(Calendar.DAY_OF_MONTH)+"/";
        		s+=(c.get(Calendar.MONTH)+1)+"";
        		txtOrario.setText(s);
        		aggiornaLista();
        	}
        });
        
        buttonMinus = (Button)findViewById(R.id.button2);    
        buttonMinus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
//        		orario.setMinutes(orario.getMinutes()-15);
        		c.add(Calendar.MINUTE, -15);
        		String s=new String("Dalle ");
        		if (c.get(Calendar.HOUR_OF_DAY)<10)
        			s+="0";
        		s+=c.get(Calendar.HOUR_OF_DAY)+":";
        		if (c.get(Calendar.MINUTE)<10)
        			s+="0";
        		s+=c.get(Calendar.MINUTE)+" del ";
        		s+=c.get(Calendar.DAY_OF_MONTH)+"/";
        		s+=(c.get(Calendar.MONTH)+1)+"";
        		txtOrario.setText(s);
        		aggiornaLista();
        	}
        });
        
        buttonPlus = (Button)findViewById(R.id.button3);    
        buttonPlus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
//        		orario.setMinutes(orario.getMinutes()+15);
        		c.add(Calendar.MINUTE, 15);
        		String s=new String("Dalle ");
        		if (c.get(Calendar.HOUR_OF_DAY)<10)
        			s+="0";
        		s+=c.get(Calendar.HOUR_OF_DAY)+":";
        		if (c.get(Calendar.MINUTE)<10)
        			s+="0";
        		s+=c.get(Calendar.MINUTE)+" del ";
        		s+=c.get(Calendar.DAY_OF_MONTH)+"/";
        		s+=(c.get(Calendar.MONTH)+1)+"";
        		txtOrario.setText(s);
        		aggiornaLista();
        	}
        });
        
        buttonPlusPlus = (Button)findViewById(R.id.button4);    
        buttonPlusPlus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
//        		orario.setHours(orario.getHours()+1);
        		c.add(Calendar.HOUR, 1);
        		String s=new String("Dalle ");
        		if (c.get(Calendar.HOUR_OF_DAY)<10)
        			s+="0";
        		s+=c.get(Calendar.HOUR_OF_DAY)+":";
        		if (c.get(Calendar.MINUTE)<10)
        			s+="0";
        		s+=c.get(Calendar.MINUTE)+" del ";
        		s+=c.get(Calendar.DAY_OF_MONTH)+"/";
        		s+=(c.get(Calendar.MONTH)+1)+"";
        		txtOrario.setText(s);
        		aggiornaLista();
        	}
        });
        setSpinner();

        
        listMezzi = new ArrayList <Mezzo>();
        lvMezzi=(ListView)findViewById(R.id.listMezzi);
        aalvMezzi = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);       
        lvMezzi.setAdapter(aalvMezzi);
        
        dettagliMezzoDialog = new DettagliMezzoDialog(this);
        lvMezzi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	//listener sul click di un item della lista
        	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				int position=arg2;
				for (int i=0;i<aalvMezzi.getCount();i++){
					if (selectMezzi.get(i).getOrderInList()==position)
						dettagliMezzoDialog.setMezzo(selectMezzi.get(i));
				}
					
				
//				problema: clicco sulla lista ma ho solo la stringa, non il mezzo corrispondente
//				soluzione: mantenere una variabile ordine che abbini lvMezzi con Mezzi
//				altra soluzione: trovare il mezzo dalla stringa
				dettagliMezzoDialog.fill(listCompagnia);
				dettagliMezzoDialog.show();
			}

        	
		});
        
        riempiLista();
        aggiornaLista();

    }
    
    private void riempiLista() {
    	listCompagnia=new ArrayList<Compagnia>();
    	
    	Compagnia c=new Compagnia("Caremar");
    	c.addTelefono("Napoli (Molo Beverello)", "0815513882");
    	c.addTelefono("Pozzuoli", "0815262711");
    	c.addTelefono("Pozzuoli", "0815261335");
    	c.addTelefono("Ischia", "081984818");
    	c.addTelefono("Ischia", "081991953");
    	c.addTelefono("Procida", "0818967280");
    	listCompagnia.add(c);
    	
    	c=new Compagnia("Gestur");
    	c.addTelefono("Procida", "0818531405");
    	listCompagnia.add(c);
    	
    	c=new Compagnia("SNAV");
    	c.addTelefono("Napoli", "0814285111");
    	c.addTelefono("Ischia", "081984818");
    	c.addTelefono("Procida", "0818969975");  	
    	listCompagnia.add(c);
    	
    	c=new Compagnia("Medmar");
    	c.addTelefono("Napoli", "0813334411");
    	listCompagnia.add(c);
    	
    	
   	// convenzione giorni settimana:
	// DOMENICA =1 LUNEDI=2 MARTEDI=3 MERCOLEDI=4 GIOVEDI=5 VENERDI=6 SABATO=7
    	
//    	listMezzi.add(new Mezzo("Prova",0,20,8,25,"Prova","Prova",2,10,2011,6,10,2011,"1234567"));
//    	listMezzi.add(new Mezzo("Prova-",0,20,8,25,"Prova","Prova",6,10,2011,7,10,2011,"1234567"));
//    	listMezzi.add(new Mezzo("Prova1",0,40,8,25,"Prova","Prova",0,0,0,0,0,0,"12345"));
//    	listMezzi.add(new Mezzo("Prova2",0,50,8,25,"Prova","Prova",0,0,0,0,0,0,"5"));
//    	listMezzi.add(new Mezzo("Prova3",0,55,8,25,"Prova","Prova",0,0,0,0,0,0,"67"));
////    	
    	listMezzi.add(new Mezzo("Aliscafo Caremar",8,10,8,25,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",9,10,9,45,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",12,10,12,40,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",18,35,19,00,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",10,20,10,55,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",13,50,14,20,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",19,15,19,45,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",8,55,9,15,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",7,30,8,10,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",8,50,9,30,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",11,45,12,25,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",13,10,13,50,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",15,10,15,50,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",17,30,18,10,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",18,15,18,55,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		//listMezzi.add(new Mezzo("Traghetto Caremar",0,15,1,15,"Napoli Porta di Massa","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",6,25,7,25,"Napoli Porta di Massa","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",9,10,10,10,"Napoli Porta di Massa","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",10,45,11,45,"Napoli Porta di Massa","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",15,15,16,15,"Napoli Porta di Massa","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",17,45,18,45,"Napoli Porta di Massa","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",19,30,20,30,"Napoli Porta di Massa","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",22,15,23,15,"Napoli Porta di Massa","Procida",0,0,0,0,0,0,"1234567"));
		//listMezzi.add(new Mezzo("Traghetto Caremar",2,20,3,20,"Procida","Napoli Porta di Massa",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",7,40,8,40,"Procida","Napoli Porta di Massa",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",13,35,14,35,"Procida","Napoli Porta di Massa",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",14,35,15,35,"Procida","Napoli Porta di Massa",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",16,15,17,15,"Procida","Napoli Porta di Massa",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",18,5,19,0,"Procida","Napoli Porta di Massa",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",20,30,21,30,"Procida","Napoli Porta di Massa",0,0,0,0,0,0,"1234567"));
		//listMezzi.add(new Mezzo("Traghetto Caremar",22,55,23,55,"Procida","Napoli Porta di Massa",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",6,35,7,15,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",7,55,8,35,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",9,25,10,5,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",10,35,11,15,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",13,30,14,10,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",14,55,15,35,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",16,55,17,35,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",6,50,7,30,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",9,40,10,20,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",11,30,12,10,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",14,5,14,45,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",17,5,17,45,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",8,25,9,5,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",10,40,11,20,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",13,0,13,40,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",15,30,16,10,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Gestur",17,55,18,35,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",8,25,9,0,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",12,20,12,55,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",16,20,16,55,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",19,0,19,35,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",7,30,8,5,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",10,10,10,45,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",14,15,14,50,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",18,5,18,40,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Medmar",4,10,4,50,"Pozzuoli","Procida",0,0,0,0,0,0,"23456"));
		listMezzi.add(new Mezzo("Medmar",20,30,21,10,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Medmar",3,10,3,50,"Procida","Pozzuoli",0,0,0,0,0,0,"23456"));
		listMezzi.add(new Mezzo("Medmar",19,40,20,20,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
		
		listMezzi.add(new Mezzo("Traghetto Caremar",7,35,7,55,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",10,20,10,40,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",11,5,11,25,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",11,55,12,15,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",14,30,14,50,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",16,25,18,45,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",18,55,19,15,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",19,50,20,10,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",20,35,20,55,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",23,20,23,40,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		
		listMezzi.add(new Mezzo("Traghetto Caremar",7,0,7,20,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",8,30,8,50,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",11,30,11,50,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",12,55,13,15,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",13,55,14,15,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",15,30,15,50,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",17,25,17,45,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",18,0,18,20,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Traghetto Caremar",19,55,20,15,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		
		listMezzi.add(new Mezzo("Aliscafo Caremar",9,35,9,50,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",12,30,12,45,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",13,55,14,10,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",15,55,16,10,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",19,0,19,15,"Procida","Ischia",0,0,0,0,0,0,"1234567"));
		
		listMezzi.add(new Mezzo("Aliscafo Caremar",7,30,7,45,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",10,10,10,25,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",13,5,13,20,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",14,30,14,45,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",16,30,16,45,"Ischia","Procida",0,0,0,0,0,0,"1234567"));
		
		listMezzi.add(new Mezzo("Aliscafo SNAV",7,10,7,25,"Procida","Casamicciola",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",9,45,10,0,"Procida","Casamicciola",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",13,50,14,10,"Procida","Casamicciola",0,0,0,0,0,0,"1234567"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",17,40,17,55,"Procida","Casamicciola",0,0,0,0,0,0,"1234567"));		
		
	}

	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case ABOUT_DIALOG_ID:
			return aboutDialog;
        }        
        return null;
    }
    
//    // the callback received when the user "sets" the time in the dialog
//    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {                
//            	orario.setHours(hourOfDay);
//                orario.setMinutes(minute);
//            }
//        };
        
        
    public void aggiornaLista() {    	
    	selectMezzi= new ArrayList <Mezzo>();
        Comparator<Mezzo> comparator = new Comparator<Mezzo>() {
    		@Override
    		public int compare(Mezzo m1, Mezzo m2) {
    			if (m1.getGiornoSeguente()==m2.getGiornoSeguente()){
	    			if(m1.oraPartenza.before(m2.oraPartenza))
	    				return -1;
	    			else if (m1.oraPartenza.after(m2.oraPartenza))
	    				return 1;
	    			else
	    				return 0;
    			}
    			else if (m1.getGiornoSeguente()){
    				return 1;
    				}
    			else if (m2.getGiornoSeguente()){
    				return -1;
    			}
				return 0;
    		}
    		};
    	
    	aalvMezzi.clear();

		Calendar oraLimite=(Calendar) c.clone();
		oraLimite.add(Calendar.HOUR_OF_DAY, finestraTemporale);

    	//qui riempio aalvMezzi in base agli input e ai dati di listMezzi
    	for (int i=0;i<listMezzi.size();i++){
    		//per ogni mezzo valuta se ci interessa
    		Calendar oraNave=(Calendar) listMezzi.get(i).oraPartenza.clone();
    		oraNave.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
    		oraNave.set(Calendar.MONTH, c.get(Calendar.MONTH));
    		oraNave.set(Calendar.YEAR, c.get(Calendar.YEAR));
    		if ((oraNave.get(Calendar.HOUR_OF_DAY)<c.get(Calendar.HOUR_OF_DAY))||(oraNave.get(Calendar.HOUR_OF_DAY)==c.get(Calendar.HOUR_OF_DAY))&&(oraNave.get(Calendar.MINUTE)<c.get(Calendar.MINUTE)))
    			oraNave.add(Calendar.DAY_OF_MONTH, 1);

    		if (listMezzi.get(i).nave.equals(nave) || nave.equals("Tutti")){
    			if (listMezzi.get(i).portoPartenza.contains((portoPartenza))  || portoPartenza.equals("Tutti")){
    				if (listMezzi.get(i).portoArrivo.contains(portoArrivo)  || portoArrivo.equals("Tutti")){
    					if (listMezzi.get(i).inizioEsclusione.after(oraNave) || listMezzi.get(i).fineEsclusione.before(oraNave))
    						if (listMezzi.get(i).giorniSettimana.contains(String.valueOf(oraNave.get(Calendar.DAY_OF_WEEK))))
		    					if (oraNave.before(oraLimite))	{
		    						if (oraNave.get(Calendar.DAY_OF_MONTH)!=c.get(Calendar.DAY_OF_MONTH))
		    							listMezzi.get(i).setGiornoSeguente(true);
		    						else
		    							listMezzi.get(i).setGiornoSeguente(false);
		    						listMezzi.get(i).setId(i);
		    						selectMezzi.add(listMezzi.get(i));	    								    								
		    						}
        				}
    			}
    		}
	
    				
    	}
    	Collections.sort(selectMezzi,comparator);
		for (int i=0;i<selectMezzi.size();i++){
			selectMezzi.get(i).setOrderInList(i); 
    		String s=new String(selectMezzi.get(i).nave+" - "+selectMezzi.get(i).portoPartenza+" - "+selectMezzi.get(i).portoArrivo+" - ");
//    		s += selectMezzi.get(i).getOrderInList()+" - ";
    		if (selectMezzi.get(i).oraPartenza.get(Calendar.HOUR_OF_DAY)<10)
    			s+="0";
    		s+=selectMezzi.get(i).oraPartenza.get(Calendar.HOUR_OF_DAY)+":";
    		if (selectMezzi.get(i).oraPartenza.get(Calendar.MINUTE)<10)
    			s+="0";
    		s+=selectMezzi.get(i).oraPartenza.get(Calendar.MINUTE)+" ";
//    		s+=selectMezzi.get(i).getGiornoSeguente()+" ";
			aalvMezzi.add(s);
		}

	}
    
	private void setSpinner(){
    	Spinner spnNave = (Spinner)findViewById(R.id.spnNave);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.strMezzi, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNave.setAdapter(adapter);
        
        nave=new String("Tutti");
        
        spnNave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	nave=parent.getItemAtPosition(pos).toString();
            	aggiornaLista();            	
            }

			public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    	Spinner spnPortoPartenza = (Spinner)findViewById(R.id.spnPortoPartenza);
    	ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.strPorti, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPortoPartenza.setAdapter(adapter2);
        
        portoPartenza=new String(adapter2.getItem(0).toString());
        
        spnPortoPartenza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	portoPartenza=parent.getItemAtPosition(pos).toString();            	
            	aggiornaLista();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    	Spinner spnPortoArrivo = (Spinner)findViewById(R.id.spnPortoArrivo);
    	ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.strPorti, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPortoArrivo.setAdapter(adapter3);
        
        portoArrivo=new String(adapter3.getItem(0).toString());
        
        spnPortoArrivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	portoArrivo=parent.getItemAtPosition(pos).toString();
            	aggiornaLista();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });        
        
        

        




	}
}