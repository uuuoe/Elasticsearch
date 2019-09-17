package com.uisftech.elasticsearch.query.pojo.match;

/**
 * 加权匹配
 */
public class BoostMatch extends Match{
    /**
     * 加权值 默认1
     */
    private Float boost;
    private MatchType type = MatchType.BOOST;

    public BoostMatch(){
        boost = 1F;
    }

    @Override
    public String toString() {
        return "BoostMatch{" +
                "boost=" + boost +
                ", type=" + type +
                '}';
    }

    public Float getBoost() {
        return boost;
    }

    public void setBoost(Float boost) {
        this.boost = boost;
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
