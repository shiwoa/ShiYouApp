package com.jiaoyu.entity;

import java.io.Serializable;

/**
 * 类说明: 公共的实体类
 */
public class EntityPublicType implements Serializable {

	private String type;
	private String logo;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
