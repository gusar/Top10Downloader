package org.example.top10downloader;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class ParseApplications {

	private String data;
	private ArrayList<Application> applications;
	
	public ParseApplications(String xmlData) {
		data = xmlData;
		applications = new ArrayList<Application>();
	}

	public ArrayList<Application> getApplications() {
		return applications;
	}
	
	public boolean process() {
		boolean operationStatus = true;
		Application currentRecord = null;
		boolean inEntry = false;
		String textValue = "";
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			xpp.setInput(new StringReader(this.data));
			int eventType = xpp.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = xpp.getName();
				
				if(eventType == XmlPullParser.START_TAG) {
					// Create new application instance
					if(tagName.equalsIgnoreCase("entry")) {
						inEntry = true;
						currentRecord = new Application();
					}
					
				} else if(eventType == XmlPullParser.TEXT) {
					textValue = xpp.getText();					
					
				} else if(eventType == XmlPullParser.END_TAG) {
					if(inEntry) {
						// Add app as new array list entry
						if(tagName.equalsIgnoreCase("entry")) {
							applications.add(currentRecord);
							inEntry = false;
						}
						// Extract app details
						if(tagName.equalsIgnoreCase("name")) {
							currentRecord.setName(textValue);
						} else if(tagName.equalsIgnoreCase("artist")) {
							currentRecord.setArtist(textValue);
						} else if(tagName.equalsIgnoreCase("releaseDate")) {
							currentRecord.setReleaseDate(textValue);
						}
						
					}
					
				} // end while
				
				eventType = xpp.next();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			operationStatus = false;
		}
		
		// Validate data
		for(Application app : applications) {
			Log.d("LOG", "***************");
			Log.d("LOG", app.getName());
			Log.d("LOG", app.getArtist());
			Log.d("LOG", app.getReleaseDate());
		}
		
		return operationStatus;
	}
	
}
