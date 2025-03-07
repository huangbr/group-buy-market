package edu.jnu.domain.activity.model.valobj;

import edu.jnu.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 拼团活动折扣配置值对象
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyActivityDiscountVO {
    /** 活动ID */
    private Long activityId;

    /** 活动名称 */
    private String activityName;

    /** 折扣ID */
    private GroupBuyDiscount groupBuyDiscount;

    /** 拼团方式（0自动成团、1达成目标拼团） */
    private Integer groupType;

    /** 拼团次数限制 */
    private Integer takeLimitCount;

    /** 拼团目标 */
    private Integer target;

    /** 拼团时长（分钟） */
    private Integer validTime;

    /** 活动状态（0创建、1生效、2过期、3废弃） */
    private Integer status;

    /** 活动开始时间 */
    private Date startTime;

    /** 活动结束时间 */
    private Date endTime;

    /** 人群标签规则标识 */
    private String tagId;

    /** 人群标签规则范围（三种状态码：1不可见不可参与、2可见不可参与、3可见可参与） */
    private String tagScope;


    /** 拼团折扣 */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GroupBuyDiscount {
        /** 折扣ID */
        private String discountId;

        /** 折扣标题 */
        private String discountName;

        /** 折扣描述 */
        private String discountDesc;

        /** 折扣类型（0:base、1:tag） */
        private DisconutTypeEnum discountType;

        /** 营销优惠计划（ZJ:直减、MJ:满减、N元购、ZK:打折） */
        private String marketPlan;

        /** 营销优惠表达式 */
        private String marketExpr;

        /** 人群标签，特定优惠限定 */
        private String tagId;
    }


    // 改进：查询活动的可见性、可参与性



    //    /** 活动可见性查询 */
//    public boolean isVisible(){
//        // tagScope字段为空，表示没有人群标签限制，即所有用户对活动可见
//        if(StringUtils.isBlank(this.tagScope)){
//            return TagScopeEnumVO.VISIBLE.isAllow();
//        }
//        String[] split = this.tagScope.split(Constants.SPLIT);
//        if(split.length ==2 && split[0].equals("1")){
//            // 如果配置了tagScope，且配置了可见性字段 - 1，则默认设置为不可见
//            return TagScopeEnumVO.VISIBLE.isRefuse();
//        }
//
//        return TagScopeEnumVO.VISIBLE.isAllow();
//    }
//
//    /** 活动可参与性查询 */
//    public boolean isEnable(){
//        // tagScope字段为空，表示没有人群标签限制，即所有用户可参与此活动
//        if(StringUtils.isBlank(this.tagScope)){
//            return TagScopeEnumVO.ENABLE.isAllow();
//        }
//        String[] split = this.tagScope.split(Constants.SPLIT);
//        if(split.length ==2 && split[1].equals("2")){
//            // 如果配置了tagScope，且配置了可参与性字段 - 2，则默认设置为不可参与
//            return TagScopeEnumVO.ENABLE.isRefuse();
//        }
//
//        return TagScopeEnumVO.ENABLE.isAllow();
//    }
}
