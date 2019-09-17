package com.uisftech.elasticsearchPool.util;

import com.uisftech.elasticsearchPool.client.QueryClient;
import com.uisftech.elasticsearchPool.entity.QueryConfig;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接工厂类
 */
public class QueryClientFactory extends BasePooledObjectFactory<QueryClient> {

    /**
     * 创建client对象的时候生产id
     * @return
     * @throws Exception
     */
    private AtomicInteger idsCount = new AtomicInteger(1);

    /**
     * queryData配置
     * @return
     * @throws Exception
     */
    private QueryConfig config;

    public QueryClientFactory(QueryConfig config){ this.config = config; }

    @Override
    public QueryClient create() throws Exception {
        //创建elasticsearch设置
        Settings settings = Settings.builder().put("cluster.name",config.getClusterName())
                .put("client.transport.sniff",config.isTransportSniff())
                .put("client.transport.ignore_cluster_name",config.isIgnoreClusterName())
                .put("client.transport.ping_timeout",config.getPingTimeout())
                .put("client.transport.nodes_sampler_interval",config.getSamplerInterval())
                .build();

        QueryClient client = new QueryClient(settings);

        //split节点信息
        if(null != config.getClusterPort() && !"".equals(config.getClusterNodes())){
            String[] nodes = config.getClusterNodes().split(",");
            for(int i = 0;i < nodes.length;i++){
                client.addTransportAddress(new TransportAddress(InetAddress.getByName(nodes[i]),
                        Integer.parseInt(config.getClusterPort())));
            }
        }
        client.setId(idsCount.getAndAdd(1));
        return client;

    }

    @Override
    public PooledObject<QueryClient> wrap(QueryClient client) {
        return new DefaultPooledObject<QueryClient>(client);
    }
}
