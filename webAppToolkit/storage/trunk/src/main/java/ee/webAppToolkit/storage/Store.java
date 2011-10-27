package ee.webAppToolkit.storage;


/*
 * sortOrder 
 * 		should be specified as: "field1 asc, field2 desc"
 * 		only works one level deep
 * 
 * exampleEntity
 * 		only one level deep matching is used
 */

public interface Store {
	
	/**
	 * May return null if the entity has no key
	 */
	public Object getKey(Object entity);
	
	/**
	 * A value of 0 means the entity has no key
	 */
	public long getKeyAsLong(Object entity);
	
	public <T> T load(Class<T> entityClass, Object key);
	public <T> T load(Class<T> entityClass, long key);
	
	public void save(Object entity);
	public void remove(Object entity);	
	public void removeByKey(Object key);	
	public void removeByKey(Class<?> entityClass, long key);	
	
	public int count(Class<?> entityClass);
	public <T> Iterable<T> list(Class<T> entityClass);
	public <T> Iterable<T> list(Class<T> entityClass, String sortOrder);
	public <T> Iterable<T> list(Class<T> entityClass, int offset, int maxResults);
	public <T> Iterable<T> list(Class<T> entityClass, int offset, int maxResults, String sortOrder);
	
	public int count(Object exampleEntity);
	public <T> Iterable<T> find(T exampleEntity);
	public <T> Iterable<T> find(T exampleEntity, String sortOrder);
	public <T> Iterable<T> find(T exampleEntity, int offset, int maxResults);
	public <T> Iterable<T> find(T exampleEntity, int offset, int maxResults, String sortOrder);
	
	/**
	 * The path should consist of property names separated by a dot 
	 */
	public <T> Iterable<T> find(String path, T exampleEntity);
	public <T> Iterable<T> find(String path, T exampleEntity, String sortOrder);
	public <T> Iterable<T> find(String path, T exampleEntity, int offset, int maxResults);
	public <T> Iterable<T> find(String path, T exampleEntity, int offset, int maxResults, String sortOrder);

}
