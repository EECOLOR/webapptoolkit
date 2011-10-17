package ee.webAppToolkit.storage;

import javax.inject.Inject;

public class IdEntity implements Identifiable {

	//typed as object in order for it to be null
	private transient Long _id;
	
	@Inject
	//No constructor injection to keep API simple
	private transient Store _store;
	
	@Override
	public Long getId() {
		if (_id == null)
		{
			long id = _store.getKeyAsLong(this);
			
			if (id != 0)
			{
				_id = id;
			}
		}
		
		return _id;
	}
	
	public void setId(Long id) {
		_id = id;
	}

}
