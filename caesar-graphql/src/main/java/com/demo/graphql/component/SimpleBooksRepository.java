package com.demo.graphql.component;

import com.demo.graphql.entity.Books;
import com.demo.graphql.repository.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * For the purpose of this guide, you will simply use a naive implementation that simulates some latency
 * (network service, slow delay, or other issues).
 * You could have used SQl or NoSQl stores.
 */
@Component
public class SimpleBooksRepository implements BookRepository {

    /**
     * 开启缓存
     * @param isbn 参数
     * @return 书
     */
    @Override
    @Cacheable("books")
    public Books getByIsbn(String isbn) {
        simulateSlowService(); // native delay.
        return new Books(isbn, "Some book. ");
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
