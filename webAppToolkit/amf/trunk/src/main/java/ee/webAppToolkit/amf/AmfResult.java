package ee.webAppToolkit.amf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.exadel.flamingo.flex.messaging.amf.io.AMF3Serializer;

import ee.webAppToolkit.core.expert.impl.DefaultResult;

public class AmfResult extends DefaultResult {
	
	static private byte[] _createAmfBytes(Object object)
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		AMF3Serializer amf3Serializer = new AMF3Serializer(byteArrayOutputStream);
		try {
			amf3Serializer.writeObject(object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return byteArrayOutputStream.toByteArray();
	}
	
	public AmfResult(Object object)
	{
		super("application/x-amf", _createAmfBytes(object), true);
	}
}
