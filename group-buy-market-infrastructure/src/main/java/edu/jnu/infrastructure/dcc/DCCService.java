package edu.jnu.infrastructure.dcc;

import edu.jnu.types.annotations.DCCValue;
import org.springframework.stereotype.Service;

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

}
