package com.kyobeeUserService.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobeeUserService.dao.CustomerDAO;
import com.kyobeeUserService.dao.LangMasterDAO;
import com.kyobeeUserService.dao.LookupDAO;
import com.kyobeeUserService.dao.OrganizationTypeDAO;
import com.kyobeeUserService.dao.SmsTemplateLanguageMappingDAO;
import com.kyobeeUserService.dto.AddressDTO;
import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.OrganizationTypeDTO;
import com.kyobeeUserService.entity.Address;
import com.kyobeeUserService.entity.Customer;
import com.kyobeeUserService.entity.LangMaster;
import com.kyobeeUserService.entity.Lookup;
import com.kyobeeUserService.entity.Organization;
import com.kyobeeUserService.entity.OrganizationCategory;
import com.kyobeeUserService.entity.OrganizationLang;
import com.kyobeeUserService.entity.OrganizationTemplate;
import com.kyobeeUserService.entity.OrganizationType;
import com.kyobeeUserService.entity.SmsTemplateLanguageMapping;
import com.kyobeeUserService.service.SignUpService;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;

@Service
public class SignUpServiceImpl implements SignUpService {

	@Autowired
	private OrganizationTypeDAO organizationTypeDAO;

	@Autowired
	private LookupDAO lookUpDAO;

	@Autowired
	private SmsTemplateLanguageMappingDAO smsTemplateLanguageMappingDAO;

	@Autowired
	private LangMasterDAO langMasterDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Override
	public OrganizationDTO addBusiness(OrganizationDTO organizationDTO) {

		Customer customer = new Customer();

		customer.setCustomerName(organizationDTO.getOrganizationName());
		customer.setTrialPeriodStartDate(new Date());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		customer.setTrialPeriodEndDate(cal.getTime());
		customer.setCreatedAt(new Date());
		customer.setCreatedBy(UserServiceConstants.ADMIN);
		customer.setActive(UserServiceConstants.ACTIVE_ORG);

		Address address = new Address();

		AddressDTO addressDTO = organizationDTO.getAddressDTO();
		BeanUtils.copyProperties(addressDTO, address);
		address.setCreatedAt(new Date());
		address.setCreatedBy(UserServiceConstants.ADMIN);
		LoggerUtil.logInfo("Address is added");

		OrganizationType organizationType = organizationTypeDAO
				.fetchOrganizationType(organizationDTO.getOrgTypeId());

		Organization organization = new Organization();
		BeanUtils.copyProperties(organizationDTO, organization);
		organization.setActive(UserServiceConstants.ACTIVE_ORG);
		organization.setEmail(organizationDTO.getEmail());
		organization.setClientBase(UserServiceConstants.ADMIN);
		organization.setNotifyUserCount(UserServiceConstants.NOTIFY_FIRST);
		organization.setMaxParty(UserServiceConstants.MAX_PARTY);
		organization.setCreatedAt(new Date());
		organization.setAddress(address);
		organization.setOrganizationType(organizationType);
		organization.setCreatedBy(UserServiceConstants.ADMIN);
		organization.setCustomer(customer);
		LoggerUtil.logInfo("Organization is added");

		customer.setOrganization(organization);

		List<Lookup> lookupList = lookUpDAO.fetchSeatingAndMarketingPref(UserServiceConstants.SEATING_PREF_ID,
				UserServiceConstants.MARKETING_PREF_ID);

		List<OrganizationCategory> orgCategoryList = new ArrayList<>();
		OrganizationCategory orgCategory;
		for (Lookup lookup : lookupList) {
			orgCategory = new OrganizationCategory();
			orgCategory.setOrganization(organization);
			orgCategory.setLookup(lookup);
			orgCategory.setLookuptype(lookup.getLookuptype());
			orgCategoryList.add(orgCategory);
		}

		organization.setOrganizationcategories(orgCategoryList);

		List<SmsTemplateLanguageMapping> smstemplateList = smsTemplateLanguageMappingDAO
				.fetchSmsTemplate(UserServiceConstants.DEFAULT_LANG);
		List<OrganizationTemplate> orgTemplateList = new ArrayList<>();
		OrganizationTemplate orgTemplate;
		for (SmsTemplateLanguageMapping smstemplate : smstemplateList) {
			orgTemplate = new OrganizationTemplate();
			BeanUtils.copyProperties(smstemplate, orgTemplate);
			orgTemplate.setLanguageID(smstemplate.getLangmaster().getLangID());
			orgTemplate.setOrganization(organization);
			orgTemplate.setCreatedAt(new Date());
			orgTemplateList.add(orgTemplate);
		}
		organization.setOrganizationtemplates(orgTemplateList);
		LoggerUtil.logInfo("Organization Template Added");

		LangMaster langMaster = langMasterDAO.fetchLang(UserServiceConstants.DEFAULT_LANG);
		List<OrganizationLang> organizationLangList = new ArrayList<>();
		OrganizationLang organizationLang = new OrganizationLang();
		BeanUtils.copyProperties(langMaster, organizationLang);
		organizationLang.setActive(UserServiceConstants.ACTIVE_ORG);
		organizationLang.setCreatedAt(new Date());
		organizationLang.setCreatedBy(organizationDTO.getEmail());
		organizationLang.setOrganization(organization);
		organizationLang.setLangmaster(langMaster);
		organizationLangList.add(organizationLang);

		organization.setOrganizationlangs(organizationLangList);
		organization.setDefaultLangId(UserServiceConstants.DEFAULT_LANG);
		organization.setPplBifurcation(UserServiceConstants.PPL_BIFURCATION);

		LoggerUtil.logInfo("Organization Lang is Added");

		Customer savedCustomer = customerDAO.save(customer);
		organizationDTO.setCustomerId(savedCustomer.getCustomerID());
		organizationDTO.setOrgId(savedCustomer.getOrganization().getOrganizationID());
		
		LoggerUtil.logInfo("Business added successfully");
		return organizationDTO;

	}

	@Override
	public List<OrganizationTypeDTO> fetchAllOrganizationType() {
		return organizationTypeDAO.fetchAllOrganizationType();
	}

}
