package com.guima.services;

import com.guima.base.kits.ModelWrapper;
import com.guima.base.kits.QueryParam;
import com.guima.base.service.BaseService_;
import com.guima.base.service.ServiceManager;
import com.guima.domain.Plan;
import com.guima.domain.PlanDetail;
import com.guima.domain.Sign;
import com.guima.domain.User;
import com.guima.enums.ConstantEnum;
import com.guima.kits.Constant;
import com.guima.kits.DateKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignService extends BaseService_<Sign>
{

    @Override
    protected Sign getConreteObject()
    {
        return new Sign();
    }

    @Override
    public ModelWrapper<Sign> getWrapper(Sign sign)
    {
        return new ModelWrapper(sign);
    }

    @Override
    public Sign getDao()
    {
        return Sign.dao;
    }

    private Page<Record> list(int pageNumber, int pageSize,StringBuffer conditionSql,List<Object> params){
        StringBuffer sqlSelect=new StringBuffer().append("select m.id,m.describer,m.creator,m.is_deleted,m.photo_url,m.create_time,m.privacy,")
                .append("m.plan_id,m.plan_detail_id,")
                .append(" n.name as creator_name,n.header_url as creator_header_url");

        return Db.paginate(pageNumber,pageSize,sqlSelect.toString(),conditionSql.toString(),params.toArray());
    }

    public Page<Record> listPublicSigns(int pageNumber, int pageSize){
        StringBuffer sql=new StringBuffer();
        sql.append(" from sign m join user n on m.creator=n.id")
                .append(" where 1=1 ")
                .append(" and m.privacy=?")
                .append(" and m.").append(Constant.IS_DELETED_MARK).append("=?")
                .append(" order by create_time desc");
        List<Object> params=new ArrayList<>();
        params.add(ConstantEnum.PRIVACY_PUBLIC.getValue());
        params.add(Constant.ACTIVE);
        return list(pageNumber,pageSize,sql,params);
    }

    public Page<Record> listMySigns(User user,int pageNumber, int pageSize,String currentMonth){
        StringBuffer sql=new StringBuffer();
        List<Object> params=new ArrayList<>();
        sql.append(" from sign m join user n on m.creator=n.id")
                .append(" where 1=1 ")
                .append(" and m.creator=?")
                .append(" and m.plan_id is null ")
                .append(" and m.").append(Constant.IS_DELETED_MARK).append("=?");
        params.add(user.getId());
        params.add(Constant.ACTIVE);
        //获取指定年月的打卡记录
        if(StringUtils.isNotBlank(currentMonth)){
            sql.append(" and m.create_time like ?");
            params.add(currentMonth+"%");
        }
        sql.append(" order by create_time asc");
        return list(pageNumber,pageSize,sql,params);
    }

    /**
     * 获取该计划下的打卡情况
     * @param planId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public Page<Record> listPlanSigns(String planId,int pageNumber, int pageSize){
        StringBuffer sql=new StringBuffer();
        sql.append(" from sign m join user n on m.creator=n.id")
                .append(" where 1=1 ")
                .append(" and m.plan_id=?")
                .append(" and m.").append(Constant.IS_DELETED_MARK).append("=?")
                .append(" order by create_time desc");
        List<Object> params=new ArrayList<>();
        params.add(planId);
        params.add(Constant.ACTIVE);
        return list(pageNumber,pageSize,sql,params);
    }

    public Page<Record> listAllSigns(int pageNumber, int pageSize){
        StringBuffer sql=new StringBuffer();
        sql.append(" from sign m join user n on m.creator=n.id")
                .append(" where 1=1 ")
                .append(" order by create_time desc");
        return list(pageNumber,pageSize,sql,new ArrayList<>());
    }

    public boolean removeSign(Sign sign){
        sign.setIsDeleted(Constant.IS_DELETED_YES);
        if(!StrKit.isBlank(sign.getPlanDetailId())){
            PlanDetailService planDetailService=((PlanDetailService) ServiceManager.instance().getService("plandetail"));
            PlanDetail planDetail=planDetailService.findById(sign.getPlanDetailId());
            if(planDetail!=null){
                planDetail.setFinishPercentage(planDetail.getFinishPercentage()-1);
                return Db.tx(()->{
                    return sign.update() && planDetail.update();
                });
            }
        }

        return sign.update();
    }

    /**
     * 删除打卡记录及父级的打卡记录
     * @param sign
     * @param parentSign
     * @return
     */
    public boolean removeSign(Sign sign,Sign parentSign){
        return Db.tx(()->{
            return removeSign(sign) && removeSign(parentSign);
        });
    }

    public List<Sign> listSigns(String userId,String planId,String detailId){
        QueryParam param=QueryParam.Builder().equalsTo("creator",userId)
                .equalsTo("plan_id",planId)
                .equalsTo("plan_detail_id",detailId)
                .equalsTo("is_deleted",Constant.MARK_ZERO_STR);
        return list(param);
    }

    /**
     * 该计划事项下是否有打卡记录
     * @param userId
     * @param planId
     * @param detailId
     * @return
     */
    public boolean hasSign(String userId,String planId,String detailId){
        QueryParam param=QueryParam.Builder().equalsTo("creator",userId)
                .equalsTo("plan_id",planId)
                .equalsTo("plan_detail_id",detailId)
                .equalsTo("is_deleted",Constant.MARK_ZERO_STR);
        Sign sign=findFirst(param);
        return sign!=null;
    }

}
