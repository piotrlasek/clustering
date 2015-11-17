package org.dmtools.clustering;

import java.util.ArrayList;

public class CDMRuleOperator {

	ArrayList<CDMPredicate> predicates;
	
	public boolean and()
	{
		return false;
	}
	
	public boolean or()
	{
		return true;
	}
	
	
}
