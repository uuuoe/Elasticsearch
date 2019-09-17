package com.uisftech.elasticsearchPool.query;

import com.uisftech.elasticsearchPool.client.QueryClient;
import com.uisftech.elasticsearchPool.entity.QueryConfig;
import com.uisftech.elasticsearchPool.entity.QueryParam;
import com.uisftech.elasticsearchPool.entity.match.BoostMatch;
import com.uisftech.elasticsearchPool.entity.match.RangeMatch;
import com.uisftech.elasticsearchPool.entity.match.TermsMatch;
import com.uisftech.elasticsearchPool.entity.match.WildcardMatch;
import com.uisftech.elasticsearchPool.util.QueryClientFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 查询工具类
 */
public class QueryData {

    /**
     * log
     */
    private static final Logger log = Logger.getLogger(QueryData.class);

    /**
     * elasticsearch连接池
     */
    private static ObjectPool<QueryClient> pool;

    public QueryData() {
    }

    static {
        //创建queryConfig
        QueryConfig config = new QueryConfig();

        //加载elasticsearch.properties
        Properties properties = new Properties();
        try {
            //设置elasticsearch连接配置
            properties.load(QueryClientFactory.class.getClassLoader().getResourceAsStream("elasticsearch.properties"));
            config.setClusterName(properties.getProperty("elasticsearch.cluster_name", "elasticsearch"));
            config.setClusterNodes(properties.getProperty("elasticsearch.cluster_nodes", "127.0.0.1:9300"));
            config.setClusterPort(properties.getProperty("elasticsearch.cluster_port", "9300"));
            config.setTransportSniff(Boolean.parseBoolean(properties.getProperty("client.transport.sniff", "true")));
            config.setIgnoreClusterName(Boolean.parseBoolean(properties.getProperty("client.transport.ignore_cluster_name", "false")));
            config.setPingTimeout(properties.getProperty("client.transport.ping_timeout", "5s"));
            config.setSamplerInterval(properties.getProperty("client.transport.nodes_sampler_interva", "5s"));

            //设置连接池配置
            config.setMaxTotal(Integer.parseInt(properties.getProperty("pool.maxTotal", "8")));//设置连接池最大连接数
            config.setMaxWaitMillis(Long.parseLong(properties.
                    getProperty("pool.maxWaitMillis", "-1")));//设置连接最大等待时间，单位毫秒

        } catch (IOException e) {
            log.info("没有找到elasticsearch.properties文件，加载默认配置");
        }

        //创建连接池
        pool = new GenericObjectPool<QueryClient>(new QueryClientFactory(config), config);
    }

    public static ObjectPool<QueryClient> getPool() {
        return pool;
    }

    /**
     * 查询es索引静态方法
     * 1.支持多字段模糊查询
     * 2.支持多字段精确查询
     * 3.支持多字段精确不包含查询
     * 4.支持加权查询
     * 5.支持范围查询
     * 6.支持分页
     * 7.支持指定返回字段
     */
    public static Map<String, Object> queryData(QueryParam param) {
        Map<String, Object> result = null; //返回结果集，包含数据集data，总条数totleNum

        if (null == param) {
            throw new NullPointerException("es查询获得的参数为空，请创建QueryParam对象");
        } else {
            result = queryDataForGeneral(param);
        }
        return result;
    }

    /**
     * 普通查询
     *
     * @return
     */
    private static Map<String, Object> queryDataForGeneral(QueryParam param) {
        //分析参数，构建build,获取数据
        Map<String, Object> result = null; //结果集
        List<Map<String, Object>> data = null; //数据
        long totleNum = 0; //总条数
        QueryClient client = null; //es查询客户端
        SearchRequestBuilder builder = null; //es search build
        try {
            client = pool.borrowObject();

            //设置索引名称
            if (isNull(param.getIndex())) {
                log.error("没找到索引名称，请检查参数！");
                return result;
            } else {
                builder = client.prepareSearch(param.getIndex());
            }

            //设置类型
            if (!isNull(param.getType())) {
                builder.setTypes(param.getType());
            }

            //设置查询条件
            builder.setQuery(createBuilder(param));

            //设置排序
            if (!isNull(param.getSortField())) {
                if (isNull(param.getSortOrder()) && param.getSortOrder().equalsIgnoreCase("asc")) {
                    builder.addSort(param.getSortField(), SortOrder.ASC);
                } else {
                    builder.addSort(param.getSortField(), SortOrder.DESC);
                }
            }

            //设置分页，默认是1页10条
            if (param.getPage() <= 0) {
                param.setPage(1);
            }

            if (param.getSize() <= 0) {
                param.setSize(10);
            }

            builder.setFrom((param.getPage() - 1) * param.getSize()).setSize(param.getSize());

            //设置返回字段
            if (null != param.getFieldNames() && param.getFieldNames().length > 0) {
                builder.setFetchSource(param.getFieldNames(), null);
            }

            //获取结果集
            SearchResponse scrollResp = builder.get();
            if (null != scrollResp) {
                data = new ArrayList<Map<String, Object>>();
                for (SearchHit hit : scrollResp.getHits().getHits()) {
                    data.add(hit.getSourceAsMap());
                }

                totleNum = scrollResp.getHits().getTotalHits();

                result = new HashMap<String, Object>();
                result.put("data", data);
                result.put("totleNum", totleNum);
            }

        } catch (Exception e) {
            log.error("创建链接失败!", e);
        } finally {
            if (null != client) {
                //关闭连接
                log.info("brrow a connection:" + client + "active connection:" + pool.getNumActive());
                try {
                    pool.returnObject(client);
                } catch (Exception e) {
                    log.error("关闭连接失败！,e");
                }
            }
        }
        return result;
    }

