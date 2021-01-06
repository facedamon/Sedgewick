package org.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singleton {

    /**
     * 懒汉式，线程不安全
     * 这种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程。
     * 因为没有加锁synchronized, 所以严格意义上它并不算单例模式。
     * 这种方式 lazy loading很明显，不要求线程安全，在多线程下不能正常工作
     */
    private static class Singleton1 {
        private static Singleton1 instance;

        private Singleton1() {
        }

        private static int k = 1;

        public static Singleton1 getInstance() {
            if (null == instance) {
                System.out.println("创建" + k++ + "次");
                instance = new Singleton1();
            }
            return instance;
        }
    }

    @Test
    public void test1() {
        List<Thread> list = new ArrayList<>(2048);

        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> Singleton1.getInstance(), "thread-"+i);
            thread.start();
            list.add(thread);
        }
        try {
            for (Thread thread : list) {
                thread.join();
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 懒汉式，线程安全
     * 这种方式具备很好的lazy loading, 能够在多线程中很好的工作，
     * 但是，效率底下，99%情况下不需要同步。
     * 优点：第一次调用才初始化，避免内存浪费。
     * 缺点：必须加锁才能保证单例，但加锁会影响效率。加锁其实只需要在第一次初始化的时候用到，之后的调用都没必要再加锁。
     */
    private static class Singleton2 {
        private static Singleton2 instance;

        private Singleton2() {
        }

        private static int k = 1;

        public static synchronized Singleton2 getInstance() {
            if (null == instance) {
                System.out.println("创建" + k++ + "次");
                instance = new Singleton2();
            }
            return instance;
        }
    }

    @Test
    public void test2() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    Singleton2.getInstance();
                }
            });
        }
        pool.shutdown();
        while (!pool.isTerminated());
    }

    /**
     * 恶汉式
     * 这种方式比较常用，但很容易产生垃圾。
     * 优点：没有加锁，执行效率高。
     * 缺点：类加载时就初始化，浪费内存。
     * 它基于classloader机制避免了多线程的同步问题，
     * 不过，instance在类装载时就实例化，虽然导致类装载的原因有很多种，
     * 在单例模式中大多数都是调用getInstance方法，但是也不能确定有其它的方式导致类装载，
     * 这时候初始化instance显然没有达到lazy loading的效果。
     */
    private static class Singleton3 {
        private static Singleton3 instance = new Singleton3();
        private Singleton3(){}
        public static Singleton3 getInstance() {
            System.out.println(instance.hashCode());
            return instance;
        }
    }

    @Test
    public void test3() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    Singleton3.getInstance();
                }
            });
        }
        pool.shutdown();
        while (!pool.isTerminated());
    }

    /**
     * 双重检验锁
     * 新判断对象是否已经被初始化，再决定要不要加锁。
     * 执行双重检查是因为，如果多个线程同时通过了第一次检查，
     * 并且其中一个线程首先通过了第二次检查并实例化了对象，
     * 那么剩余通过了第一次检查的线程就不会再去实例化对象。
     * 这样，除了初始化的时候会出现加锁的情况，后续的所有调用都会避免加锁而直接返回，解决了性能消耗的问题。
     */
    private static class Singleton4 {
        //有些编译器为了性能，会将第二步和第三步进行重排序。
        //使用volatile关键字后，重排序被禁止，所有的写操作都将发生在读操作之前。
        private volatile static Singleton4 instance;
        private Singleton4(){}
        public static Singleton4 getInstance() {
            if (null == instance) {
                synchronized (Singleton4.class) {
                    if (null == instance) {
                        instance = new Singleton4();
                    }
                }
            }
            System.out.println(instance.hashCode());
            return instance;
        }
    }

    @Test
    public void test4() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    Singleton4.getInstance();
                }
            });
        }
        pool.shutdown();
        while (!pool.isTerminated());
    }

    /**
     * 静态内部类
     * 线程安全，懒加载。
     * 这种方式能达到双检锁方式一样的功效，但实现更简单。
     * 对静态域使用延迟初始化，应使用这种方式而不是双检锁方式。
     * 这种方式只适用于静态域的情况，双检锁方式可在实例域需要延迟初始化时使用。
     *
     * 这种方式同样利用了classloader机制来保证初始化instance时只有一个线程，
     * 它跟懒汉式不同的是：懒汉式只要Singleton类被装载了，那么instance就会被实例化(没有达到lazy loading效果)，
     * 而这种方式是Singleton类被装载，instance不一定被初始化。
     * 因为SingletonHolder类没有被使用，只有通过显示调用getInstance方法时，
     * 才会显示装载SingletonHolder类，从而实例化instance。
     */
    private static class Singleton5 {
        private static class Singleton5Holder {
            private static final Singleton5 INSTANCE = new Singleton5();
        }
        private Singleton5(){}
        public static final Singleton5 getInstance() {
            System.out.println(Singleton5Holder.INSTANCE.hashCode());
            return Singleton5Holder.INSTANCE;
        }
    }

    @Test
    public void test5() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    Singleton5.getInstance();
                }
            });
        }
        pool.shutdown();
        while (!pool.isTerminated());
    }

    /**
     * 这种方式是实现单例模式的最佳方法。简洁，自动支持序列化，绝对防止多次实例化。
     */
    private static enum Singleton6 {
        INSTANCE;
        public void whateverMethod() {}
    }

    @Test
    public void test6() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Singleton6.INSTANCE.hashCode());
                }
            });
        }
        pool.shutdown();
        while (!pool.isTerminated());
    }

}
