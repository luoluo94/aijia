package com.guima.domain;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePlanDetailAnnex<M extends BasePlanDetailAnnex<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setPlanDetailId(java.lang.String planDetailId) {
		set("plan_detail_id", planDetailId);
	}

	public java.lang.String getPlanDetailId() {
		return get("plan_detail_id");
	}

	public void setAnnexUrl(java.lang.String annexUrl) {
		set("annex_url", annexUrl);
	}

	public java.lang.String getAnnexUrl() {
		return get("annex_url");
	}

}
