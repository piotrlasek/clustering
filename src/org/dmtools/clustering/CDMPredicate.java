package org.dmtools.clustering;

import javax.datamining.rule.Predicate;

public class CDMPredicate implements Predicate {
	
	Object variable;
	Object operator;
	Object value;
	
	public CDMPredicate(String variable, String operator, String value)
	{
		this.variable = variable;
		this.operator = operator;
		this.value = value;
	}
	
	public boolean test(Object testValue)
	{
		boolean result = false;
		
		if (testValue.getClass().equals(variable.getClass()))
		{
			if (testValue.getClass().equals(Double.class))
			{
				if (operator.equals("=="))
				{
					
				}
				else if (operator.equals("<"))
				{
					
				}
				else if (operator.equals(">"))
				{
					
				}
			}
		}
		
		
		
		return result; 
	}
	
}