    /**
     * 构建es build对象
     *
     * @param param
     * @return
     */
    private static QueryBuilder createBuilder(QueryParam param) {

        //创建bool query对象，封装精确查询，模糊查询，范围查询，加权查询
        BoolQueryBuilder boolQueryBuilder = boolQuery();

        //设置多字段多条件，精确查询，支持单值精确查询和多值精确查询，如果同时存在单值和多值，取多值
        if (!isNullList(param.getTermsMatch())) {
            for (TermsMatch termsMatch : param.getTermsMatch()) {
                if (!isNull(termsMatch.getFieldName())) {
                    if (termsMatch.getMust()) {
                        if (!isNullArray(termsMatch.getValues())) {
                            boolQueryBuilder.must(termsQuery(termsMatch.getFieldName(), termsMatch.getValues()));
                        } else {
                            if (!isNullObject(termsMatch.getValue())) {
                                boolQueryBuilder.must(termQuery(termsMatch.getFieldName(), termsMatch.getValue()));
                            }
                        }
                    } else {
                        if (!isNullArray(termsMatch.getValues())) {
                            boolQueryBuilder.mustNot(termsQuery(termsMatch.getFieldName(), termsMatch.getValues()));
                        } else {
                            if (!isNullObject(termsMatch.getValue())) {
                                boolQueryBuilder.mustNot(termQuery(termsMatch.getFieldName(), termsMatch.getValue()));
                            }
                        }
                    }
                }
            }
        }

        //设置多字段多条件，模糊查询
        if (!isNullList(param.getWildcardMatch())) {
            for (WildcardMatch wildcardMatch : param.getWildcardMatch()) {
                if (!isNull(wildcardMatch.getFieldName())) {
                    boolQueryBuilder.must(wildcardQuery(wildcardMatch.getFieldName(), wildcardMatch.getValue().toString() + "*"));
                }
            }
        }

        //设置多字段范围查询
        if (!isNullList(param.getRangeMatch())) {
            Object gt, gte, lt, lte;
            RangeQueryBuilder rangeQueryBuilder;
            for (RangeMatch rangeMatch : param.getRangeMatch()) {
                gt = rangeMatch.getGt();
                gte = rangeMatch.getGte();
                lt = rangeMatch.getLt();
                lte = rangeMatch.getLte();
                rangeQueryBuilder = rangeQuery(rangeMatch.getFieldName());
                if (!isNullObject(gt))
                    rangeQueryBuilder.gte(gt);
                if (!isNullObject(gte))
                    if (!isNullObject(gte)) ;
                if (!isNullObject(lt))
                    rangeQueryBuilder.gte(lt);
                if (!isNullObject(lte))
                    if (!isNullObject(lte)) ;

                boolQueryBuilder.must(rangeQueryBuilder);
            }
        }

        //设置加权查询
        if(!isNullList(param.getBoostMatch())){
            for (BoostMatch boostMatch:param.getBoostMatch()) {
                if(!isNullArray(boostMatch.getValues())){
                    boolQueryBuilder.should(termsQuery(boostMatch.getFieldName(),boostMatch.getValues()).boost(boostMatch.getBoost()));
                }
            }
        }

        return boolQueryBuilder;
    }

    private static boolean isNullObject(Object value) {
        return null == value ? true : false;
    }

    private static boolean isNullArray(Object[] values) {
        return null == values ? true : values.length == 0 ? true : false;
    }

    private static boolean isNullList(List<?> list) {
        return null == list ? true : list.size() == 0 ? true : false;
    }


    private static boolean isNull(String str) {
        return null == str ? true : "".equals(str) ? true : false;
    }

}


