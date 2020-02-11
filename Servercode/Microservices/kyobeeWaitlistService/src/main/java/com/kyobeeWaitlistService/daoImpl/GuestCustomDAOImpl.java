package com.kyobeeWaitlistService.daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;


import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobeeWaitlistService.dao.GuestCustomDAO;
import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestDetailsDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Guest;

import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@Repository
public class GuestCustomDAOImpl implements GuestCustomDAO{

	@Autowired
	private EntityManager entityManager;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Guest> fetchAllGuestHistoryList(Integer orgId,Integer pageSize,Integer pageNo,String searchText,String clientTimezone,Integer sliderMaxTime,Integer sliderMinTime,String statusOption) {
		//for fetching data according to page number 
		
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
		 
		
	
		
		int firstPage = (pageNo == 1) ? 0 : (pageSize*(pageNo-1));
		String sliderMinTimeString = sliderMinTime+":00";
			String sliderMaxTimeString = sliderMaxTime+":01";
			StringBuilder query=new StringBuilder("FROM Guest g left join fetch g.langmaster WHERE g.resetTime is  null and g.status not in ('CHECKIN')"
					+ " and g.organizationID=:orgId and ((time(convert_tz(g.checkinTime,'-05:00', :clientTimezone)) between time(:sliderMinValue) and time(:sliderMaxValue)))");
			if(statusOption.equals("Not Present")) {
				query=query.append(" and calloutCount > 0");
			}
			if(statusOption.equals("Incomplete")) {
				query=query.append(" and incompleteParty > 0");
			}
			if(searchText != null) {
				query=query.append(" and (g.name like :searchText  or g.sms like :searchText)");
			}
			query=query.append(" order by g.rank asc");	
			if(searchText!=null) {
				return sessionFactory.getCurrentSession().createQuery(query.toString()).setParameter("orgId",orgId).setParameter("sliderMinValue", sliderMinTimeString).setParameter("sliderMaxValue", sliderMaxTimeString).setParameter("clientTimezone", clientTimezone).setParameter("searchText", "%"+searchText+"%").setFirstResult(firstPage).setMaxResults(pageSize).getResultList();	
			}else {
				return sessionFactory.getCurrentSession().createQuery(query.toString()).setParameter("orgId",orgId).setParameter("sliderMinValue", sliderMinTimeString).setParameter("sliderMaxValue", sliderMaxTimeString).setParameter("clientTimezone",clientTimezone).setFirstResult(firstPage).setMaxResults(pageSize).getResultList();
			}
	 }

	
	@Override
	public AddUpdateGuestDTO addGuest(GuestDTO guestObj,String seatingPref,String marketingPref) {
		
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
	         
		//SessionFactory sessionFactory=getSessionFactory();
		AddUpdateGuestDTO addUpdateGuestDTO = null;
		try {
			
			addUpdateGuestDTO = sessionFactory.getCurrentSession().doReturningWork(new ReturningWork<AddUpdateGuestDTO>() {

				@Override
				public AddUpdateGuestDTO execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call ADDGUESTLATEST(?, ?, ?, ?, "
							+ "?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ?, ?, ?)}");
					
					AddUpdateGuestDTO addUpdateGuestDTO = new AddUpdateGuestDTO();
					try {
						cStmt.setLong(1, guestObj.getOrganizationID());
						cStmt.setString(2, guestObj.getName());
						cStmt.setString(3, guestObj.getUuid());
						cStmt.setLong(4, guestObj.getNoOfChildren());
						cStmt.setLong(5, guestObj.getNoOfChildren());
						cStmt.setLong(6, guestObj.getNoOfChildren());
						
						cStmt.setLong(7, guestObj.getNoOfPeople());
						cStmt.setLong(8, guestObj.getLangguagePref().getLangId());
						cStmt.setInt(9, guestObj.getPartyType());
						cStmt.setString(10, guestObj.getDeviceType());
						cStmt.setString(11, guestObj.getDeviceId());
						cStmt.setString(12, guestObj.getSms());
						cStmt.setString(13, guestObj.getEmail());
						cStmt.setString(14, guestObj.getPrefType());
						cStmt.setInt(15, guestObj.getOptin());
						cStmt.setString(16, guestObj.getNote());
						cStmt.setString(17,seatingPref);
						cStmt.setString(18, marketingPref);

						cStmt.registerOutParameter(19, Types.INTEGER);
						cStmt.registerOutParameter(20, Types.INTEGER);
						cStmt.registerOutParameter(21, Types.INTEGER);
						cStmt.registerOutParameter(22, Types.VARCHAR);
						cStmt.registerOutParameter(23, Types.INTEGER);
						cStmt.registerOutParameter(24, Types.VARCHAR);
						cStmt.registerOutParameter(25, Types.INTEGER);
						cStmt.registerOutParameter(26, Types.VARCHAR);
						
						cStmt.execute();
					
						addUpdateGuestDTO.setAddedGuestId(cStmt.getInt(19));
						addUpdateGuestDTO.setGuestRank(cStmt.getInt(20));			
						addUpdateGuestDTO.setNextToNotifyGuestId(cStmt.getInt(25));
						addUpdateGuestDTO.setNowServingGuestId(cStmt.getInt(21));					
						addUpdateGuestDTO.setTotalGuestWaiting(cStmt.getInt(22));	
						addUpdateGuestDTO.setTotalWaitTime(cStmt.getInt(23));
						addUpdateGuestDTO.setClientBase(cStmt.getString(26));
					
					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return addUpdateGuestDTO;
				}
			});

				
		}
		catch(Exception e) {
				LoggerUtil.logError("Error in proc "+e.getMessage());
		}
		finally {
			//session.flush();
			//session.close();	
		}
		return addUpdateGuestDTO;	
	}

