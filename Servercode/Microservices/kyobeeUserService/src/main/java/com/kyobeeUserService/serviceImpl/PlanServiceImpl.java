package com.kyobeeUserService.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeUserService.dao.CountryDAO;
import com.kyobeeUserService.dao.CustomerDAO;
import com.kyobeeUserService.dao.OrganizationDAO;
import com.kyobeeUserService.dao.OrganizationSubscriptionDetailDAO;
import com.kyobeeUserService.dao.PlanDAO;
import com.kyobeeUserService.dao.PlanFeatureChargeDAO;
import com.kyobeeUserService.dao.PromotionalCodeDAO;
import com.kyobeeUserService.dto.PlanDTO;
import com.kyobeeUserService.dto.PlanDetailsDTO;
import com.kyobeeUserService.dto.PlanFeatureChargeDTO;
import com.kyobeeUserService.dto.PlanFeatureDTO;
import com.kyobeeUserService.dto.PlanTermDTO;
import com.kyobeeUserService.dto.PromoCodeDTO;
import com.kyobeeUserService.entity.Country;
import com.kyobeeUserService.entity.Customer;
import com.kyobeeUserService.entity.Feature;
import com.kyobeeUserService.entity.Organization;
import com.kyobeeUserService.entity.OrganizationSubscription;
import com.kyobeeUserService.entity.OrganizationSubscriptionDetail;
import com.kyobeeUserService.entity.PlanFeatureCharge;
import com.kyobeeUserService.entity.PlanTerm;
import com.kyobeeUserService.entity.PromotionalCode;
import com.kyobeeUserService.service.PlanService;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.PromoCodeException;

@Service
public class PlanServiceImpl implements PlanService {

	@Autowired
	private CountryDAO countryDAO;

	@Autowired
	private PlanFeatureChargeDAO planFeatureChargeDAO;

	@Autowired
	private PlanDAO planDAO;

	@Autowired
	private OrganizationDAO organizationDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private OrganizationSubscriptionDetailDAO orgSubscriptionDetailsDAO;

	@Autowired
	private PromotionalCodeDAO promotionalCodeDAO;

	@Override
	public PlanDetailsDTO fetchPlanDetails(String country) {

		PlanDetailsDTO planDetailsDTO = new PlanDetailsDTO();
		PlanFeatureChargeDTO planFeatureChargeDTO = null;
		PlanFeatureDTO planFeatureDTO = null;
		PlanTermDTO planTermDTO = null;

		List<PlanTermDTO> planTermList = new ArrayList<>();

		Country countryDetails = countryDAO.findByCountryName(country);
		List<PlanFeatureCharge> planFeatureChargeList = planFeatureChargeDAO
				.fetchPlanCharge(countryDetails.getCountryID());

		// for bifurcating plan term
		Map<PlanTerm, List<PlanFeatureCharge>> planTermMap = planFeatureChargeList.stream().collect(Collectors.groupingBy(PlanFeatureCharge::getPlanterm));

		for (Entry<PlanTerm, List<PlanFeatureCharge>> entry : planTermMap.entrySet()) {
			List<PlanFeatureDTO> planFeatureDTOList = new ArrayList<>();
			planTermDTO = new PlanTermDTO();
			List<PlanFeatureCharge> termList = entry.getValue();
			PlanTerm planterm = termList.get(0).getPlanterm();
			planTermDTO.setTermID(planterm.getPlanTermID());
			planTermDTO.setTermName(planterm.getPlanTermName());

			// for bifurcating plan features
			Map<Feature, List<PlanFeatureCharge>> featureMap = termList.stream()
					.collect(Collectors.groupingBy(PlanFeatureCharge::getFeature));

			for (Entry<Feature, List<PlanFeatureCharge>> featureentry : featureMap.entrySet()) {
				List<PlanFeatureChargeDTO> planFeatureChargeDTOList = new ArrayList<>();
				planFeatureDTO = new PlanFeatureDTO();
				List<PlanFeatureCharge> featureList = featureentry.getValue();
				Feature feature = featureList.get(0).getFeature();
				planFeatureDTO.setFeatureId(feature.getFeatureID());
				planFeatureDTO.setFeatureName(feature.getFeatureName());
				planFeatureDTO.setFeatureDesc(feature.getFeatureDescription());

				// for setting plan feature charge details
				for (PlanFeatureCharge plan : featureList) {
					planFeatureChargeDTO = new PlanFeatureChargeDTO();
					planFeatureChargeDTO.setFeatureCharge(plan.getTermChargeAmt());
					planFeatureChargeDTO.setPlanId(plan.getPlan().getPlanId());
					planFeatureChargeDTO.setPlanName(plan.getPlan().getPlanName());
					planFeatureChargeDTO.setCurrencyId(plan.getCurrency().getCurrencyID());
					planFeatureChargeDTO.setPlanFeatureChargeId(plan.getPlanFeatureChargeID());

					planFeatureChargeDTOList.add(planFeatureChargeDTO);
				}
				planFeatureDTO.setFeatureChargeDetails(planFeatureChargeDTOList);
				planFeatureDTOList.add(planFeatureDTO);

			}

			planTermDTO.setFeatureList(planFeatureDTOList);
			planTermList.add(planTermDTO);
		}

		planDetailsDTO.setTermList(planTermList);

		// for fetching plan list
		List<PlanDTO> planList = planDAO.getPlanList();
		planDetailsDTO.setPlanList(planList);

		return planDetailsDTO;
	}

