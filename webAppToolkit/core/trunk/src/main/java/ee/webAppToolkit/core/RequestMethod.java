package ee.webAppToolkit.core;

import com.google.inject.ProvidedBy;
import com.google.inject.servlet.RequestScoped;

import ee.webAppToolkit.core.expert.RequestMethodProvider;

@ProvidedBy(RequestMethodProvider.class)
@RequestScoped
public enum RequestMethod
{
	GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE;
}