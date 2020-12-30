package org.example;

import java.io.*;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hello world!
 *
 */
public class TokenBucket {
    // 默认桶容量 即最大瞬时流量是64MB
    // 1024B = 1KB
    // 1MB = 1024KB = 1KB * 1024 = 1024B * 1024
    // 64MB = 64 * 1MB
    private static final int DEFAULT_BUCKET_SIZE = 1024 * 1024 * 64;

    // 每个桶数据的单位是1字节 1Byte
    private int everyTokenSize = 1;

    // 瞬时最大流量
    private int maxFlowRate;

    // 平均流量
    private int avgFlowRate;

    // 使用队列来装载桶数据
    // 最大的流量峰值就是 = everyTokenSize * DEFAULT_BUCKET_SIZE
    private ArrayBlockingQueue<Byte> tokenQueue =
            new ArrayBlockingQueue<>(DEFAULT_BUCKET_SIZE);

    private ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();
    private volatile boolean isStart = false;
    private ReentrantLock lock = new ReentrantLock(true);
    private static final byte A_CHAR = 'a';

    public TokenBucket(){}

    public TokenBucket(int maxFlowRate, int avgFlowRate) {
        this.maxFlowRate = maxFlowRate;
        this.avgFlowRate = avgFlowRate;
    }

    public TokenBucket(int everyTokenSize, int maxFlowRate, int avgFlowRate) {
        this.everyTokenSize = everyTokenSize;
        this.maxFlowRate = maxFlowRate;
        this.avgFlowRate = avgFlowRate;
    }

    public void addToken(Integer tokenNum) {
        // 若队列(桶)已经满了，就不再入新的令牌
        for (int i = 0; i < tokenNum; i++) {
            tokenQueue.offer(Byte.valueOf(A_CHAR));
        }
    }

    // 获取令牌个数
    public boolean getTokens(byte[] dataSize) {
        // 传输率 = 传输内容大小对应的桶个数
        int needTokenNum = dataSize.length / everyTokenSize + 1;
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            // 是否存在足够的桶
            boolean result = needTokenNum <= tokenQueue.size();
            if (!result) {
                return false;
            }
            int tokenCount = 0;
            for (int i = 0; i < needTokenNum; i++) {
                Byte poll = tokenQueue.poll();
                if (poll != null) {
                    tokenCount++;
                }
            }
            return tokenCount == needTokenNum;
        }finally {
            lock.unlock();
        }
    }

    public TokenBucket build() {
        start();
        return this;
    }

    public static TokenBucket newBuilder() {
        return new TokenBucket();
    }

    public TokenBucket everyTokenSize(int everyTokenSize) {
        this.everyTokenSize = everyTokenSize;
        return this;
    }

    public TokenBucket maxFlowRate(int maxFlowRate) {
        this.maxFlowRate = maxFlowRate;
        return this;
    }

    public TokenBucket avgFlowRate(int avgFlowRate) {
        this.avgFlowRate = avgFlowRate;
        return this;
    }

    private String stringCopy(String data, int copyNum) {
        StringBuilder sbuilder = new StringBuilder(data.length() * copyNum);
        for (int i = 0; i < copyNum; i++) {
            sbuilder.append(data);
        }
        return sbuilder.toString();
    }

    public void start() {
        // 初始化桶队列大小
        if (maxFlowRate != 0) {
            tokenQueue = new ArrayBlockingQueue<Byte>(maxFlowRate);
        }
        // 初始化令牌生产者
        TokenProducer tokenProducer = new TokenProducer(avgFlowRate, this);
        scheduledExecutorService.scheduleAtFixedRate(tokenProducer, 0, 1, TimeUnit.SECONDS);
        isStart = true;
    }

    public void stop() {
        isStart = false;
        scheduledExecutorService.shutdown();
    }

    public boolean isStart() {
        return isStart;
    }

    // 令牌生产者线程
    class TokenProducer implements Runnable {
        private int avgFlowRate;
        private TokenBucket tokenBucket;

        public TokenProducer(int avgFlowRate, TokenBucket tokenBucket) {
            this.avgFlowRate = avgFlowRate;
            this.tokenBucket = tokenBucket;
        }

        @Override
        public void run() {
            tokenBucket.addToken(avgFlowRate);
        }
    }

    private static void tokenTest() throws IOException, InterruptedException {
        TokenBucket tokenBucket = TokenBucket.newBuilder().avgFlowRate(512).maxFlowRate(1024).build();
        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("/Users/facedamon/Desktop/t.txt"))
        );
        // 四个byte
        String data = "xxxx";
        // 模拟1000次请求
        // 每次请求获取i1字节个数据
        for (int i = 1; i <= 1000; i++) {
            Random random = new Random();
            int i1 = random.nextInt(100);
            boolean tokens = tokenBucket.getTokens(tokenBucket.stringCopy(data, i1).getBytes());
            TimeUnit.MILLISECONDS.sleep(100);
            if (tokens) {
                bufferedWriter.write("token pass --- index:" + i1);
            } else {
                bufferedWriter.write("token rejuect --- index:" + i1);
            }
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        bufferedWriter.close();
        tokenBucket.stop();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        tokenTest();
    }
}
