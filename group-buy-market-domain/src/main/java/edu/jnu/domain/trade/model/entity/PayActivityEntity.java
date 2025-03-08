package edu.jnu.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 支付活动实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayActivityEntity {
    /** 拼团队伍ID */
    private String teamId;
    /** 拼团活动ID */
    private Long activityId;
    /** 拼团活动名称 */
    private String activityName;
    /** 拼团开始时间 */
    private Date startTime;
    /** 拼团结束时间 */
    private Date endTime;
    /** 目标数量 */
    private Integer targetCount;

}
