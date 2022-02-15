package com.example.demo;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestFulController {
	
	@GetMapping("/hello")
    public String hello() {
	    return "hello world";
    }
	
	@GetMapping("/user")
	public String testApi(@RequestParam(value="id", required=false) String id) {
	    return "사용자 id : " + id;
    }
	
    @GetMapping("/detail")
    public HashMap<String,String> detail() {
//    	HashMap<String,String> hasMap = new HashMap<>(){{
//    	    put("id","devcation");
//    	    put("pw","20");
//    	    put("name","jyp");
//    	}};    
    	
    	HashMap<String,String> hasMap = new HashMap<>();
    	hasMap.put("id", "devcation");
    	hasMap.put("pw","20");
    	hasMap.put("name","jyp");
    	return hasMap;
    }

}
