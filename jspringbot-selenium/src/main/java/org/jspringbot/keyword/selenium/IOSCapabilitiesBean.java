package org.jspringbot.keyword.selenium;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.InitializingBean;
import org.uiautomation.ios.IOSCapabilities;

public class IOSCapabilitiesBean implements InitializingBean {
    private static final Logger LOGGER = Logger.getLogger(IOSCapabilitiesBean.class);

    private DesiredCapabilities capabilities;
    
    private String browser = "Safari";

    public IOSCapabilitiesBean(DesiredCapabilities capabilities, String device) {
        this.capabilities = capabilities;
        if(device.equalsIgnoreCase("iphone")){
    		this.capabilities = IOSCapabilities.iphone(browser);
    	}
    }    
    
    public void setDeviceId(String uuid) {
    	if(!StringUtils.equalsIgnoreCase(uuid, "none")) {
    		capabilities.setCapability(IOSCapabilities.UUID, uuid);
    	}
    }
    
    public void setSimulatorMode(boolean mode){
    	capabilities.setCapability(IOSCapabilities.SIMULATOR, mode);
    }
   
    public void setBrowserName(String browserName) {
        if(!StringUtils.equalsIgnoreCase(browserName, "none")) {
            capabilities.setBrowserName(browserName);
        }
    }

    public void setVersion(String version){
        if(!StringUtils.equalsIgnoreCase(version, "none")) {
            capabilities.setVersion(version);
        }
    }

    public void setPlatform(String platform){
        if(!StringUtils.equalsIgnoreCase(platform, "none")) {
            capabilities.setPlatform(Platform.valueOf(platform));
        }
    }


    @SuppressWarnings("unchecked")
    public void setCapabilities(String properties) throws JSONException {
        if(!StringUtils.equalsIgnoreCase(properties, "none")) {
            JSONObject obj = new JSONObject(properties);

            Iterator<String> itr = obj.keys();
            while(itr.hasNext()) {
                String key = itr.next();
                capabilities.setCapability(key, obj.getString(key));
            }
        }
    }


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
