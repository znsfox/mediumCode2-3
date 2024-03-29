package com.example.mediumcode23.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@EnableAsync// 支持异步操作
@Configuration
public class AsyncTaskConfig {

    /**
     * com.google.guava中的线程池
     * @return
     */
    @Bean("my-executor")
    public Executor firstExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-executor").build();
        // 获取CPU的处理器数量
        int curSystemThreads = Runtime.getRuntime().availableProcessors() * 2;
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(curSystemThreads, 100,
                200, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), threadFactory);
        threadPool.allowsCoreThreadTimeOut();
        return threadPool;
    }

    /**
     * Spring线程池
     * @return
     */
    @Bean("async-executor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(24);
        // 线程池维护线程的最大数量，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(200);
        // 缓存队列
        taskExecutor.setQueueCapacity(50);
        // 空闲时间，当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(200);
        // 异步方法内部线程名称
        taskExecutor.setThreadNamePrefix("async-executor-");

        /**
         * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略
         * 通常有以下四种策略：
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：重试添加当前的任务，自动重复调用 execute() 方法，直到成功
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
