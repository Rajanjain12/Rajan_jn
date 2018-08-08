package com.kyobee.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StringType;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobee.dto.GuestMarketingPreference;
import com.kyobee.dto.GuestPreferencesDTO;
import com.kyobee.dto.LanguageMasterDTO;
import com.kyobee.dto.OrganizationTemplateDTO;
import com.kyobee.dto.SMSDetailsWrapper;
import com.kyobee.dto.ScreensaverDTO;
import com.kyobee.dto.WaitlistMetrics;
import com.kyobee.dto.common.Response;
import com.kyobee.entity.AddMarketing;
import com.kyobee.entity.Guest;
import com.kyobee.entity.GuestNotificationBean;
import com.kyobee.entity.GuestPreferences;
import com.kyobee.entity.GuestReset;
import com.kyobee.entity.Organization;
import com.kyobee.entity.SmsLog;
import com.kyobee.exception.RsntException;
import com.kyobee.service.IWaitListService;
import com.kyobee.util.AppTransactional;
import com.kyobee.util.common.*;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.NativeQueryConstants;
import com.kyobee.util.jms.NotificationQueueSender;


@AppTransactional
@Repository
public class WaitListServiceImpl implements IWaitListService {


	/*@Logger
	private Log log;*/

	/*@In
	private EntityManager entityManager;*/
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private NotificationQueueSender notificationQueueSender;
	
	private Logger log = Logger.getLogger(WaitListServiceImpl.class);
	
