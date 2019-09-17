package com.uisftech.elasticsearchPool.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile {

    public List<Map<String, String>> readFile(String filePath, String delimiter, String[] columns) {
        List<Map<String, String>> list = null;

        //读取文件，封装成map对象
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            if (null != br) {
                list = new ArrayList<Map<String, String>>();
                String line = null;
                while (null != (line = br.readLine())) {
                    Map<String, String> map = getMap(delimiter, columns, line);
                    if (null != map && map.size() > 0) {
                        list.add(map);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    private Map<String, String> getMap(String delimiter, String[] columns, String line) {
        Map<String, String> map = null;

        if (null != line && !"".equals(line)) {
            String[] lines = line.split(delimiter);
            if (lines.length == columns.length) {
                map = new HashMap<String, String>();
                for (int i = 0; i < columns.length; i++) {
                    map.put(columns[i],lines[i]);
                }
            }
        }
        return map;
    }
}
