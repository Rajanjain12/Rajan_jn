package com.kyobeeAccountService.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyobeeAccountService.dao.OrganizationDAO;
import com.kyobeeAccountService.dao.OrganizationTypeDAO;
import com.kyobeeAccountService.dao.OrganizationUserDAO;
import com.kyobeeAccountService.dao.TimezoneDAO;
import com.kyobeeAccountService.dao.UserDAO;
import com.kyobeeAccountService.dto.AddressDTO;
import com.kyobeeAccountService.dto.LoginUserDTO;
import com.kyobeeAccountService.dto.OrganizationDTO;
import com.kyobeeAccountService.entity.Address;
import com.kyobeeAccountService.entity.Organization;
import com.kyobeeAccountService.entity.User;
import com.kyobeeAccountService.service.AccountService;
import com.kyobeeAccountService.util.AWSUtil;
import com.kyobeeAccountService.util.CommonUtil;
import com.kyobeeAccountService.util.LoggerUtil;
import com.kyobeeAccountService.util.Exception.PasswordNotMatchException;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	OrganizationDAO organizationDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	OrganizationTypeDAO organizationTypeDAO;

	@Autowired
	TimezoneDAO timezoneDAO;

	@Autowired
	AWSUtil awsUtil;

	@Autowired
	OrganizationUserDAO organizationUserDAO;

	// For fetching account details
	@Override
	public OrganizationDTO fetchAccountDetails(Integer orgId) {
		OrganizationDTO orgDTO = new OrganizationDTO();
		AddressDTO addressDTO = new AddressDTO();
		Organization org = organizationDAO.getOne(orgId);
		BeanUtils.copyProperties(org, orgDTO);
		BeanUtils.copyProperties(org.getAddress(), addressDTO);
		orgDTO.setAddressDTO(addressDTO);
		orgDTO.setOrgTypeId(org.getOrganizationType().getTypeID());
		orgDTO.setTimezoneId(org.getTimezone().getTimezoneId());

		return orgDTO;
	}

	// For updating account details
	@Override
	public void updateAccountDetails(HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile imageFile = multipartRequest.getFile("imageFile");
		String orgDTOJson = multipartRequest.getParameter("orgDTO");
		String userDTOJson = multipartRequest.getParameter("userDTO");

		ObjectMapper mpr = new ObjectMapper();
		// converting json string to dto
		LoginUserDTO userDTO = mpr.readValue(userDTOJson, LoginUserDTO.class);
		User user = userDAO.findByUserID(userDTO.getUserID());
		BeanUtils.copyProperties(userDTO, user);
		user.setModifiedBy(user.getEmail());
		user.setModifiedAt(new Date());
		LoggerUtil.logInfo("Going to update user details");
		userDAO.save(user);

		OrganizationDTO orgDTO = mpr.readValue(orgDTOJson, OrganizationDTO.class);
		Organization org = organizationDAO.getOne(userDTO.getOrganizationID());
		Address address = org.getAddress();
		BeanUtils.copyProperties(orgDTO.getAddressDTO(), address);
		BeanUtils.copyProperties(orgDTO, org);
		org.setAddress(address);
		org.setOrganizationType(organizationTypeDAO.getOne(orgDTO.getOrgTypeId()));
		org.setTimezone(timezoneDAO.getOne(orgDTO.getTimezoneId()));
		org.setModifiedBy(org.getEmail());
		org.setModifiedAt(new Date());

		if (imageFile != null) {
			// image upload
			String name = imageFile.getOriginalFilename();
			LoggerUtil.logInfo(name);
			int index = name.lastIndexOf(".");
			String extension = name.substring(index + 1);
			String fileName = userDTO.getOrganizationID() + "." + extension;// set File Name
			fileName = fileName.replaceAll("\\\\", "-");
			fileName = fileName.replaceAll("\\/", "-");
			fileName = fileName.replaceAll(" ", "-");
			File file = new File(fileName);
			Files.copy(imageFile.getInputStream(), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
			String path = awsUtil.uploadProfileImage(file);
			org.setLogoFileName(path);

		}
		LoggerUtil.logInfo("Going to update organization details");
		organizationDAO.save(org);
	}

	// For updating password
	@Override
	public void updatePassword(String oldPassword, String newPassword, Integer userId)
			throws PasswordNotMatchException {

		User user = userDAO.findByUserID(userId);
		if (!user.getPassword().equals(CommonUtil.encryptPassword(oldPassword + user.getSaltString()))) {
			throw new PasswordNotMatchException("Old password does not match");
		}
		String salt = CommonUtil.getSaltString();
		user.setPassword(CommonUtil.encryptPassword(newPassword + salt));
		user.setSaltString(salt);
		user.setModifiedBy(user.getEmail());
		user.setModifiedAt(new Date());
		userDAO.save(user);
	}

	// For deleting account
	@Override
	public void deleteAccount(Integer orgId, Integer userId) {
		userDAO.deleteUser(userId);
		organizationDAO.deleteOrganization(orgId);
		organizationUserDAO.deleteOrganizationUser(userId, orgId);
	}

}
