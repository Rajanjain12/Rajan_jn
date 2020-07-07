package com.kyobeeWaitlistService.daoImpl;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dao.GuestCustomDAO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Guest;

import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@Repository
@Transactional
public class GuestCustomDAOImpl implements GuestCustomDAO {

	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Guest> fetchAllGuestHistoryList(Integer orgId, Integer pageSize, Integer startIndex, String searchText,
			String clientTimezone, Integer sliderMaxTime, Integer sliderMinTime, String statusOption) {
		// for fetching data according to page number

		List<Guest> guestList = null;

		try {

			String sliderMinTimeString = sliderMinTime + ":00";
			String sliderMaxTimeString = sliderMaxTime + ":01";
			StringBuilder query = new StringBuilder(
					"SELECT * FROM GUEST g WHERE g.resetTime is  null and g.status not in ('CHECKIN')"
					+ " and g.OrganizationID=:orgId and ((time(convert_tz(g.checkinTime,'-05:00', :clientTimezone)) between time(:sliderMinValue) and time(:sliderMaxValue)))");
			if (statusOption.equals("Not Present")) {
				query = query.append(" and calloutCount > 0");
			}
			if (statusOption.equals("Incomplete")) {
				query = query.append(" and incompleteParty > 0");
			}
			if((searchText != null) && (!searchText.equalsIgnoreCase("null"))) {
				query=query.append(" and (g.name like :searchText  or g.contactNo like :searchText)");
			}
			query = query.append(" order by g.rank asc limit :pageSize OFFSET :startIndex");
			if ((searchText != null) && (!searchText.equalsIgnoreCase("null"))) {
				guestList = entityManager.createNativeQuery(query.toString(), Guest.class).setParameter("orgId", orgId)
						.setParameter("sliderMinValue", sliderMinTimeString)
						.setParameter("sliderMaxValue", sliderMaxTimeString)
						.setParameter("clientTimezone", clientTimezone)
						.setParameter("searchText", "%" + searchText + "%").setParameter("pageSize", pageSize)
						.setParameter("startIndex", startIndex).getResultList();

			} else {
				guestList = entityManager.createNativeQuery(query.toString(), Guest.class).setParameter("orgId", orgId)
						.setParameter("sliderMinValue", sliderMinTimeString)
						.setParameter("sliderMaxValue", sliderMaxTimeString)
						.setParameter("clientTimezone", clientTimezone).setParameter("pageSize", pageSize)
						.setParameter("startIndex", startIndex).getResultList();

			}
		} catch (Exception e) {
			LoggerUtil.logError("Error in fetch history  " + e.getMessage());
		}
		return guestList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Guest> fetchAllGuestList(Integer orgId, Integer pageSize, Integer startIndex, String searchText) {
		// for fetching data according to page number

		List<Guest> guestList = null;

		try {

			StringBuilder query = new StringBuilder(
					"SELECT * FROM GUEST g join LANGMASTER l on l.langID=g.languagePrefID WHERE g.Status ='CHECKIN' and g.ResetTime is null and g.OrganizationID=:orgId");

			if ((searchText != null) && (!searchText.equalsIgnoreCase("null"))) {
				query = query.append(" and (g.Name like :searchText  or g.ContactNo like :searchText)");
			}
			query = query.append(" order by g.Rank asc limit :pageSize OFFSET :startIndex");
			if ((searchText != null) && (!searchText.equalsIgnoreCase("null"))) {
				guestList = entityManager.createNativeQuery(query.toString(), Guest.class).setParameter("orgId", orgId)
						.setParameter("searchText", "%" + searchText + "%").setParameter("pageSize", pageSize)
						.setParameter("startIndex", startIndex).getResultList();

			} else {
				guestList = entityManager.createNativeQuery(query.toString(), Guest.class).setParameter("orgId", orgId)
						.setParameter("pageSize", pageSize).setParameter("startIndex", startIndex).getResultList();

			}
		} catch (Exception e) {
			LoggerUtil.logError("Error in fetch history  " + e.getMessage());
		}
		return guestList;
	 }

	@Override
	public WaitlistMetrics addGuest(GuestDTO guestObj,String seatingPref) {
		
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
		Session session = sessionFactory.openSession();
		WaitlistMetrics waitlistMetrics = null;
		try {

			waitlistMetrics = session.doReturningWork(new ReturningWork<WaitlistMetrics>() {

				@Override
				public WaitlistMetrics execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call ADDGUESTTECHREVAMP(? , ? , ? , ? , "
							+ "?, ?, ?, ? , ?, ?, ?, ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )}");

					WaitlistMetrics waitlistMetrics = new WaitlistMetrics();
					try {
						cStmt.setLong(1, guestObj.getOrganizationID());
						cStmt.setString(2, guestObj.getName());
						cStmt.setString(3, guestObj.getUuid());
						cStmt.setLong(4, guestObj.getNoOfChildren());
						cStmt.setLong(5, guestObj.getNoOfAdults());
						cStmt.setLong(6, guestObj.getNoOfInfants());

						cStmt.setLong(7, guestObj.getNoOfPeople());
						cStmt.setLong(8, guestObj.getLanguagePref().getLangId());
						cStmt.setInt(9, guestObj.getPartyType());
						cStmt.setString(10, guestObj.getDeviceType());
						cStmt.setString(11, guestObj.getDeviceId());
						cStmt.setString(12, guestObj.getContactNo());
						cStmt.setString(13, guestObj.getEmail());
						cStmt.setString(14, guestObj.getPrefType());
						cStmt.setInt(15, guestObj.getOptin());
						cStmt.setString(16, guestObj.getNote());
						cStmt.setString(17, seatingPref);
						

						cStmt.registerOutParameter(18, Types.INTEGER);
						cStmt.registerOutParameter(19, Types.INTEGER);
						cStmt.registerOutParameter(20, Types.INTEGER);
						cStmt.registerOutParameter(21, Types.VARCHAR);
						cStmt.registerOutParameter(22, Types.INTEGER);
						cStmt.registerOutParameter(23, Types.VARCHAR);
						cStmt.registerOutParameter(24, Types.INTEGER);
						cStmt.registerOutParameter(25, Types.VARCHAR);

						cStmt.execute();

						waitlistMetrics.setGuestId(cStmt.getInt(18));
						waitlistMetrics.setGuestRank(cStmt.getInt(19));			
						waitlistMetrics.setGuestToBeNotified(cStmt.getInt(24));
						waitlistMetrics.setNowServingGuest(cStmt.getInt(20));					
						waitlistMetrics.setTotalWaitingGuest(cStmt.getInt(21));	
						waitlistMetrics.setTotalWaitTime(cStmt.getInt(22));
						waitlistMetrics.setClientBase(cStmt.getString(25));
					
						
						

					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return waitlistMetrics;
				}
			});

		} catch (Exception e) {
			LoggerUtil.logError("Error in proc " + e.getMessage());
		}
		finally {
			session.close();	
		}
		return waitlistMetrics;	
	}

	@Override
	public WaitlistMetrics updateGuestDetails(GuestDTO guestObj, String seatingPref) {

		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
		WaitlistMetrics waitlistMetrics = null;
		Session session = sessionFactory.openSession();
		try {

			waitlistMetrics = session.doReturningWork(new ReturningWork<WaitlistMetrics>() {

				@Override
				public WaitlistMetrics execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call UPDATEGUESTREVAMP(?, ?, ?, ?, "
							+ "?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?, ?, ? , ? )}");

					WaitlistMetrics waitlistMetrics = new WaitlistMetrics();
					try {
						cStmt.setLong(1, guestObj.getOrganizationID());
						cStmt.setLong(2, guestObj.getGuestID());
						cStmt.setString(3, guestObj.getName());

						cStmt.setLong(4, guestObj.getNoOfChildren());
						cStmt.setLong(5, guestObj.getNoOfAdults());
						cStmt.setLong(6, guestObj.getNoOfChildren());

						cStmt.setLong(7, guestObj.getNoOfPeople());
						cStmt.setLong(8, guestObj.getLanguagePref().getLangId());
						cStmt.setInt(9, guestObj.getPartyType());
						cStmt.setString(10, guestObj.getDeviceType());
						cStmt.setString(11, guestObj.getDeviceId());
						cStmt.setString(12, guestObj.getContactNo());
						cStmt.setString(13, guestObj.getEmail());
						cStmt.setString(14, guestObj.getPrefType());
						cStmt.setInt(15, guestObj.getOptin());
						cStmt.setString(16, seatingPref);
						cStmt.setString(17, guestObj.getNote());

						cStmt.registerOutParameter(18, Types.INTEGER);
						cStmt.registerOutParameter(19, Types.INTEGER);
						cStmt.registerOutParameter(20, Types.INTEGER);
						cStmt.registerOutParameter(21, Types.VARCHAR);
						cStmt.registerOutParameter(22, Types.INTEGER);
						cStmt.registerOutParameter(23, Types.INTEGER);
						cStmt.registerOutParameter(24, Types.VARCHAR);

						cStmt.execute();
					
						waitlistMetrics.setGuestId(guestObj.getGuestID());	
						waitlistMetrics.setGuestToBeNotified(cStmt.getInt(22));
						waitlistMetrics.setNowServingGuest(cStmt.getInt(18));					
						waitlistMetrics.setTotalWaitingGuest(cStmt.getInt(19));	
						waitlistMetrics.setTotalWaitTime(cStmt.getInt(20));
						waitlistMetrics.setClientBase(cStmt.getString(24));

					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return waitlistMetrics;
				}
			});

		} catch (Exception e) {
			LoggerUtil.logError("Error in proc " + e.getMessage());
		} finally {

			session.close();
		}
		return waitlistMetrics;	
	}

	@Override
	public WaitlistMetrics updateGuestStatus(Integer guestId, Integer orgId, String status) {
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
		WaitlistMetrics waitlistMetrics = null;
		Session session = sessionFactory.openSession();
		try {

			waitlistMetrics = session.doReturningWork(new ReturningWork<WaitlistMetrics>() {

				@Override
				public WaitlistMetrics execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection
							.prepareCall("{call UPDATEGUESTSTATUSREVAMP(?, ?, ?, ?, " + "?, ?, ?, ? , ?, ?, ?, ?, ?)}");

					WaitlistMetrics waitlistMetrics = new WaitlistMetrics();
					try {
						cStmt.setLong(1, orgId);
						cStmt.setLong(2, guestId);

						cStmt.setInt(3, status.equalsIgnoreCase(WaitListServiceConstants.NOTPRESENT_STATUS) ? 1 : 0);
						cStmt.setInt(4, status.equalsIgnoreCase(WaitListServiceConstants.INCOMPLETE_STATUS) ? 1 : 0);
						cStmt.setInt(5, status.equalsIgnoreCase(WaitListServiceConstants.SEATED_STATUS) ? 1 : 0);
						cStmt.setInt(6, status.equalsIgnoreCase(WaitListServiceConstants.DELETE_STATUS) ? 1 : 0);

						cStmt.registerOutParameter(7, Types.INTEGER);
						cStmt.registerOutParameter(8, Types.INTEGER);
						cStmt.registerOutParameter(9, Types.INTEGER);
						cStmt.registerOutParameter(10, Types.VARCHAR);
						cStmt.registerOutParameter(11, Types.INTEGER);
						cStmt.registerOutParameter(12, Types.INTEGER);
						cStmt.registerOutParameter(13, Types.VARCHAR);

						cStmt.execute();

						waitlistMetrics.setGuestToBeNotified(cStmt.getInt(11));
						waitlistMetrics.setNowServingGuest(cStmt.getInt(7));
						waitlistMetrics.setTotalWaitingGuest(cStmt.getInt(8));
						waitlistMetrics.setTotalWaitTime(cStmt.getInt(9));
						waitlistMetrics.setClientBase(cStmt.getString(13));

					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return waitlistMetrics;
				}
			});

		} catch (Exception e) {
			LoggerUtil.logError("Error in proc " + e.getMessage());
		} finally {

			session.close();
		}
		return waitlistMetrics;
	}

	@Override
	public List<Guest> fetchGuestByContact(Integer orgID, String contactNumber) {

		@SuppressWarnings({ "unchecked"} )
		List<Guest> guestDTO = entityManager.createNativeQuery(
				"select * from (select gr.GuestID, gr.OrganizationID, gr.Name,gr.Uuid,gr.NoOfAdults,gr.NoOfChildren,gr.NoOfInfants,gr.NoOfPeople,0 as QuoteTime,0 as PartyType,gr.DeviceType,gr.DeviceId,gr.ContactNo,gr.Email,gr.PrefType,gr.LanguagePrefID,gr.Optin,gr.Rank,gr.Status,gr.SeatingPreference,gr.RecvLeveltwo,gr.CalloutCount,gr.Note,gr.IncompleteParty,gr.ResetTime,gr.CheckinTime,gr.SeatedTime,gr.CreatedBy,gr.CreatedAt,gr.ModifiedBy,gr.ModifiedAt from GUESTRESET gr left join GUEST g on g.GuestID = gr.GuestID where gr.OrganizationID=:orgID and gr.ContactNo=:contactNumber union"
						+ " select g.GuestID, g.OrganizationID, g.Name,g.Uuid,g.NoOfAdults,g.NoOfChildren,g.NoOfInfants,g.NoOfPeople,g.QuoteTime,g.PartyType,g.DeviceType,g.DeviceId,g.ContactNo,g.Email,g.PrefType,g.LanguagePrefID,g.Optin,g.Rank,g.Status,g.SeatingPreference,g.RecvLeveltwo,g.CalloutCount,g.Note,g.IncompleteParty,g.ResetTime,g.CheckinTime,g.SeatedTime,g.CreatedBy,g.CreatedAt,g.ModifiedBy,g.ModifiedAt from GUEST g where g.OrganizationID=:orgID and g.ContactNo=:contactNumber) as u order by u.CreatedAt desc",Guest.class)
				.setParameter("orgID", orgID).setParameter("contactNumber", contactNumber).getResultList();
//unwrap(org.hibernate.query.NativeQuery.class ).setResultTransformer(Transformers.aliasToBean( GuestDetailsDTO.class))
		return guestDTO;
	}
	
	

	@Override
	public Integer fetchAllGuestListCount(Integer orgId, String searchText) {
		// for fetching data according to page number

		   BigInteger count  = BigInteger. valueOf(0);

				try {
					StringBuilder query = new StringBuilder(
							"SELECT count(*) FROM GUEST g join LANGMASTER l on l.langID=g.LanguagePrefID WHERE g.status ='CHECKIN' and g.ResetTime is null and g.OrganizationID=:orgId");

					if ((searchText != null) && (!searchText.equalsIgnoreCase("null"))) {
						query = query.append(" and (g.Name like :searchText  or g.ContactNo like :searchText)");
					}
					
					if ((searchText != null) && (!searchText.equalsIgnoreCase("null"))) {
						count = (BigInteger) entityManager.createNativeQuery(query.toString()).setParameter("orgId", orgId)
								.setParameter("searchText", "%" + searchText + "%").getSingleResult();

					} else {
						count = (BigInteger) entityManager.createNativeQuery(query.toString()).setParameter("orgId", orgId).getSingleResult();

					}
				} catch (Exception e) {
					LoggerUtil.logError("Error in fetch guest  " + e.getMessage());
				}
				return count.intValue();
	}

	@Override
	public Integer fetchAllGuestHistoryListCount(Integer orgId, String searchText, String clientTimezone,
			Integer sliderMaxTime, Integer sliderMinTime, String statusOption) {
		// for fetching history data count  

		 BigInteger count  = BigInteger. valueOf(0);

				try {

					String sliderMinTimeString = sliderMinTime + ":00";
					String sliderMaxTimeString = sliderMaxTime + ":01";
					StringBuilder query = new StringBuilder(
							"SELECT count(*) FROM GUEST g WHERE g.ResetTime is  null and g.Status not in ('CHECKIN')"
									+ " and g.OrganizationID=:orgId and ((time(convert_tz(g.CheckinTime,'-05:00', :clientTimezone)) between time(:sliderMinValue) and time(:sliderMaxValue)))");
					if (statusOption.equals("Not Present")) {
						query = query.append(" and CalloutCount > 0");
					}
					if (statusOption.equals("Incomplete")) {
						query = query.append(" and IncompleteParty > 0");
					}
					if ((searchText != null) && (!searchText.equalsIgnoreCase("null"))) {
						query = query.append(" and (g.Name like :searchText  or g.ContactNo like :searchText)");
					}
					//query = query.append(" order by g.Rank asc limit :pageSize OFFSET :startIndex");
					if ((searchText != null) && (!searchText.equalsIgnoreCase("null"))) {
						count = (BigInteger) entityManager.createNativeQuery(query.toString()).setParameter("orgId", orgId)
								.setParameter("sliderMinValue", sliderMinTimeString)
								.setParameter("sliderMaxValue", sliderMaxTimeString)
								.setParameter("clientTimezone", clientTimezone)
								.setParameter("searchText", "%" + searchText + "%").getSingleResult();

					} else {
						count = (BigInteger) entityManager.createNativeQuery(query.toString()).setParameter("orgId", orgId)
								.setParameter("sliderMinValue", sliderMinTimeString)
								.setParameter("sliderMaxValue", sliderMaxTimeString)
								.setParameter("clientTimezone", clientTimezone).getSingleResult();

					}
				} catch (Exception e) {
					LoggerUtil.logError("Error in fetch history  " + e.getMessage());
				}
				return count.intValue();
	}
	
	@Override
	public GuestMetricsDTO getGuestMetrics(Integer guestId, Integer orgId) {

		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
		GuestMetricsDTO guestMetrics = null;
		Session session = sessionFactory.openSession();
		try {

			guestMetrics = session.doReturningWork(new ReturningWork<GuestMetricsDTO>() {

				@Override
				public GuestMetricsDTO execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call FETCHGUESTMETRICS(?, ?, ?, ?,?)}");

					GuestMetricsDTO metricsDTO = new GuestMetricsDTO();
					try {
						cStmt.setInt(1, orgId);
						cStmt.setInt(2, guestId);
						cStmt.registerOutParameter(3, Types.INTEGER);
						cStmt.registerOutParameter(4, Types.INTEGER);
						cStmt.registerOutParameter(5, Types.INTEGER);

						cStmt.execute();

						metricsDTO.setGuestRank(cStmt.getInt(5));
						metricsDTO.setTotalWaitTime(cStmt.getInt(4) <= 0 ? cStmt.getInt(3)
								: (cStmt.getInt(4) * cStmt.getInt(3)) + cStmt.getInt(3));
						metricsDTO.setOrgWaitTime(cStmt.getInt(3));
						metricsDTO.setGuestAheadCount(cStmt.getInt(4) <= 0 ? 0 : cStmt.getInt(4));

					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return metricsDTO;
				}
			});

		} catch (Exception e) {
			LoggerUtil.logError("Error in proc " + e.getMessage());
		} finally {

			session.close();
		}
		return guestMetrics;
	}

}
