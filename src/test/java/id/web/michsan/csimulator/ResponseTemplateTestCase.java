package id.web.michsan.csimulator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ichsan@gmail.com">Muhammad Ichsan</a>
 *
 */
public class ResponseTemplateTestCase {
	private Resolver resolver;

	@Before
	public void before() {
		resolver = new Resolver() {

			public String resolve(String value) {
				return value;
			}
		};
	}

	@Test
	public void shouldMatch() {
		// Given
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("32", "Dummy hello 123");

		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "0==\"0200\" && 5==\"keren\"");

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
			new ResponseTemplate("aRule", "Desc", templateFields, "unused");
		template.setResolver(resolver);

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
			new ResponseTemplate("aRule", "Desc", templateFields, "unused");
		template.setResolver(resolver);

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
		template.setResolver(resolver);

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
			new ResponseTemplate("aRule", "Desc", templateFields, "unused");
		template.setResolver(resolver);

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
			new ResponseTemplate("aRule", "Desc", templateFields, "unused");
		template.setResolver(resolver);

		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("48", "Jack");

		// When
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then
		assertEquals("Jack      Jakarta", responseFields.get("48"));
	}

	@Test
	public void shouldUseResolver() {
		// Given a template
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("31", "Three One");
		templateFields.put("32", "<a-trick>");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unused");

		// And expectation that the resolver react to the value
		resolver = createMock(Resolver.class);
		template.setResolver(resolver);
		expect(resolver.resolve("Three One")).andReturn("Three One");
		expect(resolver.resolve("<a-trick>")).andReturn("Wonderful trick!");
		replay(resolver);

		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("32", "Suka2");

		// When we create a response
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then
		assertEquals("Three One", responseFields.get("31"));
		assertEquals("Wonderful trick!", responseFields.get("32"));
	}


	@Test
	public void shouldRenderTheSameResolvedValueInOneCycle() {
		// Given: we have a response template having the same resolving values
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("F55", "<counting>");
		fields.put("F65", "<counting>");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", fields, "unused");

		// And: a template resolver
		resolver = createMock(Resolver.class);
		template.setResolver(resolver);
		expect(resolver.resolve("<counting>")).andReturn("500");
		replay(resolver);

		// When: we create a response
		Map<String, String> responseFields = template.createResponse(new HashMap<String, String>());

		// Then: we should get the same rendered value
		assertEquals("500", responseFields.get("F55"));
		assertEquals("500", responseFields.get("F65"));

		// And: resolver is just called once
		verify(resolver);
	}

	@Test
	public void shouldRenderDifferentlyBetweenCycles() {
		// Given: we have a response template having the same resolving values
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("F55", "<counting>");
		fields.put("F65", "<counting>");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", fields, "unused");

		// And: a template resolver
		resolver = createMock(Resolver.class);
		template.setResolver(resolver);
		expect(resolver.resolve("<counting>")).andReturn("500");
		expect(resolver.resolve("<counting>")).andReturn("550");
		replay(resolver);

		// When:we create a response
		Map<String, String> responseFields1 = template.createResponse(new HashMap<String, String>());
		Map<String, String> responseFields2 = template.createResponse(new HashMap<String, String>());

		// Then: we should get different render result
		assertEquals("500", responseFields1.get("F55"));
		assertEquals("550", responseFields2.get("F65"));

		// And: resolver is just called once
		verify(resolver);
	}

	@Test
	public void shouldEchoOtherRequestField() {
		// Given a template which echo from other field
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("32", "<echo|f:24>");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unused");
		template.setResolver(resolver);

		// When a request message comes
		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("24", "Halo Bandung");
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then field 32 should be the same with field 24
		assertEquals("Halo Bandung", responseFields.get("32"));
	}

	@Test
	public void shouldEchoSubstringOfOtherRequestField() {
		// Given a template
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("RX@F48", "1234567890<echo|*,10>Jakarta        <echo|0,3|f:RX@F17>Cool");
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unused");
		template.setResolver(resolver);

		// When a request (with different fields from the template) comes
		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("RX@F17", "Sun is beautiful");
		requestFields.put("RX@F48", "0000000000CyberoMM  Jakarta        XXDWonderful");
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then
		assertEquals("1234567890CyberoMM  Jakarta        SunCool", responseFields.get("RX@F48"));
	}

	@Test
	public void shouldNotTrimMessage() {
		// Given a template with tailing space
		Map<String, String> templateFields = new HashMap<String, String>();
		templateFields.put("RX@F48", "<echo|*,10> is cool! "); // Notice the last space
		ResponseTemplate template =
			new ResponseTemplate("aRule", "Desc", templateFields, "unsed");
		template.setResolver(resolver);

		// When a request (with different fields from the template) comes
		Map<String, String> requestFields = new HashMap<String, String>();
		requestFields.put("RX@F48", "Naomi Sach");
		Map<String, String> responseFields = template.createResponse(requestFields);

		// Then
		assertEquals("Naomi Sach is cool! ", responseFields.get("RX@F48"));
	}

	@Test(expected = InvalidMessageFormatException.class)
	public void shouldTellForMissingDetails() throws Exception {
		ResponseTemplate.convert(new TemplateLoader(
				"src/test/files/missing-templates.txt", "rule_names",
				"response").load());
	}
}
