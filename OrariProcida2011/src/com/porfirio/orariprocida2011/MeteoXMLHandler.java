package com.porfirio.orariprocida2011;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler; 

import android.util.Log;

public class MeteoXMLHandler extends DefaultHandler{

	    StringBuffer buff = null;
	    boolean buffering = false; 
	    
	    @Override
	    public void startDocument() throws SAXException {
	        Log.d("ORARI","Start document");
	    } 
	    
	    @Override
	    public void endDocument() throws SAXException {
	    	Log.d("ORARI","End document");
	    } 
	    
	    @Override
	    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	    	Log.d("ORARI","Start element");
	    	if (localName.equals("wind_condition")) {
	    		/** Get attribute value */
	    		String attr = attributes.getValue("data");
	    		Log.d("ORARI", attr);
	    	}

	    }

	    @Override
	    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	    	Log.d("ORARI","End element");
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
