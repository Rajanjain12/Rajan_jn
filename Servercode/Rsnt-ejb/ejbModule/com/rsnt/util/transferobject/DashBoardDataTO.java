package com.rsnt.util.transferobject;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("dashboardDataTO")
public class DashBoardDataTO implements Serializable {
	@JsonProperty
	private String mapwidth;
	
	@JsonProperty
	private String mapheight;
	
	@JsonProperty("mapplicCatagoryDataTO")
	private List<MapplicCatagryDataTO> categories;
	
	@JsonProperty("mapplicLevelDataTO")
	private List<MapplicLevelDataTO> levels;

	public String getMapwidth() {
		return mapwidth;
	}

	public void setMapwidth(String mapwidth) {
		this.mapwidth = mapwidth;
	}

	public String getMapheight() {
		return mapheight;
	}

	public void setMapheight(String mapheight) {
		this.mapheight = mapheight;
	}

	public List<MapplicCatagryDataTO> getCategories() {
		return categories;
	}

	public void setCategories(List<MapplicCatagryDataTO> categories) {
		this.categories = categories;
	}

	public List<MapplicLevelDataTO> getLevels() {
		return levels;
	}

	public void setLevels(List<MapplicLevelDataTO> levels) {
		this.levels = levels;
	}
}
