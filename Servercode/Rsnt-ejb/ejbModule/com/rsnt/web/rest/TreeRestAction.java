package com.rsnt.web.rest;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

@Path("/treeRestAction")
@Name("TreeRestAction")
public class TreeRestAction {

	 	@Logger
	    private Log log;
	    
	 	 @GET
	     @Path("/loadTreeData")
	     @Produces(MediaType.APPLICATION_JSON)
	     public String loadTreeData(){
	 		 
	 		 try{
	             
	             //String data = "[ { \"text\": \"1. Pre Lunch (120 min)\", \"expanded\": true, \"classes\": \"important\", \"children\": [ { \"text\": \"1.1 The State of the Powerdome (30 min)\" }, { \"text\": \"1.2 The Future of jQuery (30 min)\" }, { \"text\": \"1.2 jQuery UI - A step to richnessy (60 min)\" } ] }, { \"text\": \"2. Lunch  (60 min)\" }, { \"text\": \"3. After Lunch  (120+ min)\", \"children\": [ { \"text\": \"3.1 jQuery Calendar Success Story (20 min)\" }, { \"text\": \"3.2 jQuery and Ruby Web Frameworks (20 min)\" }, { \"text\": \"3.3 Hey, I Can Do That! (20 min)\" }, { \"text\": \"3.4 Taconite and Form (20 min)\" }, { \"text\": \"3.5 Server-side JavaScript with jQuery and AOLserver (20 min)\" }, { \"text\": \"3.6 The Onion: How to add features without adding features (20 min)\", \"id\": \"36\", \"hasChildren\": true }, { \"text\": \"3.7 Visualizations with JavaScript and Canvas (20 min)\" }, { \"text\": \"3.8 ActiveDOM (20 min)\" }, { \"text\": \"3.8 Growing jQuery (20 min)\" } ] } ]";
	 			final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
	 			JSONArray dWindowArray = null;
	 			  Map<String, Object> dSuperMap;
	 		      Map<String, Object> dSubMap;
	 		      JSONArray dSubJsonArray = null;
	 		      
	 		      
	 		      
	           
	             final List<String> dataArray = new ArrayList<String>(0);
	             //dataArray.add("{ \"text\": \"1. Pre Lunch (120 min)\", \"expanded\": true, \"classes\": \"important\", \"children\": [ { \"text\": \"1.1 The State of the Powerdome (30 min)\" }");
	             //dataArray.add("{ \"text\": \"1.2 The Future of jQuery (30 min)\" }");
	             //dataArray.add("{ \"text\": \"3. After Lunch  (120+ min)\", \"children\": [ { \"text\": \"3.1 jQuery Calendar Success Story (20 min)\" }, { \"text\": \"3.2 jQuery and Ruby Web Frameworks (20 min)\" }, { \"text\": \"3.3 Hey, I Can Do That! (20 min)\" }, { \"text\": \"3.4 Taconite and Form (20 min)\" }, { \"text\": \"3.5 Server-side JavaScript with jQuery and AOLserver (20 min)\" }, { \"text\": \"3.6 The Onion: How to add features without adding features (20 min)\", \"id\": \"36\", \"hasChildren\": true }, { \"text\": \"3.7 Visualizations with JavaScript and Canvas (20 min)\" }, { \"text\": \"3.8 ActiveDOM (20 min)\" }, { \"text\": \"3.8 Growing jQuery (20 min)\" } ] }");
	             //dataArray.add("{\"title\": \"Item 1\"}");
	             //dataArray.add("{\"title\": \"Folder 2\", \"isFolder\": true, \"key\": \"folder2\", \"children\": [ {\"title\": \"Sub-item 2.1\"}, {\"title\": \"Sub-item 2.2\"} ] }");
	             //dataArray.add("{\"title\": \"Folder 3\", \"isFolder\": true, \"key\": \"folder3\", \"children\": [ {\"title\": \"Sub-item 3.1\"}, {\"title\": \"Sub-item 3.2\"} ] }");
	             //dataArray.add("{\"title\": \"Lazy Folder 4\", \"isFolder\": true, \"isLazy\": true, \"key\": \"folder4\"}");
	             //final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
	             //final List<String> errorArray = new ArrayList<String>(0);
	            
	             //rootMap.put("data", dataArray);
	             dWindowArray = JSONArray.fromObject(new Object[0]);
	             rootMap.put("children", dWindowArray.toString());
	             final JSONObject jsonObject = JSONObject.fromObject(rootMap);
	             
	             
	             //final JSONObject jsonObject = JSONObject.fromObject(rootMap);
	             log.info(jsonObject.toString());
	             return jsonObject.toString();
	             //return dataArray.toString();
	         }
	         catch(Exception e){
	             e.printStackTrace();
	         }
	        
	         
	         return "fail";
	         
	 
	 	 }
}
