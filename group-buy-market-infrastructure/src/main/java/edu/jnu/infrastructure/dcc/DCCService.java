package edu.jnu.infrastructure.dcc;

import edu.jnu.types.annotations.DCCValue;
import edu.jnu.types.common.Constants;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 动态配置服务
 */

@Service
public class DCCService {

    /**
     * 降级开关：0关闭，1开启
     */
    @DCCValue("downgradeSwitch:0")
    private String downgradeSwitch;

    /**
     * 切量比率：允许cutRange%的流量通过
     */
    @DCCValue("cutRange:100")
    private String cutRange;

    /**
     * SC黑名单拦截：拦截source为s02, channel为c02的渠道
     */
    @DCCValue("scBlacklist:s02c02")
    private String scBlackList;

    // 是否降级拦截
    public boolean isDowngradeSwitch(){
        return "1".equals(downgradeSwitch);
    }

    // 是否在切量范围内：在范围内的用户无需拦截
    public boolean isCutRange(String userId){
        // 计算哈希码的绝对值
        int hashCode = Math.abs(userId.hashCode());
        // 获取最后两位
        int lastTowDigital = hashCode%100;
        // 判断是否在切量范围内
        if(lastTowDigital<=Integer.parseInt(cutRange)){
            return true;
        }
        return false;
    }

    // 判断黑名单拦截渠道：True拦截，False放行
    public boolean isSCBlackIntercept(String source, String channel){
        List<String> list = Arrays.asList(scBlackList.split(Constants.SPLIT));
        return list.contains(source+channel);
    }
}
