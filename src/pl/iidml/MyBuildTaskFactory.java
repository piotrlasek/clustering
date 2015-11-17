package pl.iidml;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.MiningFunction;
import javax.datamining.task.BuildTask;
import javax.datamining.task.BuildTaskCapability;
import javax.datamining.task.BuildTaskFactory;


public class MyBuildTaskFactory implements BuildTaskFactory {

	@Override
	public BuildTask create(String buildData, String buildSettingsName,
			String modelName)
			throws JDMException {
		// TODO Auto-generated method stub
		MyBuildTask mbt = new MyBuildTask();
		mbt.setBuildDataName(buildData);
		mbt.setBuildSettingsName(buildSettingsName);
		mbt.setModelName(modelName);
		return mbt;
	}

	@Override
	public boolean supportsCapability(MiningFunction arg0,
			MiningAlgorithm arg1, BuildTaskCapability arg2) throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

}