	@Override
	public void savePlanDetails(Integer orgId, Integer customerId, List<Integer> planFeatureChargeIds) {

		BigDecimal totalAmount = null;
		List<OrganizationSubscriptionDetail> orgSubscriptionDetailList = new ArrayList<>();
		List<PlanFeatureCharge> selectedPlanList = planFeatureChargeDAO
				.findByPlanFeatureChargeIDIn(planFeatureChargeIds);
		Organization org = organizationDAO.getOne(orgId);
		Customer customer = customerDAO.getOne(customerId);

		OrganizationSubscription orgSubscription = new OrganizationSubscription();
		orgSubscription.setOrganization(org);
		orgSubscription.setCustomer(customer);
		orgSubscription.setCurrentActiveSubscription(UserServiceConstants.INACTIVE_PLAN);
		orgSubscription.setActive(UserServiceConstants.INACTIVE);
		orgSubscription.setCreatedAt(new Date());
		orgSubscription.setCreatedBy(UserServiceConstants.ADMIN);

		OrganizationSubscriptionDetail orgSubscriptionDetails = null;
		for (PlanFeatureCharge planList : selectedPlanList) {
			totalAmount = totalAmount != null ? totalAmount.add(planList.getTermChargeAmt())
					: planList.getTermChargeAmt();
			orgSubscriptionDetails = new OrganizationSubscriptionDetail();
			BeanUtils.copyProperties(planList, orgSubscriptionDetails);
			orgSubscriptionDetails.setOrganization(org);
			orgSubscriptionDetails.setPlanFeatureCharge(planList);
			orgSubscriptionDetails.setSubscriptionStatus(UserServiceConstants.PENDING);
			orgSubscriptionDetails.setIsFree(UserServiceConstants.INACTIVE_PLAN);
			orgSubscription.setBillAmt(totalAmount);
			orgSubscription.setTotalBillAmount(totalAmount);
			orgSubscriptionDetails.setOrganizationSubscription(orgSubscription);
			orgSubscriptionDetails.setCurrentActiveSubscription(UserServiceConstants.INACTIVE_PLAN);
			orgSubscriptionDetails.setActive(UserServiceConstants.INACTIVE);
			orgSubscriptionDetails.setCreatedAt(new Date());
			orgSubscriptionDetails.setCreatedBy(UserServiceConstants.ADMIN);
			orgSubscriptionDetailList.add(orgSubscriptionDetails);
		}

		orgSubscriptionDetailsDAO.saveAll(orgSubscriptionDetailList);
	}

	@Override
	public void savePromoCode(PromoCodeDTO promoCodeDTO) throws PromoCodeException {

		if (promoCodeDTO.getFlatAmt() != null && promoCodeDTO.getPercAmt() != null
				&& promoCodeDTO.getFlatAmt().compareTo(BigDecimal.ZERO) != 0
				&& promoCodeDTO.getPercAmt().compareTo(BigDecimal.ZERO) != 0) {
			throw new PromoCodeException("Please enter discount either in amount(flatAmt) or percentage(perAmt)");
		} else if (promoCodeDTO.getFlatAmt() == null && promoCodeDTO.getPercAmt() == null
				&& promoCodeDTO.getFlatAmt().compareTo(BigDecimal.ZERO) == 0
				&& promoCodeDTO.getPercAmt().compareTo(BigDecimal.ZERO) == 0) {
			throw new PromoCodeException("Please enter discount value");
		}

		PromotionalCode promoCode = new PromotionalCode();
		BeanUtils.copyProperties(promoCodeDTO, promoCode);

		Country country = countryDAO.findByCountryName(promoCodeDTO.getCountry());
		promoCode.setCodeType(
				promoCodeDTO.getPercAmt() != null ? UserServiceConstants.PERC : UserServiceConstants.AMOUNT);
		promoCode.setCurrency(country.getCurrency());
		promoCode.setActive(UserServiceConstants.ACTIVE);
		promoCode.setCreatedAt(new Date());
		promoCode.setCreatedBy(UserServiceConstants.ADMIN);

		promotionalCodeDAO.save(promoCode);

	}
}