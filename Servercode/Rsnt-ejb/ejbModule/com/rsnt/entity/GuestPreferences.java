package com.rsnt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Formula;


public class GuestPreferences  implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="GUEST_PREF_ID")
	private Long guestPrefId;
	@Column(name="PREF_ID")
	private Long prefValueId;
	@Formula("(SELECT l.NAME FROM LOOKUP l WHERE l.LOOKUPID=PREF_ID)")
	private String prefValue;
	@JoinColumn(name="GUEST_ID" ,nullable=false)
	@ManyToOne
	@JsonBackReference
	private Guest guest;
	public Long getGuestPrefId() {
		return guestPrefId;
	}
	public void setGuestPrefId(Long guestPrefId) {
		this.guestPrefId = guestPrefId;
	}
	public Long getPrefValueId() {
		return prefValueId;
	}
	public void setPrefValueId(Long prefValueId) {
		this.prefValueId = prefValueId;
	}
	public String getPrefValue() {
		return prefValue;
	}
	public void setPrefValue(String prefValue) {
		this.prefValue = prefValue;
	}
	public Guest getGuest() {
		return guest;
	}
	public void setGuest(Guest guest) {
		this.guest = guest;
	}
	
	
	
}
