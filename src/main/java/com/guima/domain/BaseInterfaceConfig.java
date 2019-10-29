package com.guima.domain;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseInterfaceConfig<M extends BaseInterfaceConfig<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setConfigKey(java.lang.String configKey) {
		set("config_key", configKey);
	}

	public java.lang.String getConfigKey() {
		return get("config_key");
	}

	public void setConfigValue(java.lang.String configValue) {
		set("config_value", configValue);
	}

	public java.lang.String getConfigValue() {
		return get("config_value");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setSortIdx(java.lang.Integer sortIdx) {
		set("sort_idx", sortIdx);
	}

	public java.lang.Integer getSortIdx() {
		return get("sort_idx");
	}

}
