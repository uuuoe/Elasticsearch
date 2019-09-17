package com.uisftech.elasticsearch.query.pojo;

import java.util.List;
import java.util.Map;

public class ElasticsearchQueryData {
    private int page;//当前页
    private int size;//页大小
    private long totlePage;//总页数
    private long totleNum;//总条数
    private List<Map<String,Object>> data;//数据

    @Override
    public String toString() {
        return "ElasticsearchQueryData{" +
                "page=" + page +
                ", size=" + size +
                ", totlePage=" + totlePage +
                ", totleNum=" + totleNum +
                ", data=" + data +
                '}';
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

    public long getTotlePage() {
        return totlePage;
    }

    public void setTotlePage(long totlePage) {
        this.totlePage = totlePage;
    }

    public long getTotleNum() {
        return totleNum;
    }

    public void setTotleNum(long totleNum) {
        this.totleNum = totleNum;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
