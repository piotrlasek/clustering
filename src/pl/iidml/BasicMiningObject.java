/**
 * 
 */
package pl.iidml;

import javax.datamining.MiningObject;
import java.util.Date;

/**
 * @author Piotrek
 *
 */
public abstract class BasicMiningObject implements MiningObject {

	Date creationDate;
	String creationInfo;
	String name;
	String objectIdentifier;
	String description;

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public String getCreatorInfo() {
		return "PL";
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getObjectIdentifier() {
		return null;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

}
