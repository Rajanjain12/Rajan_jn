package com.kyobee.util.common;


public class NativeQueryConstants {
	
	public static final String GET_ORG_AUTO_RENEW_OPTION = "SELECT AutoRenew from ORGANIZATION where ORGANIZATIONID = ?1";
	
	public static final String GET_ORGANIZATION_PLAN_TYPE="SELECT count(*) FROM ORGANIZATION OT "+
		 "INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPT ON OPT.ORGANIZATIONPLANID = OT.ACTIVEORGANIZATIONPLANID "+
		 "INNER JOIN "+
		 "PLANFEATURE PLF ON OPT.PLANID=PLF.PLANID "+
			"WHERE PLF.FEATUREID=15 "+
            "AND OT.OrganizationID=?1";
	

	public static final String GET_ALL_LAYOUTS_FOR_ORG= "SELECT OrganizationLayoutID,OrganizationID,LayoutIdentificationName,LayoutCapacity,CreatedBy,CreatedAt,ModifiedBy, " +
			" ModifiedAt,BIN(isDefault),BIN(ActiveFlag) From ORGANIZATIONLAYOUT where organizationId= ?1 ";
	
	//Added by Aditya for Arrange Layout
	public static final String GET_LAYOUTS_BY_LAYOUTID = "SELECT OrganizationLayoutID,OrganizationID,LayoutIdentificationName,LayoutCapacity,CreatedBy,CreatedAt,ModifiedBy, " +
			" ModifiedAt,BIN(isDefault),BIN(ActiveFlag) From ORGANIZATIONLAYOUT where organizationId= ?1 and OrganizationLayoutID = ?2";

	public static final String GET_STAFF_DATA = " SELECT user.*,lkp.description FROM USER user INNER JOIN ORGANIZATIONUSER orgUser "
					+ " ON user.UserID = orgUser.UserId " +
					" inner join USERROLE urole on user.UserID = urole.UserId " +
					" inner join LOOKUP lkp on urole.RoleId = lkp.LookupID  and lkp.LookupTypeID = 2 " +
					" where orgUser.OrganizationID = (?1)" +
					" order by user.CreatedBy, user.Active ";
	
	public static final String GET_OPTION_DATA = " Select * from ORGANIZATIONOPTION where OrganizationID = ?1";

	public static final String GET_ORG_LAYOUT_SELECTED_OPTIONS = "Select OT.OrganizationOptionid, OT.OptionDescription, null Optiontype from ORGANIZATIONLAYOUTOPTION OLT " +
			"	inner join ORGANIZATIONOPTION OT on OLT.OrganizationOptionid = OT.OrganizationOptionid  and OT.organizationid = ?1 where "+ 
			" OLT.Organizationid = ?1 and OLT.OrganizationLayoutId = ?2" +
			" UNION "+ 
			" Select LKP.LOOKUPID, LKP.CODE, LKP.lookuptypeid Optiontype from ORGANIZATIONLAYOUTOPTION OLT "+ 
			" inner join LOOKUP LKP ON OLT.ORGANIZATIONOPTIONID = LKP.LOOKUPID "+
			" WHERE OLT.Organizationid = ?1 and OLT.OrganizationLayoutId =?2 AND LKP.LOOKUPTYPEID=1 ";

	public static final String GET_ORG_ALL_OPTIONS = "Select OT.OrganizationOptionId, OT.OptionDescription "+
					" from ORGANIZATIONOPTION OT where OT.OrganizationId = ?1 "+
					" union "+
					" select LKP.lookuPid, LKP.CODE from ORGANIZATION OT "+
					" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPT ON OPT.ORGANIZATIONPLANID = OT.ACTIVEORGANIZATIONPLANID " +
					" INNER JOIN PLANFEATURE PF ON OPT.PLANID = PF.PLANID "+
					" INNER JOIN LOOKUP LKP ON PF.CHILDFEATUREID  = LOOKUPID AND LKP.LOOKUPTYPEID= 1 "+
					" WHERE OT.ORGANIZATIONID=?1 ";
	
	

	public static final String UPDATE_ORG_DEFAULT_FLAGS = "Update ORGANIZATIONLAYOUT set isDefault = 0 where OrganizationLayoutId not in (?1) ";
	
	public static final String GET_ORG_LAYOUT_DEFAULT_CHECK_FLAG = "Select count(1) from ORGANIZATIONLAYOUT where OrganizationId = ?1 and isDefault = 1";
	
	public static final String GET_PLAN_LOOKUPS = "Select p.planId, p.planName from PLAN p where p.active = 1";
	
	//public static final String GET_PLAN_TYPE_LOOKUPS = "SELECT LKP.LOOKUPID, LKP.NAME FROM LOOKUP LKP INNER JOIN LOOKUPTYPE LKPTYPE ON LKP.LOOKUPTYPEID = LKPTYPE.LOOKUPTYPEID WHERE LKPTYPE.LOOKUPTYPEID = 4 ";
	
	//public static final String GET_CURRENCY_LOOKUPS = "SELECT LKP.LOOKUPID, LKP.NAME FROM LOOKUP LKP INNER JOIN LOOKUPTYPE LKPTYPE ON LKP.LOOKUPTYPEID = LKPTYPE.LOOKUPTYPEID WHERE LKPTYPE.LOOKUPTYPEID = 6 ";
	
	public static final String GET_ALL_LAYOUTS_FOR_ORG_ACTIVE_FILTER = GET_ALL_LAYOUTS_FOR_ORG +  " And ActiveFlag =1  ";
	
	public static final String GET_DEFAULT_ORG_OPTION_LOOKUPS = "Select lkp.Code from LOOKUP lkp where lkp.LookupTypeID = 1";
	
	public static final String GET_PLAN_DATA = "SELECT P.PLANID, P.PLANNAME, IF(P.ACTIVE=1,'Y','N') FROM PLAN P ";
	
	public static final String GET_ADS_DATA = "SELECT ADS.ADSID, ADS.TITLE, IF(ADS.ACTIVE=1,'Y','N'), ADS.WEEKDAYRUN," +
			"	STR_TO_DATE(ADS.STARTDATETIME,'%Y-%m-%d %H:%i'),STR_TO_DATE(ADS.ENDDATETIME,'%Y-%m-%d %H:%i'),  " +
			" IF(ADS.TILLCREDITAVAILABLE=1,'Y','N'), ADM.IMAGE  " +
			" FROM ADS INNER JOIN ADSIMAGE ADM ON ADS.ADSID = ADM.ADSID WHERE ADS.ORGANIZATIONID = ?1 AND ADS.ACTIVE=?2 AND ADM.IMAGETYPEID=33 ";
	
	public static final String GET_FEEDBACK_DATA = "SELECT FQ.FEEDBACKQUESTIONAIREID, FQ.FEEDBACKQUESTIONAIRETITLE, IF(FQ.ACTIVE=1,'Y','N')  FROM FEEDBACKQUESTIONAIRE FQ "+ 
													" INNER JOIN ORGANIZATIONLAYOUTMARKERS OLM ON FQ.ORGANIZATIONID=OLM.ORGANIZATIONID "+
													" WHERE OLM.ORGANIZATIONLAYOUTMARKERID = ?1 ";
	
