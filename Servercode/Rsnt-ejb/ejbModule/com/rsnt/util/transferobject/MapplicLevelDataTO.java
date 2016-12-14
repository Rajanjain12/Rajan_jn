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
@Name("mapplicLevelDataTO")
public class MapplicLevelDataTO  implements Serializable{
	
	
	@JsonProperty
	private String id;
   
   @JsonProperty
   private String name;
   
    @JsonProperty
    private String title;
    
    @JsonProperty
    private String map;
    
    @JsonProperty
    private String minimap;
    
    @JsonProperty("mapplicLocaionsDataTO")
    private List<MapplicLocaionsDataTO> locations;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getMinimap() {
		return minimap;
	}

	public void setMinimap(String minimap) {
		this.minimap = minimap;
	}

	public List<MapplicLocaionsDataTO> getLocations() {
		return locations;
	}

	public void setLocations(List<MapplicLocaionsDataTO> locations) {
		this.locations = locations;
	}
	
}
