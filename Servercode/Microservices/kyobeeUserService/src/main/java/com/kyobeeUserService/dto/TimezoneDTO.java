package com.kyobeeUserService.dto;

public class TimezoneDTO {
	private Integer timezoneId;
	private String timezoneName;
	private String offset;

	public Integer getTimezoneId() {
		return timezoneId;
	}

	public void setTimezoneId(Integer timezoneId) {
		this.timezoneId = timezoneId;
	}

	public String getTimezoneName() {
		return timezoneName;
	}

	public void setTimezoneName(String timezoneName) {
		this.timezoneName = timezoneName;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public TimezoneDTO(Integer timezoneId, String timezoneName, String offset) {
		this.timezoneId = timezoneId;
		this.timezoneName = timezoneName;
		this.offset = offset;
	}
}
