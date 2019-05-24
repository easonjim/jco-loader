package com.github.easonjim.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRecordMetaData;
import com.sap.conn.jco.JCoTable;

/**
 * @author jim
 * @date 2019/05/24
 */
public class MultiFromSAP {
    /**
     * description: 从sap获取数据的工具类
     *
     * @param sapConn          sap连接
     * @param interfaceName    接口名称
     * @param firstParamKey    一级参数key 用逗号分隔
     * @param firstParamValue  一级参数value 用逗号分隔
     * @param secondParamKey   二级参数key 用逗号分隔
     * @param secondParamValue 二级参数value 用逗号分隔
     * @return List
     */
    public static List<SapData> getSapData(SapConn sapConn, String interfaceName,
                                           String firstParamKey, String firstParamValue,
                                           String secondParamKey, String secondParamValue) {
        List<SapData> returnList = new ArrayList<SapData>();
        JCoFunction function;
        JCoDestination destination = SapConnUtils.connect(sapConn);
        System.out.println("正在从SAP获取数据");
        try {
            // 调用interface函数
            function = destination.getRepository().getFunction(interfaceName);


            // 遍历全部得到的table并处理
            for (JCoField field : function.getTableParameterList()) {

                // 按照需求可以整理出一条一条的数据以便插入数据库
                JCoTable responseTable = field.getTable();
                // 一级参数
                if ("".equals(firstParamKey) || "".equals(firstParamValue)) {
                    // 不需要参数
                } else {
                    String[] paramKeys = firstParamKey.split(",");
                    String[] paramValues = firstParamValue.split(",");
                    for (int i = 0; i < paramKeys.length; i++) {
                        function.getImportParameterList().setValue(paramKeys[i],
                                "null".equals(paramValues[i]) ? "" : paramValues[i]);
                    }
                }

                // 二级参数
                if ("".equals(secondParamKey) || "".equals(secondParamValue)) {
                    // 不需要参数
                } else {
                    responseTable.appendRow();
                    String[] paramKeys = secondParamKey.split(",");
                    String[] paramValues = secondParamValue.split(",");
                    for (int i = 0; i < paramKeys.length; i++) {
                        responseTable.setValue(paramKeys[i],
                                "null".equals(paramValues[i]) ? "" : paramValues[i]);
                    }
                }
                // 调用函数得到返回结果(调用接口把值放到function中)
                function.execute(destination);

                // 获取metaData(包含表的关键信息)
                JCoRecordMetaData metaData = responseTable.getRecordMetaData();
                SapData sapData = new SapData();
                sapData.setFieldCount(metaData.getFieldCount());
                String[] name = new String[sapData.getFieldCount()];
                List<Map<String, String>> sapList = new ArrayList<Map<String, String>>();
                // 获取全部名称
                for (int j = 0; j < sapData.getFieldCount(); j++) {
                    name[j] = metaData.getName(j);
                }
                sapData.setFieldNames(name);
                // 获取全部数据
                for (int i = 0; i < responseTable.getNumRows(); i++) {
                    responseTable.setRow(i);
                    Map<String, String> sapMap = new HashMap<String, String>();
                    for (String fieldName : sapData.getFieldNames()) {
                        sapMap.put(fieldName, responseTable.getString(fieldName));
                    }
                    sapList.add(sapMap);
                }
                sapData.setData(sapList);
                returnList.add(sapData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("获取成功");
        return returnList;
    }
}
