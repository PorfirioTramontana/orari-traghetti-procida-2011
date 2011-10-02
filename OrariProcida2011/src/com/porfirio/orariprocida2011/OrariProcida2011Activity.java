package com.porfirio.orariprocida2011;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class OrariProcida2011Activity extends Activity {
    protected static final int TIME_DIALOG_ID = 0;
	/** Called when the activity is first created. */
    

    public String nave;
    public String portoPartenza;
    public String portoArrivo;
    public Date orario;
	private int mHour;
	private int mMinute;
	public ArrayAdapter<String> aalvMezzi;
	public ListView lvMezzi;
	public Button buttonMinusMinus;
	public Button buttonMinus;
	public Button buttonPlusPlus;
	public Button buttonPlus;
	public TextView txtOrario;
	
	
	private ArrayList<Mezzo> listMezzi;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // get the current time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        orario=new Date();
        
        txtOrario = (TextView)findViewById(R.id.txtOrario);
        txtOrario.setText("Dalle "+orario.getHours()+":"+orario.getMinutes());
        
        buttonMinusMinus = (Button)findViewById(R.id.button1);    
        buttonMinusMinus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		orario.setHours(orario.getHours()-1);
        		txtOrario.setText("Dalle "+orario.getHours()+":"+orario.getMinutes());
        		aggiornaLista();
        	}
        });
        
        buttonMinus = (Button)findViewById(R.id.button2);    
        buttonMinus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		orario.setMinutes(orario.getMinutes()-1);
        		txtOrario.setText("Dalle "+orario.getHours()+":"+orario.getMinutes());
        		aggiornaLista();
        	}
        });
        
        buttonPlus = (Button)findViewById(R.id.button3);    
        buttonPlus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		orario.setMinutes(orario.getMinutes()+1);
        		txtOrario.setText("Dalle "+orario.getHours()+":"+orario.getMinutes());
        		aggiornaLista();
        	}
        });
        
        buttonPlusPlus = (Button)findViewById(R.id.button4);    
        buttonPlusPlus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		orario.setHours(orario.getHours()+1);
        		txtOrario.setText("Dalle "+orario.getHours()+":"+orario.getMinutes());
        		aggiornaLista();
        	}
        });
        setSpinner();

        
        listMezzi = new ArrayList <Mezzo>();
        lvMezzi=(ListView)findViewById(R.id.listMezzi);
        aalvMezzi = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);       
        lvMezzi.setAdapter(aalvMezzi);
        
        riempiLista();
        aggiornaLista();

    }
    
    private void riempiLista() {
		listMezzi.add(new Mezzo("Aliscafo Caremar",8,10,8,25,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Traghetto Caremar",9,10,9,45,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Traghetto Caremar",12,10,12,40,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Traghetto Caremar",18,35,19,00,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Traghetto Caremar",10,20,10,55,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",13,50,14,20,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",19,15,19,45,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",8,55,9,15,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",7,30,8,10,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",8,50,9,30,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",11,45,12,25,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",13,10,13,50,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",15,10,15,50,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",17,30,18,10,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",18,15,18,55,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",0,15,1,15,"Napoli Porta di Massa","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",6,25,7,25,"Napoli Porta di Massa","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",9,10,10,10,"Napoli Porta di Massa","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",10,45,11,45,"Napoli Porta di Massa","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",15,15,16,15,"Napoli Porta di Massa","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",17,45,18,45,"Napoli Porta di Massa","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",19,30,20,30,"Napoli Porta di Massa","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",22,15,23,15,"Napoli Porta di Massa","Procida"));
		listMezzi.add(new Mezzo("Traghetto Caremar",2,20,3,20,"Procida","Napoli Porta di Massa"));
		listMezzi.add(new Mezzo("Traghetto Caremar",7,40,8,40,"Procida","Napoli Porta di Massa"));
		listMezzi.add(new Mezzo("Traghetto Caremar",13,35,14,35,"Procida","Napoli Porta di Massa"));
		listMezzi.add(new Mezzo("Traghetto Caremar",14,35,15,35,"Procida","Napoli Porta di Massa"));
		listMezzi.add(new Mezzo("Traghetto Caremar",16,15,17,15,"Procida","Napoli Porta di Massa"));
		listMezzi.add(new Mezzo("Traghetto Caremar",18,5,19,0,"Procida","Napoli Porta di Massa"));
		listMezzi.add(new Mezzo("Traghetto Caremar",20,30,21,30,"Procida","Napoli Porta di Massa"));
		listMezzi.add(new Mezzo("Traghetto Caremar",22,55,23,55,"Procida","Napoli Porta di Massa"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",6,35,7,15,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",7,55,8,35,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",9,25,10,5,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",10,35,11,15,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",13,30,14,10,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",14,55,15,35,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo Caremar",16,55,17,35,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Gestur",6,50,7,30,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Gestur",9,40,10,20,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Gestur",11,30,12,10,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Gestur",14,5,14,45,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Gestur",17,5,17,45,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Gestur",8,25,9,5,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Gestur",10,40,11,20,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Gestur",13,0,13,40,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Gestur",15,30,16,10,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Gestur",17,55,18,35,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",8,25,9,0,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",12,20,12,55,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",16,20,16,55,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",19,0,19,35,"Napoli Beverello","Procida"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",7,30,8,5,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",10,10,10,45,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",14,15,14,50,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Aliscafo SNAV",18,5,18,40,"Procida","Napoli Beverello"));
		listMezzi.add(new Mezzo("Medmar",4,10,4,50,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Medmar",20,30,21,10,"Pozzuoli","Procida"));
		listMezzi.add(new Mezzo("Medmar",3,10,3,50,"Procida","Pozzuoli"));
		listMezzi.add(new Mezzo("Medmar",19,40,20,20,"Procida","Pozzuoli"));
		
		//TODO To be continued
	}

	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
        }
        return null;
    }
    
    // the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                orario.setHours(hourOfDay);
                orario.setMinutes(minute);
            }
        };
        
        
    public void aggiornaLista() {
		// TODO Qui tutto l'algoritmo
    	ArrayList <Mezzo> selectMezzi= new ArrayList <Mezzo>();
    	
        Comparator<Mezzo> comparator = new Comparator<Mezzo>() {
    		@Override
    		public int compare(Mezzo m1, Mezzo m2) {
    			if(m1.oraArrivo.before(m2.oraArrivo))
    				return -1;
    			else if (m1.oraArrivo.after(m2.oraArrivo))
    				return 1;
    			else
    				return 0;
    		}
    		};
    	
    	aalvMezzi.clear();
    	//qui riempio aalvMezzi in base agli input e ai dati di listMezzi
    	for (int i=0;i<listMezzi.size();i++){
    		//per ogni mezzo valuta se ci interessa
    		if (listMezzi.get(i).nave.equals(nave) || nave.equals("Tutti")){
    			if (listMezzi.get(i).portoPartenza.equals(portoPartenza)  || portoPartenza.equals("Tutti")){
    				if (listMezzi.get(i).portoArrivo.equals(portoArrivo)  || portoArrivo.equals("Tutti")){
    					if (listMezzi.get(i).oraPartenza.before(orario)){
    							selectMezzi.add(listMezzi.get(i));
    						}
        				}
    			}
    		}
	
    				
    	}
    	Collections.sort(selectMezzi,comparator);
		for (int i=0;i<selectMezzi.size();i++){
			aalvMezzi.add(selectMezzi.get(i).nave+" - "+selectMezzi.get(i).portoPartenza+" - "+selectMezzi.get(i).portoArrivo+" - "+selectMezzi.get(i).oraPartenza.getHours()+":"+selectMezzi.get(i).oraPartenza.getMinutes());
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