	@Override
	public AddUpdateGuestDTO updateGuestDetails(GuestDTO guestObj, String seatingPref, String marketingPref) {
		
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
		AddUpdateGuestDTO addUpdateGuestDTO = null;
		Session session= sessionFactory.openSession();
		try {
			
			addUpdateGuestDTO = session.doReturningWork(new ReturningWork<AddUpdateGuestDTO>() {

				@Override
				public AddUpdateGuestDTO execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call UPDATEGUESTREVAMP(?, ?, ?, ?, "
							+ "?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ? , ?, ? , ? , ? , ?, ?, ? , ? )}");
					
					AddUpdateGuestDTO addUpdateGuestDTO = new AddUpdateGuestDTO();
					try {
						cStmt.setLong(1, guestObj.getOrganizationID());
						cStmt.setLong(2, guestObj.getGuestID());
						cStmt.setString(3, guestObj.getName());
						
						cStmt.setLong(4, guestObj.getNoOfChildren());
						cStmt.setLong(5, guestObj.getNoOfChildren());
						cStmt.setLong(6, guestObj.getNoOfChildren());
						
						cStmt.setLong(7, guestObj.getNoOfPeople());
						cStmt.setLong(8, guestObj.getLangguagePref().getLangId());
						cStmt.setInt(9, guestObj.getPartyType());
						cStmt.setString(10, guestObj.getDeviceType());
						cStmt.setString(11, guestObj.getDeviceId());
						cStmt.setString(12, guestObj.getSms());
						cStmt.setString(13, guestObj.getEmail());
						cStmt.setString(14, guestObj.getPrefType());
						cStmt.setInt(15, guestObj.getOptin());
						cStmt.setString(16,seatingPref);
						cStmt.setString(17, guestObj.getNote());					

						cStmt.registerOutParameter(18, Types.INTEGER);
						cStmt.registerOutParameter(19, Types.INTEGER);
						cStmt.registerOutParameter(20, Types.INTEGER);
						cStmt.registerOutParameter(21, Types.VARCHAR);
						cStmt.registerOutParameter(22, Types.INTEGER);
						cStmt.registerOutParameter(23, Types.INTEGER);
						cStmt.registerOutParameter(24, Types.VARCHAR);
						
						cStmt.execute();
					
						addUpdateGuestDTO.setAddedGuestId(guestObj.getGuestID());	
						addUpdateGuestDTO.setNextToNotifyGuestId(cStmt.getInt(22));
						addUpdateGuestDTO.setNowServingGuestId(cStmt.getInt(18));					
						addUpdateGuestDTO.setTotalGuestWaiting(cStmt.getInt(19));	
						addUpdateGuestDTO.setTotalWaitTime(cStmt.getInt(20));
						addUpdateGuestDTO.setClientBase(cStmt.getString(24));
					
					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return addUpdateGuestDTO;
				}
			});

				
		}
		catch(Exception e) {
				LoggerUtil.logError("Error in proc "+e.getMessage());
		}
		finally {
			
			session.close();
		}
		return addUpdateGuestDTO;	
	}


