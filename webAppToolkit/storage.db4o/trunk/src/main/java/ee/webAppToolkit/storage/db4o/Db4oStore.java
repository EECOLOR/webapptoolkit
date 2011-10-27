package ee.webAppToolkit.storage.db4o;

import javax.inject.Inject;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.query.Query;
import com.google.inject.Provider;

import ee.objectCloner.ObjectCloner;
import ee.webAppToolkit.storage.Identifiable;
import ee.webAppToolkit.storage.Store;

public class Db4oStore implements Store {

	private Provider<ObjectContainer> _objectContainerProvider;
	private Provider<ObjectCloner> _objectCloner;
	
	@Inject
	public Db4oStore(Provider<ObjectContainer> objectContainerProvider, Provider<ObjectCloner> objectCloner)
	{
		_objectContainerProvider = objectContainerProvider;
		_objectCloner = objectCloner;
	}
	
	protected ObjectContainer getObjectContainer()
	{
		return _objectContainerProvider.get();
	}
	
	@Override
	public Object getKey(Object entity) {
		
		if (entity instanceof Identifiable)
		{
			return ((Identifiable) entity).getId();
		} else
		{
			long id = getKeyAsLong(entity);
			
			return id == 0 ? null : id;
		}
	}

	@Override
	public long getKeyAsLong(Object entity) {
		return getObjectContainer().ext().getID(entity);
	}

	@Override
	public <T> T load(Class<T> entityClass, Object key) {
		return _getByKey(key);
	}

	@Override
	public <T> T load(Class<T> entityClass, long key) {
		return _getByKey(key);
	}

	@Override
	public void save(Object entity) {
		
		Object key = getKey(entity);
		
		Object entityToSave;
		
		if (key == null) {
			entityToSave = entity;
		} else {
			entityToSave = _getByKey(key);
		
			//TODO check if we can use the bind method of the ext() container
			ObjectCloner objectCloner = _objectCloner.get();
			objectCloner.copy(entity, entityToSave);
		}
		
		getObjectContainer().store(entityToSave);
	}

	@Override
	public void remove(Object entity) {
		Object existingEntity = _getByKey(getKey(entity));
		getObjectContainer().delete(existingEntity);
	}

	@Override
	public void removeByKey(Object key) {
		getObjectContainer().delete(_getByKey(key));
	}
	
	@Override
	public void removeByKey(Class<?> entityClass, long key) {
		getObjectContainer().delete(_getByKey(key));
	}
	
	@Override
	public int count(Class<?> entityClass) {
		Query query = getObjectContainer().query();
		query.constrain(entityClass);
		return query.execute().size();
	}
	
	@Override
	public <T> Iterable<T> list(Class<T> entityClass) {
		return list(entityClass, null);
	}

	@Override
	public <T> Iterable<T> list(Class<T> entityClass, String sortOrder) {
		return list(entityClass, 0, 0, sortOrder);
	}

	@Override
	public <T> Iterable<T> list(Class<T> entityClass, int offset, int maxResults) {
		return list(entityClass, 0, 0, null);
	}

	@Override
	public <T> Iterable<T> list(Class<T> entityClass, int offset, int maxResults, String sortOrder) {
		Query query = getObjectContainer().query();
		query.constrain(entityClass);
		
		if (sortOrder != null) {
			_addSort(sortOrder, query);
		}
		
		ObjectSet<T> result = query.execute();
		
		return _getPaged(result, offset, maxResults);
	}

	@Override
	public int count(Object exampleEntity) {
		Query query = getObjectContainer().query();
		query.constrain(exampleEntity);
		return query.execute().size();
	}
	
	@Override
	public <T> Iterable<T> find(T exampleEntity) {
		return find(exampleEntity, null);
	}

	@Override
	public <T> Iterable<T> find(T exampleEntity, String sortOrder) {
		return find(exampleEntity, 0, 0, sortOrder);
	}

	@Override
	public <T> Iterable<T> find(T exampleEntity, int offset, int maxResults) {
		return find(exampleEntity, 0, 0, null);
	}

	@Override
	public <T> Iterable<T> find(T exampleEntity, int offset, int maxResults, String sortOrder) {
		Query query = getObjectContainer().query();
		query.constrain(exampleEntity);
		
		if (sortOrder != null) {
			_addSort(sortOrder, query);
		}
		
		ObjectSet<T> result = query.execute();
		
		return _getPaged(result, offset, maxResults);
	}

	private <T> T _getByKey(Object key) {
		ExtObjectContainer ext = getObjectContainer().ext();
		T object = ext.getByID((long) key);
		ext.activate(object);
		return object;
	}
	
	private <T> void _addSort(String sortOrder, Query query) {
		String[] sortOrderArray = sortOrder.split(",");
		
		for (String sortOrderPart : sortOrderArray)
		{
			sortOrderPart = sortOrderPart.trim();
			
			String[] sortOrderPartArray = sortOrderPart.split(" +");
			
			Query subQuery = query.descend(sortOrderPartArray[0]);
			
			if (sortOrderPartArray.length == 1)
			{
				subQuery.orderAscending();
			} else
			{
				switch (sortOrderPartArray[1].toLowerCase())
				{
					case "asc":
						subQuery.orderAscending();
						break;
					case "desc":
						subQuery.orderDescending();
						break;
				}
			}
		}
	}

	private <T> Iterable<T> _getPaged(ObjectSet<T> result, int offset, int maxResults) {
		int size = result.size();
		int start = offset;
		int end = size;
		
		if (start >= size)
		{
			start = size;
		}
		
		if (maxResults > 0)
		{
			end = start + maxResults;
			
			if (end >= size)
			{
				end = size;
			}
		}

		return result.subList(start, end);
	}
}
