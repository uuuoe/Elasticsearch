package com.uisftech.elasticsearch.client;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * elasticsearch查询客户端
 */
public class ElasticsearchQueryClient extends PreBuiltTransportClient {
    private Integer id;

    public ElasticsearchQueryClient(Settings settings){
        super(settings);
    }

    @Override
    public String toString() {
        return "ElasticsearchQueryClient{" + "" +"id=" + id + '}';
    }

    public void setId(Integer id){
        this.id = id;
    }
}
