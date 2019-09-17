package com.uisftech.elasticsearch.query.pojo.match;

/**
 * 模糊匹配（通配符匹配）
 */
public class WildcardMatch extends Match{
    private Boolean must = Boolean.FALSE;
    private MatchType type = MatchType.WILDCARD;

    @Override
    public String toString() {
        return "WildcardMatch{" + "must=" +must + ",type=" +type +'}';
    }

    public Boolean getMust() {
        return must;
    }

    public void setMust(Boolean must) {
        this.must = must;
    }

    @Override
    public MatchType getType() {
        return type;
    }

    @Override
    public void setType(MatchType type) {
        this.type = type;
    }
}
