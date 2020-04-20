package com.kyobeeUserService.dto;

public class OrgCardDetailsDTO {

	private Integer cardNo;
	private String cardType;
	private String cardName;
	private String vaultID;
	
	public Integer getCardNo() {
		return cardNo;
	}
	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getVaultID() {
		return vaultID;
	}
	public void setVaultID(String vaultID) {
		this.vaultID = vaultID;
	}
	
}
