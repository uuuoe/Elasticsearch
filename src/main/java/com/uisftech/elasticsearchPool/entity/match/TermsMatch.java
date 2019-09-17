package com.uisftech.elasticsearchPool.entity.match;

/**
 * 精确匹配
 */
public class TermsMatch extends Match{
    private Boolean must = Boolean.TRUE;//yes or no 默认是yes

    @Override
    public String toString() {
        return "TermsMatch{" +
                "must=" + must +
                '}';
    }

    public Boolean getMust() {
        return must;
    }

    public void setMust(Boolean must) {
        this.must = must;
    }
}
