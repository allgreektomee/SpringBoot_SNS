package com.sns.board;

import java.util.HashMap;
import java.util.List;

public class ResponseDTO<T> {

	private String resultCode; 
	private String resultMsg;
	
	private List<T> list;
	private HashMap<String, T> hasmap;
	

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

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public HashMap<String, T> getHasmap() {
		return hasmap;
	}

	public void setHasmap(HashMap<String, T> hasmap) {
		this.hasmap = hasmap;
	}
	
	
	
}