	public static final String GET_FEEDBACK_DATA_BY_PLANFEATURE = "SELECT PLF.FEATUREID , OLP.PlanID , PLF.ChildFeatureID "+
        "FROM ORGANIZATIONLAYOUTMARKERS OLM "+
        "INNER JOIN ORGANIZATION ORG ON OLM.ORGANIZATIONID=ORG.ORGANIZATIONID "+
        "INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OLP ON ORG.ACTIVEORGANIZATIONPLANID = OLP.ORGANIZATIONPLANID "+
        "LEFT  JOIN PLANFEATURE PLF ON OLP.PLANID =PLF.PLANID "+
        "AND PLF.FEATUREID=?1 "+
        "WHERE OLM.ORGANIZATIONLAYOUTMARKERID= ?1";

	
	public static final String GET_FEEDBACK_DETAIL = "SELECT FDA.FEEDBACKQUESTIONAIREDETAILID, FDA.QUESTIONTEXT,FDA.OPTIONTEXT, IF(FDA.ACTIVE=1,'Y','N') FROM " +
			 			" FEEDBACKQUESTIONAIREDETAIL FDA  INNER JOIN FEEDBACKQUESTIONAIRE FQ ON  FQ.FEEDBACKQUESTIONAIREID=FDA.FEEDBACKQUESTIONAIREID "+
		 				" WHERE FQ.ORGANIZATIONID=?1 AND FQ.ACTIVE=1" ;
	
	public static final String GET_FEEDBACK_DETAIL_ACTIVEQS = "SELECT FDA.FEEDBACKQUESTIONAIREDETAILID, FDA.QUESTIONTEXT,FDA.OPTIONTEXT, IF(FDA.ACTIVE=1,'Y','N') FROM " +
		" FEEDBACKQUESTIONAIREDETAIL FDA  INNER JOIN FEEDBACKQUESTIONAIRE FQ ON  FQ.FEEDBACKQUESTIONAIREID=FDA.FEEDBACKQUESTIONAIREID "+
		" WHERE FQ.ORGANIZATIONID=?1 AND FQ.ACTIVE=1" ;
	
	//The code lookup join is for determining question Type. This will be used for the webservice response
	public static final String GET_FEEDBACK_MOBILE_DATA = "SELECT FDA.QUESTIONTEXT,QTYPELKP.CODE,FDA.OPTIONTEXT FROM "+
							" FEEDBACKQUESTIONAIREDETAIL FDA  INNER JOIN FEEDBACKQUESTIONAIRE FQ ON "+ 
							" FQ.FEEDBACKQUESTIONAIREID=FDA.FEEDBACKQUESTIONAIREID "+
							" INNER JOIN ORGANIZATIONLAYOUTMARKERS OLM ON FQ.ORGANIZATIONID=OLM.ORGANIZATIONID "+
							" INNER JOIN LOOKUP QTYPELKP ON QTYPELKP.LOOKUPID = FDA.QUESTIONTYPEID AND QTYPELKP.LOOKUPTYPEID=17 "+
							" WHERE OLM.ORGANIZATIONLAYOUTMARKERID = ?1 AND FQ.ACTIVE=1 AND FDA.ACTIVE=1";
	
	public static final String GET_FEEDBACK_QUESTIONAIRE_ID = "SELECT FQ.FEEDBACKQUESTIONAIREID  FROM " +
		" FEEDBACKQUESTIONAIRE FQ WHERE FQ.ORGANIZATIONID=?1 AND FQ.ACTIVE=1 " ;
	
	//public static final String GET_PLAN_ALL_ALERT_LOOKUPS = "SELECT LKP.LOOKUPID, LKP.NAME FROM LOOKUP LKP INNER JOIN LOOKUPTYPE LKPTYPE ON LKP.LOOKUPTYPEID = LKPTYPE.LOOKUPTYPEID WHERE LKPTYPE.LOOKUPTYPEID = 1 ";
	
	public static final String GET_LOOKUPS_FOR_LOOKUPTYPE = "SELECT LKP.LOOKUPID, LKP.NAME,LKP.FILTER FROM LOOKUP LKP INNER JOIN LOOKUPTYPE LKPTYPE ON LKP.LOOKUPTYPEID = LKPTYPE.LOOKUPTYPEID WHERE LKPTYPE.LOOKUPTYPEID = ?";
	
	public static final String UPDATE_USER_PASSWORD_USERID = "Update USER u set u.Password = ?1 where u.userId = ?2";
	
	public static final String GET_QRCODE_BTN_DISPLAY = "select count(1) generatedCount from ORGANIZATIONLAYOUTMARKERS where organizationLayoutId= ?1 and layoutMarkerQRCodeGenerated =1";
	
	public static final String GET_DUPLICATE_OPTION = "Select count(1) from ORGANIZATIONOPTION ot where ot.OrganizationId = ?1  and LOWER(ot.OptionDescription) = ?2 ";
	
	/*public static final String GET_MOBILE_ORG_DATA  = 
			//" SELECT  LKP.LOOKUPID, LKP.CODE,LKP.LOOKUPTYPEID FROM ORGANIZATIONLAYOUTMARKERS OLM  " + 
			//" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OP ON OLM.ORGANIZATIONID = OP.ORGANIZATIONID " + 
			//" INNER JOIN PLANFEATURE PL ON OP.PLANID = PL.PLANID " + 
			//" INNER JOIN LOOKUP LKP ON PL.CHILDFEATUREID = LKP.LOOKUPID AND LKP.LOOKUPTYPEID = 1 " + 
			//" WHERE OLM.ORGANIZATIONLAYOUTMARKERID= ?1 " + 
			" SELECT   DISTINCT LKP.LOOKUPID, LKP.CODE, LKP.lookuptypeid FROM ORGANIZATIONLAYOUTMARKERS OLM  " + 
			" INNER JOIN ORGANIZATIONLAYOUT OT ON OLM.ORGANIZATIONLAYOUTID= OT.ORGANIZATIONLAYOUTID AND OLM.ORGANIZATIONID=OT.ORGANIZATIONID " + 
			" INNER JOIN ORGANIZATIONLAYOUTOPTION OLT ON OT.ORGANIZATIONLAYOUTID = OLT.ORGANIZATIONLAYOUTID AND OT.ORGANIZATIONID=OLT.ORGANIZATIONID " + 
			" INNER JOIN LOOKUP LKP ON OLT.ORGANIZATIONOPTIONID = LKP.LOOKUPID AND LKP.LOOKUPTYPEID =1 "+
			" WHERE  OLM.ORGANIZATIONLAYOUTMARKERID= ?1 AND EXISTS ( SELECT 1 FROM ORGANIZATIONPLANSUBSCRIPTION OPL INNER JOIN " +
			" PLANFEATURE PLF ON OPL.PLANID=PLF.PLANID "+
			" WHERE OPL.ORGANIZATIONID = OLM.ORGANIZATIONID  "+
			" AND PLF.CHILDFEATUREID = LKP.LOOKUPID AND PLF.FEATUREID=1 "+ 
			"	)"  +
			" UNION " + 
			" SELECT  DISTINCT OPT.ORGANIZATIONOPTIONID, OPT.OPTIONDESCRIPTION,NULL FROM ORGANIZATIONLAYOUTMARKERS OLM  " + 
			" INNER JOIN ORGANIZATIONLAYOUT OT ON OLM.ORGANIZATIONLAYOUTID= OT.ORGANIZATIONLAYOUTID AND OLM.ORGANIZATIONID=OT.ORGANIZATIONID " + 
			" INNER JOIN ORGANIZATIONLAYOUTOPTION OLT ON OT.ORGANIZATIONLAYOUTID = OLT.ORGANIZATIONLAYOUTID AND OT.ORGANIZATIONID=OLT.ORGANIZATIONID " + 
			" INNER JOIN ORGANIZATIONOPTION OPT ON OLT.ORGANIZATIONOPTIONID = OPT.ORGANIZATIONOPTIONID AND OLT.ORGANIZATIONID = OPT.ORGANIZATIONID " + 
			" WHERE OLM.ORGANIZATIONLAYOUTMARKERID= ?1 AND EXISTS ( SELECT 1 FROM ORGANIZATIONPLANSUBSCRIPTION OPL INNER JOIN "+
			" PLANFEATURE PLF ON OPL.PLANID=PLF.PLANID "+
			"	WHERE OPL.ORGANIZATIONID = OLM.ORGANIZATIONID AND PLF.FEATUREID=15)";*/
	
