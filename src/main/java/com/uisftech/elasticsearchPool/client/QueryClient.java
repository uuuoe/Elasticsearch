package com.uisftech.elasticsearchPool.client;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * 查询elasticsearch transport客户端
 */
public class QueryClient extends PreBuiltTransportClient {
    private int id;

    @SuppressWarnings("unchecked")
    public QueryClient(Settings settings) {
        super(settings);
    }

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }

    public String toString(){ return "id = " + id; }

}
