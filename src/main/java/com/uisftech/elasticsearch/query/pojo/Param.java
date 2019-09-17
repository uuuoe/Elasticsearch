package com.uisftech.elasticsearch.query.pojo;


import com.uisftech.elasticsearch.query.pojo.match.Match;

import java.util.Arrays;
import java.util.List;

public class Param {
    private String index; //索引名称
    private String type; //类型
    private int page = 1;//页数
    private int size = 10;//页大小
    private String sortField;//排序字段
    private String sortOrder = "desc"; //排序
    private String[] fieldNames;//返回字段集合
    private List<Match> matches;//匹配规则
    private String keyword;//关键字。适用于模糊查询
    private String[] fuzzyFields;//模糊查询字段，适用于模糊查询

    public Param(){}

    public Param(String index,String type){
        this.index = index;
        this.type = type;
    }

    @Override
    public String toString() {
        return "param{" +
                "index='" + index + '\'' +
                ",type='" + type + '\'' +
                ",page=" + page +
                ",size=" + size +
                ",sortField='" + sortField + '\'' +
                ",sortOrder='" + sortOrder + '\'' +
                ",fieldNames=" + Arrays.toString(fieldNames) +
                ",matches=" + matches +
                ",keyword='" + keyword +'\'' +
                ",fuzzyFields=" + Arrays.toString(fuzzyFields) +
                '}';
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String[] getFuzzyFields() {
        return fuzzyFields;
    }

    public void setFuzzyFields(String[] fuzzyFields) {
        this.fuzzyFields = fuzzyFields;
    }
}
