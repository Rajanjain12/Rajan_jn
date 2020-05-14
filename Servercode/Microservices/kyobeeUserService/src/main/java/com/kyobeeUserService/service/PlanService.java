package com.kyobeeUserService.service;

import java.util.List;

import com.kyobeeUserService.dto.PlanDetailsDTO;
import com.kyobeeUserService.dto.PromoCodeDTO;
import com.kyobeeUserService.util.Exception.PromoCodeException;

public interface PlanService {

	public PlanDetailsDTO fetchPlanDetails(String country);
	public Integer savePlanDetails(Integer orgId, Integer customerId, List<Integer> planFeatureChargeIds);
	public void savePromoCode(PromoCodeDTO promoCodeDTO) throws PromoCodeException;

}
