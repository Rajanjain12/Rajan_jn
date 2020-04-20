package com.kyobeeUserService.serviceImpl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeUserService.dao.CustomerDAO;
import com.kyobeeUserService.dao.OrganizationCardDetailDAO;
import com.kyobeeUserService.dao.OrganizationDAO;
import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.entity.Customer;
import com.kyobeeUserService.entity.Organization;
import com.kyobeeUserService.entity.OrganizationCardDetail;
import com.kyobeeUserService.service.PaymentService;
import com.kyobeeUserService.util.UserServiceConstants;

@Service
public class PaymentServiceImpl implements PaymentService {
		
	@Autowired
	private OrganizationCardDetailDAO orgCardDetailsDAO;
	
	@Autowired
	private OrganizationDAO organizationDAO;
	
	@Autowired
	private CustomerDAO customerDAO;

	@Override
	public void saveOrgCardDetails(Integer orgId, Integer customerId, OrgCardDetailsDTO orgCardDetailsDTO) {
		OrganizationCardDetail orgCardDetail = new OrganizationCardDetail();
		
		BeanUtils.copyProperties(orgCardDetailsDTO,orgCardDetail);
		
		Organization org = organizationDAO.getOne(orgId);
		Customer customer = customerDAO.getOne(customerId);
		
		orgCardDetail.setOrganization(org);
		orgCardDetail.setCustomer(customer);
		orgCardDetail.setActive(UserServiceConstants.ACTIVE);
		orgCardDetail.setCreatedBy(UserServiceConstants.ADMIN);
		orgCardDetail.setCreatedAt(new Date());
	
		orgCardDetailsDAO.save(orgCardDetail);
	}

}
