package ee.webAppToolkit.storage;

import java.util.List;

/*
 * sortOrder 
 * 		should be specified as: "field1 asc, field2 desc"
 * 		only works one level deep
 * 
 * exampleEntity
 * 		only one level deep matching is used
 */

//TODO add exceptions
public interface Store {
	
	public <T> T load(Class<T> entityClass, Object key);
	public <T> T load(T exampleEntity);
	
	public void save(Object entity);
	public void remove(Object entity);	
	
	public <T> List<T> list(Class<T> entityClass);
	public <T> List<T> list(Class<T> entityClass, String sortOrder);
	public <T> List<T> list(Class<T> entityClass, int offset, int maxResults);
	public <T> List<T> list(Class<T> entityClass, int offset, int maxResults, String sortOrder);
	
	public <T> List<T> find(T exampleEntity);
	public <T> List<T> find(T exampleEntity, String sortOrder);
	public <T> List<T> find(T exampleEntity, int offset, int maxResults);
	public <T> List<T> find(T exampleEntity, int offset, int maxResults, String sortOrder);

}
