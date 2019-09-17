package com.uisftech.elasticsearch.query.pojo.match;



/**
 * 精确匹配（术语匹配）
 */
public class TermsMatch extends Match{
    private Boolean must = Boolean.TRUE;  //yes or no 默认yes
    private Boolean should = Boolean.FALSE; //true:or,false:and
    private MatchType type = MatchType.TERMS;

    @Override
    public String toString() {
        return "TermsMatch{" +
                "must=" + must +
                ", should=" + should +
                ", type=" + type +
                '}';
    }

    public Boolean getMust() {
        return must;
    }

    public void setMust(Boolean must) {
        this.must = must;
    }

    public Boolean getShould() {
        return should;
    }

    public void setShould(Boolean should) {
        this.should = should;
    }

    public MatchType getType() {
        return type;
    }

    public void setType(MatchType type) {
        this.type = type;
    }
}
