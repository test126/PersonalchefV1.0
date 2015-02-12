package com.goodfriends.personalchef.bean;

import java.io.Serializable;
import java.util.List;

public class GsonAdvs implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Advs> content;
	private String errcode ;
	private String errmsg;
	
	public List<Advs> getContent() {
		return content;
	}
	public void setContent(List<Advs> content) {
		this.content = content;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}
