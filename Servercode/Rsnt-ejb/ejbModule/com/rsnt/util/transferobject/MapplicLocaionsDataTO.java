package com.rsnt.util.transferobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("mapplicLocaionsDataTO")
public class MapplicLocaionsDataTO implements Serializable {


		
		@JsonProperty
		private String id;
     
 		@JsonProperty 
 		private String title;
 
 		@JsonProperty
 		private String about;
 		
 		@JsonProperty
 		private String description;
 		
 		@JsonProperty
 		private String category;
 		
        @JsonProperty
        private String link;
        
        @JsonProperty
        private double x;
        
        @JsonProperty
        private double y;
        
        @JsonProperty
        private String zoom;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAbout() {
			return about;
		}

		public void setAbout(String about) {
			this.about = about;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public String getZoom() {
			return zoom;
		}

		public void setZoom(String zoom) {
			this.zoom = zoom;
		}
}
