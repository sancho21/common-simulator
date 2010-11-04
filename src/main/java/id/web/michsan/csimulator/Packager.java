package id.web.michsan.csimulator;



import id.web.michsan.csimulator.util.StringHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Packager composes fields into a long string
 * @author Muhammad Ichsan (ichsan@gmail.com)
 * @since 1.0.0
 */
public class Packager {

	private final String code;
	private final List<Field> sortedFields;

	/**
	 * Constructor
	 * @param code Packager unique code
	 * @param fields Packager's fields
	 * @throws RuntimeException If fields is overlapping with each other
	 */
	public Packager(String code, List<Field> fields) throws RuntimeException {
		this.code = code;

		Collections.sort(fields);
		ensureValid(fields);
		this.sortedFields = fields;
	}

	/**
	 * Constructor
	 * @param code Packager unique code
	 * @param packagerLocation XML file which defines packager fields
	 * @throws SAXException If failed to load file
	 * @throws Exception Other exceptions
	 */
	public Packager(String code, String packagerLocation) throws SAXException, Exception  {
		this(code, loadFields(packagerLocation));
	}

	private static List<Field> loadFields(String packagerLocation) throws SAXException, IOException, Exception {
		List<Field> fields = new ArrayList<Field>();

		DocumentBuilderFactory domFactory =
		    DocumentBuilderFactory.newInstance();
		          domFactory.setNamespaceAware(true);
		    DocumentBuilder builder = domFactory.newDocumentBuilder();
		    Document doc = builder.parse(packagerLocation);

		    XPath xpath = XPathFactory.newInstance().newXPath();

		    // XPath Query for showing all nodes value
		    XPathExpression expr = xpath.compile("/packager/field");

		    Object result = expr.evaluate(doc, XPathConstants.NODESET);
		    NodeList nodes = (NodeList) result;
		    for (int i = 0; i < nodes.getLength(); i++) {
		    	NamedNodeMap attrs = nodes.item(i).getAttributes();
		    	String name = attrs.getNamedItem("name").getNodeValue();
		    	int index = Integer.parseInt(attrs.getNamedItem("index").getNodeValue());
		    	int length = Integer.parseInt(attrs.getNamedItem("length").getNodeValue());
		    	String description = attrs.getNamedItem("description").getNodeValue();
		    	boolean isLeftAligned = attrs.getNamedItem("leftAligned").getNodeValue().equals("true");
		    	char filler = attrs.getNamedItem("filler").getNodeValue().charAt(0);
		    	String validationRegex = attrs.getNamedItem("validationRegex").getNodeValue();
		    	if (validationRegex.isEmpty()) validationRegex = null;

		    	fields.add(new Field(name, index, length, description,
		    			isLeftAligned, filler, validationRegex));
		    }

		return fields;
	}

	/**
	 * Pack message which values are residing in a value holder. Values which are
	 * not registered in the packager fields will be ignored.
	 * @param valueHolder A holder which contains key-to-value list
	 * @return Packed message
	 */
	public String pack(Map<String, String> valueHolder) {
		Map<Field, String> fieldValues = new HashMap<Field, String>();

		for (Field field : sortedFields) {
			String value = valueHolder.get(field.getName());
			if (!field.isValid(value)) throw new IllegalArgumentException(
					"Value is not valid. Regex " + field.getValidationRegex() +
					" does not match with value " + value);

			fieldValues.put(field, value);
		}

		return render(fieldValues);
	}

	/**
	 * Render field values
	 * @param fieldValues
	 * @return Rendered field values
	 */
	private String render(Map<Field, String> fieldValues) {
		StringBuffer buffer = new StringBuffer();

		for (Field field : sortedFields) {
			String value = fieldValues.get(field);
			if (value == null) value = "";

			buffer.append(StringHelper.pad(
					value, field.getLength(),
					field.isLeftAligned(), field.getFiller()));
		}

		return buffer.toString();
	}

	/**
	 * Unpack the packed message
	 * @param packedMessage Packed message
	 * @param valueHolder Holder to put unpacked values into
	 */
	public void unpack(String packedMessage, Map<String, String> valueHolder) {
		int begin = 0, end;
		for (Field field : sortedFields) {
			end = begin + field.getLength();
			valueHolder.put(field.getName(), packedMessage.substring(begin, end));
			begin = end;
		}
	}

	public String getCode() {
		return code;
	}

	public List<Field> getSortedFields() {
		return sortedFields;
	}

	private void ensureValid(List<Field> sortedFields) {
		int nextStart = 0;
		for (Field field : sortedFields) {
			if (field.getIndex() != nextStart)
				throw new IllegalArgumentException("Field " + field.getName() +
				" is not next to previous field");
			nextStart += field.getLength();
		}
	}
}
