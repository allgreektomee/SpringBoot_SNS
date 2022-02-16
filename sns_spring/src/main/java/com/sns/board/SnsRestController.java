package com.sns.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/sns")
public class SnsRestController {
	final SnsDAO dao ;
	
	@Autowired
	public SnsRestController(SnsDAO dao) {
		this.dao =  dao;
	}
	
	//등록 
	@PostMapping
	public String addSns(@RequestBody Sns sns) {
		
		try {
			dao.addPost(sns);
		}catch(Exception e){
			e.printStackTrace();
			
			return "addSns fail";
		}
		
		return "sns 등록";
	}
	
	//전체목록 
	@GetMapping
	public List<Sns> listSns() {
		List<Sns> list = null;
		try {
			list = dao.getAllPost();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	//목록 
	@GetMapping("/{sid}")
	public Sns getSns(@PathVariable int sid) {
		Sns sns = null;
		
		try {
			sns = dao.getPost(sid);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sns;
		
	}
	
	//삭제 
	@DeleteMapping("{sid}")
	public String deleteSns(@PathVariable("sid") int sid) {
		try {
			dao.delPost(sid);
		} catch (Exception e) {
			e.printStackTrace();
			return "sns 삭제 오류 ! - "+ sid;
		}
		return "sns 삭제 ";
	}
	
	//수정 
	@PatchMapping("{sid}")
	public String updateSns(@RequestBody Sns sns ,@PathVariable("sid") int sid ){
		try {
			dao.updatePost(sns, sid);
		} catch (Exception e) {
			e.printStackTrace();
			return "sns 업데이트 오류 ! - "+ sid;
		}
		return "sns 업데이트 ";
		
	}
	
}
