package com.kyobee.dto.common;

import java.util.List;

public class PaginationReqParam {

	private List<FilterParam> filters;

	private String sort;

	private String sortOrder;

	private Integer pageSize;

	private Integer pageNo;

	public List<FilterParam> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterParam> filters) {
		this.filters = filters;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

}
