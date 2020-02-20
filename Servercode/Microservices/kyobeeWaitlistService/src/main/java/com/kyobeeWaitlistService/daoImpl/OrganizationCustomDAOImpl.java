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
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.kyobeeWaitlistService.dao.OrganizationCustomDAO;
import com.kyobeeWaitlistService.dto.GuestDetailsDTO;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.SmsDetailsDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.util.LoggerUtil;

@Repository
public class OrganizationCustomDAOImpl implements OrganizationCustomDAO {

	@Autowired
	private EntityManager entityManager;

	@Override
	public OrganizationMetricsDTO getOrganizationMetrics(Integer orgId) {

		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);

		OrganizationMetricsDTO organizationMetricsDTO = null;
		Session session = sessionFactory.openSession();
		try {

			organizationMetricsDTO = session.doReturningWork(new ReturningWork<OrganizationMetricsDTO>() {

				@Override
				public OrganizationMetricsDTO execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection
							.prepareCall("{call CALCHEADERMETRICS(?, ?, ?, ?, " + "?, ?, ?, ? , ?, ?)}");

					OrganizationMetricsDTO organizationMetricsDTO = new OrganizationMetricsDTO();
					try {
						cStmt.setInt(1, orgId);

						cStmt.registerOutParameter(2, Types.INTEGER);
						cStmt.registerOutParameter(3, Types.INTEGER);
						cStmt.registerOutParameter(4, Types.INTEGER);
						cStmt.registerOutParameter(5, Types.INTEGER);
						cStmt.registerOutParameter(6, Types.VARCHAR);
						cStmt.registerOutParameter(7, Types.INTEGER);
						cStmt.registerOutParameter(8, Types.INTEGER);
						cStmt.registerOutParameter(9, Types.INTEGER);
						cStmt.registerOutParameter(10, Types.VARCHAR);

						cStmt.execute();

						organizationMetricsDTO.setGuestMinRank(cStmt.getInt(2));
						organizationMetricsDTO.setGuestNotifiedWaitTime(cStmt.getInt(7));
						organizationMetricsDTO.setGuestToBeNotified(cStmt.getString(6));
						organizationMetricsDTO.setNotifyUserCount(cStmt.getInt(9));
						organizationMetricsDTO.setOrgGuestCount(cStmt.getInt(3));
						organizationMetricsDTO.setOrgTotalWaitTime(cStmt.getInt(4));
						organizationMetricsDTO.setPerPartyWaitTime(cStmt.getInt(8));
						organizationMetricsDTO.setClientBase(cStmt.getString(10));

					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return organizationMetricsDTO;
				}
			});

		} catch (Exception e) {
			LoggerUtil.logError("Error in getOrganizationMetrics proc " + e.getMessage());
		} finally {
			
			session.close();
		}
		return organizationMetricsDTO;

	}

	@Override
	public WaitlistMetrics updateOrgSettings(Integer orgId, Integer perPartyWaitTime, Integer numberOfUsers) {
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);

		WaitlistMetrics waitlistMetrics = null;
		Session session = sessionFactory.openSession();
		try {

			waitlistMetrics = session.doReturningWork(new ReturningWork<WaitlistMetrics>() {

				@Override
				public WaitlistMetrics execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection
							.prepareCall("{call UPDATEHEADERMETRICS(?, ?, ?, ?, " + "?, ?, ?)}");

					WaitlistMetrics waitlistMetrics = new WaitlistMetrics();
					try {
						cStmt.setInt(1, orgId);
						cStmt.setInt(2, perPartyWaitTime);
						cStmt.setInt(3, numberOfUsers);

						cStmt.registerOutParameter(4, Types.INTEGER);
						cStmt.registerOutParameter(5, Types.INTEGER);
						cStmt.registerOutParameter(6, Types.INTEGER);
						cStmt.registerOutParameter(7, Types.VARCHAR);

						cStmt.execute();

						waitlistMetrics.setNowServingGuest(cStmt.getInt(4));
						waitlistMetrics.setTotalWaitingGuest(cStmt.getInt(5));
						waitlistMetrics.setTotalWaitTime(cStmt.getInt(6));
						waitlistMetrics.setClientBase(cStmt.getString(7));
						

					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
					return waitlistMetrics;
				}
			});
		} catch (Exception e) {
			LoggerUtil.logError("Error in updateOrgSettings proc " + e.getMessage());
		}finally {
			
			session.close();
		}
		return waitlistMetrics;
	}

	@Override
	public void resetOrganizationByOrgId(Long orgId) {
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);

		Session session = sessionFactory.openSession();
		try {

			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection
							.prepareCall("{call RESETGUESTBYORGID(?)}");
					try {
						cStmt.setLong(1, orgId);
						cStmt.execute();
				
					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
				}
			});

		} catch (Exception e) {
			LoggerUtil.logError("Error in getOrganizationMetrics proc " + e.getMessage());
		} finally {
			
			session.close();
		}
		
	}

}
