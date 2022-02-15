package com.example.demo;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class WebController {
	
	@GetMapping("/hello")
	public String test() {
		return "hello";
	}
	
	
    @GetMapping("/msg")
    @ResponseBody
    public String testWithMsg(@RequestParam(value="msg", required=false) String msg) {
	    return msg;
    }
    
    @GetMapping("/user/{id}")
    public String testWithModel(@PathVariable String id, Model m) {
    	
    	m.addAttribute("id", id);
	    return "hello";
    }
    
    @GetMapping("/detail")
    public String detail(Model m) {
//    	HashMap<String,String> hasMap = new HashMap<>(){{
//    	    put("id","devcation");
//    	    put("pw","20");
//    	    put("name","jyp");
//    	}};    
    	
    	HashMap<String,String> hashMap = new HashMap<>();
    	hashMap.put("id", "devcation");
    	hashMap.put("pw","20");
    	hashMap.put("name","jyp");
    	
    	m.addAttribute("hashMap", hashMap);
    	
    	return "hello";
    }
    
}
