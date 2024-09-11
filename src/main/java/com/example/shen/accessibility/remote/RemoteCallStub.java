package com.example.shen.accessibility.remote;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 模拟多个远程服务，返回所有支付方式的可用状态
 *
 * @author Administrator
 */
@Slf4j
public final class RemoteCallStub {
    /**
     * 远程调用代理，模拟多个远程服务，随机阻塞一段时间，随机返回指定支付方式的可用状态
     */
    public static boolean queryAvailability(int paymentType) {
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            log.error("Some error occurred, can not get the real status of payment : {}", paymentType);
            return false;
        }
        return new Random().nextBoolean();
    }
}