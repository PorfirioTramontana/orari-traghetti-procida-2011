package com.porfirio.orariprocida2011;

//versione 1.3 per Android Market 

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

public class OrariProcida2011Activity extends Activity {
	/** Called when the activity is first created. */
    

    public String nave;
    public String portoPartenza;
    public String portoArrivo;
    public Calendar c;
    public Calendar aggiornamentoOrariWeb;
    public Calendar aggiornamentoOrariIS;
	public ArrayAdapter<String> aalvMezzi;
	public ListView lvMezzi;
	public Button buttonMinusMinus;
	public Button buttonMinus;
	public Button buttonPlusPlus;
	public Button buttonPlus;
	public TextView txtOrario;
	public AlertDialog aboutDialog;
	public ConfigData configData; 
	private DettagliMezzoDialog dettagliMezzoDialog;
	private ArrayList <Mezzo> selectMezzi;
	
    static final int ABOUT_DIALOG_ID = 0;

	private ArrayList<Mezzo> listMezzi;
	private ArrayList<Compagnia> listCompagnia;
	private LocationManager myManager;
	private Criteria criteria;
	private String BestProvider;
	private boolean updateWeb=true; //capacità di fare l'upload degli orari da Web: impostata a true

    
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
        case R.id.finTemp:
            FinestraDialog finestraDialog = new FinestraDialog(this,configData);
    		finestraDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                        aggiornaLista();
                        return;
                }
    		});        
        	finestraDialog.show();
        	return true;
        case R.id.updateWeb:
        	if (updateWeb){
        		updateWeb=false;
        		item.setTitle(getResources().getString(R.string.updateWeb));
        		return true;
        	}
        	else {
        		updateWeb=true;
        		item.setTitle(getResources().getString(R.string.noUpdateWeb));
        		if (isOnline())
        			riempiMezzidaWeb();
        		else
        			Log.d("ORARI", "Non c'è la connessione: non carico orari da Web");
        		return true;
        	}
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
        Log.d("ACTIVITY","create");
        setContentView(R.layout.main);

        myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        BestProvider = myManager.getBestProvider(criteria, true);        
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);      
        builder.setMessage("Gli orari sono quelli resi noti dalle compagnie di navigazione alle biglietterie o sui loro siti web. L'autore non e' in alcun modo responsabile di ogni eventuale loro cambiamento. By Porfirio Tramontana 2011. In licenza GPL3. ")
               .setCancelable(false)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        aboutDialog = builder.create();
               
        configData=new ConfigData();
        configData.setFinestraTemporale(24);
        
        // get the current time
        
        c = Calendar.getInstance(TimeZone.getDefault());
        
        txtOrario = (TextView)findViewById(R.id.txtOrario);
        
		setTxtOrario(c);
        
        buttonMinusMinus = (Button)findViewById(R.id.button1);    
        buttonMinusMinus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		
//        		orario.setHours(orario.getHours()-1);
        		c.add(Calendar.HOUR, -1);
        		setTxtOrario(c);
        		aggiornaLista();
        	}
        });
        
        buttonMinus = (Button)findViewById(R.id.button2);    
        buttonMinus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
//        		orario.setMinutes(orario.getMinutes()-15);
        		c.add(Calendar.MINUTE, -15);
        		setTxtOrario(c);
        		aggiornaLista();
        	}
        });
        
        buttonPlus = (Button)findViewById(R.id.button3);    
        buttonPlus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
//        		orario.setMinutes(orario.getMinutes()+15);
        		c.add(Calendar.MINUTE, 15);
        		setTxtOrario(c);
        		aggiornaLista();
        	}
        });
        
        buttonPlusPlus = (Button)findViewById(R.id.button4);    
        buttonPlusPlus.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
