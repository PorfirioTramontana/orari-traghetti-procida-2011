package com.porfirio.orariprocida2011;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler; 

import android.util.Log;

public class MeteoXMLHandler extends DefaultHandler{

	    StringBuffer buff = null;
	    boolean buffering = false;
		private OrariProcida2011Activity callingActivity; 
	    
		public MeteoXMLHandler(OrariProcida2011Activity orariProcida2011Activity) {
			callingActivity=orariProcida2011Activity;
		}

		@Override
	    public void startDocument() throws SAXException {
//	        Log.d("ORARI","Start document");
	    } 
	    
	    @Override
	    public void endDocument() throws SAXException {
//	    	Log.d("ORARI","End document");
	    } 
	    
	    @Override
	    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
//	    	Log.d("ORARI","Start element");
	    	if (localName.equals("wind_condition")) {
	    		/** Get attribute value */
	    		String attr = attributes.getValue("data");
	    		setMeteo(attr);
	    		Log.d("ORARI", attr);
	    	}

	    }

	    private void setMeteo(String attr) {
	    	if (attr.contains(" N "))
				callingActivity.meteo.setWindDirection(0);
	    	else if (attr.contains(" NE "))
	    		callingActivity.meteo.setWindDirection(45);
	    	else if (attr.contains(" E "))
	    		callingActivity.meteo.setWindDirection(90);
	    	else if (attr.contains(" SE "))
	    		callingActivity.meteo.setWindDirection(135);
	    	else if (attr.contains(" S "))
	    		callingActivity.meteo.setWindDirection(180);
	    	else if (attr.contains(" SW "))
	    		callingActivity.meteo.setWindDirection(225);
	    	else if (attr.contains(" W "))
	    		callingActivity.meteo.setWindDirection(270);
	    	else if (attr.contains(" NW "))
	    		callingActivity.meteo.setWindDirection(315);
			
	    	Log.d("ORARI","Vento da "+callingActivity.meteo.getWindDirection());	    	
	    	
	    	//TODO gestire anche i kmh
	    	int pos= attr.indexOf(" at ");
	    	int pos2=attr.indexOf("mph");
	    	String wind=attr.substring(pos+4, pos2-1);
	    	double wkmh=Integer.parseInt(wind)*1.609;
	    	if (wkmh<=1)
	    		callingActivity.meteo.setWindBeaufort(0.0);
	    	else if (wkmh>1 && wkmh<=5)
	    		callingActivity.meteo.setWindBeaufort(1+(wkmh-3)/(5-1));
	    	else if (wkmh>=6 && wkmh<=11)
	    		callingActivity.meteo.setWindBeaufort(2+(wkmh-8.5)/(11-6));
	    	else if (wkmh>=12 && wkmh<=19)
	    		callingActivity.meteo.setWindBeaufort(3+(wkmh-15.5)/(19-12));
	    	else if (wkmh>=20 && wkmh<=28)
	    		callingActivity.meteo.setWindBeaufort(4+(wkmh-24)/(28-20));
	    	else if (wkmh>=29 && wkmh<=38)
	    		callingActivity.meteo.setWindBeaufort(5+(wkmh-33.5)/(38-29));
	    	else if (wkmh>=39 && wkmh<=49)
	    		callingActivity.meteo.setWindBeaufort(6+(wkmh-44)/(49-39));
	    	else if (wkmh>=50 && wkmh<=61)
	    		callingActivity.meteo.setWindBeaufort(7+(wkmh-55.5)/(61-50));
	    	else if (wkmh>=62 && wkmh<=74)
	    		callingActivity.meteo.setWindBeaufort(8+(wkmh-68)/(74-62));
	    	else if (wkmh>=75 && wkmh<=88)
	    		callingActivity.meteo.setWindBeaufort(9+(wkmh-81.5)/(88-75));
	    	else if (wkmh>=89 && wkmh<=102)
	    		callingActivity.meteo.setWindBeaufort(10+(wkmh-95.5)/(102-89));
	    	else if (wkmh>=103 && wkmh<=117)
	    		callingActivity.meteo.setWindBeaufort(11+(wkmh-110)/(117-103));
	    	else if (wkmh>=118)
	    		callingActivity.meteo.setWindBeaufort(12);
	    	Log.d("ORARI","Vento forza "+callingActivity.meteo.getWindBeaufort());
		}

		@Override
	    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
//	    	Log.d("ORARI","End element");
	    }

//	    /** Called to get tag characters ( ex:- <name>AndroidPeople</name>
//	    * -- to get AndroidPeople Character ) */
//	    @Override
//	    public void characters(char[] ch, int start, int length)
//	    throws SAXException {
//
//	    if (currentElement) {
//	    currentValue = new String(ch, start, length);
//	    currentElement = false;
//	    }
//
//	    }

}
