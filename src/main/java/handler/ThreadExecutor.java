package handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor {
    private final ThreadPoolExecutor threadPoolExecutor;

    public ThreadExecutor() {
        this.threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20)
        );
    }

    public void execute(Runnable task){
        threadPoolExecutor.execute(task);
    }
}
