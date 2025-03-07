package edu.jnu.trigger.http;

import edu.jnu.api.IDCCService;
import edu.jnu.api.response.Response;
import edu.jnu.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * DCC控制器：当调用DCCController时，会触发Redis的发布/订阅，动态值的变更，以此把类上的属性的值做变更。
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/dcc/")
public class DCCController implements IDCCService {

    @Resource
    private RTopic dccTopic;

    @RequestMapping(value = "update_config",method = RequestMethod.GET)
    @Override
    public Response<Boolean> updateConfig(String key, String value) {
        try {
            log.info("DCC 动态配置变更 Key:{} Value:{}", key, value);
            // 发布者-发布消息
            dccTopic.publish(key+":"+value);
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();

        }catch (Exception e){
            log.error("DCC 动态配置变更失败 Key:{} Value:{}", key, value);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }
}
