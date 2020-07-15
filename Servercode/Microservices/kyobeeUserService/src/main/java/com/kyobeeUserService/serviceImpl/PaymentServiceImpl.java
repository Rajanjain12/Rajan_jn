package com.kyobeeUserService.serviceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.itextpdf.text.DocumentException;
import com.kyobeeUserService.dao.CustomerDAO;
import com.kyobeeUserService.dao.OrganizationCardDetailDAO;
import com.kyobeeUserService.dao.OrganizationDAO;
import com.kyobeeUserService.dao.OrganizationPaymentDAO;
import com.kyobeeUserService.dao.OrganizationSubscriptionDAO;
import com.kyobeeUserService.dao.PaymentCustomDAO;
import com.kyobeeUserService.dao.PlanFeatureChargeDAO;
import com.kyobeeUserService.dao.PromotionalCodeDAO;
import com.kyobeeUserService.dto.DiscountDTO;
import com.kyobeeUserService.dto.InvoiceDTO;
import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.dto.OrgPaymentDTO;
import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.dto.UpdatePaymentDetailsDTO;
import com.kyobeeUserService.entity.Customer;
import com.kyobeeUserService.entity.Organization;
import com.kyobeeUserService.entity.OrganizationCardDetail;
import com.kyobeeUserService.entity.OrganizationPayment;
import com.kyobeeUserService.entity.PlanFeatureCharge;
import com.kyobeeUserService.entity.PromotionalCode;
import com.kyobeeUserService.service.PaymentService;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.PdfUtil;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.PromoCodeException;
import com.kyobeeUserService.util.Exception.TransactionFailureException;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private OrganizationCardDetailDAO orgCardDetailsDAO;

	@Autowired
	private OrganizationDAO organizationDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private BraintreeGateway gateway;

	@Autowired
	private OrganizationPaymentDAO orgPaymentDAO;

	@Autowired
	private PaymentCustomDAO paymentCustomDAO;

	@Autowired
	private PlanFeatureChargeDAO planFeatureChargeDAO;

	@Autowired
	private PdfUtil pdfUtil;

	@Autowired
	private OrganizationSubscriptionDAO orgSubscriptionDAO;

	@Autowired
	PromotionalCodeDAO promotionalCodeDAO;
	
	@Autowired
	RestTemplate restTemplate;
	

	@Override
	public Integer saveOrgCardDetails(OrgCardDetailsDTO orgCardDetailsDTO) {
		OrganizationCardDetail orgCardDetail = new OrganizationCardDetail();

		BeanUtils.copyProperties(orgCardDetailsDTO, orgCardDetail);

		Organization org = organizationDAO.getOne(orgCardDetailsDTO.getOrgId());
		Customer customer = customerDAO.getOne(orgCardDetailsDTO.getCustomerId());

		orgCardDetail.setOrganization(org);
		orgCardDetail.setCustomer(customer);
		orgCardDetail.setActive(UserServiceConstants.ACTIVE);
		orgCardDetail.setCreatedBy(UserServiceConstants.ADMIN);
		orgCardDetail.setCreatedAt(new Date());

		orgCardDetail = orgCardDetailsDAO.save(orgCardDetail);
		return orgCardDetail.getOrganizationCardDetailID();
	}

	@Override
	public void createTransaction(OrgPaymentDTO orgPaymentDTO) throws TransactionFailureException {

		OrganizationPayment org = null;

		// Checking if payment entry previously exists or not

		OrganizationPayment orgPayDetails = orgPaymentDAO.fetchOrgPaymentDetails(orgPaymentDTO.getOrgID());

		if (orgPayDetails == null
				|| (orgPayDetails != null && !orgPayDetails.getPaymentStatus().equals(UserServiceConstants.FAIL))) {
			OrganizationPayment orgPayment = new OrganizationPayment();

			orgPayment.setOrganization(organizationDAO.getOne(orgPaymentDTO.getOrgID()));
			orgPayment.setOrganizationcardDetail(orgCardDetailsDAO.getOne(orgPaymentDTO.getOrganizationCardDetailID()));
			orgPayment.setOrganizationSubscription(
					orgSubscriptionDAO.getOne(orgPaymentDTO.getOrganizationSubscriptionID()));
			orgPayment.setAmount(new BigDecimal(orgPaymentDTO.getAmount()));
			orgPayment.setActive(UserServiceConstants.INACTIVE);
			orgPayment.setCreatedBy(UserServiceConstants.ADMIN);
			orgPayment.setCreatedAt(new Date());

			org = orgPaymentDAO.save(orgPayment);
		} else if (orgPayDetails != null && orgPayDetails.getPaymentStatus().equals(UserServiceConstants.FAIL)) {
			org = orgPayDetails;
		}

		// Transaction request to braintree
		LoggerUtil.logInfo("Going to create transaction for payment");
		TransactionRequest request = new TransactionRequest().amount(new BigDecimal(orgPaymentDTO.getAmount()))
				.paymentMethodNonce(orgPaymentDTO.getPaymentNonce()).options().storeInVaultOnSuccess(true).done();

		Result<Transaction> result = gateway.transaction().sale(request);
		LoggerUtil.logInfo("Transaction result message:" + result.getMessage());

		if (result.isSuccess()) {
			Transaction transaction = result.getTarget();

			UpdatePaymentDetailsDTO updatePaymentDetailDTO = new UpdatePaymentDetailsDTO();
			BeanUtils.copyProperties(orgPaymentDTO, updatePaymentDetailDTO);
			updatePaymentDetailDTO.setOrganizationPaymentID(org.getOrganizationPaymentID());
			updatePaymentDetailDTO.setInvoiceStatus(UserServiceConstants.INVOICE_STATUS_BILLED);
			updatePaymentDetailDTO.setSubscriptionStatus(UserServiceConstants.SUBSCRIBED);
			updatePaymentDetailDTO.setCurrentActiveSubscription(UserServiceConstants.ACTIVE_SUBSC);
			updatePaymentDetailDTO.setVaultId(transaction.getCreditCard().getToken());
			updatePaymentDetailDTO.setTransactionId(transaction.getId());
			updatePaymentDetailDTO.setPaymentStatus(UserServiceConstants.SUCCESS);
			updatePaymentDetailDTO.setPaymentStatusReason(transaction.getStatus().toString());

			Timestamp stamp = new Timestamp(transaction.getCreatedAt().getTimeInMillis());
			updatePaymentDetailDTO.setPaymentDateTime(stamp);
			LoggerUtil.logInfo("customer id:" + transaction.getCustomer().getId());

			paymentCustomDAO.updatePaymentDetailsOnSuccess(updatePaymentDetailDTO);

			LoggerUtil.logInfo("Payment done successfully");
		} else {
			StringBuilder errorDetails = new StringBuilder();

			for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
				errorDetails.append("Error: " + error.getCode() + ": " + error.getMessage() + "\n");
			}

			LoggerUtil.logInfo("ERROR:" + errorDetails);
			UpdatePaymentDetailsDTO updatePaymentDetailDTO = new UpdatePaymentDetailsDTO();

			updatePaymentDetailDTO.setOrganizationSubscriptionID(orgPaymentDTO.getOrganizationSubscriptionID());
			updatePaymentDetailDTO.setOrganizationPaymentID(org.getOrganizationPaymentID());
			updatePaymentDetailDTO.setInvoiceStatus(UserServiceConstants.CANCELLED);
			updatePaymentDetailDTO.setSubscriptionStatus(UserServiceConstants.CANCELLED);
			updatePaymentDetailDTO.setPaymentStatus(UserServiceConstants.FAIL);
			updatePaymentDetailDTO.setPaymentStatusReason(errorDetails.toString());

			paymentCustomDAO.updatePaymentDetailsOnFailure(updatePaymentDetailDTO);

			throw new TransactionFailureException("Payment transaction failure");

		}
	}

	@Override
	public Integer generateInvoice(InvoiceDTO invoiceDTO) throws DocumentException, IOException, ParseException {
		List<PlanFeatureCharge> selectedPlanList = planFeatureChargeDAO
				.findByPlanFeatureChargeIDIn(invoiceDTO.getFeatureChargeIds());
		String invoiceFile = pdfUtil.generateInvoice(invoiceDTO.getOrgDTO(), selectedPlanList,
				invoiceDTO.getOrgSubscriptionId(),invoiceDTO.getDiscount());
		LoggerUtil.logInfo("File stored in aws s3 on path:" + invoiceFile);

		orgSubscriptionDAO.updateInvoiceDetails(UserServiceConstants.INVOICE_STATUS_BILLED, invoiceFile,
				invoiceDTO.getOrgSubscriptionId());

		return null;
	}

	@Override
	public BigDecimal calculateDiscount(DiscountDTO discountDTO) throws PromoCodeException {

		PromotionalCode promotionalCode = promotionalCodeDAO.findByPromoCode(discountDTO.getPromoCode());
		if (promotionalCode != null) {
			BigDecimal discPercentage = promotionalCode.getPercAmt();
			BigDecimal discAmount = discountDTO.getAmount().multiply(discPercentage).divide(new BigDecimal(100));
			BigDecimal finalAmount = discountDTO.getAmount().subtract(discAmount);
			LoggerUtil.logInfo("Final amount to be paid:" + finalAmount);
			orgSubscriptionDAO.updateBillingPrice(discountDTO.getInvoiceDTO().getOrgSubscriptionId(), discAmount, finalAmount);
			discountDTO.getInvoiceDTO().setDiscount(discAmount);
			restTemplate.postForObject(UserServiceConstants.INVOICE_API, discountDTO.getInvoiceDTO(), ResponseDTO.class);
			
			return finalAmount;
		} else {
			throw new PromoCodeException("Invalid PromoCode");
		}

	}

}
