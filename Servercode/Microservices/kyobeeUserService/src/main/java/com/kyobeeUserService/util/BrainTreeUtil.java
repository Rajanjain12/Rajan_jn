package com.kyobeeUserService.util;

import org.springframework.stereotype.Component;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

@Component
public class BrainTreeUtil {

	public BraintreeGateway getBrainTreeGateway() {
		return new BraintreeGateway(Environment.SANDBOX, UserServiceConstants.BT_MERCHANT_ID,
				UserServiceConstants.BT_PUBLIC_KEY, UserServiceConstants.BT_PRIVATE_KEY);
	}

}
