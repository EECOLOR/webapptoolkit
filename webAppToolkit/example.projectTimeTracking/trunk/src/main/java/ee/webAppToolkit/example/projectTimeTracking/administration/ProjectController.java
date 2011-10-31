package ee.webAppToolkit.example.projectTimeTracking.administration;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.inject.Provider;

import ee.webAppToolkit.core.QueryString;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Flash;
import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.core.expert.ThreadLocalProvider;
import ee.webAppToolkit.example.projectTimeTracking.domain.Project;
import ee.webAppToolkit.example.projectTimeTracking.domain.ProjectComponent;
import ee.webAppToolkit.example.projectTimeTracking.domain.ProjectNumber;
import ee.webAppToolkit.example.projectTimeTracking.domain.ProjectPurchaseAndSale;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.rendering.RenderingController;
import ee.webAppToolkit.storage.Store;

@NavigationDisplayName(@LocalizedString("navigation.projects"))
public class ProjectController extends RenderingController {

	@Inject
	private ThreadLocalProvider<EmployeeContext> _employeeContextProvider;

	@Override
	public void beforeHandling(String memberName, Object controller) {
		_employeeContextProvider.set(EmployeeContext.OPERATIONS_ONLY);

		super.beforeHandling(memberName, controller);
	}

	@Inject
	@LocalizedString("project.saved")
	private String _savedMessage;

	@Inject
	@LocalizedString("project.removed")
	private String _removedMessage;

	@Inject
	private Store _store;

	@Inject
	private Provider<Calendar> _calendarProvider;

	@Inject
	private Provider<Project> _projectProvider;

	public Result index(
			@Optional @Parameter("project") Project project,
			@Optional @Parameter("id") Long id,
			ValidationResults validationResults,
			@Flash String message,
			@Optional @Parameter("followUpProjectNumber") String followUpProjectNumber,
			@Optional @Parameter("removeProjectComponent") Long projectComponentId,
			@Optional @Parameter("removeProjectPurchaseAndSale") Long projectPurchaseAndSaleId) {
		if (validationResults.getValidated("project")) {
			if (project == null) {
				if (id == null) {
					// create a project number
					project = _projectProvider.get();
					project.projectNumber = _getProjectNumber(followUpProjectNumber);
				} else {
					project = _store.load(Project.class, id);
				}
			} else {
				if (projectComponentId != null) {
					_removeProjectComponent(project, projectComponentId);
				}

				if (projectPurchaseAndSaleId != null) {
					_removeProjectPurchaseAndSale(project,
							projectPurchaseAndSaleId);
				}

				_store.save(project);
				flash.put(_savedMessage);
				redirect(new QueryString("id", project.getId()));
			}
		}

		Model model = new Model(_store.list(Project.class), project, message);

		return render(model);
	}

	private void _removeProjectComponent(Project project,
			Long projectComponentId) {
		for (ProjectComponent projectComponent : project.components) {
			if (projectComponent.getId().equals(projectComponentId)) {
				project.components.remove(projectComponent);
				_store.remove(projectComponent);
				break;
			}
		}
	}

	private void _removeProjectPurchaseAndSale(Project project,
			Long projectPurchaseAndSaleId) {
		for (ProjectPurchaseAndSale projectPurchaseAndSale : project.purchasesAndSales) {
			if (projectPurchaseAndSale.getId().equals(projectPurchaseAndSaleId)) {
				project.purchasesAndSales.remove(projectPurchaseAndSale);
				_store.remove(projectPurchaseAndSale);
				break;
			}
		}
	}

	private ProjectNumber _getProjectNumber(String followUpProject) {

		if (followUpProject == null) {
			// create a new project number
			Calendar calendar = _calendarProvider.get();
			int year = calendar.get(Calendar.YEAR);

			if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER
					&& calendar.get(Calendar.DAY_OF_MONTH) > 10) {
				year += 1;
			}

			/*
			 * We need special sorting
			 */
			Function<Project, String> getProjectNumberAsString = new Function<Project, String>() {

				@Override
				public String apply(Project project) {
					return project.projectNumber.toString();
				}
			};

			Iterable<Project> projects = _store.list(Project.class);

			Ordering<Project> projectNumberOrdering = Ordering.natural()
					.reverse().onResultOf(getProjectNumberAsString);
			List<Project> sortedProjects = projectNumberOrdering
					.sortedCopy(projects);

			Iterator<Project> lastProject = sortedProjects.iterator();

			if (lastProject.hasNext()) {
				return ProjectNumber.increment(
						lastProject.next().projectNumber, year);
			} else {
				return new ProjectNumber(year, 0, 0);
			}
		} else {
			return ProjectNumber.followUp(new ProjectNumber(followUpProject));
		}

	}

	@HideFromNavigation
	public Result remove(@Parameter("id") Long id) {
		Project project = _store.load(Project.class, id);
		return render(new Model(project));
	}

	@HideFromNavigation
	public void removeConfirm(@Parameter("id") Long id) {
		_store.removeByKey(Project.class, id);

		flash.put(_removedMessage);
		redirect();
	}

	public class Model {
		public Iterable<Project> projects;
		public Project project;
		public String message;

		public Model(Project project) {
			this(null, project, null);
		}

		public Model(Iterable<Project> projects, Project project, String message) {
			this.projects = projects;
			this.project = project;
			this.message = message;
		}
	}
}
