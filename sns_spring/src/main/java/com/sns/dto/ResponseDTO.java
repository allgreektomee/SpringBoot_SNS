package com.sns.dto;

import java.util.HashMap;
import java.util.List;

public class ResponseDTO<T> {

	private String resultCode; 
	private String resultMsg;
	
	private List<T> resultData;
	private HashMap<String,Object> resultMap;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public List<T> getResultData() {
		return resultData;
	}

	public void setResultData(List<T> resultData) {
		this.resultData = resultData;
	}

	public HashMap<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}






	
	
}
