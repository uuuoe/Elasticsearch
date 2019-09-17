package com.uisftech.elasticsearch.query;

import com.uisftech.elasticsearch.query.pojo.ElasticsearchQueryData;
import com.uisftech.elasticsearch.query.pojo.Param;

import java.util.Map;

/**
 * elasticsearch查询接口
 */
public interface Query {

    /**
     * 构建查询参数，查询elasticsearch
     * @param param
     * @return ElasticsearchQueryData
     */
    ElasticsearchQueryData query(Param param);

    /**
     * 构建查询参数，查询elasticsearch
     * @param paramJson
     * @return
     */
    ElasticsearchQueryData query(String paramJson);

    /**
     * 构建查询参数，查询elasticsearch
     * @param paramMap
     * @return
     */
    Map<String,Object> query(Map<String,Object> paramMap);

    /**
     * 构建查询参数，查询elasticsearch
     * @param param
     * @return
     */
    String queryToStirng(Param param);

    /**
     * 构建查询参数，查询elasticsearch
     * @param paramJson
     * @return
     */
    String queryToString(String paramJson);




}
