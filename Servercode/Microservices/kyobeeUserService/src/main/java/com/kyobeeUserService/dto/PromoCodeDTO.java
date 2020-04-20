package com.kyobeeUserService.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PromoCodeDTO {

	private String promoCode;
	private Date validStartDate;
	private Date validEndDate;
	private BigDecimal flatAmt;
	private BigDecimal percAmt;
	private String country;

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Date getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

	public Date getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public BigDecimal getFlatAmt() {
		return flatAmt;
	}

	public void setFlatAmt(BigDecimal flatAmt) {
		this.flatAmt = flatAmt;
	}

	public BigDecimal getPercAmt() {
		return percAmt;
	}

	public void setPercAmt(BigDecimal percAmt) {
		this.percAmt = percAmt;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