	public static final String GET_MOBILE_ORG_DATA  = 
		//" SELECT  LKP.LOOKUPID, LKP.CODE,LKP.LOOKUPTYPEID FROM ORGANIZATIONLAYOUTMARKERS OLM  " + 
		//" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OP ON OLM.ORGANIZATIONID = OP.ORGANIZATIONID " + 
		//" INNER JOIN PLANFEATURE PL ON OP.PLANID = PL.PLANID " + 
		//" INNER JOIN LOOKUP LKP ON PL.CHILDFEATUREID = LKP.LOOKUPID AND LKP.LOOKUPTYPEID = 1 " + 
		//" WHERE OLM.ORGANIZATIONLAYOUTMARKERID= ?1 " + 
		" SELECT   DISTINCT LKP.LOOKUPID, LKP.CODE, LKP.lookuptypeid FROM ORGANIZATIONLAYOUTMARKERS OLM  " + 
		" INNER JOIN ORGANIZATIONLAYOUT OT ON OLM.ORGANIZATIONLAYOUTID= OT.ORGANIZATIONLAYOUTID AND OLM.ORGANIZATIONID=OT.ORGANIZATIONID " + 
		" INNER JOIN ORGANIZATIONLAYOUTOPTION OLT ON OT.ORGANIZATIONLAYOUTID = OLT.ORGANIZATIONLAYOUTID AND OT.ORGANIZATIONID=OLT.ORGANIZATIONID " + 
		" INNER JOIN LOOKUP LKP ON OLT.ORGANIZATIONOPTIONID = LKP.LOOKUPID AND LKP.LOOKUPTYPEID =1 "+
		" WHERE  OLM.ORGANIZATIONLAYOUTMARKERID= ?1 AND EXISTS ( SELECT 1 FROM ORGANIZATION OT " +
		" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPT ON OPT.ORGANIZATIONPLANID = OT.ACTIVEORGANIZATIONPLANID " +
		" INNER JOIN " +
		" PLANFEATURE PLF ON OPT.PLANID =PLF.PLANID "+
		" WHERE OT.ORGANIZATIONID = OLM.ORGANIZATIONID  "+
		" AND PLF.CHILDFEATUREID = LKP.LOOKUPID AND PLF.FEATUREID=1 "+ 
		"	)"  +
		" UNION " + 
		" SELECT  DISTINCT OPT.ORGANIZATIONOPTIONID, OPT.OPTIONDESCRIPTION,NULL FROM ORGANIZATIONLAYOUTMARKERS OLM  " + 
		" INNER JOIN ORGANIZATIONLAYOUT OT ON OLM.ORGANIZATIONLAYOUTID= OT.ORGANIZATIONLAYOUTID AND OLM.ORGANIZATIONID=OT.ORGANIZATIONID " + 
		" INNER JOIN ORGANIZATIONLAYOUTOPTION OLT ON OT.ORGANIZATIONLAYOUTID = OLT.ORGANIZATIONLAYOUTID AND OT.ORGANIZATIONID=OLT.ORGANIZATIONID " + 
		" INNER JOIN ORGANIZATIONOPTION OPT ON OLT.ORGANIZATIONOPTIONID = OPT.ORGANIZATIONOPTIONID AND OLT.ORGANIZATIONID = OPT.ORGANIZATIONID " + 
		" WHERE OLM.ORGANIZATIONLAYOUTMARKERID= ?1 AND EXISTS ( SELECT 1 FROM ORGANIZATION OT " +
		" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPT ON OPT.ORGANIZATIONPLANID = OT.ACTIVEORGANIZATIONPLANID " +
		" INNER JOIN "+
		" PLANFEATURE PLF ON OPT.PLANID=PLF.PLANID "+
		"	WHERE OT.ORGANIZATIONID = OLM.ORGANIZATIONID AND PLF.FEATUREID=15)";
	
	
	/*public static final String GET_MOBILE_ORG_PLAN_DATA = "SELECT  LKP.lookuptypeid, LKP.LOOKUPID, LKP.CODE FROM ORGANIZATIONLAYOUTMARKERS OLM " +
			" INNER JOIN ORGANIZATION OT ON OLM.ORGANIZATIONID = OT.ORGANIZATIONID "+
			" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPT ON OPT.ORGANIZATIONPLANID = OT.ACTIVEORGANIZATIONPLANID " +
			" INNER JOIN PLANFEATURE PL ON OPT.PLANID = PL.PLANID "+
			" INNER JOIN LOOKUP LKP ON PL.CHILDFEATUREID = LKP.LOOKUPID AND LKP.LOOKUPTYPEID <> 1 "+
			" WHERE OLM.ORGANIZATIONLAYOUTMARKERID= ?1 ";*/
	
//#without the last join will below it fectch all lookups for the lookup, We only need the lookups which are mapped to plan and not others.
	public static final String GET_MOBILE_ORG_PLAN_DATA = " SELECT  DISTINCT LKPT.lookuptypeid, LKPT.NAME,LKP.LOOKUPID, LKP.DESCRIPTION  " +
		" FROM ORGANIZATIONLAYOUTMARKERS OLM " +
		" INNER JOIN ORGANIZATION OT ON OLM.ORGANIZATIONID = OT.ORGANIZATIONID " +
		" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPT ON OPT.ORGANIZATIONPLANID = OT.ACTIVEORGANIZATIONPLANID " +
		" INNER JOIN PLANFEATURE PL ON OPT.PLANID = PL.PLANID " +
		" INNER JOIN LOOKUPTYPE LKPT ON PL.FEATUREID = LKPT.LOOKUPTYPEID AND LKPT.LOOKUPTYPEID <> 1 " +
		" LEFT JOIN LOOKUP LKP ON LKPT.LOOKUPTYPEID = LKP.LOOKUPTYPEID " +
		" AND PL.CHILDFEATUREID = LKP.LOOKUPID " + 
		" WHERE OLM.ORGANIZATIONLAYOUTMARKERID = ?1 ";

