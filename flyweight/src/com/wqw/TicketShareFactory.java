package com.wqw;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author swk
 * @Date 2022/6/13 10:38
 * @Blog http://43.138.50.101:8081/
 * @Description: 定义一个工厂类来管理享元对象
 * @Version 1.0
 */

/*
 * 享元对象工厂
 */
public class TicketShareFactory {
    private static Map<String, IShareTicket> CACHE_POOL = new HashMap<>();
    public static IShareTicket getTicketInfo(String from, String to) {
        String key = from + "->" + to;
        if (TicketShareFactory.CACHE_POOL.containsKey(key)) {
            System.out.println("使用缓存");
            return TicketShareFactory.CACHE_POOL.get(key);
        }
        System.out.println("未使用缓存");
        IShareTicket ticket = new TrainShareTicket(from, to);
        CACHE_POOL.put(key, ticket);
        return ticket;

    }
}
