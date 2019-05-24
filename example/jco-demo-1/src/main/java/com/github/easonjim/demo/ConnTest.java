package com.github.easonjim.demo;

/**
 * @author jim
 * @date 2019/05/24
 */
public class ConnTest {
    public static void main(String[] args) {
        SapConn con = new SapConn(
                "127.0.0.1",
                "123",
                "456",
                "abc",
                "abc",
                "abc",
                "123",
                "321",
                "123456"
        );

        // 测试连接
        SapConnUtils.connect(con);
    }
}