	@Override
	public Guest saveorUpdateGuest(Guest guest) throws RsntException {
		try{
			return (Guest) sessionFactory.getCurrentSession().merge(guest);
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("saveorUpdateGuest()", e);
			throw new RsntException(e);
		}

	}
	public GuestPreferences saveorUpdateGuestPreferences(GuestPreferences pref) throws RsntException {
		try{
			return (GuestPreferences) sessionFactory.getCurrentSession().merge(pref);
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("saveorUpdateGuestPreferences()", e);
			throw new RsntException(e);
		}
	}
	/**
	 * Notify the numberOfUsers depending upon user preferences
	 * @param numberOfUsers
	 * @param orgid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void notifyUsers(int numberOfUsers,Long orgid) {
		//EmailUtil emailUtil = new EmailUtil();
		try {
			sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.UPDATE_ORG_NOTIFY_COUNT).setParameter(1, numberOfUsers)
			.setParameter(2, orgid)
			.executeUpdate();
			log.info("numberOfUsers =="+numberOfUsers);
			List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_TOP_GUEST_DETAILS).setParameter(2, numberOfUsers).setParameter(1, orgid)
					.list();
			//Bugtracker ID:230, notify only when the guest comes in threshold. 
			/*for (Object[] guestObject : list) {
				  Map<String, String> map= getGuesteMetrics(Long.parseLong(guestObject[0].toString()), orgid);
				  String msg = "Your estimated wait time is "+map.get(Constants.TOTAL_WAIT_TIME)+" mins.  "+System.getProperty("rsnt.base.upadteguest.url") + guestObject[2].toString();
				if(guestObject[6].toString().equalsIgnoreCase(Constants.RSNT_EMAIL)){
					//send email
					 sendMail(guestObject[5].toString(), msg,"Your estimated wait time ");
				}else if(guestObject[6].toString().equalsIgnoreCase(Constants.RSNT_SMS)){
					log.info("=Phone #=="+guestObject[4].toString());
					sendSMStoGuest(guestObject[4].toString(),guestObject[2].toString(),msg);
				}
			}*/
		} catch (Exception e) {
			log.error("Error ::notifyUsers::",e);
		}


	}

	/**
	 * This is the service used to notify the guest as per their preference upon change of any guests Seated/Deleated Status
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void notifyUsersOnStatusChange(Long currentguestid,Long orgid) {
		//EmailUtil emailUtil = new EmailUtil();
		try {
			Object counter2 = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_NOTIFY_COUNT).setParameter(1, orgid).uniqueResult();
			int notifyUserCount = Integer.parseInt(counter2.toString());


			List<Guest> list = sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_TOP_GUEST_DETAILS_NOTMARKED).setParameter(1, orgid).setMaxResults(notifyUserCount).list();
			int i = 1 ;
			for (Guest guestObject : list) {

				Long guestId= guestObject.getGuestID();
				//Guest guest = entityManager.find(Guest.class,guestId);
				//Only Notify the Guests which are affected due to seated or detelted status of currentguestid
				//Bugtracker ID:230 - if(guestId>currentguestid ) // added i == notifyuser count to send an alert only when the guest enters into the threshold. 
				if(guestId>currentguestid && i == notifyUserCount)
				{
					//log.info("----------------------------------------"+guest.getName());

					Map<String, String> map= getGuesteMetrics(guestId, orgid);
					String msg1 = "Cust #"+getGuestNoPrefix(orgid)+guestObject.getRank()+": Please come back w/ your entire party! Your table is almost ready! For LIVE updates: ";

					// String msg1 = "Guest #"+guest.getRank()+" - At this time, please make your way back to the restaurant with your entire party. There are "+ map.get(Constants.RSNT_GUEST_AHEAD_COUNT)+" parties ahead of you. Your approx. wait time is "+
					//map.get(Constants.TOTAL_WAIT_TIME)+" mins. ";//Additional notifications to follow."+
					String msg2 = System.getProperty("rsnt.base.upadteguest.url")+guestObject.getUuid()+"\n\n"+
							"-Sent by KYOBEE.com";
					//System.getProperty("rsnt.base.upadteguest.url") + guestObject[2].toString();

					if(guestObject.getPrefType().equalsIgnoreCase(Constants.RSNT_EMAIL)){
						//send email
						sendMail(guestObject.getEmail(), msg1+msg2,"Your estimated wait time ");

					}else if(guestObject.getPrefType().equalsIgnoreCase(Constants.RSNT_SMS)){
						//sendSMStoGuest(guestObject.getSms(),guestObject.getName(),msg1+msg2);
						//sendSMStoGuest(guestObject[4].toString(),guestObject[2].toString(),msg2);

					}
				}
				i = i + 1;
			}
		} catch (Exception e) {
			log.error("Error ::notifyUsersOnStatusChange::",e);
		}


	}

	/**
	 * Send SMS to Guest 
	 * @param number
	 * @param uuid
	 * @param msg
	 */
	/*public void sendSMStoGuest(String number, String uuid, String msg) {
		URL url;
		InputStream is = null;
		StringBuffer sb = null;
		try {
			url = new URL(System.getProperty("sms.source.url"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			String urlParameters = "email="
					+ System.getProperty("sms.source.email") + "&password="
					+ System.getProperty("sms.source.password") + "&number="
					+ number + "&message=" + msg + "&device="
					+ System.getProperty("sms.source.deviceid");
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(urlParameters);
			wr.flush();
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
			} else {
				is = conn.getErrorStream();
			}
			int ch;
			sb = new StringBuffer();
			while ((ch = is.read()) != -1) {
				sb.append((char) ch);
			}
		} catch (MalformedURLException e) {
			log.error("Error :: sendSMStoGuest ::",e.getMessage());
		} catch (IOException e) {
			log.error("Error :: sendSMStoGuest ::",e.getMessage());
		}
	}*/

	/**
	 * Fetch Guests with Status is CHECKIN Users
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Guest> loadAllCheckinUsers(Long orgid, int recordsPerPage, int pageNumber, String partyType) throws RsntException{
		try {
			int firstPage = (pageNumber == 1) ? 0 : (recordsPerPage*(pageNumber-1));
			
			/*changes by krupali, line 241 to 257 (16/06/2017)*/
			//To do switch case
			String type = partyType;
			if(type.equalsIgnoreCase("l")){
				return sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUESTS_CHECKIN_BY_ORG_LARGE)
						.setParameter(Constants.RSNT_ORG_ID,orgid).setFirstResult(firstPage).setMaxResults(recordsPerPage).list();
			}
			else if(type.equalsIgnoreCase("s")){
				return sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUESTS_CHECKIN_BY_ORG_SMALL)
						.setParameter(Constants.RSNT_ORG_ID,orgid).setFirstResult(firstPage).setMaxResults(recordsPerPage).list();
			}
			else if(type.equalsIgnoreCase("b")){
				return sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUESTS_CHECKIN_BY_ORG_BOTH)
						.setParameter(Constants.RSNT_ORG_ID,orgid).setFirstResult(firstPage).setMaxResults(recordsPerPage).list();
			}
			else {
				return sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUESTS_CHECKIN_BY_ORG_COMMON)
						.setParameter(Constants.RSNT_ORG_ID,orgid).setFirstResult(firstPage).setMaxResults(recordsPerPage).list();
			}
			/*return sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUESTS_CHECKIN_BY_ORG)
					.setParameter(Constants.RSNT_ORG_ID,orgid).setFirstResult(firstPage).setMaxResults(recordsPerPage).list();*/
		} catch (Exception e) {
			log.error("loadAllCheckinUsers()", e);
			throw new RsntException(e);
		}

	}
	
	/**
	 * Fetch Guests with Status is CHECKIN Users
	 */
	@Override
	public Long getAllCheckinUsersCount(Long orgid) throws RsntException{
		try {
			return (Long) sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUESTS_COUNT_CHECKIN_BY_ORG)
					.setParameter(Constants.RSNT_ORG_ID,orgid).uniqueResult();
		} catch (Exception e) {
			log.error("loadAllCheckinUsers()", e);
			throw new RsntException(e);
		}

	}
	/**
	 * Search Guests with name is CHECKIN Users
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Guest> searchAllCheckinUsersByName(Long orgid, int recordsPerPage, int pageNumber, String partyType, String searchName) throws RsntException{
		try {
 			int firstPage = (pageNumber == 1) ? 0 : (recordsPerPage*(pageNumber-1));
 			String name = searchName;
 			StringBuffer query=new StringBuffer("FROM Guest g left join fetch g.languagePrefID WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId");
 			if(name != null) {
 				query=query.append(" and g.name like :Name");
 			}
 			/*if(statusOption.equals("Incomplete")) {
 				query=query.append(" and incompleteParty > 0");
 			}*/
 			query=query.append(" order by g.rank asc");
 			//HQL_GET_GUESTS_HISTORY
 			return sessionFactory.getCurrentSession().createQuery(query.toString())
 					.setParameter(Constants.RSNT_ORG_ID,orgid).setParameter("Name", "%"+name+"%").setFirstResult(firstPage).setMaxResults(recordsPerPage).list();
 		} catch (Exception e) {
 			log.error("searchAllCheckinUsersByName()", e);
 			throw new RsntException(e);
 		}
	}
	
	@Override
	public Long getAllCheckinUsersCountByName(Long orgid,String searchName) throws RsntException{
		try {
			String name = searchName;
			StringBuffer query=new StringBuffer("select count(*) FROM Guest g WHERE g.status ='CHECKIN' and g.resetTime is null and g.OrganizationID=:orgId");
			if(name != null) {
 				query=query.append(" and g.name like :Name");
 			}
 			/*if(statusOption.equals("Incomplete")) {
 				query=query.append(" and incompleteParty > 0");
 			}*/
 			query=query.append(" order by g.rank asc");
			return (Long) sessionFactory.getCurrentSession().createQuery(query.toString())
					.setParameter(Constants.RSNT_ORG_ID,orgid).setParameter("Name", "%"+name+"%").uniqueResult();
		} catch (Exception e) {
			log.error("getAllCheckinUsersCountByName()", e);
			throw new RsntException(e);
		}

	}
	/**
	 * Fetch guests history by orgId
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Guest> loadGuestsHistoryByOrg(Long orgId)
			throws RsntException {
		try {
			return sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUESTS_HISTORY).setParameter(Constants.RSNT_ORG_ID,orgId).list();
		} catch (Exception e) {
			log.error("loadGuestsHistoryByOrg()", e);
			throw new RsntException(e);
		}
	}
	/**
	 * Feth guest by pkid
	 */
	@Override
	public Guest getGuestById(long guestId) {
		//return (Guest) sessionFactory.getCurrentSession().get(Guest.class, guestId);
		Guest guest = (Guest) sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUEST_BY_ID).setParameter("guestId", guestId).uniqueResult();
		return guest;
	}
	/**
	 * Update organization wait time
	 * @param waitTime
	 * @param orgid
	 */
	@Override
	public int updateOrganizationWaitTime(int waitTime,Long orgid) {
		try {
			sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.UPDATE_ORG_WAITTIME).setParameter(1, waitTime)
			.setParameter(2, orgid)
			.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}
	/**
	 * update all guests status as RESET byorgId
	 * @param  orgid
	 */
	/*@Override
	public int resetOrganizationsByOrgid(Long orgid) {
		try {
			//Added for Guest Reset functionality
			resetGuestTables(orgid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}*/
	
	public int resetOrganizationsByOrgid(Long orgid) {
		
		try {
			sessionFactory.getCurrentSession().doWork(new Work(){
			
				@Override
				public void execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call RESETGUESTBYORGID(?)}");
					
					try {
						if(orgid!=null){
						cStmt.setLong(1, orgid);
						}
						else{
							cStmt.setLong(1, Types.INTEGER);
						}
						cStmt.execute();
					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
				}
			});
			return 1;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	//Added for Guest Reset functionality
	private void resetGuestTables(Long orgId)
	{
		//Copy Guest to GuestReset
		List<Guest> guestList = sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUESTS).setParameter("orgId", orgId).list();
		GuestReset oGuestReset;
		//List<GuestReset> guestResetList = new ArrayList<GuestReset>();
		for(Guest oGuest : guestList)
		{
			oGuestReset = new GuestReset();
			oGuestReset.setGuestID(oGuest.getGuestID());
			oGuestReset.setCalloutCount(oGuest.getCalloutCount());
			oGuestReset.setCheckinTime(oGuest.getCheckinTime());
			oGuestReset.setCreatedTime(oGuest.getCreatedTime());
			oGuestReset.setEmail(oGuest.getEmail());
			oGuestReset.setIncompleteParty(oGuest.getIncompleteParty());
			oGuestReset.setName(oGuest.getName());
			oGuestReset.setNoOfPeople(oGuest.getNoOfPeople());
			oGuestReset.setNote(oGuest.getNote());
			oGuestReset.setOptin(oGuest.isOptin());
			oGuestReset.setOrganizationID(oGuest.getOrganizationID());
			oGuestReset.setPrefType(oGuest.getPrefType());
			oGuestReset.setLanguagePrefID(oGuest.getLanguagePrefID().getLangId());
			oGuestReset.setRank(oGuest.getRank());
			oGuestReset.setResetTime(oGuest.getResetTime());
			oGuestReset.setSeatedTime(oGuest.getSeatedTime());
			oGuestReset.setSms(oGuest.getSms());
			oGuestReset.setStatus(oGuest.getStatus());
			oGuestReset.setUpdatedTime(oGuest.getUpdatedTime());
			oGuestReset.setUuid(oGuest.getUuid());
			sessionFactory.getCurrentSession().persist(oGuestReset);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
			oGuestReset = null;
		}
		//entityManager.persist(guestResetList);

		//Copy GuestPreferences to GuestPreferencesRest
		//List<GuestPreferences> guestPreferencesList = entityManager.createQuery(NativeQueryConstants.HQL_GET_GUESTS_PREFERENCES).getResultList();
		/*GuestPreferencesReset oGuestPreferencesReset;
		//List<GuestPreferencesReset> guestPreferencesResetList = new ArrayList<GuestPreferencesReset>();
		for(GuestPreferences oGuestPreferences : guestPreferencesList)
		{
			oGuestPreferencesReset = new GuestPreferencesReset();
			oGuestPreferencesReset.setPrefValue(oGuestPreferences.getPrefValue());
			oGuestPreferencesReset.setPrefValueId(oGuestPreferences.getPrefValueId());
			oGuestPreferencesReset.setGuest(oGuestPreferences.getGuest());
			entityManager.persist(oGuestPreferencesReset);
			entityManager.flush();
			entityManager.clear();
			oGuestPreferencesReset = null;
		}*/

		//entityManager.createQuery(NativeQueryConstants.HQL_DELETE_GUESTS_PREFERENCES).executeUpdate();
		//entityManager.createQuery(NativeQueryConstants.HQL_DELETE_GUESTS).setParameter(1,orgId).executeUpdate();

	}

	/**
	 * Mark guest as DELETED
	 * @param guestId
	 */
	@Override
	public int deleteGuestbyId(Long guestId) {
		try {

			sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.DELETE_GUEST_BY_ID).setParameter(1, guestId)
			.executeUpdate();
		} catch (Exception e) {
		}
		return 1;
	}
	/**
	 * Fetch Guest By UUID
	 */
	@Override
	public Guest getGuestByUUID(String uuid) {
		try {
			return (Guest) sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_GUEST_BY_UUID).setParameter(Constants.RSNT_GUEST_UUID,uuid).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * GET Organization Metrics like total wait time, guest count , notify count
	 */
	//@Override
	public Map<String, String> getTotalPartyWaitTimeMetrics(Long orgId) {
		// TODO Auto-generated method stub
		
		/*OP_NOWSERVERINGPARTY
		OP_TOTALWAITINGGUEST
		OP_TOTALWAITTIME - GET_ORG_WAIT_TIME
		OP_NOOFPARTIESAHEAD
		OP_GUESTTOBENOTIFIED
		OP_GUESTNOTIFIEDWAITTIME
		
		2 OUT `OP_NOWSERVERINGPARTY` INT, 
		3 OUT `OP_TOTALWAITINGGUEST` INT, 
		4 OUT `OP_TOTALWAITTIME` INT, 
		5 OUT `OP_NOOFPARTIESAHEAD` INT, 
		6 OUT `OP_GUESTTOBENOTIFIED` VARCHAR(55), 
		7 OUT `OP_GUESTNOTIFIEDWAITTIME` INT, 
		8 OUT `OP_PERPARTYWAITTIME` INT, 
		9 OUT `OP_NOTIFYUSERCOUNT` INT )*/

		//CallableStatement cStmt = null;

		HashMap<String, String> retMetricks =  new HashMap<String, String>();
		/*Object countObj = entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_WAIT_TIME).setParameter(1, orgId).getSingleResult();
		metricks.put(Constants.RSNT_ORG_WAIT_TIME, countObj.toString());
		Object countObj1 = entityManager.createNativeQuery(NativeQueryConstants.GET_GUEST_CHECKIN_COUNT).setParameter(1, orgId).getSingleResult();
		metricks.put(Constants.RSNT_ORG_GUEST_COUNT, countObj1.toString());
		Object countObj2 = entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_NOTIFY_COUNT).setParameter(1, orgId).getSingleResult();
		metricks.put(Constants.RSNT_ORG_NOTIFY_COUNT, countObj2.toString());
		Object countObj3 = entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_GUEST_MAX_RANK).setParameter(1, orgId).getSingleResult();
		metricks.put(Constants.RSNT_GUEST_RANK, countObj3.toString());
		Object countObj4 = entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_GUEST_MIN_CHECKIN_RANK).setParameter(1, orgId).getSingleResult();
		metricks.put(Constants.RSNT_GUEST_RANK_MIN, countObj4.toString());*/

		try {
			/*org.hibernate.Session session = (org.hibernate.Session)entityManager.getDelegate();
			Connection conn = session.connection();
			cStmt = conn.prepareCall("{call CALCHEADERMETRICS(?, ?, ?, ?, ?, ?, ?, ?, ?)}");  
			cStmt.setLong(1,orgId);
			cStmt.registerOutParameter(2, Types.INTEGER);
			cStmt.registerOutParameter(3, Types.INTEGER);
			cStmt.registerOutParameter(4, Types.INTEGER);
			cStmt.registerOutParameter(5, Types.INTEGER);
			cStmt.registerOutParameter(6, Types.INTEGER);
			cStmt.registerOutParameter(7, Types.INTEGER);
			cStmt.registerOutParameter(8, Types.INTEGER);
			cStmt.registerOutParameter(9, Types.INTEGER);
		
			
			cStmt.execute();
			System.out.println(cStmt.getInt(4));
			System.out.println(cStmt.getInt(3));
			System.out.println(cStmt.getInt(8));
			System.out.println(cStmt.getInt(9));
			metricks.put(Constants.RSNT_ORG_GUEST_COUNT, String.valueOf(cStmt.getInt(3)));//OP_TOTALWAITINGGUEST
			metricks.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, String.valueOf(cStmt.getInt(4)));//OP_TOTALWAITTIME
			metricks.put(Constants.RSNT_ORG_WAIT_TIME, String.valueOf(cStmt.getInt(8)));//OP_PERPARTYWAITTIME
			metricks.put(Constants.RSNT_GUEST_RANK_MIN, String.valueOf(cStmt.getInt(2)));//OP_NOWSERVERINGPARTY
			metricks.put("OP_NOOFPARTIESAHEAD", String.valueOf(cStmt.getInt(5)));//OP_NOOFPARTIESAHEAD
			metricks.put("OP_GUESTTOBENOTIFIED", String.valueOf(cStmt.getInt(6)));//OP_GUESTTOBENOTIFIED
			metricks.put("OP_GUESTNOTIFIEDWAITTIME", String.valueOf(cStmt.getInt(7)));//OP_GUESTNOTIFIEDWAITTIME
			metricks.put("OP_NOTIFYUSERCOUNT", String.valueOf(cStmt.getInt(9)));//OP_NOTIFYUSERCOUNT
			metricks.put("success", "0");*/
			retMetricks = sessionFactory.getCurrentSession().doReturningWork(new ReturningWork<HashMap<String, String>>() {
				
				@Override
				public HashMap<String, String>  execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call CALCHEADERMETRICS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");  
					HashMap<String, String> metricks =  new HashMap<String, String>();
					try {
					cStmt.setLong(1,orgId);
					cStmt.registerOutParameter(2, Types.INTEGER);
					cStmt.registerOutParameter(3, Types.INTEGER);
					cStmt.registerOutParameter(4, Types.INTEGER);
					cStmt.registerOutParameter(5, Types.INTEGER);
					cStmt.registerOutParameter(6, Types.INTEGER);
					cStmt.registerOutParameter(7, Types.INTEGER);
					cStmt.registerOutParameter(8, Types.INTEGER);
					cStmt.registerOutParameter(9, Types.INTEGER);
					cStmt.registerOutParameter(10, Types.VARCHAR);
				
					
					cStmt.execute();
					metricks.put(Constants.RSNT_ORG_GUEST_COUNT, String.valueOf(cStmt.getInt(3)));//OP_TOTALWAITINGGUEST
					metricks.put(Constants.RSNT_ORG_TOTAL_WAIT_TIME, String.valueOf(cStmt.getInt(4)));//OP_TOTALWAITTIME
					metricks.put(Constants.RSNT_ORG_WAIT_TIME, String.valueOf(cStmt.getInt(8)));//OP_PERPARTYWAITTIME
					metricks.put(Constants.RSNT_GUEST_RANK_MIN, String.valueOf(cStmt.getInt(2)));//OP_NOWSERVERINGPARTY
					metricks.put("OP_NOOFPARTIESAHEAD", String.valueOf(cStmt.getInt(5)));//OP_NOOFPARTIESAHEAD
					metricks.put("OP_GUESTTOBENOTIFIED", String.valueOf(cStmt.getInt(6)));//OP_GUESTTOBENOTIFIED
					metricks.put("OP_GUESTNOTIFIEDWAITTIME", String.valueOf(cStmt.getInt(7)));//OP_GUESTNOTIFIEDWAITTIME
					metricks.put("OP_NOTIFYUSERCOUNT", String.valueOf(cStmt.getInt(9)));//OP_NOTIFYUSERCOUNT
					metricks.put("CLIENT_BASE", cStmt.getString(10));
					metricks.put("success", "0");
					} finally {
						if(cStmt != null){
							cStmt.close();
						}
					}
					return metricks;
					
				}
			});

			/*metricks.put(Constants.RSNT_ORG_NOTIFY_COUNT, countObj2.toString());
			metricks.put(Constants.RSNT_GUEST_RANK, countObj3.toString());
			metricks.put(Constants.RSNT_GUEST_RANK_MIN, countObj4.toString());*/

			
		}
		catch(Exception e){
			e.printStackTrace();
			retMetricks.put("success", "1");

		}
		/*finally {
			try {
				cStmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			metricks.put("success", "1");
		}*/
		return retMetricks;
	}

	@Override
	public String getGuestRankMin(Long orgId) {
		List<Guest> guestList = null;
		guestList = sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_MIN_RANK_OF_GUEST).setParameter(1, orgId).setMaxResults(1).list();
		return guestList.get(0).getRank()+"";
	}

	@Override
	public Map<String, String> getGuesteMetrics(Long guestId,Long orgId) {
		// TODO Auto-generated method stub
		HashMap<String, String> metricks =  new HashMap<String, String>();
		Object orgwaitTimeObj = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_WAIT_TIME).setParameter("orgId", orgId).uniqueResult();
		Object guestCountobj = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_GUEST_AHEAD_COUNT).setParameter("guestId", guestId).setParameter("orgId", orgId).uniqueResult();
		Object countObj4 = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_GUEST_MIN_CHECKIN_RANK).setParameter("orgId", orgId).uniqueResult();
		metricks.put(Constants.RSNT_GUEST_RANK_MIN, countObj4.toString());
		int orgwaitTime = Integer.parseInt(orgwaitTimeObj.toString());
		int guestCount = Integer.parseInt(guestCountobj.toString());
		metricks.put(Constants.TOTAL_WAIT_TIME, String.valueOf(guestCount <=0 ?orgwaitTime:((guestCount)*orgwaitTime)+orgwaitTime));
		metricks.put(Constants.RSNT_ORG_WAIT_TIME, orgwaitTimeObj.toString());
		metricks.put(Constants.RSNT_GUEST_AHEAD_COUNT, String.valueOf(guestCount <=0 ?0:(guestCount)));
		Integer maxParty = (Integer) sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_MAX_PARTY).setParameter("orgId", orgId).uniqueResult();
		metricks.put(Constants.ORG_MAX_PARTY, maxParty.toString());
		return metricks;
	}

	/**
	 * notify guest by guestId
	 */
	@Override
	public void notifyGuestByPref(Long guestId){
		Guest guest = (Guest) sessionFactory.getCurrentSession().get(Guest.class,guestId);
		Map<String, String> map= getGuesteMetrics(guestId, guest.getOrganizationID());
		//Organization organization =entityManager.find(Organization.class, guest.getOrganizationID());
		//String msg = "Your estimated wait time is "+map.get(Constants.TOTAL_WAIT_TIME)+" mins.  "+System.getProperty("rsnt.base.upadteguest.url")+guest.getUuid();
		String msg1 = "Cust. #"+getGuestNoPrefix(guest.getOrganizationID())+guest.getRank()+": There are "+ map.get(Constants.RSNT_GUEST_AHEAD_COUNT)+" parties ahead of you w/ approx. wait-time of "+
				map.get(Constants.TOTAL_WAIT_TIME)+" min. ";
		String msg2 = "For LIVE updates: "+
				System.getProperty("rsnt.base.upadteguest.url")+guest.getUuid()+"\n\n"+
				"- Sent by KYOBEE.com";


	}

	@Override
	public void sendNotificationToGuest(GuestNotificationBean guestNotificationBean) {
		//QueueConnection connection = null;
		try {
			/*InitialContext ctx = new InitialContext();			
			Queue queue = (Queue) ctx.lookup("/queue/NotificationQueue");
			QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
			connection = factory.createQueueConnection();//(QueueConnection) ds.getConnection(); 
			QueueSession session = 
					connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);*/
			
			SMSDetailsWrapper smsDetails  =  getSMSDetail(guestNotificationBean.getOrgId());
			guestNotificationBean.setSmsRoute(smsDetails.getSmsRoute());
			guestNotificationBean.setSmsSignature(smsDetails.getSmsSignature());			
			guestNotificationBean.setSmsRouteNo(smsDetails.getSmsRouteNo());

			//ObjectMessage objectMessage = 
			//		session.createObjectMessage(guestNotificationBean);
			
			//sender.send(objectMessage);
			notificationQueueSender.sendMessage(guestNotificationBean);
		} catch (Exception e) {
			LoggerUtil.logError(e.getMessage(),e);
		}
		finally {
			/*try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	
	public SMSDetailsWrapper getSMSDetail(Long orgID){
		/*Map<String, Object> rootMap = new LinkedHashMap<String, Object>();*/		
		SMSDetailsWrapper smsDetails=null;
		try{
			SQLQuery query =sessionFactory.getCurrentSession().createSQLQuery("SELECT smsSignature,smsRoute,smsRouteNo FROM ORGANIZATION where OrganizationID =:orgId");
			query.setParameter("orgId", orgID);
			query.addScalar("smsSignature", StringType.INSTANCE);
			query.addScalar("smsRoute", StringType.INSTANCE);
			query.addScalar("smsRouteNo", StringType.INSTANCE);
			
			smsDetails =  (SMSDetailsWrapper) (query.setResultTransformer(
					new AliasToBeanResultTransformer(SMSDetailsWrapper.class)).uniqueResult());
			/*rootMap.put("smsSignature", smsDetails.getSmsSignature());
			rootMap.put("smsRoute", smsDetails.getSmsRoute());
			rootMap.put("smsRouteNo", smsDetails.getSmsRouteNo());*/
			
			/*Object smsSignature = sessionFactory.getCurrentSession().createSQLQuery("SELECT smsSignature FROM ORGANIZATION where OrganizationID =:orgId").setParameter("orgId", orgID)
			.uniqueResult();
			System.out.println(smsSignature.toString());
			rootMap.put("smsSignature", smsSignature.toString());
			String smsRoute = sessionFactory.getCurrentSession().createSQLQuery("SELECT smsRoute FROM ORGANIZATION where OrganizationID =:orgId").setParameter("orgId", orgID)
					.uniqueResult().toString();			
			rootMap.put("smsRoute", smsRoute.toString());
*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return smsDetails;
	}
	
	// TODO - Uncomment below method once JMS is configured. - rohit
	/*@Override
	public void sendNotificationToGuestForSeated(GuestNotificationBean guestNotificationBean) {
		QueueConnection connection = null;
		try {
			InitialContext ctx = new InitialContext();//props
			
			//DataSource ds = (DataSource)ctx.lookup("java:jboss/datasources/RsntDatasource");
			
			Queue queue = (Queue) ctx.lookup("/queue/NotificationQueue");
			QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
			
			connection = factory.createQueueConnection();//(QueueConnection) ds.getConnection(); 
			QueueSession session = 
					connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			ObjectMessage objectMessage = 
					session.createObjectMessage(guestNotificationBean);
			sender.send(objectMessage); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/

	/*@Override
	public void sendWaitListBackupEmail(Long guestId, boolean isUpdate){
		Guest guest = (Guest) sessionFactory.getCurrentSession().get(Guest.class,guestId);
		Organization organization =(Organization) sessionFactory.getCurrentSession().get(Organization.class, guest.getOrganizationID());
		String subject = buildSubjectAndMessage(guest, organization, true, isUpdate);
		String message = buildSubjectAndMessage(guest, organization, false, isUpdate);
		if(null != organization.getWaitListBackupEmail() && !organization.getWaitListBackupEmail().equals("")){
			sendMail(organization.getWaitListBackupEmail(), message, subject);
		}
	}*/

	/**
	 * Send  Table ready Notification to guest
	 * @param guestId
	 */
	@Override
	public void readyNotifyGuestByPref(Long guestId){
		Guest guest = (Guest) sessionFactory.getCurrentSession().get(Guest.class,guestId);
		Organization organization =(Organization) sessionFactory.getCurrentSession().get(Organization.class, guest.getOrganizationID());
		// String msg = "We are ready for you at  "+organization.getOrganizationName();
		/*String msg="Cust. #"+guest.getRank()+" - Thanks for your patience! Your table is just about ready! Please be ready to be seated when called - anyone not ready with their entire party will not be seated. A member of our staff will be with you shortly. Thank you!\n\n" +
			"- Sent by KYOBEE.com";*/
		String msg = "Thank you so much for using the KYOBEE wait-list system! Please enjoy your visit, and we appreciate any GLOWING reviews! Come again soon!~"+

			"\n\n- Sent by KYOBEE.com"+"\n";
		//+"(Additional features also available)";


		if(guest.getPrefType().equalsIgnoreCase(Constants.RSNT_SMS)){
			//sendSMStoGuest(guest.getSms(), guest.getUuid(),msg);
		}else if(guest.getPrefType().equalsIgnoreCase("email")){
			sendMail(guest.getEmail(), msg,"We are ready for you");
		}
	}
	/**
	 * Get organization name by id
	 * @param orgId
	 * @return
	 */
	public String getOrganizationNameById(Long orgId){
		return sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_NAME_BY_ID).setParameter(1, orgId).uniqueResult().toString();
	}
	@Override
	public Guest getMaxGuestRankByOrgId(Long orgId) {
		//long count = 0;
		List<Guest> guestWithMaxRank = null;
		try {
			guestWithMaxRank = sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.HQL_GET_MAX_RANK_OF_GUEST).setParameter(1, orgId).setMaxResults(1).list();
			//log.info("getMaxGuestRankByOrgId=="+countObj.toString());
			//count =Integer.parseInt(countObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return guestWithMaxRank.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GuestPreferencesDTO> getOrganizationSeatingPref(Long orgId) {
		List<GuestPreferencesDTO> pref = null;
		GuestPreferencesDTO dto = new GuestPreferencesDTO();
		List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_SEATING_PREF_VALUES).
				setParameter("orgId", orgId).setParameter("catTypeId", Constants.CONT_LOOKUPTYPE_SEATTYPE).list();
		if(null != list && list.size()>0){
			pref = new ArrayList<GuestPreferencesDTO>();
			for (Object[] lookupvalue : list) {
				dto = new GuestPreferencesDTO();
				dto.setPrefValueId(Long.valueOf(lookupvalue[0].toString()));
				dto.setPrefValue(lookupvalue[1].toString());
				pref.add(dto);
			}
		}


		return pref;
	}

/*change by sunny for getting Organization Marketing Pref*/
	
	@Override
	public List<GuestMarketingPreference> getOrganizationMarketingPref(Long orgid) {
		List<GuestMarketingPreference> pref = null;
		GuestMarketingPreference dto = new GuestMarketingPreference();
		List<Object[]> list = sessionFactory.getCurrentSession()
				.createSQLQuery(NativeQueryConstants.GET_ORG_MARKETING_PREF_VALUES).setParameter("orgId", orgid)
				.setParameter("catTypeId", Constants.CONT_LOOKUPTYPE_GUEST_MARKETING_PREF).list();
		if (null != list && list.size() > 0) {
			pref = new ArrayList<GuestMarketingPreference>();
			for (Object[] lookupvalue : list) {
				dto = new GuestMarketingPreference();
				dto.setGuestMarketPrefValueId(Long.valueOf(lookupvalue[0].toString()));
				dto.setGuestMarketPrefValue(lookupvalue[1].toString());
				pref.add(dto);
			}
		}

		return pref;

	}

	private String buildSubjectAndMessage(Guest guest, Organization organization, boolean isSubject, boolean isUpdate){

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
		String createdDateTimeSring = simpleDateFormat.format(guest.getCreatedTime());
		if(isSubject){

			StringBuffer subjectBuffer = new StringBuffer();

			if(isUpdate){
				subjectBuffer.append("Update : ");
			}
			subjectBuffer.append("# ");
			subjectBuffer.append(guest.getRank()+", ");
			subjectBuffer.append(guest.getName()+", ");
			subjectBuffer.append("Party of "+guest.getNoOfPeople()+", ");
			subjectBuffer.append(createdDateTimeSring);

			return subjectBuffer.toString();

		}else{

			StringBuffer messageBuffer = new StringBuffer();

			messageBuffer.append("<table width='100%'>");
			messageBuffer.append("<tr>");
			messageBuffer.append("<td width='40%'><b>Organization name:</b>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>"+organization.getOrganizationName());
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			messageBuffer.append("<tr>");
			messageBuffer.append("<td width='40%'><b>Date/Time:</b>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>"+createdDateTimeSring);
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			messageBuffer.append("<tr>");
			messageBuffer.append("<td width='40%'><b>Customer # :</b>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>"+ guest.getRank());
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			messageBuffer.append("<tr>");
			messageBuffer.append("<td width='40%'><b>Name:</b>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>"+ guest.getName());
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			messageBuffer.append("<tr>");
			messageBuffer.append("<td width='40%'><b>Party of:</b>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>"+ guest.getNoOfPeople());
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			List<GuestPreferences> guestPreferences = guest.getGuestPreferences();
			String allGuestPreferences = "";
			for (GuestPreferences current : guestPreferences) {
				allGuestPreferences += current.getPrefValue() +", ";
			}


			messageBuffer.append("<tr>");
			messageBuffer.append("<td width='40%'><b>Seating Preference:</b>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>"+ allGuestPreferences);
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");


			if(null != guest.getEmail()){

				messageBuffer.append("<tr>");
				messageBuffer.append("<td width='40%'><b>SMS/E-mail:</b>");
				messageBuffer.append("</td>");
				messageBuffer.append("<td>"+ guest.getEmail());
				messageBuffer.append("</td>");
				messageBuffer.append("</tr>");

			}else{

				messageBuffer.append("<tr>");
				messageBuffer.append("<td width='40%'><b>SMS/E-mail:</b>");
				messageBuffer.append("</td>");
				messageBuffer.append("<td>"+ guest.getSms());
				messageBuffer.append("</td>");
				messageBuffer.append("</tr>");

			}
			if(guest.getNote() != null){

				messageBuffer.append("<tr>");
				messageBuffer.append("<td width='40%'><b>Notes:</b>");
				messageBuffer.append("</td>");
				messageBuffer.append("<td>"+ guest.getNote());
				messageBuffer.append("</td>");
				messageBuffer.append("</tr>");

			}else{

				messageBuffer.append("<tr>");
				messageBuffer.append("<td width='40%'><b>Notes:</b>");
				messageBuffer.append("</td>");
				messageBuffer.append("<td>"+ "N/A");
				messageBuffer.append("</td>");
				messageBuffer.append("</tr>");

			}

			String Opt = guest.isOptin() ? "Yes" : "No";

			messageBuffer.append("<tr>");
			messageBuffer.append("<td><b>Opt-in:</b>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>"+ Opt);
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			messageBuffer.append("<tr>");
			messageBuffer.append("<td>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>");
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			messageBuffer.append("<tr>");
			messageBuffer.append("<td>");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>");
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			messageBuffer.append("<tr>");
			messageBuffer.append("<td>- Sent by KYOBEE.com");
			messageBuffer.append("</td>");
			messageBuffer.append("<td>");
			messageBuffer.append("</td>");
			messageBuffer.append("</tr>");

			return messageBuffer.toString();
		}
	}

	/**
	 * sendmail method
	 * @param to
	 * @param msg
	 * @param subject
	 */

	static public void sendMail(String to,String msg,String subject){

		Thread t1 = null;
		if(t1==null){
			t1= new Thread();
		}
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", System.getProperty("rsnt.mail.smtp.auth"));
		properties.put("mail.smtp.starttls.enable", System.getProperty("rsnt.mail.smtp.starttls.enable"));
		properties.put("mail.smtp.host", System.getProperty("rsnt.mail.smtp.host"));
		properties.put("mail.smtp.port", System.getProperty("rsnt.mail.smtp.port"));
		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(System.getProperty("rsnt.mail.username"), System.getProperty("rsnt.mail.password"));
			}
		});

		try {

			/*MimeMessage  message = new MimeMessage(session);
				message.setFrom(new InternetAddress(System.getProperty("rsnt.mail.username")));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				message.setContent(msg, "text/html");
				message.setSubject(subject);
				message.setText(msg);
				Transport.send(message);*/

			//
			// This HTML mail have to 2 part, the BODY and the embedded image
			//
			MimeMultipart multipart = new MimeMultipart("related");

			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msg, "text/html");

			// add it
			multipart.addBodyPart(messageBodyPart);

			MimeMessage messsg = new MimeMessage(session);
			// put everything together
			messsg.setContent(multipart);

			messsg.setSubject(subject);
			messsg.setFrom(new InternetAddress(System.getProperty("rsnt.mail.username")));
			messsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			Transport.send(messsg);

		} catch (MessagingException e) {
			//throw new RuntimeException(e);
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		//		 Session session = Session.getDefaultInstance(properties);
		//		 
		//		 try {
		//			// Create a default MimeMessage object.
		//	         MimeMessage message = new MimeMessage(session);
		//
		//	         // Set From: header field of the header.
		//	       message.setFrom(new InternetAddress("support@kyobee.com"));
		//
		//	         // Set To: header field of the header.
		//	         message.addRecipient(Message.RecipientType.TO,
		//	                                  new InternetAddress(to));
		//
		//	         // Set Subject: header field
		//	         message.setSubject("This is the Subject Line!");
		//
		//	         // Now set the actual message
		//	         message.setText("This is actual message");
		//
		//	         // Send message
		//	         Transport.send(message);
		//	         System.out.println("Sent message successfully....");
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
	}

	/*public void sendSMStoGuest(String number, String uuid, String msg) {
		log.info("------Inside sendSMStoGuest------");
		String PROJECT_API_KEY = System.getProperty("sms.api.key");//"uCa1TCCMti3B9buAZGTIaBYGfOzb7C60";
		String PROJECT_ID = System.getProperty("sms.project.id");//"PJe726ee67deeb1949";
		try {
			TelerivetAPI tr = new TelerivetAPI(PROJECT_API_KEY);
			Project project = tr.initProjectById(PROJECT_ID);
			project.sendMessage(Util.options(
					"to_number", number,
					"content", msg
					));
		} catch (MalformedURLException e) {
			log.error("Error :: sendSMStoGuest ::",e);
		} catch (IOException e) {
			log.error("Error :: sendSMStoGuest ::",e);
		}
	}*/

	private String getGuestNoPrefix(long orgId){
		String prefix = "";
		if(orgId == 15){
			prefix = "A";
		}
		return prefix;
	}

	public void updateTotalWaitTime(Long organizationId, boolean incrementTimeFlag)
	{
		if(incrementTimeFlag)
			sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.UPDATE_ORG_TOTAL_WAITTIME_INCREMENT).setParameter(1, organizationId).executeUpdate();
		else 
			sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.UPDATE_ORG_TOTAL_WAITTIME_DECREMENT).setParameter(1, organizationId).executeUpdate();
	}

	public WaitlistMetrics addGuest(Guest guestObj) {
		WaitlistMetrics waitListMetrics = null;
		// CallableStatement cStmt = null;
		try {
			/*
			 * org.hibernate.Session session =
			 * (org.hibernate.Session)entityManager.getDelegate(); Connection
			 * conn = session.connection();
			 * 
			 * cStmt = conn.prepareCall("{call addGuest(?, ?, ?, " +
			 * "?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			 * cStmt.setLong(1,guestObj.getOrganizationID());
			 * cStmt.setString(2,guestObj.getName());
			 * cStmt.setString(3,guestObj.getUuid());
			 * cStmt.setLong(4,guestObj.getNoOfPeople()); cStmt.setString(5,
			 * guestObj.getDeviceType()); cStmt.setString(6,
			 * guestObj.getDeviceId()); cStmt.setString(7,guestObj.getSms());
			 * cStmt.setString(8,guestObj.getEmail());
			 * cStmt.setString(9,guestObj.getPrefType());
			 * cStmt.setBoolean(10,guestObj.isOptin());
			 * cStmt.setString(11,guestObj.getNote());
			 * cStmt.setString(12,guestObj.getSeatingPreference());
			 * cStmt.registerOutParameter(13, Types.INTEGER);
			 * cStmt.registerOutParameter(14, Types.INTEGER);
			 * cStmt.registerOutParameter(15, Types.INTEGER);
			 * cStmt.registerOutParameter(16, Types.VARCHAR);
			 * cStmt.registerOutParameter(17, Types.INTEGER);
			 * cStmt.registerOutParameter(18, Types.VARCHAR);
			 * cStmt.registerOutParameter(19, Types.INTEGER); cStmt.execute();
			 * 
			 * oWaitlistMetrics.setGuestId(cStmt.getInt(13));
			 * oWaitlistMetrics.setGuestRank(cStmt.getInt(14));
			 * oWaitlistMetrics.setNowServingParty(cStmt.getInt(15));
			 * oWaitlistMetrics.setTotalWaitingGuest(cStmt.getInt(16));
			 * oWaitlistMetrics.setTotalWaitTime(cStmt.getInt(17));
			 * oWaitlistMetrics.setNoOfPartiesAhead(cStmt.getInt(18));
			 * oWaitlistMetrics.setGuestToBeNotified(cStmt.getInt(19));
			 */
			waitListMetrics = sessionFactory.getCurrentSession().doReturningWork(new ReturningWork<WaitlistMetrics>() {

				/* changes by krupali, line 1078 to line 1111 (15/06/2017) */

				@Override
				public WaitlistMetrics execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call addGuest(?, ?, ?, ?, "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
					WaitlistMetrics oWaitlistMetrics = new WaitlistMetrics();

					try {
						cStmt.setLong(1, guestObj.getOrganizationID());
						cStmt.setString(2, guestObj.getName());
						cStmt.setString(3, guestObj.getUuid());
						cStmt.setLong(4, guestObj.getNoOfChildren());
						cStmt.setLong(5, guestObj.getNoOfAdults());
						cStmt.setLong(6, guestObj.getNoOfInfants());
						cStmt.setLong(7, guestObj.getNoOfPeople());
						cStmt.setLong(8, guestObj.getLanguagePrefID().getLangId());
						cStmt.setInt(9, guestObj.getQuoteTime());
						cStmt.setInt(10, guestObj.getPartyType());
						cStmt.setString(11, guestObj.getDeviceType());
						cStmt.setString(12, guestObj.getDeviceId());
						cStmt.setString(13, guestObj.getSms());
						cStmt.setString(14, guestObj.getEmail());
						cStmt.setString(15, guestObj.getPrefType());
						cStmt.setBoolean(16, guestObj.isOptin());
						cStmt.setString(17, guestObj.getNote());
						cStmt.setString(18, guestObj.getSeatingPreference());

						cStmt.registerOutParameter(19, Types.INTEGER);
						cStmt.registerOutParameter(20, Types.INTEGER);
						cStmt.registerOutParameter(21, Types.INTEGER);
						cStmt.registerOutParameter(22, Types.VARCHAR);
						cStmt.registerOutParameter(23, Types.INTEGER);
						cStmt.registerOutParameter(24, Types.VARCHAR);
						cStmt.registerOutParameter(25, Types.INTEGER);
						cStmt.registerOutParameter(26, Types.VARCHAR);
						cStmt.setString(27, guestObj.getMarketingPreference());// change by sunny line 1195 to 1196 at 2018-07-04
						cStmt.setString(28, guestObj.getCustomPreference());
						cStmt.execute();
						oWaitlistMetrics.setGuestId(cStmt.getInt(19));
						oWaitlistMetrics.setGuestRank(cStmt.getInt(20));
						oWaitlistMetrics.setNowServingParty(cStmt.getInt(21));
						oWaitlistMetrics.setTotalWaitingGuest(cStmt.getInt(22));
						oWaitlistMetrics.setTotalWaitTime(cStmt.getInt(23));
						oWaitlistMetrics.setNoOfPartiesAhead(cStmt.getInt(24));
						oWaitlistMetrics.setGuestToBeNotified(cStmt.getInt(25));
						oWaitlistMetrics.setClientBase(cStmt.getString(26));
						
						// change by sunny for addMarketing Pref (3-08-2018)
						if(null != guestObj.getGuestMarketingPreferences()){
							
							AddMarketing addMarketing = new AddMarketing();
							
							addMarketing.setGuestID(cStmt.getInt(19));
							addMarketing.setOrgID(Integer.parseInt(guestObj.getOrganizationID().toString()));
						
							for(int i= 0; i<guestObj.getGuestMarketingPreferences().size(); i++){
								GuestMarketingPreference guestMarketingPreference = new GuestMarketingPreference();
								guestMarketingPreference = guestObj.getGuestMarketingPreferences().get(i);
								
								// set Facebook
								if(true == guestMarketingPreference.isSelected() && guestMarketingPreference.getGuestMarketPrefValue().equals("Facebook")){
									addMarketing.setFacebookStatus(1);
								}
								// set Instagram
								if(true == guestMarketingPreference.isSelected() && guestMarketingPreference.getGuestMarketPrefValue().equals("Instagram")){
									addMarketing.setInstagramStatus(1);
								}
								// set Google+
								if(true == guestMarketingPreference.isSelected() && guestMarketingPreference.getGuestMarketPrefValue().equals("Google+")){
									addMarketing.setGooglePlusStatus(1);
								}
								// set Word of Mouth
								if(true == guestMarketingPreference.isSelected() && guestMarketingPreference.getGuestMarketPrefValue().equals("Word of Mouth")){
									addMarketing.setWordofMouthStatus(1);
								}
							}
							addMarketing.setCreatedAt(new Date());
							addMarketing.setCreatedBy(null);
							addMarketing.setActive(1);
							addMarketing.setModifiedAt(new Date());
							addMarketing.setModifiedBy(null);
						
							addMarketingPref(addMarketing);
						}
						
						
					
					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return oWaitlistMetrics;

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * finally { try { cStmt.close(); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */
		return waitListMetrics;

	}

	public WaitlistMetrics updateGuestInfo(Guest guestObj, int actionFlag) {
		WaitlistMetrics waitlistMetrics = null;
		// CallableStatement cStmt = null;
		try {
			// org.hibernate.Session session =
			// (org.hibernate.Session)entityManager.getDelegate();
			/*
			 * org.hibernate.Session session =
			 * (org.hibernate.Session)entityManager.getDelegate(); Connection
			 * conn = session.connection();
			 * 
			 * cStmt = conn.prepareCall("{call UPDATEGUEST(?, ?, ?, " +
			 * "?, ?, ?, ?, ?, ?, ?, ?," + "?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			 * cStmt.setLong(1,guestObj.getOrganizationID()!=null ?
			 * guestObj.getOrganizationID() : 0);
			 * cStmt.setLong(2,guestObj.getGuestID());
			 * cStmt.setString(3,guestObj.getName() != null ? guestObj.getName()
			 * : ""); cStmt.setLong(4,guestObj.getNoOfPeople() != null ?
			 * guestObj.getNoOfPeople() : 0);
			 * cStmt.setString(5,guestObj.getSms() != null ? guestObj.getSms() :
			 * ""); cStmt.setString(6,guestObj.getEmail() != null ?
			 * guestObj.getEmail() : "");
			 * cStmt.setString(7,guestObj.getPrefType() != null ?
			 * guestObj.getPrefType() : "");
			 * cStmt.setBoolean(8,guestObj.isOptin());
			 * cStmt.setString(9,guestObj.getSeatingPreference() != null ?
			 * guestObj.getSeatingPreference() : "");
			 * cStmt.setString(10,guestObj.getNote() != null ?
			 * guestObj.getNote() : ""); cStmt.setLong(11,actionFlag ==
			 * Constants.WAITLIST_UPDATE_CALLOUT ? 1 : 0);
			 * cStmt.setLong(12,actionFlag ==
			 * Constants.WAITLIST_UPDATE_INCOMPLETE ? 1 : 0);
			 * cStmt.setLong(13,actionFlag ==
			 * Constants.WAITLIST_UPDATE_MARK_AS_SEATED ? 1 : 0);
			 * cStmt.setLong(14,actionFlag == Constants.WAITLIST_UPDATE_DELETE ?
			 * 1 : 0); cStmt.registerOutParameter(15, Types.INTEGER);
			 * cStmt.registerOutParameter(16, Types.INTEGER);
			 * cStmt.registerOutParameter(17, Types.INTEGER);
			 * cStmt.registerOutParameter(18, Types.VARCHAR);
			 * cStmt.registerOutParameter(19, Types.INTEGER);
			 * cStmt.registerOutParameter(20, Types.INTEGER); cStmt.execute();
			 * 
			 * oWaitlistMetrics.setNowServingParty(cStmt.getInt(15));
			 * oWaitlistMetrics.setTotalWaitingGuest(cStmt.getInt(16));
			 * oWaitlistMetrics.setTotalWaitTime(cStmt.getInt(17));
			 * oWaitlistMetrics.setNoOfPartiesAhead(cStmt.getInt(18));
			 * oWaitlistMetrics.setGuestToBeNotified(cStmt.getInt(19));
			 * oWaitlistMetrics.setGuestNotifiedWaitTime(cStmt.getInt(20));
			 */
			waitlistMetrics = sessionFactory.getCurrentSession().doReturningWork(new ReturningWork<WaitlistMetrics>() {

				/* changes by krupali, line 1191 to 1231 (15/06/2017) */
				@Override
				public WaitlistMetrics execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call UPDATEGUEST(?, ?, ?, ?,"
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
					WaitlistMetrics oWaitlistMetrics = new WaitlistMetrics();
					try {
						cStmt.setLong(1, guestObj.getOrganizationID() != null ? guestObj.getOrganizationID() : 0);
						cStmt.setLong(2, guestObj.getGuestID());
						cStmt.setString(3, guestObj.getName() != null ? guestObj.getName() : "");
						cStmt.setLong(4, guestObj.getNoOfChildren() != null ? guestObj.getNoOfChildren() : 0);
						cStmt.setLong(5, guestObj.getNoOfAdults() != null ? guestObj.getNoOfAdults() : 0);
						cStmt.setLong(6, guestObj.getNoOfInfants() != null ? guestObj.getNoOfInfants() : 0);
						cStmt.setLong(7, guestObj.getNoOfPeople() != null ? guestObj.getNoOfPeople() : 0);
						cStmt.setLong(8,
								guestObj.getLanguagePrefID() != null ? guestObj.getLanguagePrefID().getLangId() : 0);
						cStmt.setInt(9, guestObj.getQuoteTime() != null ? guestObj.getQuoteTime() : 0);
						cStmt.setInt(10, guestObj.getPartyType() != null ? guestObj.getPartyType() : 0);
						cStmt.setString(11, guestObj.getSms() != null ? guestObj.getSms() : "");
						cStmt.setString(12, guestObj.getEmail() != null ? guestObj.getEmail() : "");
						cStmt.setString(13, guestObj.getPrefType() != null ? guestObj.getPrefType() : "");
						cStmt.setBoolean(14, guestObj.isOptin());
						cStmt.setString(15,
								guestObj.getSeatingPreference() != null ? guestObj.getSeatingPreference() : "");
						cStmt.setString(16, guestObj.getNote() != null ? guestObj.getNote() : "");
						cStmt.setLong(17, actionFlag == Constants.WAITLIST_UPDATE_CALLOUT ? 1 : 0);
						cStmt.setLong(18, actionFlag == Constants.WAITLIST_UPDATE_INCOMPLETE ? 1 : 0);
						cStmt.setLong(19, actionFlag == Constants.WAITLIST_UPDATE_MARK_AS_SEATED ? 1 : 0);
						cStmt.setLong(20, actionFlag == Constants.WAITLIST_UPDATE_DELETE ? 1 : 0);
						cStmt.registerOutParameter(21, Types.INTEGER);
						cStmt.registerOutParameter(22, Types.INTEGER);
						cStmt.registerOutParameter(23, Types.INTEGER);
						cStmt.registerOutParameter(24, Types.VARCHAR);
						cStmt.registerOutParameter(25, Types.INTEGER);
						cStmt.registerOutParameter(26, Types.INTEGER);
						cStmt.registerOutParameter(27, Types.VARCHAR);
						// change by sunny for updating Marketing Pref
						cStmt.setString(28,
								guestObj.getMarketingPreference() != null ? guestObj.getMarketingPreference() : "");
						cStmt.setString(29,
								guestObj.getCustomPreference() != null ? guestObj.getCustomPreference() : "");
						cStmt.execute();

						oWaitlistMetrics.setGuestId(guestObj.getGuestID().intValue());
						oWaitlistMetrics.setNowServingParty(cStmt.getInt(21));
						oWaitlistMetrics.setTotalWaitingGuest(cStmt.getInt(22));
						oWaitlistMetrics.setTotalWaitTime(cStmt.getInt(23));
						oWaitlistMetrics.setNoOfPartiesAhead(cStmt.getInt(24));
						oWaitlistMetrics.setGuestToBeNotified(cStmt.getInt(25));
						oWaitlistMetrics.setGuestNotifiedWaitTime(cStmt.getInt(26));
						oWaitlistMetrics.setClientBase(cStmt.getString(27));
						// change by sunny line 1482 to 1522 (03-08-2018)
						if(null != guestObj.getGuestMarketingPreferences()){
							AddMarketing addMarketing = new AddMarketing();
							Query query = sessionFactory.openSession().createQuery("FROM AddMarketing WHERE guestID ="+ Integer.parseInt(guestObj.getGuestID().toString()));
							List guestMarketingList = query.list();
							System.out.println(guestMarketingList.size());
							for(int i=0; i<guestMarketingList.size();i++){
								addMarketing = (AddMarketing) guestMarketingList.get(i);
								addMarketing.setFacebookStatus(0);
								addMarketing.setInstagramStatus(0);
								addMarketing.setGooglePlusStatus(0);
								addMarketing.setWordofMouthStatus(0);
							}
							for(int i= 0; i<guestObj.getGuestMarketingPreferences().size(); i++){
								GuestMarketingPreference guestMarketingPreference = new GuestMarketingPreference();
								guestMarketingPreference = guestObj.getGuestMarketingPreferences().get(i);
								
								// set Facebook
								if(true == guestMarketingPreference.isSelected() && guestMarketingPreference.getGuestMarketPrefValue().equals("Facebook")){
									addMarketing.setFacebookStatus(1);
								}
								// set Instagram
								if(true == guestMarketingPreference.isSelected() && guestMarketingPreference.getGuestMarketPrefValue().equals("Instagram")){
									addMarketing.setInstagramStatus(1);
								}
								// set Google+
								if(true == guestMarketingPreference.isSelected() && guestMarketingPreference.getGuestMarketPrefValue().equals("Google+")){
									addMarketing.setGooglePlusStatus(1);
								}
								// set Word of Mouth
								if(true == guestMarketingPreference.isSelected() && guestMarketingPreference.getGuestMarketPrefValue().equals("Word of Mouth")){
									addMarketing.setWordofMouthStatus(1);
								}
							}
								addMarketing.setModifiedAt(new Date());
								addMarketingPref(addMarketing);
						}
						
					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return oWaitlistMetrics;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return waitlistMetrics;
	}

	public Object[] deleteGuest(String guestId) {
		List<Object[]> resultList = null;
		Object[] metricsArray = null;
		try {
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("{call deleteGuestProc(:guestId, @guestId, "
					+ "@nowServingParty, @totalWaitTime, @guestIdToBeNotified)}");  
			query.setParameter("guestId",guestId);   
			resultList = query.list();
			if(resultList != null) {
				metricsArray = resultList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return metricsArray;

	}

	@Override
	public WaitlistMetrics changeNotificationThreshold(int numberOfUsers,int perPartyWaitTime, Long orgid) throws RsntException {
		// TODO Auto-generated method stub
		/*List<Object[]> resultList = null;
		Object[] metricsArray = null;
		Query query = entityManager.createNativeQuery("{call changeNotificationThreshold(:numberOfUsers, :orgId,  @guestId, "
				+ "@nowServingParty, @totalWaitTime, @guestIdToBeNotified)}");  
		
		
		
		query.setParameter("numberOfUsers",numberOfUsers);
		query.setParameter("orgId",orgId);
		resultList = query.getResultList();
		if(resultList != null) {
			metricsArray = resultList.get(0);
		}
		return metricsArray;*/
		WaitlistMetrics waitlistMetrics = new WaitlistMetrics();
		//CallableStatement cStmt = null;
		try {
			/*org.hibernate.Session session = (org.hibernate.Session)entityManager.getDelegate();
			Connection conn = session.connection();
			cStmt = conn.prepareCall("{call UPDATEHEADERMETRICS(?, ?, ?, ?, ?, ?)}");  
			cStmt.setLong(1,orgid);
			cStmt.setLong(2,perPartyWaitTime);
			cStmt.setLong(3,numberOfUsers);
			cStmt.registerOutParameter(4, Types.INTEGER);
			cStmt.registerOutParameter(5, Types.INTEGER);
			cStmt.registerOutParameter(6, Types.INTEGER);
			cStmt.execute();
			
			oWaitlistMetrics.setNowServingParty(cStmt.getInt(4));
			oWaitlistMetrics.setTotalWaitingGuest(cStmt.getInt(5));
			oWaitlistMetrics.setTotalWaitTime(cStmt.getInt(6));*/
			waitlistMetrics = sessionFactory.getCurrentSession().doReturningWork(new ReturningWork<WaitlistMetrics>() {
				
				@Override
				public WaitlistMetrics execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call UPDATEHEADERMETRICS(?, ?, ?, ?, ?, ?, ?)}");  
					WaitlistMetrics oWaitlistMetrics = new WaitlistMetrics();
					
					try {
						cStmt.setLong(1, orgid);
						cStmt.setLong(2, perPartyWaitTime);
						cStmt.setLong(3, numberOfUsers);
						cStmt.registerOutParameter(4, Types.INTEGER);
						cStmt.registerOutParameter(5, Types.INTEGER);
						cStmt.registerOutParameter(6, Types.INTEGER);
						cStmt.registerOutParameter(7, Types.VARCHAR);
						cStmt.execute();

						oWaitlistMetrics.setNowServingParty(cStmt.getInt(4));
						oWaitlistMetrics.setTotalWaitingGuest(cStmt.getInt(5));
						oWaitlistMetrics.setTotalWaitTime(cStmt.getInt(6));
						oWaitlistMetrics.setClientBase(cStmt.getString(7));
					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return oWaitlistMetrics;
					
				}
			});
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		return waitlistMetrics;
	}
	@Override
	public List<Object[]> getLookupsForLookupType(Long pLookupTypeId)
			throws RsntException {
		try{
			return sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_LOOKUPS_FOR_LOOKUPTYPE).setParameter(0, pLookupTypeId).list();
		}catch(Exception e){
			e.printStackTrace();
			throw new RsntException(e);
		}
	}
	@Override
	public WaitlistMetrics changePerPartyWaitTime(int numberOfUsers,
			int perPartyWaitTime, Long orgid) throws RsntException {
		WaitlistMetrics oWaitlistMetrics = new WaitlistMetrics();
		//CallableStatement cStmt = null;
		try {
			/*org.hibernate.Session session = (org.hibernate.Session)entityManager.getDelegate();
			Connection conn = session.connection();
			cStmt = conn.prepareCall("{call UPDATEHEADERMETRICS(?, ?, ?, ?, ?, ?)}");  
			cStmt.setLong(1,orgid);
			cStmt.setLong(2,perPartyWaitTime);
			cStmt.setLong(3,numberOfUsers);
			cStmt.registerOutParameter(4, Types.INTEGER);
			cStmt.registerOutParameter(5, Types.INTEGER);
			cStmt.registerOutParameter(6, Types.INTEGER);
			cStmt.execute();
			
			oWaitlistMetrics.setNowServingParty(cStmt.getInt(4));
			oWaitlistMetrics.setTotalWaitingGuest(cStmt.getInt(5));
			oWaitlistMetrics.setTotalWaitTime(cStmt.getInt(6));*/
			
			sessionFactory.getCurrentSession().doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call UPDATEHEADERMETRICS(?, ?, ?, ?, ?, ?, ?)}");  
					cStmt.setLong(1,orgid);
					cStmt.setLong(2,perPartyWaitTime);
					cStmt.setLong(3,numberOfUsers);
					cStmt.registerOutParameter(4, Types.INTEGER);
					cStmt.registerOutParameter(5, Types.INTEGER);
					cStmt.registerOutParameter(6, Types.INTEGER);
					cStmt.registerOutParameter(7, Types.VARCHAR);
					cStmt.execute();
					
					oWaitlistMetrics.setNowServingParty(cStmt.getInt(4));
					oWaitlistMetrics.setTotalWaitingGuest(cStmt.getInt(5));
					oWaitlistMetrics.setTotalWaitTime(cStmt.getInt(6));
					oWaitlistMetrics.setClientBase(cStmt.getString(7));
					
				}
			});
		}
		catch(Exception e){
			throw new RsntException(e);
		}
		return oWaitlistMetrics;
	}
	/*
	 +	 * (non-Javadoc)
	 +	 * @see com.rsnt.service.IWaitListService#loadGuests
ByOrgRecords(java.lang.Long, int, int)
	 +	 *
	 +	 *this method is load for get history with pagination create by ronak
	 		HQL_GET_GUESTS_HISTORY was used before adding dropdown for All,callcount and incomplete
	 +	 */
	 	@SuppressWarnings("unchecked")
	 	@Override
	 	public List<Guest> loadGuestsHistoryByOrgRecords(Long orgid, int recordsPerPage, int pageNumber,String statusOption, int sliderMinTime, int sliderMaxTime, String clientTimezone) throws RsntException {
	 		try {
	 			int firstPage = (pageNumber == 1) ? 0 : (recordsPerPage*(pageNumber-1));
	 			
	 			String sliderMinTimeString = sliderMinTime+":00";
	 			String sliderMaxTimeString = sliderMaxTime+":01";
	 			StringBuffer query=new StringBuffer("FROM Guest g left join fetch g.languagePrefID WHERE g.resetTime is  null and g.status not in ('CHECKIN')"
	 					+ " and g.OrganizationID=:orgId and ((time(convert_tz(g.checkinTime,'-05:00', :ClientTimezone)) between time(:SliderMinValue) and time(:SliderMaxValue)))");
	 			/*convert_tz(g.checkinTime,'+05:30','-05:00'*/
	 			if(statusOption.equals("Not Present")) {
	 				query=query.append(" and calloutCount > 0");
	 			}
	 			if(statusOption.equals("Incomplete")) {
	 				query=query.append(" and incompleteParty > 0");
	 			}
	 			query=query.append(" order by g.rank asc");
	 			//HQL_GET_GUESTS_HISTORY
	 			return sessionFactory.getCurrentSession().createQuery(query.toString())
	 					.setParameter(Constants.RSNT_ORG_ID,orgid).setParameter("SliderMinValue", sliderMinTimeString).setParameter("SliderMaxValue", sliderMaxTimeString).setParameter("ClientTimezone", clientTimezone).setFirstResult(firstPage).setMaxResults(recordsPerPage).list();
	 		} catch (Exception e) {
	 			log.error("loadGuestsHistoryByOrg()", e);
	 			throw new RsntException(e);
	 		}
	 	}
	 	
	 	@SuppressWarnings("unchecked")
	 	@Override
	 	public List<Guest> loadGuestsHistoryByName(Long orgid, int recordsPerPage, int pageNumber,String statusOption, int sliderMinTime, int sliderMaxTime, String searchName, String clientTimezone) throws RsntException {
	 		try {
	 			int firstPage = (pageNumber == 1) ? 0 : (recordsPerPage*(pageNumber-1));
	 			String name = searchName;
	 			String sliderMinTimeString = sliderMinTime+":00";
	 			String sliderMaxTimeString = sliderMaxTime+":01";
	 			StringBuffer query=new StringBuffer("FROM Guest g left join fetch g.languagePrefID WHERE g.resetTime is  null and g.status not in ('CHECKIN')"
	 					+ " and g.OrganizationID=:orgId and ((time(convert_tz(g.checkinTime,'-05:00', :ClientTimezone)) between time(:SliderMinValue) and time(:SliderMaxValue)))");
	 			if(statusOption.equals("Not Present")) {
	 				query=query.append(" and calloutCount > 0");
	 			}
	 			if(statusOption.equals("Incomplete")) {
	 				query=query.append(" and incompleteParty > 0");
	 			}
	 			if(name != null) {
	 				query=query.append(" and g.name like :Name");
	 			}
	 			query=query.append(" order by g.rank asc");
	 			//HQL_GET_GUESTS_HISTORY
	 			return sessionFactory.getCurrentSession().createQuery(query.toString())
	 					.setParameter(Constants.RSNT_ORG_ID,orgid).setParameter("SliderMinValue", sliderMinTimeString).setParameter("SliderMaxValue", sliderMaxTimeString).setParameter("ClientTimezone", clientTimezone).setParameter("Name", "%"+name+"%").setFirstResult(firstPage).setMaxResults(recordsPerPage).list();
	 		} catch (Exception e) {
	 			log.error("loadGuestsHistoryByName()", e);
	 			throw new RsntException(e);
	 		}
	 	}
	 	
	 	/*
	 	 * (non-Javadoc)
	 	 * @see com.kyobee.service.IWaitListService#getHistoryUsersCountForOrg(java.lang.Long, java.lang.String)
	 	 *changed query from HQL_GET_GUESTS_COUNT_HISTORY as dropdown is needed for All,callcount and incomplete
	 	 */
	 	@Override
		public Long getHistoryUsersCountForOrg(Long orgid,String statusOption,int sliderMinTime,int sliderMaxTime, String clientTimezone) throws RsntException{
			try {
				String sliderMinTimeString = sliderMinTime+":00";
	 			String sliderMaxTimeString = sliderMaxTime+":01";
				StringBuffer query=new StringBuffer("select count(*) FROM Guest g WHERE g.resetTime is  null and g.status not in ('CHECKIN')"
	 					+ " and g.OrganizationID=:orgId and ((time(convert_tz(g.checkinTime,'-05:00', :ClientTimezone)) between time(:SliderMinValue) and time(:SliderMaxValue)))");
	 			if(statusOption.equals("Not Present")) {
	 				query=query.append(" and calloutCount > 0");
	 			}
	 			if(statusOption.equals("Incomplete")) {
	 				query=query.append(" and incompleteParty > 0");
	 			}
	 			query=query.append(" order by g.rank asc");
				return (Long) sessionFactory.getCurrentSession().createQuery(query.toString())
						.setParameter(Constants.RSNT_ORG_ID,orgid).setParameter("SliderMinValue", sliderMinTimeString).setParameter("SliderMaxValue", sliderMaxTimeString).setParameter("ClientTimezone", clientTimezone).uniqueResult();
			} catch (Exception e) {
				log.error("loadAllCheckinUsers()", e);
				throw new RsntException(e);
			}

		}
	 	
	 	@Override
		public Long getHistoryUsersCountForName(Long orgid,String statusOption,int sliderMinTime,int sliderMaxTime,String searchName, String clientTimezone) throws RsntException{
			try {
				String name = searchName;
				int SliderMaxTimeLessOne = sliderMaxTime-1;
				String sliderMinTimeString = sliderMinTime+":00";
	 			String sliderMaxTimeString = sliderMaxTime+":01";
				StringBuffer query=new StringBuffer("select count(*) FROM Guest g WHERE g.resetTime is  null and g.status not in ('CHECKIN')"
	 					+ " and g.OrganizationID=:orgId and ((time(convert_tz(g.checkinTime,'-05:00', :ClientTimezone)) between time(:SliderMinValue) and time(:SliderMaxValue)))");
	 			if(statusOption.equals("Not Present")) {
	 				query=query.append(" and calloutCount > 0");
	 			}
	 			if(statusOption.equals("Incomplete")) {
	 				query=query.append(" and incompleteParty > 0");
	 			}
	 			if(name != null) {
	 				query=query.append(" and g.name like :Name");
	 			}
	 			query=query.append(" order by g.rank asc");
				return (Long) sessionFactory.getCurrentSession().createQuery(query.toString())
						.setParameter(Constants.RSNT_ORG_ID,orgid).setParameter("SliderMinValue", sliderMinTimeString).setParameter("SliderMaxValue", sliderMaxTimeString).setParameter("ClientTimezone", clientTimezone).setParameter("Name", "%"+name+"%").uniqueResult();
			} catch (Exception e) {
				log.error("getHistoryUsersCountForName()", e);
				throw new RsntException(e);
			}

		}
	 	
	 	/*Get Organization language preferences by OrgId (krupali 07/11/2017)*/
	 	@SuppressWarnings("unchecked")
		@Override
		public List<LanguageMasterDTO> getOrganizationLanguagePref(Long orgId) {
			List<LanguageMasterDTO> langPref = null;
			LanguageMasterDTO dto = new LanguageMasterDTO();
			List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_LANGUAGE_PREF_VALUES).
					setParameter("orgId", orgId).list();
			if(null != list && list.size()>0){
				langPref = new ArrayList<LanguageMasterDTO>();
				for (Object[] pref : list) {
					dto = new LanguageMasterDTO();
					dto.setLangId(Long.valueOf(pref[0].toString()));
					dto.setLangName(pref[1].toString());
					dto.setLangIsoCode(pref[2].toString());
					//dto.setLangDisplayName(pref[3].toString());
					/*dto.setPrefValueId(Long.valueOf(pref[0].toString()));
					dto.setPrefValue(pref[1].toString());*/
					langPref.add(dto);
				}
			}
			return langPref;
		}
	 	
	 	/*for getting language preferences by id (krupali 13/11/2017)*/
	 	@SuppressWarnings("unchecked")
		@Override
		public LanguageMasterDTO getLangPrefById(Long languagePrefID) {
			// TODO Auto-generated method stub
			LanguageMasterDTO langPref = new LanguageMasterDTO();
			List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_USER_LANGUAGE_PREF_VALUES).
					setParameter("langId", languagePrefID).list();
			if(null != list && list.size()>0){
				for (Object[] pref : list) {
					langPref.setLangId(Long.valueOf(pref[0].toString()));
					langPref.setLangName(pref[1].toString());
					langPref.setLangIsoCode(pref[2].toString());
				}
			}
			return langPref;
		}
		
	 	@SuppressWarnings("unchecked")
		@Override
		public List<OrganizationTemplateDTO> getOrganizationTemplates(Long orgId,Long langID,Integer levelId) {
			List<OrganizationTemplateDTO> templates = null;
			OrganizationTemplateDTO dto = new OrganizationTemplateDTO();
			StringBuffer query= new StringBuffer("select SmsTemplateID, TemplateText, LanguageID, Level"
					+ " from ORGANIZATIONTEMPLATE ot"
					+ " where ot.OrgID=:orgId and ot.LanguageID=:langID and ot.Active=1");
			
			if(levelId!=null) {
				query=query.append(" and ot.Level=:levelId");
			}
			
			//String queryStr=query.toString();
			SQLQuery queryStr=sessionFactory.getCurrentSession().createSQLQuery(query.toString());
			queryStr.setParameter("orgId", orgId);
			queryStr.setParameter("langID", langID);
			if(levelId!=null) {
				queryStr.setParameter("levelId", levelId);
			}
			
			List<Object[]> list = queryStr.list();
			if(null != list && list.size()>0){
				templates = new ArrayList<OrganizationTemplateDTO>();
				for (Object[] template : list) {
					dto = new OrganizationTemplateDTO();
					dto.setSmsTemplateID(Long.valueOf(template[0].toString()));
					dto.setTemplateText(template[1].toString());
					dto.setLanguageID(Long.valueOf(template[2].toString()));
					dto.setLevel(Integer.valueOf(template[3].toString()));
					templates.add(dto);
				}
			}


			return templates;
		}
	 	
		@Override
		public ScreensaverDTO getOrganizationScreensaver(long orgId) {
			ScreensaverDTO screensaver = new ScreensaverDTO();
			List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_SCREENSAVER).
					setParameter("orgId", orgId).list();
			if(null != list && list.size()>0){
				for (Object[] dto : list) {
					screensaver.setScreensaverFlag(dto[0].toString());
					if(screensaver.getScreensaverFile() != null){
						screensaver.setScreensaverFile(dto[1].toString());
					}else{
						screensaver.setScreensaverFile(null);
					}
				}
			}
			return screensaver;
		}
	 	
	 	@SuppressWarnings("unchecked")
		@Override
	 	public void saveSmsLog(Guest guest, Long orgId, Long templateId, String smsText){
	 		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 		Date date = new Date();
	 		
			SmsLog log = new SmsLog();
			log.setOrgID(orgId);
			log.setGuestID(guest.getGuestID());
			log.setPhoneNo(guest.getSms());
			log.setMsgText(smsText);
			log.setProcess("P");
			log.setCreatedBy("admin");
			log.setCreatedAt(dateFormat.format(date));
			log.setModifiedAt(dateFormat.format(date));
			
			if(templateId!=null){
			Integer templateLevel = fetchTemplateLevel(orgId, templateId);
			log.setTemplateID(templateId);
			log.setTempLevel(templateLevel);
			}
			
			Long i = (Long) sessionFactory.getCurrentSession().save(log);
		}
	 	
	 	@SuppressWarnings("unchecked")
		@Override
	 	public Integer fetchTemplateLevel(Long orgId, Long templateId){
	 		Integer templateLevel=null;
	 		
	 		List<Integer> lists = sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_ORG_SMS_TEMPLATE_LEVEL_BY_ID).
	 				setParameter("orgId", orgId).setParameter("templateID", templateId).list();
	 				for (Integer level : lists) {
	 					templateLevel = level;
	 				}
	 				
	 		return templateLevel;
	 	}
	 	
	 	public WaitlistMetrics convertToObject(Map<String, String> metricsMap) {
			WaitlistMetrics wMetrics=new WaitlistMetrics();
			//wMetrics.setGuestId();
			wMetrics.setNowServingParty(Integer.parseInt(metricsMap.get(Constants.RSNT_GUEST_RANK_MIN)));
			wMetrics.setTotalWaitTime(Integer.parseInt(metricsMap.get(Constants.RSNT_ORG_TOTAL_WAIT_TIME)));
			wMetrics.setGuestToBeNotified(Integer.parseInt(metricsMap.get("OP_GUESTTOBENOTIFIED")));
			wMetrics.setTotalWaitingGuest(Integer.parseInt(metricsMap.get(Constants.RSNT_ORG_GUEST_COUNT)));
			wMetrics.setNoOfPartiesAhead(Integer.parseInt(metricsMap.get("OP_NOOFPARTIESAHEAD")));
			//wMetrics.setGuestRank(Integer.parseInt(metricsMap.get("OP_NOOFPARTIESAHEAD")));
			wMetrics.setGuestNotifiedWaitTime(Integer.parseInt(metricsMap.get("OP_GUESTNOTIFIEDWAITTIME")));
			wMetrics.setClientBase(metricsMap.get("CLIENT_BASE"));
			return wMetrics;		
		}
	 	
		@Override
		public String getSmsRouteByOrgid(Long orgId) throws Exception {
			//return sessionFactory.getCurrentSession().createQuery(NativeQueryConstants.GET_SMSROUTE_BY_ORGID).setParameter("orgID",orgID).uniqueResult();
			
			return (String) sessionFactory.getCurrentSession().createSQLQuery(NativeQueryConstants.GET_SMSROUTE_BY_ORGID).setParameter("orgId",orgId).uniqueResult();
		}
		/*change by sunny For add Marketing Pre (31-07-2018)*/
		@Override
		public Response<Map<String, Object>> addMarketingPref(AddMarketing addMarketing) {
			Response<Map<String, Object>> response = new Response<Map<String, Object>>();
			try{	
				sessionFactory.getCurrentSession().saveOrUpdate(addMarketing);
				
			}catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				
			}
			response.setStatus("SUCCESS");
			return response;
		}

}