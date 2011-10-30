package ee.webAppToolkit.example.projectTimeTracking.domain;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectNumber {
	
	static public final MessageFormat messageFormat = new MessageFormat("{0,number,0000}{1,number,000}_{2}");
	static public final Pattern pattern = Pattern.compile("([0-9]{4})([0-9]{3})_([0-9])");
	
	static public ProjectNumber increment(ProjectNumber previousProjectNumber, int currentYear) {
		int counter;
		
		if (previousProjectNumber.year == currentYear)
		{
			counter = previousProjectNumber.counter++;
		} else
		{
			counter = 0;
		}
		return new ProjectNumber(currentYear, counter, 0);
	}
	
	static public ProjectNumber followUp(ProjectNumber projectNumber) {
		return new ProjectNumber(projectNumber.year, projectNumber.counter, projectNumber.followUpCounter + 1);
	}
	
	transient private String _value;

	public int year;

	public int counter;

	public int followUpCounter;
	
	public ProjectNumber() {
		_setValue();
	}
	
	public ProjectNumber(int year, int counter, int followUpCounter) {
		this.year = year;
		this.counter = counter;
		this.followUpCounter = followUpCounter;
		
		_setValue();
	}
	
	public ProjectNumber(String value) {
		_value = value;
		
		_parseValue(value);
	}
	
	private void _parseValue(String value) {
		Matcher matcher = pattern.matcher(value);
		
		if (matcher.matches()) {
			year = Integer.parseInt(matcher.group(1));
			counter = Integer.parseInt(matcher.group(2));
			followUpCounter = Integer.parseInt(matcher.group(3));
		} else
		{
			throw new RuntimeException("Invalid project number, should be in the following format: 'YYYYCCC_F'. Y = Year, C = Counter (prefixed with zero's), F = follow up counter");
		}
		
	}
	
	public void _setValue() {
		_value = messageFormat.format(new Object[] {year, counter, followUpCounter});
	}
	
	public String toString() {
		if (_value == null) {
			_setValue();
		}
		
		return _value;
	}

}
