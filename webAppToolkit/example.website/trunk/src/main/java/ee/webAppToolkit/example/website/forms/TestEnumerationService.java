package ee.webAppToolkit.example.website.forms;

import java.util.ArrayList;
import java.util.List;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.freemarker.forms.EnumerationProvider;

public class TestEnumerationService implements EnumerationProvider<TestEnumeration>, Converter<String, TestEnumeration>{
	private List<TestEnumeration> _list;
	
	public TestEnumerationService()
	{
		List<TestEnumeration> list = new ArrayList<TestEnumeration>(2);
		
		TestEnumeration testEnumeration;
		
		testEnumeration = new TestEnumeration();
		testEnumeration.label = "TestEnumeration 1";
		testEnumeration.value = 1;
		list.add(testEnumeration);
		
		testEnumeration = new TestEnumeration();
		testEnumeration.label = "TestEnumeration 2";
		testEnumeration.value = 2;
		list.add(testEnumeration);
		
		_list = list;
	}
	
	@Override
	public List<TestEnumeration> get() {
		return _list;
	}

	@Override
	public TestEnumeration convert(String value) throws EmptyValueException,
			ConversionFailedException {
		
		return _list.get(Integer.parseInt(value));
	}
	
	
}
