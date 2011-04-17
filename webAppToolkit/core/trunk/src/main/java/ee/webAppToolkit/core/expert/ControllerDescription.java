package ee.webAppToolkit.core.expert;

import java.util.List;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.expert.impl.ControllerDescriptionImpl;

@ImplementedBy(ControllerDescriptionImpl.class)
public interface ControllerDescription {

	static public final String INDEX = "index";
	
	public List<Action> getActions();
	public List<SubController> getSubControllers();
	public Class<?> getControllerType();
}