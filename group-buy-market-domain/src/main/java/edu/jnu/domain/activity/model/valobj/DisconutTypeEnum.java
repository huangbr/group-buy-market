package edu.jnu.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 枚举：折扣优惠类型
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DisconutTypeEnum {
    
    BASE(0,"基础优惠"),
    TAG(1,"人群标签");

    private Integer code;
    private String info;

    public static DisconutTypeEnum get(Integer code){
        switch (code){
            case 0:
                return BASE;
            case 1:
                return TAG;
            default:
                throw new RuntimeException("err code!");
        }
    }





}
