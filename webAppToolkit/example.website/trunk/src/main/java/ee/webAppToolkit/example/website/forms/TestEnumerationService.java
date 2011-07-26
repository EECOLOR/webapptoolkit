package ee.webAppToolkit.example.website.forms;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.inject.Provider;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.freemarker.forms.EnumerationProvider;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.exceptions.CustomEmptyValueException;

public class TestEnumerationService implements EnumerationProvider<TestEnumeration>, Converter<String, TestEnumeration>{
	private List<TestEnumeration> _list;
	private Provider<String> _localizedStringProvider;
	
	@Inject
	public TestEnumerationService(@LocalizedString("forms.noTestEnumeration") Provider<String> localizedStringProvider)
	{
		_localizedStringProvider = localizedStringProvider;
		
		List<TestEnumeration> list = new ArrayList<TestEnumeration>(2);
		
		TestEnumeration testEnumeration;
		
		testEnumeration = new TestEnumeration();
		testEnumeration.label = "TestEnumeration 1";
		testEnumeration.value = 0;
		list.add(testEnumeration);
		
		testEnumeration = new TestEnumeration();
		testEnumeration.label = "TestEnumeration 2";
		testEnumeration.value = 1;
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
		if (value.length() == 0)
		{
			throw new CustomEmptyValueException(_localizedStringProvider.get());
		}
		System.out.println(value);
		return _list.get(Integer.parseInt(value));
	}
	
	
}
