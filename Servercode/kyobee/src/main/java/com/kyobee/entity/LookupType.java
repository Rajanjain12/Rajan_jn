package com.kyobee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "LOOKUPTYPE")
public class LookupType extends BaseEntity{
	
		@Id
	    @Column(name = "LookupTypeID")
	    private long lookupTypeID;

	    @Column(name = "Name")
	    private String name;

		public long getLookupTypeID() {
			return lookupTypeID;
		}

		public void setLookupTypeID(long lookupTypeID) {
			this.lookupTypeID = lookupTypeID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	   
}
