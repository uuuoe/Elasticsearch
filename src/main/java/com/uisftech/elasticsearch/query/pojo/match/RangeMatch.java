package com.uisftech.elasticsearch.query.pojo.match;

/**
 * 范围匹配
 */
public class RangeMatch extends Match{
    private Object from;
    private Object to;
    /**
     * true表示 >=, false表示 >
     */
    private Boolean includeLower;
    /**
     * true表示<=, false表示<
     */
    private Boolean includeUpper;
    private MatchType type = MatchType.RANGE;

    public RangeMatch(){
        includeUpper = true;
        includeLower = true;
    }

    @Override
    public String toString() {
        return "RangeMatch{" +
                "from=" + from +
                ", to=" + to +
                ", includeLower=" + includeLower +
                ", includeUpper=" + includeUpper +
                ", type=" + type +
                '}';
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public Object getTo() {
        return to;
    }

    public void setTo(Object to) {
        this.to = to;
    }

    public Boolean isIncludeLower() {
        return includeLower;
    }

    public void setIncludeLower(Boolean includeLower) {
        this.includeLower = includeLower;
    }

    public Boolean isIncludeUpper() {
        return includeUpper;
    }

    public void setIncludeUpper(Boolean includeUpper) {
        this.includeUpper = includeUpper;
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
