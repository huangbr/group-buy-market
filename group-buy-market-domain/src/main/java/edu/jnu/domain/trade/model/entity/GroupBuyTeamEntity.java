package edu.jnu.domain.trade.model.entity;

import edu.jnu.types.enums.GroupBuyOrderEnumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 拼团组队实体对象
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyTeamEntity {
    /** 拼团组队Id */
    private String teamId;
    /** 活动Id */
    private Long activityId;
    /** 组队目标人数 */
    private Integer targetCount;
    /** 组队已完成人数 */
    private Integer completeCount;
    /** 组队锁定人数 */
    private Integer lockCount;
    /** 拼团活动状态枚举 （0-拼单中、1-完成、2-失败） */
    private GroupBuyOrderEnumVO status;
    /** 拼团开始时间：拼团队伍的创建时间 */
    private Date validStartTime;
    /** 拼团结束时间：拼团队伍的创建时间 + 拼团队伍的有效时长 */
    private Date validEndTime;
}
