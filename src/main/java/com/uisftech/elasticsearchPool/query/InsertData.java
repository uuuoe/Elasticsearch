package com.uisftech.elasticsearchPool.query;

import com.uisftech.elasticsearchPool.client.QueryClient;
import org.apache.commons.pool2.ObjectPool;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;

import java.util.List;
import java.util.Map;

public class InsertData {

    /**
     * pool
     */
    private ObjectPool<QueryClient> pool = QueryData.getPool();

    public boolean insertData(String index, String type, List<Map<String, String>> list) {

        if (null != list && list.size() > 0) {
            QueryClient client = null;
            try {
                client = pool.borrowObject();
                BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
                for (int i = 0; i < list.size(); i++) {
                    bulkRequestBuilder.add(client.prepareIndex(index, type).setSource(list.get(i)));
                }

                BulkResponse bulkItemResponses = bulkRequestBuilder.get();
                if (bulkItemResponses.hasFailures()) {
                    System.out.println(bulkItemResponses.buildFailureMessage());
                } else {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pool.returnObject(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
