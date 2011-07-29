package ee.webAppToolkit.storage.twigPersist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.vercer.engine.persist.FindCommand.RootFindCommand;
import com.vercer.engine.persist.ObjectDatastore;

import ee.metadataUtils.PropertyMetadata;
import ee.metadataUtils.PropertyMetadataRegistry;
import ee.webAppToolkit.storage.Store;

public class TwigPersistStore implements Store {

	static private final Map<String, SortDirection> _sortOrderReference = _getSortOrderReference();
	
	private static Map<String, SortDirection> _getSortOrderReference() {
		
		Map<String, SortDirection> sortOrderReference = new HashMap<String, SortDirection>();
		sortOrderReference.put("asc", SortDirection.ASCENDING);
		sortOrderReference.put("desc", SortDirection.DESCENDING);
		
		return sortOrderReference;
	}
	
	private ObjectDatastore _objectDatastore;
	private PropertyMetadataRegistry _propertyMetadataRegistry;

	@Inject
	public TwigPersistStore(ObjectDatastore objectDatastore, PropertyMetadataRegistry propertyMetadataRegistry)
	{
		_objectDatastore = objectDatastore;
		_propertyMetadataRegistry = propertyMetadataRegistry;
	}


	@Override
	public <T> T load(Class<T> entityClass, Object key) {
		return _objectDatastore.load(entityClass, key);
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
		
		_addSort(sortOrder, findCommand);
		
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
		
		_addPropertyFilters(exampleEntity, findCommand);
		
		_addSort(sortOrder, findCommand);
		
		return Lists.newArrayList(findCommand.returnResultsNow());
	}


	private <T> void _addSort(String sortOrder, RootFindCommand<T> findCommand) {
		String[] sortOrderArray = sortOrder.split(",");
		
		for (String sortOrderPart : sortOrderArray)
		{
			sortOrderPart = sortOrderPart.trim();
			
			String[] sortOrderPartArray = sortOrderPart.split(" +");
			
			if (sortOrderPartArray.length == 1)
			{
				findCommand.addSort(sortOrderPartArray[0]);
			} else
			{
				findCommand.addSort(sortOrderPartArray[0], _sortOrderReference.get(sortOrderPartArray[1].toLowerCase()));
			}
		}
	}
	
	private <T> void _addPropertyFilters(T exampleEntity, RootFindCommand<T> findCommand) {
		Map<String, PropertyMetadata> propertyMetadataMap = _propertyMetadataRegistry.getPropertyMetadataMap(exampleEntity.getClass());
		
		for (Entry<String, PropertyMetadata> entry : propertyMetadataMap.entrySet())
		{
			Object value;
			try {
				value = entry.getValue().getValue(exampleEntity);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
			
			if (value != null)
			{
				findCommand.addFilter(entry.getKey(), FilterOperator.EQUAL, value);
			}
		}
	}

}
