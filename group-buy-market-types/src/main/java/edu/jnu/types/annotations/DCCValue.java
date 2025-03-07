package edu.jnu.types.annotations;

import java.lang.annotation.*;

/**
 * DCC(Dynamic Configuration Center，动态配置中心)：自定义注解，实现降级、切量
 */

@Retention(RetentionPolicy.RUNTIME)  // 运行时注解
@Target(ElementType.FIELD) // 只能作用在属性上
@Documented
public @interface DCCValue {
    String value() default  "";
}
