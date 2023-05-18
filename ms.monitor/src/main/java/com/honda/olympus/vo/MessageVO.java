package com.honda.olympus.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MessageVO {
	
	
	@NotNull(message = "status is mandatory")
	private String status;
	
	@NotBlank(message = "msg is mandatory")
	private String msg;
	
	private String file;
	
	

	public MessageVO() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	
	

}
