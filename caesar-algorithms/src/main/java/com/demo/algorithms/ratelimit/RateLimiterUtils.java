package com.demo.algorithms.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * a simple demo for rateLimit using Singleton mode
 */
public enum RateLimiterUtils {

    TWO_LIMITER(RateLimiterUtils.rate_two),
    TEN_LIMITER(RateLimiterUtils.rate_ten),
    DEFAULT_LIMITER(RateLimiterUtils.rate_default);

    public static final int rate_two = 2;
    public static final int rate_ten = 10;
    public static final int rate_default = Integer.MAX_VALUE;

    public static final String two = "TWO";
    public static final String ten = "TEN";

    private RateLimiter rateLimiter;

    RateLimiterUtils(int rate) {
        rateLimiter = RateLimiter.create(rate);
    }

    public static RateLimiter getRateLimiter(String market) {
        switch (market) {
            case two:
                return TWO_LIMITER.rateLimiter;
            case ten:
                return TEN_LIMITER.rateLimiter;
            default:
                return DEFAULT_LIMITER.rateLimiter;
        }
    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiterUtils.getRateLimiter("TWO");
        while (true) {
            try {
                if (rateLimiter.tryAcquire()) {
                    System.out.println("getAcquire");
                    continue;
                }
                System.out.println("loseAcquire");
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
