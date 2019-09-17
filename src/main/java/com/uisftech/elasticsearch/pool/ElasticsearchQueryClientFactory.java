package com.uisftech.elasticsearch.pool;

import com.uisftech.elasticsearch.client.ElasticsearchQueryClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 创建elasticsearch客户端需要消耗资源，
 * 当查询请求变大会频繁创建客户端连接，消耗大量系统资源，
 * 因此引入apache pool连接
 */
public class ElasticsearchQueryClientFactory extends BasePooledObjectFactory<ElasticsearchQueryClient> {

    /*
    集群名称
     */
    private String clusterName;

    /*
     集群节点，多个节点以逗号隔开
     */
    private String clusterNodes;

    /*
    集群端口号
     */
    private String clusterPort;

    /*
    启用嗅探
     */
    private boolean transportSniff;

    /*
    设置为true忽略已连接节点的集群名称验证
     */
    private boolean ignoreClusterName;

    /*
    等待节点ping响应的时间。默认为5s
     */
    private String pingTimeout;

    /*
    对列出和连接的节点进行采样/ ping的频率。默认为5s
     */
    private String samplerInterval;

    /*
    创建client对象的时候生产id
     */
    private AtomicInteger idsCount = new AtomicInteger(1);

    public ElasticsearchQueryClientFactory(String clusterName,String clusterNodes,String clusterPort){
        this.clusterName = clusterName;
        this.clusterNodes = clusterNodes;
        this.clusterPort = clusterPort;
        transportSniff = true;
        ignoreClusterName = false;
        pingTimeout = "5s";
        samplerInterval = "5s";
    }

    @Override
    public ElasticsearchQueryClient create() throws Exception {
        //构建elasticsearch客户端设置
        Settings settings = Settings.builder()
                .put("client.transport.sniff",transportSniff)
                .put("client.transport.ignore_cluster_name",ignoreClusterName)
                .put("client.transport.ping_timeout",pingTimeout)
                .put("client.transport.nodes_sampler_interval",samplerInterval)
                .build();
        ElasticsearchQueryClient client = new ElasticsearchQueryClient(settings);

        if(isNotEmpty(clusterNodes) && isNotEmpty(clusterPort)){  //这个*********我看俞老师没有导包 已解决 嗯嗯对的，后面有定义这个方法
            String[] nodes = clusterNodes.split(",");
            int port = Integer.parseInt(clusterPort);
            for (String node : nodes) {
                client.addTransportAddress(new TransportAddress(InetAddress.getByName(node),port));
            }
        }else{
            throw new IllegalArgumentException("没有找到elasticsearch集群节点和端口号信息或者集群节点和端口号信息为空");
        }

        client.setId(idsCount.getAndAdd(1));
        return client;
    }



    @Override
    public PooledObject<ElasticsearchQueryClient> wrap(ElasticsearchQueryClient client) {
        return new DefaultPooledObject<ElasticsearchQueryClient>(client);
    }

    private boolean isNotEmpty(String args){
        return null != args && !"".equals(args);
    }
}
