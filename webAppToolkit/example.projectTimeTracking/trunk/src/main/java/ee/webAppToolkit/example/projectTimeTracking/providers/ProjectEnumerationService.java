package ee.webAppToolkit.example.projectTimeTracking.providers;

import java.util.List;

import ee.webAppToolkit.example.projectTimeTracking.domain.Project;
import ee.webAppToolkit.rendering.freemarker.utils.Enumeration;
import ee.webAppToolkit.rendering.freemarker.utils.EnumerationProvider;

public class ProjectEnumerationService implements EnumerationProvider<Project> {

	@Override
	public List<Enumeration<Project>> get() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<Project> get(final Project instance) {
		return new Enumeration<Project>() {

			@Override
			public String getLabel() {
				return instance.name;
			}

			@Override
			public Object getValue() {
				return instance.getId();
			}
			
		};
	}
	
}
