package com.kyobeeAccountService.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeAccountService.dao.CustomerDAO;
import com.kyobeeAccountService.dao.OrganizationDAO;
import com.kyobeeAccountService.dao.OrganizationSubscriptionDAO;
import com.kyobeeAccountService.dao.OrganizationSubscriptionDetailDAO;
import com.kyobeeAccountService.dao.PlanFeatureChargeDAO;
import com.kyobeeAccountService.dto.InvoiceDTO;
import com.kyobeeAccountService.dto.SubscribedPlanDetailsDTO;
import com.kyobeeAccountService.entity.Customer;
import com.kyobeeAccountService.entity.Organization;
import com.kyobeeAccountService.entity.OrganizationSubscription;
import com.kyobeeAccountService.entity.OrganizationSubscriptionDetail;
import com.kyobeeAccountService.entity.PlanFeatureCharge;
import com.kyobeeAccountService.service.PlanService;
import com.kyobeeAccountService.util.AccountServiceConstants;
import com.kyobeeAccountService.util.LoggerUtil;

@Service
public class PlanServiceImpl implements PlanService {
	@Autowired
	OrganizationSubscriptionDetailDAO orgSubscDetailDAO;

	@Autowired
	OrganizationSubscriptionDAO orgSubscDAO;

	@Autowired
	OrganizationDAO organizationDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	PlanFeatureChargeDAO planFeatureChargeDAO;

	@Autowired
	OrganizationSubscriptionDetailDAO orgSubscriptionDetailsDAO;

	@Override
	public List<InvoiceDTO> fetchInvoiceDetails(Integer orgId) {
		List<InvoiceDTO> invoiceDTOList = new ArrayList<>();
		InvoiceDTO invoiceDTO = null;

		// For fetching plan subscription details
		List<OrganizationSubscription> invoiceDetailsList = orgSubscDAO.fetchInvoiceDetails(orgId);
		// Getting subscription id from plan subscription list
		List<Integer> orgSubscIdsList = invoiceDetailsList.stream()
				.map(orgSubscDTO -> orgSubscDTO.getOrganizationSubscriptionID()).collect(Collectors.toList());
		LoggerUtil.logInfo("Org Subscription id list:" + orgSubscIdsList);
		// Fetching plan and invoice details for all subscribed plan
		List<OrganizationSubscriptionDetail> planSubscDetailsList = orgSubscDetailDAO
				.fetchPlanFeatureDetails(orgSubscIdsList);

		LoggerUtil.logInfo("Size of invoice details list:" + planSubscDetailsList.size());

		Map<OrganizationSubscription, List<OrganizationSubscriptionDetail>> groupBy = planSubscDetailsList.stream()
				.collect(Collectors.groupingBy(OrganizationSubscriptionDetail::getOrganizationSubscription));
		List<String> invoiceTitle = null;

		for (Map.Entry<OrganizationSubscription, List<OrganizationSubscriptionDetail>> entry : groupBy.entrySet()) {
			invoiceTitle = new ArrayList<>();
			invoiceDTO = new InvoiceDTO();
			invoiceDTO.setInvoiceID(entry.getKey().getOrganizationSubscriptionID());
			invoiceDTO.setInvoiceDate(entry.getKey().getSubscriptionDate());
			invoiceDTO.setAmount(entry.getKey().getTotalBillAmount());
			invoiceDTO.setInvoiceUrl(entry.getKey().getInvoiceFile());
			invoiceDTO.setStatus(entry.getKey().getActive());

			for (OrganizationSubscriptionDetail list : entry.getValue()) {
				invoiceTitle.add(list.getFeature().getFeatureName() + "-" + list.getPlan().getPlanName() + "("
						+ list.getPlanterm().getPlanTermName() + ")");
				invoiceDTO.setCurrency(list.getCurrency().getIcon());
			}
			invoiceDTO.setTitle(invoiceTitle);
			invoiceDTOList.add(invoiceDTO);

		}
		LoggerUtil.logInfo("Invoice details list:" + invoiceDTOList);
		invoiceDTOList.sort(Comparator.comparing(InvoiceDTO::getInvoiceID).reversed());
		return invoiceDTOList;
	}

	@Override
	public List<SubscribedPlanDetailsDTO> fetchSubscribedPlanDetails(Integer orgId) {
		List<SubscribedPlanDetailsDTO> subscPlanList = new ArrayList<>();
		OrganizationSubscription orgSubsc = orgSubscDAO.fetchSubcribedPlanDetails(orgId);
		List<OrganizationSubscriptionDetail> planSubscDetailsList = orgSubscDetailDAO
				.fetchPlanFeatureDetails(Arrays.asList(orgSubsc.getOrganizationSubscriptionID()));
		SubscribedPlanDetailsDTO planDetailsDTO = null;

		for (OrganizationSubscriptionDetail planSubscDetails : planSubscDetailsList) {
			planDetailsDTO = new SubscribedPlanDetailsDTO();
			planDetailsDTO.setFeatureName(planSubscDetails.getFeature().getFeatureName());
			planDetailsDTO.setFeatureDesc(planSubscDetails.getFeature().getFeatureDescription());
			planDetailsDTO.setPlanName(planSubscDetails.getPlan().getPlanName());
			planDetailsDTO.setPlanTerm(planSubscDetails.getPlanterm().getPlanTermName());
			planDetailsDTO.setStatus(planSubscDetails.getActive());
			planDetailsDTO.setSubscribedDate(planSubscDetails.getSubscribedDate());
			planDetailsDTO.setEndDate(planSubscDetails.getEndDate());
			subscPlanList.add(planDetailsDTO);
		}
		LoggerUtil.logInfo("subscPlanList" + subscPlanList);
		return subscPlanList;
	}