	public static final String INSERT_LAYOUT_ALERT_OPTION_REQUEST = 
			" INSERT INTO ORGANIZATIONLAYOUTDASHBOARD (OrganizationID, OrganizationLayoutID, OrganizationLayoutMarkerID, AlertID, RequestReceived, RequestAcknowledged, RequestReceivedDateTime, RequestAcknowledgedDateTime, TotalServingTime, AssignedWaiterID, DeviceId, DeviceMake)  " + 
			" SELECT ORGANIZATIONID, ORGANIZATIONLAYOUTID, ORGANIZATIONLAYOUTMARKERID,ALERTID,1,0,CURRENT_TIMESTAMP(),NULL,NULL,NULL,?3,?4  " + 
			" FROM ( " + 
			" SELECT  LKP.LOOKUPID ALERTID, LKP.CODE,OLM.ORGANIZATIONID ORGANIZATIONID,  " + 
			" OLM.ORGANIZATIONLAYOUTID ORGANIZATIONLAYOUTID,  " + 
			" OLM.ORGANIZATIONLAYOUTMARKERID ORGANIZATIONLAYOUTMARKERID " + 
			" FROM ORGANIZATIONLAYOUTMARKERS OLM  " + 
			" INNER JOIN ORGANIZATION OT ON OLM.ORGANIZATIONID = OT.ORGANIZATIONID " + 
			" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPT ON OPT.ORGANIZATIONPLANID = OT.ACTIVEORGANIZATIONPLANID " +
			" INNER JOIN PLANFEATURE PL ON OPT.PLANID = PL.PLANID " + 
			" INNER JOIN LOOKUP LKP ON PL.CHILDFEATUREID = LKP.LOOKUPID AND LKP.LOOKUPTYPEID = 1 " + 
			" WHERE OLM.ORGANIZATIONLAYOUTMARKERID= ?1 " + 
			" AND LKP.LOOKUPID = ?2 " + 
			"  " + 
			" UNION " + 
			"  " + 
			" SELECT  OPT.ORGANIZATIONOPTIONID ALERTID, OPT.OPTIONDESCRIPTION, OLM.ORGANIZATIONID ORGANIZATIONID, " + 
			" OLM.ORGANIZATIONLAYOUTID ORGANIZATIONLAYOUTID, " + 
			" OLM.ORGANIZATIONLAYOUTMARKERID ORGANIZATIONLAYOUTMARKERID " + 
			" FROM ORGANIZATIONLAYOUTMARKERS OLM  " + 
			" INNER JOIN ORGANIZATIONLAYOUT OT ON OLM.ORGANIZATIONLAYOUTID= OT.ORGANIZATIONLAYOUTID AND OLM.ORGANIZATIONID=OT.ORGANIZATIONID " + 
			" INNER JOIN ORGANIZATIONLAYOUTOPTION OLT ON OT.ORGANIZATIONLAYOUTID = OLT.ORGANIZATIONLAYOUTID AND OT.ORGANIZATIONID=OLT.ORGANIZATIONID " + 
			" INNER JOIN ORGANIZATIONOPTION OPT ON OLT.ORGANIZATIONOPTIONID = OPT.ORGANIZATIONOPTIONID AND OLT.ORGANIZATIONID = OPT.ORGANIZATIONID " + 
			" WHERE OLM.ORGANIZATIONLAYOUTMARKERID= ?1 " + 
			" AND OPT.ORGANIZATIONOPTIONID = ?2 " + 
			"  " + 
			" ) AS T " ;
	
	public static final String GET_DASHBOARD_UNPROCESSED_ALERT = "SELECT COUNT(1) FROM ORGANIZATIONLAYOUTDASHBOARD WHERE ORGANIZATIONLAYOUTMARKERID = ?1 AND ALERTID = ?2 AND " +
			" REQUESTACKNOWLEDGED = 0 AND DEVICEID = ?3";
	
	//Added by Aditya
	public static final String GET_DASHBOARD_ACTIVE_ALERT = "select count(*) from ORGANIZATIONLAYOUTDASHBOARD where OrganizationLayoutMarkerID = ?1 and RequestAcknowledged = 0";
	
	public static final String UPDATE_DASHBOARD_PROCESS_ALERT_REQUEST = "UPDATE ORGANIZATIONLAYOUTDASHBOARD SET REQUESTACKNOWLEDGEDDATETIME = CURRENT_TIMESTAMP(), ASSIGNEDWAITERID = ?3, "+
			" REQUESTACKNOWLEDGED =1, TOTALSERVINGTIME = TIMESTAMPDIFF(SECOND,REQUESTRECEIVEDDATETIME,CURRENT_TIMESTAMP()) "+
			" WHERE ORGANIZATIONLAYOUTMARKERID = ?1 AND ALERTID= ?2 ";
		
		
	public static String GET_ORG_CORDS = " SELECT ADDR.LATITUDE,ADDR.LONGITUDE, LKP.CODE, ORG.CHECKINSPECIALAVAILABLE, ORG.CHECKINSPECIALNOTE,PLF.FEATUREID ,OLM.ORGANIZATIONLAYOUTID" +
		    " FROM ORGANIZATIONLAYOUTMARKERS OLM "+
			" INNER JOIN ORGANIZATION ORG ON OLM.ORGANIZATIONID=ORG.ORGANIZATIONID "+
			" INNER JOIN ADDRESS ADDR ON ORG.ADDRESSID = ADDR.ADDRESSID "+
			" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OLP ON ORG.ACTIVEORGANIZATIONPLANID = OLP.ORGANIZATIONPLANID "+
			" INNER JOIN LOOKUP LKP ON OLP.CURRENCYID = LKP.LOOKUPID "+
			" LEFT  JOIN PLANFEATURE PLF ON OLP.PLANID =PLF.PLANID AND PLF.FEATUREID= 13 "+
			" WHERE OLM.ORGANIZATIONLAYOUTMARKERID=?1";
	
