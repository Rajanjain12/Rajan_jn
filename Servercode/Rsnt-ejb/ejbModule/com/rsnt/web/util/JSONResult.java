/**
 * JSONResult is an wrapper class created to implement the infinte scrolling grid.
 * Using this class, the list of objects are embedded in a class with actual count of the objects
 * in the db. To implement scrollable pagination, you need to send the JSONResult<T> object instead of
 * List<T> to the JSON provider. And the returning JSON object contains the totalCount property
 * with the all data objects as json collection.
 * The dataobject have to be specify in the @xmlSeeAlso annotation used below.
 * @author bpatil
 */

package com.rsnt.web.util;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.rsnt.util.transferobject.LayoutDataTO;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ LayoutDataTO.class})
public class JSONResult<T> implements Serializable {

    private static final long serialVersionUID = 29961515987988929L;

    private List<T> data;

    private long totalCount;

    private boolean success;

    public JSONResult() {
        super();
        totalCount = 0;
        success = false;
    }

    public JSONResult(final List<T> data, final long totalCount) {
        this.data = data;
        this.totalCount = totalCount;
        this.success = true;
    }

    public JSONResult(final List<T> data, final long totalCount,
            final boolean success) {
        this.data = data;
        this.totalCount = totalCount;
        this.success = success;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(final List<T> data) {
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }
}
