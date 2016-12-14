package com.rsnt.util.transferobject;

import java.io.Serializable;
import java.math.BigDecimal;

public class StripeDataTO implements Serializable{

	private String cardNumber;
	private String expMonth;
	private String expYear;
	private String cvv;
	private String customerId;
	private BigDecimal chargeAmount;
	
	public StripeDataTO(BigDecimal pChargeAmount, String pCardNo,String pExpMonth, String pExpYear, String pCvv){
		this.cardNumber = pCardNo;
		this.expMonth = pExpMonth;
		this.expYear = pExpYear;
		this.cvv = pCvv;
		this.chargeAmount=pChargeAmount;
		
	}
	
	public StripeDataTO(BigDecimal pChargeAmount, String pCustId){
		this.customerId = pCustId;
		this.chargeAmount=pChargeAmount;
		
	}


	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
