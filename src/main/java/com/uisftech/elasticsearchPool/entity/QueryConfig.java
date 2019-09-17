package com.uisftech.elasticsearchPool.entity;

import com.uisftech.elasticsearchPool.client.QueryClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * queryData配置类
 */
public class QueryConfig extends GenericObjectPoolConfig<QueryClient> {
    /**
     * 集群名称
     */
    public static String ELASTICSEARCH_CLUSTER_NAME = "elasticsearch";

    /**
     * 集群节点
     */
    public static String ELASTICSEARCH_CLUSTER_NODES = "127.0.0.1";

    /**
     * 集群端口
     */
    public static String ELASTICSEARCH_CLUSTER_PORT = "9300";

    /**
     * 启用嗅探
     */
    public static boolean ELASTICSEARCH_CLIENT_TRANSPORT_SNIFF = true;

    /**
     * 设置为true忽略已连接节点的集群名称验证
     */
    public static boolean ELASTICSEARCH_CLIENT_TRANSPORT_IGNORE_CLUSTER_NAME = false;

    /**
     * 等待节点ping响应的时间，默认为5s
     */
    public static String ELASTICSEARCH_CLIENT_TRANSPORT_PING_TIMEOUT = "5s";

    /**
     * 对列出和连接的节点进行采样ping的频率。默认为5s
     */
    public static String ELASTICSEARCH_CLIENT_TRANSPORT_NODES_SAMPLER_INTERNAL = "5s";

    private String clusterName = ELASTICSEARCH_CLUSTER_NAME;

    private String clusterNodes = ELASTICSEARCH_CLUSTER_NODES;

    private String clusterPort = ELASTICSEARCH_CLUSTER_PORT;

    private boolean transportSniff = ELASTICSEARCH_CLIENT_TRANSPORT_SNIFF;

    private boolean ignoreClusterName = ELASTICSEARCH_CLIENT_TRANSPORT_IGNORE_CLUSTER_NAME;

    private String pingTimeout = ELASTICSEARCH_CLIENT_TRANSPORT_PING_TIMEOUT;

    private String samplerInterval = ELASTICSEARCH_CLIENT_TRANSPORT_NODES_SAMPLER_INTERNAL;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public String getClusterPort() {
        return clusterPort;
    }

    public void setClusterPort(String clusterPort) {
        this.clusterPort = clusterPort;
    }

    public boolean isTransportSniff() {
        return transportSniff;
    }

    public void setTransportSniff(boolean transportSniff) {
        this.transportSniff = transportSniff;
    }

    public boolean isIgnoreClusterName() {
        return ignoreClusterName;
    }

    public void setIgnoreClusterName(boolean ignoreClusterName) {
        this.ignoreClusterName = ignoreClusterName;
    }

    public String getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(String pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public String getSamplerInterval() {
        return samplerInterval;
    }

    public void setSamplerInterval(String samplerInterval) {
        this.samplerInterval = samplerInterval;
    }
}
