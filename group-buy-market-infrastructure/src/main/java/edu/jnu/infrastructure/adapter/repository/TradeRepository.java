package edu.jnu.infrastructure.adapter.repository;

import edu.jnu.domain.trade.adapter.repository.ITradeReposity;
import edu.jnu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import edu.jnu.domain.trade.model.entity.MarketPayOrderEntity;
import edu.jnu.domain.trade.model.entity.PayActivityEntity;
import edu.jnu.domain.trade.model.entity.PayDiscountEntity;
import edu.jnu.domain.trade.model.entity.UserEntity;
import edu.jnu.domain.trade.model.valobj.GroupBuyProgressVO;
import edu.jnu.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import edu.jnu.infrastructure.dao.IGroupBuyOrderDao;
import edu.jnu.infrastructure.dao.IGroupBuyOrderListDao;
import edu.jnu.infrastructure.dao.po.GroupBuyOrder;
import edu.jnu.infrastructure.dao.po.GroupBuyOrderList;
import edu.jnu.types.enums.ResponseCode;
import edu.jnu.types.exception.AppException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Repository
public class TradeRepository implements ITradeReposity {

    @Resource
    private IGroupBuyOrderDao groupBuyOrderDao;

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;


    // 根据外部订单编号查询拼团订单明细
    @Override
    public MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOutTradeNo(outTradeNo);
        GroupBuyOrderList groupBuyOrderListRes = groupBuyOrderListDao.queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderListReq);
        if (null == groupBuyOrderListRes) return null;

        return MarketPayOrderEntity.builder()
                .orderId(groupBuyOrderListRes.getOrderId())
                .deductionPrice(groupBuyOrderListRes.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.valueOf(groupBuyOrderListReq.getStatus()))
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

}
