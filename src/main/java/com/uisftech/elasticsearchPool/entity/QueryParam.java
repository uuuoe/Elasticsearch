package com.uisftech.elasticsearchPool.entity;

import com.uisftech.elasticsearchPool.entity.match.BoostMatch;
import com.uisftech.elasticsearchPool.entity.match.RangeMatch;
import com.uisftech.elasticsearchPool.entity.match.TermsMatch;
import com.uisftech.elasticsearchPool.entity.match.WildcardMatch;
import sun.java2d.loops.TransformHelper;

import java.util.Arrays;
import java.util.List;

/**
 * 查询参数类
 */
public class QueryParam {
    private String index = "party_index";//索引名称
    private String type = "party";//类型
    private int page = 1;//页数
    private int size = 10;//页大小
    private String sortField;//排序字段
    private String sortOrder = "desc";//排序
    private String[] fieldNames;//返回字段集合

    //添加字段
    private List<TermsMatch> termsMatch; //精确匹配
    private List<WildcardMatch> wildcardMatch; //模糊匹配
    private List<RangeMatch> rangeMatch;//范围匹配
    private List<BoostMatch> boostMatch; //加权匹配

    @Override
    public String toString() {
        return "QueryParam{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sortField='" + sortField + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", fieldNames=" + Arrays.toString(fieldNames) +
                ", termsMatch=" + termsMatch +
                ", wildcardMatch=" + wildcardMatch +
                ", rangeMatch=" + rangeMatch +
                ", boostMatch=" + boostMatch +
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

    public List<TermsMatch> getTermsMatch() {
        return termsMatch;
    }

    public void setTermsMatch(List<TermsMatch> termsMatch) {
        this.termsMatch = termsMatch;
    }

    public List<WildcardMatch> getWildcardMatch() {
        return wildcardMatch;
    }

    public void setWildcardMatch(List<WildcardMatch> wildcardMatch) {
        this.wildcardMatch = wildcardMatch;
    }

    public List<RangeMatch> getRangeMatch() {
        return rangeMatch;
    }

    public void setRangeMatch(List<RangeMatch> rangeMatch) {
        this.rangeMatch = rangeMatch;
    }

    public List<BoostMatch> getBoostMatch() {
        return boostMatch;
    }

    public void setBoostMatch(List<BoostMatch> boostMatch) {
        this.boostMatch = boostMatch;
    }
}
