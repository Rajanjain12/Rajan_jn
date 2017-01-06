package com.kyobee.util;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.kyobee.exception.RsntException;

/*
 * 
 */
// TODO - this method needs to be called after the deployment to setup props - Rohit
@Component
@Scope(value = "singleton")
public class AppInitializer {
	
/*	@Logger
    private Log log;*/
	private Logger log = Logger.getLogger(AppInitializer.class);

    /**
     * initialize() method will be called once the server is started.
     */



    public String appVersion = "1.6";

    private String serverName;
    
    private int maxLayoutMarkers;
    
    public static String importPath;
    public static String applePusherPath;
    public static String freePlanId;
    public static String adAccessCharge;
    public static String stripeApiKey;
    public static String adImagePath;
    public static String rmsThankYou;
    public static String rmsWelcome;
    public static String rmsActSuccess;
    public static String defaultUserEmail;
    public static String defaultEmailSubject;
    public static String appLinkApple;
    public static String appLinkAndroid;
    public static String appLinkImg;
    public static String appLinkAppleText;
    public static String androidLinkImg;
    public static String appLinkAndroidText;
    public static String residualIncText;
    public static String residualInc;
    public static String rmsActAddInfo;
    public static String reminderText;
    public static String renewReminderDays;
    public static String staffActTextPart1;
    public static String staffActTextPart2;
    public static String domainNameUrl;
    public static String bccRcpntEmailId;
    public static String qrCodeEmailText;
    public static String qrCodeGroupEmail;
    public static String monthlyStatementText;
    public static String monthlyReportOrgIdStart;
    public static String appleCertPassword;
    public static String googleAPIKey;
    public static String supportEmail;
    public static String appImgResourcePath;
    public static String appImangeExtPath;
    public static String mapplicflepath;
    public static String environment;
    //setup logo file system
    public static String appExtPathOrganization;
    //Added by Aditya
    public static String orgLogoPath;
    //Added by Shruti
    public static String orgLogoPathHtml;
    public static String pusherChannelEnv;
    public static String dashBoardChannel;
    public String footerMsg;
    public static String staticFooterMsg;
	public String smsRouteId;
	public String smsRouteOrgId; 

    /**
     * rpmPropertiesMap will contain all the props defined in rpm.properties at application startup.
     */

    @PostConstruct
    public void initialize() throws RsntException {
        loadVersionProps();
       
    }

    /*
     * This function will be used to load the properties file.
     */
    public void loadProps() {
        try {
           
          
        } catch (final Exception e) {
            // log.debug("Error comes while load props", e);
            log.error(e);
        }
    }

