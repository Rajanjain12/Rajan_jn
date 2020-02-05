package com.kyobeeWaitlistService.daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobeeWaitlistService.dao.GuestCustomDAO;
import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestHistoryRequestDTO;
import com.kyobeeWaitlistService.dto.GuestRequestDTO;
import com.kyobeeWaitlistService.dto.SeatingMarketingPrefDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Guest;
import com.kyobeeWaitlistService.util.LoggerUtil;

@Repository
public class GuestCustomDAOImpl implements GuestCustomDAO{

	/*
	 * @Autowired private SessionFactory sessionFactory;
	 */
	
	  @PersistenceContext 
	  private EntityManager entityManger;
	 
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Guest> fetchAllGuestHistoryList(GuestHistoryRequestDTO guestRequestDTO) {
		//for fetching data according to page number 
		Integer pageNumber=guestRequestDTO.getPageNo();
		Integer sliderMinValue=guestRequestDTO.getSliderMinTime();
		Integer sliderMaxValue=guestRequestDTO.getSliderMaxTime();
		String statusOption=guestRequestDTO.getStatusOption();
		
		int firstPage = (pageNumber == 1) ? 0 : (guestRequestDTO.getPageSize()*(pageNumber-1));
		String sliderMinTimeString = sliderMinValue+":00";
			String sliderMaxTimeString = sliderMaxValue+":01";
			StringBuilder query=new StringBuilder("FROM Guest g left join fetch g.langmaster WHERE g.resetTime is  null and g.status not in ('CHECKIN')"
					+ " and g.organizationID=:orgId and ((time(convert_tz(g.checkinTime,'-05:00', :clientTimezone)) between time(:sliderMinValue) and time(:sliderMaxValue)))");
			if(statusOption.equals("Not Present")) {
				query=query.append(" and calloutCount > 0");
			}
			if(statusOption.equals("Incomplete")) {
				query=query.append(" and incompleteParty > 0");
			}
			if(guestRequestDTO.getSearchText() != null) {
				query=query.append(" and (g.name like :searchText  or g.sms like :searchText)");
			}
			query=query.append(" order by g.rank asc");	
			if(guestRequestDTO.getSearchText()!=null) {
				return entityManger.createQuery(query.toString()).setParameter("orgId",guestRequestDTO.getOrgId()).setParameter("sliderMinValue", sliderMinTimeString).setParameter("sliderMaxValue", sliderMaxTimeString).setParameter("clientTimezone", guestRequestDTO.getClientTimezone()).setParameter("searchText", "%"+guestRequestDTO.getSearchText()+"%").setFirstResult(firstPage).setMaxResults(guestRequestDTO.getPageSize()).getResultList();	
			}else {
				return entityManger.createQuery(query.toString()).setParameter("orgId",guestRequestDTO.getOrgId()).setParameter("sliderMinValue", sliderMinTimeString).setParameter("sliderMaxValue", sliderMaxTimeString).setParameter("clientTimezone", guestRequestDTO.getClientTimezone()).setFirstResult(firstPage).setMaxResults(guestRequestDTO.getPageSize()).getResultList();
			}
	 }

	public Session getSessionFactory(){
		 return entityManger.unwrap(Session.class);
	
	}

	@Override
	public AddUpdateGuestDTO addGuest(GuestDTO guestObj,String seatingPref,String marketingPref) {
		Session session=getSessionFactory();
		SessionFactory sessionFactory=session.getSessionFactory();
		AddUpdateGuestDTO addUpdateGuestDTO = null;
		try {
			
			addUpdateGuestDTO = sessionFactory.openSession().doReturningWork(new ReturningWork<AddUpdateGuestDTO>() {

				@Override
				public AddUpdateGuestDTO execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call ADDGUESTLATEST(?, ?, ?, ?, "
							+ "?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ?, ?, ?)}");
					
					AddUpdateGuestDTO addUpdateGuestDTO = new AddUpdateGuestDTO();
					try {
						cStmt.setLong(1, guestObj.getOrganizationID());
						cStmt.setString(2, guestObj.getName());
						cStmt.setString(3, guestObj.getUuid());
						cStmt.setLong(4, guestObj.getNoOfPeople());
						cStmt.setLong(5, guestObj.getLangguagePref().getLangId());
						cStmt.setInt(6, guestObj.getPartyType());
						cStmt.setString(7, guestObj.getDeviceType());
						cStmt.setString(8, guestObj.getDeviceId());
						cStmt.setString(9, guestObj.getPhoneNumber());
						cStmt.setString(10, guestObj.getEmail());
						cStmt.setString(11, guestObj.getPrefType());
						cStmt.setInt(12, guestObj.getOptin());
						cStmt.setString(13, guestObj.getNote());
						cStmt.setString(14,seatingPref);
						cStmt.setString(15, marketingPref);

						cStmt.registerOutParameter(16, Types.INTEGER);
						cStmt.registerOutParameter(17, Types.INTEGER);
						cStmt.registerOutParameter(18, Types.INTEGER);
						cStmt.registerOutParameter(19, Types.VARCHAR);
						cStmt.registerOutParameter(20, Types.INTEGER);
						cStmt.registerOutParameter(21, Types.VARCHAR);
						cStmt.registerOutParameter(22, Types.INTEGER);
						cStmt.registerOutParameter(23, Types.VARCHAR);
						
						cStmt.execute();
					
						addUpdateGuestDTO.setAddedGuestId(cStmt.getInt(16));
						addUpdateGuestDTO.setGuestRank(cStmt.getInt(17));			
						addUpdateGuestDTO.setNextToNotifyGuestId(cStmt.getInt(22));
						addUpdateGuestDTO.setNowServingGuestId(cStmt.getInt(18));					
						addUpdateGuestDTO.setTotalGuestWaiting(cStmt.getInt(19));	
						addUpdateGuestDTO.setTotalWaitTime(cStmt.getInt(20));
						addUpdateGuestDTO.setClientBase(cStmt.getString(23));
					
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
			sessionFactory.close();
			session.close();	
		}
		return addUpdateGuestDTO;	
	}

}
