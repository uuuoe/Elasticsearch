package com.uisftech.elasticsearch.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uisftech.elasticsearch.client.ElasticsearchQueryClient;
import com.uisftech.elasticsearch.pool.ElasticsearchQueryClientFactory;
import com.uisftech.elasticsearch.query.pojo.ElasticsearchQueryData;
import com.uisftech.elasticsearch.query.pojo.Param;
import com.uisftech.elasticsearch.query.pojo.match.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.io.*;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * elasticsearch查询构建器
 */
public class ElasticsearchQueryBuilder {
    private static final Logger logger = Logger.getLogger(ElasticsearchQueryBuilder.class);//导包的时候注意不要导错了
    private ElasticsearchQuery query;
    private ObjectPool<ElasticsearchQueryClient> pool;
    private String clusterName;
    private String clusterNodes;
    private String clusterPort;
    private int maxTotal = 8;
    private long maxWaitMillis = -1L;

    //?
    private ElasticsearchQueryBuilder() {
        query = new ElasticsearchQuery();
    }

    public static ElasticsearchQueryBuilder buildElasticsearchQuery() {
        return new ElasticsearchQueryBuilder();
    }

    public ElasticsearchQueryBuilder setDefault() {
        InputStream in = ElasticsearchQueryBuilder.class.getClassLoader().getResourceAsStream("elasticsearch.properties");

        if (in != null) {
            try {
                setParam(new InputStreamReader(in, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public ElasticsearchQueryBuilder setProperties(String filePath) {
        try {
            setParam(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ElasticsearchQueryBuilder setClusterName(String clusterName) {
        this.clusterName = clusterName;
        return this;
    }

    public ElasticsearchQueryBuilder setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
        return this;
    }

    public ElasticsearchQueryBuilder setClusterPort(String clusterPort) {
        this.clusterPort = clusterPort;
        return this;
    }

    public ElasticsearchQueryBuilder setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        return this;
    }

    public ElasticsearchQueryBuilder setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
        return this;
    }

    public Query build() {   //????????
        if (!isNotEmpty(clusterName)) {
            throw new IllegalArgumentException("没有找到elasticsearch集群名称！");
        }

        if (!isNotEmpty(clusterNodes)) {
            throw new IllegalArgumentException("没有找到elasticsearch集群节点！");
        }

        if (!isNotEmpty(clusterPort)) {
            throw new IllegalArgumentException("没有找到elasticsearch集群端口号！");
        }

        ElasticsearchQueryClientFactory factory = new ElasticsearchQueryClientFactory(clusterName, clusterNodes, clusterPort);
        GenericObjectPoolConfig<ElasticsearchQueryClient> config = new GenericObjectPoolConfig<ElasticsearchQueryClient>();
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWaitMillis);
        pool = new GenericObjectPool<ElasticsearchQueryClient>(factory, config);
        return query;   //???query不是Query类型的啊。!!!明白了。是继承query啊。
    }

    private void setParam(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
            this.clusterName = properties.getProperty("elasticsearch.cluster_name");
            this.clusterNodes = properties.getProperty("elasticsearch.cluster_nodes");
            this.clusterPort = properties.getProperty("elasticsearch.cluster_port");
            this.maxTotal = Integer.parseInt(properties.getProperty("pool.maxTotal"));
            this.maxWaitMillis = Long.parseLong(properties.getProperty("pool.maxWaitMillis"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isNotEmpty(String args) {
        return null != args && !"".equals(args);
    }

    private boolean isNotEmptyArray(Object[] values) {
        return null != values && values.length > 0;
    }

    private boolean isNotEmptyObject(Object value) {
        return null != value;
    }


    //？？？？？为什么把它做成内部类啊
    private class ElasticsearchQuery implements Query {

        private ElasticsearchQuery() {
        }

        @Override
        public ElasticsearchQueryData query(Param param) {
            ElasticsearchQueryData result; //返回结果集，包含数据集data，总条数totalNum

            if (null == param) {
                throw new NullPointerException("elasticsearch查询获得的参数为空，请创建Param对象");
            } else {
                //增加模糊查询判断
                String keyword = param.getKeyword(); //关键字
                String[] fuzzyFields = param.getFuzzyFields();//模糊查询字段
                if (isNotEmpty(keyword) && isNotEmptyArray(fuzzyFields)) {
                    //构建模糊匹配对象
                    List<Match> matches = new ArrayList<Match>();
                    for (String fuzzyField : fuzzyFields) {
                        WildcardMatch wildcard = new WildcardMatch();
                        wildcard.setFieldName(fuzzyField);
                        wildcard.setValue(keyword);
                        matches.add(wildcard);
                    }
                    param.setMatches(matches);
                }
                result = queryDataForGeneral(param);
            }
            return result;
        }

        @Override
        public ElasticsearchQueryData query(String paramJson) {
            ElasticsearchQueryData result; //返回结果集

            if (null == paramJson) {
                throw new NullPointerException("elasticsearch查询获得发参数为空，请创建paramJson对象");
            } else {
                Param param = getParam(paramJson);
                result = query(param);
            }
            return result;
        }

        @Override
        public Map<String, Object> query(Map<String, Object> paramMap) {
            Map<String, Object> map = null;
            if (null == paramMap || paramMap.size() <= 0) {
                throw new NullPointerException("elasticsearch查询获得的参数为空，请创建paramMap对象");
            } else {
                Param param = getParam(paramMap); //??????
                ElasticsearchQueryData result = query(param);
                if (null != result) {
                    map = new HashMap<String, Object>();
                    map.put("page", result.getPage());
                    map.put("size", result.getSize());
                    map.put("totlePage", result.getTotlePage());
                    map.put("totleNum", result.getTotleNum());
                    map.put("data", result.getData());
                }
            }
            return map;
        }

        @Override
        public String queryToStirng(Param param) {
            ElasticsearchQueryData data = query(param);
            if (null != data) {
                return JSON.toJSONString(query(param)).trim();
            }
            return null;
        }

        @Override
        public String queryToString(String paramJson) {
            Param param = getParam(paramJson);
            return queryToStirng(param);
        }
    }

    /**
     * 普通查询
     *
     * @param param
     * @return
     */
    private ElasticsearchQueryData queryDataForGeneral(Param param) {
        //分析参数，构建build,获取数据
        ElasticsearchQueryData result = null;//结果集
        ElasticsearchQueryClient client = null;//es查询客户端
        SearchRequestBuilder builder;//elasticsearch search build

        try {
            client = pool.borrowObject();//获取elasticsearch客户端

            //设置索引名称
            if (!isNotEmpty(param.getIndex())) {
                logger.error("没找到索引名称，请检查参数！");
                return null;
            } else {
                builder = client.prepareSearch(param.getIndex());
            }

            //设置类型
            if (isNotEmpty(param.getType())) {
                builder.setTypes(param.getType());
            }

            //设置查询条件
            builder.setQuery(createQueryBuilder(param));

            //设置排序
            if (isNotEmpty(param.getSortField())) {
                if (isNotEmpty(param.getSortOrder()) && param.getSortOrder().equalsIgnoreCase("asc")) {
                    builder.addSort(param.getSortField(), SortOrder.ASC);
                } else {
                    builder.addSort(param.getSortField(), SortOrder.DESC);
                }
            }

            //设置分页，默认是一页十条
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
            SearchResponse search = builder.get();
            if (null != search) {
                result = new ElasticsearchQueryData();
                List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
                for (SearchHit hit : search.getHits().getHits()) {
                    data.add(hit.getSourceAsMap());
                }
                int page = param.getPage();
                int size = param.getSize();
                long totleNum = search.getHits().getTotalHits();
                long totlePage = totleNum % size == 0 ? totleNum / size : totleNum / size + 1;
                result.setPage(page);
                result.setSize(size);
                result.setTotlePage(totlePage);
                result.setTotleNum(totleNum);
                result.setData(data);
            }
        } catch (Exception e) {
            logger.error("获取elasticsearch客户端失败！", e);
        } finally {
            if (null != client) {
                try {
                    //关闭连接
                    logger.debug("brrow a connection :" + client + ",active connection :" + pool.getNumActive());
                    pool.returnObject(client);
                } catch (Exception e) {
                    logger.error("关闭elasticsearch客户端失败！", e);
                }
            }
        }
        return result;
    }

    private QueryBuilder createQueryBuilder(Param param) {
        //创建bool query对象 封装精确查询，模糊查询，范围查询，加权查询
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        List<Match> matches = param.getMatches();
        if (null != matches && matches.size() > 0) {
            for (Match match : matches) {
                switch (match.getType()) {
                    case TERMS:
                        TermsMatch terms = (TermsMatch) match;
                        this.createTermsMatch(boolQueryBuilder, terms);
                        break;
                    case WILDCARD:
                        WildcardMatch wildcard = (WildcardMatch) match;
                        this.createWildcardMatch(boolQueryBuilder, wildcard);
                        break;
                    case RANGE:
                        RangeMatch range = (RangeMatch) match;
                        this.createWRangeMatch(boolQueryBuilder, range);
                        break;
                    case BOOST:
                        BoostMatch boost = (BoostMatch) match;
                        this.createBoostMatch(boolQueryBuilder, boost);
                        break;
                }
            }
        }
        return boolQueryBuilder;
    }

    private void createTermsMatch(BoolQueryBuilder boolQueryBuilder, TermsMatch terms) {
        Boolean must = terms.getMust();//true:精确匹配，false：不包含匹配
        Boolean should = terms.getShould();//true:条件之间是or关系。false:条件之间是and关系
        if (must) { //精确匹配
            if (should) { //or
                //设置多字段多条件，精确查询，支持单值精确查询和多值精确查询，如果同时存在单值和多值，取多值
                if (isNotEmptyArray(terms.getValues())) {
                    boolQueryBuilder.should(termsQuery(terms.getFieldName(), terms.getValue()));
                } else {
                    if (isNotEmptyObject(terms.getValue()))
                        boolQueryBuilder.should(termQuery(terms.getFieldName(), terms.getValue()));
                }
            } else { //and
                if (isNotEmptyArray(terms.getValues())) {
                    boolQueryBuilder.must(termsQuery(terms.getFieldName(), terms.getValues()));
                } else {
                    if (isNotEmptyObject(terms.getValues()))
                        boolQueryBuilder.must(termQuery(terms.getFieldName(), terms.getValue()));
                }
            }
        } else { //不包含匹配
            if (isNotEmptyArray(terms.getValues())) {
                boolQueryBuilder.mustNot(termsQuery(terms.getFieldName(), terms.getValues()));
            } else {
                if (isNotEmptyObject(terms.getValue()))
                    boolQueryBuilder.mustNot(termQuery(terms.getFieldName(), terms.getValue()));
            }
        }
    }

    private void createWildcardMatch(BoolQueryBuilder boolQueryBuilder, WildcardMatch wildcard) {
        //设置多字段多条件，模糊查询
        if (wildcard.getMust())
            boolQueryBuilder.must(wildcardQuery(wildcard.getFieldName(), "*" + wildcard.getValue().toString() + "*"));
        if (!wildcard.getMust())
            boolQueryBuilder.should(wildcardQuery(wildcard.getFieldName(), "*" + wildcard.getValue().toString() + "*"));
    }

    private void createWRangeMatch(BoolQueryBuilder boolQueryBuilder, RangeMatch range) {
        //设置多字段范围查询
        boolQueryBuilder.must(rangeQuery(range.getFieldName()).from(range.getFrom()).to(range.getTo()).
                includeLower(range.isIncludeLower()).includeUpper(range.isIncludeUpper()));
    }

    private void createBoostMatch(BoolQueryBuilder boolQueryBuilder, BoostMatch boost) {
        //设置加权查询
        if (isNotEmptyArray(boost.getValues())) {
            boolQueryBuilder.should(termsQuery(boost.getFieldName(), boost.getValues()).boost(boost.getBoost()));
        } else {
            if (isNotEmptyObject(boost.getValue()))
                boolQueryBuilder.should(termQuery(boost.getFieldName(), boost.getValue()).boost(boost.getBoost()));
        }
    }

    private Param getParam(String json) {
        Param param;
        if (!isNotEmpty(json))
            return null;

        JSONObject jsonObject = JSON.parseObject(json);
        String index = jsonObject.getString("index");
        String type = jsonObject.getString("type");
        Integer page = jsonObject.getInteger("page");
        Integer size = jsonObject.getInteger("size");
        String sortField = jsonObject.getString("sortField");
        String sortOrder = jsonObject.getString("sortOrder");
        String[] fieldNames = getArray(jsonObject.getJSONArray("fieldNames"));
        List<Match> matches = getMatch(jsonObject.getJSONArray("matches"));

        param = new Param();
        if (isNotEmpty(index))
            param.setIndex(index);
        if (isNotEmpty(type))
            param.setType(type);
        if (isNotEmptyObject(page))
            param.setPage(page);  //???????
        if (isNotEmptyObject(size))
            param.setSize(size);
        if (isNotEmpty(sortField))
            param.setSortField(sortField);
        if (isNotEmpty(sortOrder))
            param.setSortOrder(sortOrder);
        if (isNotEmptyArray(fieldNames))
            param.setFieldNames(fieldNames);
        if (null != matches && matches.size() > 0)
            param.setMatches(matches);

        return param;
    }

    private Param getParam(Map<String, Object> paramMap) {
        if (null == paramMap || paramMap.size() <= 0)
            return null;
        Param param;
        String index = paramMap.containsKey("index") && null != paramMap.get("index") ? paramMap.get("index").toString() : null;
        String type = paramMap.containsKey("type") && null != paramMap.get("type") ? paramMap.get("type").toString() : null;
        int page = paramMap.containsKey("page") && null != paramMap.get("page") ? Integer.parseInt(paramMap.get("page").toString()) : 1;
        int size = paramMap.containsKey("size") && null != paramMap.get("size") ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        String sortField = paramMap.containsKey("sortField") && null != paramMap.get("sortField") ? paramMap.get("sortField").toString() : null;
        String sortOrder = paramMap.containsKey("sortOrder") && null != paramMap.get("sortOrder") ? paramMap.get("sortOrder").toString() : "desc";
        String[] fieldNames = paramMap.containsKey("fieldNames") && null != paramMap.get("fieldName") ? (String[]) paramMap.get("fieldNames") : null;
        List<Map<String, Object>> list = paramMap.containsKey("matches") && null != paramMap.get("matches") ? (List<Map<String, Object>>) paramMap.get("matches") : null;
        List<Match> matches = getMatch(list);

        param = new Param();
        param.setIndex(index);
        param.setType(type);
        param.setPage(page);
        param.setSize(size);
        param.setSortField(sortField);
        param.setSortOrder(sortOrder);
        param.setFieldNames(fieldNames);
        param.setMatches(matches);
        return param;
    }

    private String[] getArray(JSONArray jsonArray) {
        String[] array = new String[jsonArray.size()];
        if (!jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++)
                array[i] = jsonArray.getString(i);
        }
        return array;
    }

    private List<Match> getMatch(JSONArray jsonArray) {
        List<Match> matches = null;
        if (!jsonArray.isEmpty()) {
            matches = new ArrayList<Match>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.getString("type").toUpperCase();
                String fieldName = jsonObject.getString("fieldName");
                Object[] values = null != jsonObject.getJSONArray("values") ? jsonObject.getJSONArray("values").toArray() : null;
                Object value = jsonObject.get("value");
                if (!"".equals(type)) {
                    if ("TERMS".equals(type)) {
                        Boolean must = null != jsonObject.getBoolean("must") ? jsonObject.getBoolean("must") : Boolean.TRUE;
                        Boolean should = null != jsonObject.getBoolean("should") ? jsonObject.getBoolean("should") : Boolean.FALSE;
                        TermsMatch terms = new TermsMatch();
                        terms.setFieldName(fieldName);
                        terms.setValues(values);
                        terms.setValue(value);
                        terms.setMust(must);
                        terms.setShould(should);
                        matches.add(terms);
                    } else if ("WILDCARD".equals(type)) {
                        Boolean must = null != jsonObject.getBoolean("must") ? jsonObject.getBoolean("must") : Boolean.FALSE;
                        WildcardMatch wildcard = new WildcardMatch();
                        wildcard.setFieldName(fieldName);
                        wildcard.setValue(value);
                        wildcard.setMust(must);
                        matches.add(wildcard);
                    } else if ("RANGE".equals(type)) {
                        Object from = jsonObject.get("from");
                        Object to = jsonObject.get("to");
                        Boolean includeLower = null != jsonObject.getBoolean("includeLower") ? jsonObject.getBoolean("includeLower") : Boolean.TRUE;
                        Boolean includeUpper = null != jsonObject.getBoolean("includeUpper") ? jsonObject.getBoolean("includeUpper") : Boolean.TRUE;
                        RangeMatch range = new RangeMatch();
                        range.setFieldName(fieldName);
                        range.setFrom(from);
                        range.setTo(to);
                        range.setIncludeLower(includeLower);
                        range.setIncludeUpper(includeUpper);
                        matches.add(range);
                    } else if ("BOOST".equals(type)) {
                        Float boost = null != jsonObject.getFloat("boost") ? jsonObject.getFloat("boost") : 1F;
                        BoostMatch boostMatch = new BoostMatch();
                        boostMatch.setFieldName(fieldName);
                        boostMatch.setValues(values);
                        boostMatch.setValue(value);
                        boostMatch.setBoost(boost);
                        matches.add(boostMatch);
                    }
                }
            }
        }
        return matches;
    }

    private List<Match> getMatch(List<Map<String, Object>> list) {
        List<Match> matches = null;
        if (null != list && list.size() > 0) {
            matches = new ArrayList<Match>();
            for (Map<String, Object> map : list) {
                String type = map.containsKey("type") && null != map.get("type") ? map.get("type").toString().toUpperCase() : null;
                String fieldName = map.containsKey("fieldName") && null != map.get("fieldName") ? map.get("fieldName").toString() : null;
                Object[] values = map.containsKey("values") && null != map.get("values") ? (Object[]) map.get("values") : null;
                Object value = map.containsKey("value") && null != map.get("value") ? map.get("value")  : null;
                if ("TERMS".equals(type)) {
                    Boolean must = map.containsKey("must") && null != map.get("must") ? Boolean.parseBoolean(map.get("must").toString()) : Boolean.TRUE;
                    Boolean should = map.containsKey("should") && null != map.get("should") ? Boolean.parseBoolean(map.get("should").toString()) : Boolean.FALSE;
                    TermsMatch terms = new TermsMatch();
                    terms.setFieldName(fieldName);
                    terms.setValues(values);
                    terms.setValue(value);
                    terms.setMust(must);
                    terms.setShould(should);
                    matches.add(terms);
                } else if ("WILDCARD".equals(type)) {
                    Boolean must = map.containsKey("must") && null != map.get("must") ? Boolean.parseBoolean(map.get("must").toString()) : Boolean.TRUE;
                    WildcardMatch wildcard = new WildcardMatch();
                    wildcard.setFieldName(fieldName);
                    wildcard.setValue(value);
                    wildcard.setMust(must);
                    matches.add(wildcard);
                } else if ("RANGE".equals(type)) {
                    Object from = map.containsKey("from") && null != map.get("from") ? map.get("from") : null;
                    Object to = map.containsKey("to") && null != map.get("to") ? map.get("to") : null;
                    Boolean includeLower = map.containsKey("includeLower") && null != map.get("includeLower") ? Boolean.parseBoolean(map.get("includeLower").toString()) : Boolean.TRUE;
                    Boolean includeUpper = map.containsKey("includeUpper") && null != map.get("includeUpper") ? Boolean.parseBoolean(map.get("includeUpper").toString()) : Boolean.TRUE;
                    RangeMatch range = new RangeMatch();
                    range.setFieldName(fieldName);
                    range.setFrom(from);
                    range.setTo(to);
                    range.setIncludeLower(includeLower);
                    range.setIncludeUpper(includeUpper);
                    matches.add(range);
                } else if ("BOOST".equals(type)) {
                    Float boost = map.containsKey("boost") && null != map.get("boost") ? Float.parseFloat(map.get("boost").toString()) : 1F;
                    BoostMatch boostMatch = new BoostMatch();
                    boostMatch.setFieldName(fieldName);
                    boostMatch.setValues(values);
                    boostMatch.setValue(value);
                    boostMatch.setBoost(boost);
                    matches.add(boostMatch);
                }
            }
        }
        return matches;
    }


}