//        		orario.setHours(orario.getHours()+1);
        		c.add(Calendar.HOUR, 1);
        		setTxtOrario(c);
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

	private void setTxtOrario(Calendar c) {
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
	}
    
	private void riempiMezzidaInternalStorage(FileInputStream fstream){
		try{
			  // Open the file that is the first 
			  // command line parameter
			  Log.d("ORARI", "Inizio caricamento orari da IS");
			  
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String rigaAggiornamento=br.readLine();
  			  StringTokenizer st0 = new StringTokenizer( rigaAggiornamento, "," );			
			  aggiornamentoOrariIS=Calendar.getInstance(TimeZone.getDefault());
			  aggiornamentoOrariIS.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st0.nextToken()));
			  aggiornamentoOrariIS.set(Calendar.MONTH, Integer.parseInt(st0.nextToken()));
			  aggiornamentoOrariIS.set(Calendar.YEAR, Integer.parseInt(st0.nextToken()));
			  aboutDialog.setMessage("Gli orari sono quelli resi noti dalle compagnie di navigazione alle biglietterie o sui loro siti web. L'autore non e' in alcun modo responsabile di ogni eventuale loro cambiamento. Orari aggiornati al "+aggiornamentoOrariIS.get(Calendar.DAY_OF_MONTH)+"/"+aggiornamentoOrariIS.get(Calendar.MONTH)+"/"+aggiornamentoOrariIS.get(Calendar.YEAR)+". By Porfirio Tramontana 2011. In licenza GPL3. http://code.google.com/p/orari-traghetti-procida-2011/");
			  
			  for (String line = br.readLine(); line != null; line = br.readLine())
				{
				  //esamino la riga e creo un mezzo
					StringTokenizer st = new StringTokenizer( line, "," );
					listMezzi.add(new Mezzo(st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()));
				}

			  //Close the input stream
			  in.close();
			  Log.d("ORARI", "Fine caricamento orari da IS");
			  String str=new String("Orari aggiornati al "+aggiornamentoOrariIS.get(Calendar.DAY_OF_MONTH)+"/"+aggiornamentoOrariIS.get(Calendar.MONTH)+"/"+aggiornamentoOrariIS.get(Calendar.YEAR));
			  Log.d("ORARI", str);
			  Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);

			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }	
	}
	
	private void riempiMezzidaWeb(){
		Log.d("ORARI", "Inizio Lettura da Web");
		String url="http://wpage.unina.it/ptramont/orari.csv";
		HttpURLConnection conn = null;
		InputStream in=null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in = conn.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		try {
			String rigaAggiornamento=r.readLine();
			StringTokenizer st0 = new StringTokenizer( rigaAggiornamento, "," );			
			aggiornamentoOrariWeb=(Calendar) aggiornamentoOrariIS.clone();
			aggiornamentoOrariWeb.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st0.nextToken()));
			aggiornamentoOrariWeb.set(Calendar.MONTH, Integer.parseInt(st0.nextToken()));
			aggiornamentoOrariWeb.set(Calendar.YEAR, Integer.parseInt(st0.nextToken()));
		    String str=new String("Orari Web aggiornati al "+aggiornamentoOrariWeb.get(Calendar.DAY_OF_MONTH)+"/"+aggiornamentoOrariWeb.get(Calendar.MONTH)+"/"+aggiornamentoOrariWeb.get(Calendar.YEAR));
		    aboutDialog.setMessage("Gli orari sono quelli resi noti dalle compagnie di navigazione alle biglietterie o sui loro siti web. L'autore non e' in alcun modo responsabile di ogni eventuale loro cambiamento. Orari aggiornati al "+aggiornamentoOrariIS.get(Calendar.DAY_OF_MONTH)+"/"+aggiornamentoOrariIS.get(Calendar.MONTH)+"/"+aggiornamentoOrariIS.get(Calendar.YEAR)+". By Porfirio Tramontana 2011. In licenza GPL3. http://code.google.com/p/orari-traghetti-procida-2011/");
			Log.d("ORARI", str);
			Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);

			if (aggiornamentoOrariWeb.after(aggiornamentoOrariIS)){
				Log.d("ORARI", "GLi orari dal Web sono più aggiornati");
				FileOutputStream fos = openFileOutput("orari.csv", Context.MODE_WORLD_WRITEABLE);
				fos.write(rigaAggiornamento.getBytes());
				fos.write("\n".getBytes());
				listMezzi.clear();
				for (String line = r.readLine(); line != null; line = r.readLine())
				{
					//esamino la riga e creo un mezzo
					StringTokenizer st = new StringTokenizer( line, "," );
					listMezzi.add(new Mezzo(st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()));
					fos.write(line.getBytes());
					fos.write("\n".getBytes());
				}
				fos.close();
				aggiornamentoOrariIS=aggiornamentoOrariWeb;
			}
			r.close();
			Log.d("ORARI", "Orari web letti");
			Toast.makeText(getApplicationContext(), "Aggiornamento degli orari letto da Web", Toast.LENGTH_LONG);
			Log.d("ORARI", "Orari IS aggiornati");
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}

	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
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

    	//TODO Aggiungere taxi
    	
    	try {
			FileInputStream fstream = new FileInputStream("/data/data/com.porfirio.orariprocida2011/files/orari.csv");			
    		riempiMezzidaInternalStorage(fstream);
		} catch (FileNotFoundException e) {
			Log.d("ORARI", "File non trovato su IS. Leggo da codice");
		   	// convenzione giorni settimana:
			// DOMENICA =1 LUNEDI=2 MARTEDI=3 MERCOLEDI=4 GIOVEDI=5 VENERDI=6 SABATO=7
		    	
				aggiornamentoOrariIS=Calendar.getInstance(TimeZone.getDefault());		
			    aggiornamentoOrariIS.set(2011, 11, 1); //Orari aggiornato all'1/11/2011
			    aboutDialog.setMessage("Gli orari sono quelli resi noti dalle compagnie di navigazione alle biglietterie o sui loro siti web. L'autore non e' in alcun modo responsabile di ogni eventuale loro cambiamento. Orari aggiornati al "+aggiornamentoOrariIS.get(Calendar.DAY_OF_MONTH)+"/"+aggiornamentoOrariIS.get(Calendar.MONTH)+"/"+aggiornamentoOrariIS.get(Calendar.YEAR)+". By Porfirio Tramontana 2011. In licenza GPL3. http://code.google.com/p/orari-traghetti-procida-2011/");

//		    	listMezzi.add(new Mezzo("Prova",0,20,8,25,"Prova","Prova",2,10,2011,6,10,2011,"1234567"));
//		    	listMezzi.add(new Mezzo("Prova-",0,20,8,25,"Prova","Prova",6,10,2011,7,10,2011,"1234567"));
//		    	listMezzi.add(new Mezzo("Prova1",0,40,8,25,"Prova","Prova",0,0,0,0,0,0,"12345"));
//		    	listMezzi.add(new Mezzo("Prova2",0,50,8,25,"Prova","Prova",0,0,0,0,0,0,"5"));
//		    	listMezzi.add(new Mezzo("Prova3",0,55,8,25,"Prova","Prova",0,0,0,0,0,0,"67"));
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
				listMezzi.add(new Mezzo("Aliscafo Caremar",8,50,9,30,"Napoli Beverello","Procida",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",11,45,12,25,"Napoli Beverello","Procida",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",13,10,13,50,"Napoli Beverello","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",15,10,15,50,"Napoli Beverello","Procida",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",17,30,18,10,"Napoli Beverello","Procida",1,11,2011,1,6,2012,"1234567"));
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
				listMezzi.add(new Mezzo("Aliscafo Caremar",7,55,8,35,"Procida","Napoli Beverello",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",9,25,10,5,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",10,35,11,15,"Procida","Napoli Beverello",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",13,30,14,10,"Procida","Napoli Beverello",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",14,55,15,35,"Procida","Napoli Beverello",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",16,55,17,35,"Procida","Napoli Beverello",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Gestur",6,50,7,30,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Gestur",9,40,10,20,"Procida","Pozzuoli",19,10,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Gestur",11,30,12,10,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Gestur",14,5,14,45,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Gestur",17,5,17,45,"Procida","Pozzuoli",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Gestur",8,25,9,5,"Pozzuoli","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Gestur",10,40,11,20,"Pozzuoli","Procida",19,10,2011,1,6,2012,"1234567"));
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
				listMezzi.add(new Mezzo("Medmar",5,0,5,20,"Procida","Ischia Porto",0,0,0,0,0,0,"23456"));
				listMezzi.add(new Mezzo("Medmar",21,20,21,40,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Medmar",2,30,2,50,"Ischia Porto","Procida",0,0,0,0,0,0,"23456"));
				listMezzi.add(new Mezzo("Medmar",6,25,6,45,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Medmar",10,35,10,55,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				
				listMezzi.add(new Mezzo("Traghetto Caremar",7,35,7,55,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",10,20,10,40,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",11,5,11,25,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",11,55,12,15,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",14,30,14,50,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",16,25,18,45,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",18,55,19,15,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",19,50,20,10,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",20,35,20,55,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",23,20,23,40,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				
				listMezzi.add(new Mezzo("Traghetto Caremar",7,0,7,20,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",8,30,8,50,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",11,30,11,50,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",12,55,13,15,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",13,55,14,15,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",15,30,15,50,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",17,25,17,45,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",18,0,18,20,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Traghetto Caremar",19,55,20,15,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				
				listMezzi.add(new Mezzo("Aliscafo Caremar",9,35,9,50,"Procida","Ischia Porto",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",12,30,12,45,"Procida","Ischia Porto",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",13,55,14,10,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",15,55,16,10,"Procida","Ischia Porto",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",19,0,19,15,"Procida","Ischia Porto",0,0,0,0,0,0,"1234567"));
				
				listMezzi.add(new Mezzo("Aliscafo Caremar",7,30,7,45,"Ischia Porto","Procida",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",10,10,10,25,"Ischia Porto","Procida",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",13,5,13,20,"Ischia Porto","Procida",1,11,2011,1,6,2012,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",14,30,14,45,"Ischia Porto","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo Caremar",16,30,16,45,"Ischia Porto","Procida",1,11,2011,1,6,2012,"1234567"));
				
				listMezzi.add(new Mezzo("Aliscafo SNAV",7,10,7,25,"Procida","Casamicciola",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo SNAV",9,45,10,0,"Procida","Casamicciola",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo SNAV",13,50,14,10,"Procida","Casamicciola",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo SNAV",17,40,17,55,"Procida","Casamicciola",0,0,0,0,0,0,"1234567"));		

				listMezzi.add(new Mezzo("Aliscafo SNAV",9,0,9,15,"Casamicciola","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo SNAV",13,15,13,30,"Casamicciola","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo SNAV",17,5,17,20,"Casamicciola","Procida",0,0,0,0,0,0,"1234567"));
				listMezzi.add(new Mezzo("Aliscafo SNAV",19,45,10,0,"Casamicciola","Procida",0,0,0,0,0,0,"1234567"));		
		}


		if (updateWeb){
			if (isOnline())
				riempiMezzidaWeb();
			else
				Log.d("ORARI", "Non c'è connessione: non carico orari da Web");
		}
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

		String portoPartenzaEspanso=new String(portoPartenza);
		if (portoPartenza.equals("Napoli"))
			portoPartenzaEspanso="Napoli Porta di Massa o Napoli Beverello";
		if (portoPartenza.equals("Napoli o Pozzuoli"))
			portoPartenzaEspanso="Napoli Porta di Massa o Napoli Beverello o Pozzuoli";
		if (portoPartenza.equals("Ischia"))
			portoPartenzaEspanso="Ischia Porto o Casamicciola";
		String portoArrivoEspanso=new String(portoArrivo);
		if (portoArrivo.equals("Napoli"))
			portoArrivoEspanso="Napoli Porta di Massa o Napoli Beverello";
		if (portoArrivo.equals("Napoli o Pozzuoli"))
			portoArrivoEspanso="Napoli Porta di Massa o Napoli Beverello o Pozzuoli";
		if (portoArrivo.equals("Ischia"))
			portoArrivoEspanso="Ischia Porto o Casamicciola";
		Calendar oraLimite=(Calendar) c.clone();
		oraLimite.add(Calendar.HOUR_OF_DAY, configData.getFinestraTemporale());

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
    			if ((listMezzi.get(i).portoPartenza.equals((portoPartenza))) || (portoPartenzaEspanso.contains(listMezzi.get(i).portoPartenza)) || (portoPartenza.equals("Tutti"))){
    				if ((listMezzi.get(i).portoArrivo.equals((portoArrivo))) || (portoArrivoEspanso.contains(listMezzi.get(i).portoArrivo)) || (portoArrivo.equals("Tutti"))){
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
        
        //portoPartenza=new String(adapter2.getItem(0).toString());
        portoPartenza=setPortoPartenza();
        if (!(portoPartenza.equals("Tutti")))
        	Toast.makeText(getApplicationContext(), "Secondo me, vuoi partire da "+portoPartenza, Toast.LENGTH_LONG).show();
        //trova il valore corretto nello spinner
        for (int i=0;i<spnPortoPartenza.getCount();i++){
        	if (adapter2.getItem(i).equals(portoPartenza)){
        		spnPortoPartenza.setSelection(i);
        	}
        }
        	       
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
        
        if (!(portoPartenza.contentEquals("Procida"))){
        	portoArrivo="Procida";
        }
        else{
        	portoArrivo=new String(adapter3.getItem(0).toString());
        }
        
        //trova il valore corretto nello spinner
        for (int i=0;i<spnPortoArrivo.getCount();i++){
        	if (adapter3.getItem(i).equals(portoArrivo)){
        		spnPortoArrivo.setSelection(i);
        	}
        }

        
        spnPortoArrivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	portoArrivo=parent.getItemAtPosition(pos).toString();
            	aggiornaLista();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });        
	}
      
	private String setPortoPartenza() {
		// Trova il porto più vicino a quello di partenza
		Location l=null;
		try {
			l = myManager.getLastKnownLocation(BestProvider);
			Log.d("ACTIVITY", "Posizione:"+l.getLongitude()+","+l.getLatitude());
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("ACTIVITY", "Problema con GPS");
		}
        if (l==null)
        	return new String("Tutti");
        //Coordinate angoli Procida
        if ((l.getLatitude()>40.7374)&&(l.getLatitude()<40.7733)&&(l.getLongitude()>13.9897)&&(l.getLongitude()<14.0325))
        	return new String ("Procida");
        //Coordinate angoli Isola d'Ischia
        if ((l.getLatitude()>40.6921)&&(l.getLatitude()<40.7626)&&(l.getLongitude()>13.8465)&&(l.getLongitude()<13.9722)){
        	//Isola d'Ischia
        	if (calcolaDistanza(l,13.9063,40.7496)>calcolaDistanza(l,13.9602,40.7319))
        		return new String ("Ischia");
        	else
        		return new String ("Casamicciola");
        }
      //Inserire coordinate Napoli (media porti) e Pozzuoli
      double distNapoli=calcolaDistanza(l,14.2575,40.84); Log.d("OrariProcida","d(Napoli)="+distNapoli);
      double distPozzuoli=calcolaDistanza(l,14.1179,40.8239); Log.d("OrariProcida","d(Pozzuoli)="+distPozzuoli);
      if (distPozzuoli<distNapoli){
    	  if (distPozzuoli<15000)
      		return new String ("Pozzuoli");
    	  else
    		return new String ("Napoli o Pozzuoli");
      }
      else { //TODO Inserire coordinate Porti Napoli
    	  if (distNapoli<15000){
    		  if (distNapoli>1000)
    			  return new String ("Napoli");
    		  else{
    	        	if (calcolaDistanza(l,14.2548,40.8376)<calcolaDistanza(l,14.2602,40.8424))
    	        		return new String ("Napoli Beverello");
    	        	else
    	        		return new String ("Napoli Porta di Massa");
    		  }    			  
    	  }        		
      	  else
      		return new String ("Napoli o Pozzuoli");    	  
      }
	}

	public double calcolaDistanza(Location location, double lon, double lat) {
		//calcola distanza da obiettivo
		double deltaLong=Math.abs(lon-location.getLongitude());
		double deltaLat=Math.abs(lat-location.getLatitude());
		double delta=(Math.sqrt(deltaLong*deltaLong+deltaLat*deltaLat));
		delta=delta*60*1852;		
		return Math.ceil(delta);
	}
	protected void onStart(){
		super.onStart();
		Log.d("ACTIVITY","start");
		
	}
    
    protected void onRestart(){
    	super.onRestart();
    	Log.d("ACTIVITY","restart");
    }

    protected void onResume(){
    	super.onResume();
    	Log.d("ACTIVITY","resume");
    }

    protected void onPause(){
    	super.onPause();
    	Log.d("ACTIVITY","pause");
    }

    protected void onStop(){
    	super.onStop();
    	Log.d("ACTIVITY","stop");
    }


    protected void onDestroy(){
    	super.onDestroy();
    	Log.d("ACTIVITY","destroy");
    }
}