	@Override
	public WaitlistMetrics updateGuestStatus(Integer guestId, Integer orgId, String status) {
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
		WaitlistMetrics waitlistMetrics = null;
		Session session= sessionFactory.openSession();
		try {
			
			waitlistMetrics = session.doReturningWork(new ReturningWork<WaitlistMetrics>() {

				@Override
				public WaitlistMetrics execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call UPDATEGUESTSTATUSREVAMP(?, ?, ?, ?, "
							+ "?, ?, ?, ? , ?, ?, ?, ?, ?)}");
					
					WaitlistMetrics waitlistMetrics = new WaitlistMetrics();
					try {
						cStmt.setLong(1, orgId);
						cStmt.setLong(2, guestId);
					
						cStmt.setInt(3, status.equalsIgnoreCase(WaitListServiceConstants.NOTPRESENT_STATUS) ? 1: 0);
						cStmt.setInt(4, status.equalsIgnoreCase(WaitListServiceConstants.INCOMPLETE_STATUS) ? 1: 0);
						cStmt.setInt(5, status.equalsIgnoreCase(WaitListServiceConstants.SEATED_STATUS) ? 1: 0);
						cStmt.setInt(6, status.equalsIgnoreCase(WaitListServiceConstants.DELETE_STATUS) ? 1: 0);
		
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

				
		}
		catch(Exception e) {
				LoggerUtil.logError("Error in proc "+e.getMessage());
		}
		finally {
			
			session.close();
		}
		return waitlistMetrics;	
	}

@Override
	public List<GuestDetailsDTO> fetchGuestByContact(Integer orgID, String contactNumber) {
		
		@SuppressWarnings({ "deprecation", "unchecked" })
		List<GuestDetailsDTO> guestDTO  = entityManager.createNativeQuery("select * from (select gr.GuestID, gr.organizationID, gr.name,gr.note,gr.uuid,gr.noOfPeople,gr.email,gr.sms,gr.status,gr.rank,gr.prefType,gr.optin,gr.calloutCount,gr.checkinTime,gr.seatedTime,gr.createdTime,gr.updatedTime,gr.incompleteParty,gr.seatingPreference,gr.marketingPreference,gr.deviceType,gr.deviceId,gr.languagePrefID from GUESTRESET gr left join GUEST g on g.guestID = gr.GuestID where gr.organizationID=:orgID and gr.sms=:contactNumber union" + 
" select g.guestID, g.organizationID, g.name,g.note,g.uuid,g.noOfPeople,g.email,g.sms,g.status,g.rank,g.prefType,g.optin,g.calloutCount,g.checkinTime,g.seatedTime,g.createdTime,g.updatedTime,g.incompleteParty,g.seatingPreference,g.marketingPreference,g.deviceType,g.deviceId,g.languagePrefID from GUEST g where g.organizationID=:orgID and g.sms=:contactNumber) as u order by u.createdTime desc").setParameter("orgID", orgID).setParameter("contactNumber", contactNumber).unwrap(org.hibernate.query.NativeQuery.class ).setResultTransformer(Transformers.aliasToBean( GuestDetailsDTO.class)).getResultList();
				
		return guestDTO;
	}


}
