package com.kyobeeAccountService.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeAccountService.dao.OrganizationSubscriptionDAO;
import com.kyobeeAccountService.dao.OrganizationSubscriptionDetailDAO;
import com.kyobeeAccountService.dto.InvoiceDTO;
import com.kyobeeAccountService.dto.SubscribedPlanDetailsDTO;
import com.kyobeeAccountService.entity.OrganizationSubscription;
import com.kyobeeAccountService.entity.OrganizationSubscriptionDetail;
import com.kyobeeAccountService.service.PlanService;
import com.kyobeeAccountService.util.LoggerUtil;

@Service
public class PlanServiceImpl implements PlanService {
	@Autowired
	OrganizationSubscriptionDetailDAO orgSubscDetailDAO;

	@Autowired
	OrganizationSubscriptionDAO orgSubscDAO;

	@Override
	public List<InvoiceDTO> fetchInvoiceDetails(Integer orgId) {
		List<InvoiceDTO> invoiceDTOList = new ArrayList<>();
		InvoiceDTO invoiceDTO = null;

		// For fetching last six plan subscription details
		List<OrganizationSubscription> invoiceDetailsList = orgSubscDAO.fetchInvoiceDetails(orgId);
		// Getting subcription id id from plan subscription list
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
			}
			invoiceDTO.setTitle(invoiceTitle);
			invoiceDTOList.add(invoiceDTO);

		}
		LoggerUtil.logInfo("Invoice details list:" + invoiceDTOList);
		invoiceDTOList.sort(Comparator.comparing(InvoiceDTO::getInvoiceID).reversed());
		return invoiceDTOList;
	}

	@Override
	public List<SubscribedPlanDetailsDTO> fetchPlanDetails(Integer orgId) {
		List<SubscribedPlanDetailsDTO> subscPlanList = new ArrayList<>();
		List<OrganizationSubscription> orgSubsc = orgSubscDAO.fetchInvoiceDetails(orgId);
		List<OrganizationSubscriptionDetail> planSubscDetailsList = orgSubscDetailDAO
				.fetchPlanFeatureDetails(Arrays.asList(orgSubsc.get(0).getOrganizationSubscriptionID()));
		SubscribedPlanDetailsDTO planDetailsDTO = null;
		
		for(OrganizationSubscriptionDetail planSubscDetails : planSubscDetailsList) {
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
		LoggerUtil.logInfo("subscPlanList"+subscPlanList);
		return subscPlanList;
	}

}
