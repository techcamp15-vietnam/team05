package com.techcamp05.hanoiplaces;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * Default Notification handler class for receiving ContentHandler
 * events raised by the SAX Parser
 * @author Huy Phung
 * */
public class XParser extends DefaultHandler {

	ArrayList<String> labelList = new ArrayList<String>();
	ArrayList<Double> lonList = new ArrayList<Double>();
	ArrayList<Double> latList = new ArrayList<Double>();
	ArrayList<String> uriList = new ArrayList<String>();
	ArrayList<String> numList = new ArrayList<String>();
	ArrayList<String> streetList = new ArrayList<String>();
	ArrayList<String> descList = new ArrayList<String>();
	ArrayList<String> imgList = new ArrayList<String>();
	ArrayList<String> catList = new ArrayList<String>();

	// temp variable to store the data chunk read while parsing
	private String tempStore = null;

	/*
	 * DefaultConstructor
	 */
	public XParser() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Clears the tempStore variable on every start of the element notification
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);

		if (localName.equalsIgnoreCase("label")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("lon")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("lat")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("uri")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("num")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("street")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("desc")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("img")) {
			tempStore = "";
		} else if (localName.equalsIgnoreCase("cat")) {
			tempStore = "";
		} else {
			tempStore = "";
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);

		if (localName.equalsIgnoreCase("label")) {
			labelList.add(tempStore);
		} else if (localName.equalsIgnoreCase("lon")) {
			lonList.add(Double.parseDouble(tempStore));
		} else if (localName.equalsIgnoreCase("lat")) {
			latList.add(Double.parseDouble(tempStore));
		} else if (localName.equalsIgnoreCase("uri")) {
			uriList.add(tempStore);
		} else if (localName.equalsIgnoreCase("num")) {
			numList.add(tempStore);
		} else if (localName.equalsIgnoreCase("street")) {
			streetList.add(tempStore);
		} else if (localName.equalsIgnoreCase("desc")) {
			descList.add(tempStore);
		} else if (localName.equalsIgnoreCase("img")) {
			imgList.add(tempStore);
		} else if (localName.equalsIgnoreCase("cat")) {
			catList.add(tempStore);
		}
		tempStore = "";
	}

	/*
	 * adds the incoming data chunk of character data to the temp data variable
	 * - tempStore
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		tempStore += new String(ch, start, length);
	}

}
