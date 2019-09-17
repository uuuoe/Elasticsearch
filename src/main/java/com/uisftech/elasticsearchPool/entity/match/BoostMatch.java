package com.uisftech.elasticsearchPool.entity.match;

/**
 * 加权匹配
 */
public class BoostMatch extends Match{
    private  Float boost = 1F;//加权值，默认为1

    @Override
    public String toString() {
        return "BoostMatch{" +
                "boost=" + boost +
                '}';
    }

    public Float getBoost() {
        return boost;
    }

    public void setBoost(Float boost) {
        this.boost = boost;
    }
}
