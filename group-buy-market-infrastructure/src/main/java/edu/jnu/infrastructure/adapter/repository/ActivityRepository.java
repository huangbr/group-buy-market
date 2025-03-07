package edu.jnu.infrastructure.adapter.repository;

import com.alibaba.fastjson.JSON;
import edu.jnu.domain.activity.adapter.repository.IActivityRepository;
import edu.jnu.domain.activity.model.valobj.DisconutTypeEnum;
import edu.jnu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import edu.jnu.domain.activity.model.valobj.SCSkuActivityVO;
import edu.jnu.domain.activity.model.valobj.SkuVO;
import edu.jnu.infrastructure.dao.IGroupBuyActivityDao;
import edu.jnu.infrastructure.dao.IGroupBuyDiscountDao;
import edu.jnu.infrastructure.dao.ISCSkuActivityDao;
import edu.jnu.infrastructure.dao.ISkuDao;
import edu.jnu.infrastructure.dao.po.GroupBuyActivity;
import edu.jnu.infrastructure.dao.po.GroupBuyDiscount;
import edu.jnu.infrastructure.dao.po.SCSkuActivity;
import edu.jnu.infrastructure.dao.po.Sku;
import edu.jnu.infrastructure.dcc.DCCService;
import edu.jnu.infrastructure.redis.IRedisService;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.security.acl.Group;


/**
 * 活动仓储：实现类中进行dao操作
 */

@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;
    @Resource
    private IGroupBuyDiscountDao groupBuyDiscountDao;
    @Resource
    private ISkuDao skuDao;
    @Resource
    private ISCSkuActivityDao scSKuActivityDao;
    @Resource
    private IRedisService redisService;
    @Resource
    private DCCService dccService;


    @Override
    public GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId) {
        // 根据source、channel查询配置中最新的1个有效的活动
        GroupBuyActivity groupBuyActivityRes = groupBuyActivityDao.queryValidGroupBuyActivity(activityId);
        if(groupBuyActivityRes == null) return null;

        String discountId = groupBuyActivityRes.getDiscountId();

        // 根据折扣配置Id查找该商品的折扣
        GroupBuyDiscount groupBuyDiscountRes = groupBuyDiscountDao.queryGroupBuyActivityDiscountByDiscountId(discountId);
        if(groupBuyDiscountRes == null) return null;
        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount = GroupBuyActivityDiscountVO.GroupBuyDiscount.builder()
                .discountName(groupBuyDiscountRes.getDiscountName())
                .discountDesc(groupBuyDiscountRes.getDiscountDesc())
                .discountType(DisconutTypeEnum.get(groupBuyDiscountRes.getDiscountType()))
                .marketPlan(groupBuyDiscountRes.getMarketPlan())
                .marketExpr(groupBuyDiscountRes.getMarketExpr())
                .tagId(groupBuyDiscountRes.getTagId())
                .build();

        // 返回对应的VO
        return GroupBuyActivityDiscountVO.builder()
                .activityId(groupBuyActivityRes.getActivityId())
                .activityName(groupBuyActivityRes.getActivityName())
                .groupBuyDiscount(groupBuyDiscount)
                .groupType(groupBuyActivityRes.getGroupType())
                .takeLimitCount(groupBuyActivityRes.getTakeLimitCount())
                .target(groupBuyActivityRes.getTarget())
                .validTime(groupBuyActivityRes.getValidTime())
                .status(groupBuyActivityRes.getStatus())
                .startTime(groupBuyActivityRes.getStartTime())
                .endTime(groupBuyActivityRes.getEndTime())
                .tagId(groupBuyActivityRes.getTagId())
                .tagScope(groupBuyActivityRes.getTagScope())
                .build();

    }

    @Override
    public SkuVO querySkuByGoodsId(String goodsId) {
        // 根据商品Id查找商品信息
        Sku sku = skuDao.querySkuByGoodsId(goodsId);
        if (null == sku) return null;

        // 返回对应的VO
        return SkuVO.builder()
                .goodsId(sku.getGoodsId())
                .goodsName(sku.getGoodsName())
                .originalPrice(sku.getOriginalPrice())
                .build();
    }

    @Override
    public SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId) {
        SCSkuActivity scSkuActivityReq = new SCSkuActivity();
        scSkuActivityReq.setSource(source);
        scSkuActivityReq.setChannel(channel);
        scSkuActivityReq.setGoodsId(goodsId);
        SCSkuActivity scSkuActivityRes = scSKuActivityDao.querySCSkuActivityBySCGoodsId(scSkuActivityReq);
        if (null == scSkuActivityRes) return null;

        return SCSkuActivityVO.builder()
                .source(scSkuActivityRes.getSource())
                .chanel(scSkuActivityRes.getChannel())
                .activityId(scSkuActivityRes.getActivityId())
                .goodsId(scSkuActivityRes.getGoodsId())
                .build();
    }

    @Override
    public boolean isTagCrowdRange(String tagId, String userId){
        RBitSet bitSet = redisService.getBitSet(tagId);
        if(!bitSet.isExists()){
            // 该tag不产生限制
            return true;
        }
        return bitSet.get(redisService.getIndexFromUserId(userId));
    }

    @Override
    public boolean downgradeSwitch() {
        return dccService.isDowngradeSwitch();
    }

    @Override
    public boolean cutRange(String userId) {
        return dccService.isCutRange(userId);
    }


}
