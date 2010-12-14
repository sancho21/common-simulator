package id.web.michsan.csimulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 *
 * @author Muhammad Ichsan (ichsan@gmail.com)
 *
 */
public class ResponseTemplateTestCase {
	@Test
	public void shouldMatch() {
		// Given
		Map<String, String> templateFields = new HashMap<String, String>();
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "0:0200 && 5:keren");

		Map<String, String> requestFields1 = new HashMap<String, String>();
		requestFields1.put("0", "0200");
		requestFields1.put("5", "keren");

		Map<String, String> requestFields2 = new HashMap<String, String>();
		requestFields2.put("0", "0200");
		requestFields2.put("5", "keraan");

		Map<String, String> requestFields3 = new HashMap<String, String>();
		requestFields3.put("0", "0200");
		requestFields3.put("7", "keren");

		assertTrue(template.match(requestFields1));
		assertFalse(template.match(requestFields2));
		assertFalse(template.match(requestFields3));
	}

	@Test
	public void shouldCopyTheResponseField() {
		// Given
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("32", "Pasti Ini");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unsed");

		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("32", "Suka2");

		// When
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then
		assertEquals("Pasti Ini", responseFields.get("32"));

		// -- repeat--------
		requestFields.put("32", "HHXD");
		responseFields = template.createResponse(requestFields);

		assertEquals("Pasti Ini", responseFields.get("32"));
	}

	@Test
	public void shouldEchoRequestField() {
		// Given
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("32", "<echo>");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unsed");

		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("32", "Halo Bandung");

		// When
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then
		assertEquals("Halo Bandung", responseFields.get("32"));

		// -- repeat--------
		requestFields.put("32", "Jakarta Z");
		responseFields = template.createResponse(requestFields);

		assertEquals("Jakarta Z", responseFields.get("32"));
	}

	@Test
	public void shouldEchoSubstringOfRequestField() {
		// Given
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("48", "1234567890<echo|*,10>Jakarta        <echo|*,3>Cool");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unsed");

		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("48", "0000000000Sancho21  Jakarta        XXDWonderful");

		// When
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then
		assertEquals("1234567890Sancho21  Jakarta        XXDCool", responseFields.get("48"));

		// -- repeat--------
		requestFields.put("48", "0000000000Ahmed     Jakarta        XYZCool");
		responseFields = template.createResponse(requestFields);

		assertEquals("1234567890Ahmed     Jakarta        XYZCool", responseFields.get("48"));
	}

	@Test
	public void shouldEchoSubstringOfRequestFieldOnSpecifiedIndex() {
		// Given
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("61", "KTP9999905000<echo|8,4>abc<echo|8,4>");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unsed");

		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("61", "KTP99999REFX");

		// When
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then, we can echo REFX from specified index
		assertEquals("KTP9999905000REFXabcREFX", responseFields.get("61"));

		// -- repeat--------
		requestFields.put("61", "KTP99999REFY");
		responseFields = template.createResponse(requestFields);

		assertEquals("KTP9999905000REFYabcREFY", responseFields.get("61"));
	}

	@Test
	public void shouldPadRequestedFieldBeforeEcho() {
		// Given
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("48", "<echo|*,10>Jakarta");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unsed");

		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("48", "Jack");

		// When
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then
		assertEquals("Jack      Jakarta", responseFields.get("48"));
	}
}
