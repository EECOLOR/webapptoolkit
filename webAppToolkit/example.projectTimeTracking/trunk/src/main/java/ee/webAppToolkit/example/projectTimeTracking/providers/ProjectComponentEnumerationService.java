package ee.webAppToolkit.example.projectTimeTracking.providers;

import java.util.List;

import ee.webAppToolkit.example.projectTimeTracking.domain.ProjectComponent;
import ee.webAppToolkit.rendering.freemarker.utils.Enumeration;
import ee.webAppToolkit.rendering.freemarker.utils.EnumerationProvider;

public class ProjectComponentEnumerationService implements EnumerationProvider<ProjectComponent> {

	@Override
	public List<Enumeration<ProjectComponent>> get() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<ProjectComponent> get(final ProjectComponent instance) {
		return new Enumeration<ProjectComponent>() {

			@Override
			public String getLabel() {
				return instance.description;
			}

			@Override
			public Object getValue() {
				return instance.getId();
			}
			
		};
	}

}
