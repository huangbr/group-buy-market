package edu.jnu.infrastructure.adapter.repository;

import com.alibaba.fastjson.JSON;
import edu.jnu.domain.trade.adapter.repository.ITradeReposity;
import edu.jnu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import edu.jnu.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import edu.jnu.domain.trade.model.entity.*;
import edu.jnu.domain.trade.model.valobj.GroupBuyProgressVO;
import edu.jnu.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import edu.jnu.infrastructure.dao.IGroupBuyActivityDao;
import edu.jnu.infrastructure.dao.IGroupBuyOrderDao;
import edu.jnu.infrastructure.dao.IGroupBuyOrderListDao;
import edu.jnu.infrastructure.dao.INotifyTaskDao;
import edu.jnu.infrastructure.dao.po.GroupBuyActivity;
import edu.jnu.infrastructure.dao.po.GroupBuyOrder;
import edu.jnu.infrastructure.dao.po.GroupBuyOrderList;
import edu.jnu.infrastructure.dao.po.NotifyTask;
import edu.jnu.types.common.Constants;
import edu.jnu.types.enums.ActivityStatusEnumVO;
import edu.jnu.types.enums.GroupBuyOrderEnumVO;
import edu.jnu.types.enums.ResponseCode;
import edu.jnu.types.exception.AppException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


@Repository
public class TradeRepository implements ITradeReposity {

    @Resource
    private IGroupBuyOrderDao groupBuyOrderDao;
    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;
    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;
    @Resource
    private INotifyTaskDao notifyTaskDao;


