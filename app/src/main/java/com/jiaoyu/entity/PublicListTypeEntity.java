package com.jiaoyu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类说明: 公共的实体类首页--合作机构
 */
public class PublicListTypeEntity implements Serializable {

	private String message;
	private boolean success;
	private List<OrganizationEntity.EntityBean.CompanyVoListBean> entity;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<OrganizationEntity.EntityBean.CompanyVoListBean> getEntity() {
		return entity;
	}

	public void setEntity(List<OrganizationEntity.EntityBean.CompanyVoListBean> entity) {
		this.entity = entity;
	}
}
