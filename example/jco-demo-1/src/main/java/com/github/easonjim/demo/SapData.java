package com.github.easonjim.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author jim
 * @date 2019/05/24
 */
public class SapData {

    // 列数
    private int fieldCount;

    // 列的具体名称
    private String[] fieldNames;

    // 全部数据
    private List<Map<String, String>> data;

    public SapData(int fieldCount, String[] fieldNames, List<Map<String, String>> data) {
        this.fieldCount = fieldCount;
        this.fieldNames = fieldNames;
        this.data = data;
    }

    public SapData() {

    }

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SapData{" +
                "fieldCount=" + fieldCount +
                ", fieldNames=" + Arrays.toString(fieldNames) +
                ", data=" + data +
                '}';
    }
}
