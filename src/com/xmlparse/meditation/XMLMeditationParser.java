package com.xmlparse.meditation;

/**
 * XMLLessonParser Responsible for All XML PARSING Using  SAXParser
 * @author lenovo
 */
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

public class XMLMeditationParser {

	public static Meditation parse(InputStream is) {
		Meditation meditation = null;
		try {
			// create a XMLReader from SAXParser
			XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
					.getXMLReader();
			// create a LessonHandler too
			MeditationHandler meditationHandler = new MeditationHandler();
			// apply handler to the XMLReader
			xmlReader.setContentHandler(meditationHandler);
			// the process starts
			xmlReader.parse(new InputSource(is));
			// get the target `Lesson`
			meditation = meditationHandler.getMeditation();

		} catch (Exception ex) {
			Log.d("XML", "Meditation: parse() failed");
		}

		// return lesson we found
		return meditation;
	}
}
