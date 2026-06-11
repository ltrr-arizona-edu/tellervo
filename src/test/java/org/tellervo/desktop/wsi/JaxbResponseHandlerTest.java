package org.tellervo.desktop.wsi;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;

import org.apache.http.entity.BasicHttpEntity;
import org.tellervo.schema.WSIRootElement;

import junit.framework.TestCase;

public class JaxbResponseHandlerTest extends TestCase {

	public void testUtf8BomWithoutHttpCharset() throws Exception {
		System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");

		byte[] xml = "<tellervo xmlns=\"http://www.tellervo.org/schema/1.0\"/>"
				.getBytes(StandardCharsets.UTF_8);
		byte[] response = new byte[xml.length + 3];
		response[0] = (byte) 0xEF;
		response[1] = (byte) 0xBB;
		response[2] = (byte) 0xBF;
		System.arraycopy(xml, 0, response, 3, xml.length);

		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(new ByteArrayInputStream(response));

		JaxbResponseHandler<WSIRootElement> handler = new JaxbResponseHandler<WSIRootElement>(
				JAXBContext.newInstance(WSIRootElement.class), WSIRootElement.class);

		assertNotNull(handler.toDocument(entity, null));
	}

	public void testMalformedResponseReportsResponsePrefix() throws Exception {
		System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");

		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(new ByteArrayInputStream(
				"<html><body>Database unavailable</body></html>".getBytes(StandardCharsets.UTF_8)));

		JaxbResponseHandler<WSIRootElement> handler = new JaxbResponseHandler<WSIRootElement>(
				JAXBContext.newInstance(WSIRootElement.class), WSIRootElement.class);

		try {
			handler.toDocument(entity, null);
			fail("Expected malformed response to fail");
		} catch (ResponseProcessingException e) {
			assertTrue(e.getCause().getMessage().contains("Database unavailable"));
		}
	}
}
