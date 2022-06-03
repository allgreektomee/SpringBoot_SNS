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

import com.sns.dto.ResponseDTO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	public ResponseDTO<Sns> addSns(@RequestBody Sns sns) {
		logger.info("sns 등록");
		ResponseDTO<Sns> responseDTO = new ResponseDTO<Sns>();
		
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
//		responseDTO.getResultData(Sns)
		
		return responseDTO;
	}
	
	
	//전체목록 
	@GetMapping
	public ResponseDTO<Sns>  listSns() {
		ResponseDTO<Sns> responseDTO = new ResponseDTO<Sns>();
		String result = "";
		
		List<Sns> list = null;
		try {
			list = dao.getAllPost();
			
			
		}catch(Exception e){
			e.printStackTrace();
			result ="실패  msg";
		}
		result = "성공 msg";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		responseDTO.setResultData(list);

		
		return responseDTO;
	}
	
	//목록 
	@GetMapping("/{sid}")
	public ResponseDTO<Sns>  getSns(@PathVariable int sid) {
		ResponseDTO<Sns> responseDTO = new ResponseDTO<Sns>();
		List<Sns> list = new ArrayList<>();
		
		String result = "";
		Sns sns = null;
		
		try {
			sns = dao.getPost(sid);
			list.add(sns);
		}catch(Exception e){
			e.printStackTrace();
			result ="실패  msg";
		}
		result = "성공 msg";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		responseDTO.setResultData(list);
		
		return responseDTO;
		
		
	}
	
	//삭제 
	@DeleteMapping("{sid}")
	public ResponseDTO<Sns>  deleteSns(@PathVariable("sid") int sid) {
		ResponseDTO<Sns> responseDTO = new ResponseDTO<Sns>();
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
	public ResponseDTO<Sns> updateSns(@RequestBody Sns sns ,@PathVariable("sid") int sid ){
		
		ResponseDTO<Sns> responseDTO = new ResponseDTO<Sns>();
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
	// 포스트man 
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
//		result = "http://localhost:8080/api/sns/img/"+filename;
		result = "addPost 완료";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		return responseDTO;
	}
	
	
	// 
	// 파일 업로드 
	//
	@PostMapping("/m/upload")
	public ResponseDTO<Object> uploadImg(@RequestParam("file") MultipartFile file) {
		logger.info("uploadImg 등록");
		logger.info(file.getName());
		String filename = "";
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();
		String result = "";
	
//		try {
//
//			
			//경로 생성 
//			File dir = new File(restDir+"/"+fcode);
//			dir.mkdir(); // 디렉토리 생성 
			
//			int i=0;
//			//업로드한 파일들 저장 
//			for(MultipartFile file : files) {
//				i++;
//				// UUID(fcode)_1.png
//				logger.info(file.getName());
//				logger.info(file.getOriginalFilename());
//				
//				filename = file.getName() + Long.toString(i)+"."+FilenameUtils.getExtension(file.getOriginalFilename());  ;
//				// 저장 파일 객체 생성
////				File dest = new File(restDir+"/"+file.getName()+"/"+filename); 
////			
////				logger.info(dest.getName());
////				
////				// 파일 저장
////				file.transferTo(dest);
////				
////				dao.addFile(filename, file.getName());
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info("addPost 오류");
//			result =  "addPost 오류 ";
//		}
//		result = "http://localhost:8080/api/sns/img/"+filename;
		result = "addPost 완료";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		return responseDTO;
	}
	
	@PostMapping("/m/upload2")
	public ResponseDTO<Object> uploadimages (@RequestParam("files") ArrayList<MultipartFile> files)  {
		logger.info("uploadImg222 등록");
		String filename = "";
		String result = "";
		
		try {

			int i=0;
			//업로드한 파일들 저장 
			for(MultipartFile file : files) {
				logger.info("1." + file.getName());
				logger.info("2." + file.getOriginalFilename());
				
				filename = file.getOriginalFilename();
				String fileNamePath[] = filename.split("_");
				
				if (i == 0) {
					//경로 생성 
				
				
					File dir = new File(restDir+"/"+fileNamePath[0]);
					dir.mkdir(); // 디렉토리 생성 
				}
			
				
				filename = fileNamePath[0]+"_" + Long.toString(i)+"."+FilenameUtils.getExtension(file.getOriginalFilename());  
				
				logger.info(filename);
				// 저장 파일 객체 생성
				File dest = new File(restDir+"/"+fileNamePath[0]+"/"+filename); 
			
				logger.info(dest.getName());
				
				// 파일 저장
				file.transferTo(dest);
				
				dao.addFile(filename, fileNamePath[0]);
				i++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("addPost 오류");
			result =  "addPost 오류 ";
		}
	
	
		ResponseDTO<Object> responseDTO = new ResponseDTO<Object>();

		
		
		result = "addPost 완료";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		return responseDTO;
		
	}
	// 이미지 다운로드 포스트
	//	http://localhost:8080/api/sns/img/33484151-6adb-4fb6-bc08-04a56226536b_1_66.png
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
        String imgName;

        imgName = uuid +"_"+idx;
        
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
		responseDTO.setResultMap(hasMap);
		
		return responseDTO;
	}
	

	//전체목록 
	@GetMapping("/v2")
	public ResponseDTO<Sns2>  listSnsV2() {
		ResponseDTO<Sns2> responseDTO = new ResponseDTO<Sns2>();
		String result = "";
		
		List<Sns2> list = null;
		try {
			list = dao.getAllPostV2();
			
			
		}catch(Exception e){
			e.printStackTrace();
			result ="실패  msg";
		}
		result = "성공 msg";
		responseDTO.setResultCode("0000");
		responseDTO.setResultMsg(result);
		responseDTO.setResultData(list);

		
		return responseDTO;
	}
	
	
	
}
