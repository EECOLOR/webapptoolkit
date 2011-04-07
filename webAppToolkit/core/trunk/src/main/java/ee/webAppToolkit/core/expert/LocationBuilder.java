package ee.webAppToolkit.core.expert;

import com.google.inject.ImplementedBy;

@ImplementedBy(LocationBuilderImpl.class)
public interface LocationBuilder
{
	public void setLocation(String action, QueryString query, String fragment);
	public void in(String context);
	public String getLocation();
}
