package com.kyobeeUserService.dto;

public class OrgCardDetailsDTO {

	private Integer cardNo;
	private String cardType;
	private String cardName;
	private String vaultID;
	private Integer orgId;
	private Integer customerId;
	
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
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
}
