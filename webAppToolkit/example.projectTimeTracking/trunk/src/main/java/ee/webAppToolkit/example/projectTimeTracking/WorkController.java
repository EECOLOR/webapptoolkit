package ee.webAppToolkit.example.projectTimeTracking;

import java.util.Date;

import javax.inject.Inject;

import com.google.common.collect.Iterables;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Flash;
import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.example.projectTimeTracking.domain.Project;
import ee.webAppToolkit.example.projectTimeTracking.domain.ProjectStatus;
import ee.webAppToolkit.example.projectTimeTracking.domain.Work;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.rendering.RenderingController;
import ee.webAppToolkit.storage.Store;

@NavigationDisplayName(@LocalizedString("navigation.workItems"))
public class WorkController extends RenderingController {

	@Inject
	@LocalizedString("work.saved")
	private String _savedMessage;
	
	@Inject
	@LocalizedString("work.removed")
	private String _removedMessage;
	
	private Store _store;

	@Inject
	public WorkController(Store store) {
		_store = store;
	}
	
	public Result index(
			@Optional @Parameter("project") Project project,
			@Optional @Parameter("work") Work work,
			@Optional @Parameter("id") Long id,
			ValidationResults validationResults,
			@Flash String message)
	{
		//if the work is validated (or not processed)
		if (validationResults.getValidated("work"))
		{
			if (work == null)
			{
				//no work available, check the id
				if (id != null)
				{
					work = _store.load(Work.class, id);
				}
			} else
			{
				//store the work
				_store.save(work);
				
				//redirect to prevent F5 creation
				flash.put(_savedMessage);
				redirect();
			}
		}
		
		if (project == null && work != null) {
			//project = work.projectComponent.project;
		}
		
		Model model = new Model(_store.list(Work.class), work, project, _getAllProjects(), message);
		
		return render(model);
	}

	private Iterable<Project> _getAllProjects() {
		Project exampleProject = new Project();

		//TODO add sorting
		exampleProject.status = ProjectStatus.ACTIVE;
		Iterable<Project> activeProjects = _store.find(exampleProject);
		
		exampleProject.status = ProjectStatus.PROPOSED;
		Iterable<Project> proposedProjects = _store.find(exampleProject);
		
		return Iterables.concat(activeProjects, proposedProjects);
	}
	
	@HideFromNavigation
	public Result remove(@Parameter("id") Long id) {
		Work work = _store.load(Work.class, id);
		return render(new Model(work));
	}
	
	@HideFromNavigation
	public void removeConfirm(@Parameter("id") Long id) {
		_store.removeByKey(Work.class, id);
		
		flash.put(_removedMessage);
		redirect();
	}
	
	public class Model
	{
		public Iterable<Work> workItems;
		public Work work;
		public String message;
		public Date date;
		public Project project;
		public Iterable<Project> projects;
		
		public Model(Work work)
		{
			this(null, work, null, null, null);
		}
		
		public Model(Iterable<Work> workItems, Work work, Project project, Iterable<Project> projects, String message)
		{
			this.workItems = workItems;
			this.work = work;
			this.project = project;
			this.projects = projects;
			this.message = message;
			
			this.date = new Date();
		}
	}
}
