package org.dmtools.clustering.model;

/**
 * Created by Piotr on 06.01.2017.
 */
public interface IConstraintObject extends ISpatialObject {

    boolean isCannotLinkPoint();
    boolean isMustLinkPoint();
    boolean wasDeferred();

    void isCannotLinkPoint(boolean isCannotLinkPoint);
    void isMustLinkPoint(boolean isMustLinkPoint);
    void wasDeferred(boolean wasDeferred);

    IConstraintObject getParentCannotLinkPoint();
    void setParentCannotLinkPoint(IConstraintObject parentCannotLinkPoint);

    int getClusterId();
}
