package ee.webAppToolkit.parameters;

import java.lang.reflect.Type;
import java.util.Map;

import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.EmptyValueException;

public interface ParameterConverter {

	//TODO fix comments GenericConverter is removed
	/**
	 * <p>
	 * Converts the given parameters to an instance using the given information. If the value could 
	 * not be found or if the value is empty, an EmptyValueException is thrown.
	 * </p>
	 * 
	 * If the type is a simple or a list type the parameterKey should be present. This value is 
	 * the key at which the value to convert is stored.

	 * <pre>
	 *  {
	 *     "userID" : ["10"]
	 *  }
	 *   
	 *  //throws a ConfigurationException
	 *  void action(@Parameter int userID)
	 *  
	 *  //success
	 *  void action(@Parameter("userID") int userID)
	 *  void action(@Parameter("userID") int[] userIDs)</pre>
	 * 
	 * If the type is a map type and the parameter does not have a value, the complete parameters 
	 * map will be used to create an instance of the map. Note that the map should have generics 
	 * information in order for the parameter converter to convert the parameters correctly.
	 * 
	 * <pre>
	 *  {
	 *     "number1" : ["10"],
	 *     "number2" : ["20"]
	 *  }
	 * 
	 *  void action(@Parameter Map&lt;String, Integer> numbers)</pre>
	 * 
	 * If the type is a map type and the parameter has a value, all keys starting with that value
	 * will be used. This can be useful if you have more then one parameter.
	 * 
	 * <pre>
	 *  {
	 *     "map1.key1" : ["10"],
	 *     "map1.key2" : ["20"],
	 *     "map2.key1" : ["10"],
	 *     "map2.key2" : ["20"]
	 *  }
	 * 
	 *   void action(@Parameter("map1") Map&lt;String, Integer> map1, 
	 *               @Parameter("map2") Map&lt;String, List<Integer>> map2)</pre>
	 * 
	 * If the type is any other type the same rules apply as with maps. With the exception that nested 
	 * properties can be used.
	 * 
	 * <pre>
	 *  {
	 *     "object1.property1" : ["10"]
	 *     "object1.property2" : ["10"]
	 *     "object2.property1.property1" : ["10"]
	 *     "object2.property1.property2" : ["10"]
	 *     "object2.property2" : ["10"]
	 *  }
	 *  
	 *  class Object1 {
	 *     public int property1;
	 *     public int property2;
	 *  }
	 *  
	 *  class Object2 {
	 *     public Object1 property1;
	 *     public int property2;
	 *  }
	 *  
	 *  void action(@Parameter("object1") Object1 object1,
	 *              @Parameter("object2") Object2 object2)</pre>
	 *  
	 * Parameters are converted with the help of <code>Converters</code> and <code>GenericConverters</code> 
	 * these are registered like this:
	 * 
	 * <pre>
	 *  bind(new TypeLiteral&lt;Converter&lt;String, TargetType>>(){}).to(...)
	 *  bind(new TypeLiteral&lt;Converter&lt;String[], TargetType>>(){}).to(...)
	 *  bind(GenericConverter.class).annotatedWith(TypeKindAnnotations.create(TypeKind.Complex)
	 *  	.to(ComplexGenericConverter.class)</pre>
	 *  
	 * By default only <code>GenericConverter</code> classes are bound. The following rules are used 
	 * when resolving a converter:
	 * <ul>
	 * 	<li>Look for a converter that converts from <code>String[]</code> to the desired type</li>
	 * 	<li>Look for converter that converts from <code>String</code> to the desired type if the 
	 * 	parameter is a <code>String</code> or length of the <code>String[]</code> is one.</li>
	 *  <li>Determine the <code>TypeKind</code> of the desired type and grab the appropriate 
	 *  <code>GenericConverter</code></li>
	 * </ul>
	 * 
	 * If during any point of the conversion an exception is thrown an <code>ExceptionConverter</code> 
	 * is used to convert the exception to a validation result. You can bind exception converters like 
	 * this:
	 * 
	 * <pre>
	 *  bind(new TypeLiteral&lt;ExceptionConverter&lt;ExceptionType>>(){}).to(...)</pre>
	 * 
	 * <p>
	 * Properties may be annotated with @Optional, if this is the case no <code>EmptyValueException</code> 
	 * is thrown and the <code>EmptyValueExceptionConverter</code> will not be used to create a validation 
	 * result.
	 * </p>
	 * <p>
	 * Properties may be annotated with @Default, if this is the case and no value was found during conversion 
	 * the <code>DefaultValueProvider</class> is used to populate the property.
	 * </p>
	 * 
	 * @param parameters
	 *            The request parameters
	 * @param type
	 *            The type of object requested
	 * @param validationResults
	 *            An instance of validation results that can be used to put any validation problems
	 * @param parameterKey
	 *            The key of the parameter
	 * 
	 * @return An instance of the requested type
	 * 
	 * @throws EmptyValueException		If the value that is converted could not be found or is empty.
	 * @throws ConfigurationException	If anything happens that is cause by a programming error.
	 * 
	 * @see 
	 */
	public Object convert(Map<String, String[]> parameters, Type type,
			ValidationResults validationResults, String parameterKey) throws EmptyValueException, ConfigurationException;
}
