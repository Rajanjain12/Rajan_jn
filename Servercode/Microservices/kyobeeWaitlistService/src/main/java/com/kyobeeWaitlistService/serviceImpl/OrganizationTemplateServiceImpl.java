package com.kyobeeWaitlistService.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dao.OrganizationTemplateDAO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.SmsContentDTO;
import com.kyobeeWaitlistService.entity.OrganizationTemplate;
import com.kyobeeWaitlistService.service.OrganizationTemplateService;
import com.kyobeeWaitlistService.util.CommonUtil;
import com.kyobeeWaitlistService.util.LoggerUtil;

@Service
@Transactional
public class OrganizationTemplateServiceImpl implements OrganizationTemplateService {
	@Autowired
	OrganizationTemplateDAO organizationTemplateDAO;

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
}
