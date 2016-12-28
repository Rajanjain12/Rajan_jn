package com.telerivet;

import java.util.Iterator;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author youngj
 */
public abstract class Entity {
    protected TelerivetAPI api;
    protected JSONObject data;
    protected boolean isLoaded;
    protected CustomVars vars;
    protected JSONObject dirty = new JSONObject();
    
    public Entity(TelerivetAPI api, JSONObject data)
    {
        this(api, data, true);
    }
    
    public Entity(TelerivetAPI api, JSONObject data, boolean isLoaded)
    {
        this.api = api;
        this.setData(data);
        this.isLoaded = isLoaded;
    }
    
    public void setData(JSONObject data)
    {
        this.data = data;
        if (data.has("vars"))
        {
            try {
				this.vars = new CustomVars(data.getJSONObject("vars"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else
        {
            this.vars = new CustomVars(new JSONObject());
        }                
    }
    
    public void load() throws IOException
    {
        if (!isLoaded)
        {
            isLoaded = true;
            setData((JSONObject) api.doRequest("GET", getBaseApiPath()));
            
            Iterator dirtyIter = dirty.keys();
            while (dirtyIter.hasNext()) 
            {
                String key = (String)dirtyIter.next();
                try {
					data.put(key, dirty.get(key));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }        
    }
    
    public Object get(String name)
    {               
        if (data.has(name))
        {
            try {
				return Util.convertNull(data.get(name));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        assertLoaded();
        return null;
    }
    
    protected void assertLoaded()
    {
        if (!isLoaded)
        {
            throw new RuntimeException("Entity data is not loaded yet; call load() first");
        }
    }
    
    public CustomVars vars()
    {
        return this.vars;
    }
    
    public void set(String name, Object value)
    {
        try {
			data.put(name, value);
			dirty.put(name, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void save() throws IOException
    {
        JSONObject dirtyProps = dirty;
        if (vars != null)
        {
            JSONObject dirtyVars = vars.getDirtyVariables();
            if (dirtyVars.length() > 0)
            {
                try {
					dirtyProps.put("vars", dirtyVars);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        api.doRequest("POST", getBaseApiPath(), dirtyProps);
        dirty = new JSONObject();
        if (vars != null)
        {
            vars.clearDirtyVariables();
        }
    }
    
    @Override
    public String toString()
    {    
        String res = getClass().getName();
        if (!isLoaded)
        {
            res += " (not loaded)";
        }
        try {
			res += " JSON: " + data.toString(1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return res;
    }
    
    public abstract String getBaseApiPath() throws IOException;
    
    
    public class CustomVars
    {
        private JSONObject dirty = new JSONObject();
        private JSONObject vars;
    
        public CustomVars(JSONObject vars)
        {
            this.vars = vars;
        }
    
        public JSONObject all()
        {
            return vars;
        }
    
        public JSONObject getDirtyVariables()
        {
            return dirty;
        }
    
        public void clearDirtyVariables()
        {
            dirty = new JSONObject();
        }

        public Object get(String name)
        {
            return Util.convertNull(vars.opt(name));
        }

        public long getLong(String name)
        {
            return ((Long)get(name)).longValue();
        }

        public int getInt(String name)
        {
            return ((Long)get(name)).intValue();
        }

        public boolean getBoolean(String name)
        {
            return ((Boolean)get(name)).booleanValue();
        }

        public String getString(String name)
        {
            return (String)get(name);
        }
        
        public void set(String name, Object value)
        {        
            try {
				vars.put(name, value);
	            dirty.put(name, value);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
    