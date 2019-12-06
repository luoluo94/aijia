package com.guima.domain;

import com.guima.kits.NumberConstant;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class PlanDetail extends BasePlanDetail<PlanDetail> {
	public static final PlanDetail dao = new PlanDetail();

	public void init(String planId,String planDetail,int sort,int signMaxNum){
		this.setPlanId(planId);
		this.setFinishPercentage(0);
		this.setHasAnnex(NumberConstant.ZERO);
		this.setPlanDetail(planDetail);
		this.setSortIndex(sort);
		this.setSignMaxNum(signMaxNum);
	}


}
