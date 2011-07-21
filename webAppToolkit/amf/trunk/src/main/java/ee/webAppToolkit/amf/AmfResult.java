package ee.webAppToolkit.amf;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.exadel.flamingo.flex.messaging.amf.io.AMF3Serializer;

import ee.webAppToolkit.core.expert.impl.DefaultResult;

public class AmfResult extends DefaultResult {
	
	static private byte[] _createAmfBytes(Object object)
	{
		 BeanInfo info;
		try {
			info = Introspector.getBeanInfo(object.getClass());
		} catch (IntrospectionException e1) {
			throw new RuntimeException(e1);
		}
	        for (PropertyDescriptor property : info.getPropertyDescriptors()) {
	          String propertyName = property.getName();
	          System.out.println(propertyName);
	        }
		
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
