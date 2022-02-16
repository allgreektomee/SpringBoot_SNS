package com.sns.login;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class LoginRestController {
	final UserDAO dao;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public LoginRestController(UserDAO dao) {
		this.dao =  dao;
	}
	
	//회원가입 
	@PostMapping("/signup")
	public String signUp(@RequestBody User user) {
		
		try {
			dao.signUp(user);
			logger.warn("signup 완료 !");
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("signup 실패 !");
			return "signUp 실패";
		}
		
		return "result 0";
	}
	
	@PostMapping("/signin")
	public User signIn(@RequestParam String id, @RequestParam String pw) {
		User user = null;
		try {
			user = dao.signIn(id, pw);
			
		}catch(Exception e){
			e.printStackTrace();
		
		}
		
		return user;
	}
	
	@GetMapping
	public List<User>  userList(HttpServletRequest request) {
    	List<User> list = null;
		try {
			list = dao.getAllUser();
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return list;
    }
	
}
