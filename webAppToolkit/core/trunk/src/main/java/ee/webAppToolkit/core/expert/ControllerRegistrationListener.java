package ee.webAppToolkit.core.expert;

public interface ControllerRegistrationListener {
	public void controllerRegistered(String path, Class<?> controller);
}
