package practice1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestTest {
    private static final int SIZE = 500;
    private static Integer cnt = 0;
    List<Integer> asyncList = new ArrayList<>();
    List<Integer> mSyncList = new ArrayList<>();
    final List<Integer> syncList = Collections.synchronizedList(mSyncList);
    Executor executor = Executors.newFixedThreadPool(5);
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


    @Test
    public void testAsyncList() {
        for (int i = 0; i < SIZE; ++i) {
            Integer finalI = i;
            executor.execute(() -> asyncList.add(finalI));
        }
        Assertions.assertIterableEquals(asyncList, answer);
    }

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
        Assertions.assertIterableEquals(syncList, answer);
    }

}

