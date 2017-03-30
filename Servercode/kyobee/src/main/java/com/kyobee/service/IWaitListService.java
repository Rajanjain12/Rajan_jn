/**
 * EJB Local interface provides Guest interaction methods
 */
package com.kyobee.service;

import java.util.List;
import java.util.Map;

import com.kyobee.dto.GuestPreferencesDTO;
import com.kyobee.dto.GuestWrapper;
import com.kyobee.dto.WaitlistMetrics;
import com.kyobee.entity.Guest;
import com.kyobee.entity.GuestNotificationBean;
import com.kyobee.entity.GuestPreferences;
import com.kyobee.exception.RsntException;


public interface IWaitListService {
	public String SEAM_NAME = "waitListService";
	public String JNDI_NAME="waitListServiceImpl";
	
	public String getGuestRankMin(Long orgId) ;
	
	/**
	 * Save or update Guest
	 * @param guest
	 * @return Guest
	 * @throws RsntException
	 */
	public Guest saveorUpdateGuest(Guest guest) throws RsntException;
	/**
	 * Save or update Guest preferences
	 * @param guest
	 * @return Guest
	 * @throws RsntException
	 */
	public GuestPreferences saveorUpdateGuestPreferences(GuestPreferences pref) throws RsntException;
	/**
	 * Notify first n users
	 * @param numberOfUsers
	 * @param orgid
	 */
	public void notifyUsers(int numberOfUsers,Long orgid);
	/**
	 * Fetch All check-in users
	 * @param orgid
	 * @param recordsPerPage
	 * @param pageNumber
	 * @return
	 * @throws RsntException
	 */
	public List<GuestWrapper> loadAllCheckinUsers(Long orgid, int recordsPerPage, int pageNumber) throws RsntException;
	/**
	 * Fetch History of Guests by orgId
	 * @param orgid
	 * @return List<Guest>
	 * @throws RsntException
	 */
	public List<Guest> loadGuestsHistoryByOrg(Long orgid) throws RsntException;
	/**
	 * Ftech Guest By id(pkId)
	 * @param guestId
	 * @return Guest
	 */
	public Guest getGuestById(long guestId);
	/**
	 * Fetch unique Guest by UUID
	 * @param uuid
	 * @returnv Guest
	 */
	public Guest getGuestByUUID(String uuid);
	/**
	 * Update organization wait time by orgId
	 * @param waitTime
	 * @param orgid
	 * @return 0 or 1 value
	 */
	public int updateOrganizationWaitTime(int waitTime,Long orgid);
	/**
	 * SET all guests status as RESET by orgid
	 * @param orgid
	 * @return 0 or 1
	 */
	public int resetOrganizationsByOrgid(Long orgid);
	/**
	 * Set status as DELETED to guest by guestId
	 * @param guestId
	 * @return 0 or 1
	 */
	public int deleteGuestbyId(Long guestId);
	/**
	 * Get Organization Metrics like Total part wait time 
	 * @param orgId
	 * @return Map<String, String>
	 */
	public Map<String, String> getTotalPartyWaitTimeMetrics(Long orgId);
	/**
	 * Get the Guesrt Metrics like Guest wait time and 
	 * Number of guests a head
	 * @param guestId
	 * @param orgId
	 * @return Map<String, String>
	 */
	public Map<String, String> getGuesteMetrics(Long guestId,Long orgId);
	/**
	 * Notify Guest by his preferences
	 * @param guestId
	 */
	public void notifyGuestByPref(Long guestId);
	
	/**
	 * Backup the Waiting List information by send them to a 
	 * special email configured in Organization level
	 * @param guestId
	 */
	//public void sendWaitListBackupEmail(Long guestId, boolean isUpdate);
	
	/**
	 * Send Ready Notification to guest
	 * @param guestId
	 */
	public void readyNotifyGuestByPref(Long guestId);
	/**
	 * Send notification to users , if any of guest status is changed
	 * @param currentguestid
	 * @param orgid
	 */
	public void notifyUsersOnStatusChange(Long currentguestid, Long orgid);
	/**
	 * Get the max rank or latest rank value of an Organization
	 * @param orgId
	 * @return rankValue
	 */
	public Guest getMaxGuestRankByOrgId(Long orgId);
	/**
	 * Get Organization seating preferences by OrgId
	 * @param orgId
	 * @return {@link List<GuestPreferencesDTO>}
	 */
	public List<GuestPreferencesDTO> getOrganizationSeatingPref(long orgId);
	/**
	 * Updates the total waittime of the organization
	 * @param organizationId
	 * @param incrementTimeFlag : true if wait time is to be incremented else false
	 */
	public void updateTotalWaitTime(Long organizationId, boolean incrementTimeFlag);
	
	/**
	 * Saves newly added Guest information
	 * @param guestObj
	 * @return WaitlistMetrics
	 */
	public WaitlistMetrics addGuest(Guest guestObj);
	
	/**
	 * Sends notification via SMS/EMail to Guest
	 * @param guestNotificationBean
	 */
	public void sendNotificationToGuest(GuestNotificationBean guestNotificationBean);
	
	/**
	 * Updates Guest information
	 * @param guestObj
	 * @param actionFlag
	 * @return
	 */
	public WaitlistMetrics updateGuestInfo(Guest guestObj, int actionFlag);
	
	/**
	 * Delete Guest
	 * @param guestId
	 * @return JSON String of Waitlist metrics
	 */
	public Object[] deleteGuest(String guestId);
	
	/**
	 * Change Notification threshold
	 * @param numberOfUsers
	 * @param waitTime
	 * @param orgid
	 * @return
	 * @throws RsntException 
	 */
	public WaitlistMetrics changeNotificationThreshold(int numberOfUsers,int waitTime, Long orgid) throws RsntException;
	/**
	 * Change Per Party Wait Time
	 * @param numberOfUsers
	 * @param waitTime
	 * @param orgid
	 * @return
	 * @throws RsntException 
	 */
	public WaitlistMetrics changePerPartyWaitTime(int numberOfUsers, int waitTime,Long orgid) throws RsntException;
	/**
	 * 
	 * @param pLookupTypeId
	 * @return
	 * @throws RsntException
	 */
	public List<Object[]> getLookupsForLookupType(final Long pLookupTypeId) throws RsntException;

	//public void sendNotificationToGuestForSeated(GuestNotificationBean guestNotificationBean);
	public List<Guest> loadGuestsHistoryByOrgRecords(Long orgid, int recordsPerPage, int pageNumber)throws RsntException;

	Long getAllCheckinUsersCount(Long orgid) throws RsntException;

	Long getHistoryUsersCountForOrg(Long orgid) throws RsntException;
}