	public static final String GET_DASHBOARD_OPTION_DATA = " SELECT LKP.LOOKUPID,LKP.NAME FROM ORGANIZATIONLAYOUTDASHBOARD OB "+
				" INNER JOIN LOOKUP LKP ON OB.ALERTID = LKP.LOOKUPID "+
				" WHERE OB.ORGANIZATIONLAYOUTMARKERID = ?1 " +
				" AND  OB.REQUESTRECEIVED = 1 "+ 
				" AND OB.REQUESTACKNOWLEDGED = 0 "+
				" UNION "+
				" SELECT OGPT.ORGANIZATIONOPTIONID, OGPT.OPTIONDESCRIPTION FROM ORGANIZATIONLAYOUTDASHBOARD OB "+
				" INNER JOIN ORGANIZATIONOPTION OGPT ON OB.ALERTID = OGPT.ORGANIZATIONOPTIONID "+
				" WHERE OB.ORGANIZATIONLAYOUTMARKERID = ?1 AND OB.ORGANIZATIONID = OGPT.ORGANIZATIONID "+
				" AND  OB.REQUESTRECEIVED = 1 "+
		        " AND OB.REQUESTACKNOWLEDGED = 0 ";
	
/*	public static final String GET_DASHBOARD_ALERT_DATA = "SELECT DISTINCT OD.ORGANIZATIONLAYOUTMARKERID " +
				" FROM ORGANIZATIONLAYOUTDASHBOARD OD WHERE OD.ORGANIZATIONLAYOUTID = ?1 AND " +
				" OD.REQUESTRECEIVED = 1 AND OD.REQUESTACKNOWLEDGED = 0";*/
	
	// Backup on 13-01-2015 for multiple serving requests
//	public static final String GET_DASHBOARD_ALERT_DATA = " SELECT OD.ORGANIZATIONLAYOUTMARKERID, GROUP_CONCAT(OD.ALERTID SEPARATOR ',' ) "+
//				" FROM ORGANIZATIONLAYOUTDASHBOARD OD WHERE OD.ORGANIZATIONLAYOUTID = ?1 AND " +
//				" OD.REQUESTRECEIVED = 1 AND OD.REQUESTACKNOWLEDGED = 0 GROUP BY OD.ORGANIZATIONLAYOUTMARKERID "; 
	
	// vinit changes get only latest request from same marker
	//backup on 06/02/2015
	/*public static final String GET_DASHBOARD_ALERT_DATA = "SELECT o.ORGANIZATIONLAYOUTMARKERID, o.ALERTID FROM  ORGANIZATIONLAYOUTDASHBOARD o WHERE o.ORGANIZATIONLAYOUTDASHBOARDID IN ("+
		"SELECT MAX(ORGANIZATIONLAYOUTDASHBOARDID) FROM ORGANIZATIONLAYOUTDASHBOARD OD WHERE OD.ORGANIZATIONLAYOUTID = ?1 AND OD.REQUESTRECEIVED = 1 AND"+
		" OD.REQUESTACKNOWLEDGED = 0 GROUP BY OD.ORGANIZATIONLAYOUTMARKERID)";*/
	//----------------------------------------- query 1--------------------------------------------
	public static final String GET_DASHBOARD_ALERT_DATA = "  SELECT o.ORGANIZATIONLAYOUTMARKERID, GROUP_CONCAT(o.ALERTID SEPARATOR ',' ) "
				+ "FROM (( SELECT OD.ORGANIZATIONLAYOUTMARKERID, OD.ALERTID,MAX(ORGANIZATIONLAYOUTDASHBOARDID) MAXREQUESTID "
				+ "FROM ORGANIZATIONLAYOUTDASHBOARD OD WHERE OD.ORGANIZATIONLAYOUTID = ?1 AND OD.REQUESTRECEIVED = 1 AND"
				+ " OD.REQUESTACKNOWLEDGED = 0 GROUP BY OD.ORGANIZATIONLAYOUTMARKERID, OD.ALERTID) o) GROUP BY organizationlayoutmarkerid";

	//------------------------------------------- query 2 -----------------------------------------------
	public static final String GET_DEVICE_DETAIL ="SELECT o.DeviceId, o.DeviceMake FROM  ORGANIZATIONLAYOUTDASHBOARD o WHERE"
				+ " o.ORGANIZATIONLAYOUTDASHBOARDID IN (SELECT MAX(ORGANIZATIONLAYOUTDASHBOARDID) MAXREQUESTID FROM ORGANIZATIONLAYOUTDASHBOARD OD "
				+ "WHERE OD.organizationLayoutMarkerID=?1 AND OD.alertid=?2"
				+ " AND OD.REQUESTRECEIVED = 1 AND OD.REQUESTACKNOWLEDGED = 0)";

		
/*	public static final String GET_ORG_SPECIAL_AD_LIST = " SELECT AD.ADSID, AD.CAPTION " +
				" FROM ORGANIZATIONLAYOUTMARKERS OMS INNER JOIN  ADS AD ON  OMS.ORGANIZATIONID = AD.ORGANIZATIONID "+ 
				" WHERE OMS.ORGANIZATIONLAYOUTMARKERID = :1 AND  "+
				"  AD.ACTIVE = 1 AND " +
				" ( ( AD.WEEKDAYRUN LIKE :2 AND  AD.STARTDATETIME <= STR_TO_DATE(:3,'%Y-%m-%d %H:%i')  "+ 
				" AND AD.ENDDATETIME >= STR_TO_DATE(:3,'%Y-%m-%d %H:%i') ) "+ 
				" OR "+ 
				" AD.TILLCREDITAVAILABLE = 1 "+ 
				" ) ";*/
	
	public static final String GET_ORG_SPECIAL_AD_LIST = " SELECT AD.ADSID, AD.TITLE, AIM.IMAGE IMAGEURL " +
				" FROM ORGANIZATIONLAYOUTMARKERS OMS INNER JOIN  ADS AD ON  OMS.ORGANIZATIONID = AD.ORGANIZATIONID "+ 
				" LEFT JOIN ADSIMAGE AIM ON AD.ADSID=AIM.ADSID AND AIM.IMAGETYPEID=33 "+
				" WHERE OMS.ORGANIZATIONLAYOUTMARKERID = :1  AND "+
				"  AD.ACTIVE = 1 AND " +
				" ( ( AD.WEEKDAYRUN LIKE :2 AND  DATE(AD.STARTDATETIME) <= STR_TO_DATE(:3,'%Y-%m-%d')  "+ 
				" AND DATE(AD.ENDDATETIME) >= STR_TO_DATE(:3,'%Y-%m-%d') " +
				" AND EXTRACT(HOUR_MINUTE FROM AD.STARTDATETIME) <= :4 " +
				" AND EXTRACT(HOUR_MINUTE FROM AD.ENDDATETIME) >=:4 " +
				") "+ 
				" OR "+ 
				" AD.TILLCREDITAVAILABLE = 1 "+ 
				" ) ";
	
	
	public static final String GET_ORG_AD_BALANCE = " SELECT ORG.ORGANIZATIONID FROM ORGANIZATIONLAYOUTMARKERS OLM "+
				" INNER JOIN ORGANIZATION ORG ON OLM.ORGANIZATIONID=ORG.ORGANIZATIONID "+
				" WHERE OLM.ORGANIZATIONLAYOUTMARKERID=?1 AND ORG.ADSBALANCE>=5 ";
	
