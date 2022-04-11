package com.sns.board;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/sns")
public class SnsRestController {
	final SnsDAO dao ;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
//	
	//어플리케이션.프로퍼티에 설정한 경로 
	@Value("${sns.imgPath}")
	String restDir;
//	
	@Autowired
	public SnsRestController(SnsDAO dao) {
		this.dao =  dao;
	}
	
	//등록 
	@PostMapping
	public ResponseDTO<Object> addSns(@RequestBody Sns sns) {
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();
		String result = "";
		
		try {
			dao.addPost(sns);
		}catch(Exception e){
			e.printStackTrace();
			result =  "sns 등록 실패 ";
		}
		
		result =  "sns 등록";
		
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		
		return responseDTO;
	}
	
	
	//전체목록 
	@GetMapping
	public ResponseDTO<Object>  listSns() {
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();
		HashMap<String,Object> hasMap = new HashMap<>();
		String result = "";
		
		List<Sns> list = null;
		try {
			list = dao.getAllPost();
			hasMap.put("list", list);
			
		}catch(Exception e){
			e.printStackTrace();
			result ="실패  msg";
		}
		result = "성공 msg";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		responseDTO.setHasmap(hasMap);
		
		return responseDTO;
	}
	
	//목록 
	@GetMapping("/{sid}")
	public ResponseDTO<Object>  getSns(@PathVariable int sid) {
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();
		HashMap<String,Object> hasMap = new HashMap<>();
		
		String result = "";
		Sns sns = null;
		
		try {
			sns = dao.getPost(sid);
			hasMap.put("sns", sns);
		}catch(Exception e){
			e.printStackTrace();
			result ="실패  msg";
		}
		result = "성공 msg";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		responseDTO.setHasmap(hasMap);
		return responseDTO;
		
		
	}
	
	//삭제 
	@DeleteMapping("{sid}")
	public ResponseDTO<Object>  deleteSns(@PathVariable("sid") int sid) {
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();
		String result = "";
		
		try {
			dao.delPost(sid);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getLocalizedMessage();
		}
		result = " 성공";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		
		return responseDTO;
	}
	
	//수정 
	@PatchMapping("{sid}")
	public ResponseDTO<Object> updateSns(@RequestBody Sns sns ,@PathVariable("sid") int sid ){
		
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();
		String result = "";
		
		try {
			dao.updatePost(sns, sid);
		} catch (Exception e) {
			e.printStackTrace();
			result = "업데이트 실패 ";
		}
		
		
		result = "업데이트 성공";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		
		return responseDTO;
	}
	
	
	// 
	// 다중파일 업로드 
	//
	@PostMapping("/m")
	public ResponseDTO<Object> addSns2(@ModelAttribute Sns sns, @RequestParam("files") ArrayList<MultipartFile> files) {
		
		String filename = "";
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();
		String result = "";
		try {
			/*파일 이름만 저장, 이미지 저장 경로로 접근 (image/파일명)  */ 
//			String fcode = UUID.randomUUID().toString(); // 랜덤 UUID 3470bf91-f8b0-4774-958f-5848302f0fcb
//			
//			//게시물 등록 
//			sns.setImg(fcode);//파일id
//			dao.addPost(sns);
//			
//			//경로 생성 
//			File dir = new File(restDir+"/"+fcode);
//			dir.mkdir(); // 디렉토리 생성 
//			
//			//업로드한 파일들 저장 
//			for(MultipartFile file : files) {
//				
//				// 저장 파일 객체 생성
//				File dest = new File(restDir+"/"+fcode+"/"+file.getOriginalFilename()); 
//			
//				logger.info(dest.getName());
//				
//				// 파일 저장
//				file.transferTo(dest);
//				
//				dao.addFile(file.getOriginalFilename(), fcode);
//			}
			
			/*image url로 저장하고 접근 */	 
			
			String fcode = UUID.randomUUID().toString(); // 랜덤 UUID 3470bf91-f8b0-4774-958f-5848302f0fcb
			
			//게시물 등록 
			sns.setImg(fcode);//파일id
			Long sid = dao.addPost(sns); // 난수 값 fcode를, 게시글 고유아이디 sid로만 사용해도 된다. 현재는 둘다 사용 
			
			//경로 생성 
			File dir = new File(restDir+"/"+fcode);
			dir.mkdir(); // 디렉토리 생성 
			
			int i=0;
			//업로드한 파일들 저장 
			for(MultipartFile file : files) {
				i++;
				// UUID(fcode)_1_sid.png
				filename = fcode+"_"+ Long.toString(i)+"_"+sid+"."+FilenameUtils.getExtension(file.getOriginalFilename());  ;
				// 저장 파일 객체 생성
				File dest = new File(restDir+"/"+fcode+"/"+filename); 
			
				logger.info(dest.getName());
				
				// 파일 저장
				file.transferTo(dest);
				
				dao.addFile(filename, fcode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("addPost 오류");
			result =  "addPost 오류 ";
		}
		result = "http://localhost:8080/api/sns/img/"+filename;
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		return responseDTO;
	}
	
	// 이미지 다운로드 
	//	http://localhost:8080/image/"+filename
	@GetMapping("/img/{filename}")
	public ResponseEntity<Resource> getImg(@PathVariable String filename, HttpServletRequest request) {
		
		
		//filename  UUID_1_32.png   UUID(fcode. sns.img)_이미지순서_게시글SID
		String fileNamePath[] = filename.split("_");
        
        for(int i=0 ; i<fileNamePath.length ; i++)
        {
        	logger.info("fileNamePath["+i+"] : "+fileNamePath[i]);
        }
        String uuid = fileNamePath[0];
        String idx = fileNamePath[1];
        String imgName = fileNamePath[2];

        imgName = uuid +"_"+idx+"_"+imgName;
        
		Path filePath = Paths.get(restDir+"/"+uuid+"/"+imgName);
		
		logger.info(filePath.toString());
		Resource resource = null;
		
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	     
	    
	}
	
	@GetMapping("/m/{sid}")
	public ResponseDTO<Object> getSns2(@PathVariable int sid) {
		HashMap<String,Object> hasMap = new HashMap<>();
    
		List<String> list;
		try {
			Sns sns = dao.getPost(sid);
			list = dao.getImgaes(sns.getImg());

			
			hasMap.put("sns", sns);
			hasMap.put("imglist", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();
		
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg("msg");
		responseDTO.setHasmap(hasMap);
		
		return responseDTO;
	}
	

	
	
	
	
}
