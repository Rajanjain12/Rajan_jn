package com.rsnt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;

import com.rsnt.common.entity.BaseEntity;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "LOOKUP")
@Name("Lookup")
@NamedQueries({
	@NamedQuery(name = Lookup.GET_PLAN_LIST,
            query = "Select l from Lookup l where l.lookupTypeId = 2",
                    hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class Lookup extends BaseEntity{

	public static final String GET_PLAN_LIST = "getPlanList";
	 	@Id
	    @Column(name = "LookupID")
	    private Long lookupId;

	    @Column(name = "name")
	    private String name;

	    @Column(name = "code")
	    private String code;

	    @Column(name = "lookupTypeID")
	    private String lookupTypeId;

		public Long getLookupId() {
			return lookupId;
		}

		public void setLookupId(Long lookupId) {
			this.lookupId = lookupId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getLookupTypeId() {
			return lookupTypeId;
		}

		public void setLookupTypeId(String lookupTypeId) {
			this.lookupTypeId = lookupTypeId;
		}

	   
}