	//public static final String UPDATE_ORG_AD_BALANCE = "UPDATE ORGANIZATION ORG SET ORG.ADSBALANCE = ORG.ADSBALANCE -1 WHERE ORG.ORGANIZATIONID = ?1 ";
	//changed on 01/19/2015 by Aakash, Krunal and Vinit for multiple request on dashboard bug
	/*public static final String GET_DEVICE_DETAIL = "Select omap.DeviceId, omap.DeviceMake from ORGANIZATIONLAYOUTMARKERDEVICEMAP " +
			" omap where omap.organizationLayoutMarkerID = ?1 order by requestDate desc LIMIT 1 ";*/
	/*commented on 02/08/2015 as backup
	 * public static final String GET_DEVICE_DETAIL ="SELECT o.DeviceId, o.DeviceMake FROM  ORGANIZATIONLAYOUTDASHBOARD o WHERE o.ORGANIZATIONLAYOUTDASHBOARDID IN ("+
			"SELECT MAX(ORGANIZATIONLAYOUTDASHBOARDID) FROM ORGANIZATIONLAYOUTDASHBOARD OD WHERE OD.organizationLayoutMarkerID = ?1 AND OD.REQUESTRECEIVED = 1 AND"+
			" OD.REQUESTACKNOWLEDGED = 0 GROUP BY OD.ORGANIZATIONLAYOUTMARKERID)";*/
	
	public static final String DELETE_FEEDBACK_DETAIL = "DELETE FROM FEEDBACKQUESTIONAIREDETAIL where FEEDBACKQUESTIONAIREDETAILID IN (?1) ";	
	
	public static final String UPDATE_AD_STATUS= "UPDATE ADS AD SET AD.ACTIVE = IF(AD.ACTIVE=1,0,1) WHERE AD.ADSID = ?1";
	
	public static final String UPDATE_FEEDBACK_DETAIL_STATUS= "UPDATE FEEDBACKQUESTIONAIREDETAIL FD SET FD.ACTIVE = IF(FD.ACTIVE=1,0,1) WHERE FD.feedbackQuestionaireDetailId = ?1";

	public static final String UPDATE_ACTIVE_ORG_SUBS_PLAN_ID= "UPDATE ORGANIZATION OP SET OP.ACTIVEORGANIZATIONPLANID = ?2 WHERE OP.ORGANIZATIONID = ?1";
	
	public static final String GET_FREE_AD_COUNT_CREDITED_FOR_PLAN = "SELECT SUM(NOOFADS) FROM ORGANIZATIONADCREDIT WHERE ORGANIZATIONID = ?1 AND ORGANIZATIONPLANID = ?2 AND CREDITTYPE=1 ";
	
	public static final String GET_SCHEDULER_ORG_DATA = "SELECT ORG.ORGANIZATIONID,OPL.ORGANIZATIONPLANID,ORG.STRIPECUSTOMERID, ORG.AUTORENEW FROM ORGANIZATION ORG "+
			" INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPL ON ORG.ACTIVEORGANIZATIONPLANID = OPL.ORGANIZATIONPLANID "+
			" WHERE OPL.ENDDATE < DATE(SYSDATE()) "+
			" AND OPL.TERMINATEDATE IS NULL AND  "+
			" ORG.ACTIVE=1 ";
	
	public static final String GET_PLAN_RENEW_REMINDER_DATA= "SELECT ORG.ORGANIZATIONID,OPL.ORGANIZATIONPLANID,ORG.STRIPECUSTOMERID, ORG.AUTORENEW ,OPL.ENDDATE , SYSDATE() "+
			" FROM ORGANIZATION ORG INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPL ON ORG.ACTIVEORGANIZATIONPLANID = OPL.ORGANIZATIONPLANID "+
			" WHERE OPL.ENDDATE = STR_TO_DATE(DATE_ADD(SYSDATE(),INTERVAL ?1 DAY),'%Y-%m-%d') AND OPL.TERMINATEDATE IS NULL "+ 
			" AND ORG.ACTIVE=1";
	
	public static final String GET_EXPIRING_ORG_DATA= "SELECT ORG.ORGANIZATIONID,ORG.ORGANIZATIONNAME, OPL.ENDDATE FROM ORGANIZATION ORG " +
		"  INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPL ON ORG.ACTIVEORGANIZATIONPLANID = OPL.ORGANIZATIONPLANID " +
		"  WHERE OPL.ENDDATE <= STR_TO_DATE(DATE_ADD(SYSDATE(),INTERVAL 1 MONTH),'%Y-%m-%d') AND OPL.TERMINATEDATE IS NULL " +
		"  AND OPL.TERMINATEDATE IS NULL AND  " +
		"  ORG.ACTIVE=1 " ;
	
	public static final String GET_CREATED_ORG_DATA= "SELECT ORG.ORGANIZATIONID,ORG.ORGANIZATIONNAME, OPL.STARTDATE FROM ORGANIZATION ORG " +
		"  INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPL ON ORG.ACTIVEORGANIZATIONPLANID = OPL.ORGANIZATIONPLANID " +
		"  WHERE OPL.STARTDATE >= STR_TO_DATE(DATE_ADD(SYSDATE(),INTERVAL -2 MONTH),'%Y-%m-%d') AND OPL.TERMINATEDATE IS NULL " +
		"  AND OPL.TERMINATEDATE IS NULL AND  " +
		"  ORG.ACTIVE=1 " ;
	
	public static final String GET_MONTHLY_REPORT_DATA= "SELECT ORG.ORGANIZATIONID,OPL.ORGANIZATIONPLANID  "+
			" FROM ORGANIZATION ORG INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPL ON ORG.ACTIVEORGANIZATIONPLANID = OPL.ORGANIZATIONPLANID "+
			" WHERE OPL.TERMINATEDATE IS NULL "+ 
			" AND ORG.ACTIVE=1 AND ORG.ORGANIZATIONID >= ?1 ";
	
	public static final String GET_FEEDBACK_QSTNR_ID = "SELECT FQ.FEEDBACKQUESTIONAIREID, OLM.ORGANIZATIONID FROM FEEDBACKQUESTIONAIRE FQ INNER JOIN ORGANIZATIONLAYOUTMARKERS OLM "+ 
			" ON OLM.ORGANIZATIONID=FQ.ORGANIZATIONID WHERE OLM.ORGANIZATIONLAYOUTMARKERID=?1";
	
	public static final String GET_USER_EMAIL = "SELECT U.USERNAME FROM USER U WHERE U.ACTIVATIONID = ?1";
	
	public static final String UPDATE_USER_ACTIVE_FLAG = "UPDATE USER U SET U.ACTIVE = ?2 WHERE U.ACTIVATIONID= ?1";
	
	public static final String UPDATE_USER_PASSWORD_ACTID = "UPDATE USER U SET PASSWORD = ?1 WHERE U.ACTIVATIONID= ?2";
	
	public static final String UPDATE_USER_ACTID = "UPDATE USER U SET U.ACTIVATIONID = ?1 WHERE U.EMAIL= ?2";
	
	//public static final String GET_HASHED_PASSWORD = "SELECT GET_HASHED_PWD(?1) FROM DUAL ";
	
