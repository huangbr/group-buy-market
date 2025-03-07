package edu.jnu.config;

import edu.jnu.types.annotations.DCCValue;
import edu.jnu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.client.RedisClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态配置管理的工厂：基于Redis实现动态配置中心，会自动的完成属性信息的填充和动态变更操作
 *
 * DCCValueBeanFactory继承了BeanPostProcessor，重写的postProcessAfterInitialization方法会在每次启动程序后对已经完成初始化的Bean进行后置处理
 */

@Slf4j
@Configuration
public class DCCValueBeanFactory implements BeanPostProcessor {

    private static final String BASE_CONFIG_PATH = "gruop_buy_market_dcc_";

    private final Map<String, Object> dccObjGroup = new HashMap<>();

    private final RedissonClient redissonClient;

    @Autowired
    public DCCValueBeanFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    // 对已经完成初始化的Bean进行后置处理
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 注意：若增加AOP代理后，bean.getClass()得到的只是代理类，而真正的目标类需要通过AopUtils、AopProxyUtil去活得
        Class<?> targetBeanClass = bean.getClass();
        Object targetBeanObject = bean;
        if(AopUtils.isAopProxy(bean)){
            targetBeanClass = AopUtils.getTargetClass(bean); // 从代理类中获得目标类
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean); // 根据目标类获得目标类对象
        }

        Field[] fields = targetBeanClass.getDeclaredFields();
        for(Field field: fields){

            if(!field.isAnnotationPresent(DCCValue.class)){
                continue;
            }
            // 过滤出@DCCValue标记的属性
            DCCValue dccValue = field.getAnnotation(DCCValue.class);

            // value格式为 "cutRange:100"
            String value = dccValue.value();
            if(StringUtils.isBlank(value)){
                throw new RuntimeException(field.getName() + " @DCCValue is not config value config case 「isSwitch/isSwitch:1」");
            }

            String[] splits = value.split(":");
            String key = BASE_CONFIG_PATH.concat(splits[0]);
            String defaultValue = splits.length==2?splits[1]:null;

            // 设置值
            String setValue = defaultValue;

            try{
                if(StringUtils.isBlank(defaultValue)){
                    throw new RuntimeException("dcc config error " + key + " is not null - 请配置默认值！");
                }

                // Redis操作: 判断key是否存在，不存在则创建，存在则获取最新值
                RBucket<String> bucket = redissonClient.getBucket(key);
                if(!bucket.isExists()){
                    bucket.set(defaultValue);
                } else {
                    setValue = bucket.get();
                }

                // 反射更新配置:此处targetBeanObject具有@DCCValue注解的对象，这行代码是设置targetBeanObject对象的key属性值为value
                field.setAccessible(true);
                field.set(targetBeanObject, setValue);
                field.setAccessible(false);

            }catch (Exception e){
                throw new RuntimeException(e);
            }

            dccObjGroup.put(key, targetBeanObject);
        }
        return bean;
    }



    // 订阅者：在Spring容器创建bean后就一直监听着topic
    @Bean("dccTopic")
    public RTopic dccRedisTopicListener(RedissonClient redissonClient){

        // 订阅主题
        RTopic dccTopic = redissonClient.getTopic("group_buy_market_dcc");

        // 添加监听器
        dccTopic.addListener(String.class, new MessageListener<String>() {
            // 创建一个实现了MessageListener接口的匿名内部类实例
            @Override
            public void onMessage(CharSequence charSequence, String s) {
                String[] splits = s.split(":");

                // 获取值
                String attribute = splits[0];
                String key = BASE_CONFIG_PATH.concat(attribute);
                String value = splits[1];

                // 设置值
                RBucket<String> bucket = redissonClient.getBucket(key);
                if(!bucket.isExists()) return;
                bucket.set(value);

                Object objBean = dccObjGroup.get(key);
                Object targetBeanObject = objBean;
                if(objBean == null) return;

                // 检查objBean是否为代理对象
                Class<?> objBeanClass = objBean.getClass();
                if(AopUtils.isAopProxy(objBeanClass)){
                    // 获取代理对象的目标对象
                    objBeanClass = AopUtils.getTargetClass(objBean);
                    targetBeanObject = AopProxyUtils.getSingletonTarget(objBean);
                }

                try{
                    // 1. getDeclaredField 方法用于获取指定类中声明的所有字段，包括私有字段、受保护字段和公共字段。
                    // 2. getField 方法用于获取指定类中的公共字段，即只能获取到公共访问修饰符（public）的字段。
                    Field field = objBeanClass.getDeclaredField(attribute);
                    field.setAccessible(true);
                    field.set(targetBeanObject,value);

                    log.info("DCC 节点监听，动态设置值 key:{} value:{}",key,value);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return dccTopic;
    }



}
