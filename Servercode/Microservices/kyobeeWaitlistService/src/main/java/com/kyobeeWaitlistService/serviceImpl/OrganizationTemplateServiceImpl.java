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
import com.kyobeeWaitlistService.dto.OrgSettingPusherDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.SmsContentDTO;
import com.kyobeeWaitlistService.dto.SmsTemplateDTO;
import com.kyobeeWaitlistService.entity.LangMaster;
import com.kyobeeWaitlistService.entity.Organization;
import com.kyobeeWaitlistService.entity.OrganizationLang;
import com.kyobeeWaitlistService.entity.OrganizationTemplate;
import com.kyobeeWaitlistService.entity.SmsTemplateLanguageMapping;
import com.kyobeeWaitlistService.service.GuestService;
import com.kyobeeWaitlistService.service.OrganizationTemplateService;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.CommonUtil;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.pusherImpl.NotificationUtil;

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
	
	@Autowired
	private GuestService guestService;

	@Override
	public List<OrganizationTemplateDTO> getOrganizationTemplates(SmsContentDTO smsContentDTO) {

		List<OrganizationTemplateDTO> templates = new ArrayList<>();

		List<OrganizationTemplate> smsTemplates = organizationTemplateDAO
				.getSmsTemplatesForOrganizationByLanguage(smsContentDTO.getOrgId(), smsContentDTO.getLangId());
		OrganizationTemplateDTO organizationTemplateDTO;

		for (OrganizationTemplate template : smsTemplates) {
			organizationTemplateDTO = new OrganizationTemplateDTO();
			BeanUtils.copyProperties(template, organizationTemplateDTO);
			switch (template.getLevel()) { 
			case 1:
				organizationTemplateDTO.setLevelName(WaitListServiceConstants.SMS_LEVEL_1_NAME);
				break;
			case 2:
				organizationTemplateDTO.setLevelName(WaitListServiceConstants.SMS_LEVEL_2_NAME);
				break;
			case 3:
				organizationTemplateDTO.setLevelName(WaitListServiceConstants.SMS_LEVEL_3_NAME);
				break;
			default:
				break;
			}
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
			
			switch (smsTemplateDTO.getLevel()) {
			case 1:
				smsTemplateDTO.setLevelName(WaitListServiceConstants.SMS_LEVEL_1_NAME);
				break;
			case 2:
				smsTemplateDTO.setLevelName(WaitListServiceConstants.SMS_LEVEL_2_NAME);
				break;
			case 3:
				smsTemplateDTO.setLevelName(WaitListServiceConstants.SMS_LEVEL_3_NAME);
				break;
			default:
				break;
			}
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
		
		LanguageKeyMappingDTO addedLanguage = langKeyMapList.stream()
				  .filter(language -> langId.equals(language.getLangId()))
				  .findAny()
				  .orElse(null);
		LoggerUtil.logInfo("added language:"+addedLanguage);
			
		//sending pusher
		OrgSettingPusherDTO pusherDTO = new OrgSettingPusherDTO();
		pusherDTO.setOp(WaitListServiceConstants.ADD_LANG_PUSHER);
		smsTemplates.forEach(f -> f.setSmsTemplateID(0));
		pusherDTO.setSmsTemplateDTO(smsTemplates);
		pusherDTO.setLangKeyMap(addedLanguage);
		
		NotificationUtil.sendMessage(pusherDTO, WaitListServiceConstants.PUSHER_CHANNEL_ENV+"_"+orgId);
		
		return orgSettingDTO;
		
	}

	@Override
	public void deleteLanguage(Integer orgId, Integer langId) {
		
		OrgSettingDTO orgSettingDTO = new OrgSettingDTO();
		OrgSettingPusherDTO pusherDTO = new OrgSettingPusherDTO();
		
		
		organizationTemplateDAO.deleteOrgTemplate(orgId, langId);
		organizationLanguageDAO.deleteOrgLanguage(orgId, langId);
		
			
		List<LanguageKeyMappingDTO> langKeyMapList = waitListService.fetchOrgLangKeyMap(orgId);
		orgSettingDTO.setLanguageList(langKeyMapList);
		
		pusherDTO.setOp(WaitListServiceConstants.DELETE_LANG_PUSHER);
		pusherDTO.setLangID(langId);
		
		NotificationUtil.sendMessage(pusherDTO, WaitListServiceConstants.PUSHER_CHANNEL_ENV+"_"+orgId);
	}

	@Override
	public List<OrganizationTemplateDTO> fetchSmsContentByOrganizationTeplates(SmsContentDTO smsContentDTO) {
		
		List<OrganizationTemplateDTO> smsTemplates = getOrganizationTemplates(smsContentDTO);
		GuestMetricsDTO metricsDTO = guestService.getGuestMetrics(smsContentDTO.getGuestId(),
				smsContentDTO.getOrgId());
		List<OrganizationTemplateDTO> smsContents = getSmsContent(smsContentDTO, smsTemplates,
				metricsDTO);
		return smsContents;
	}
}