	public static final String GET_USER_PROTECTED_OBJECT_DATA=" SELECT COUNT(1) FROM USERROLE UR "+
			" INNER JOIN ROLEPROTECTEDOBJECT RPO ON UR.ROLEID= RPO.ROLEID "+ 
			" INNER JOIN PROTECTEDOBJECT PO ON RPO.PROTECTEDOBJECTID=PO.PROTECTEDOBJECTID "+
			" WHERE UR.USERID=?1 AND PO.PROTECTEDOBJECTNAME=?2 ";
	
	public static final String GET_ROLE_PROTECTED_OBJECT_DATA=" SELECT RPO.PROTECTEDOBJECTID," +
			" PO.PROTECTEDOBJECTNAME FROM "+
			" ROLEPROTECTEDOBJECT RPO INNER JOIN PROTECTEDOBJECT PO ON RPO.PROTECTEDOBJECTID=PO.PROTECTEDOBJECTID "+
			" WHERE RPO.ROLEID=?1 ";
			
	public static final String GET_ALL_PROTECTED_OBJECT_DATA=" SELECT PO.PROTECTEDOBJECTID ," +
			" PO.PROTECTEDOBJECTNAME FROM "+
			" PROTECTEDOBJECT PO ";
	
	public static final String DELETE_ALL_ROLE_PROTECTED_OBJECT_DATA=" DELETE FROM "+
			" ROLEPROTECTEDOBJECT WHERE ROLEID=?1";
	
	
	public static final String GET_USER_ROLE_ID =" SELECT LKP.LOOKUPID FROM USER U INNER JOIN USERROLE UR ON U.USERID=UR.USERID "+
			" AND U.USERNAME=?1 INNER JOIN LOOKUP LKP ON UR.ROLEID = LKP.LOOKUPID AND LKP.LOOKUPTYPEID =2 ";

	
	public static final String GET_ORGANIZATION_FEEDBACK_LOOKUP = " SELECT  LKP.LOOKUPID FROM "+
			 " ORGANIZATION OT "+
			 " INNER JOIN ORGANIZATIONPLANSUBSCRIPTION OPT ON OPT.ORGANIZATIONPLANID = OT.ACTIVEORGANIZATIONPLANID "+
			 " INNER JOIN PLANFEATURE PL ON OPT.PLANID = PL.PLANID "+
			 " INNER JOIN LOOKUPTYPE LKPT ON PL.FEATUREID = LKPT.LOOKUPTYPEID AND LKPT.LOOKUPTYPEID = 11 "+
			 " LEFT JOIN LOOKUP LKP ON LKPT.LOOKUPTYPEID = LKP.LOOKUPTYPEID "+
			 " AND PL.CHILDFEATUREID = LKP.LOOKUPID "+
			 " WHERE OT.ORGANIZATIONID= ?1 "; 
	
	public static final String GET_LATEST_AD_BALANCE =" SELECT ACTIVITYSEQ, ADSBALANCE FROM ADUSAGEHISTORYREPORT WHERE ACTIVITYSEQ = ( " +
			" SELECT MAX(ACTIVITYSEQ) FROM ADUSAGEHISTORYREPORT WHERE ORGANIZATIONID = ?1)  AND ORGANIZATIONID = ?1 ";
	
	public static final String GET_ORGANIZATION_ADUSAGE_HISTORY = " SELECT ACTIVITYDATE, ACTIVITYDESC,ADSCREDIT,ADSBALANCE, AMOUNT,NOTES,ACTIVITYSEQ " +
			" FROM ADUSAGEHISTORYREPORT WHERE ORGANIZATIONID = ?1 AND ACTIVITYDATE >= (curdate() - dayofmonth(curdate()) + 1) " +
			" AND ACTIVITYDATE <=CURDATE() ORDER BY ACTIVITYSEQ ";
	
	public static final String CHECK_ADUSAGE_HIST_EXISTS = "SELECT COUNT(1) FROM ADUSAGEHISTORYREPORT WHERE ORGANIZATIONID = ?1" +
			" AND ACTIVITYDATE >= (curdate() - dayofmonth(curdate()) + 1) AND ACTIVITYDATE <=CURDATE() 	AND ACTIVITYREF= 'END_BALANCE' ";
	
	public static final String GET_ANALYTICKS_REPORT = "SELECT OLD.AlertID, L.Description, DATE_FORMAT(OLD.REQUESTRECEIVEDDATETIME,'%Y-%m-%d %h %a'), count(*) 'Totalcount' from  "+
														" ORGANIZATIONLAYOUTDASHBOARD OLD, LOOKUP L WHERE OLD.AlertID=L.LookupID  " +
														" and DATE_FORMAT(OLD.REQUESTRECEIVEDDATETIME,'%Y-%m-%d') >= ?1 " +
														" and DATE_FORMAT(OLD.REQUESTRECEIVEDDATETIME,'%Y-%m-%d') <= ?2"+
														" GROUP BY OLD.AlertID, L.Description, DATE_FORMAT(OLD.REQUESTRECEIVEDDATETIME,'%Y-%m-%d %h %a')";
	public static final String GET_TOP_GUEST_DETAILS = "SELECT guestID,name,uuid,noOfPeople,sms,email,prefType FROM GUEST WHERE STATUS='CHECKIN' AND resetTime is null AND OrganizationID =?1 Limit ?2";
	public static final String GET_TOP_GUEST_DETAILS_NOTMARKED = "SELECT guestID,name,uuid,noOfPeople,sms,email,prefType FROM GUEST WHERE STATUS='CHECKIN' AND incompleteParty is NULL AND calloutCount is NULL AND resetTime is null AND OrganizationID =?1 Limit ?2";

	public static final String UPDATE_ORG_WAITTIME = "UPDATE ORGANIZATION SET waitTime=?1 where OrganizationID =?2";
	public static final String UPDATE_ORG_NOTIFY_COUNT = "UPDATE ORGANIZATION SET notifyUserCount=?1 where OrganizationID =?2";

	public static final String DELETE_GUEST_BY_ID = "UPDATE GUEST SET status='REMOVED' where guestID=?1";
	public static final String GET_ORG_WAIT_TIME = "SELECT waitTime FROM ORGANIZATION where  OrganizationID =:orgId";
	public static final String GET_ORG_NOTIFY_COUNT = "SELECT notifyUserCount FROM ORGANIZATION where  OrganizationID =?1";
	public static final String GET_GUEST_CHECKIN_COUNT = "SELECT count(*) FROM GUEST where  STATUS='CHECKIN' and resetTime is  null and calloutCount is null and incompleteParty is null and OrganizationID =?1 ";
	public static final String GET_GUEST_AHEAD_COUNT = "SELECT count(*) FROM GUEST where  STATUS='CHECKIN' and resetTime is  null and calloutCount is null and incompleteParty is null and guestID<:guestId and OrganizationID =:orgId";
	public static final String GET_ORG_NAME_BY_ID = "SELECT OrganizationName FROM ORGANIZATION where OrganizationId=?1";
	public static final String GET_ORG_GUEST_MAX_RANK = "SELECT IF(max(rank) IS NULL ,0,max(rank))  from GUEST WHERE OrganizationID =?1 and status in('CHECKIN','SEATED','DELETED','REMOVED') and resetTime is null";
	
