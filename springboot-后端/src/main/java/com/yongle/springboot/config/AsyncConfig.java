package com.yongle.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean("asyncExecutor")
    public Executor asyncExecutor(){
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(5);//核心线程数
        threadPool.setMaxPoolSize(5);//最大线程数
        threadPool.setQueueCapacity(Integer.MAX_VALUE);//线程池缓冲队列容量
        threadPool.setThreadNamePrefix("custom-async-");//线程名前缀

        threadPool.setWaitForTasksToCompleteOnShutdown(true);        //关机时，是否等待任务执行完

        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //设置拒绝策略
        //CALLER_RUNS：由调用者所在的线程执行该任务


        threadPool.initialize();//初始化线程
        return threadPool.getThreadPoolExecutor();
    }
}
