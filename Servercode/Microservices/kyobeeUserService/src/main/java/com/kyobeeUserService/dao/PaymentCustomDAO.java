package com.kyobeeUserService.dao;

import com.kyobeeUserService.dto.UpdatePaymentDetailsDTO;

public interface PaymentCustomDAO {

	public void updatePaymentDetailsOnSuccess(UpdatePaymentDetailsDTO updatePaymentDetailsDTO);
	
	public void updatePaymentDetailsOnFailure(UpdatePaymentDetailsDTO updatePaymentDetailsDTO);
}
