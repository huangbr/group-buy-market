package edu.jnu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 营销商品实体信息
 */


@Data  // 生成常见的 getter、setter、toString、equals、hashCode 方法
@Builder // 用于以链式调用的方式创建对象
@AllArgsConstructor // 自动生成一个包含所有字段的构造函数
@NoArgsConstructor // 自动生成一个无参构造函数
public class MarketProductEntity {
    /** 活动ID */
    private Long activityId;
    /** 用户ID */
    private String userId;
    /** 商品ID */
    private String goodsId;
    /** 渠道，例如APP、WEB */
    private String source;
    /** 来源，例如WeChat、Alipay */
    private String channel;

}