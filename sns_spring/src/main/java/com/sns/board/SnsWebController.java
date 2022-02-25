package com.sns.board;

import java.io.File;
import java.sql.SQLException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/sns")
public class SnsWebController {
	final SnsDAO dao ;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//어플리케이션.프로퍼티에 설정한 경로 
	@Value("${sns.imgPath}")
	String fdir;
	
	@Autowired
	public SnsWebController(SnsDAO dao) {
		this.dao =  dao;
	}
	
	@PostMapping("/add")
	public String addSns(@ModelAttribute Sns sns, Model model, @RequestParam("file") MultipartFile file) {
		try {
			// 저장 파일 객체 생성
			File dest = new File(fdir+"/"+file.getOriginalFilename()); 
			
			// 파일 저장
			file.transferTo(dest);
			
			// News 객체에 파일 이름 저장
			sns.setImg("/image/"+dest.getName());
			dao.addPost(sns);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("addPost 오류");
			model.addAttribute("error", "addPost 오류 ");
		}
		
		
		return "redirect:/sns/list";
	}
	
	@GetMapping("/list")
	public String listSns(Model model) {
		List<Sns> list;
		try {
			list = dao.getAllPost();
			model.addAttribute("snsList", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("오류발생 목록 조회 실패!");
			model.addAttribute("error", "오류발생 목록 조회 실패");
		}
		return "sns/snsList";
	}
	
	@GetMapping("/{sid}")
	public String getSns(@PathVariable int sid, Model model) {
		try {
			Sns sns = dao.getPost(sid);
			model.addAttribute("sns", sns);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.warn("오류발생 SNS 조회 실패!");
			model.addAttribute("error", "오류발생 SNS 조회 실패");
		}
		return "sns/snsView";
	}
	
	
	
	@GetMapping("/delete/{sid}")
	public String deleteSns(@PathVariable int sid, Model model) {
		try {
			dao.delPost(sid);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.warn("오류발생 SNS 삭제 실패!");
			model.addAttribute("error", "오류발생 SNS 삭제 실패");
		}
		return "redirect:/sns/list";
	}
	
	@PostMapping("/update/{sid}")
	public String updateSns(@ModelAttribute Sns sns,@PathVariable int sid, Model model) {
		try {
		
			dao.updatePost(sns, sid);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("update 오류");
			model.addAttribute("error", "update 오류 ");
		}
		
		
		return "redirect:/sns/list";
	}
	
	// 
	//다중파일 업로드 
	//
	
	@PostMapping("/add2")
	public String addSns2(@ModelAttribute Sns sns, Model model, @RequestParam("files") ArrayList<MultipartFile> files) {
		try {
			String fcode = UUID.randomUUID().toString(); // 랜덤 UUID 3470bf91-f8b0-4774-958f-5848302f0fcb
			
			//게시물 등록 
			sns.setImg(fcode);//파일id
			dao.addPost(sns);
			
			//경로 생성 
			File dir = new File(fdir+"/"+fcode);
			dir.mkdir(); // 디렉토리 생성 
			
			//업로드한 파일들 저장 
			for(MultipartFile file : files) {
				
				// 저장 파일 객체 생성
				File dest = new File(fdir+"/"+fcode+"/"+file.getOriginalFilename()); 
			
				logger.info(dest.getName());
				
				// 파일 저장
				file.transferTo(dest);
				
				dao.addFile(file.getOriginalFilename(), fcode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("addPost 오류");
			model.addAttribute("error", "addPost 오류 ");
		}
		
		
		return "redirect:/sns/list2";
	}
	
	@GetMapping("/m/{sid}")
	public String getSns2(@PathVariable int sid, Model model) {
		List<String> list;
		try {
			Sns sns = dao.getPost(sid);
			list = dao.getImgaes(sns.getImg()); // sns.getImg() // filecode

			logger.info(list.toString());
			model.addAttribute("sns", sns);
			model.addAttribute("imglist", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("오류발생 SNS 조회 실패!");
			model.addAttribute("error", "오류발생 SNS 조회 실패");
		}
		return "sns/snsView2";
	}
	
	
	@GetMapping("/list2")
	public String listSns2(Model model) {
		List<Sns> list;
		try {
			list = dao.getAllPost();
			model.addAttribute("snsList", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("오류발생 목록 조회 실패!");
			model.addAttribute("error", "오류발생 목록 조회 실패");
		}
		return "sns/snsList2";
	}
	

	@GetMapping("/m/delete/{sid}")
	public String deleteSns2(@PathVariable int sid, 
			@RequestParam("filecode") String filecode, 
			Model model) {
		
		logger.info(filecode);
		
		try {
			dao.delPost(sid);
			dao.delFile(filecode);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.warn("오류발생 SNS 삭제 실패!");
			model.addAttribute("error", "오류발생 SNS 삭제 실패");
		}
		return "redirect:/sns/list2";
	}
}