	//changes by krupali, line 422 to 425 (16/06/2017)
	public static final String HQL_GET_GUESTS_CHECKIN_BY_ORG_COMMON = "FROM Guest g left join fetch g.languagePrefID WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId order by g.rank asc";
	public static final String HQL_GET_GUESTS_CHECKIN_BY_ORG_SMALL = "FROM Guest g left join fetch g.languagePrefID WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId and g.partyType=0 order by g.rank asc";
	public static final String HQL_GET_GUESTS_CHECKIN_BY_ORG_LARGE = "FROM Guest g left join fetch g.languagePrefID WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId and g.partyType=1 order by g.rank asc";
	public static final String HQL_GET_GUESTS_CHECKIN_BY_ORG_BOTH = "FROM Guest g left join fetch g.languagePrefID WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId and (g.partyType in (0,1)) order by g.rank asc";
	public static final String HQL_GET_GUESTS_CHECKIN_BY_ORG_COMMON_SEARCH = "FROM Guest g WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId and g.name=:Name order by g.rank asc";
	//Added Later for Gen enhancement
	public static final String GET_GUESTS_CHECKIN_BY_ORG = "Select @row \\:= @row + 1 AS row, g.* , ( @row * oo.waitTime) WaitTime FROM GUEST g, (SELECT @row \\:= 0) x , ORGANIZATION oo WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId and oo.OrganizationID=:orgId order by g.rank asc";
	public static final String HQL_GET_GUESTS_COUNT_CHECKIN_BY_ORG = "select count(*) FROM Guest g WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId";
	public static final String HQL_GET_GUESTS_HISTORY = "FROM Guest g WHERE g.resetTime is  null and g.status not in ('CHECKIN') and g.OrganizationID=:orgId order by g.rank asc";
	public static final String HQL_GET_GUESTS_COUNT_HISTORY = "select count(*) FROM Guest g WHERE g.resetTime is  null and g.status not in ('CHECKIN') and g.OrganizationID=:orgId order by g.rank asc";
	public static final String HQL_GET_GUEST_BY_UUID = "FROM Guest g WHERE g.status ='CHECKIN' and  g.resetTime is null and uuid=:UUID";
	public static final String GET_ORG_SEATING_PREF_VALUES = "select lkp.LookupID,lkp.Name from ORGANIZATION org inner join ORGANIZATIONCATEGORY oc on org.organizationID=oc.organizationID "+
                                                              " inner join LOOKUP lkp on oc.CategoryValueID=lkp.LookupID where oc.organizationID=:orgId and oc.CategoryTypeID=:catTypeId";
	public static final String GET_ORG_LANGUAGE_PREF_VALUES = "select LangID, LangName, LangIsoCode from "+
																"ORGANIZATIONLANG ol inner join LANGMASTER lm on lm.LangID = ol.LanguageID "+
																"where ol.OrganizationID=:orgId and ol.Active=1";
	public static final String GET_USER_LANGUAGE_PREF_VALUES = "select LangID, LangName, LangIsoCode from LANGMASTER lm where lm.LangID=:langId";
	public static final String GET_ORG_SMS_TEMPLATE_VALUES = "select SmsTemplateID, TemplateText, LanguageID, Level from ORGANIZATIONTEMPLATE ot where ot.OrgID=:orgId and ot.LanguageID=:langID and ot.Active=1";
	public static final String GET_ORG_SMS_TEMPLATE_LEVEL_BY_ID  = "select Level from ORGANIZATIONTEMPLATE ot where ot.OrgID=:orgId and ot.Active=1 and ot.SmsTemplateID=:templateID";
	public static final String GET_ORG_SCREENSAVER = "select ScreensaverFlag, ScreensaverFileName from ORGANIZATION o where o.OrganizationID=:orgId";
	//Added for Guest Reset functionality
	public static final String HQL_GET_GUESTS = "FROM Guest where OrganizationID = :orgId";
	//public static final String HQL_GET_GUESTS_PREFERENCES = "FROM GuestPreferences";
	public static final String HQL_DELETE_GUESTS = "DELETE FROM Guest where OrganizationID = ?1";
	//public static final String HQL_DELETE_GUESTS_PREFERENCES = "DELETE FROM GuestPreferences";

	public static final String GET_ALL_ORG = "select OrganizationID from ORGANIZATION";
	public static final String GET_ORG_GUEST_MIN_CHECKIN_RANK = "SELECT IF(min(rank) IS NULL ,0,min(rank))  from GUEST WHERE calloutCount is null and incompleteParty is null and OrganizationID =:orgId and status in('CHECKIN') and resetTime is null";
	
	
	public static final String HQL_GET_ORGANIZATION = "from Organization where organizationId = ?1";
	public static final String HQL_GET_GUEST_DETAILS_CHECKIN = "from Guest where  status='CHECKIN' and resetTime is  null and OrganizationID =?1";
	public static final String HQL_GET_MIN_RANK_OF_GUEST = "from Guest where  status IN ('CHECKIN','SEATED','DELETED','REMOVED') and resetTime is  null and OrganizationID =?1 order by rank";
	public static final String HQL_GET_MAX_RANK_OF_GUEST = "from Guest where  status IN ('CHECKIN','SEATED','DELETED','REMOVED') and resetTime is  null and OrganizationID =?1 order by rank desc";
	public static final String UPDATE_ORG_TOTAL_WAITTIME_INCREMENT = "update ORGANIZATION set TotalWaitTime = TotalWaitTime +  waitTime where organizationId = ?1";
	public static final String UPDATE_ORG_TOTAL_WAITTIME_DECREMENT = "update ORGANIZATION set TotalWaitTime = TotalWaitTime -  waitTime where organizationId = ?1";
	public static final String HQL_GET_TOP_GUEST_DETAILS_NOTMARKED = "from Guest WHERE status='CHECKIN' AND incompleteParty is NULL AND calloutCount is NULL AND resetTime is null AND OrganizationID =?1";
	public static final String GET_USER_LOGIN_AUTH = "SELECT PASSWORD, ORGUSR.ORGANIZATIONID, ORG.logoFileName, ORG.clientBase , ORG.smsRoute , ORG.MaxParty FROM USER U INNER JOIN ORGANIZATIONUSER ORGUSR ON U.USERID = ORGUSR.USERID INNER JOIN ORGANIZATION ORG ON ORGUSR.ORGANIZATIONID = ORG.ORGANIZATIONID AND ORG.ACTIVEORGANIZATIONPLANID IS NOT NULL WHERE U.USERNAME=:username AND U.ACTIVE=1";
	
	public static final String CHECK_ORGANIZATION_IF_EXISTS = "FROM Organization u WHERE u.organizationName=:orgName";
	public static final String CHECK_USER_IF_EXISTS = "FROM User u WHERE u.userName=:userName";
	
	public static final String GET_SMSROUTE_BY_ORGID = "SELECT o.smsRoute from ORGANIZATION o where o.OrganizationID=:orgId";

	public static final String HQL_GET_GUEST_BY_ID = "FROM Guest g left join fetch g.languagePrefID WHERE g.guestID=:guestId";
}
