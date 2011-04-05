package ee.webAppToolkit.core;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface Result {
	public String getCharacterEncoding();
	public String getContentType();
	public String getContent();
	public Map<String, String> getHeaders();
	public byte[] getBytes() throws UnsupportedEncodingException;
	public boolean preventWrapping();
}
