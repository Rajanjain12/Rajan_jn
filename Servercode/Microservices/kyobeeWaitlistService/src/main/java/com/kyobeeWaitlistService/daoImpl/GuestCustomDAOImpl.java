package com.kyobeeWaitlistService.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyobeeWaitlistService.dao.GuestCustomDAO;
import com.kyobeeWaitlistService.entity.Guest;

@Repository
public class GuestCustomDAOImpl implements GuestCustomDAO{

	/*
	 * @Autowired private SessionFactory sessionFactory;
	 */
	
	@PersistenceContext
	 private EntityManager entityManger;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Guest> fetchAllGuestHistoryList(Integer orgId, Integer recordsPerPage, Integer pageNumber,String statusOption,
			String clientTimezone, Integer sliderMinValue, Integer sliderMaxValue, String searchText) {
	
		int firstPage = (pageNumber == 1) ? 0 : (recordsPerPage*(pageNumber-1));
		String sliderMinTimeString = sliderMinValue+":00";
			String sliderMaxTimeString = sliderMaxValue+":01";
			StringBuffer query=new StringBuffer("FROM Guest g left join fetch g.langmaster WHERE g.resetTime is  null and g.status not in ('CHECKIN')"
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
		
			return entityManger.createQuery(query.toString()).setParameter("orgId",orgId).setParameter("sliderMinValue", sliderMinTimeString).setParameter("sliderMaxValue", sliderMaxTimeString).setParameter("clientTimezone", clientTimezone).setParameter("searchText", "%"+searchText+"%").setFirstResult(firstPage).setMaxResults(recordsPerPage).getResultList();
		//return sessionFactory.getCurrentSession().createQuery(query.toString()).setParameter("orgId",orgId).setParameter("sliderMinValue", sliderMinTimeString).setParameter("sliderMaxValue", sliderMaxTimeString).setParameter("clientTimezone", clientTimezone).setParameter("searchText", "%"+searchText+"%").setFirstResult(firstPage).setMaxResults(recordsPerPage).list();
	}

}
