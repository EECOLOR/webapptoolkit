package ee.webAppToolkit.example.projectTimeTracking.administration;

import javax.inject.Inject;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Flash;
import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.core.expert.ThreadLocalProvider;
import ee.webAppToolkit.example.projectTimeTracking.domain.Customer;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.rendering.RenderingController;
import ee.webAppToolkit.storage.Store;

@NavigationDisplayName(@LocalizedString("navigation.customers"))
public class CustomerController extends RenderingController {

	@Inject
	private ThreadLocalProvider<CustomerContext> _customerContextProvider;
	
	@Override
	public void beforeHandling(String memberName, Object controller) {
		_customerContextProvider.set(CustomerContext.OPERATIONS_ONLY);
		
		super.beforeHandling(memberName, controller);
	}

	@Inject
	@LocalizedString("customer.saved")
	private String _savedMessage;
	
	@Inject
	@LocalizedString("customer.removed")
	private String _removedMessage;
	
	private Store _store;

	@Inject
	public CustomerController(Store store) {
		_store = store;
	}
	
	public Result index(
			@Optional @Parameter("customer") Customer customer,
			@Optional @Parameter("id") Long id,
			ValidationResults validationResults,
			@Flash String message)
	{
		if (validationResults.getValidated("customer"))
		{
			if (customer == null)
			{
				if (id != null)
				{
					customer = _store.load(Customer.class, id);
				}
			} else
			{
				_store.save(customer);
				flash.put(_savedMessage);
				redirect();
			}
		}
		
		Model model = new Model(_store.list(Customer.class), customer, message);
		
		return render(model);
	}
	
	@HideFromNavigation
	public Result remove(@Parameter("id") Long id) {
		Customer customer = _store.load(Customer.class, id);
		return render(new Model(customer));
	}
	
	@HideFromNavigation
	public void removeConfirm(@Parameter("id") Long id) {
		_store.removeByKey(id);
		
		flash.put(_removedMessage);
		redirect();
	}
	
	public class Model
	{
		public Iterable<Customer> customers;
		public Customer customer;
		public String message;

		public Model(Customer customer)
		{
			this(null, customer, null);
		}
		
		public Model(Iterable<Customer> customers, Customer customer, String message)
		{
			this.customers = customers;
			this.customer = customer;
			this.message = message;
		}
	}
}