    // 根据外部订单编号查询拼团订单明细
    @Override
    public MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOutTradeNo(outTradeNo);
        GroupBuyOrderList groupBuyOrderListRes = groupBuyOrderListDao.queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderListReq);
        if (null == groupBuyOrderListRes) return null;

        return MarketPayOrderEntity.builder()
                .teamId(groupBuyOrderListRes.getTeamId())
                .orderId(groupBuyOrderListRes.getOrderId())
                .deductionPrice(groupBuyOrderListRes.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.valueOf(groupBuyOrderListRes.getStatus()))
                .build();
    }


    // 查询拼团进度
    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {

        GroupBuyOrder groupBuyOrderRes = groupBuyOrderDao.queryGroupBuyProgress(teamId);
        if (null == groupBuyOrderRes) return null;
        return GroupBuyProgressVO.builder()
                .targetCount(groupBuyOrderRes.getTargetCount())
                .completeCount(groupBuyOrderRes.getCompleteCount())
                .lockCount(groupBuyOrderRes.getLockCount())
                .build();
    }




    // 锁单
    @Transactional(timeout = 500)
    @Override
    public MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate) {
        // 聚合对象信息
        UserEntity userEntity = groupBuyOrderAggregate.getUserEntity();
        PayActivityEntity payActivityEntity = groupBuyOrderAggregate.getPayActivityEntity();
        PayDiscountEntity payDiscountEntity = groupBuyOrderAggregate.getPayDiscountEntity();
        Integer userTakeOrderCount = groupBuyOrderAggregate.getUserTakeOrderCount();

        // 1.更新GrouyBuyOrder表
        // 判断是否有团：teamId为空则新团，teamId不空则老团
        String teamId = payActivityEntity.getTeamId();
        if(StringUtils.isBlank(teamId)){
            // 新团
            teamId = RandomStringUtils.randomNumeric(8); // 替代公司里用的雪花算法UUID

            // 构建拼团订单
            GroupBuyOrder groupBuyOrder = GroupBuyOrder.builder()
                    .teamId(teamId)
                    .activityId(payActivityEntity.getActivityId())
                    .source(payDiscountEntity.getSource())
                    .channel(payDiscountEntity.getChannel())
                    .originalPrice(payDiscountEntity.getOriginalPrice())
                    .deductionPrice(payDiscountEntity.getDeductionPrice())
                    .payPrice(payDiscountEntity.getDeductionPrice())
                    .targetCount(payActivityEntity.getTargetCount())
                    .completeCount(0)
                    .lockCount(1)
                    //.status(0)    默认
                    .build();

            groupBuyOrderDao.insert(groupBuyOrder);
        } else {
            // 更新记录：如果更新记录数为1，则表示更新成功。否则表示拼团已满，抛出溢出
            int updateAddTargetCount = groupBuyOrderDao.updateAddLockCount(teamId);
            if(updateAddTargetCount != 1){
                throw new AppException(ResponseCode.E0005);
            }
        }

        // 2.更新GrouyBuyOrderList表
        String orderId = RandomStringUtils.randomNumeric(12); // 实际公司中一般用雪花算法生成UUID
        GroupBuyOrderList groupBuyOrderList = GroupBuyOrderList.builder()
                .userId(userEntity.getUserId())
                .teamId(teamId)
                .orderId(orderId)
                .activityId(payActivityEntity.getActivityId())
                .startTime(payActivityEntity.getStartTime())
                .endTime(payActivityEntity.getEndTime())
                .goodsId(payDiscountEntity.getGoodsId())
                .source(payDiscountEntity.getSource())
                .channel(payDiscountEntity.getChannel())
                .originalPrice(payDiscountEntity.getOriginalPrice())
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .status(TradeOrderStatusEnumVO.CREATE.getCode())
                .outTradeNo(payDiscountEntity.getOutTradeNo())
                // 构建bizId唯一值；活动id_用户id_参与次数累加
                .bizId(payActivityEntity.getActivityId()+ Constants.UNDERLINE + userEntity.getUserId() + Constants.UNDERLINE + (userTakeOrderCount+1))
                .build();
        try{
            groupBuyOrderListDao.insert(groupBuyOrderList);
        } catch (DuplicateKeyException e){
            throw new AppException(ResponseCode.INDEX_EXCEPTION);
        }

        return MarketPayOrderEntity.builder()
                .orderId(orderId)
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .build();
    }


    @Override
    public GroupBuyActivityEntity queryGroupBuyActivityEntityByActivityId(Long activityId) {
        GroupBuyActivity groupBuyActivity = groupBuyActivityDao.queryGroupBuyActivityByActivityId(activityId);
        return GroupBuyActivityEntity.builder()
                .activityId(groupBuyActivity.getActivityId())
                .activityName(groupBuyActivity.getActivityName())
                .discountId(groupBuyActivity.getDiscountId())
                .groupType(groupBuyActivity.getGroupType())
                .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                .target(groupBuyActivity.getTarget())
                .validTime(groupBuyActivity.getValidTime())
                .status(ActivityStatusEnumVO.valueOf(groupBuyActivity.getStatus()))
                .startTime(groupBuyActivity.getStartTime())
                .endTime(groupBuyActivity.getEndTime())
                .tagId(groupBuyActivity.getTagId())
                .tagScope(groupBuyActivity.getTagScope())
                .build();
    }

    @Override
    public Integer queryOrderCountByActivityId(Long activityId, String userId) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setActivityId(activityId);
        groupBuyOrderListReq.setUserId(userId);
        return groupBuyOrderListDao.queryOrderCountByActivityId(groupBuyOrderListReq);
    }

    @Override
    public GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId) {
        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyTeamByTeamId(teamId);

        return GroupBuyTeamEntity.builder()
                .teamId(teamId)
                .activityId(groupBuyOrder.getActivityId())
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .lockCount(groupBuyOrder.getLockCount())
                .status(GroupBuyOrderEnumVO.valueOf(groupBuyOrder.getStatus()))
                .build();
    }

    @Transactional(timeout = 500)
    @Override
    public void settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate) {

        UserEntity userEntity = groupBuyTeamSettlementAggregate.getUserEntity();
        GroupBuyTeamEntity groupBuyTeamEntity = groupBuyTeamSettlementAggregate.getGroupBuyTeamEntity();
        TradePaySuccessEntity tradePaySuccessEntity = groupBuyTeamSettlementAggregate.getTradePaySuccessEntity();

        // 1. 更新拼团订单明细的订单状态：拼单中 -> 已完成
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userEntity.getUserId());
        groupBuyOrderListReq.setOutTradeNo(tradePaySuccessEntity.getOutTradeNo());
        int updateOrderListStatusCount = groupBuyOrderListDao.updateOrderStatus2COMPLETE(groupBuyOrderListReq);
        if(1 != updateOrderListStatusCount){
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        // 2. 更新拼团达成数量
        int updateAddCount = groupBuyOrderDao.updateAddCompleteCount(groupBuyTeamEntity.getTeamId());
        // 更新成功返回1
        if(1 != updateAddCount){
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }


        // 3. 更新拼团订单的状态：仅当队伍的最后一个用户支付完成时，才更新拼团完成状态，否则只更新该用户对应订单明细中的订单状态
        if(groupBuyTeamEntity.getTargetCount() - groupBuyTeamEntity.getCompleteCount() == 1){
            int updateOrderStatusCount = groupBuyOrderDao.updateOrderStatus2COMPLETE(groupBuyTeamEntity.getTeamId());
            // 更新成功返回1
            if(updateOrderStatusCount != 1){
                throw new AppException(ResponseCode.UPDATE_ZERO);
            }

            // 查询拼团交易完成外部单号列表
            List<String> outTradeNoList = groupBuyOrderListDao.queryGroupBuyCompleteOrderOutTradeNoListByTeamId(groupBuyTeamEntity.getTeamId());

            // 拼团完成写入回调记录
            NotifyTask notifyTask = new NotifyTask();
            notifyTask.setActivityId(groupBuyTeamEntity.getActivityId());
            notifyTask.setTeamId(groupBuyTeamEntity.getTeamId());
            notifyTask.setNotifyUrl("暂无");
            notifyTask.setNotifyCount(0);
            notifyTask.setNotifyStatus(0);
            notifyTask.setParameterJson(JSON.toJSONString(new HashMap<String, Object>(){{
                put("teamId", groupBuyTeamEntity.getTeamId());
                put("outTradeNoList",outTradeNoList);
            }}));
            notifyTaskDao.insert(notifyTask);

        }
    }
}
