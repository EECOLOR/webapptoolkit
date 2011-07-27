package ee.webAppToolkit.storage.twigPersist;

import java.util.List;

import javax.inject.Inject;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.vercer.engine.persist.FindCommand.RootFindCommand;
import com.vercer.engine.persist.ObjectDatastore;

import ee.webAppToolkit.storage.Store;

public class TwigPersistStore implements Store {

	private ObjectDatastore _objectDatastore;

	@Inject
	public TwigPersistStore(ObjectDatastore objectDatastore)
	{
		_objectDatastore = objectDatastore;
	}
	
	@Override
	public <T> T load(Class<T> entityClass, Object key) {
		return _objectDatastore.load(entityClass, key);
	}

	@Override
	public <T> T load(T exampleEntity) {
		
		@SuppressWarnings("unchecked")
		Class<T> entityClass = (Class<T>) exampleEntity.getClass();
		
		RootFindCommand<T> findCommand = _objectDatastore.find().type(entityClass);
		
		//TODO create metadata project
		//TODO for each property
		//findCommand.addFilter("property", FilterOperator.EQUAL, "value, type is Object");
		
		QueryResultIterator<T> results = findCommand.returnResultsNow();
		
		if (results.hasNext())
		{
			T result = results.next();
			
			if (results.hasNext())
			{
				//TODO throw exception
			}
			
			return result;
		} else
		{
			return null;
		}
	}

	@Override
	public void save(Object entity) {
		_objectDatastore.store(entity);
	}

	@Override
	public void remove(Object entity) {
		_objectDatastore.delete(entity);
	}

	@Override
	public <T> List<T> list(Class<T> entityClass) {
		return list(entityClass, 0, 0, null);
	}

	@Override
	public <T> List<T> list(Class<T> entityClass, String sortOrder) {
		return list(entityClass, 0, 0, sortOrder);
	}

	@Override
	public <T> List<T> list(Class<T> entityClass, int offset, int maxResults) {
		
		return list(entityClass, offset, maxResults, null);
	}

	@Override
	public <T> List<T> list(Class<T> entityClass, int offset, int maxResults, String sortOrder) {
		RootFindCommand<T> findCommand = _objectDatastore.find().type(entityClass);
		
		if (offset > 0)
		{
			findCommand.startFrom(offset);
		}
		
		if (maxResults > 0)
		{
			findCommand.maximumResults(maxResults);
		}
		
		//TODO for each sortOrder
		//findCommand.addSort("field", Query.SortDirection.ASCENDING)
		
		return Lists.newArrayList(findCommand.returnResultsNow());
	}

	@Override
	public <T> List<T> find(T exampleEntity) {
		return find(exampleEntity, 0, 0, null);
	}

	@Override
	public <T> List<T> find(T exampleEntity, String sortOrder) {
		return find(exampleEntity, 0, 0, sortOrder);
	}

	@Override
	public <T> List<T> find(T exampleEntity, int offset, int maxResults) {
		return find(exampleEntity, offset, maxResults, null);
	}

	@Override
	public <T> List<T> find(T exampleEntity, int offset, int maxResults, String sortOrder) {
		@SuppressWarnings("unchecked")
		Class<T> entityClass = (Class<T>) exampleEntity.getClass();
		
		RootFindCommand<T> findCommand = _objectDatastore.find().type(entityClass);
		
		if (offset > 0)
		{
			findCommand.startFrom(offset);
		}
		
		if (maxResults > 0)
		{
			findCommand.maximumResults(maxResults);
		}
		
		//TODO for each property
		//findCommand.addFilter("property", FilterOperator.EQUAL, "value, type is Object");
		
		//TODO for each sortOrder
		//findCommand.addSort("field", Query.SortDirection.ASCENDING)
		
		return Lists.newArrayList(findCommand.returnResultsNow());
	}

}
