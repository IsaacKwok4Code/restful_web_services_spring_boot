package com.yin4learning.restful_web_service.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.yin4learning.restful_web_service.DTO.UserDTO;
import com.yin4learning.restful_web_service.entity.UserEntity;
import com.yin4learning.restful_web_service.repository.UserRepository;
import com.yin4learning.restful_web_service.utils.Utils;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils util;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDTO createUserFunction(UserDTO userRsDTO) {
		//checking existing user or not. if yes, throw error message
		UserEntity existingUserDetails = userRepository.findByEmail(userRsDTO.getEmail());
		if(existingUserDetails !=null) {
			throw new RuntimeException("Record is existing");
		}
		//create initial entity object to hold request data
		UserEntity userEntity = new UserEntity();
		//create return DTO object
		UserDTO returnObject = new UserDTO();
		
		BeanUtils.copyProperties(userRsDTO, userEntity);
		
		String publicUserId = util.generateUserId(30);
		userEntity.setUserId(publicUserId);
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userRsDTO.getPassword()));
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		BeanUtils.copyProperties(storedUserDetails, returnObject);
		return returnObject;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
