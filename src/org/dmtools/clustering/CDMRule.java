package org.dmtools.clustering;

import java.util.ArrayList;

import javax.datamining.JDMException;
import javax.datamining.rule.Predicate;
import javax.datamining.rule.Rule;
import javax.datamining.rule.RuleTranslationFormat;

public class CDMRule implements Rule {

	String rule;
	
	public void setRule(String rule)
	{
		this.rule = rule;
	}
	
	@Override
	public long getAbsoluteSupport() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Predicate getAntecedent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getConfidence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Predicate getConsequent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRuleIdentifier() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSupport() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String translate() throws JDMException {
		// TODO Auto-generated method stub
		return rule;
	}

	@Override
	public String translate(RuleTranslationFormat arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

}
