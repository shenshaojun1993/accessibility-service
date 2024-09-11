package com.example.shen.accessibility.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import org.springframework.util.CollectionUtils;

import com.example.shen.accessibility.constant.PaymentEnum;
import com.example.shen.accessibility.model.PaymentRealStatus;
import com.example.shen.accessibility.remote.RemoteCallStub;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * 缓存最新可用列表, 对外提供缓存批量更新、批量查询功能
 *
 * @author Administrator
 */
@Slf4j
public final class PaymentCache {
    /**
     * 缓存有效期
     */
    private static final long EXPIRATION = 60000L;
    /**
     * 缓存实体
     */
    private static final Map<Integer, PaymentRealStatus> CACHE = new ConcurrentHashMap<>(PaymentEnum.values().length);
    /**
     * 全局线程池
     */
    private static final ExecutorService THREAD_POOL;
    /**
     * 缓存上次更新的时间
     */
    private static volatile long lastUpdatedTime = 0L;

    static {
        // 初始化全局线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        int paymentNum = PaymentEnum.values().length;
        THREAD_POOL = new ThreadPoolExecutor(paymentNum, 3 * paymentNum, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(paymentNum), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Application is going to be closed, demo thread is being closed...");
            shutdownAndAwaitTermination();
            log.error("Demo thread closed.");
        }));
    }

    private static void shutdownAndAwaitTermination() {
        THREAD_POOL.shutdown(); // 禁止新任务提交
        try {
            // 等待已有任务完成
            if (!THREAD_POOL.awaitTermination(EXPIRATION, TimeUnit.MILLISECONDS)) {
                THREAD_POOL.shutdownNow(); // 强制关闭
                if (!THREAD_POOL.awaitTermination(EXPIRATION, TimeUnit.MILLISECONDS)) {
                    log.error("Demo thread pool failed to be closed normally！");
                }
            }
        } catch (InterruptedException ie) {
            THREAD_POOL.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 对外提供可用列表查询功能，当缓存为空或已过期触发强制刷新 为避免多个线程同时全量查询，限制一个线程进行查询
     *
     * @return 可用列表
     */
    public static List<PaymentRealStatus> getAvailableList() {
        // 当缓存已过期时或可用列表为空，需要触发立即刷新
        List<PaymentRealStatus> availableList = CACHE.values().stream().toList();
        if (System.currentTimeMillis() - lastUpdatedTime > EXPIRATION || CollectionUtils.isEmpty(availableList)) {
            synchronized (PaymentCache.class) {
                if (System.currentTimeMillis() - lastUpdatedTime > EXPIRATION
                    || CollectionUtils.isEmpty(availableList)) {
                    log.warn("The cache is expired, force to update it.");
                    updatePaymentAvailabilityCache(PaymentEnum.getAllPaymentWay());
                }
            }
        }
        return CACHE.values().stream().toList();
    }

    /**
     * 全量获取支付方式的可用性，并缓存结果
     *
     * @param paymentEnumList 支付方式的枚举
     */
    public static synchronized void updatePaymentAvailabilityCache(List<PaymentEnum> paymentEnumList) {
        // 通过CompletableFuture并行检查所有支付方式的可用性
        List<CompletableFuture<Void>> futures =
            paymentEnumList.stream().map(payment -> CompletableFuture.runAsync(() -> {
                // 检查该支付方式是否可用
                boolean isAvailable = RemoteCallStub.queryAvailability(payment.getType());
                // 更新缓存
                refresh(payment.getType(), new PaymentRealStatus(payment.getType(), payment.getName(), isAvailable));
            }, THREAD_POOL)).toList();

        // 等待所有任务完成
        futures.forEach(CompletableFuture::join);
        // 刷新完成时间
        lastUpdatedTime = System.currentTimeMillis();
    }

    /**
     * 刷新某个支付服务的可用性
     */
    public static void refresh(Integer key, PaymentRealStatus status) {
        CACHE.put(key, status);
    }
}