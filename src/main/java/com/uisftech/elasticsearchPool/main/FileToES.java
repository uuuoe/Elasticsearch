package com.uisftech.elasticsearchPool.main;

import com.uisftech.elasticsearchPool.entity.QueryParam;
import com.uisftech.elasticsearchPool.query.InsertData;
import com.uisftech.elasticsearchPool.query.QueryData;
import com.uisftech.elasticsearchPool.util.ReadFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class FileToES {
    public static void main(String[] args) {

        //加载配置文件，获取加载数据信息
        Properties properties = new Properties();
        InputStream in = FileToES.class.getClassLoader().getResourceAsStream("elasticsearch.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //读取文件，获取数据
        ReadFile readFile = new ReadFile();
        String filePath = properties.getProperty("filePath");
        String delimiter = properties.getProperty("delimiter");
        String[] columns = properties.getProperty("columns").split(",");
        List<Map<String,String>> list = readFile.readFile(filePath,delimiter,columns);

        //录入ES
        InsertData insertData = new InsertData();
        String index = properties.getProperty("index");
        String type = properties.getProperty("type");
        boolean result = insertData.insertData(index,type,list);
        if(result){
            System.out.println(index+"插入数据成功");
        }

        //查询数据
        //创建QueryParam对象，构建elasticsearch查询条件
        QueryParam param = new QueryParam();

        //设置要查询的索引名称，必须设置，不然返回null
        param.setType("person");

        //查询数据
        Map<String,Object> resultData = QueryData.queryData(param);
        if(null!=resultData && resultData.size()>0){
            ArrayList<Map<String,Object>> data = (ArrayList<Map<String,Object>>) resultData.get("data");//获取数据
            Long totleNum = (Long)resultData.get("totleNum"); //获取总条数
            System.out.println("总条数 = " + totleNum);
            for(int i = 0;i < data.size();i++){
                System.out.println(data.get(i));
            }
        }

    }
}
