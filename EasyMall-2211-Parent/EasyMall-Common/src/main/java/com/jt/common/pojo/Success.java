package com.jt.common.pojo;

import java.util.Date;

public class Success {
	private Long successId;
	private Long seckillId;
	private Long userPhone;
	private Integer state;
	private Date createTime;
	public Long getSuccessId() {
		return successId;
	}
	public void setSuccessId(Long successId) {
		this.successId = successId;
	}
	public Long getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}
	public Long getuserPhone() {
		return userPhone;
	}
	public void setuserPhone(Long userPhone) {
		this.userPhone = userPhone;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
