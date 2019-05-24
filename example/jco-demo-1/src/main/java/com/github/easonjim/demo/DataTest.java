package com.github.easonjim.demo;

/**
 * @author jim
 * @date 2019/05/24
 */
public class DataTest {
    public static void main(String[] args) {

        final String interfaceName = "xxx";
        // 一级参数
        final String firstParamKey = "xx";
        final String firstParamValue = "xx";
        // 二级参数
        final String secondParamKey = "xx";
        final String secondParamValue = "xx";

        SapConn con = new SapConn(
                "xx",
                "xx",
                "xx",
                "xxx",
                "xxx",
                "xx",
                "xx",
                "xx",
                "xxx"
        );

        // 测试数据
        MultiFromSAP.getSapData(con, interfaceName, firstParamKey, firstParamValue, secondParamKey, secondParamValue);
    }
}
