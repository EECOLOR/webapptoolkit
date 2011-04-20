package ee.webAppToolkit.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ValidationResultContainer extends ValidationResult implements List<ValidationResult>
{
	private static final long serialVersionUID = 1L;
	
	private List<ValidationResult> _validationResults;
	
	public ValidationResultContainer()
	{
		this(null);
	}
	
	public ValidationResultContainer(String errorMessage)
	{
		super(errorMessage);
		_validationResults = new ArrayList<ValidationResult>();
	}	
	
	private boolean _getSubResultsValidated()
	{
		if (size() > 0)
		{
			Iterator<ValidationResult> iterator = iterator();
			
			boolean validated = true;
			
			while (iterator.hasNext())
			{
				validated = iterator.next().getValidated();
				
				if (!validated)
				{
					break;
				}
			}
			
			return validated;
		} else
		{
			return true;
		}
	}
	
	@Override
	public boolean getValidated()
	{
		return super.getValidated() && _getSubResultsValidated();
	}
	
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		if (size() > 1)
		{
			s.append("[ValidationResultContainer]");
			
			for (ValidationResult validationResult : this)
			{
				s.append("\n\t" + validationResult.toString());
			}
			
		} else
		{
			s.append(super.toString());
		}
		
		return s.toString();
	}

	@Override
	public String getErrorMessage()
	{
		if (size() == 1)
		{
			return get(0).getErrorMessage();
		} else
		{
			return super.getErrorMessage();
		}
	}

	@Override
	public Object getOriginalValue()
	{
		if (size() > 0)
		{
			//values should be the same for all validation results
			return get(0).getOriginalValue();
		} else
		{
			return super.getOriginalValue();
		}
	}

	@Override
	public void add(int index, ValidationResult element)
	{
		_validationResults.add(index, element);
	}

	@Override
	public boolean add(ValidationResult e)
	{
		return _validationResults.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends ValidationResult> c)
	{
		return _validationResults.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends ValidationResult> c)
	{
		return _validationResults.addAll(index, c);
	}

	@Override
	public void clear()
	{
		_validationResults.clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return _validationResults.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return _validationResults.containsAll(c);
	}

	@Override
	public ValidationResult get(int index)
	{
		return _validationResults.get(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return _validationResults.indexOf(o);
	}

	@Override
	public boolean isEmpty()
	{
		return _validationResults.isEmpty();
	}

	@Override
	public Iterator<ValidationResult> iterator()
	{
		return _validationResults.iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return _validationResults.lastIndexOf(o);
	}

	@Override
	public ListIterator<ValidationResult> listIterator()
	{
		return _validationResults.listIterator();
	}

	@Override
	public ListIterator<ValidationResult> listIterator(int index)
	{
		return _validationResults.listIterator(index);
	}

	@Override
	public ValidationResult remove(int index)
	{
		return _validationResults.remove(index);
	}

	@Override
	public boolean remove(Object o)
	{
		return _validationResults.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return _validationResults.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return _validationResults.retainAll(c);
	}

	@Override
	public ValidationResult set(int index, ValidationResult element)
	{
		return _validationResults.set(index, element);
	}

	@Override
	public int size()
	{
		return _validationResults.size();
	}

	@Override
	public List<ValidationResult> subList(int fromIndex, int toIndex)
	{
		return _validationResults.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		return _validationResults.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return _validationResults.toArray(a);
	}
	
	
}
