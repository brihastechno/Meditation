package com.xmlparse.meditation;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MeditationHandler extends DefaultHandler {
	// members
	private String ID;
	private ArrayList<String> meditation_array;
	private ArrayList<String> meditation_store_array;
	private ArrayList<String> meditation_list_array;

	// 'Lesson' entity to parse
	private Meditation mMeditation;

	// 'getter' is enough
	public Meditation getMeditation() {
		return mMeditation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		// create new object
		mMeditation = new Meditation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		// nothing we need to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		// if this element value equals "Lesson_name"
		if (localName.equals(Meditation.MEDITATION)) {
			// get id right away
			meditation_array = new ArrayList<String>();
			ID = atts.getValue("id");
			meditation_array.add(atts.getValue("authorname"));
			meditation_array.add(atts.getValue("authorimage"));
			meditation_array.add(atts.getValue("title"));
			meditation_array.add(atts.getValue("name"));
			meditation_array.add(atts.getValue("backgroundFile"));
			meditation_array.add(atts.getValue("videoFile"));
			mMeditation.meditation_app_list.put(ID, meditation_array);

		}
		// if this element value equals "ID"
		else if (localName.equals(Meditation.STORE_APP)) {
			meditation_store_array = new ArrayList<String>();
			meditation_store_array.add(atts.getValue(Meditation.ID));
			meditation_store_array.add(atts.getValue(Meditation.APP_NAME));
			meditation_store_array.add(atts.getValue(Meditation.APP_TITLE));
			meditation_store_array.add(atts.getValue(Meditation.APP_URL));
			meditation_store_array.add(atts.getValue(Meditation.APP_AUTHOR));
			meditation_store_array.add(atts.getValue(Meditation.APP_THUMBNAIL));

			mMeditation.meditation_store_list.put(
					ID + "_" + atts.getValue(Meditation.ID),
					meditation_store_array);
			// isMeditations = true;
		}

		// if this element value equals "ID"
		else if (localName.equals(Meditation.Med)) {
			meditation_list_array = new ArrayList<String>();
			meditation_list_array.add(atts.getValue(Meditation.ID));
			meditation_list_array.add(atts.getValue(Meditation.MEDI_NAME));
			meditation_list_array.add(atts.getValue(Meditation.MEDI_LOGO));
			meditation_list_array.add(atts.getValue(Meditation.MEDI_AUDIO));
			meditation_list_array.add(atts.getValue(Meditation.MEDI_VIDEO));

			mMeditation.meditation_medi_list.put(
					ID + "_" + atts.getValue(Meditation.ID),
					meditation_list_array);
			// isMeditations = true;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		if (localName.equals(Meditation.MEDITATION)) {
			// uncheck marking
			// isMeditation = false;
		}
		// if this element value equals "Lesson_cost"
		else if (localName.equals(Meditation.STORE_APP)) {
			// uncheck marking
			// isStore = false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		// get all text value inside the element tag
		String chars = new String(ch, start, length);
		chars = chars.trim(); // remove all white-space characters
		//
		// // if this tag is "Lesson_name", set "Lesson_name" value
		// if (isName)
		// mMeditation.mName.add(chars);
		// // if this tag is "ID", set "ID" value
		// else if (isId)
		// mMeditation.mId.add(chars);
		// // if this tag is "Lesson_url", set "Lesson_url" value
		// else if (isUrl)
		// mMeditation.mUrl.add(chars);
		// // if this tag is "Lesson_thumbnail", set "Lesson_thumbnail" value
		// else if (isThumnail)
		// mMeditation.mThumnail.add(chars);
		// // if this tag is "Lesson_cost", set "Lesson_cost" value
		// else if (isCost)
		// mMeditation.mCost.add(chars);
	}
}
