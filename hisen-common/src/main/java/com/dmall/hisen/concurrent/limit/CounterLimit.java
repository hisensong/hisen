package com.dmall.hisen.concurrent.limit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 缓存实现计数器限流
 * Created by : Hisensong
 * Created time:  2016/8/31.
 */
public class CounterLimit {
    public static void main(String[] args) throws ExecutionException {

        /**
         * Guava Cache是一个全内存的本地缓存实现，它提供了线程安全的实现机制。整体上来说Guava cache 是本地缓存的不二之选，简单易用，性能好
         */
        LoadingCache<Long, AtomicLong> counter =
                CacheBuilder.newBuilder()
                        .expireAfterWrite(2, TimeUnit.SECONDS)
                        .build(new CacheLoader<Long, AtomicLong>() {
                            @Override
                            public AtomicLong load(Long seconds) throws Exception {
                                return new AtomicLong(0);
                            }
                        });
        long limit = 1000;
        while(true) {
            //得到当前秒
            long currentSeconds = System.currentTimeMillis() / 1000;
            System.out.println("currentSeconds==="+currentSeconds);
            if(counter.get(currentSeconds).incrementAndGet() > limit) {
                System.out.println("限流了:" + currentSeconds);
                System.out.println("稍等试试吧.....");
                System.out.println("counter.get(currentSeconds).get()==="+counter.get(currentSeconds).get());
                break;
            }else{
                System.out.println("当前值==="+counter.get(currentSeconds).get());
            }
            //业务处理
        }

    }
}
