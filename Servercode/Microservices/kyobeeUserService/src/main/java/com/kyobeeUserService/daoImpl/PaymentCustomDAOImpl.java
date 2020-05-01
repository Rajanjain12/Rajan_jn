package com.kyobeeUserService.daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.dao.PaymentCustomDAO;
import com.kyobeeUserService.dto.UpdatePaymentDetailsDTO;
import com.kyobeeUserService.util.LoggerUtil;

@Repository
@Transactional
public class PaymentCustomDAOImpl implements PaymentCustomDAO {

	@Autowired
	private EntityManager entityManager;

	@Override
	public void updatePaymentDetailsOnSuccess(UpdatePaymentDetailsDTO updatePaymentDetailsDTO) {
	
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);

		Session session = sessionFactory.openSession();
		try {

			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection
							.prepareCall("{call TRANSACTIONSUCCESS(?,?,?,?,?,?,?,?,?,?,?)}");
					try {
						cStmt.setInt(1, updatePaymentDetailsDTO.getOrganizationSubscriptionID());
						cStmt.setInt(2, updatePaymentDetailsDTO.getOrganizationCardDetailID());
						cStmt.setInt(3, updatePaymentDetailsDTO.getOrganizationPaymentID());
						cStmt.setString(4, updatePaymentDetailsDTO.getInvoiceStatus());
						cStmt.setString(5, updatePaymentDetailsDTO.getCurrentActiveSubscription());
						cStmt.setString(6, updatePaymentDetailsDTO.getSubscriptionStatus());
						cStmt.setString(7, updatePaymentDetailsDTO.getVaultId());
						cStmt.setString(8, updatePaymentDetailsDTO.getTransactionId());
						cStmt.setTimestamp(9, updatePaymentDetailsDTO.getPaymentDateTime());
						cStmt.setString(10, updatePaymentDetailsDTO.getPayemntStatusReason());
						cStmt.setString(11, updatePaymentDetailsDTO.getPaymentStatus());

						cStmt.execute();

					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
				}
			});

		} catch (Exception e) {
			LoggerUtil.logError("Error in TRANSACTIONSUCCESS Proc" + e.getMessage());
		} finally {

			session.close();
		}

	}

	@Override
	public void updatePaymentDetailsOnFailure(UpdatePaymentDetailsDTO updatePaymentDetailsDTO) {
		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);

		Session session = sessionFactory.openSession();
		try {

			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					CallableStatement cStmt = connection.prepareCall("{call TRANSACTIONFAILURE(?,?,?,?,?)}");
					try {
						cStmt.setInt(1, updatePaymentDetailsDTO.getOrganizationSubscriptionID());
						cStmt.setInt(2, updatePaymentDetailsDTO.getOrganizationPaymentID());
						cStmt.setString(3, updatePaymentDetailsDTO.getInvoiceStatus());
						cStmt.setString(4, updatePaymentDetailsDTO.getSubscriptionStatus());
						cStmt.setString(5, updatePaymentDetailsDTO.getPaymentStatus());

						cStmt.execute();

					} finally {
						if (cStmt != null) {
							cStmt.close();
						}
					}
				}
			});

		} catch (Exception e) {
			LoggerUtil.logError("Error in TRANSACTIONFAILURE Proc" + e.getMessage());
		} finally {

			session.close();

		}

	}

}
