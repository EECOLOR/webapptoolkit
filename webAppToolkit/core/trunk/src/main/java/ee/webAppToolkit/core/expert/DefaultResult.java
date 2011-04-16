package ee.webAppToolkit.core.expert;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.DefaultCharacterEncoding;

public class DefaultResult implements Result 
{
	@Inject
	@DefaultCharacterEncoding
	public static String DEFAULT_CHARACTER_ENCODING;
	
	private String _characterEncoding;
	private String _contentType;
	private String _content;
	private byte[] _bytes;
	private boolean _preventWrapping;
	private Map<String, String> _headers;
	
	/**
	 * Calls DefaultResult("text/plain", content)
	 * 
	 * @param content
	 */
	public DefaultResult(String content)
	{
		this("text/html", content);
	}
	
	/**
	 * Calls DefaultResult("text/plain", content, preventWrapping)
	 * 
	 * @param content
	 * @param preventWrapping
	 */
	public DefaultResult(String content, boolean preventWrapping)
	{
		this("text/html", content, preventWrapping);
	}
	
	/**
	 * Calls DefaultResult(contentType, content, null)
	 * 
	 * @param content
	 * @param stringContent
	 */
	public DefaultResult(String contentType, String content)
	{
		this(contentType, content, DEFAULT_CHARACTER_ENCODING);
	}
	
	/**
	 * Calls DefaultResult(contentType, content, null, preventWrapping)
	 * 
	 * @param content
	 * @param stringContent
	 * @param preventWrapping
	 */
	public DefaultResult(String contentType, String content, boolean preventWrapping)
	{
		this(contentType, content, DEFAULT_CHARACTER_ENCODING, preventWrapping);
	}
	
	/**
	 * Calls DefaultResult(contentType, content, characterEncoding, false)
	 * 
	 * @param contentType
	 * @param content
	 * @param characterEncoding
	 */
	public DefaultResult(String contentType, String content, String characterEncoding)
	{
		this(contentType, content, characterEncoding, false);
	}
	
	public DefaultResult(String contentType, String content, String characterEncoding, boolean preventWrapping)
	{
		_contentType = contentType;
		_content = content;
		_characterEncoding = characterEncoding == null ? DEFAULT_CHARACTER_ENCODING : characterEncoding;
		_preventWrapping = preventWrapping;
		_headers = new HashMap<String, String>();
	}
	
	/**
	 * Calls DefaultResult(contentType, bytes, null)
	 * 
	 * @param contentType
	 * @param bytes
	 */
	public DefaultResult(String contentType, byte[] bytes)
	{
		this(contentType, bytes, null);
	}
	
	/**
	 * Calls DefaultResult(contentType, bytes, null, preventWrapping)
	 * 
	 * @param contentType
	 * @param bytes
	 */
	public DefaultResult(String contentType, byte[] bytes, boolean preventWrapping)
	{
		this(contentType, bytes, null, preventWrapping);
	}	
	
	/**
	 * Calls DefaultResult(contentType, bytes, characterEncoding, false)
	 * 
	 * @param contentType
	 * @param bytes
	 * @param characterEncoding
	 */
	public DefaultResult(String contentType, byte[] bytes, String characterEncoding)
	{
		this(contentType, bytes, characterEncoding, false);
	}
	
	public DefaultResult(String contentType, byte[] bytes, String characterEncoding, boolean preventWrapping)
	{
		_contentType = contentType;
		_bytes = bytes;
		_characterEncoding = characterEncoding;
		_preventWrapping = preventWrapping;
		_headers = new HashMap<String, String>();
	}

	public void addHeader(String name, String value)
	{
		_headers.put(name, value);
	}
	
	@Override
	public String getCharacterEncoding()
	{
		return _characterEncoding;
	}

	@Override
	public String getContentType()
	{
		return _contentType;
	}

	@Override
	public String getContent()
	{
		return _content;
	}

	@Override
	/**
	 * If bytes are available, bytes are returned. If not it will check for content. If 
	 * no content is available, bytes are set to an empty byte[] and returned. If content 
	 * is available it will be set as bytes using the character encoding and returned.
	 */
	public byte[] getBytes() throws UnsupportedEncodingException
	{
		if (_bytes == null)
		{
			if (_content == null)
			{
				_bytes = new byte[] {};
			} else
			{
				_bytes = _content.getBytes(_characterEncoding);
			}
		}
		
		return _bytes;
	}

	@Override
	public boolean preventWrapping()
	{
		return _preventWrapping;
	}

	@Override
	public Map<String, String> getHeaders()
	{
		return _headers;
	}
}
