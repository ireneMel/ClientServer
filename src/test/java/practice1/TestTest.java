package practice1;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestTest {
    private static final int SIZE = 5000;
    private static Integer cnt = 0;
    List<Integer> asyncList = new ArrayList<>();
    List<Integer> mSyncList = new ArrayList<>();
    final List<Integer> syncList = Collections.synchronizedList(mSyncList);
    ExecutorService executor = Executors.newFixedThreadPool(5);
    private static List<Integer> answer = new ArrayList<>(SIZE);

    @BeforeAll
    public static void answerList() {
        for (int i = 0; i < SIZE; ++i) {
            answer.add(i);
        }
    }

    @BeforeEach
    public void clearCnt() {
        cnt = 0;
    }

    private final static List<Integer> lst = Collections.synchronizedList(new LinkedList<>());

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < SIZE / 10; ++i) {
            executor.execute(() -> {
                synchronized (lst) {
                    for (int j = 0; j < 10; ++j) {
                        lst.add((int) Thread.currentThread().getId());
                    }
                }
            });
        }
        synchronized (lst) {
            for (int i : lst) {
                System.out.print(", " + i);
            }
        }
        System.exit(0);
    }

    @SneakyThrows
    @Test
    public void testAsyncList() {
        for (int i = 0; i < SIZE / 10; ++i) {
            Integer finalI = i;
            executor.execute(() -> asyncList.add(finalI));
        }
        Assertions.assertIterableEquals(asyncList, answer);
    }

    @SneakyThrows
    @Test
    public void testSyncList() {
        for (int i = 0; i < SIZE; ++i) {
            Integer finalI = i;
            executor.execute(new Runnable() {
                @Override
                public synchronized void run() {
                    syncList.add(finalI);
                }
            });
        }
        executor.awaitTermination(3, TimeUnit.SECONDS);
        executor.shutdown();
        Collections.sort(syncList);
        Assertions.assertIterableEquals(syncList, answer);
    }
}

