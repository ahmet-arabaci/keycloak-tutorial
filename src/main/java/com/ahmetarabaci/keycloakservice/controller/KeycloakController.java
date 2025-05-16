package com.ahmetarabaci.keycloakservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/test")
@SecurityRequirement(name = "Keycloak")
public class KeycloakController {
	
	// API: /test/public | Access: Public
	@PostMapping("/public")	
	public String checkPublic() {
		return "API: '/test/public' | Public access has been confirmed.";
	}
	
	// API: /test/user1 | Access: "user1"
	@PostMapping("/user1")
	public String checkUser1() {
		return "API: '/test/user1' | 'user1' has been authorized.";
	}
	
	// API: /test/user2 | Access: "user2"
	@PostMapping("/user2")
	public String checkUser2() {
		return "API: '/test/user2' | 'user2' has been authorized.";
	}
	
	// API: /test/user3 | Access: "user3"
	@GetMapping("/user3")
	public String checkUser3WithGET() {
		return "API: '/test/user3' (HTTP GET) | 'user3' has been authorized.";
	}
	
	// API: /test/user3 | Not Access: "user3"
	@PostMapping("/user3")
	public String checkUser3WithPOST() {
		return "API: '/test/user3' (HTTP POST) | 'user3' has been authorized.";
	}
	
	// API: /test/all | Access: "user1", "user2", "user3"
	@GetMapping("/all/{user}")
	public String checkAll(@PathVariable("user") String user) {
		return "API: '/test/all/{user}' | '" + user + "' has been authorized.";
	}
	
	// API: /test/user4 | Access: "user4" User Policy
	@PostMapping("/user4")
	public String checkUser4() {
		return "API: '/test/user4' | 'user4' has been authorized.";
	}
	
	// API: /test/group | Access: "user1", "user2" Group Policy
	@PostMapping("/group")
	public String checkGroup() {
		return "API: '/test/group' | Group has been authorized.";
	}
	
	// API: /test/regex | Access: "test" Regex Policy
	@PostMapping("/regex")
	public String checkRegex() {
		return "API: '/test/regex' | 'user5' has been authorized.";
	}
	
	// API: /test/time | Access: Specific Time Interval (1 Minute)
	@PostMapping("/time")
	public String checkTime() {
		return "API: '/test/time' | 'user6' has been authorized.";
	}
}

