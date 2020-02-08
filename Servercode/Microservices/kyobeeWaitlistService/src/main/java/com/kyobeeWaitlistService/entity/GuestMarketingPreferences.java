package com.kyobeeWaitlistService.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="GUESTMARKETINGPREFERENCES")
public class GuestMarketingPreferences {

	@Id
	@Column(name="GUEST_MARKETING_PREF_ID")
	@GeneratedValue
	private Integer guestMarketingPrefId;
	
	@OneToOne
	@JoinColumn(name="PREF_ID")
	private Lookup lookup;
	
	@ManyToOne
	@JoinColumn(name="GUEST_ID")
	private Guest guest;

	public Integer getGuestMarketingPrefId() {
		return guestMarketingPrefId;
	}

	public void setGuestMarketingPrefId(Integer guestMarketingPrefId) {
		this.guestMarketingPrefId = guestMarketingPrefId;
	}

	public Lookup getLookup() {
		return lookup;
	}

	public void setLookup(Lookup lookup) {
		this.lookup = lookup;
	}

	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
	}
	
	
}