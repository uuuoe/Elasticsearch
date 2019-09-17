package com.uisftech.elasticsearch.query.pojo.match;

import java.util.Arrays;

/**
 * 匹配父类
 */
public class Match {
    private String fieldName;//字段名称
    private Object[] values;//字段值多值数组
    private Object value;//字段值
    private MatchType type = MatchType.MATCH;//类型

    @Override
    public String toString() {
        return "Match{" +
                "fieldName='" + fieldName + '\'' +
                ", values=" + Arrays.toString(values) +
                ", value=" + value +
                ", type=" + type +
                '}';
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public MatchType getType() {
        return type;
    }

    public void setType(MatchType type) {
        this.type = type;
    }
}
