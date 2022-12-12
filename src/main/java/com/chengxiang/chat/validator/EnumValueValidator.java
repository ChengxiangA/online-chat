package com.chengxiang.chat.validator;

import com.chengxiang.chat.annotition.EnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author 程祥
 * @date 2022/11/28 10:35
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue,Object> {
    private String[] strValues;

    private int[] intValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value instanceof String) {
            for (String s : strValues) {
                if(s.equals(value)) {
                    return true;
                }
            }
        } else if(value instanceof Integer) {
            for (int s : intValues) {
                if(s == ((Integer) value).intValue()) {
                    return true;
                }
            }
        }
        return false;
    }
}
