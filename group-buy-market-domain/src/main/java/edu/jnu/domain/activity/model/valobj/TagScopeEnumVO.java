package edu.jnu.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 枚举：活动人群标签作用域，共三种状态：1用户不可见不可参与、2用户可见不可参与、3用户可见可参与
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TagScopeEnumVO {
    /**
     * 如果拼团活动默认是可见的（visible == true），那么无论用户是否在人群范围内，活动都应该是可见的;
     * 如果拼团活动默认不可见（visible == false），则只有当用户在人群范围内时，活动才可见。
     * 如果拼团活动默认是可参与的（enable == true），那么无论用户是否在人群范围内，活动都应该是可参与的。
     * 如果拼团活动默认不可参与（enable == false），则只有当用户在人群范围内时，活动才可参与。
     */
    INVISIBLE_DISABLE("1", "用户不可见不可参与"),
    VISIBLE_DISABLE("2", "用户可见不可参与"),
    VISIBLE_ENABLE("3", "用户可见可参与");

    private String code;
    private String desc;


    // 判断是否可见
    public boolean isVisible() {
        return this == VISIBLE_DISABLE || this == VISIBLE_ENABLE;
    }

    // 判断是否可参与
    public boolean isEnable() {
        return this == VISIBLE_ENABLE;
    }

    // 根据 code 获取对应的枚举值
    public static TagScopeEnumVO fromCode(String code) {
        if(StringUtils.isBlank(code)){
            // 没有配置tagScope字段，默认为用户可见可参与
            return VISIBLE_ENABLE;
        }
        for (TagScopeEnumVO scope : values()) {
            if (scope.getCode().equals(code)) {
                return scope;
            }
        }
        throw new IllegalArgumentException("Invalid tagScope code: " + code);
    }


}
