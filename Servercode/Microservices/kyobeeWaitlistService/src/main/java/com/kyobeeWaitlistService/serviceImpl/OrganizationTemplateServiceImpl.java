package com.kyobeeWaitlistService.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dao.OrganizationDAO;
import com.kyobeeWaitlistService.dao.OrganizationLanguageDAO;
import com.kyobeeWaitlistService.dao.OrganizationTemplateDAO;
import com.kyobeeWaitlistService.dao.SmsTemplateLanguageMappingDAO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.LanguageKeyMappingDTO;
import com.kyobeeWaitlistService.dto.OrgSettingDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.SmsContentDTO;
import com.kyobeeWaitlistService.dto.SmsTemplateDTO;
import com.kyobeeWaitlistService.entity.LangMaster;
import com.kyobeeWaitlistService.entity.Organization;
import com.kyobeeWaitlistService.entity.OrganizationLang;
import com.kyobeeWaitlistService.entity.OrganizationTemplate;
import com.kyobeeWaitlistService.entity.SmsTemplateLanguageMapping;
import com.kyobeeWaitlistService.service.OrganizationTemplateService;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.CommonUtil;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@Service
@Transactional
public class OrganizationTemplateServiceImpl implements OrganizationTemplateService {
	@Autowired
	private OrganizationTemplateDAO organizationTemplateDAO;
	
	@Autowired
	private OrganizationDAO organizationDAO;
	
	@Autowired
	private SmsTemplateLanguageMappingDAO smsTemplateLanguageMappingDAO;
	
	@Autowired
	private OrganizationLanguageDAO organizationLanguageDAO;
	
	@Autowired
	private WaitListService waitListService;
	
	

	@Override
	public List<OrganizationTemplateDTO> getOrganizationTemplates(SmsContentDTO smsContentDTO) {

		List<OrganizationTemplateDTO> templates = new ArrayList<>();

		List<OrganizationTemplate> smsTemplates = organizationTemplateDAO
				.getSmsTemplatesForOrganization(smsContentDTO.getOrgId(), smsContentDTO.getLangId());
		OrganizationTemplateDTO organizationTemplateDTO;

		for (OrganizationTemplate template : smsTemplates) {
			organizationTemplateDTO = new OrganizationTemplateDTO();
			BeanUtils.copyProperties(template, organizationTemplateDTO);
			templates.add(organizationTemplateDTO);

		}

		return templates;
	}

	@Override
	public List<OrganizationTemplateDTO> getSmsContent(SmsContentDTO smsContentDTO,
			List<OrganizationTemplateDTO> organizationTemplateDTOs, GuestMetricsDTO guestMetrics) {

		List<OrganizationTemplateDTO> smsContents = new ArrayList<>();

		for (OrganizationTemplateDTO organizationTemplateDTO : organizationTemplateDTOs) {
			String smsContent = organizationTemplateDTO.getTemplateText();
			LoggerUtil.logInfo("smsContent " + smsContent);
			smsContent = smsContent.replace("G_name", smsContentDTO.getGuestName());
			smsContent = smsContent.replace("#G_name", smsContentDTO.getGuestName());
			smsContent = smsContent.replace("G_rank", smsContentDTO.getGuestRank().toString());
			smsContent = smsContent.replace("Turl",
					CommonUtil.buildURL(smsContentDTO.getClientBase(), smsContentDTO.getGuestUuid()));
			smsContent = smsContent.replace("P_ahead", guestMetrics.getGuestAheadCount().toString());
			smsContent = smsContent.replace("W_time", guestMetrics.getTotalWaitTime().toString());
			organizationTemplateDTO.setTemplateText(smsContent);
			smsContents.add(organizationTemplateDTO);

		}
		return smsContents;

	}

	@Override
	public OrgSettingDTO addLanguage(Integer langId, Integer orgId) {
		
		List<SmsTemplateDTO> smsTemplates = new ArrayList<>();
		Organization organization = organizationDAO.findByOrganizationID(orgId);
		OrgSettingDTO orgSettingDTO = new OrgSettingDTO();
		
		OrganizationTemplate orgTemplate;
		SmsTemplateDTO smsTemplateDTO;
		
		List<SmsTemplateLanguageMapping> smsTemplateList = smsTemplateLanguageMappingDAO.fetchSmsTemplate(langId);
		
		List<OrganizationTemplate> orgTemplateList = new ArrayList<>();
		for(SmsTemplateLanguageMapping smsTemplate : smsTemplateList)
		{
			orgTemplate = new OrganizationTemplate();
			smsTemplateDTO = new SmsTemplateDTO();
			
			BeanUtils.copyProperties(smsTemplate, orgTemplate);
			
			orgTemplate.setOrganization(organization);
			orgTemplate.setLanguageID(smsTemplate.getLangmaster().getLangID());
			orgTemplate.setCreatedAt(new Date());
			orgTemplateList.add(orgTemplate);
			BeanUtils.copyProperties(orgTemplate, smsTemplateDTO);
			smsTemplates.add(smsTemplateDTO);
		}	
		organizationTemplateDAO.saveAll(orgTemplateList);
		
		// for saving organization lang info
		OrganizationLang organizationLang = new OrganizationLang();

		LangMaster langMaster = new LangMaster();
		langMaster.setLangID(langId);
		organizationLang.setLangmaster(langMaster);
		organizationLang.setOrganization(organization);
		organizationLang.setActive(WaitListServiceConstants.ACTIVE);
		organizationLang.setCreatedBy(WaitListServiceConstants.ADMIN);
		organizationLang.setCreatedAt(new Date());

		organizationLanguageDAO.save(organizationLang);
		
		List<LanguageKeyMappingDTO> langKeyMapList = waitListService.fetchOrgLangKeyMap(orgId);
		orgSettingDTO.setLanguageList(langKeyMapList);
		orgSettingDTO.setSmsTemplateDTO(smsTemplates);
		
		return orgSettingDTO;
		
	}

	@Override
	public void deleteLanguage(Integer orgId, Integer langId) {
		
		organizationTemplateDAO.deleteOrgTemplate(orgId, langId);
		organizationLanguageDAO.deleteOrgLanguage(orgId, langId);
	}
}