    public void loadVersionProps() {
        try {
            //Properties properties = new Properties();
            Properties extProperties = new Properties();
            Properties loadToJvm = new Properties();
            //properties.load(this.getClass().getClassLoader().getResourceAsStream("appVersion.properties"));
            
            extProperties.load(this.getClass().getClassLoader().getResourceAsStream("rsnt.properties"));
            
            Properties sysProps = new Properties(System.getProperties());
            mapplicflepath = sysProps.getProperty("jboss.server.base.dir");
            //sysProps.load(this.getClass().getClassLoader().getResourceAsStream("waitlist.properties"));
            sysProps.load(this.getClass().getClassLoader().getResourceAsStream("waitlist.properties"));
			System.setProperties(sysProps);
			System.getProperties().list(System.out);
            //appVersion = properties.getProperty("majorBuildNo");
            //appVersion = appVersion + properties.getProperty("minorBuildNo");
            //maxLayoutMarkers = new Integer(properties.getProperty("maxLayoutMarkers"));
            
            pusherChannelEnv = extProperties.getProperty("pusherChannelEnv");
            dashBoardChannel = extProperties.getProperty("dashBoardChannel");
            environment = extProperties.getProperty("environment");
            importPath = extProperties.getProperty("importPath");
            applePusherPath = extProperties.getProperty("applePusherPath");
            freePlanId = extProperties.getProperty("freePlanId");
            adAccessCharge = extProperties.getProperty("adAccessCharge");
            stripeApiKey = extProperties.getProperty("stripeApiKey");
            adImagePath = extProperties.getProperty("adImagePath");
            rmsThankYou = extProperties.getProperty("rmsThankYou");
            rmsWelcome = extProperties.getProperty("rmsWelcome");
            rmsActSuccess = extProperties.getProperty("rmsActSuccess");
            defaultUserEmail = extProperties.getProperty("defaultUserEmail");
            defaultEmailSubject = extProperties.getProperty("defaultEmailSubject");
            appLinkApple= extProperties.getProperty("appLinkApple");
            appLinkImg=extProperties.getProperty("appLinkImg");
            appLinkAndroid = extProperties.getProperty("appLinkAndroid");
            androidLinkImg=extProperties.getProperty("androidLinkImg");
            appLinkAppleText= extProperties.getProperty("appLinkAppleText");
            appLinkAndroidText = extProperties.getProperty("appLinkAndroidText");
            residualIncText= extProperties.getProperty("residualIncText");
            residualInc = extProperties.getProperty("residualInc");
            rmsActAddInfo = extProperties.getProperty("rmsActAddInfo");
            reminderText =  extProperties.getProperty("reminderText");
            renewReminderDays = extProperties.getProperty("renewReminderDays");
            staffActTextPart1 = extProperties.getProperty("staffActTextPart1");
            staffActTextPart2 = extProperties.getProperty("staffActTextPart2");
            domainNameUrl = extProperties.getProperty("domainNameUrl");
            bccRcpntEmailId = extProperties.getProperty("bccRcpntEmailId");
            qrCodeEmailText = extProperties.getProperty("qrCodeEmailText");
            qrCodeGroupEmail = extProperties.getProperty("qrCodeGroupEmail");
            monthlyStatementText= extProperties.getProperty("monthlyStatementText");
            monthlyReportOrgIdStart = extProperties.getProperty("monthlyReportOrgIdStart");
            appleCertPassword  = extProperties.getProperty("appleCertPassword");
            googleAPIKey = extProperties.getProperty("googleAPIKey");
            supportEmail = extProperties.getProperty("supportEmail");
            appImgResourcePath = extProperties.getProperty("appImgResourcePath");
            appImangeExtPath = extProperties.getProperty("appImangeExtPath");
            
            //setup logo file system
            appExtPathOrganization = extProperties.getProperty("appExtPathOrganization");
            
            //Added on 04/12/2015
            //adImagePath = extProperties.getProperty("adImagePath");
            orgLogoPath = extProperties.getProperty("orgLogoPath");
            //End
            orgLogoPathHtml = extProperties.getProperty("orgLogoPathHtml");
            footerMsg = extProperties.getProperty("footerMsg");
            staticFooterMsg = extProperties.getProperty("footerMsg");
            smsRouteId = extProperties.getProperty("smsRouteId");
            smsRouteOrgId = extProperties.getProperty("smsRouteOrgId");

            // Removed the path from the appVersion.properties and moved just a single path ITSM_PATH to
            // props/conf/rpm.properties file.
            /*
             * String env = properties.getProperty("env");
             * if(env.equals("PROD")) {
             * itsmPath = properties.getProperty("Prod");
             * } else if(env.equals("DEV")) {
             * itsmPath = properties.getProperty("Dev");
             * } else {
             * itsmPath = properties.getProperty("QA");
             * }
             */
          

        } catch (final IOException e) {
            // log.debug("Error comes while load props", e);
            log.error(e);
        }
    }
    
    
   /* private void loadRPMProperties() throws RPMException {
        try {
            RPM_PROPERTIES_MAP = new HashMap<String, String>();
            Properties props = new Properties();
            props.load(this.getClass().getClassLoader().getResourceAsStream("props/rpm.properties"));
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.IMPORT_PATH);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.SEND_TO_DEFECT_REPORT);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.QUARTZ_DEBUG);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.QUARTZ_INTERVAL_TIME);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.QUERY_EXEC_AVG_TIME);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.ENTITY_LOAD_FETCH_COUNT);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.EXT_JS_FILE);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.LOG_PHASE);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.OLAP_CUBE_SERVER);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.OLAP_CUBE_CATALOG);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.MAIL_SMTP_HOST);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.MAIL_SMTP_PORT);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.ITSM_PATH);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.RUN_ALERTS_QUEUE_CHECKER);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.RUN_AVAILS_SESSION_QUEUE_CHECKER);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.RUN_PRODUCT_LIBRARY_CHECKER);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.DATABASE_CONNECTION_URL);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.SCHEMA_NAME);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.SCHEMA_PASSWORD);
            addPropertyToRPMPropsMap(props, RPMConstants.RPMPropsKey.SERVER_NAME);

        } catch (IOException exception) {
            log.error("Error Occured while Initializing RPM, Loading Properties", exception);
            throw new RPMException("Error Occured while Initializing RPM, Loading Properties", exception);
        }
    }*/

  /*  private String addPropertyToRPMPropsMap(Properties props, String key) {
        return RPM_PROPERTIES_MAP.put(key, props.getProperty(key) == null ? "" : props.getProperty(key).toString());
    }*/
    

    /*
     * .......................Getters and Setters section...............................
     */
    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }


    public static String getEnvironment() {
		return environment;
	}

	public static void setEnvironment(String environment) {
		AppInitializer.environment = environment;
	}

	public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

	public int getMaxLayoutMarkers() {
		return maxLayoutMarkers;
	}

	public void setMaxLayoutMarkers(int maxLayoutMarkers) {
		this.maxLayoutMarkers = maxLayoutMarkers;
	}
	public String getFooterMsg() {
		return footerMsg;
	}
	
	public void setFooterMsg(String footerMsg) {
		this.footerMsg = footerMsg;
	}
	 public String getSmsRouteId() {
			return smsRouteId;
		}

		public void setSmsRouteId(String smsRouteId) {
			this.smsRouteId = smsRouteId;
		}


		public String getSmsRouteOrgId() {
			return smsRouteOrgId;
		}

		public void setSmsRouteOrgId(String smsRouteOrgId) {
			this.smsRouteOrgId = smsRouteOrgId;
		}

}