	@Override
	public List<Integer> fetchChangedPlanDetails(Integer orgId) {

		List<OrganizationSubscription> orgSubscDetails = orgSubscDAO.fetchInvoiceDetails(orgId);

		List<OrganizationSubscriptionDetail> changedPlan = orgSubscDetailDAO
				.fetchPlanFeatureDetails(Arrays.asList(orgSubscDetails.get(0).getOrganizationSubscriptionID()));

		List<Integer> planFeatureChargeIds = new ArrayList<>();
		for (OrganizationSubscriptionDetail orgSubscDetail : changedPlan) {
			planFeatureChargeIds.add(orgSubscDetail.getPlanFeatureCharge().getPlanFeatureChargeID());
		}
		return planFeatureChargeIds;
	}

	@Override
	public Integer changePlanSubcription(Integer orgId, Integer customerId, List<Integer> planFeatureChargeIds) {

		// For checking if any plan is active for organization
		List<OrganizationSubscriptionDetail> activeSubscriptionList = orgSubscriptionDetailsDAO
				.fetchSubcribedPlanDetails(orgId);

		// If any plan is active for organization then changes its renewal type to
		// Manual
		if (!activeSubscriptionList.isEmpty()) {
			orgSubscriptionDetailsDAO.changeRenewalType(
					activeSubscriptionList.get(0).getOrganizationSubscription().getOrganizationSubscriptionID());
		}

		List<OrganizationSubscriptionDetail> orgSubscriptionDetailList = new ArrayList<>();

		BigDecimal totalAmount = null;

		List<PlanFeatureCharge> selectedPlanList = planFeatureChargeDAO
				.findByPlanFeatureChargeIDIn(planFeatureChargeIds);

		Organization org = organizationDAO.getOne(orgId);
		Customer customer = customerDAO.getOne(customerId);

		OrganizationSubscription orgSubscription = new OrganizationSubscription();

		orgSubscription.setOrganization(org);
		orgSubscription.setCustomer(customer);
		orgSubscription.setCurrentActiveSubscription(AccountServiceConstants.INACTIVE_PLAN);
		orgSubscription.setActive(AccountServiceConstants.INACTIVE);
		orgSubscription.setCreatedAt(new Date());
		orgSubscription.setCreatedBy(AccountServiceConstants.ADMIN);

		OrganizationSubscriptionDetail orgSubscriptionDetails = null;

		for (PlanFeatureCharge planList : selectedPlanList) {

			totalAmount = totalAmount != null ? totalAmount.add(planList.getTermChargeAmt())
					: planList.getTermChargeAmt();
			orgSubscriptionDetails = new OrganizationSubscriptionDetail();
			BeanUtils.copyProperties(planList, orgSubscriptionDetails);
			orgSubscriptionDetails.setOrganization(org);
			orgSubscriptionDetails.setPlanFeatureCharge(planList);
			orgSubscriptionDetails.setSubscriptionStatus(AccountServiceConstants.PENDING);
			orgSubscriptionDetails.setIsFree(AccountServiceConstants.INACTIVE_PLAN);
			orgSubscription.setBillAmt(totalAmount);
			orgSubscription.setTotalBillAmount(totalAmount);
			orgSubscriptionDetails.setOrganizationSubscription(orgSubscription);
			Calendar cal = null;
			OrganizationSubscriptionDetail activeFeature = null;
			// Checking if plan feature active
			if (!activeSubscriptionList.isEmpty()) {
				activeFeature = activeSubscriptionList.stream().filter(featureName -> planList.getFeature()
						.getFeatureName().equals(featureName.getFeature().getFeatureName())).findAny().orElse(null);

			}
			if (activeFeature != null) {
				LoggerUtil.logInfo("Feature is already active");
				cal = Calendar.getInstance();
				cal.setTime(activeFeature.getEndDate());
				cal.add(Calendar.DATE, 1);
				orgSubscriptionDetails.setStartDate(cal.getTime());
				cal.setTime(orgSubscriptionDetails.getStartDate());
				cal.add(Calendar.DATE,
						orgSubscriptionDetails.getPlanterm().getPlanTermName().equals("Monthly") ? 30 : 365);
				orgSubscriptionDetails.setEndDate(cal.getTime());

			} else {
				LoggerUtil.logInfo("Feature expired");
				cal = Calendar.getInstance();
				orgSubscriptionDetails.setStartDate(new Date());
				cal.add(Calendar.DATE,
						orgSubscriptionDetails.getPlanterm().getPlanTermName().equals("Monthly") ? 30 : 365);
				orgSubscriptionDetails.setEndDate(cal.getTime());
			}
			orgSubscriptionDetails.setCurrentActiveSubscription(AccountServiceConstants.INACTIVE_PLAN);
			orgSubscriptionDetails.setActive(AccountServiceConstants.INACTIVE);
			orgSubscriptionDetails.setCreatedAt(new Date());
			orgSubscriptionDetails.setCreatedBy(AccountServiceConstants.ADMIN);
			orgSubscriptionDetailList.add(orgSubscriptionDetails);
		}
		List<OrganizationSubscriptionDetail> orgSubscDetail = orgSubscriptionDetailsDAO
				.saveAll(orgSubscriptionDetailList);
		return orgSubscDetail.get(0).getOrganizationSubscription().getOrganizationSubscriptionID();

	}
}
