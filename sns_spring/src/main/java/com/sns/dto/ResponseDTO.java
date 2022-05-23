package com.sns.dto;

import java.util.HashMap;
import java.util.List;

public class ResponseDTO<T> {

	private String resultCode; 
	private String resultMsg;
	
	private HashMap<String, T> resultData;
	

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

	public HashMap<String, T> getResultData() {
		return resultData;
	}

	public void setResultData(HashMap<String, T> resultData) {
		this.resultData = resultData;
	}



	
	
	
}
