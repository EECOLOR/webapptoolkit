package ee.webAppToolkit.core.expert;

import ee.webAppToolkit.core.exceptions.ConfigurationException;

public interface ActionHandler extends Handler {
	public void addAction(Action action) throws ConfigurationException;
}
