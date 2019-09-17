package com.uisftech.elasticsearchPool.entity.match;

/**
 * 范围匹配
 */
public class RangeMatch extends Match{
    private Object gt; //大于
    private Object gte; //大于等于
    private Object lt; //小于
    private Object lte; //小于等于

    @Override
    public String toString() {
        return "RangeMatch{" +
                "gt=" + gt +
                ", gte=" + gte +
                ", lt=" + lt +
                ", lte=" + lte +
                '}';
    }

    public Object getGt() {
        return gt;
    }

    public void setGt(Object gt) {
        this.gt = gt;
    }

    public Object getGte() {
        return gte;
    }

    public void setGte(Object gte) {
        this.gte = gte;
    }

    public Object getLt() {
        return lt;
    }

    public void setLt(Object lt) {
        this.lt = lt;
    }

    public Object getLte() {
        return lte;
    }

    public void setLte(Object lte) {
        this.lte = lte;
    }
}
