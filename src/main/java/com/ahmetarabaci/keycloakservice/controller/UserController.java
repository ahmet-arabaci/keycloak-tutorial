package com.ahmetarabaci.keycloakservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahmetarabaci.keycloakservice.model.RoleDto;
import com.ahmetarabaci.keycloakservice.model.UserDto;
import com.ahmetarabaci.keycloakservice.service.KeycloakAdminUtil;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private KeycloakAdminUtil util;
	
	@Value("${realm}")
	private String realm;
	
	@GetMapping("/list")
	public List<UserDto> getUserList() {
		Keycloak keycloak = util.getInstance();
		List<UserRepresentation> userRepList = keycloak.realm(realm).users().list();
		List<UserDto> userList = new ArrayList<UserDto>();
		
		for (UserRepresentation userRep : userRepList) {
			userList.add(map(userRep));
			LOGGER.info(String.format("ID: %s, First Name: %s, Last Name: %s, Email: %s, Username: %s", 			
				userRep.getId(), userRep.getFirstName(), userRep.getLastName(), userRep.getEmail(), userRep.getUsername()));
		}
		
		return userList;		
	}
	
	@GetMapping("/get/{id}")
	public UserDto getUser(@PathVariable("id") String id) {
		Keycloak keycloak = util.getInstance();
		UserRepresentation userRep = keycloak.realm(realm).users().get(id).toRepresentation();
		
		LOGGER.info(String.format("First Name: %s, Last Name: %s, Email: %s, Username: %s", 			
				userRep.getFirstName(), userRep.getLastName(), userRep.getEmail(), userRep.getUsername()));
		return map(userRep);
	}
	
	@PostMapping("/create")
	public String createUser(@RequestBody UserDto dto) {
		try {
			Keycloak keycloak = util.getInstance();
			keycloak.realm(realm).users().create(map(dto));
			return "User created successfully.";
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing createUser function!", e);
			return "Exception occurred while executing createUser function!";
		}
	}
	
	@PutMapping("/update")
	public String updateUser(@RequestBody UserDto dto) {
		try {
			Keycloak keycloak = util.getInstance();
			keycloak.realm(realm).users().get(dto.getID()).update(map(dto));
			return "User updated successfully.";
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing updateUser function!", e);
			return "Exception occurred while executing updateUser function!";
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") String id) {
		try {
			Keycloak keycloak = util.getInstance();
			keycloak.realm(realm).users().delete(id);
			return "User deleted successfully.";
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing deleteUser function!", e);
			return "Exception occurred while executing deleteUser function!";
		}
	}
	
	@GetMapping("/roles/{id}")
	public String getUserRoles(@PathVariable("id") String id) {
		try {
			Keycloak keycloak = util.getInstance();
			List<RoleRepresentation> roleRepList = keycloak.realm(realm).users().get(id)
					.roles().realmLevel().listAll();
			for (RoleRepresentation roleRep : roleRepList) {
				LOGGER.info(String.format("Role ID: %s, Role Name: %s, Role Description: %s", 
						roleRep.getId(), roleRep.getName(), roleRep.getDescription()));
			}
			
			return "User roles have been received successfully.";
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing getUserRoles function!", e);
			return "Exception occurred while executing getUserRoles function!";
		}
	}
	
	@PostMapping("/roles/remove")
	public String removeUserRoles(@RequestBody RoleDto dto) {
		try {
			Keycloak keycloak = util.getInstance();
			keycloak.realm(realm).users().get(dto.getUserId()).roles().realmLevel()
				.remove(List.of(map(dto)));
			return "User role has been removed successfully!";
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing removeUserRoles function!", e);
			return "Exception occurred while executing removeUserRoles function!";
		}
	}
	
	@PostMapping("/roles/add")
	public String addUserRoles(@RequestBody RoleDto dto) {
		try {
			Keycloak keycloak = util.getInstance();
			keycloak.realm(realm).users().get(dto.getUserId()).roles().realmLevel()
				.add(List.of(map(dto)));
			return "User role has been added successfully!";
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing addUserRoles function!", e);
			return "Exception occurred while executing addUserRoles function!";
		}
	}
	
	private RoleRepresentation map(RoleDto dto) {
		RoleRepresentation roleRep = new RoleRepresentation();
		roleRep.setId(dto.getId());
		roleRep.setName(dto.getName());
		roleRep.setDescription(dto.getDescription());
		return roleRep;
	}
	
	private UserRepresentation map(UserDto dto) {
		List<CredentialRepresentation> credentialRepList = new ArrayList<CredentialRepresentation>();
		CredentialRepresentation credentialRep = new CredentialRepresentation();
		credentialRep.setTemporary(false);
		credentialRep.setValue(dto.getPassword());
		credentialRepList.add(credentialRep);
		
		Map<String, List<String>> attributes = new HashMap<String, List<String>>();
		attributes.put("street", Arrays.asList("XXX"));
		attributes.put("locality", Arrays.asList("XXX"));
		attributes.put("postal_code", Arrays.asList("12345"));
		attributes.put("country", Arrays.asList("XXX"));
		attributes.put("region", Arrays.asList("XXX"));
		attributes.put("formatted", Arrays.asList("XXX"));
		
		UserRepresentation userRep = new UserRepresentation();
		userRep.setId(dto.getID());
		userRep.setFirstName(dto.getFirstName());
		userRep.setLastName(dto.getLastName());
		userRep.setEmail(dto.getEmail());
		userRep.setUsername(dto.getUsername());
		userRep.setEnabled(true);
		userRep.setEmailVerified(true);
		
		userRep.setCredentials(credentialRepList);
		userRep.setAttributes(attributes);
		return userRep;
	}
	
	private UserDto map(UserRepresentation userRep) {
		UserDto dto = new UserDto();
		dto.setID(userRep.getId());
		dto.setFirstName(userRep.getFirstName());
		dto.setLastName(userRep.getLastName());
		dto.setEmail(userRep.getEmail());
		dto.setUsername(userRep.getUsername());
		return dto;
	}
	
}
