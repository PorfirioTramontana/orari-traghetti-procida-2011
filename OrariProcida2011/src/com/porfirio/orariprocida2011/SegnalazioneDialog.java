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
	
	//Aggiunta anche la possibilità di confermare (con un extra button)

	private String scriviSegnalazione(boolean problema){
		HttpClient Client = new DefaultHttpClient();
        String URL = "http://unoprocidaresidente.altervista.org/segnala.php?data=";
        //Pare funzionino le segnalazioni relative al giorno successivo!        
        if (mezzo.getGiornoSeguente())
        	orarioRef.add(Calendar.DAY_OF_YEAR, 1);
        URL+=orarioRef.get(Calendar.DAY_OF_MONTH)+","+(1+orarioRef.get(Calendar.MONTH))+","+orarioRef.get(Calendar.YEAR)+"&s=";
        if (mezzo.getGiornoSeguente()) //rimettiamo a posto!
        	orarioRef.add(Calendar.DAY_OF_YEAR, -1);
        try {
			URL+=URLEncoder.encode((mezzo.nave+","+mezzo.oraPartenza.get(Calendar.HOUR_OF_DAY)+","+mezzo.oraPartenza.get(Calendar.MINUTE)
			   +","+mezzo.oraArrivo.get(Calendar.HOUR_OF_DAY)+","+mezzo.oraArrivo.get(Calendar.MINUTE)+","
			   +mezzo.portoPartenza+","+mezzo.portoArrivo+","
			   +mezzo.inizioEsclusione.get(Calendar.DAY_OF_MONTH)+","+mezzo.inizioEsclusione.get(Calendar.MONTH)+","+mezzo.inizioEsclusione.get(Calendar.YEAR)+","
			   +mezzo.fineEsclusione.get(Calendar.DAY_OF_MONTH)+","+mezzo.fineEsclusione.get(Calendar.MONTH)+","+mezzo.fineEsclusione.get(Calendar.YEAR)+","
			   +mezzo.giorniSettimana), "UTF-8");
			if (problema)
				URL=URL+"&motivo="+ragione+"&dettagli="+URLEncoder.encode(txtDettagli.getText().toString(), "UTF-8");	
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
	
//	private void scriviSegnalazione(){
//		FileOutputStream fos = null;
//		try {
//			fos = callingContext.openFileOutput("segnalazioni.csv", Context.MODE_WORLD_WRITEABLE);
//		} catch (FileNotFoundException e) {
//			// 
//			e.printStackTrace();
//		}
//		try {
//			fos.write((new String("prova,prova")).getBytes());		
//			fos.write("\n".getBytes());
//			fos.close();		
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
//		ftpConnect("wpage.unina.it","ptramont","nabla2nabla2",21);
//		ftpUpload("segnalazioni.csv","segnalazioni.csv","ptramont");
//		
//		return;
//	}
//	
//
//	public boolean ftpConnect(String host, String username,
//	                          String password, int port)
//	{
//	    try {
//	        mFTPClient = new FTPClient();
//	        // connecting to the host
//	        mFTPClient.connect(host, port);
//
//	        // now check the reply code, if positive mean connection success
//	        if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
//	            // login using username & password
//	            boolean status = mFTPClient.login(username, password);
//
//	            /* Set File Transfer Mode
//	             *
//	             * To avoid corruption issue you must specified a correct
//	             * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
//	             * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
//	             * for transferring text, image, and compressed files.
//	             */
//	            mFTPClient.setFileType(FTP.ASCII_FILE_TYPE);
//	            mFTPClient.enterLocalPassiveMode();
//
//	            return status;
//	        }
//	    } catch(Exception e) {
//	        Log.i("TAG", "Error: could not connect to host " + host );
//	    }
//
//	    return false;
//	}
//	
//	public boolean ftpChangeDirectory(String directory_path)
//	{
//	    try {
//	        return mFTPClient.changeWorkingDirectory(directory_path);
//	    } catch(Exception e) {
//	        Log.i("TAG", "Error: could not change directory to " + directory_path);
//	    }
//
//	    return false;
//	}
	
	/**
	 * mFTPClient: FTP client connection object (see FTP connection example)
	 * srcFilePath: source file path in sdcard
	 * desFileName: file name to be stored in FTP server
	 * desDirectory: directory path where the file should be upload to
	 */
//	public boolean ftpUpload(String srcFilePath, String desFileName,
//	                         String desDirectory)
//	{
//	    boolean status = false;
//	    try {
//	        FileInputStream srcFileStream = new FileInputStream(srcFilePath);
//
//	        // change working directory to the destination directory
//	        if (ftpChangeDirectory(desDirectory)) {
//	            status = mFTPClient.storeFile(desFileName, srcFileStream);
//	        }
//
//	        srcFileStream.close();
//	        return status;
//	    } catch (Exception e) {
//	        Log.i("TAG", "upload failed");
//	    }
//
//	    return status;
//	}

	 
//    private void scriviSegnalazione(){
//    	String postMessage="prova,prova";
//    	int TIMEOUT_MILLISEC = 60000;  // = 10 seconds
//    	HttpParams httpParams = new BasicHttpParams();
//    	HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
//    	HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
//    	HttpClient client = new DefaultHttpClient(httpParams);
//
//    	HttpPost request = new HttpPost("http://wpage.unina.it/ptramont/segnalazioni.csv");
//    	try {
//			request.setEntity(new ByteArrayEntity(postMessage.toString().getBytes("UTF8")));
//		} catch (UnsupportedEncodingException e) {
//			
//			e.printStackTrace();
//		}
//    	HttpResponse response = null;
//    	try {
//			response = client.execute(request);
//		} catch (ClientProtocolException e) {
//			
//			e.printStackTrace();
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
//    	Log.i("POSTCSV",response.toString());
//    	
//    	return;
//    }

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
